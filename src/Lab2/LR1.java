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

public class LR1 implements Serializable{
	public States dfa;
	public int stateNum;
	public int actionLength;
	public int gotoLength;
	private String[] actionCol;
	private String[] gotoCol;
	private String[][] actionTable;
	private int[][] gotoTable;
	
	//当第x号DFA状态,输入S符号时,转移到第y号DFA状态,则:
	private ArrayList<Integer> gotoStart = new ArrayList<Integer>();
	private ArrayList<Integer> gotoEnd = new ArrayList<Integer>();
	private ArrayList<String> gotoPath = new ArrayList<String>();
	
	//构造分析表
	public LR1(){
		createTableHeader();
		this.actionLength = actionCol.length;
		this.gotoLength = gotoCol.length;
		createDFA();
		this.stateNum = dfa.size();
		this.gotoTable = new int[stateNum][gotoLength+actionLength-1];
		this.actionTable = new String[stateNum][actionLength];
		createAnalyzeTable();
	}
	
	//建立分析表表头
	private void createTableHeader(){
		//以下是建立一个表的列
		this.actionCol = new String[ProductionRec.VT.size()+1];
		this.gotoCol = new String[ProductionRec.VN.size()+ProductionRec.VT.size()];
		Iterator<String> iter1 = ProductionRec.VT.iterator();//遍历所有的终结符
		Iterator<String> iter2 = ProductionRec.VN.iterator();//遍历所有的非终结符
		int i = 0;
		// 终结符集合
		while(iter1.hasNext()){
			String vt = iter1.next();
			if(!vt.equals(ProductionRec.emp)){
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
	
    //递归地建立一个DFA
	private void createDFA(){
		this.dfa = new States();
		State state0 = new State(0);
		state0.addNewDerivation(new Sets(getDerivation("P'").get(0),"#",0));  
		// 状态0中加入一个增广的产生式
		for(int i = 0;i < state0.set.size();i++){
			//遍历状态0的所有产生式
			Sets lrd = state0.set.get(i);
			//lrd.index是点所在的位置
			if(lrd.index < lrd.d.list.size()){
				// 获取"."后面的符号
				String A = lrd.d.list.get(lrd.index);
				Set<String> firstB = new HashSet<String>();
				// 点后面只有一个符号
				if(lrd.index==lrd.d.list.size()-1){
					//加入自身的展望符
					firstB.add(lrd.lr);
				//后面有多个符号的时候，需要看first集合
				}else{
					Boolean flag = true;
					for(int m = lrd.index+1; m < lrd.d.list.size(); m++){
						Set<String> list1 = first(lrd.d.list.get(m));	
						firstB.addAll(list1);
						if(!list1.contains("ε")){
							flag = false;
							break;
						}						
					}
					if(flag)
						firstB.add(lrd.lr);
				}
				if(ProductionRec.VN.contains(A)){
					ArrayList<Production> dA = getDerivation(A);
					//遍历所有的后继产生式，做相应的处理
					for(int j=0,length1=dA.size();j<length1;j++){
						for(String f:firstB){
							if(!f.equals("ε")){
								Sets lrd1;
								if(dA.get(j).list.get(0).equals("ε"))
									lrd1 = new Sets(dA.get(j), f, 1);
								else
									lrd1 = new Sets(dA.get(j), f, 0);
								if(!state0.contains(lrd1))
									state0.addNewDerivation(lrd1);
							}
						}
					}
				}
			}
		}
		//状态0构造完成，加入集合
		dfa.states.add(state0);
		ArrayList<String> gotoPath = state0.getGotoPath();
		for(String path:gotoPath){
			//直接通过路径传到下一个状态的情况
			ArrayList<Sets> list = state0.getLRDs(path);
			//开始进行递归，建立用于分析的DFA
			addState(0,path,list);
		}
	}

	private void addState(int lastState,String path,ArrayList<Sets> list){
		State temp = new State(0);
		for(int i = 0;i < list.size();i++){
			list.get(i).index++;
			temp.addNewDerivation(list.get(i));
		}
		for(int i = 0;i < temp.set.size();i++){
			// 非规约
			if(temp.set.get(i).d.list.size() != temp.set.get(i).index){
				String A = temp.set.get(i).d.list.get(temp.set.get(i).index);
				Set<String> firstB = new HashSet<String>();
				// 类似于“A->BBB.C, #”的状态
				if(temp.set.get(i).index+1 == temp.set.get(i).d.list.size()){
					firstB.add(temp.set.get(i).lr);
				}else{
					Boolean flag = true;
					for(int m = temp.set.get(i).index+1; m < temp.set.get(i).d.list.size(); m++){
						Set<String> list1 = first(temp.set.get(i).d.list.get(m));	
						firstB.addAll(list1);
						if(!list1.contains("ε")){
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
						if(!f.equals("ε")){
							Sets lrd;
							if(dA.get(j).list.get(0).equals("ε"))
								lrd = new Sets(dA.get(j), f, 1);
							else
								lrd = new Sets(dA.get(j), f, 0);
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
			//直接通过路径传到下一个状态的情况
			ArrayList<Sets> l = temp.getLRDs(p);
			addState(temp.id,p,l);
		}
	}
	
	//获取以一个特定符号为左部的所有产生式
	public ArrayList<Production> getDerivation(String v){
		ArrayList<Production> result = new ArrayList<Production>();
		Iterator<Production> iter = ProductionRec.F.iterator();
		while(iter.hasNext()){
			Production d = iter.next();
			if(d.left.equals(v))
				result.add(d);
		}
		return result;
	}
	
	//获取文法符号的first集合
	private Set<String> first(String v){
		Set<String> result = new HashSet<String>();
		if(v.equals("#")){
			result.add("#");
		}else{
			//System.out.println(v);
			//System.out.println(ProductionRec.firstMap.get(v));
			Iterator<String> iter = ProductionRec.firstMap.get(v).iterator();
			while(iter.hasNext())
				result.add(iter.next());
		}
		return result;
	}
	
	//填充语法分析表
	private void createAnalyzeTable(){
		//先全部填上空值
		for(int i = 0;i < gotoTable.length; i++)
			for(int j = 0;j < gotoTable[0].length;j++)
				gotoTable[i][j] = -1;
		for(int i = 0;i < actionTable.length;i++)
			for(int j = 0;j < actionTable[0].length;j++)
				actionTable[i][j] = "  ";
		//完善语法分析表的goto部分
		int gotoCount = this.gotoStart.size();
		for(int i = 0;i < gotoCount;i++){
			int start = gotoStart.get(i);
			int end = gotoEnd.get(i);
			String path = gotoPath.get(i);
			int pathIndex = gotoIndex(path);
			//System.out.println(path);
			this.gotoTable[start][pathIndex] = end;
		}
		//完善语法分析表的action部分
		int stateCount = dfa.states.size();
		for(int i = 0;i < stateCount;i++){
			//获取dfa的单个状态
			State state = dfa.get(i);
			//对每一个进行分析
			for(Sets lrd:state.set){
				if(lrd.index == lrd.d.list.size()){
					if(!lrd.d.left.equals("P'")){
						int derivationIndex = derivationIndex(lrd.d);
						String value = "r"+derivationIndex;
						//设为规约
						actionTable[i][actionIndex(lrd.lr)] = value;
					}else{
						//设为接受
						actionTable[i][actionIndex("#")] = "acc";
					}
				}else{
					//获取·后面的文法符号
					String next = lrd.d.list.get(lrd.index);
					if(ProductionRec.VT.contains(next))
						if(gotoTable[i][gotoIndex(next)] != -1)
							actionTable[i][actionIndex(next)] = "s"+gotoTable[i][gotoIndex(next)];
				}
			}
		}
	}
	//返回goto中的列数
	private int gotoIndex(String s){
		for(int i = 0;i < gotoLength;i++)
			if(gotoCol[i].equals(s))
				return i;
		return -1;
	}
	//返回action中的列数
	private int actionIndex(String s){
		for(int i = 0;i < actionLength;i++)
			if(actionCol[i].equals(s))
				return i;
		return -1;
	}
	//返回是第几个表达式
	private int derivationIndex(Production d){
		int size = ProductionRec.F.size();
		for(int i = 0;i < size;i++)
			if(ProductionRec.F.get(i).equals(d))
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
	
	//输出分析表
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
	
	
	public void writefile(StringBuffer str){		
        String path = "Lab2-data/Table.txt";
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
