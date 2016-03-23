package disjoint.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DivExpr;
import soot.jimple.EqExpr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IntConstant;
import soot.jimple.LeExpr;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SubExpr;
import soot.jimple.internal.JimpleLocal;
import soot.Value;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.Model;
import com.microsoft.z3.Params;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class SolverWrapperZ3 implements SolverWrapper {


	/**
	 * This class is a wrapper for the actual
	 * solver used in the analysis which is Z3
	 * @author elenasherman
	 *
	 */
	private Context ctx;
	//no need to create a new IntExpr every time
	private Map<Value, IntExpr> sootVarToZ3Var;

	private int timeout; //in milliseconds

	public SolverWrapperZ3() throws Z3Exception{
		Map<String, String> cfg = new HashMap<String, String>();
		ctx = new Context(cfg);
		sootVarToZ3Var = new HashMap<Value,IntExpr>();
	}

	public boolean equals(BinopExpr expr1, BinopExpr expr2){
		BoolExpr z3Formula1 = generate(expr1);
		BoolExpr z3Formula2 = generate(expr2);
		BoolExpr z3Formula = null;;
		try {
			BoolExpr lhs = ctx.MkImplies(z3Formula1, z3Formula2);
			BoolExpr rhs = ctx.MkImplies(z3Formula2, z3Formula1);
			z3Formula = ctx.MkAnd(new BoolExpr[]{lhs, rhs});
			Expr [] forall = new Expr[sootVarToZ3Var.size()];
			int i = 0;
			for(IntExpr var : sootVarToZ3Var.values()){
				forall[i] = var;
				i++;
			}
			z3Formula = ctx.MkForall(forall, z3Formula, 0, null, null, null, null);
		} catch (Z3Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean ret = solve(z3Formula);
		return ret;
	}

	public boolean solve(BoolExpr z3Formula){
		boolean ret = true;
		try {
			Solver solver = ctx.MkSolver();
			Params p = ctx.MkParams();
			p.Add("soft_timeout", timeout);
			solver.setParameters(p);
			solver.Assert(z3Formula);
			Status result = solver.Check();
			if(result.equals(Status.SATISFIABLE)){
				ret = true;
			} else if (result.equals(Status.UNSATISFIABLE)){
				ret = false;
			} else {
				//unknown
				System.out.println("Warning: " + result + " for " + z3Formula);
			}
		} catch (Z3Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public boolean evaluate(BinopExpr expr){
		BoolExpr z3Formula = generate(expr);
		boolean ret = solve(z3Formula);
		return ret;

	}

	public boolean evaluateNot(BinopExpr expr){
		boolean ret = false;
		BoolExpr z3Formula = generate(expr);
		BoolExpr z3FormulaNot;
		try {
			z3FormulaNot = ctx.MkNot(z3Formula);
			ret = solve(z3FormulaNot);
		} catch (Z3Exception e) {
			e.printStackTrace();
		}
		return ret;

	}

	public String smt2(BinopExpr expr){
		return generate(expr).toString();
	}

	private BoolExpr generate(BinopExpr expr) {
		BoolExpr ret = null;
		if(expr instanceof ConditionExpr){
			//get two sides and the operator
			ConditionExpr condExpr = (ConditionExpr)expr;
			//lhs can either be constant or a local
			//12-29-14, not right now when we 
			//have more general formula
			Value lhs = condExpr.getOp1();
			IntExpr lhsExpr = evaluateExpr(lhs);

			//rhs can also be an arithmetic expression
			//from converting assignments to equality
			Value rhs = condExpr.getOp2();
			ArithExpr rhsExpr = null;
			//add conditionals here first to check
			if(rhs instanceof BinopExpr){
				BinopExpr rhsBinop = (BinopExpr) rhs;
				IntExpr lhsArith = evaluateExpr(rhsBinop.getOp1());
				IntExpr rhsArith = evaluateExpr(rhsBinop.getOp2());
				//now determine the operator add, sub, mult
				try {
					if(rhsBinop instanceof AddExpr){
						ArithExpr[] operands = new ArithExpr[]{lhsArith, rhsArith};
						rhsExpr = ctx.MkAdd(operands);
					} else if (rhsBinop instanceof SubExpr){
						ArithExpr[] operands = new ArithExpr[]{lhsArith, rhsArith};
						rhsExpr = ctx.MkSub(operands);
					} else if (rhsBinop instanceof MulExpr){
						ArithExpr[] operands = new ArithExpr[]{lhsArith, rhsArith};
						rhsExpr = ctx.MkMul(operands);
					} else if (rhsBinop instanceof DivExpr){
						rhsExpr = ctx.MkDiv(lhsArith, rhsArith);
					} else if(rhsBinop instanceof RemExpr){
						rhsExpr = ctx.MkMod(lhsArith, rhsArith);
					} else if (rhsBinop instanceof ShrExpr){
						//can only handle when rhs,i.e., y is not a variable
						// x >> y = x / (2^y)
						if(rhsArith.IsArithmeticNumeral()){
							IntNum number = (IntNum)rhsArith;
							rhsArith = ctx.MkInt(1<<number.Int()); // this is 2^y
							rhsExpr = ctx.MkDiv(lhsArith, rhsArith);
						} else {
							System.out.println("Rhs in ShrExpr is not a number " + rhsArith.getClass());
							System.exit(2);
						}
					} else if(rhsBinop instanceof ShlExpr){
						//can only handle when rhs, i.e., u is not a variable
						// x << y = x * (2^y)
						if(rhsArith.IsArithmeticNumeral()){
							IntNum number = (IntNum)rhsArith;
							rhsArith = ctx.MkInt(1<<number.Int()); // this is 2^y
							ArithExpr[] operands = new ArithExpr[]{lhsArith, rhsArith};
							rhsExpr = ctx.MkMul(operands);
						} else {
							System.out.println("Rhs in ShlExpr is not a number " + rhsArith.getClass());
							System.exit(2);
						}

					} else {
						System.out.println("Cannot process rhsBinop " + rhsBinop.getClass());
						System.exit(2);
					}
				} catch (Z3Exception e) {
					e.printStackTrace();
				}
			} else if (rhs instanceof NegExpr){
				try {
					ArithExpr[] operands;
					operands = new ArithExpr[]{ctx.MkInt(0), evaluateExpr(((NegExpr)rhs).getOp())};
					rhsExpr = ctx.MkSub(operands);
				} catch (Z3Exception e) {
					e.printStackTrace();
				}
			} else  {
				rhsExpr = evaluateExpr(rhs);
			}

			//now generate the condition
			try {
				if(expr instanceof EqExpr){
					ret = ctx.MkEq(lhsExpr, rhsExpr);
				} else if (expr instanceof GeExpr){
					ret = ctx.MkGe(lhsExpr, rhsExpr);
				} else if (expr instanceof GtExpr){
					ret = ctx.MkGt(lhsExpr, rhsExpr);
				} else if (expr instanceof LeExpr){
					ret = ctx.MkLe(lhsExpr, rhsExpr);
				} else if (expr instanceof LtExpr){
					ret = ctx.MkLt(lhsExpr, rhsExpr);
				} else if (expr instanceof NeExpr){
					ret = ctx.MkNot(ctx.MkEq(lhsExpr, rhsExpr));
				}
			} catch (Z3Exception e) {
				e.printStackTrace();
			}

		} else if (expr instanceof OrExpr){
			BoolExpr lhs = generate((BinopExpr)expr.getOp1());
			BoolExpr rhs = generate((BinopExpr)expr.getOp2());
			try {
				ret = ctx.MkOr(new BoolExpr[]{lhs, rhs});
			} catch (Z3Exception e) {
				e.printStackTrace();
			}
		} else if (expr instanceof AndExpr){
			BoolExpr lhs = generate((BinopExpr)expr.getOp1());
			BoolExpr rhs = generate((BinopExpr)expr.getOp2());
			try {
				ret = ctx.MkAnd(new BoolExpr[]{lhs, rhs});
			} catch (Z3Exception e) {
				e.printStackTrace();
			}
		} else {
			//something else that we don't handle yet :(
			System.out.println("Cannot process " + expr);
			System.exit(2);
		}
		return ret;
	}


	private IntExpr evaluateExpr(Value v){
		IntExpr ret = null;
		if(v instanceof JimpleLocal){
			//check in the map
			if(sootVarToZ3Var.containsKey(v)){
				ret = sootVarToZ3Var.get(v);
			} else {
				try {
					ret = ctx.MkIntConst(v.toString());
				} catch (Z3Exception e) {
					e.printStackTrace();
				}
				sootVarToZ3Var.put(v, ret);
			}
		} else if (v instanceof IntConstant){
			try {
				ret = ctx.MkInt(((IntConstant)v).value);
			} catch (Z3Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Cannot process singelton " + v + " of " + v.getClass());
		}
		return ret;
	}

	//evaluates and returns the value of the unknown
	public List<Long> evaluateSol(BinopExpr expr, BinopExpr a, Value ... unknown) {
		List<Long> ret = null;
		//("expr " + expr + " " + " a " + a);
		BoolExpr z3Formula = generate(expr);
		try {
			BoolExpr z3ForA = a==null? ctx.MkFalse() : generate(a);
			//negate a
			BoolExpr negA = ctx.MkNot(z3ForA);
			//("negA " + negA);
			z3Formula = ctx.MkAnd(new BoolExpr[]{z3Formula, negA});
			Solver solver = ctx.MkSolver();
			Params p = ctx.MkParams();
			p.Add("soft_timeout", timeout);
			solver.setParameters(p);
			solver.Assert(z3Formula);
			Status result = solver.Check();
			if(result.equals(Status.SATISFIABLE)){
				ret = new ArrayList<Long>();
				Model m = solver.Model();
				for(Value v : unknown){
					IntExpr res = (IntExpr)m.ConstInterp(sootVarToZ3Var.get(v));
					if(res.toString().contains("mod") || res.toString().contains("div")){
						System.out.println("Z3Formula  " + z3Formula);
						return null;
					}
					ret.add(Long.parseLong(res.toString()));
				}
			} else if (result.equals(Status.UNSATISFIABLE)){
				ret = new ArrayList<Long>();
			} else {
				//unknown
				System.out.println("Warning: " + result + " for " + z3Formula);
			}
		} catch (Z3Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void setTimeOut(int valueInMilliseconds) {
		timeout = valueInMilliseconds;

	}

}
