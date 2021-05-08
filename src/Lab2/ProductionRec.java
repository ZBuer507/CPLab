package Lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class ProductionRec{
	public static String emp = "ε";  // 空串
	public static String end = "#";  // 结束符
	public static TreeSet<String> VN = new TreeSet<String>();  // 非终结符集
	public static TreeSet<String> VT = new TreeSet<String>();  // 终结符集
	public static ArrayList<Production> F = new ArrayList<Production>();  // 产生式集
	//  每个符号的first集
	public static HashMap<String,TreeSet<String> > firstMap = new HashMap<String,TreeSet<String>>();
	//在类被加载的时候运行此段代码
	static{
		//读取文法，添加产生式
		try{
			read("data/Productions.txt");
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}

		// 添加非终结符
		VN.add("P'");	VN.add("P");  VN.add("SEMI");   VN.add("COMMA");   VN.add("ASSIGNOP"); 
		VN.add("RELOP");	VN.add("PLUS");	VN.add("MINUS");	VN.add("STAR");
		VN.add("DIV");	VN.add("AND");	VN.add("OR");	VN.add("DOT");
		VN.add("NOT");	VN.add("TYPE");	VN.add("LP");	VN.add("RP");
		VN.add("LB");	VN.add("RB");	VN.add("LC");	VN.add("RC");
		VN.add("STRUCT");VN.add("RETURN");	VN.add("IF");	VN.add("ELSE");
		VN.add("WHILE");	VN.add("Program");	VN.add("ExtDefList");
		VN.add("ExtDef");	VN.add("Specifier");	VN.add("FunDec");
		VN.add("CompSt");	VN.add("VarDec");	VN.add("StructSpecifier");
		VN.add("OptTag");	VN.add("Tag");	VN.add("VarList");	VN.add("ParamDec");
		VN.add("StmtList");	VN.add("Stmt");	VN.add("Exp");
		VN.add("DefList");	VN.add("Def");	VN.add("Dec");
		VN.add("Args");	VN.add("ExtDecList");	VN.add("DecList");
		// 添加终结符
		VT.add(";");
		VT.add("int");	VT.add("float");	VT.add("struct");	VT.add("return");
		VT.add("ID");	VT.add("INT");	VT.add("FLOAT");
		VT.add("[");     VT.add("]");     VT.add("num");    VT.add("=");  
		VT.add("+");     VT.add("/");	VT.add("*");     VT.add("-");
		VT.add("(");      VT.add(")");  
		VT.add("if");    VT.add("else");  VT.add("while");
		VT.add("<");      VT.add("<=");   VT.add("==");    VT.add("!=");     VT.add(">");     
		VT.add(">=");     VT.add(",");	VT.add("||");	VT.add("&&");	VT.add(".");
		VT.add("{");	VT.add("}");	VT.add("!");	VT.add("or");
		
		addFirst();
	}
	
	//解析表达式
	private static void read(String filename) throws FileNotFoundException{
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		while(scanner.hasNext()){
			String line = scanner.nextLine().trim();
			if(!line.equals("")){
				String[] div = line.split("->");
				String[] right = div[1].split("\\|");
				for(String r:right){
					Production derivation = new Production(div[0].trim()+"->"+r.trim());
					//存储
					F.add(derivation);
				}
			}			
		}
		scanner.close();
	}
	
   //计算first集合
	private static void addFirst(){
		//将所有的终结符的first都设为本身
		Iterator<String> iterVT = VT.iterator();
		while(iterVT.hasNext()){
			String vt = iterVT.next();
			firstMap.put(vt,new TreeSet<String>());
			firstMap.get(vt).add(vt);
		}
		//计算所有非终结符的first集合
		Iterator<String> iterVN = VN.iterator();
		while(iterVN.hasNext()){
			String vn = iterVN.next();
			firstMap.put(vn, new TreeSet<String>());
			firstMap.get(vn).addAll(findFirst(vn));
		}
		//System.out.println(firstMap);
	}
	
	//一个用于查找first的递归函数
	private static TreeSet<String> findFirst(String vn){
		TreeSet<String> set = new TreeSet<String>();
		int size1 = 0;
		int size2 = 0;
		while(true){
			size1 = set.size(); 
			for(Production d:F){
				if(d.left.equals(vn)){
					// 终结符，直接加入
					if(VT.contains(d.list.get(0)))
						set.add(d.list.get(0));
					// 空符号，直接加入
					else if(d.list.get(0).equals(emp))
						set.add(emp);
					// 非终结符，递归
					else if(VN.contains(d.list.get(0))){
						// 去除左递归
						if(!vn.equals(d.list.get(0))){
							TreeSet<String> set2 = findFirst(d.list.get(0));
							set.addAll(set2);  // 再次递归
							if(set2.contains(emp)){
								for(int j=1; j<d.list.size(); j++){
									TreeSet<String> set3 = findFirst(d.list.get(j));									
									set.addAll(set3);  // 再次递归
									if(!set3.contains(emp))
										break;
								}
							}							
						}
					}
				}
			}
			size2 = set.size(); 
			if(size1 == size2)
				break;
		}
		return set;
	}
}
