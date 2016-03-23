package disjoint.domain;

import soot.Value;
import soot.grimp.internal.GEqExpr;
import soot.grimp.internal.GGeExpr;
import soot.grimp.internal.GGtExpr;
import soot.grimp.internal.GLeExpr;
import soot.grimp.internal.GLtExpr;
import soot.grimp.internal.GNeExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.IntConstant;

/**
 * Special type of predicates
 * that are used to build
 * an interval (base element)
 * Contain of value (rhs) and 
 * the relational operator
 * X < 2, means less than 2
 * predicate, that can be associate
 * with any variable X.
 * @author elenasherman
 *
 */
public class IntervalPredicate extends Predicate {
	
	String val;
	
	public IntervalPredicate(String setOp, String setVal){
		op = setOp;
		val = setVal;
	}
	
	//val is always on rhs
	public String getVal(){
		return val;
	}

	@Override
	public boolean evaluate(long... solutions) {
		long sol = solutions[0];
		boolean ret = false;
		long rhs = Long.parseLong(val);
		switch(op){
		case "=": ret = (sol == rhs);
		break;
		case ">=" : ret = (sol >= rhs);
		break;
		case ">" : ret = (sol > rhs);
		break;
		case "<=" : ret = (sol <= rhs);
		break;
		case "<" : ret = (sol < rhs);
		break;
		case "!=" : ret = (sol != rhs);
		break;
		}
		return ret;
	}

	
	@Override
	public String toString(){
		String s= "X" + op + val;
		return s;
	}

	@Override
	public ConditionExpr instantitate(Value var) {
		ConditionExpr ret = null;
		IntConstant valInt = IntConstant.v(Integer.parseInt(val));
		switch(op){
		case "=": ret = new GEqExpr(var,valInt);
		break;
		case ">=" : ret = new GGeExpr(var, valInt);
		break;
		case ">" : ret = new GGtExpr(var, valInt);
		break;
		case "<=" : ret = new GLeExpr(var, valInt);
		break;
		case "<" : ret = new GLtExpr(var, valInt);
		break;
		case "!=" : ret = new GNeExpr(var, valInt);
		break;
		}
		if(ret == null){
			System.out.println("Cannot initialize interval predicate");
			System.exit(2);
		}
		return ret;
	}
	
	@Override
	public boolean equals(Object o){
		boolean ret = false;
		if(o instanceof IntervalPredicate){
			IntervalPredicate other = (IntervalPredicate) o;
			if (op.equals(other.getOp()) && val.equals(other.getVal())){
				ret = true;
			}
		}
		return ret;
	}
	
	@Override
	 public int hashCode() {
	 return op.concat(val).hashCode();	
	}
}
