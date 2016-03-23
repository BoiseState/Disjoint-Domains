package disjoint.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import disjoint.analysis.ValueAnalysis;

import soot.G;
import soot.Local;
import soot.Value;
import soot.util.Chain;

public class AbstractState {

	/*
	 * Contains a collection of different types states
	 * For now at least three:
	 * 1) Interval state
	 * 2) Relational state
	 * 3) Symbolic state
	 */

	boolean feasible; //becomes infeasible when at least of the states become infeasible

	List<State> states; //it should be ordered, so can do merges with other states in the expected order
	public static List<Local> localVars = new ArrayList<Local>(); //used for state initializations

	public AbstractState(){
		states = new ArrayList<State>();
		feasible = true;
	}

	public AbstractState(List<State> initStates){
		states = new ArrayList<State>();
		for(State s : initStates){
			states.add(s.copy());
		}
		feasible = true;
	}

	public boolean addState(State s){
		return states.add(s);
	}

	public AbstractState copy() {
		AbstractState copy = new AbstractState();
		for(State s : states){
			copy.addState(s.copy());
		}
		copy.feasible = feasible;
		return copy;
	}

	@Override
	public String toString(){
		return states.toString() + " " + isFeasible();
	}

	@Override
	public boolean equals(Object o){
		boolean ret = false;
		if(o instanceof AbstractState){
			AbstractState other = (AbstractState) o;
			if(other.isFeasible() == feasible){
				if(states.equals(other.getStates())){
					ret = true;
				}
			}
		}
		return ret;
	}

	public List<State> getStates(){
		return states;
	}


	public void setInitialFlow() {
		//divide the states that are
		//per variable 
		//and othe that are not
		for(State s : states){
			if(s instanceof SymbolicState){
				//not per varialbe
				s.initEntry();
			} else {
				//per variable
				for(Local l : localVars){
					s.initFlowVar(l);
				}
			}
		}
		feasible = false;
	}

	public void setEntryFlow() {
		for(Local l : localVars){
			for(State s : states){
				s.initEntryVar(l);
			}
		}

	}

	public static void setLocals(Chain<Local> locals) {
		//clear the previous locals
		localVars.clear();
		//now set new locals
		Iterator<Local> iter = locals.iterator();
		while(iter.hasNext()){
			Local l = iter.next();
			if(ValueAnalysis.isAnyIntType(l)){
				localVars.add(l);
			}
		}

	}

	public AbstractState merge(AbstractState in2) {
		List<State> newStates = new ArrayList<State>();
		for(int i = 0; i < states.size(); i++){
			State s = states.get(i).merge(in2.getStates().get(i));
			newStates.add(s);
		}
		AbstractState as = new AbstractState(newStates);
		return as;
	}

	//copy the value of the source state
	public void copy(AbstractState source) {
		AbstractState newState = source.copy();
		states = newState.states;
		feasible = source.isFeasible();
	}

	public void setInfeasible(){
		feasible = false;
	}

	public boolean isFeasible(){
		return feasible;
	}

}
