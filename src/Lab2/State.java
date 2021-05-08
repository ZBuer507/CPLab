package Lab2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class State implements Serializable{
	// 项目集编号,即DFA状态号
	public int id;  
	//LR项目的集合,每个元素表示一个产生式状态
	public ArrayList<Sets> set = new ArrayList<Sets>();  
	
    //构造函数，输入是状态号
	public State(int id)	{
		this.id = id;
	}
	
	public boolean contains(Sets lrd)	{
		for(Sets l:set)
			if(l.equalTo(lrd))
				return true;
		return false;
	}
	
	//向状态中添加新的项目，成功返回true,否则返回false
	public boolean addNewDerivation(Sets d){
		if(contains(d)){
			return false;
		}else{
			set.add(d);
			return true;
		}
	}
	
    //返回点后所有的符号
	public ArrayList<String> getGotoPath(){
		ArrayList<String> result = new ArrayList<String>();
		for(Sets lrd:set){
			// 规约状态
			if(lrd.d.list.size()==lrd.index)
				continue;
			// "."后面的符号
			String s = lrd.d.list.get(lrd.index);
			if(!result.contains(s))
				result.add(s);
		}
		return result;
	}
	
    //返回此项目的后继项目集
	public ArrayList<Sets> getLRDs(String s){
		ArrayList<Sets> result = new ArrayList<Sets>();
		for(Sets lrd:set){
			// 非规约状态
			if(lrd.d.list.size() != lrd.index){
				String s1 = lrd.d.list.get(lrd.index);
				if(s1.equals(s))
					result.add((Sets)lrd.clone());
			}
		}
		return result;
	}
	
	public boolean equalTo(State state){
		if(this.toString().hashCode()==state.toString().hashCode())
            // if(contains(set,state.set)&&contains(state.set,set)){
			return true;
		else
			return false;
	}
	
	public String toString(){
		String result = "";
		for(int i = 0;i < set.size();i++){
			result += set.get(i);
			if(i < set.size()-1)
				result += "\n";
		}
		return result;
	}
	
	public void print(){
		Iterator<Sets> iter = set.iterator();
		while(iter.hasNext())
			iter.next().print();
	}
}
