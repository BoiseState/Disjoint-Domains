package disjoint.state;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import soot.Value;
/**
 * This class contains set of predicates
 * which don't have any structure, i.e.,
 * arbitrary set of predicates, unlike
 * disjoint state, where predicates
 * are disjoint
 * @author elenasherman
 *
 */

public class UnstructuredStates implements State {

	//main data structure is the map 
	// of a variable to a List of
	// Sets of bitvectors
	public Map<Value,List<Set<BitSet>>> varToValue;
	//in disjoint domain it is just a bitset
	//since we know ther will be a formula
	//p1 or p2 ... or pn
	//while for unstructured domain
	//it will be in a form
	//(p1 and p2) or (p3 and p4) or .. or (pk and ... pn)
	//it will be translated into DNF
	//DNF will allow for easier state merges
	//BitSet will set bit value to 1 if the predicate
	//is present in the conjunction

	//keeps track of number of bits in each Unstructured domain
	//should be set up by the domain manager
	static public List<Integer> bitsInDomain = new ArrayList<Integer>();

	public UnstructuredStates(){
		varToValue = new HashMap<Value, List<Set<BitSet>>>();
	}

	public UnstructuredStates(Map<Value, List<Set<BitSet>>> map){
		varToValue = new HashMap<Value, List<Set<BitSet>>>();
		for(Entry<Value, List<Set<BitSet>>> pair : map.entrySet()){
			List<Set<BitSet>> copyList = new ArrayList<Set<BitSet>>();
			for(Set<BitSet> set : pair.getValue()){
				//make a deep copy of set with the set
				Set<BitSet> copySet = copy(set);
				//add the set of bits to the list
				copyList.add(copySet);
			}//end for sets of bits
			//add the list to the map
			varToValue.put(pair.getKey(), copyList);
		}//end for vars
	}

	private Set<BitSet> copy(Set<BitSet> setOfBs){
		Set<BitSet> copySet = new HashSet<BitSet>();
		//make deep copy of the bitset
		for(BitSet bits : setOfBs){
			copySet.add((BitSet)bits.clone());
		}
		return copySet;
	}

	@Override
	public State copy() {
		// TODO Auto-generated method stub
		return new UnstructuredStates(varToValue);
	}

	@Override
	public String toString(){
		return varToValue.toString() + " ";
	}

	@Override
	public void initFlowVar(Value var) {
		//all initial flows are set to bot
		//where bot is a single bv with all
		//bits set to 0
		List<Set<BitSet>> init = new ArrayList<Set<BitSet>>();
		for(Integer size : bitsInDomain){
			Set<BitSet> initSet = new HashSet<BitSet>();
			//add only one bit of size
			BitSet newBits = new BitSet(size);
			//conjunction of all predicate 
			//should be unsat since
			// it would have p and !p in it
			initSet.add(newBits);
			init.add(initSet);
		}
		//add new list to the map
		varToValue.put(var, init);
	}

	@Override
	public void initEntryVar(Value var) {
		//will have the number of sets 
		//equal to the number of bitsInDomain
		//each set will have a single unique
		//bit set to 1
		List<Set<BitSet>> init = new ArrayList<Set<BitSet>>();
		for(Integer size : bitsInDomain){
			Set<BitSet> newSet = new HashSet<BitSet>();
			for(int i = 0; i < size; i++){
				//create BitSet with one 
				//unique bit set to 1
				BitSet newBits = new BitSet(size);
				newBits.set(i);
				//add to the set
				newSet.add(newBits);
			}
			//add set to the list
			init.add(newSet);
		}
		//add the list to the var to val map
		varToValue.put(var, init);

	}

	//The next to method should not be called
	@Override
	public void initFlow() {
		// not used

	}

	@Override
	public void initEntry() {
		// not used

	}

	//on merge unions the set of 
	//bitvectors for each domain
	//sometimes for the simplification
	//we can require call to a solver
	@Override
	public State merge(State state) {
		UnstructuredStates ret = null;
		if(state instanceof UnstructuredStates){
			UnstructuredStates other = (UnstructuredStates) state;
			Map<Value, List<Set<BitSet>>> mergedVarToValue = new HashMap<Value, List<Set<BitSet>>>();
			for(Value var: varToValue.keySet()){
				List<Set<BitSet>> thisValues = varToValue.get(var);
				List<Set<BitSet>> otherValues = other.getState(var);
				List<Set<BitSet>> mergedValues = new ArrayList<Set<BitSet>>();
				for(int i=0; i < thisValues.size(); i++){
					//for each list
					Set<BitSet> thisSet = thisValues.get(i);
					Set<BitSet> otherSet = otherValues.get(i);

					//union both of them but make sure o have
					//a deep copy of the bit set -- not sure 
					//if we need it since does not look like
					//bit set ever changes ..
					Set<BitSet> newSet = copy(thisSet);
					newSet.addAll(copy(otherSet));

					//------- here
					//we might want to call to solver
					//for simplification
					// e.g., (01) or (10) or anything else (11)
					//should be simplified to {(01),(10)}, i.e., top
					//will add in the same order
					mergedValues.add(newSet);
				}
				mergedVarToValue.put(var, mergedValues);
				ret = new UnstructuredStates(mergedVarToValue);
			}

		} else {
			System.err.println("Merging interval state and " + state.getClass());
			System.exit(2);
		}
		//System.out.println("Ret " + ret);
		return ret; 
	}

	public List<Set<BitSet>> getState(Value var){
		return varToValue.get(var);
	}


	@Override
	public boolean equals(Object o){
		boolean ret = false;
		if(o instanceof UnstructuredStates){
			UnstructuredStates other = (UnstructuredStates)o;
			//maps should be the same
			ret = varToValue.equals(other.varToValue);
		}
		return ret;
	}



}
