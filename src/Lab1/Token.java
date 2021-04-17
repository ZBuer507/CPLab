package Lab1;

import java.lang.String;

//”√¿¥¥Ê¥¢token
public class Token {
	public int line;
	public String str1;
	public String str2;
	public String str3;
	
	public Token() {
		this.line = 0;
		this.str1 = "";
		this.str2 = "";
		this.str3 = "";
	}
	public Token(int line, String str1, String str2, String str3) {
		this.line = line;
		this.str1 = str1;
		this.str2 = str2;
		this.str3 = str3;
	}
	public String print() {
		String str;
		if(this.str3.equals("ERROR")) {
			str ="Lexical error at Line" + this.line + ":\"" + 
					this.str1.replace("\r\n", "\\r\\n") + "\" " + 
					this.str2.replace("\r\n", "\\r\\n") + "\n";
			System.out.print(str);
		}else {
			str ="Line" + this.line + ":\t" + 
				this.str1.replace("\r\n", "\\r\\n") + "\t<\t" + 
				this.str2.replace("\r\n", "\\r\\n") + "\t,\t" + 
				this.str3.replace("\r\n", "\\r\\n") + "\t>\n";
			//System.out.print(str);
		}
		return str;
	}
}
