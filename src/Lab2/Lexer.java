package Lab2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//词法分析器
public class Lexer {
	private String text;
	private ArrayList<Token> list;
	public Lexer(String text, ArrayList<Token> list){
		this.text = text;
		this.list = list;
	}
	public static int symbol_pos = 0;
	//符号表
	public static Map<String, Integer> symbol = new HashMap<String, Integer>();
	public static int constant_pos = 0;
	//常量表
	public static Map<String, Integer> constant = new HashMap<String, Integer>();
	
	public ArrayList<Token> getList() {
		return this.list;
	}
	
	public void lex(){
		String[] texts = text.split("\n");
		//全部清空归零
		symbol.clear();
		symbol_pos = 0;
		constant.clear();
		constant_pos = 0;
		//开始词法识别并记录行号
		for(int m = 0; m < texts.length; m++){
			String str = texts[m];
			//空行跳过
			if (str.equals("")) continue;
			char[] strline = str.toCharArray();
			for(int i = 0; i < strline.length; i++) {
				char ch = strline[i];
				char ch0 = ' ';
				if(i > 1) ch0 = strline[i-1];
				//遇到空格视为新的串
				if (ch == ' ') continue;
				String token = "";
				//标识符和关键字
				if (TokenRec.isAlpha(ch)){
                    while (ch != '\0' && (TokenRec.isAlpha(ch) || TokenRec.isDigit(ch))){
                        token += ch;  
                        i++;  
                        if(i >= strline.length) break;  
                        ch = strline[i];  
                    }
                    i--;
                    //关键字
                    if (TokenRec.isKeyword(token)){  
                        list.add(new Token(m+1, token, TokenRec.keywords_code.get(token)));
                    }else{
                    	//标识符
                    	if (symbol.isEmpty() || (!symbol.isEmpty() && !symbol.containsKey(token))){
                            symbol.put(token, symbol_pos);   
                            symbol_pos++;
                        }
                    	list.add(new Token(m+1, token, 1));
                    }
                    token = "";
                //数字
                }else if(TokenRec.isDigit(ch)){
					int state = 1;
					int k;
					Boolean haveMistake = false;
					//八进制Octal
					Boolean isOctNum = false;
					//十六进制Hexadecimal
					Boolean isHexNum = false;
					//浮点数
                    Boolean isfloat = false;
                    //科学计数
                    Boolean isSciNum = false;
                    while ( (ch != '\0') && (TokenRec.isHexDigit(ch) || ch == '.' || ch == 'e' 
                    		|| ch == '-' || ch == 'E' || ch == '+')){
                    	if (isOctNum && !TokenRec.isOctDigit(ch) && ch != '0')
                    		haveMistake = true;
                    	if (!isHexNum && !TokenRec.isDigit(ch) 
                    			&& ch != 'e' && ch != 'E' && ch != '+' && ch != '-')
                    		haveMistake = true;
                    	if (ch == '.') {
                    		isfloat = true;
                    		haveMistake = false;
                    	}
                    	if ((ch == 'e' || ch == 'E') && !isOctNum && !isHexNum){
                    		isfloat = false;
                    		isSciNum = true;
                    		haveMistake = false;
                    	}
                    	if ((TokenRec.isPlusEqu(ch0)||ch0==' ')&& ch == '0') {
                    		isfloat = false;
                    		isSciNum = false;
                    		isOctNum = true;
                    		haveMistake = false;
                    	}
                    	if (isOctNum && (ch == 'x' || ch == 'X')) {
                    		isfloat = false;
                    		isSciNum = false;
                    		isOctNum = false;
                    		isHexNum = true;
                    		haveMistake = false;
                    	}
                    	//System.out.println(TokenRec.digitCheck[state]);
                        for (k = 0; k <= 6; k++){
                            char tmpstr[] = TokenRec.digitCheck[state].toCharArray();
                            if (ch != '#' && TokenRec.is_digit_state(ch, tmpstr[k], isHexNum) == 1){
                                token += ch;
                                state = k;
                                break;
                            }
                        }
                        if (k > 6) break;
                        i++;
                        if (i >= strline.length) break;
                        ch = strline[i];
                    }
                    
                    if (state == 2 || state == 4 || state == 5){  
                        haveMistake = true;  
                    }else{  
                        if ((ch == '.') || (!TokenRec.isOperator(String.valueOf(ch))//返回字符串
                        		&& !TokenRec.isDigit(ch) 
                        		&& !TokenRec.isDelimiter(String.valueOf(ch))
                        		&& ch != ' ')) 
                            haveMistake = true;  
                    }  
                    if (haveMistake){  
                    	while (ch != '\0' && ch != ',' && ch != ';' && ch != ' '){  
                            token += ch;  
                            i++;
                            if (i >= strline.length) break;  
                            ch = strline[i];
                            if(i > 1) ch0 = strline[i-1];
                        }
                    	list.add(new Token(m+1, token, 10000));
                    }else{  
                    	if (constant.isEmpty() 
                    			|| (!constant.isEmpty() && !constant.containsKey(token))){  
                    		constant.put(token, constant_pos);   
                            constant_pos++;
                        }
                    	if (isSciNum) list.add(new Token(m+1, token, 3));
                        else if (isfloat) list.add(new Token(m+1, token, 3));
                        else if (isOctNum) list.add(new Token(m+1, token, 3));
                        else if (isHexNum) list.add(new Token(m+1, token, 3));
                        else list.add(new Token(m+1, token, 2));
                    }
                    i--;
                    token = "";
                //字符
                }else if(ch == '\''){
					int state = 0;				        
                    token += ch;                    
                    while (state != 3){  
                        i++;
                        if (i >= strline.length) break;
                        ch = strline[i]; 
                        Boolean flag = false;
                        for (int k = 0; k < 4; k++){  
                            char tmpstr[] = TokenRec.charCheck[state].toCharArray();  
                            if (TokenRec.is_char_state(ch, tmpstr[k])){            
                                token += ch;
                                state = k; 
                                flag = true;
                                break;  
                            }  
                        }  
                        if (flag == false) break;
                    }
                    if (state != 3){
                    	list.add(new Token(m+1, token, 1001));
                        i--;  
                    }else{  
                    	if (constant.isEmpty() 
                    			|| (!constant.isEmpty() && !constant.containsKey(token))){  
                    		constant.put(token, constant_pos);   
                            constant_pos++;
                        }
                    	list.add(new Token(m+1, token, 5));
                    }
                    token = "";
                //字符串
				}else if (ch == '"'){
					Boolean haveMistake = false;
					String str1 = "";  
					str1 += ch;  
                    int state = 0;  
                    while (state != 3){  
                        i++;                             
                        if (i>=strline.length-1){  
                            haveMistake = true;
                            break;  
                        }                              
                        ch = strline[i]; 
                        if (ch == '\0'){  
                            haveMistake = true;
                            break;  
                        }
                        for (int k = 0; k < 4; k++){  
                            char tmpstr[] = TokenRec.stringCheck[state].toCharArray();  
                            if (TokenRec.is_string_state(ch, tmpstr[k])){
                            	str1 += ch;  
                                if (k == 2 && state == 1){  
                                    if (TokenRec.isEsSt(ch)) token = token + '\\' + ch;  
                                    else token += ch;  
                                }else if (k != 3 && k != 1)  
                                    token += ch;  
                                state = k;  
                                break;  
                            }  
                        }  
                    }
                    if (haveMistake){
                    	list.add(new Token(m+1, str1, 1002));
                        i--;  
                    }else{  
                    	if (constant.isEmpty() 
                    			|| (!constant.isEmpty() && !constant.containsKey(token))){  
                    		constant.put(token, constant_pos);   
                            constant_pos++;
                        }
                    	list.add(new Token(m+1, str1, 6));
                    }  
                    token = "";
                //注释或者/=
				}else if (ch == '/'){
					token += ch;  
                    i++;
                    if (i>=strline.length) break;  
                    ch = strline[i];
                    if (ch != '*' && ch != '/'){  
                        if (ch == '=') token += ch;
                        else i--;
                        list.add(new Token(m+1, token, TokenRec.operator_code.get(token)));
                        token = "";  
                    }else{
                    	Boolean haveMistake = false;
                    	int State = 0;
                    	if (ch == '*'){
                    		token += ch;  
                            int state = 2;
                            while (state != 4){                                      
                                if (i == strline.length-1){  
                                	token += '\n';  
                                	m++;
                                	if (m >= texts.length){
                                		haveMistake = true;  
                                        break;  
                                	}
                            		str = texts[m];
                            		if (str.equals("")) continue;
                            		else{
                            			strline = str.toCharArray();
                            			i=0;
                            			ch = strline[i];
                            		}
                                }else{
                                	i++;
                                    ch = strline[i];
                                }
                                for (int k = 2; k <= 4; k++){  
                                    char tmpstr[] = TokenRec.noteCheck[state].toCharArray();  
                                    if (TokenRec.is_note_state(ch, tmpstr[k], state)){
                                        token += ch;  
                                        state = k;  
                                        break;  
                                    }  
                                }  
                            }
                            State = state;
                        }else if(ch == '/'){
                    		int index = str.lastIndexOf("//");
                            String tmpstr = str.substring(index);  
                            int tmpint = tmpstr.length();  
                            for(int k=0;k<tmpint;k++)                                     
                              i++;    
                            token = tmpstr;
                            State = 4;
                    	}
                    	if(haveMistake || State != 4){
                    		list.add(new Token(m+1, token, 1003));
                            --i;
                    	}else{
                    		//System.out.println(token);
                    		list.add(new Token(m+1, token, 7));
                    	}
                    	token = "";
                    }
                //运算符或者分隔符
				}else if (TokenRec.isOperator(String.valueOf(ch)) 
						|| TokenRec.isDelimiter(String.valueOf(ch))){  
					token += ch; 						
                    if (TokenRec.isPlusEqu(ch)){  
                        i++;
                        if (i>=strline.length) break;  
                        ch = strline[i];  
                        if (ch == '=') token += ch;  
                        else i--;
                    }                  
                    if(token.length() == 1){
                    	String signal = token;
                    	if(TokenRec.isDelimiter(signal)) list.add(new Token(m+1, token, TokenRec.delimiter_code.get(token)));
                    	else list.add(new Token(m+1, token, TokenRec.operator_code.get(token)));
                    }else list.add(new Token(m+1, token, TokenRec.operator_code.get(token)));
                    token = "";
                //未知符号
                }else{  
                    if(ch != ' ' && ch != '\t' && ch != '\0' && ch != '\n' && ch != '\r'){
                    	token += ch;
                    	list.add(new Token(m+1, token, 1004));
                        token = "";
                    }  
                }				
			}
		}
    }
}

