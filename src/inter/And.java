package inter;

import lexer.Token;

public class And extends Logical {
 
	public And(Token tok,Expr expr1,Expr expr2){
		super(tok,expr1,expr2);
	}
	public void jumping(int t,int f){
		int label=t!=0?t:newlable();
		expr1.jumping(0, label);
		expr2.jumping(t, f);
		if(f==0)emitLabel(label);
	}
}
