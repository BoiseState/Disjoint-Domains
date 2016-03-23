package disjoint.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
/* Symbolic state holds the set of soot 
 * assignment of conditional statements
 * that can be used to expressed
 * the current state symbolically
 */
import soot.jimple.internal.ImmediateBox;

public class SymbolicState implements State {
	//initialize to all statements because we need to do
	//the intersection on merges
	public static Set<Stmt> allStmt = new HashSet<Stmt>(); 
	//each state is a set of jimple statements
	private Set<Stmt> reachedStmt;
	//and the reached branches of conditional stmts
	//stmt itself will be used for merges and
	//usedVars dependencies 
	//while BinopExpr will be used for 
	//determining the branch of the cond stmt
	private Map<Stmt,BinopExpr> condToExpr;

	//jimple is not pure SSA, so when
	//a variables used in smt \in reachedStmt
	// has been re-assigned then
	//this stmt should be removed from reachedStmt
	//e.g., let's have reachedStmt = {i0 = i1 -1}
	//then if the new statement is i1 = 2*i0
	//then the update reachedStmt = {i1= 2*i0}
	//if the same vars appear in lhs and rhs
	//like i1 = i1 - 1 then such stmt
	//is not added to reachedStmt
	private Map<Value, Set<Stmt>> usedVars;

	public SymbolicState(){
		reachedStmt = new HashSet<Stmt>();
		usedVars = new HashMap<Value, Set<Stmt>>();
		condToExpr = new HashMap<Stmt, BinopExpr>();
	}

	/*
	 * Make sure to create a deep copy
	 * of stmts, since it is used up
	 * in the constructor
	 */
	public SymbolicState(Set<Stmt> stmts, Map<Value,Set<Stmt>> used, Map<Stmt,BinopExpr> cond){
		reachedStmt = stmts;
		usedVars = used;
		condToExpr = cond;
	}

	@Override
	public State copy() {
		Set<Stmt> newSet = new HashSet<Stmt>();
		newSet.addAll(reachedStmt);
		Map<Value,Set<Stmt>> newUsed = new HashMap<Value,Set<Stmt>>();
		for(Entry<Value, Set<Stmt>> es : usedVars.entrySet()){
			Set<Stmt> newStmtSet = new HashSet<Stmt>();
			newStmtSet.addAll(es.getValue());
			newUsed.put(es.getKey(), newStmtSet);
		}
		Map<Stmt,BinopExpr> newCond = new HashMap<Stmt, BinopExpr>();
		newCond.putAll(condToExpr);
		SymbolicState ret = new SymbolicState(newSet, newUsed, newCond);
		return ret;
	}

	// symbolic state is not per variable state,
	//so the next two methods have no effect.

	@Override
	public void initFlowVar(Value var) {

	}

	@Override
	public void initEntryVar(Value var) {

	}

	@Override
	public State merge(State state) {
		SymbolicState ret = null;
		if(state instanceof SymbolicState){
			SymbolicState other = (SymbolicState)state;
			Set<Stmt> newSet = new HashSet<Stmt>();
			newSet.addAll(reachedStmt);
			newSet.retainAll(other.getStaments()); // intersection of statements

			Map<Stmt,BinopExpr> newCond = new HashMap<Stmt, BinopExpr>();
			Set<Stmt> removeStmt = new HashSet<Stmt>();
			//go through condstmt that must be in both
			for(Stmt stmt : newSet){
				if(stmt instanceof IfStmt){
					//get the map values from both states
					BinopExpr thisExpr = condToExpr.get(stmt);
					BinopExpr otherExpr = other.getBinop(stmt);
					//if one of them is null
					//it means that its INFLOW to while loop
					//both of them cannot be null
					if(thisExpr == null && otherExpr == null){
						System.err.println("BinopExpr cannot be both null!");
						System.exit(2);
					}
					if(thisExpr == null){
						//override with other
						newCond.put(stmt, otherExpr);
					} else if (otherExpr == null){
						//override with this
						newCond.put(stmt, thisExpr);
					} else if (thisExpr.equals(otherExpr)){
						//add one of them
						newCond.put(stmt, thisExpr);
					} else {
						//they are not equals and not null
						//means merging flows from different
						//cond branches -- remove the stmt itself
						removeStmt.add(stmt);
						//should be only one at a time removed
						//since soot never merges several flows
						//at a time
					}

				}
			}//end iterating over cond stmts
			//remove condStmt of jointed flows
			newSet.removeAll(removeStmt);


			Map<Value, Set<Stmt>> newUsed = new HashMap<Value, Set<Stmt>>();
			for (Entry<Value, Set<Stmt>> es : usedVars.entrySet()) {
				// it can happen that other might not have
				// statements for that variable
				// if so we don't have to add this variable
				// to the newUsed map
				Set<Stmt> otherUsed = other.getUsed(es.getKey());
				if (otherUsed != null) {
					Set<Stmt> newStmtSet = new HashSet<Stmt>();
					newStmtSet.addAll(es.getValue());
					// remove those that for merged condStmt
					newStmtSet.removeAll(removeStmt);
					newStmtSet.retainAll(otherUsed);
					newUsed.put(es.getKey(), newStmtSet);
				}
			}

			ret = new SymbolicState(newSet, newUsed, newCond);
		}
		return ret;
	}

	public Set<Stmt> getStaments(){
		return reachedStmt;
	}

	public Set<Stmt> getUsed(Value var){
		return usedVars.get(var);
	}

	//however initFlow must initialize to all possible
	//value
	@Override
	public void initFlow() {
		reachedStmt.clear();
		reachedStmt.addAll(allStmt);
	}

	//initEntyrVar must be the empty set
	@Override
	public void initEntry() {
		reachedStmt.clear();
	}

	@Override
	public String toString(){
		String ret = "(";
		Object[] listStmt = reachedStmt.toArray();
		int size = listStmt.length;
		int i = 0;
		while(i < size -1){
			Object stmt = listStmt[i];
			ret +=stmt;
			if(stmt instanceof IfStmt){
				ret += "{" + condToExpr.get(stmt) + "}, ";
			} else {
				ret +=", ";
			}
			i++;
		}
		//last element if any
		if(size > 0){
			ret +=listStmt[i];
			if(listStmt[i] instanceof IfStmt){
				ret += "{" + condToExpr.get(listStmt[i]) + "}";
			}
		}
		ret += ")";
		return ret;
	}

	@Override
	public boolean equals(Object o){
		boolean ret = true;
		if(o instanceof SymbolicState){
			SymbolicState other = ((SymbolicState) o);
			ret = other.getStaments().equals(reachedStmt) &&
					other.getAllBinop().equals(condToExpr);
		} else {
			ret = false;
		}

		return ret;
	}

	public void removeLhsDepndencies(Value lhs){
		Set<Stmt> toRemove = new HashSet<Stmt>();
		//remove all statement where lhs has been used
		if(usedVars.containsKey(lhs)){
			toRemove.addAll(usedVars.get(lhs));
		}
		//determine whether exits reachedStmt of AssignStmt with the same lhs
		for(Stmt stmt : reachedStmt){
			if(stmt instanceof AssignStmt){
				Value lhsOld = ((AssignStmt) stmt).getLeftOp();
				if(lhs.equals(lhsOld)){
					//need to remove that statement
					toRemove.add(stmt);
					break;
				}
			}
			//conditional stmts are the case of use
		} // end for loop

		//remove the statement from the list
		reachedStmt.removeAll(toRemove);

		//remove those statements from the key/value set of 
		//usedVars to save memory
		for(Set<Stmt> es : usedVars.values()){
			es.removeAll(toRemove);
		}

		//remove from the condToExpr map -- to keep it clean
		for(Stmt rm : toRemove){
			if(rm instanceof IfStmt){
				condToExpr.remove(rm);
			}
		}
	}
	//inState is "old" state
	public void add(AssignStmt newStmt) {

		//get lhs of s
		Value lhs = newStmt.getLeftOp();
		removeLhsDepndencies(lhs);

		//remove lhsOld values from the map of the disjoint values

		boolean toAdd = true;
		//check whether newStmt is of the form i0 = i0 + 1
		for(Object box : newStmt.getRightOp().getUseBoxes()){
			if(((ImmediateBox)box).getValue().equals(lhs)){
				toAdd = false;
				break;
			}
		}

		if(toAdd){
			//adding newStmt to the list of reachedStmt
			reachedStmt.add(newStmt);

			//for an assignment stmt it's always rhs
			List<ValueBox> used = newStmt.getRightOp().getUseBoxes();
			addUsedVars(used, newStmt);
		}
	}

	//Adding conditional statement to the symbolic state
	//there is no removal to be made
	public void add(IfStmt newStmt, BinopExpr expr) {
		reachedStmt.add(newStmt);
		//get Values from rhs and lhs
		List<ValueBox> used = newStmt.getCondition().getUseBoxes();
		addUsedVars(used, newStmt);
		//add to map 
		condToExpr.put(newStmt, expr);

	}

	private void addUsedVars(List<ValueBox> vars, Stmt add){
		for(ValueBox box : vars){
			Value val = box.getValue();
			if(val instanceof Local){
				Set<Stmt> setUpdate = null;
				if(usedVars.containsKey(val)){
					setUpdate = usedVars.get(val);
				} else {
					setUpdate = new HashSet<Stmt>();
				}
				setUpdate.add(add);
				usedVars.put(val, setUpdate);
			}
		}
	}

	public BinopExpr getBinop(Stmt s){
		return condToExpr.get(s);
	}

	public Map<Stmt,BinopExpr> getAllBinop(){
		return condToExpr;
	}

}
