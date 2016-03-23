package disjoint.state;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;

import soot.Value;

/**
 * This class represent  all interval sates
 * each instantiated by a disjoint domain
 * @author elenasherman
 *
 */

public class IntervalStates implements State{

	/* A string representation of this bit set. 
	 * For every index for which this BitSet contains 
	 * a bit in the set state, the decimal 
	 * representation of that index is included in the result.
	 */
	/*
	 * Maps a variable to the list of bitvectors, where each bitvector
	 * is the abstract value of the variable in particular domain
	 */
	private Map<Value,List<BitSet>> varToValue;

	//keeps the info regarding the domain size that is used for
	//correct state initialization
	static public List<Integer> bitsInDomain = new ArrayList<Integer>();

	public IntervalStates(){
		varToValue = new HashMap<Value,List<BitSet>>();
	}

	public IntervalStates(Map<Value, List<BitSet>> map){
		varToValue = new HashMap<Value,List<BitSet>>();
		for(Entry<Value, List<BitSet>> pair : map.entrySet()){
			List<BitSet> value = new ArrayList<BitSet>();
			value.addAll(copy(pair.getValue())); //make sure to make a deep copy for list of bitsets
			varToValue.put(pair.getKey(), value);
		}
	}

	@Override
	public State copy() {
		return new IntervalStates(varToValue);
	}

	@Override
	public String toString(){
		return varToValue.toString();
	}

	@Override
	public boolean equals(Object o ){
		boolean ret = true;
		if(o instanceof IntervalStates){
			IntervalStates other = (IntervalStates)o;
			//the same variable should have the same bitSets
			//maps should be the same
			ret = varToValue.equals(other.varToValue);
		} else {
			ret = false;
		}
		return ret;
	}

	@Override
	public void initFlowVar(Value var) {
		//add a list of bitsets 
		//where all bitsets are set to 0
		//1. need to know the size of the list
		//2. need to know the number of bits in each list
		List<BitSet> init = new ArrayList<BitSet>();
		for(Integer size : bitsInDomain){
			BitSet ret = new BitSet(size);
			init.add(ret);
		}
		varToValue.put(var, init);

	}

	@Override
	public void initEntryVar(Value var) {
		List<BitSet> init = new ArrayList<BitSet>();
		for(Integer size : bitsInDomain){
			BitSet ret = new BitSet(size);
			ret.set(0,size, true);
			init.add(ret);
		}
		varToValue.put(var, init);

	}

	@Override
	public State merge(State state) {
		IntervalStates ret = null;
		if(state instanceof IntervalStates){
			Map<Value,List<BitSet>> retVarToValue = new HashMap<Value,List<BitSet>>();
			IntervalStates other = (IntervalStates) state;
			for(Value var : varToValue.keySet()){
				List<BitSet> thisValues = varToValue.get(var);
				List<BitSet> newValues = copy(thisValues);
				List<BitSet> otherValues = other.getState(var);
				for(int i=0; i < newValues.size(); i++){
					BitSet otherValue = otherValues.get(i);
					BitSet newValue = newValues.get(i);
					//now do the actual merge
					newValue.or(otherValue);//moving up the lattice
				}
				retVarToValue.put(var, newValues);
			}
			ret = new IntervalStates(retVarToValue);
		} else {
			System.err.println("Merging interval state and " + state.getClass());
			System.exit(2);
		}
		return ret;
	}

	public List<BitSet> getState(Value var){
		return varToValue.get(var);
	}

	private List<BitSet> copy(List<BitSet> src){
		List<BitSet> ret = new ArrayList<BitSet>();
		for(BitSet bs : src){
			ret.add((BitSet)bs.clone());
		}
		return ret;
	}

	@Override
	public void initFlow() {
		//not used

	}

	@Override
	public void initEntry() {
		//not used

	}	

}
