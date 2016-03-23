package disjoint.domain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Value;
import soot.grimp.internal.GAndExpr;
import soot.grimp.internal.GOrExpr;
import soot.jimple.BinopExpr;

/**
 * Domain which is describe
 * by its base element from 
 * which one can build a lattice
 * by conjoining them pairwise to get to bot
 * or disjoining them pairwise to get to top
 * @author elenasherman
 *
 */
public class Domain {

	/*
	 * The set of base elements that 
	 * define the domain, all other
	 * domain elements are the combination
	 * of these base elements.
	 */
	Set<BaseElement> element;

	/*
	 * A domain element is expressed as a bitvector
	 * if an index is set to 1 then the base element
	 * corresponding to that index is present. 
	 * This map stores such correspondence between
	 * base elements and their positions in the bitvector.
	 */
	Map<BaseElement, Integer> elementToIndex;
	/*
	 * The reverse map
	 */
	Map<Integer, BaseElement> indexToElement;

	/*
	 * Not sure if ever need it
	 * This map establishes the correspondence between
	 * domain elements and its integer value
	 */
	Map<BitSet, Integer> elementToInteger;

	/*
	 * for some domains like interval domains
	 * can have both disjoint can conjoint representations
	 * only used when creating a constraint
	 */
	boolean disjoint;

	public Domain(){
		element = new HashSet<BaseElement>();
		elementToIndex = new HashMap<BaseElement, Integer>();
		indexToElement = new HashMap<Integer, BaseElement>();
	}

	public boolean addBaseElement(BaseElement be){
		boolean added = false;
		//check to see if this be is not in the set
		if(!element.contains(be)){
			elementToIndex.put(be, element.size());
			indexToElement.put(element.size(), be);
			element.add(be);
			added = true;
		}

		return added;
	}

	public int size(){
		return element.size();
	}

	public Set<BaseElement> getElements(BitSet state){
		Set<BaseElement> ret = new HashSet<BaseElement>();
		for(int trueBit =  state.nextSetBit(0); trueBit >=0; 
				trueBit = state.nextSetBit(trueBit +1)){
			ret.add(indexToElement.get(trueBit));
		}

		return ret;
	}

	public BinopExpr instantiate(BitSet state, Value var){
		BinopExpr ret = null;
		Iterator<BaseElement> iter = getElements(state).iterator();
		//at least one base elements
		if(iter.hasNext()){
			ret = iter.next().instantiate(var);
			//if more than one - do either disjunction (default) or 
			//conjunction of negated base elements -- use DNotExpr, it is an expression so
			//but Binop takes Value, so it should work
			while(iter.hasNext()){
				if(disjoint){
					ret = new GOrExpr(ret, iter.next().instantiate(var));
				} else {
					//negated conjoint -- implement later
					//using for instantiating unstructured domain
					ret = new GAndExpr(ret, iter.next().instantiate(var));
				}
			}
		}
		return ret;
	}

	public Set<BaseElement> getBaseElements() {
		return element;
	}

	public BitSet getBitSet(Set<BaseElement> newSet) {
		BitSet ret = new BitSet();
		for(BaseElement e : newSet){
			int eIndex = elementToIndex.get(e);
			ret.set(eIndex);
		}
		return ret;
	}

	//	public BinopExpr abstractConsequence(BinopExpr lower, BinopExpr upper) {
	//		//should be more sophisticated implementation
	//		//1. converting lower in CNF
	//		//2. returning a single conjunction term
	//		return lower;
	//	}

	public BinopExpr getSatPredicates(List<Long> sol, Value lhs) {
		BinopExpr ret = null;
		for(BaseElement be : element){
			if(be.evaluate(sol.get(0))){
				BinopExpr current = be.instantiate(lhs);
				if(ret != null){
					ret = new GAndExpr(current,ret);
				} else {
					//first time around
					ret = current;
				}
			}
		}
		return ret;
	}

	public Set<BaseElement> getSatPredicates(List<Long> sol, Value ...vars){
		//vars can be used for relational predicates to know
		//which solution is associated with wihc predicate
		//at index 0 we assume the solution the empty spot
		Set<BaseElement> ret = new HashSet<BaseElement>();
		for(BaseElement be : element){
			if(be.evaluate(sol.get(0))){
				ret.add(be);
			}
		}
		return ret;
	}

	/*
	 * top is the disjunction of all predicates
	 */
	public BinopExpr top(Value lhs){
		BinopExpr ret = null;
		for(BaseElement be : element){
			BinopExpr current = be.instantiate(lhs);
			if(ret != null){
				ret = new GOrExpr(current, ret);
			} else {
				ret = current;
			}
		}
		return ret;
	}

	/*
	 * bot is the conjunction of all predicates
	 */

	public BinopExpr bot(Value lhs){
		BinopExpr ret = null;
		for(BaseElement be : element){
			BinopExpr current = be.instantiate(lhs);
			if(ret != null){
				ret = new GAndExpr(current, ret);
			} else {
				ret = current;
			}
		}
		return ret;
	}

	//interprets as a disjunction of all elements, which is just the set of elements
	public Set<Set<BaseElement>> topCNF() {
		Set<Set<BaseElement>> top = new HashSet<Set<BaseElement>>();
		top.add(element);
		return top;
	}

	public Set<Set<BaseElement>> topDNF() {
		Set<Set<BaseElement>> top = new HashSet<Set<BaseElement>>();
		for(BaseElement be : element){
			Set<BaseElement> newSet = new HashSet<BaseElement>();
			newSet.add(be);
			top.add(newSet);
		}
		return top;
	}

	public Set<BaseElement> abstractConsequence(Set<Set<BaseElement>> lowerCNF,
			Set<Set<BaseElement>> upperCNF) {
		Set<BaseElement> ret = new HashSet<BaseElement>();
		//returns such term of lowerCNF that
		//every term of the upper in CNF
		//cannot imply that lowerCNF term
		//i.e., the old set cannot be
		//contained in the new set of upper
		for(Set<BaseElement> termLower : lowerCNF){
			boolean add = true;
			for(Set<BaseElement> termUpper : upperCNF){
				if (termLower.containsAll(termUpper)){
					add = false;
					break;
				}
			}
			//if it does not contain any upperCNF terms
			//then return it.
			if(add){
				ret.addAll(termLower);
				break;
			}
		}
		return ret;
	}

	//as disjunctions
	public BinopExpr instantiate(Set<BaseElement> current, Value var) {
		BinopExpr ret = null;
		Iterator<BaseElement> iter = current.iterator();
		//at least one base elements
		if(iter.hasNext()){
			ret = iter.next().instantiate(var);
			while(iter.hasNext()){
				ret = new GOrExpr(ret, iter.next().instantiate(var));
			}
		}
		return ret;
	}

	//as disjunctions
	public BinopExpr instantiateCNFSingle(Set<BaseElement> current, Value var) {
		BinopExpr ret = null;
		Iterator<BaseElement> iter = current.iterator();
		//at least one base elements
		if(iter.hasNext()){
			ret = iter.next().instantiate(var);
			while(iter.hasNext()){
				ret = new GAndExpr(ret, iter.next().instantiate(var));
			}
		}
		return ret;
	}

	public BinopExpr instantiateCNF(Set<Set<BaseElement>> lowerCNF, Value lhs) {
		BinopExpr ret = null;
		for(Set<BaseElement> conj : lowerCNF){
			if(ret == null){
				ret = instantiate(conj,lhs);
			} else {
				ret = new GAndExpr(ret, instantiate(conj,lhs));
			}
		}
		return ret;
	}

	public Set<Set<BaseElement>> fromCNFtoDNF(Set<Set<BaseElement>> upper) {
		List<Set<BaseElement>> list = new ArrayList<Set<BaseElement>>();
		list.addAll(upper);
		return fromCNFtoDNF(list);
	}

	private Set<Set<BaseElement>> fromCNFtoDNF(List<Set<BaseElement>> from){
		Set<Set<BaseElement>> to = new HashSet<Set<BaseElement>>();;

		if(from.size() == 1){ // the base case
			for(BaseElement be : from.get(0)){
				Set<BaseElement> beSet = new HashSet<BaseElement>();
				beSet.add(be);
				to.add(beSet);
			}
		} else {
			Set<Set<BaseElement>> ret = fromCNFtoDNF(from.subList(1, from.size()));//[inclusive, exclusive)
			for(BaseElement be : from.get(0)){
				for(Set<BaseElement> retSet : ret){
					Set<BaseElement> beSet = new HashSet<BaseElement>();
					beSet.add(be);
					beSet.addAll(retSet);
					to.add(beSet);
				}
			}
		}
		return to;
	}

	public void setDisjoint(){
		disjoint = true;
	}

	public void setNotDisjoint(){
		disjoint = false;
	}

	@Override
	public String toString(){
		return element.size() + " : " + (disjoint?"disjoint":"notDisjoint") + " : " + element.toString();
	}

	public boolean isDisjoint(){
		return disjoint;
	}
}
