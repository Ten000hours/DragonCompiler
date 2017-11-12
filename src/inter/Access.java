package inter;

import lexer.Tag;
import lexer.Word;

import com.symbols.Type;

public class Access extends Op {

	public Id array;
	public Expr index;

	public Access(Id a, Expr i, Type p1) {
		super(new Word("[]", Tag.INDEX), p1);
		array = a;
		index = i;
	}

	public Expr gen() {
		return new Access(array, index.reduce(), type);
	}

	public void jumping(int t, int f) {
		emitjumps(reduce().toString(), t, f);
	}

	public String toString() {
		return array.toString() + " [ " + index.toString() + " ] ";

	}

}
