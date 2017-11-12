package com.Parser;

import inter.Access;
import inter.And;
import inter.Arith;
import inter.Break;
import inter.Constant;
import inter.Do;
import inter.Else;
import inter.Expr;
import inter.Id;
import inter.If;
import inter.Not;
import inter.Or;
import inter.Rel;
import inter.Seq;
import inter.Set;
import inter.SetElem;
import inter.Stmt;
import inter.Unary;
import inter.While;

import java.io.IOException;

import com.symbols.Array;
import com.symbols.Env;
import com.symbols.Type;

import lexer.Lexer;
import lexer.Num;
import lexer.Tag;
import lexer.Token;
import lexer.Word;

public class Parser {

	private Lexer lex;
	private Token look;
	Env top = null;
	int used = 0;

	public Parser(Lexer l) throws IOException {
		lex = l;
		move();
	}

	public void move() throws IOException {
		look = lex.scan();
	}

	public void error(String s) {
		throw new Error("near line " + lex.line + ":" + s);
	}

	public void match(int t) throws IOException {
		if (look.Tag == t)
			move();
		else
			error("syntax error");
	}

	public void program() throws IOException {
		Stmt s = block();
		int begin = s.newlable();
		int after = s.newlable();
		s.emitLabel(begin);
		s.gen(begin, after);
		s.emitLabel(after);
	}

	private Stmt block() throws IOException {
		match('{');
		Env savedEnv = top;
		top = new Env(top);
		decls();
		Stmt s = stmts();
		match('}');
		top = savedEnv;
		return s;
	}

	private Stmt stmts() throws IOException {
		if (look.Tag == '}')
			return Stmt.Null;
		return new Seq(stmt(), stmts());

	}

	private Stmt stmt() throws IOException {
		Expr expr;
		Stmt stmt1, stmt2, stmt3;
		Stmt savedStmt;
		switch (look.Tag) {

		case ';':
			move();
			return Stmt.Null;
		case Tag.IF:
			match(Tag.IF);
			match('(');
			expr = bool();
			match(')');
			stmt1 = stmt();
			if (look.Tag != Tag.ELSE)
				return new If(expr, stmt1);
			match(Tag.ELSE);
			stmt2 = stmt();
			return new Else(expr, stmt1, stmt2);
		case Tag.WHILE:
			While whilenode = new While();
			savedStmt = Stmt.Enclosing;
			Stmt.Enclosing = whilenode;
			match(Tag.WHILE);
			match('(');
			expr = bool();
			match(')');
			stmt1 = stmt();
			whilenode.init(expr, stmt1);
			Stmt.Enclosing = savedStmt;
			return whilenode;
		case Tag.DO:
			Do donode = new Do();
			savedStmt = Stmt.Enclosing;
			Stmt.Enclosing = donode;
			match(Tag.DO);
			stmt1 = stmt();
			match(Tag.WHILE);
			match('(');
			expr = bool();
			match(')');
			match(';');
			donode.init(stmt1, expr);
			Stmt.Enclosing = savedStmt;
			return donode;
		case Tag.BREAK:
			match(Tag.BREAK);
			match(';');
			return new Break();
		case '{':
			return block();
		default:
			return assign();
		}
	}

	private Stmt assign() throws IOException {
		Stmt stmt;
		Token t = look;
		match(Tag.ID);
		Id id = top.get(t);
		if (id == null)
			error(t.toString() + "uncleared");
		if (look.Tag == '=') {
			move();
			stmt = new Set(id, bool());
		} else {
			Access x = offset(id);
			match('=');
			stmt = new SetElem(x, bool());

		}
		match(';');
		return stmt;
	}

	private Expr bool() throws IOException {
		Expr x = join();
		while (look.Tag == Tag.OR) {
			Token tok = look;
			move();
			x = new Or(tok, x, join());

		}
		return x;
	}

	private Expr join() throws IOException {
		Expr x = equilty();
		while (look.Tag == Tag.AND) {
			Token tok = look;
			move();
			x = new And(tok, x, equilty());

		}
		return x;
	}

	private Expr equilty() throws IOException {
		Expr x = rel();
		while (look.Tag == Tag.EQ || look.Tag == Tag.NE) {
			Token tok = look;
			move();
			x = new Rel(tok, x, rel());
		}
		return x;
	}

	private Expr rel() throws IOException {
		Expr x = expr();
		switch (look.Tag) {
		case '<':
		case Tag.NE:
		case Tag.GE:
		case '>':
			Token tok = look;
			move();
			return new Rel(tok, x, expr());

		default:
			return x;
		}
	}

	private Expr expr() throws IOException {
		Expr x = term();
		while (look.Tag == '+' || look.Tag == '-') {
			Token tok = look;
			move();
			x = new Arith(tok, x, term());

		}
		return x;
	}

	private Expr term() throws IOException {
		Expr x = unary();
		while (look.Tag == '*' || look.Tag == '/') {
			Token tok = look;
			move();
			x = new Arith(tok, x, unary());

		}
		return x;
	}

	private Expr unary() throws IOException {
		if (look.Tag == '-') {
			move();
			return new Unary(Word.minus, unary());
		} else if (look.Tag == '!') {
			Token tok = look;
			move();
			return new Not(tok, unary());
		} else {
			return factor();
		}
	}

	private Expr factor() throws IOException {
		Expr x = null;
		switch (look.Tag) {
		case '(':
			move();
			x = bool();
			match(')');
			return x;
		case Tag.NUM:
			x = new Constant(look, Type.Int);
			move();
			return x;
		case Tag.REAL:
			x = new Constant(look, Type.Float);
			move();
			return x;
		case Tag.TRUE:
			x = Constant.True;
			move();
			return x;
		case Tag.FALSE:
			x = Constant.False;
			move();
			return x;

		default:
			error("syntax error");
			return x;
		case Tag.ID:
			String s = look.toString();
			Id id = top.get(look);
			if (id == null)
				error(look.toString() + "uncleared");
			move();
			if (look.Tag != '[')
				return id;
			else
				return offset(id);
		}
	}

	private Access offset(Id a) throws IOException {
		Expr i, w, t1, t2, loc;
		Type type = a.type;
		match('[');
		i = bool();
		match(']');
		type = ((Array) type).of;
		w = new Constant(type.width);
		t1 = new Arith(new Token('*'), i, w);
		loc = t1;
		while (look.Tag == '[') {
			match('[');
			i = bool();
			match(']');
			type = ((Array) type).of;
			w = new Constant(type.width);
			t1 = new Arith(new Token('*'), i, w);
			t2 = new Arith(new Token('+'), loc, t1);
			loc = t2;
		}
		return new Access(a, loc, type);
	}

	private void decls() throws IOException {
		while (look.Tag == Tag.BASIC) {
			Type p = type();
			Token tok = look;
			match(Tag.ID);
			match(';');
			Id id = new Id((Word) tok, p, used);
			top.put(tok, id);
			used = used + p.width;
		}

	}

	private Type type() throws IOException {
		Type p = (Type) look;
		match(Tag.BASIC);
		if (look.Tag != '[')
			return p;
		else
			return dim(p);
	}

	private Type dim(Type p) throws IOException {
		match('[');
		Token tok = look;
		match(Tag.NUM);
		match(']');
		if (look.Tag == '[')
			p = dim(p);
		return new Array(((Num) tok).value, p);
	}
}
