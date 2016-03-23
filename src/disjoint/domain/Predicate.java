package disjoint.domain;

import soot.Value;
import soot.jimple.ConditionExpr;

/**
 * Predicate that is used
 * to build the base elements
 * of a domain.
 * @author elenasherman
 *
 */
public abstract class Predicate {
	
	//each of them should have an operator;
	String op;
	
	public String getOp(){
		return op;
	}
	
	/*
	 * Evaluation is needed for the Bilateral algorithm
	 */
	public abstract boolean evaluate(long...solutions);
	
	public abstract ConditionExpr instantitate(Value var);
	
	@Override
	public abstract boolean equals(Object o);
	
	@Override
	public abstract int hashCode();

}
