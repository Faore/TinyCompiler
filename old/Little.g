grammar Little;
tokens : .* EOL ;
COMMENT : '--' .* EOL ;
WS : [ \t\r\n]+ -> skip ;
