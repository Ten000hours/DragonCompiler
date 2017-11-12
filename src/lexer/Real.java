package lexer;

public class Real extends Token {
 public final float value;
 public Real(float v){super(lexer.Tag.REAL);value=v;}
 public String toString(){return ""+value;}
}
