package Lab2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class LRState implements Serializable{
	// ��Ŀ�����,��DFA״̬��
	public int id;  
	//LR��Ŀ�ļ���,ÿ��Ԫ�ر�ʾһ������ʽ״̬
	public ArrayList<LRItem> set = new ArrayList<LRItem>();  
	
    //���캯����������״̬��
	public LRState(int id)	{
		this.id = id;
	}
	
	public boolean contains(LRItem lrd)	{
		for(LRItem l:set)
			if(l.equalTo(lrd))
				return true;
		return false;
	}
	
	//��״̬������µ���Ŀ���ɹ�����true,���򷵻�false
	public boolean addNewDerivation(LRItem d){
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
		for(LRItem lrd:set){
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
	public ArrayList<LRItem> getLRDs(String s){
		ArrayList<LRItem> result = new ArrayList<LRItem>();
		for(LRItem lrd:set){
			// �ǹ�Լ״̬
			if(lrd.d.list.size() != lrd.index){
				String s1 = lrd.d.list.get(lrd.index);
				if(s1.equals(s))
					result.add((LRItem)lrd.clone());
			}
		}
		return result;
	}
	
	public boolean equalTo(LRState state){
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
		Iterator<LRItem> iter = set.iterator();
		while(iter.hasNext())
			iter.next().print();
	}
}
