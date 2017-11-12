package com.symbols;

import lexer.Tag;
import lexer.Word;

public class Type extends Word {
	public int width = 0;

	public Type(String s, int tag, int w) {
		super(s, tag);
		width = w;
	}

	public static final Type Int = new Type("int", lexer.Tag.BASIC, 4),
			Float = new Type("float", lexer.Tag.BASIC, 8), Char = new Type(
					"char", lexer.Tag.BASIC, 1), Bool = new Type("bool",
					lexer.Tag.BASIC, 1);

	public static boolean numberic(Type p) {
		if (p == Type.Int || p == Type.Char || p == Type.Float) {
			return true;
		}
		return false;
	}

	public static Type max(Type p1, Type p2) {
		if (!numberic(p1) || !numberic(p2))
			return null;
		else if (p1 == Type.Int || p2 == Type.Int)
			return Type.Int;
		else if (p1 == Type.Float || p2 == Type.Float)
			return Type.Float;
		else
			return Type.Char;
	}
}
