package Lab1;

import java.util.HashMap;
import java.util.Map;

//词法分析器
public class Lexer {
	private String text;
	private MyList list;
	public Lexer(String text, MyList list){
		this.text = text;
		this.list = list;
	}
	public static int symbol_pos = 0;
	//符号表
	public static Map<String, Integer> symbol = new HashMap<String, Integer>();
	public static int constant_pos = 0;
	//常量表
	public static Map<String, Integer> constant = new HashMap<String, Integer>();
	
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
			if (str.equals(""))
				continue;
			char[] strline = str.toCharArray();
			for(int i = 0; i < strline.length; i++) {
				char ch = strline[i];
				//遇到空格视为新的串
				if (ch == ' ')
					continue;
				String token = "";
				if (TokenRec.isAlpha(ch)){
                    while (ch != '\0'
                    		&& (TokenRec.isAlpha(ch) || TokenRec.isDigit(ch))){
                        token += ch;  
                        i++;  
                        if(i >= strline.length) 
                        	break;  
                        ch = strline[i];  
                    }
                    i--;
                    //关键词
                    if (TokenRec.isKeyword(token)){  
                        list.insert(m+1, token, token.toUpperCase(), "-");
                    }else{
                    	if (symbol.isEmpty() 
                    			|| (!symbol.isEmpty() && !symbol.containsKey(token))){
                            symbol.put(token, symbol_pos);   
                            symbol_pos++;
                        }
                    	list.insert(m+1, token, "IDN", token);
                    }
                    token = "";
                }else if(TokenRec.isDigit(ch)){
					int state = 1;
					int k;
					//浮点数
                    Boolean isfloat = false;
                    //科学计数
                    Boolean isSciNum = false;
                    while ( (ch != '\0') && (TokenRec.isDigit(ch) || ch == '.' || ch == 'e' 
                    		|| ch == '-' || ch == 'E' || ch == '+')){
                    	if (ch == '.') 
                    		isfloat = true;
                    	if (ch == 'e' || ch == 'E'){
                    		isfloat = false;
                    		isSciNum = true;
                    	}

                        for (k = 0; k <= 6; k++){
                            char tmpstr[] = TokenRec.digitDFA[state].toCharArray();
                            if (ch != '#' && TokenRec.is_digit_state(ch, tmpstr[k]) == 1){
                                token += ch;
                                state = k;
                                break;
                            }
                        }
                        if (k > 6)
                        	break;
                        i++;
                        if (i >= strline.length)
                        	break;
                        ch = strline[i];
                    }
                    Boolean haveMistake = false;  
                    if (state == 2 || state == 4 || state == 5){  
                        haveMistake = true;  
                    }else{  
                        if ((ch == '.') || (!TokenRec.isOperator(String.valueOf(ch)) 
                        		&& !TokenRec.isDigit(ch) 
                        		&& !TokenRec.isDelimiter(String.valueOf(ch))
                        		&& ch != ' ')) 
                            haveMistake = true;  
                    }  
                    if (haveMistake){  
                    	while (ch != '\0' && ch != ',' && ch != ';' && ch != ' '){  
                            token += ch;  
                            i++;
                            if (i >= strline.length) 
                            	break;  
                            ch = strline[i];  
                        }
                    	list.insert(m+1, token, "error", "ERROR");
                    }else{  
                    	if (constant.isEmpty() 
                    			|| (!constant.isEmpty() && !constant.containsKey(token))){  
                    		constant.put(token, constant_pos);   
                            constant_pos++;
                        }
                    	if (isSciNum){
                    		list.insert(m+1, token, "SCONST", token);
                        }else if (isfloat){
                        	list.insert(m+1, token, "FCONST", token);
                        }else{
                        	list.insert(m+1, token, "CONST", token);
                        }  
                    }
                    i--;
                    token = "";
                //字符
                }else if(ch == '\''){
					int state = 0;				        
                    token += ch;                    
                    while (state != 3){  
                        i++;
                        if (i >= strline.length) 
                        	break;
                        ch = strline[i]; 
                        Boolean flag = false;
                        for (int k = 0; k < 4; k++){  
                            char tmpstr[] = TokenRec.charDFA[state].toCharArray();  
                            if (TokenRec.is_char_state(ch, tmpstr[k])){            
                                token += ch;
                                state = k; 
                                flag = true;
                                break;  
                            }  
                        }  
                        if (flag == false)
                        	break;
                    }if (state != 3){
                    	list.insert(m+1, token, "String error","ERROR");
                        i--;  
                    }else{  
                    	if (constant.isEmpty() 
                    			|| (!constant.isEmpty() && !constant.containsKey(token))){  
                    		constant.put(token, constant_pos);   
                            constant_pos++;
                        }
                    	list.insert(m+1, token, "CCONST", token);
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
                            char tmpstr[] = TokenRec.stringDFA[state].toCharArray();  
                            if (TokenRec.is_string_state(ch, tmpstr[k])){
                            	str1 += ch;  
                                if (k == 2 && state == 1){  
                                    if (TokenRec.isEsSt(ch))
                                        token = token + '\\' + ch;  
                                    else  
                                        token += ch;  
                                }else if (k != 3 && k != 1)  
                                    token += ch;  
                                state = k;  
                                break;  
                            }  
                        }  
                    }
                    if (haveMistake){
                    	list.insert(m+1, str1, "String error", "-");
                        i--;  
                    }else{  
                    	if (constant.isEmpty() 
                    			|| (!constant.isEmpty() && !constant.containsKey(token))){  
                    		constant.put(token, constant_pos);   
                            constant_pos++;
                        }
                    	list.insert(m+1, str1, "STRCONST", str1);
                    }  
                    token = "";
                //注释
				}else if (ch == '/'){
					token += ch;  
                    i++;
                    if (i>=strline.length) 
                    	break;  
                    ch = strline[i];
                    
                    if (ch != '*' && ch != '/'){  
                        if (ch == '=')  
                            token += ch;
                        else
                            i--;
                        list.insert(m+1, token, "OP", token);
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
                            		if (str.equals(""))
                            			continue;
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
                                    char tmpstr[] = TokenRec.noteDFA[state].toCharArray();  
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
                    		list.insert(m+1, token, "Note error","ERROR");
                            --i;
                    	}else{
                    		list.insert(m+1, token, "Note","-");
                    	}
                    	token = "";
                    }
                //运算符
				}else if (TokenRec.isOperator(String.valueOf(ch)) 
						|| TokenRec.isDelimiter(String.valueOf(ch))){  
					token += ch; 						
                    if (TokenRec.isPlusEqu(ch)){  
                        i++;
                        if (i>=strline.length) 
                        	break;  
                        ch = strline[i];  
                        if (ch == '=')  
                            token += ch;  
                        else{                              	
                        	if (TokenRec.isPlusSame(strline[i-1]) && ch == strline[i-1])
                                token += ch;  
                            else  
                                i--;   
                        }  
                    }                  
                    if(token.length() == 1){
                    	String signal = token;
                    	if(TokenRec.isDelimiter(signal))
                    		list.insert(m+1, token,TokenRec.getName(token), "-");
                    	else
                    		list.insert(m+1, token, "OP", token);
                    }else list.insert(m+1, token, "OP", token);
                    token = "";
                //未知符号
                }else{  
                    if(ch != ' ' && ch != '\t' && ch != '\0' && ch != '\n' && ch != '\r'){
                    	list.insert(m+1, token, "Unknown char", "-");
                        System.out.println(ch);
                    }  
                }				
			}
		}
    }
}

