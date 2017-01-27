grammar LITTLE;

tokens: .* EOF;

T1 : 'ab';

COMMENT : '--' .* EOL ;

WS : [ \t\r\n]+ -> skip;