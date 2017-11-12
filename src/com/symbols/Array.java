package com.symbols;

public class Array extends Type{
 
	public Type of;
	public int size=1;
	public Array(int size,Type p){
		super("[]",lexer.Tag.INDEX,size*p.width);
		size=this.size;
		of=p;
	}
	@Override
	public String toString() {
		return "["+size+"]"+of.toString();
	}
}
