package disjoint.analysis;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import soot.ByteType;
import soot.IntType;
import soot.Local;
import soot.ShortType;
import soot.BooleanType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.grimp.internal.GAndExpr;
import soot.grimp.internal.GEqExpr;
import soot.grimp.internal.GOrExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.Expr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.LeExpr;
import soot.jimple.LtExpr;
import soot.jimple.NeExpr;
import soot.jimple.NumericConstant;
import soot.jimple.OrExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.Stmt;
import soot.jimple.XorExpr;
import soot.jimple.internal.JEqExpr;
import soot.jimple.internal.JGeExpr;
import soot.jimple.internal.JGtExpr;
import soot.jimple.internal.JLeExpr;
import soot.jimple.internal.JLtExpr;
import soot.jimple.internal.JNeExpr;
import soot.jimple.internal.JNegExpr;
import soot.jimple.internal.JUshrExpr;
import soot.jimple.internal.JimpleLocal;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;
import soot.util.Chain;

import disjoint.domain.BaseElement;
import disjoint.domain.Domain;
import disjoint.driver.StartAnalysis;
import disjoint.solver.SolverWrapper;
import disjoint.state.*;

/**
 * The core of the analysis and 
 * transfer function algorithms
 * @author elenasherman
 *
 */
public class ValueAnalysis extends ForwardBranchedFlowAnalysis<AbstractState> {

	//only write to the file states of those statements
	private Set<Unit> outputStmt;

	//in the current implementation would have
	//at most three states:
	//IntervalState, UnstructuredState and SymbolicState
	private List<State> states;

	/*
	 * Keeps track which domain is used for
	 * which state in the list of intervals states
	 */
	private int disjointDomainIndex;

	/*
	 * Maps a domain to its index in the list of bitvectors
	 * in varToValue map in disjoint.state.ItervalState.java
	 */
	private Map<Domain,Integer> disjointDomainToIndex;
	private Map<Integer, Domain> indexToDisjointDomain;

	/*
	 * Keeps track which domain is used for
	 * which state in the list of unstructured sates
	 */
	private int unstructuredDomainIndex;
	/*
	 * Maps an unstructured domain to its index in the 
	 * list of sets of bitvectors in disjoint.state.UnstructuredState.java
	 */
	private Map<Domain,Integer> unstructuredDomainToIndex;
	private Map<Integer, Domain> indexToUnstructuredDomain;
	private SolverWrapper solver;

	/* 
	 * use that to output values
	 * only of those variables
	 * that have been changed after the state
	 */
	private Map<Unit,Set<Value>> changedVariables;

	private boolean addIntervalDomain(Domain d){
		disjointDomainToIndex.put(d, disjointDomainIndex);
		indexToDisjointDomain.put(disjointDomainIndex, d);
		disjointDomainIndex++;
		return true;
	}

	private boolean addUnstructuredDomain(Domain d){
		unstructuredDomainToIndex.put(d, unstructuredDomainIndex);
		indexToUnstructuredDomain.put(unstructuredDomainIndex, d);
		unstructuredDomainIndex++;
		return true;
	}


	public ValueAnalysis(UnitGraph graph, List<Domain> setDomains, boolean symbolicOn) {
		super(graph);

		solver = StartAnalysis.getSolver();
		//initialize the type of states that will be used in the analysis
		states = new ArrayList<State>();
		IntervalStates iState = null;
		UnstructuredStates uState = null; 
		//instantiate the right state for each domain
		for(Domain domain : setDomains){
			if(domain.isDisjoint()){
				if(iState == null){
					//need to clear it for different method analysis
					//when multiple methods of the same class are analyzed
					//in the same run -- in case code is modified to do that
					IntervalStates.bitsInDomain.clear();
					//instantiate one and initialize indices
					iState = new IntervalStates();
					states.add(iState);
					disjointDomainToIndex = new HashMap<Domain, Integer>();
					indexToDisjointDomain = new HashMap<Integer, Domain>();
					disjointDomainIndex = 0;
				}
				//now add the domain itself
				addIntervalDomain(domain);
			} else {
				//domain is unstructured
				if(uState == null){
					//instantiate the unstructured state
					UnstructuredStates.bitsInDomain.clear();
					unstructuredDomainToIndex = new HashMap<Domain, Integer>();
					indexToUnstructuredDomain = new HashMap<Integer, Domain>();
					unstructuredDomainIndex = 0;
					uState = new UnstructuredStates();
					states.add(uState);
				}
				//add the domain
				addUnstructuredDomain(domain);
			}
		}

		//update the sizes of bitvectors in each domain
		for(int i=0; i< disjointDomainIndex; i++){
			int size = indexToDisjointDomain.get(i).size();
			//System.out.println("Size Disj" + size);
			IntervalStates.bitsInDomain.add(size);
		}

		//System.out.println("index " + unstructuredDomainIndex);
		for(int i = 0; i < unstructuredDomainIndex; i++){
			int size = indexToUnstructuredDomain.get(i).size();
			UnstructuredStates.bitsInDomain.add(size);
		}

		//----------- adding the one and only symbolic state
		if(symbolicOn){
			SymbolicState.allStmt = new HashSet<Stmt>();
			Iterator<Unit> iterUnit = graph.getBody().getUnits().iterator();
			while(iterUnit.hasNext()){
				Unit u = iterUnit.next();
				if(u instanceof Stmt){
					SymbolicState.allStmt.add((Stmt)u);
				}
			}
			SymbolicState ss = new SymbolicState();
			states.add(ss);
		}
		//initial and entry flows set up
		Chain<Local> locals = graph.getBody().getLocals();
		AbstractState.setLocals(locals);
		outputStmt = new HashSet<Unit>();
		changedVariables = new HashMap<Unit, Set<Value>>();

		//start the main analysis and time it
		long start = System.currentTimeMillis();
		doAnalysis(); //call it explicitly after setting up the domains
		long end = System.currentTimeMillis();
		//done with the analysis

		//reporting part
		String timeData = graph.getBody().getMethod().getDeclaringClass() + "\t" +graph.getBody().getMethod().getSignature()+
				"\t Done in "+ (end - start)+"\n";
		System.out.println(timeData);
		//printing the result
		Iterator<Unit> iter = graph.getBody().getUnits().iterator();
		int stmtCount = 0;
		while(iter.hasNext()){
			Unit u = iter.next();
			//check against statement after
			//which state has been changed
			stmtCount++;
			if(outputStmt.contains(u)){
				//String that keeps that state info for the current state
				String outputInfo = stmtCount + " " + u +":" + graph.getBody().getMethod().getSignature() + "\n";
				System.out.println(outputInfo);
				AbstractState fall = getFallFlowAfter(u);
				if(!fall.getStates().isEmpty() && fall.isFeasible()){
					for(Local l : locals){
						if(changedVariables.get(u).contains(l)){
							Set<BinopExpr> varPerState = evaluateStates(fall, l);
							Expr state = null;
							for(Expr be : varPerState){
								if(state == null){
									state = be;
								} else {
									state = new GAndExpr(state, be);
								}
							}
							System.out.println(l + "->" + solver.smt2((BinopExpr)state));
						}
					}
				}
				//for other branch outcome if one exists
				List<AbstractState> branches = getBranchFlowAfter(u);
				if(!branches.isEmpty()){
					for(AbstractState branch : branches){
						if(!branch.getStates().isEmpty() && branch.isFeasible()){
							for(Local l : locals){
								if(changedVariables.get(u).contains(l)){
									Set<BinopExpr> varPerState = evaluateStates(branch, l);
									Expr state = null;
									for(Expr be : varPerState){
										if(state == null){
											state = be;
										} else {
											state = new GAndExpr(state, be);
										}
									}
									//branched flow will be marked with "f" after the var name
									//while fall through will have just the var name
									System.out.println(l+"f" + "->" + solver.smt2((BinopExpr)state));
								}
							}
						}

					}
				}
			}//end outputStmt check
			System.out.flush();
		}
	}

	@Override
	protected void flowThrough(AbstractState in, Unit u, List<AbstractState> fallIn,
			List<AbstractState> branchOut) {
		Stmt s = (Stmt) u;
		AbstractState inState = in;
		AbstractState ifStmtTrue = inState.copy(); //instantiated in ifStmt only; outBranch
		AbstractState ifStmtFalse = inState.copy();//fallIn; out
		//only do the computation for a feasible inState
		//check only for integers?
		if(in.isFeasible()){
			if(s instanceof AssignStmt){
				processAssignStmt((AssignStmt)s,inState, ifStmtFalse);

			} else if (s instanceof IfStmt){
				processIfStmt((IfStmt) s, inState, ifStmtFalse, ifStmtTrue);
			} 
			//other statement modify nothing
		}


		for(Iterator<AbstractState> it = fallIn.iterator(); it.hasNext();){
			copy(ifStmtFalse, it.next());
		}

		for(Iterator<AbstractState> it = branchOut.iterator(); it.hasNext();){
			copy(ifStmtTrue, it.next());
		}

	}

	//coming from feasible to infeasible happens on conditional stmts only
	private void processIfStmt(IfStmt s, AbstractState inState,
			AbstractState ifStmtFalse, AbstractState ifStmtTrue) {
		ConditionExpr condExpr = (ConditionExpr)s.getCondition();
		Value lhs = condExpr.getOp1();
		Value rhs = condExpr.getOp2();
		//make sure this is an integer conditional stmt
		if(isAnyIntType(lhs)){
			//add it to the tracked states
			outputStmt.add(s);
			//create the set of variables to be tracked
			Set<Value> track = new HashSet<Value>();
			changedVariables.put(s, track);
			//precondition of the IfStmt
			Set<Expr> precond = new HashSet<Expr>();
			Set<Value> valuesToEval = new HashSet<Value>();//there should be one value only
			addNotNull(findLocal(lhs), valuesToEval);
			addNotNull(findLocal(rhs), valuesToEval);
			//need to make a special case when valuesToEval is empty
			//it means that both sides are concrete values
			//hence no need call for the solver
			for(Value v : valuesToEval){
				precond.addAll(evaluateStates(inState, v));
			}

			precond.addAll(evaluateStates(inState, null));//to evaluate symbolic state only
			//otherwise symbolic state can be evaluated twice -- equality of BinOp has not been implemented
			//it looks like in Jimple only statement of the same object are equal, but not
			//if they have the same semantics, assuming that locals are of the same object

			//do for true branch
			//add the current expression
			BinopExpr symbState = condExpr;
			BinopExpr symbNotState = negate(condExpr);
			for(Expr be : precond){
				symbState = new GAndExpr(symbState, be);
				symbNotState = new GAndExpr(symbNotState,be);
			}

			//at this point we have precondition set
			//make sure lhs is not a constant
			if(lhs instanceof JimpleLocal){
				//find new values for lhs
				updateStateCond(lhs,symbState, condExpr, ifStmtTrue, s);//s is only used for the symbolic state
				updateStateCond(lhs, symbNotState,negate(condExpr), ifStmtFalse, s);
				condExpr = null; //so no need to update the symbolic state twice
				track.add(lhs);
			}
			//make sure rhs is not a constant
			if(rhs instanceof JimpleLocal){
				updateStateCond(rhs, symbState, condExpr, ifStmtTrue, s);
				updateStateCond(rhs, symbNotState, negate(condExpr), ifStmtFalse,s );
				track.add(rhs);
			}
			//created the negated one
		} // end if this is an integer conditional stmt

	}
	private BinopExpr negate(ConditionExpr condExpr) {
		BinopExpr ret = null;
		if(condExpr != null){
			Value lhs = condExpr.getOp1();
			Value rhs = condExpr.getOp2();
			if(condExpr instanceof EqExpr){
				ret = new JNeExpr(lhs, rhs);
			} else if(condExpr instanceof NeExpr){
				ret = new JEqExpr(lhs, rhs);
			} else if(condExpr instanceof LeExpr){
				ret = new JGtExpr(lhs, rhs);
			} else if(condExpr instanceof GtExpr){
				ret = new JLeExpr(lhs, rhs);
			} else if(condExpr instanceof GeExpr){
				ret = new JLtExpr(lhs, rhs);
			} else if(condExpr instanceof LtExpr){
				ret = new JGeExpr(lhs, rhs);
			} 
		}
		return ret;
	}

	//the main difference between this method and updateStateAssign is the 
	//set of base elements to choose the solution from
	//in updateStateCond is v's value in precondition
	//while in updateStateAssign is from all possible values
	private void updateStateCond(Value v, BinopExpr symbState, BinopExpr expr,
			AbstractState outState, IfStmt stmt){

		List<State> states = outState.getStates();
		for(State state : states){
			if(state instanceof IntervalStates){
				//this is the current value of v in each interval domain
				List<BitSet> intervalVal = ((IntervalStates)state).getState(v);
				//now iterate for each state/domain
				for(int i = 0; i < intervalVal.size(); i++){
					BitSet value = intervalVal.get(i);
					Domain d = indexToDisjointDomain.get(i);
					//this is an optimization
					//instead of iterating through all possible values
					//we know that only subset of current values
					//will be propagated to true/false branch.
					Set<BaseElement> currentValSet = d.getElements(value);
					Set<BaseElement> newSet = transferDisjoint(symbState, v, currentValSet);
					//translate the set to bitvectors
					BitSet newValue = d.getBitSet(newSet);
					intervalVal.set(i, newValue);
					if(newValue.isEmpty()){
						//means the branch is infeasible
						outState.setInfeasible();
						//no need to finish the calculations for the rest of domains
						break;
					}
				}
			} else if (state instanceof UnstructuredStates){
				List<Set<BitSet>> unstructuredVal = ((UnstructuredStates) state).getState(v);
				for(int i=0; i < unstructuredVal.size(); i++){
					//do the same optimization by setting 
					//top to those elements that can occur
					Domain d = indexToUnstructuredDomain.get(i);
					Set<Set<BaseElement>> newVals = transferBileteral(symbState, v, d);
					Set<BitSet> retVals = new HashSet<BitSet>();
					//translate each inner set into the bitvector
					for(Set<BaseElement> newVal : newVals){
						BitSet newBitVal = d.getBitSet(newVal);
						retVals.add(newBitVal);
					}
					//update the state with the new value
					unstructuredVal.set(i, retVals);
					//detect infeasible state
					if(newVals.isEmpty()){
						outState.setInfeasible();
						//no need to finish loop
						//at least one domain
						//detected that branch is infeasible
						break;
					}
				}

			}else if (state instanceof SymbolicState){
				//we need to add both the conditional statment itself
				//and the binop expr
				if(expr != null){
					//stored as a grimp binop expression
					((SymbolicState) state).add(stmt, expr);
				}
			}
			if(!outState.isFeasible()){
				break;
			}
		}
	}

	private void processAssignStmt(AssignStmt s, AbstractState inState,
			AbstractState outState) {
		Value lhs = s.getLeftOp();
		//only process if lhs of the desired type, otherwise states
		//will be unchanged.
		Value rhs = s.getRightOp();
		if(isAnyIntType(lhs)){
			//output state for this statement
			outputStmt.add(s);
			//create the set of variables to be tracked
			Set<Value> track = new HashSet<Value>();
			changedVariables.put(s, track);
			track.add(lhs);
			//identify rhs
			Set<BinopExpr> precond = new HashSet<BinopExpr>();
			//create a temp local variable since in a loop
			//it is not SSA,e.g., i1 = i1+0
			JimpleLocal temp = new JimpleLocal("temp", lhs.getType());
			//change assignments into equality -- using grimp format to hold i0 = i + 1
			//because jimple does not allow rhs to be an expression
			EqExpr current = new GEqExpr(temp, rhs);
			Set<Value> valuesToEval = new HashSet<Value>();
			if(rhs instanceof JNegExpr){
				addNotNull(findLocal(((JNegExpr) rhs).getOp()), valuesToEval);
			} else if (rhs instanceof BinopExpr && !(rhs instanceof AndExpr) &&
					!(rhs instanceof XorExpr) && !(rhs instanceof OrExpr) && 
					!(rhs instanceof JUshrExpr)){
				//can only handle some non-linear operations
				//might be different for a different solvers
				//thus a good place for re-factoring the code
				//but it would make it more slow since we need
				//to go back and forth between encodings to
				//realize that something in the solver is not 
				//supported.
				//But there is a doubt that some solvers
				//have such support
				BinopExpr bexpr = (BinopExpr) rhs;
				Value exprLhs = bexpr.getOp1();
				Value exprRhs = bexpr.getOp2();
				if(rhs instanceof ShlExpr || rhs instanceof ShrExpr){
					//check weather esprRhs is not an constant
					//Z3 cannot handle those operation
					//perhaps should be outsourced to the solver
					if(!(exprRhs instanceof IntConstant)){
						//cannot handle it, update it to top
						updateStateTop(lhs, outState);
						return;
					}
				}
				addNotNull(findLocal(exprRhs),valuesToEval);
				addNotNull(findLocal(exprLhs),valuesToEval);
			} else if (rhs instanceof JimpleLocal || //definitely can change it
					rhs instanceof NumericConstant){
				addNotNull(findLocal(rhs),valuesToEval);
			} else {
				updateStateTop(lhs, outState);
				return;
			}


			//for each value get its representation in each state
			for(Value v : valuesToEval){
				precond.addAll(evaluateStates(inState, v));
			}

			precond.addAll(evaluateStates(inState, null));//to evaluate symbolic state only
			//otherwise symbolic state can be evaluated twice -- equality of BinOp has not been implemented
			//it looks like in Jimple only statement of the same object are equal, but not
			//if they have the same semantics, assuming that locals are of the same object

			//create one big formula
			BinopExpr symbState = current;
			for(BinopExpr pre : precond){
				symbState = new GAndExpr(pre,symbState);
			}

			//temp on what to do the calculations
			//lhs for what to do the updates
			updateStateAssign(temp,lhs, symbState, outState);

			//can update symbolic state right now
			for(State state : outState.getStates()){
				if(state instanceof SymbolicState){
					//should figure out it internally what to add and how to deal with i0 = i0 + 1
					((SymbolicState) state).add(s); 
				}
			}

		}

	}


	//sets lhs to top in all substates
	private void updateStateTop(Value lhs, AbstractState outState) {
		List<State> states = outState.getStates();
		for(State state : states){
			if(state instanceof SymbolicState){
				SymbolicState ss = (SymbolicState) state;
				//for symbolic state remove
				//all previous assignment to lhs
				//and stmt where lhs was used
				ss.removeLhsDepndencies(lhs);
			} else {
				state.initEntryVar(lhs);
			}
		}

	}

	private void updateStateAssign(Value temp, Value lhs, BinopExpr symbState,
			AbstractState outState) {
		//should be extracted into a sper
		List<State> states = outState.getStates();
		for(State state : states){
			if(state instanceof IntervalStates){
				List<BitSet> intervalVal = ((IntervalStates) state).getState(lhs);
				//translate bitset of each domain to its actual predicates
				for(int i = 0; i < intervalVal.size(); i++){
					Domain d = indexToDisjointDomain.get(i);
					Set<BaseElement> newSet = transferDisjoint(symbState, temp, d.getBaseElements());
					BitSet newValue = d.getBitSet(newSet);
					intervalVal.set(i, newValue);
				}
			} else if (state instanceof UnstructuredStates){
				List<Set<BitSet>> unstructVal = ((UnstructuredStates) state).getState(lhs);
				for(int i = 0; i < unstructVal.size(); i++){
					Domain d = indexToUnstructuredDomain.get(i);
					Set<Set<BaseElement>> newVals = transferBileteral(symbState, temp, d);
					Set<BitSet> retVals = new HashSet<BitSet>();
					//translate each inner set into the bitvector
					for(Set<BaseElement> newVal : newVals){
						BitSet newBitVal = d.getBitSet(newVal);
						retVals.add(newBitVal);
					}
					//update the state with the new value
					unstructVal.set(i, retVals);
				}
			}
		}
	}

	/*
	 * Bilateral algorithm of Reps at al.
	 */
	private Set<Set<BaseElement>> transferBileteral(BinopExpr symbState, Value lhs,
			Domain d){
		Set<Set<BaseElement>> lowerDNF = new HashSet<Set<BaseElement>>();//dnf
		Set<Set<BaseElement>> lowerCNF = new HashSet<Set<BaseElement>>();//cnf

		Set<Set<BaseElement>> upper = d.topCNF();//cnf

		//the first iteration just query symbSate and get the solutions and
		//update both lower sets
		List<Long> sol = solver.evaluateSol(symbState, null, lhs);
		if (sol == null){
			//return top, i.e., over approximation in dnf
			return d.topDNF();
		} else if (sol.isEmpty()){
			//return bot since no solution exists
			return lowerDNF;
		} else {
			//there is a solution
			//find a set of predicates satisfying it
			Set<BaseElement> satSet = d.getSatPredicates(sol, new Value[]{lhs});
			//add both of them to the lower
			//cnf each in its separate set
			for(BaseElement be : satSet){
				Set<BaseElement> set = new HashSet<BaseElement>();
				set.add(be);
				lowerCNF.add(set);
			}
			//dnf as a single set
			lowerDNF.add(satSet);

			//the main loop
			boolean equals = false;
			while(!equals){
				Set<BaseElement> current = d.abstractConsequence(lowerCNF, upper);
				//convert current into BoolExpr
				BinopExpr currentExpr = d.instantiate(current, lhs);
				//find the solution
				sol = solver.evaluateSol(symbState, currentExpr, lhs);
				if(sol == null){
					//return top in dnf
					System.out.println("Timing out, returning upper in DNF");
					lowerDNF = d.fromCNFtoDNF(upper);
					//remove unsat predicates?
					Set<Set<BaseElement>> toRemove = new HashSet<Set<BaseElement>>();
					for(Set<BaseElement> set : lowerDNF){
						BinopExpr evalSet = d.instantiateCNFSingle(set, lhs);
						if(!solver.evaluate(evalSet)){
							//unsat conjunction
							toRemove.add(set);
						}
					}
					//remove from the DNF
					lowerDNF.removeAll(toRemove);
					return lowerDNF;
				} else if (sol.isEmpty()){
					// update upper -- glb with current
					upper.add(current);
				} else {
					//there is a solution
					//find a set of predicates satisfying it
					satSet = d.getSatPredicates(sol, new Value[]{lhs});//why it was null before
					if(satSet.isEmpty()){
						//something wrong, perhaps incomplete domain
						System.out.println("For non-empty solution found no predicates!");
						System.exit(2);
					}
					//System.out.println("satSet " + satSet);
					//add both of them to the lower
					//cnf double loop
					//Set<Set<BaseElement>> toRemove = new HashSet<Set<BaseElement>>();

					Set<Set<BaseElement>> lowerCNFNew = new HashSet<Set<BaseElement>>();
					//the algorithm for converting two disjunctions of conjuncts
					//into CNF, i.e., adding to each old disjunction a new
					//predicate from satSet for each each satSet predicate
					//if lowerCNF = P1 and P2, and satSet = {p3,p4},
					// where P1, P2 are disjuncts then
					// lowerCNF or (p3 and p4) becomes
					//(P1 or p3) and (P1 or p4) and (P2 or p3) and (P2 or p4)
					for(Set<BaseElement> cnfSet : lowerCNF){
						for(BaseElement be : satSet){
							Set<BaseElement> set = new HashSet<BaseElement>();
							set.add(be);
							set.addAll(cnfSet);
							lowerCNFNew.add(set);
						}
					}
					lowerCNF.clear();
					lowerCNF.addAll(lowerCNFNew);


					//dnf as a single set
					lowerDNF.add(satSet);
				}
				//check if upper and lowerCNF are equivalent using
				//the sat solver? or actual set comparison?
				//upper has the dafault top value, not
				//sure how to determine the equivalence,
				//so use the solver for now;
				BinopExpr lowerCNFbinop = d.instantiateCNF(lowerCNF, lhs);
				BinopExpr upperBinop = d.instantiateCNF(upper, lhs);
				equals = solver.equals(lowerCNFbinop, upperBinop);
			}
		}		
		return lowerDNF;
	}

	//yep just couple lines of code comparing Bilateral :)
	private Set<BaseElement> transferDisjoint(BinopExpr symbState,
			Value lhs, Set<BaseElement> values) {
		Set<BaseElement> ret = new HashSet<BaseElement>();
		for(BaseElement be : values){
			//evaluate the whole formula
			BinopExpr newSymbState = new GAndExpr(symbState, be.instantiate(lhs));
			boolean sat = solver.evaluate(newSymbState);
			if(sat){
				ret.add(be);
			}
		}
		return ret;
	}

	private void addNotNull(Value v, Set<Value> values){
		if(v != null){//null in case it is a constant
			values.add(v);
		}
	}

	private Value findLocal(Value expr){
		Value ret = null;
		if(expr instanceof JimpleLocal){
			ret = expr;
		}
		return ret;
	}

	private Set<BinopExpr> evaluateStates(AbstractState inState, Value v){
		Set<BinopExpr> ret = new HashSet<BinopExpr>();
		//should be extracted into a sper
		List<State> states = inState.getStates();
		for(State state : states){
			if(state instanceof IntervalStates && v != null){
				List<BitSet> intervalVal = ((IntervalStates) state).getState(v);
				//translate bitset of each domain to its actual predicates
				for(int i = 0; i < intervalVal.size(); i++){
					BitSet value = intervalVal.get(i);
					Domain d = indexToDisjointDomain.get(i);
					BinopExpr dExpr = d.instantiate(value, v);
					ret.add(dExpr);
				}
			} else if (state instanceof UnstructuredStates & v != null){
				List<Set<BitSet>> unstructuredVal = ((UnstructuredStates) state).getState(v);
				//for each domain
				for(int i = 0; i < unstructuredVal.size(); i++){
					Set<BitSet> value = unstructuredVal.get(i);
					BinopExpr dExpr = null;
					//for each set of bitvectors -- a conjunction
					for(BitSet bs : value){
						Domain d = indexToUnstructuredDomain.get(i);
						if(dExpr != null){
							//second iteration
							dExpr = new GOrExpr(dExpr, d.instantiate(bs, v));//disjunction of conjunction
						} else { //first iteration
							dExpr = d.instantiate(bs, v);//must return conjunction
						}
					}
					if(dExpr == null){
						UnstructuredStates ustate = (UnstructuredStates) state;
						for(Entry<Value, List<Set<BitSet>>> es : ustate.varToValue.entrySet()){
							JimpleLocal jl = (JimpleLocal)es.getKey();
						}
						//something is wrong
						System.err.println("Domain value is null");
						System.out.flush();
						System.exit(2);
					}
					ret.add(dExpr);
				} 
			} else if (state instanceof SymbolicState & v == null){
				SymbolicState symbState = (SymbolicState) state;
				//iterate over stmt
				//if it cond stmt, get its binopExpr
				//otherwise convert assign stmt to the equality binopExpr
				for(Stmt stmt : symbState.getStaments()){
					//TODO: slice on variable v?
					if(stmt instanceof IfStmt){
						ret.add(symbState.getBinop(stmt));
					} else {
						AssignStmt assignStmt = (AssignStmt)stmt; //it's either an assignment or conditional stmt
						BinopExpr newExpr = new GEqExpr(assignStmt.getLeftOp(),assignStmt.getRightOp());
						ret.add(newExpr);
					}
				}
			}
		}
		return ret;
	}

	@Override
	protected void merge(AbstractState in1, AbstractState in2, AbstractState out) {
		//System.out.println("Merging " + in1 + " " + in1.isFeasible() + " with " + in2 + " " + in2.isFeasible());
		if(in1.isFeasible() && in2.isFeasible()){
			out.copy(in1.merge(in2)); //regular
		} else if (!in2.isFeasible()){
			copy(in1,out); //if in2 is infeasible then copy in1 to out, does not matter if in1 is feasible or not
		} else if (!in1.isFeasible()){
			copy(in2,out); //if in2 is feasible but in1 is infeasible then copy in2
		}
	}

	@Override
	protected void copy(AbstractState source, AbstractState dest) {
		dest.copy(source);
	}

	@Override
	protected AbstractState newInitialFlow() {
		AbstractState as = new AbstractState(states);
		as.setInitialFlow();
		return as;
	}

	@Override
	protected AbstractState entryInitialFlow() {
		AbstractState as = new AbstractState(states);
		as.setEntryFlow();
		return as;
	}

	public static boolean isAnyIntType(Value val){
		Type t = val.getType();
		return !(val instanceof ArrayRef) && !(val instanceof InstanceFieldRef)&&(t instanceof IntType || t instanceof ByteType || t instanceof ShortType
				|| t instanceof BooleanType);
	}

}
