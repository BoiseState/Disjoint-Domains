package disjoint.solver;

import java.util.List;

import soot.Value;
import soot.jimple.BinopExpr;

/**
 * Interface for essential operation
 * used in the analysis
 * @author elenasherman
 *
 */
public interface SolverWrapper {
	
	public boolean equals(BinopExpr e1, BinopExpr e2);
	public boolean evaluate(BinopExpr e);
	public boolean evaluateNot(BinopExpr e);
	public List<Long> evaluateSol(BinopExpr e1, BinopExpr e2, Value ... unknown);
	public String smt2(BinopExpr e);
	public void setTimeOut(int valueInMilliseconds);

}