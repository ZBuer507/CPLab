package Lab1;

import java.util.HashMap;
import java.util.Map;

public class TokenRec {
	//�ؼ���
	private static String keywords[] = {
			"auto", 	"double", 	"int", 		"struct",  	"enum",		"float",
	        "break", 	"else", 	"long", 	"switch", 	"case",  	"register",  
	        "typedef", 	"char", 	"extern", 	"return", 	"union", 	"const",   
	        "short", 	"unsigned",	"continue",	"for", 		"signed", 	"void",  
	        "default", 	"goto", 	"sizeof", 	"volatile", "do", 		"if", 
	        "while",  	"static", 	"String"};
	public static Map<String, Integer> keywords_code = new HashMap<String, Integer>(){
		private static final long serialVersionUID = 1L;{
			for (int i = 0; i < keywords.length; i++)
				put(keywords[i], i + 101);
		}	
	};	
	public static Boolean isKeyword(String s){
        return keywords_code.containsKey(s);  
    }
	//�����
	private static String operator[] = {"+", "-", "*", "/", "!", "%", "~", "&", "|", "^", "=",
			"++", "--", "&&", "||", "<=", "!=", "==", ">=", "+=", "-=", "*=", "/="};
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
	//���
	private static String delimiter[] = {",", "(", ")", "{", "}", ";", "<", ">", "#", "[", "]"};
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
		return "ERROR";
	}
	
	//ʶ��ͬ�����ַ�
	public static Boolean isPlusEqu(char ch){  
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '=' || ch == '>' 
        		|| ch == '<' || ch == '&' || ch == '|'  || ch == '^' || ch == '%' || ch == '!' ;  
    }
	public static Boolean isPlusSame(char ch){  
        return ch == '+' || ch == '-' || ch == '&' || ch == '|' || ch == '>' || ch == '<';  
    }
	public static Boolean isAlpha(char ch){
	    return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_');
	}
	public static Boolean isDigit(char ch){  
        return (ch >= '0' && ch <= '9');  
    }
	
	// String DFA : a���������ַ���b�����\��"֮����ַ�
	public static String stringDFA[] = { 
		"#\\b#",
		"##a#",
		"#\\b\"",
		"####"
	};
	public static Boolean is_string_state(char ch, char key){  
        if (key == 'a')  
            return true;  
        if (key == '\\')  
            return ch == key;  
        if (key == '"')  
            return ch == key;  
        if (key == 'b')  
            return ch != '\\' && ch != '"';  
        return false;  
    }
	
	// char DFA : a���������ַ���b�����\��'֮����ַ�
	public static String charDFA[] = {
		"#\\b#", 
		"##a#", 
		"###\'", 
		"####"
	}; 
	public static Boolean is_char_state(char ch, char key){  
        if (key == 'a')  
            return true;  
        if (key == '\\')  
            return ch == key;  
        if (key == '\'')  
            return ch == key;  
        if (key == 'b')  
            return ch != '\\' && ch != '\'';  
        return false;  
    }
	public static Boolean isEsSt(char ch){  
        return ch == 'a' || ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r'  
                || ch == 't' || ch == 'v' || ch == '?' || ch == '0';  
    }
	
	//ʶ�����ֵ�DFA
	public static String digitDFA[] = {
		"#d#####",
		"#d.#e##",
		"###d###",
		"###de##",
		"#####-d",
		"######d",
		"######d",
		"",
		"2 4 7",
	};
	public static int is_digit_state(char ch, char test) {  
        if (test == 'd') 
            if (isDigit(ch))  
                return 1;  
            else  
                return 0;  
        else if (test == '-')
        	if (ch == '-' || ch == '+')
        		return 1;
        	else
        		return 0;
        else if (test == 'e')
        	if (ch == 'e' || ch == 'E')
        		return 1;
        	else
        		return 0;
        else
        	if (ch == test)
        		return 1;
        	else
        		return 0;
    }
	
	//ʶ��ע�͵�DFA
	public static String noteDFA[] = {
			"#/###",
			"##*##",
			"##c*#",
			"##c*/",
			"#####",
			"",
			"5",
	};
	public static Boolean is_note_state(char ch, char nD, int s){  
        if (s == 2)
            if (nD == 'c')
                if (ch != '*') 
                	return true;  
                else 
                	return false;
        if (s == 3)
            if (nD == 'c')
                if (ch != '*' && ch != '/') 
                	return true;
                else 
                	return false;
        return (ch == nD) ? true : false;  
    }
}
