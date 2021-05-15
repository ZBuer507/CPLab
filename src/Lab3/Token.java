package Lab3;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

//”√¿¥¥Ê¥¢token
public class Token {
	public int line;
	public String value;
	public int code;
	public String type;
	public List<Token> children = new ArrayList<Token>();
	/*
	public Token() {
		this.line = 0;
		this.value = "";
		this.code = 0;
	}*/
	public Token(int line, String value, int code) {
		this.line = line;
		this.value = value;
		this.code = code;
	}
	public String toString(){
		return this.line + ":< " + this.value + " ," + this.code + " >";
	}
}
