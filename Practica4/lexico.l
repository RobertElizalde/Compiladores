D			[0-9]
L			[a-zA-Z_]
H			[a-fA-F0-9]
E			[Ee][+-]?{D}+
FS			(f|F|l|L)
IS			(u|U|l|L)*

%{
#include <stdio.h>

void count();
%}

%%
"/*"			{ printf("<BLOQUE_COMENTADO_INICIO>\n"); }

"auto"			{ printf("<PALABRARESERVADA_AUTO>\n");}
"break"			{ printf("<PALABRARESERVADA_BREAK>\n"); }
"case"			{ printf("<PALABRARESERVADA_CASE>\n"); }
"char"			{ printf("<PALABRARESERVADA_CHAR>\n"); }
"const"			{ printf("<PALABRARESERVADA_CONST>\n"); }
"continue"		{ printf("<PALABRARESERVADA_CONTINUE>\n"); }
"default"		{ printf("<PALABRARESERVADA_DEFAULT>\n"); }
"do"			{ printf("<PALABRARESERVADA_DO>\n"); }
"double"		{ printf("<PALABRARESERVADA_DOUBLE>\n"); }
"else"			{ printf("<PALABRARESERVADA_ELSE>\n"); }
"enum"			{ printf("<PALABRARESERVADA_ENUM>\n"); }
"extern"		{ printf("<PALABRARESERVADA_EXTERN>\n"); }
"float"			{ printf("<PALABRARESERVADA_FLOAT>\n"); }
"for"			{ printf("<PALABRARESERVADA_FOR>\n"); }
"goto"			{ printf("<PALABRARESERVADA_GOTO>\n"); }
"if"			{ printf("<PALABRARESERVADA_IF>\n"); }
"int"			{ printf("<PALABRARESERVADA_INT>\n"); }
"long"			{ printf("<PALABRARESERVADA_LONG>\n"); }
"register"		{ printf("<PALABRARESERVADA_REGISTER>\n"); }
"return"		{ printf("<PALABRARESERVADA_RETURN>\n"); }
"short"			{ printf("<PALABRARESERVADA_SHORT>\n"); }
"signed"		{ printf("<PALABRARESERVADA_SIGNED>\n"); }
"sizeof"		{ printf("<PALABRARESERVADA_SIZEOF>\n"); }
"static"		{ printf("<PALABRARESERVADA_STATIC>\n"); }
"struct"		{ printf("<PALABRARESERVADA_STRUCT>\n"); }
"switch"		{ printf("<PALABRARESERVADA_SWITCH>\n"); }
"typedef"		{ printf("<PALABRARESERVADA_TYPEDEF>\n"); }
"union"			{ printf("<PALABRARESERVADA_UNION>\n"); }
"unsigned"		    { printf("<PALABRARESERVADA_UNSIGNED>\n"); }
"void"			    { printf("<PALABRARESERVADA_VOID>\n"); }
"volatile"		    { printf("<PALABRARESERVADA_VOLATILE>\n"); }
"while"			    { printf("<PALABRARESERVADA_WHILE>\n"); }

{L}({L}|{D})*		{ printf("<ID>\n"); }

0[xX]{H}+{IS}?		{ printf("<CONSTANTE_ENTERO>\n"); }
0{D}+{IS}?		{ printf("<CONSTANTE_ENTERO_OCTAL>\n"); }
{D}+{IS}?		{ printf("<CONSTANTE_ENTERO_DECIMAL>\n"); }
L?'(\\.|[^\\'])+'	{ printf("<CONSTANTE_CADENA>\n"); }

{D}+{E}{FS}?		    { printf("<CONTANTE_FLOTANTE>\n"); }
{D}*"."{D}+({E})?{FS}?	{ printf("<CONSTANTE_FLOTANTE>\n"); }
{D}+"."{D}*({E})?{FS}?	{ printf("<CONSTANTE_FLOTANTE>\n"); }

L?\"(\\.|[^\\"])*\"	{ printf("<CONSTANTE_CADENA>\n"); }

"..."			{ printf("<ELLIPSIS>\n"); }
">>="			{ printf("<ASIGNACION_CORRIMIENTO_D>\n"); }
"<<="			{ printf("<ASIGNACION_CORRIMIENTO_I>>\n"); }
"+="			{ printf("<ASIGNACION_SUM>\n"); }
"-="			{ printf("<ASIGNACION_RES>\n"); }
"*="			{ printf("<ASIGNACION_MUL>\n"); }
"/="			{ printf("<ASIGANCION_DIV>\n"); }
"%="			{ printf("<ASIGNACION_MOD>\n"); }
"&="			{ printf("<ASIGNACION_AND>\n"); }
"^="			{ printf("<ASIGNACION_XOR>\n"); }
"|="			{ printf("<ASIGNACION_OR>\n"); }
">>"			{ printf("<OPERADOR_CORRIMIENTO_D>\n"); }
"<<"			{ printf("<OPERADOR_CORRIMIENTO_I>\n"); }
"++"			{ printf("<OPERADOR_INCREMENTO>\n"); }
"--"			{ printf("<OPERADOR_DECREMENTO>\n"); }
"->"			{ printf("<OPERADOR_PTR>\n"); }
"&&"			{ printf("<OPERADOR_AND>\n"); }
"||"			{ printf("<OPERADOR_OR>\n"); }
"<="			{ printf("<OPERADOR_MENORIGUAL>\n"); }
">="			{ printf("<OPERADOR_MAYORIGUAL>\n"); }
"=="			{ printf("<OPERADOR_IGUA>\n"); }
"!="			{ printf("<OPERADOR_NOIGUAL>\n"); }
";"			{ printf("<;>\n"); }
("{"|"<%")		{ printf("<{>\n"); }
("}"|"%>")		{ printf("<}>\n"); }
","			{ printf("<,>\n"); }
":"			{ printf("<:>\n"); }
"="			{ printf("<=>\n"); }
"("			{ printf("<(>\n"); }
")"			{ printf("<)>\n"); }
("["|"<:")		{ printf("<[>\n"); }
("]"|":>")		{ printf("<]>\n"); }
"."			{ printf("<.>\n"); }
"&"			{ printf("<&>\n"); }
"!"			{ printf("<!>\n"); }
"~"			{ printf("<~>\n"); }
"-"			{ printf("<->\n"); }
"+"			{ printf("<+>\n"); }
"*"			{ printf("<*>\n"); }
"/"			{ printf("</>\n"); }
"%"			{ printf("<%%>\n"); }
"<"			{ printf("<|<|>\n"); }
">"			{ printf("<|>|>\n"); }
"^"			{ printf("<^>\n"); }
"|"			{ printf("<|>\n"); }
"?"			{ printf("<?>\n"); }

[ \t\v\n\f]		{ printf("<CARACTER_ESCAPE>\n"); }
.			{ /* ignore bad characters */ }

%%

yywrap()
{
	return(1);
}
