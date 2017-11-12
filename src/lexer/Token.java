package lexer;

public class Token {

	public final int Tag;
	public Token(int t){this.Tag=t;}
	@Override
	public String toString() {
		return ""+ (char)this.Tag;
	}
}
