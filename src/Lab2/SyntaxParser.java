package Lab2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


public class SyntaxParser{
	private Lexer lex;
	private ArrayList<Token> tokenList = new ArrayList<Token>();
	private int length;
	private int index;
	private LRTable table;
	private Stack<Integer> stateStack;
	private Stack<Token> tokenStack;
	private static List<String> tree = new ArrayList<String>();
	private static List<String> errors = new ArrayList<String>();
	public static int indent = 0;
	
	public SyntaxParser(String filename, List<String> tree, List<String> errors,int cached){
		File file = new File(filename);
		String text = "";
		try{
			InputStream in = new FileInputStream(file);
			int tempbyte;
			while ((tempbyte=in.read()) != -1){
				text += ("" + (char)tempbyte);
			}
			in.close();
		}catch(Exception event){
			event.printStackTrace();
		}
		this.lex = new Lexer(text, this.tokenList);
		this.lex.lex();
		int last = tokenList.get(tokenList.size() - 1).line + 1;
		/*
		for(Token token : this.tokenList) {
			System.out.print(token.value + "\n");
		}
		*/
		//最后一行加入#,表示结束
		this.tokenList.add(new Token(last, "#",-1));
		this.length = this.tokenList.size();
		this.index = 0;
		if(cached == 0){
			//生成分析表
			this.table = new LRTable();
			TableIO.saveObjToFile(this.table);
		}else{
		    this.table = TableIO.getObjFromFile();
		}
		this.stateStack = new Stack<Integer>();  // 状态栈
		this.stateStack.push(0);  // 初始为0状态
		this.tokenStack = new Stack<Token>();
		this.tokenStack.push(new Token(-1, "#", -1));
		// 写入Sets.txt"
		this.table.dfa.writefile();
		// 写入文件"Table.txt"
		this.table.print();
		SyntaxParser.tree = tree;
		SyntaxParser.errors = errors;
		Token root = analyze();
        printTree(root);
	}
	
	private void printTree(Token root){
		String output = "";
		for(int i=1;i<=indent;i++)
			output+="  ";
		if(root.code==1)
			tree.add(output+"id: "+root.value+" ("+root.line+")");
		else if(root.code==2||root.code==3)
			tree.add(output+root.type+": "+root.value+" ("+root.line+")");
		else
		    tree.add(output+root.value+" ("+root.line+")");
		indent+=2;
		Collections.reverse(root.children);
		for (Token child:root.children)
			printTree(child);
		indent-=2;
	}
	
	public Token readToken(){
		if(index < length)
			return tokenList.get(index++);
		else
			return null;
	}
	
	private String getValue(Token valueType, String typeBefore){
		// 获取value值，要把所有类型的数字都转换为num
		//System.out.println(valueType);
		try{
			int code = valueType.code;
			if(code == 1)
				return "ID";
			else if(code == 2)
				return "INT";
				//System.out.println(typeBefore);
				/*
				if(typeBefore.equals("ID")) return "INT";
				else return "num";
				*/
			else if(code == 3)
				return "FLOAT";
			else if(code == 7)
				return " ";
			else if(code < 400 && code >=101)
				return valueType.value;
			else if(valueType.value.equals("#"))
				return "#";
			else
				return " ";
		}catch(Exception NullPointerException){
			return "";
		}
	}
	
	public Token analyze(){
		String typeBefore = " ";
		while(true){			
			Token token = readToken();
			String value = getValue(token, typeBefore);
			//if(value.equals("ID") || value.equals("int") || value.equals("real"))
				//typeBefore = value;
			//System.out.println(typeBefore);
			if(value.equals(" "))
				continue;
			int state = stateStack.lastElement();
			//查action表
			String action = table.ACTION(state, value);
			//System.out.println(action);
			if(action.startsWith("s")){
				int newState = Integer.parseInt(action.substring(1));
				token.type = value;
				stateStack.push(newState);
				tokenStack.push(token);
			}else if(action.startsWith("r")){
				Production derivation = Pretreat.F.get(Integer.parseInt(action.substring(1)));
				//查找对应的产生式，产生式类型由左部和右部构成
				//System.out.println(derivation);
				int r = derivation.list.size();
				index--;
				Token temptoken = new Token(token.line,derivation.left,-10);
				if(!derivation.list.get(0).equals("ε")){
					for(int i = 0; i < r; i++){
						stateStack.pop();
						Token tobeinserted = tokenStack.pop();
						if(tobeinserted.line < temptoken.line)
							temptoken.line = tobeinserted.line;
						temptoken.children.add(tobeinserted);
					}
				}
				int s = table.GOTO(stateStack.lastElement(), derivation.left);
				stateStack.push(s);
				tokenStack.push(temptoken);
			}else if(action.equals("acc")){
				//System.out.print("Accepted"+"\n");
				//返回语法树的根节点
				return tokenStack.get(1);
			}else{
				error();
				while(action.startsWith("r")){
					index = index - 1;
					Token token1 = readToken();
					tokenList.remove(token1);
					index = index - 1;
					String value1 = getValue(token1, typeBefore);
					//if(value1.equals("ID") || value1.equals("int") || value1.equals("real"))
						//typeBefore = value1;
					stateStack.pop();
					tokenStack.pop();
					if(value.equals("")){
						error();
						continue;
					}
					if(value.equals(" "))
						continue;
					int state1 = stateStack.lastElement();
					action = table.ACTION(state1, value1);				
				}
			}	
		}
	}

	//出错
	public void error(){
		if(tokenList.get(index-1).value.equals("#")) return;
		String s = "Syntax error at Line[" + tokenList.get(index-1).line + "]:  \""+
				tokenList.get(index-1).value + "\"";
		errors.add(s);
		System.out.println(s);
	}
	
	//输出结果
	public void writeToFile(){
        String path = "parser.out";
        try{
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for(String line : tree) {
    			bw.write(line + "\n");
    		}
            
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        
        String path2 = "parser_error.out";
        try{
            File file2 = new File(path2);
            FileWriter fw2 = new FileWriter(file2);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            for(String line : errors) {
    			bw2.write(line + "\n");
    		}
            
            bw2.close();
        }catch (IOException e){
            e.printStackTrace();
        }
	}
}
