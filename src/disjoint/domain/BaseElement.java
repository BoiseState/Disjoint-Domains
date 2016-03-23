package disjoint.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import soot.Value;
import soot.grimp.internal.GAndExpr;
import soot.jimple.BinopExpr;

/**
 * A base element can be 
 * describe by a set of predicates
 * like x < 2 and x >= 0: these 
 * two predicates describe a 
 * base element [0,2)
 * @author elenasherman
 *
 */
public class BaseElement {

	/*
	 * Contains the set of the appropriate predicates that
	 * describe that element
	 */
	Set<Predicate> pred;

	public BaseElement(){
		pred = new HashSet<Predicate>();
	}

	/*
	 * Each instance should instantiate its own
	 * predicate addition from the passed description
	 */
	public boolean addPredicate(Predicate p){
		pred.add(p);
		return true;
	}

	public Set<Predicate> getPreicates(){
		return pred;
	}

	/*
	 * only needed for the Bilateral algorithm
	 */
	public boolean evaluate(long...solutions){
		boolean ret = true;
		//The solutions should satisfy each predicate
		//of the base element
		for(Predicate p : pred){
			if(!p.evaluate(solutions)){
				ret = false;
				break;
			}
		}
		return ret;
	}

	public BinopExpr instantiate(Value var){
		Iterator<Predicate> iter = pred.iterator();
		//should have at least one predicate
		BinopExpr ret = iter.next().instantitate(var);
		while(iter.hasNext()){
			BinopExpr predExpr = iter.next().instantitate(var);
			// if more than one they are conjoint together, i.e., logical and
			ret = new GAndExpr(ret,predExpr);
		}
		return ret;
	}

	@Override
	public String toString(){
		String s = "[";
		for(Predicate p : pred){
			s += p.toString();
		}
		s +="]";
		return s;
	}

	@Override
	public boolean equals(Object o){
		boolean ret = false;
		if(o instanceof BaseElement){
			BaseElement other = (BaseElement)o;	
			ret = pred.equals(other.getPreicates());
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return pred.hashCode();	
	}

}
