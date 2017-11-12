package inter;

import lexer.Lexer;

public class Node {

	public int lexmeline = 0;

	public Node() {
		lexmeline = Lexer.line;
	}

	public void error(String s) {
		throw new Error("near line:" + lexmeline + ": " + s);
	}

	public static int label = 0;

	public int newlable() {

		return ++label;

	}

	public void emitLabel(int i) {
		System.out.println("L" + i + ":");
	}

	public void emit(String s) {
		System.out.println("\t" + s);
	}
}
