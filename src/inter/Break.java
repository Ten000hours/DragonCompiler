package inter;

public class Break extends Stmt{

	public Stmt stmt;
	public Break(){
		if(stmt.Enclosing==null) error("unclosed break");
		stmt=Stmt.Enclosing;
	}
	public void gen(int b,int a){
		emit("goto L"+stmt.after);
	}
}
