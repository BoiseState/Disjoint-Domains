// Generated from DisjointDomain.g4 by ANTLR 4.1

package disjoint.domain.reader;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DomainParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DomainVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DomainParser#interval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterval(@NotNull DomainParser.IntervalContext ctx);

	/**
	 * Visit a parse tree produced by {@link DomainParser#singleton}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleton(@NotNull DomainParser.SingletonContext ctx);

	/**
	 * Visit a parse tree produced by {@link DomainParser#intervals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervals(@NotNull DomainParser.IntervalsContext ctx);
}