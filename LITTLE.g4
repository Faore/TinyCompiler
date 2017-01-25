grammar LITTLE;

tokens: .* EOF;

T1 : 'ab';

WS : [ \t\r\n]+ -> skip;