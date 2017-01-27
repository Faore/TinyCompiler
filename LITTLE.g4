grammar LITTLE;

tokens: .* EOF;

COMMENT: '--''.'* -> skip;
STRINGLITERAL:["]~["]*["];
INTLITERAL: [0-9]+;
FLOATLITERAL:([0-9]+'.'[0-9]+)|'.'[0-9]+;

KEYWORD:	'PROGRAM'|'BEGIN'|'ENDIF'|'ENDWHILE'|'END'|'FUNCTION'|'READ'|'WRITE'|'IF'|'ELSE'|'WHILE'|'CONTINUE'|'BREAK'|'RETURN'|'INT'|'VOID'|'STRING'|'FLOAT';
OPERATOR:	':='|'!='|'<='|'>='|'+'|'-'|'*'|'='|'<'|'>'|'/'|','|'('|')'|';';
IDENTIFIER: [A-z][A-z0-9]*;

WHITESPACE: [ \n\r\t] -> skip;