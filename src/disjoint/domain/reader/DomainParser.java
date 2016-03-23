// Generated from DisjointDomain.g4 by ANTLR 4.1

package disjoint.domain.reader;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DomainParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OPENL=1, OPENR=2, CLSDL=3, CLSDR=4, COMMA=5, DOTS=6, INF=7, INT=8, WS=9;
	public static final String[] tokenNames = {
		"<INVALID>", "'('", "')'", "'['", "']'", "','", "'..'", "'inf'", "INT", 
		"WS"
	};
	public static final int
		RULE_intervals = 0, RULE_interval = 1, RULE_singleton = 2;
	public static final String[] ruleNames = {
		"intervals", "interval", "singleton"
	};

	@Override
	public String getGrammarFileName() { return "DisjointDomain.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public DomainParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class IntervalsContext extends ParserRuleContext {
		public IntervalContext interval(int i) {
			return getRuleContext(IntervalContext.class,i);
		}
		public List<SingletonContext> singleton() {
			return getRuleContexts(SingletonContext.class);
		}
		public SingletonContext singleton(int i) {
			return getRuleContext(SingletonContext.class,i);
		}
		public List<IntervalContext> interval() {
			return getRuleContexts(IntervalContext.class);
		}
		public IntervalsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intervals; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DomainListener ) ((DomainListener)listener).enterIntervals(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DomainListener ) ((DomainListener)listener).exitIntervals(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DomainVisitor ) return ((DomainVisitor<? extends T>)visitor).visitIntervals(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntervalsContext intervals() throws RecognitionException {
		IntervalsContext _localctx = new IntervalsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_intervals);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OPENL) | (1L << CLSDL) | (1L << INT))) != 0)) {
				{
				setState(8);
				switch (_input.LA(1)) {
				case OPENL:
				case CLSDL:
					{
					setState(6); interval();
					}
					break;
				case INT:
					{
					setState(7); singleton();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(12);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntervalContext extends ParserRuleContext {
		public Token open;
		public Token lhs;
		public Token del;
		public Token rhs;
		public Token close;
		public List<TerminalNode> INF() { return getTokens(DomainParser.INF); }
		public List<TerminalNode> INT() { return getTokens(DomainParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(DomainParser.INT, i);
		}
		public TerminalNode INF(int i) {
			return getToken(DomainParser.INF, i);
		}
		public IntervalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DomainListener ) ((DomainListener)listener).enterInterval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DomainListener ) ((DomainListener)listener).exitInterval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DomainVisitor ) return ((DomainVisitor<? extends T>)visitor).visitInterval(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntervalContext interval() throws RecognitionException {
		IntervalContext _localctx = new IntervalContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_interval);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(13);
			((IntervalContext)_localctx).open = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==OPENL || _la==CLSDL) ) {
				((IntervalContext)_localctx).open = (Token)_errHandler.recoverInline(this);
			}
			consume();
			setState(14);
			((IntervalContext)_localctx).lhs = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==INF || _la==INT) ) {
				((IntervalContext)_localctx).lhs = (Token)_errHandler.recoverInline(this);
			}
			consume();
			setState(15);
			((IntervalContext)_localctx).del = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==COMMA || _la==DOTS) ) {
				((IntervalContext)_localctx).del = (Token)_errHandler.recoverInline(this);
			}
			consume();
			setState(16);
			((IntervalContext)_localctx).rhs = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==INF || _la==INT) ) {
				((IntervalContext)_localctx).rhs = (Token)_errHandler.recoverInline(this);
			}
			consume();
			setState(17);
			((IntervalContext)_localctx).close = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==OPENR || _la==CLSDR) ) {
				((IntervalContext)_localctx).close = (Token)_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SingletonContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(DomainParser.INT, 0); }
		public SingletonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleton; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DomainListener ) ((DomainListener)listener).enterSingleton(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DomainListener ) ((DomainListener)listener).exitSingleton(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DomainVisitor ) return ((DomainVisitor<? extends T>)visitor).visitSingleton(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingletonContext singleton() throws RecognitionException {
		SingletonContext _localctx = new SingletonContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_singleton);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19); match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\13\30\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\3\2\3\2\7\2\13\n\2\f\2\16\2\16\13\2\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\4\3\4\3\4\2\5\2\4\6\2\6\4\2\3\3\5\5\3\2\t\n\3\2\7\b\4\2\4\4\6\6\26"+
		"\2\f\3\2\2\2\4\17\3\2\2\2\6\25\3\2\2\2\b\13\5\4\3\2\t\13\5\6\4\2\n\b\3"+
		"\2\2\2\n\t\3\2\2\2\13\16\3\2\2\2\f\n\3\2\2\2\f\r\3\2\2\2\r\3\3\2\2\2\16"+
		"\f\3\2\2\2\17\20\t\2\2\2\20\21\t\3\2\2\21\22\t\4\2\2\22\23\t\3\2\2\23"+
		"\24\t\5\2\2\24\5\3\2\2\2\25\26\7\n\2\2\26\7\3\2\2\2\4\n\f";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}