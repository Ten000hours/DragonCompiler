package com.main;

import java.io.IOException;

import com.Parser.Parser;

import lexer.Lexer;

public class Main {
 
	public static void main(String[] args) throws IOException {
		Lexer lex=new Lexer();
		Parser parse=new Parser(lex);
		parse.program();
		System.out.println("\n");
	}
}
