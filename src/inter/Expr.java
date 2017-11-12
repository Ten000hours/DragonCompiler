package inter;

import com.symbols.Type;

import lexer.Token;

public class Expr extends Node {

	public Token op;
	public Type type;
	public Expr(Token tok,Type p){
		type=p;
		op=tok;
	}
	public Expr gen() {
		return this;
	}
	public Expr reduce(){
		return this;
	}
	public void jumping(int t,int f) {
		emitjumps(toString(),t,f);
	}
	public void emitjumps(String test,int s,int f){
		
		if(s!=0&&f!=0){
			emit("if"+test+"goto L"+s);
			emit("goto L"+f);
			
		}
		else if(s!=0){emit("if"+test+"goto L"+s);}
		else if(f!=0){emit("iffalse "+test+"goto L"+f);}
		else ;
	}
	public String toString(){return op.toString();}
}
