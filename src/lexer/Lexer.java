package lexer;

import java.io.IOException;
import java.util.Hashtable;

import com.symbols.Type;

public class Lexer {

	public static int line = 1;
	public char peek = ' ';
	Hashtable word = new Hashtable();

	void reverve(Word w) {
		word.put(w.lexeme, w);
	}

	public Lexer(){
		
		reverve(new Word("if", Tag.IF));
		reverve(new Word("else", Tag.ELSE));
		reverve(new Word("while", Tag.WHILE));
		reverve(new Word("do", Tag.DO));
		reverve(new Word("break", Tag.BREAK));
		reverve(Word.True);reverve(Word.False);
		reverve(Type.Int);reverve(Type.Char);
		reverve(Type.Bool);reverve(Type.Float);
	}

	void readch() throws IOException {
		peek = (char) System.in.read();
	}

	boolean readch(char c) throws IOException {
		readch();
		if (peek != c)
			return false;
		peek = ' ';
		return true;

	}

	public Token scan() throws IOException {
		for (;; readch()) {
			if (peek == ' ' || peek == '\t') {
				continue;
			} else if (peek == '\n')
				line = line + 1;
			else
				break;
		}
		switch (peek) {
		case '&':
			if (peek == '&')
				return Word.and;
			else
				return new Token('&');

		case '|':
			if (peek == '|')
				return Word.or;
			else
				return new Token('|');
		case '=':
			if (peek == '=')
				return Word.eq;
			else
				return new Token('=');
		case '!':
			if (peek == '=')
				return Word.ne;
			else
				return new Token('=');
		case '<':
			if (peek == '=')
				return Word.le;
			else
				return new Token('<');
		case '>':
			if (peek == '=')
				return Word.ge;
			else
				return new Token('>');

		}
		if (Character.isDigit(peek)) {
			int v = 0;
			do {
				v = 10 * v + Character.digit(peek, 10);
				readch();
			} while (Character.isDigit(peek));
			if (peek != '.')
				return new Num(v);
			float x = v;
			float d = 10;
			for (;;) {
				readch();
				if (!Character.isDigit(peek))
					break;
				x = x + Character.digit(peek, 10) / d;
				d *= 10;

			}
			return new Real(x);

		}
		if (Character.isLetter(peek)) {
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				readch();
			} while (Character.isLetterOrDigit(peek));
			String s = b.toString();
			Word w = (Word) word.get(s);
			if (w != null)
				return w;
			w = new Word(s, Tag.ID);
			word.put(s, w);
			return w;
		}
		Token k = new Token(peek);
		peek = ' ';
		return k;
	}

}
