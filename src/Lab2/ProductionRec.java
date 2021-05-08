package Lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class ProductionRec{
	public static String emp = "��";  // �մ�
	public static String end = "#";  // ������
	public static TreeSet<String> VN = new TreeSet<String>();  // ���ս����
	public static TreeSet<String> VT = new TreeSet<String>();  // �ս����
	public static ArrayList<Production> F = new ArrayList<Production>();  // ����ʽ��
	//  ÿ�����ŵ�first��
	public static HashMap<String,TreeSet<String> > firstMap = new HashMap<String,TreeSet<String>>();
	//���౻���ص�ʱ�����д˶δ���
	static{
		//��ȡ�ķ�����Ӳ���ʽ
		try{
			read("data/Productions.txt");
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}

		// ��ӷ��ս��
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
		// ����ս��
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
	
	//�������ʽ
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
					//�洢
					F.add(derivation);
				}
			}			
		}
		scanner.close();
	}
	
   //����first����
	private static void addFirst(){
		//�����е��ս����first����Ϊ����
		Iterator<String> iterVT = VT.iterator();
		while(iterVT.hasNext()){
			String vt = iterVT.next();
			firstMap.put(vt,new TreeSet<String>());
			firstMap.get(vt).add(vt);
		}
		//�������з��ս����first����
		Iterator<String> iterVN = VN.iterator();
		while(iterVN.hasNext()){
			String vn = iterVN.next();
			firstMap.put(vn, new TreeSet<String>());
			firstMap.get(vn).addAll(findFirst(vn));
		}
		//System.out.println(firstMap);
	}
	
	//һ�����ڲ���first�ĵݹ麯��
	private static TreeSet<String> findFirst(String vn){
		TreeSet<String> set = new TreeSet<String>();
		int size1 = 0;
		int size2 = 0;
		while(true){
			size1 = set.size(); 
			for(Production d:F){
				if(d.left.equals(vn)){
					// �ս����ֱ�Ӽ���
					if(VT.contains(d.list.get(0)))
						set.add(d.list.get(0));
					// �շ��ţ�ֱ�Ӽ���
					else if(d.list.get(0).equals(emp))
						set.add(emp);
					// ���ս�����ݹ�
					else if(VN.contains(d.list.get(0))){
						// ȥ����ݹ�
						if(!vn.equals(d.list.get(0))){
							TreeSet<String> set2 = findFirst(d.list.get(0));
							set.addAll(set2);  // �ٴεݹ�
							if(set2.contains(emp)){
								for(int j=1; j<d.list.size(); j++){
									TreeSet<String> set3 = findFirst(d.list.get(j));									
									set.addAll(set3);  // �ٴεݹ�
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
