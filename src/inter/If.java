package inter;

import com.symbols.Type;

public class If extends Stmt {

	Expr expr;
	Stmt stmt;
	public If(Expr x,Stmt s){
		expr=x;
		stmt=s;
		if(expr.type!=Type.Bool)expr.error("boolean is needed in if");
	}
	public void gen(int b,int a){
		int label=newlable();
		expr.jumping(0, a);
		emitLabel(label);
		stmt.gen(label, a);
	}
}
