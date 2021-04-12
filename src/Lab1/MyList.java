package Lab1;

import java.util.ArrayList;
import java.util.List;

public class MyList {
	private List<Token> tokenList = new ArrayList<Token>();
	private int len = 0;
	
	public MyList() {}
	public MyList(Token token) {
		this.tokenList.add(token);
		this.len ++;
	}
	
	public int getLen() {
		return this.len;
	}
	
	public boolean isEmpty() {
		return this.len == 0;
	}
	
	public boolean insert(Token token) {
		this.len ++;
		return this.tokenList.add(token);
	}
	
	public boolean insert(int line, String str1, String str2, String str3) {
		this.len ++;
		return this.tokenList.add(new Token(line, str1, str2, str3));
	}
	
	public void print() {
		if(isEmpty()){
            System.out.print("Empty");
            return;
        }
		System.out.println(this.len);
		for(Token token : this.tokenList) {
			token.print();
		}
	}
}
