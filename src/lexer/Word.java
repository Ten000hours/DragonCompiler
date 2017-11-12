package lexer;

public class Word extends Token {

	public String lexeme="";
	public Word(String s,int tag){super(tag);lexeme=s;}
	public String toString(){return lexeme;}
	public static final Word and=new Word("&&",lexer.Tag.AND);
	public static final Word or=new Word("||", lexer.Tag.OR);
	public static final Word eq =new Word("==",lexer.Tag.EQ),ne=new Word("!=",lexer.Tag.NE),
			le=new Word("<=", lexer.Tag.LE),ge=new Word(">=", lexer.Tag.GE),minus=new Word("minus", lexer.Tag.MINUS),
			True=new Word("true", lexer.Tag.TRUE),False=new Word("false", lexer.Tag.FALSE),temp=new Word("temp", lexer.Tag.TEMP);
	
}
