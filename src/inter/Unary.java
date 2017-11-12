package inter;

import com.symbols.Type;

import lexer.Token;

public class Unary extends Op {

	public Expr expr;
	public Unary(Token tok,Expr x){
		super(tok,null);
		expr=x;
		type=Type.max(Type.Int, expr.type);
		if(type==null) error("type error");
		
	}
	public Expr gen(){
		return new Unary(op, expr.reduce());
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return op.toString()+" "+expr.toString();
	}
}
