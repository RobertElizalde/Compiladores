a.out: main.o lex.yy.o ccompiler.tab.o 
		gcc main.o lex.yy.o ccompiler.tab.o
main.o: main.c ccompiler.tab.c
		gcc -c main.c
lex.yy.o: lex.yy.c
		gcc -c lex.yy.c
ccompiler.tab.o: ccompiler.tab.c
		gcc -c ccompiler.tab.c
lex.yy.c: ccompiler.l ccompiler.tab.c
		flex ccompiler.l
ccompiler.tab.c: ccompiler.y
		bison -d ccompiler.y
clean: 
		rm -f lex.yy.* ccompiler.tab.* a.out main.o
run: a.out
		./a.out
