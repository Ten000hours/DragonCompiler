package inter;

import com.symbols.Type;

import lexer.Token;

public class Op extends Expr{

	public Op(Token t,Type p){
		super(t,p);
	}
	public Expr reduce(){
		Expr x=gen();
		Temp t=new Temp(type);
		emit(t.toString()+"+"+x.toString());
		return t;
	}
	
}
