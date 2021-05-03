package Lab2;

import java.util.HashMap;
import java.util.Map;

public class TokenRec {
	//关键词
	private static String keywords[] = {
			"proc", "record", "integer", "real",  "and", "or", "not",
	        "if", "else", "then", "while", "do", "call", "true", "false"};
	public static Map<String, Integer> keywords_code = new HashMap<String, Integer>(){
		private static final long serialVersionUID = 1L;{
			for (int i = 0; i < keywords.length; i++)
				put(keywords[i], i + 101);
		}	
	};	
	public static Boolean isKeyword(String s){
        return keywords_code.containsKey(s);  
    }
	//运算符
	private static String operator[] = {"+", "-", "*", "<", "<=", ">", ">=", "==", "!="};
	@SuppressWarnings("serial")
	public static Map<String, Integer> operator_code = new HashMap<String, Integer>(){
		{
			for (int i = 0; i < operator.length; i++)
				put(operator[i], i + 201);
		}	
	};
	public static Boolean isOperator(String s) {  
		return operator_code.containsKey(s);
    }
	//界符
	private static String delimiter[] = {",", ";", "[", "]", "(", ")","="};
	@SuppressWarnings("serial")
	public static Map<String, Integer> delimiter_code = new HashMap<String, Integer>(){
		{
			for (int i = 0; i < delimiter.length; i++)
				put(delimiter[i], i + 301);
		}
	};
	public static Boolean isDelimiter(String s) {  
		return delimiter_code.containsKey(s);
    }
	public String toUpper(String s){
		return s.toUpperCase();
	}
	public static String getName(String s){
		if(s.equals(","))
			return "COL";
		if(s.equals(";"))
			return "SEMI";
		if(s.equals("["))
			return "MLP";
		if(s.equals("]"))
			return "MRP";
		if(s.equals("{"))
			return "LP";
		if(s.equals("}"))
			return "RP";
		if(s.equals("("))
			return "SLP";
		if(s.equals(")"))
			return "SRP";
		if(s.equals("="))
			return "EQU";
		if(s.equals("."))
			return "DOT";
		return "ERROR";
	}
	
	//识别不同类型字符
	//可跟=
	public static Boolean isPlusEqu(char ch){  
        return ch == '=' || ch == '>' || ch == '<' || ch == '!' ;  
    }
	/*
	//可跟相同运算符
	public static Boolean isPlusSame(char ch){  
        return ch == '+' || ch == '-' || ch == '&' || ch == '|' || ch == '>' || ch == '<';  
    }*/
	public static Boolean isAlpha(char ch){
	    return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_');
	}
	public static Boolean isDigit(char ch){
        return (ch >= '0' && ch <= '9');
    }
	public static Boolean isOctDigit(char ch){
        return (ch >= '0' && ch <= '7');
    }
	public static Boolean isHexDigit(char ch){
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') 
        		||(ch >= 'A' && ch <= 'F') || ch == 'x' || ch == 'X';
    }
	
	// String Check : a代表任意字符，b代表除\和"之外的字符
	public static String stringCheck[] = {"#\\b#", "##a#", "#\\b\"", "####"};
	public static Boolean is_string_state(char ch, char key){  
        if (key == 'a') return true;  
        if (key == '\\') return ch == key;  
        if (key == '"') return ch == key;  
        if (key == 'b') return ch != '\\' && ch != '"';  
        return false;  
    }
	
	// char Check : a代表任意字符，b代表除\和'之外的字符
	public static String charCheck[] = {"#\\b#", "##a#", "###\'","####"}; 
	public static Boolean is_char_state(char ch, char key){  
        if (key == 'a') return true;  
        if (key == '\\') return ch == key;  
        if (key == '\'') return ch == key;  
        if (key == 'b') return ch != '\\' && ch != '\'';  
        return false;  
    }
	public static Boolean isEsSt(char ch){  
        return ch == 'a' || ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r'  
                || ch == 't' || ch == 'v' || ch == '?' || ch == '0';  
    }
	
	//识别数字的Check : d表示对应的十进制十六进制数字
	public static String digitCheck[] = {"#d#####", "#d.#e##", "###d###", "###de##", "#####-d", "######d", "######d"};
	public static int is_digit_state(char ch, char test, Boolean isHex) { 
        if (test == 'd')
        	if (isHex && isHexDigit(ch)) return 1; 
        	else if (isDigit(ch)) return 1;  
            else return 0;  
        else if (test == '-')
        	if (ch == '-' || ch == '+') return 1;
        	else return 0;
        else if (test == 'e')
        	if (ch == 'e' || ch == 'E') return 1;
        	else return 0;
        else
        	if (ch == test) return 1;
        	else return 0;
    }
	
	//识别注释的Check : c表示任意字符
	public static String noteCheck[] = {"#/###", "##*##", "##c*#", "##c*/", "#####"};
	public static Boolean is_note_state(char ch, char nD, int s){  
        if (s == 2)
            if (nD == 'c')
                if (ch != '*') return true;  
                else return false;
        if (s == 3)
            if (nD == 'c')
                if (ch != '*' && ch != '/') return true;
                else return false;
        return (ch == nD) ? true : false;  
    }
}
