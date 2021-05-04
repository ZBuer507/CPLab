package Lab2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LRTable implements Serializable{
	public LRStateSet dfa;  // ����DFA״̬
	public int stateNum;  // DFA״̬��
	public int actionLength;  // Action������
	public int gotoLength;  // GoTo������
	private String[] actionCol;  // Action��ı�ͷ
	private String[] gotoCol;  // GoTo��ı�ͷ
	private String[][] actionTable;  // Action����ά����
	private int[][] gotoTable;  // GoTo����ά����
	
	//  ����x��DFA״̬,����S����ʱ,ת�Ƶ���y��DFA״̬,��:
	private ArrayList<Integer> gotoStart = new ArrayList<Integer>();  // �洢��x��DFA״̬
	private ArrayList<Integer> gotoEnd = new ArrayList<Integer>();  // �洢��y��DFA״̬
	private ArrayList<String> gotoPath = new ArrayList<String>();  // �洢S����
	
   //���������
	public LRTable(){
		createTableHeader();//����
		this.actionLength = actionCol.length;
		this.gotoLength = gotoCol.length;
		createDFA();//����DFA
		this.stateNum = dfa.size();
		this.gotoTable = new int[stateNum][gotoLength+actionLength-1];
		this.actionTable = new String[stateNum][actionLength];
		createAnalyzeTable();//����﷨��������������
	}
	
	//�����������ͷ
	private void createTableHeader(){
		//�����ǽ���һ�������
		this.actionCol = new String[Pretreat.VT.size()+1];
		this.gotoCol = new String[Pretreat.VN.size()+Pretreat.VT.size()];
		Iterator<String> iter1 = Pretreat.VT.iterator();//�������е��ս��
		Iterator<String> iter2 = Pretreat.VN.iterator();//�������еķ��ս��
		int i = 0;
		// �ս������
		while(iter1.hasNext()){
			String vt = iter1.next();
			if(!vt.equals(Pretreat.emp)){
				actionCol[i] = vt;
				gotoCol[i] = vt;
				i++;
			}
		}
		actionCol[i] = "#";
		while(iter2.hasNext()){
			String vn = iter2.next();
			gotoCol[i] = vn;
			i++;
		}
	}
	
    //�ݹ�ؽ���һ��DFA
	private void createDFA(){
		this.dfa = new LRStateSet();//������е�״̬
		LRState state0 = new LRState(0);
		state0.addNewDerivation(new LRItem(getDerivation("P'").get(0),"#",0));  
		// ״̬0�м���һ������Ĳ���ʽ
		for(int i = 0;i < state0.set.size();i++){
			//����״̬0�����в���ʽ
			LRItem lrd = state0.set.get(i);
			//lrd.index�ǵ����ڵ�λ��
			if(lrd.index < lrd.d.list.size()){
				// ��ȡ"."����ķ���
				String A = lrd.d.list.get(lrd.index);
				Set<String> firstB = new HashSet<String>();
				// �����ֻ��һ������
				if(lrd.index==lrd.d.list.size()-1){
					//���������չ����
					firstB.add(lrd.lr);
				//�����ж�����ŵ�ʱ����Ҫ��first����
				}else{
					Boolean flag = true;
					for(int m = lrd.index+1; m < lrd.d.list.size(); m++){
						Set<String> list1 = first(lrd.d.list.get(m));	
						firstB.addAll(list1);
						if(!list1.contains("��")){
							flag = false;
							break;
						}						
					}
					if(flag)
						firstB.add(lrd.lr);
				}
				if(Pretreat.VN.contains(A)){
					ArrayList<Production> dA = getDerivation(A);
					//�������еĺ�̲���ʽ������Ӧ�Ĵ���
					for(int j=0,length1=dA.size();j<length1;j++){
						for(String f:firstB){
							if(!f.equals("��")){
								LRItem lrd1;
								if(dA.get(j).list.get(0).equals("��"))
									lrd1 = new LRItem(dA.get(j), f, 1);
								else
									lrd1 = new LRItem(dA.get(j), f, 0);
								if(!state0.contains(lrd1))
									state0.addNewDerivation(lrd1);
							}
						}
					}
				}
			}
		}
		//״̬0������ɣ����뼯��
		dfa.states.add(state0);
		ArrayList<String> gotoPath = state0.getGotoPath();
		for(String path:gotoPath){
			//ֱ��ͨ��·��������һ��״̬�����
			ArrayList<LRItem> list = state0.getLRDs(path);
			//��ʼ���еݹ飬�������ڷ�����DFA
			addState(0,path,list);
		}
	}

	//��������һ��������ʵ���߼�
	private void addState(int lastState,String path,ArrayList<LRItem> list)
	{
		LRState temp = new LRState(0);
		for(int i = 0;i < list.size();i++){
			list.get(i).index++;
			temp.addNewDerivation(list.get(i));
		}
		for(int i = 0;i < temp.set.size();i++){
			// �ǹ�Լ
			if(temp.set.get(i).d.list.size() != temp.set.get(i).index){
				String A = temp.set.get(i).d.list.get(temp.set.get(i).index);
				Set<String> firstB = new HashSet<String>();
				// �����ڡ�A->BBB.C, #����״̬
				if(temp.set.get(i).index+1 == temp.set.get(i).d.list.size()){
					firstB.add(temp.set.get(i).lr);
				}else{
					Boolean flag = true;
					for(int m = temp.set.get(i).index+1; m < temp.set.get(i).d.list.size(); m++){
						Set<String> list1 = first(temp.set.get(i).d.list.get(m));	
						firstB.addAll(list1);
						if(!list1.contains("��")){
							flag = false;
							break;
						}						
					}
					if(flag)
						firstB.add(temp.set.get(i).lr);
				}
							
				ArrayList<Production> dA = getDerivation(A);
				for(int j = 0;j < dA.size();j++){
					for(String f:firstB){
						if(!f.equals("��")){
							LRItem lrd;
							if(dA.get(j).list.get(0).equals("��"))
								lrd = new LRItem(dA.get(j), f, 1);
							else
								lrd = new LRItem(dA.get(j), f, 0);
							if(!temp.contains(lrd))
								temp.addNewDerivation(lrd);
						}
					}
				}
			}
		}
		for(int i = 0;i < dfa.states.size();i++){
			if(dfa.states.get(i).equalTo(temp)){
				gotoStart.add(lastState);
				gotoEnd.add(i);
				gotoPath.add(path);
				return;
			}
		}
		temp.id = dfa.states.size();
		dfa.states.add(temp);
		gotoStart.add(lastState);
		gotoEnd.add(temp.id);
		gotoPath.add(path);
		ArrayList<String> gotoPath = temp.getGotoPath();
		for(String p:gotoPath){
			ArrayList<LRItem> l = temp.getLRDs(p);//ֱ��ͨ��·��������һ��״̬�����
			addState(temp.id,p,l);
		}
	}
	
	//��ȡ��һ���ض�����Ϊ�󲿵����в���ʽ
	public ArrayList<Production> getDerivation(String v)
	{
		ArrayList<Production> result = new ArrayList<Production>();
		Iterator<Production> iter = Pretreat.F.iterator();
		while(iter.hasNext()){
			Production d = iter.next();
			if(d.left.equals(v))
				result.add(d);
		}
		return result;
	}
	
	//��ȡ�ķ����ŵ�first����
	private Set<String> first(String v)
	{
		Set<String> result = new HashSet<String>();
		if(v.equals("#")){
			result.add("#");
		}else{
			//System.out.println(v);
			//System.out.println(Pretreat.firstMap.get(v));
			Iterator<String> iter = Pretreat.firstMap.get(v).iterator();
			while(iter.hasNext())
				result.add(iter.next());
		}
		return result;
	}
	
	//����﷨������
	private void createAnalyzeTable(){
		//��ȫ�����Ͽ�ֵ
		for(int i = 0;i < gotoTable.length; i++)
			for(int j = 0;j < gotoTable[0].length;j++)
				gotoTable[i][j] = -1;
		for(int i = 0;i < actionTable.length;i++)
			for(int j = 0;j < actionTable[0].length;j++)
				actionTable[i][j] = "  ";
		//�����﷨�������goto����
		int gotoCount = this.gotoStart.size();
		for(int i = 0;i < gotoCount;i++){
			int start = gotoStart.get(i);
			int end = gotoEnd.get(i);
			String path = gotoPath.get(i);
			int pathIndex = gotoIndex(path);
			//System.out.println(path);
			this.gotoTable[start][pathIndex] = end;
		}
		//�����﷨�������action����
		int stateCount = dfa.states.size();
		for(int i = 0;i < stateCount;i++){
			LRState state = dfa.get(i);//��ȡdfa�ĵ���״̬
			for(LRItem lrd:state.set){//��ÿһ�����з���
				if(lrd.index == lrd.d.list.size()){
					if(!lrd.d.left.equals("P'")){
						int derivationIndex = derivationIndex(lrd.d);
						String value = "r"+derivationIndex;
						actionTable[i][actionIndex(lrd.lr)] = value;//��Ϊ��Լ
					}else{
						actionTable[i][actionIndex("#")] = "acc";//��Ϊ����
					}
				}else{
					String next = lrd.d.list.get(lrd.index);//��ȡ��������ķ�����
					if(Pretreat.VT.contains(next))
						if(gotoTable[i][gotoIndex(next)] != -1)
							actionTable[i][actionIndex(next)] = "s"+gotoTable[i][gotoIndex(next)];
				}
			}
		}
	}
	//����goto�е�����
	private int gotoIndex(String s){
		for(int i = 0;i < gotoLength;i++)
			if(gotoCol[i].equals(s))
				return i;
		return -1;
	}
	//����action�е�����
	private int actionIndex(String s){
		for(int i = 0;i < actionLength;i++)
			if(actionCol[i].equals(s))
				return i;
		return -1;
	}
	//�����ǵڼ������ʽ
	private int derivationIndex(Production d){
		int size = Pretreat.F.size();
		for(int i = 0;i < size;i++)
			if(Pretreat.F.get(i).equals(d))
				return i;
		return -1;
	}
	
	public String ACTION(int stateIndex,String vt){
		int index = actionIndex(vt);
		if(index == -1) {
			//System.out.println("Too many errors, you have to check it carefully!");
			return "acc";
		}
		return actionTable[stateIndex][index];
	}
	
	public int GOTO(int stateIndex,String vn){
		int index = gotoIndex(vn);
		return gotoTable[stateIndex][index];
	}
	
	//���������
	public String print(){
		StringBuffer result = new StringBuffer();
		String colLine = form("");
		for(int i = 0;i < actionCol.length;i++){
			if(!actionCol[i].equals("integer")&&!actionCol[i].equals("record"))
				colLine += "\t";
			colLine += form(actionCol[i]);
		}
		for(int j = actionCol.length-1;j < gotoCol.length;j++){
			colLine += "\t";
			colLine += form(gotoCol[j]);
		}
		result.append(colLine + "\n");
		int index = 0;
		for(int i = 0;i < dfa.states.size();i++){
			String line = form(String.valueOf(i));
			while(index < actionCol.length){
				line += "\t";
				line += form(actionTable[i][index]);
				index++;
			}
			index = actionCol.length-1;
			while(index < gotoCol.length){
				line += "\t";
				if(gotoTable[i][index] == -1)
					line += form("  ");
				else
					line += form(String.valueOf(gotoTable[i][index]));
				index++;
			}
			index = 0;
			line += "\t";
			result.append(line + "\n");
			writefile(result);
			//System.out.println(line);
		}
		return result.toString();
	}
	
	public String form(String str){
		for(int i = 0; i < 9-str.length(); i++)
			str += " ";
		return str;
	}
	
	
	public void writefile(StringBuffer str)
	{		
        String path = "data/Table.txt";
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
	
	public int getStateNum(){
		return dfa.states.size();
	}

}
