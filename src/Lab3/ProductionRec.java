package Lab3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class ProductionRec{
	public static String emp = "��";
	public static String end = "#";
	//���ս����
	public static TreeSet<String> VN = new TreeSet<String>();  
	//�ս����
	public static TreeSet<String> VT = new TreeSet<String>();  
	//����ʽ��
	public static ArrayList<Production> F = new ArrayList<Production>();
	//ÿ�����ŵ�first��
	public static HashMap<String,TreeSet<String> > firstMap = new HashMap<String,TreeSet<String>>();

	static{
		//��ȡ�ķ�����Ӳ���ʽ
		try{
			read("Lab3-data/Productions.txt");
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}

		//���ս��
		VN.add("P'");  VN.add("P");   VN.add("D");   VN.add("S");   VN.add("T");  
		VN.add("X");   VN.add("C");   VN.add("E");   VN.add("E1");  VN.add("E2");  
		VN.add("S1");  VN.add("S2");  VN.add("L");   VN.add("B");   VN.add("B1");  
		VN.add("B2");  VN.add("R");   VN.add("EL");  VN.add("S3");  VN.add("S0");
		VN.add("M0");   VN.add("M");  VN.add("N");   VN.add("N1");  VN.add("N2");
		
		//�ս��
		VT.add("proc");  VT.add("id");    VT.add(";");     VT.add("record"); VT.add("integer");
		VT.add("real");  VT.add("[");     VT.add("]");     VT.add("num");    VT.add("=");  
		VT.add("+");     VT.add("*");     VT.add("-");     VT.add("(");      VT.add(")");  
		VT.add("if");    VT.add("then");  VT.add("else");  VT.add("while");  VT.add("do");    
		VT.add("or");     VT.add("and");  VT.add("not");   VT.add("true");   VT.add("false"); 
		VT.add("<");      VT.add("<=");   VT.add("==");    VT.add("!=");     VT.add(">");     
		VT.add(">=");     VT.add("call"); VT.add("begin");     VT.add("end");    VT.add(",");
		
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
	
	//����first
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
							set.addAll(set2);  //�ݹ�
							if(set2.contains(emp)){
								for(int j=1; j<d.list.size(); j++){
									TreeSet<String> set3 = findFirst(d.list.get(j));									
									set.addAll(set3);  //�ݹ�
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
