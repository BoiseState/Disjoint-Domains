package disjoint.domain.reader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

import soot.IntType;
import soot.grimp.internal.GAndExpr;
import soot.jimple.BinopExpr;
import soot.jimple.internal.JimpleLocal;

import com.microsoft.z3.Z3Exception;

import disjoint.domain.BaseElement;
import disjoint.domain.Domain;
import disjoint.domain.IntervalPredicate;
import disjoint.driver.StartAnalysis;
import disjoint.solver.SolverWrapper;

public class DomainInstantiator extends DomainBaseVisitor {

	Domain domain;

	public DomainInstantiator() throws Z3Exception{
		domain = new Domain();
	}
	@Override public BaseElement visitIntervals(@NotNull DomainParser.IntervalsContext ctx) { 
		//whatever children return add to the domain
		for( ParseTree child : ctx.children){
			Set<BaseElement> beSet = (Set<BaseElement>) visit(child);
			Iterator<BaseElement> iter = beSet.iterator();
			while(iter.hasNext()){
				domain.addBaseElement(iter.next());
			}
		}
		//domain.done(); unimplemented method that should check whether the domain is complete
		//we do it here ...
		checkCompleteAndDisjoint();
		return null; 
	}

	private void checkCompleteAndDisjoint(){
		//check whether the domain is complete, get the 
		//negation of the disjunction of all predicate
		JimpleLocal temp = new JimpleLocal("temp", IntType.v());
		BinopExpr complete = domain.top(temp);
		SolverWrapper solver = StartAnalysis.getSolver(); //new SolverWrapperZ3();
		boolean sat = solver.evaluateNot(complete);
		if(sat){
			System.out.println("Domain " + domain + " is incomplete !");
			System.exit(2);
		}
		//check whether the domain is pairwise disjoint
		//by default set that domain is disjoint
		domain.setDisjoint();
		for(BaseElement be1 : domain.getBaseElements()){
			for(BaseElement be2 : domain.getBaseElements()){
				if(!be1.equals(be2)){
					//create a conjunction of those two base elements
					BinopExpr disjoint = new GAndExpr(be1.instantiate(temp), be2.instantiate(temp));
					sat = solver.evaluate(disjoint);
					//disjoint predicates do not have a solution in common
					if(sat){
						domain.setNotDisjoint();
						return; //we're done -- found at lest one pair of predicates that are not pair-wise disjoint
					}
				}
			}
		}
	}


	@Override public  Object visitInterval(@NotNull DomainParser.IntervalContext ctx) { 
		Set<BaseElement> ret = new HashSet<BaseElement>();
		//get the lhs number
		//get open parent
		boolean lhsIncl = false;
		switch(ctx.open.getType()){
		case DomainParser.OPENL:{
			// have '(' - lhs exclusive
			lhsIncl = false;
		}; 
		break;
		case DomainParser.CLSDL:{
			// have '[' -- lhs inclusive
			lhsIncl = true;
		};
		break;
		}
		String lhsVal = ctx.lhs.getText();
		boolean lhsInfty = false;
		if(ctx.lhs.getType() == DomainParser.INF){
			lhsInfty = true;
		}

		boolean rhsIncl = false;
		switch(ctx.close.getType()){
		case DomainParser.OPENR:{
			// have '(' - lhs exclusive
			rhsIncl = false;
		}; 
		break;
		case DomainParser.CLSDR:{
			// have '[' -- lhs inclusive
			rhsIncl = true;
		};
		break;
		}
		String rhsVal = ctx.rhs.getText();
		boolean rhsInfty = false;
		if(ctx.rhs.getType() == DomainParser.INF){
			rhsInfty = true;
		}

		//figure out the delimiter
		switch(ctx.del.getType()){
		case DomainParser.COMMA:{
			//if comma then creat a range
			BaseElement be = new BaseElement();
			//add be to the set
			ret.add(be);
			//create base element with both sides
			if(!lhsInfty){
				//if not infinity then add new predicate
				if(lhsIncl){
					//include lhs integer
					be.addPredicate(new IntervalPredicate(">=", lhsVal));
				}else{
					//dont inclue lhs integer
					be.addPredicate(new IntervalPredicate(">", lhsVal));
				}
			}//end if not infty

			if(!rhsInfty){
				//if not infinity then add new predicate
				if(rhsIncl){
					//make include rhs integer
					be.addPredicate(new IntervalPredicate("<=", rhsVal));
				} else {
					//don't incluede rhs integer
					be.addPredicate(new IntervalPredicate("<", rhsVal));
				}
			}

		};
		break;
		case DomainParser.DOTS: {
			//iterate inclusively or 
			//Exclusively over the interval
			//range should not have empty lhs/rhs
			if(lhsInfty || rhsInfty){
				System.out.println("Wrong format for range");
			} else {
				//get the integer values
				int lhsIntVal = Integer.parseInt(lhsVal);
				//increment if not inclusive since we need
				//to iterate over it
				if(!lhsIncl) lhsIntVal++;
				int rhsIntVal = Integer.parseInt(rhsVal);
				//since it's rhs decrement if it 
				//is not inclusive -- need for iteration
				if(!rhsIncl) rhsIntVal--;
				for(int i = lhsIntVal; i <= rhsIntVal; i++){
					BaseElement be = new BaseElement();
					be.addPredicate(new IntervalPredicate("=",String.valueOf(i)));
					ret.add(be);
				}
			}
		}
		break;
		}
		return ret; 

	}

	@Override public Object visitSingleton(@NotNull DomainParser.SingletonContext ctx) { 
		Set<BaseElement> ret = new HashSet<BaseElement>();
		String val = ctx.getText();
		BaseElement be = new BaseElement();
		be.addPredicate(new IntervalPredicate("=",val));
		ret.add(be);
		return ret; 
	}


}
