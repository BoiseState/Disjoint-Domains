// Generated from DisjointDomain.g4 by ANTLR 4.1

package disjoint.domain.reader;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DomainParser}.
 */
public interface DomainListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DomainParser#interval}.
	 * @param ctx the parse tree
	 */
	void enterInterval(@NotNull DomainParser.IntervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DomainParser#interval}.
	 * @param ctx the parse tree
	 */
	void exitInterval(@NotNull DomainParser.IntervalContext ctx);

	/**
	 * Enter a parse tree produced by {@link DomainParser#singleton}.
	 * @param ctx the parse tree
	 */
	void enterSingleton(@NotNull DomainParser.SingletonContext ctx);
	/**
	 * Exit a parse tree produced by {@link DomainParser#singleton}.
	 * @param ctx the parse tree
	 */
	void exitSingleton(@NotNull DomainParser.SingletonContext ctx);

	/**
	 * Enter a parse tree produced by {@link DomainParser#intervals}.
	 * @param ctx the parse tree
	 */
	void enterIntervals(@NotNull DomainParser.IntervalsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DomainParser#intervals}.
	 * @param ctx the parse tree
	 */
	void exitIntervals(@NotNull DomainParser.IntervalsContext ctx);
}