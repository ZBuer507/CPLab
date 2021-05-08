package Lab2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class State implements Serializable{
	// ��Ŀ�����,��DFA״̬��
	public int id;  
	//LR��Ŀ�ļ���,ÿ��Ԫ�ر�ʾһ������ʽ״̬
	public ArrayList<Sets> set = new ArrayList<Sets>();  
	
    //���캯����������״̬��
	public State(int id)	{
		this.id = id;
	}
	
	public boolean contains(Sets lrd)	{
		for(Sets l:set)
			if(l.equalTo(lrd))
				return true;
		return false;
	}
	
	//��״̬������µ���Ŀ���ɹ�����true,���򷵻�false
	public boolean addNewDerivation(Sets d){
		if(contains(d)){
			return false;
		}else{
			set.add(d);
			return true;
		}
	}
	
    //���ص�����еķ���
	public ArrayList<String> getGotoPath(){
		ArrayList<String> result = new ArrayList<String>();
		for(Sets lrd:set){
			// ��Լ״̬
			if(lrd.d.list.size()==lrd.index)
				continue;
			// "."����ķ���
			String s = lrd.d.list.get(lrd.index);
			if(!result.contains(s))
				result.add(s);
		}
		return result;
	}
	
    //���ش���Ŀ�ĺ����Ŀ��
	public ArrayList<Sets> getLRDs(String s){
		ArrayList<Sets> result = new ArrayList<Sets>();
		for(Sets lrd:set){
			// �ǹ�Լ״̬
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
