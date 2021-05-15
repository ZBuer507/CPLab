package Lab3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


public class SyntaxParser{
	private Lexer lex;
	private ArrayList<Token> tokenList = new ArrayList<Token>();
	private int length;
	private int index;
	private LR1 table;
	private Stack<Integer> stateStack;
	private Stack<Token> tokenStack;
	private static StringBuffer result = new StringBuffer();  // �����Լ���

	private static List<String> result2 = new ArrayList();  // �����Լ���
	private static List<String> errors = new ArrayList();  // �����Լ���
	
	public static ArrayList<Tree> tree = new ArrayList<Tree>();  // �﷨��
	private Stack<TreeNode> treeNodeID = new Stack<TreeNode>();  // ���ڴ洢��Ӧ�����ڵ�
	private int count;  // ��¼�ڵ��
	
	public SyntaxParser(String filename , int cached, ArrayList<Tree> tree){
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
		//���һ�м���#,��ʾ����
		this.tokenList.add(new Token(last, "#",-1));
		this.length = this.tokenList.size();
		this.index = 0;
		if(cached == 0){
			this.table = new LR1();
			ToFile.saveObjToFile(this.table);
		}else{
		    this.table = ToFile.getObjFromFile();
		}
		this.stateStack = new Stack<Integer>();
		this.stateStack.push(0);
		this.tokenStack = new Stack<Token>();
		this.tokenStack.push(new Token(-1, "#", -1));
		//this.table.dfa.writefile();
		//this.table.print();
		this.tree = tree;
		for(int i = 0;i < tokenList.size(); i++){
			result.append(tokenList.get(i).toString() + "\n");
		}
		analyze();
		writeToFile(result);
	}
	
	public Token readToken(){
		if(index < length)
			return tokenList.get(index++);
		else
			return null;
	}
	
	private String getValue(Token valueType){
		try{
			int code = valueType.code;
			if(code == 1)
				return "id";
			else if(code == 2)
				return "num";
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
	
	public void analyze(){
		result.delete(0,result.length()-1);  // �����Լ���
		result2.clear();  // �����Լ���
		errors.clear();  // �����Լ���
		tree.clear();  // �﷨��
		treeNodeID.clear();  // ���ڴ洢��Ӧ�����ڵ�
		
		int m = 0;
		int last=1;
		while(true){
			result.append("��ǰ������: ");
			printInput();
			result.append("\n\n");
			
			Token token = readToken();
			String value = getValue(token);
			if(value.equals("")){
				error();
				m++;
				if(m>50){
					System.out.println("�������ķ�");
					break;
				}
				continue;
			}else if(value.equals(" ")){
				continue;
			}
			int state = stateStack.lastElement();
			//��action��
			String action = table.ACTION(state, value);
			//System.out.println(action);
			if(action.startsWith("s")){
				int newState = Integer.parseInt(action.substring(1));
				token.type = value;
				stateStack.push(newState);
				tokenStack.push(token);
				result.append("����"+"\t");
				treeNodeID.push(new TreeNode(count++,value,token.value,last));  
			}else if(action.startsWith("r")){
				Production derivation = ProductionRec.F.get(Integer.parseInt(action.substring(1)));
				//���Ҷ�Ӧ�Ĳ���ʽ������ʽ�������󲿺��Ҳ�����
				//System.out.println(derivation);
				result.append(derivation + "\n");
				result2.add(derivation.toString());
				int r = derivation.list.size();
				index--;
				LinkedList<TreeNode> son = new LinkedList<TreeNode>();
				Token temptoken = new Token(token.line,derivation.left,-10);
				if(!derivation.list.get(0).equals("��")){
					for(int i = 0; i < r; i++){
						stateStack.pop();
						son.addFirst(treeNodeID.pop());
						Token tobeinserted = tokenStack.pop();
						if(tobeinserted.line < temptoken.line)
							temptoken.line = tobeinserted.line;
						temptoken.children.add(tobeinserted);
					}
				}
				int s = table.GOTO(stateStack.lastElement(), derivation.left);
				stateStack.push(s);
				tokenStack.push(temptoken);
				result.append("��Լ"+"\t");
				treeNodeID.push(new TreeNode(count++,derivation.left,"--",last)); 
				ArrayList<TreeNode> sonList = new ArrayList<TreeNode>(son);
				tree.add(new Tree(treeNodeID.peek(),sonList));
			}else if(action.equals("acc")){
				System.out.print("Accepted"+"\n");
				result.append("�﷨�������"+"\t");
				return;
				//�����﷨���ĸ��ڵ�
			}else{
				error();
				m++;
				if(m>50){
					System.out.println("�������ķ�");
					break;
				}
				while(action.startsWith("r")){
					index = index - 1;
					Token token1 = readToken();
					tokenList.remove(token1);
					index = index - 1;
					String value1 = getValue(token1);
					stateStack.pop();
					tokenStack.pop();
					if(value.equals("")){
						error();
						error();
						m++;
						if(m>50){
							System.out.println("�������ķ�");
							break;
						}
						continue;
					}
					if(value.equals(" "))
						continue;
					int state1 = stateStack.lastElement();
					action = table.ACTION(state1, value1);				
				}
			}
			if(m>50){
				System.out.println("�������ķ�");
				break;
			}
			last = token.line;
		}
	}

	//����
	public void error(){
		if(tokenList.get(index-1).value.equals("#")) return;
		String s = "Syntax error at Line[" + tokenList.get(index-1).line + "]:  \""+
				tokenList.get(index-1).value + "\"";
		errors.add(s);
		System.out.println(s);
	}
	
	//������
	private static void writeToFile(StringBuffer str){
        String path = "lab3_syntax_out.txt";
        try{
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str.toString()); 
            bw.close(); 
        }catch (IOException e){
            e.printStackTrace();
        }
	}
	
	private void printInput(){
		String output = "";
		for(int i = index;i < tokenList.size();i++)
		{
			output += tokenList.get(i).value;
			output += " ";
		}
		//System.out.print(output);
		result.append(output);
	}
}
