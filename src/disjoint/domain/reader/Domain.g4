grammar DisjointDomain;
@header {
package disjoint.domain.reader;
}
intervals : (interval | singleton)*;

interval: open=('(' | '[') lhs=(INT|INF) del=(',' | '..') rhs=(INT|INF) close=(']' | ')');

singleton: INT;

OPENL : '(';
OPENR : ')';
CLSDL : '[';
CLSDR : ']';

COMMA : ',';
DOTS: '..';

INF: 'inf';

INT : '-'[0-9]+ | [0-9]+;
WS : [ \t\r\n]+ -> skip ;