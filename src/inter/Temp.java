package inter;

import lexer.Word;

import com.symbols.Type;

public class Temp extends Op{

	static int count=0;
	int number =0;
	public Temp(Type p){
		super(Word.temp,p);
		number=++count;
	}
	@Override
	public String toString() {
		return "t"+number;
	}
}
