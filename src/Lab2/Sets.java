package Lab2;

import java.io.Serializable;

//产生式状态
public class Sets implements java.lang.Cloneable,Serializable{
	public Production d;
	//展望符
	public String lr;
	//当前点所处位置
	public int index;  
	
	public Sets(Production d,String lr,int index){
		this.d = d;
		this.lr = lr;
		this.index = index;
	}
	
	public String toString(){
		String result = d.left+"->";
		int length = d.list.size();
		for(int i = 0;i < length;i++){
			if(length == 1 && d.list.get(0).equals("ε")){
				result += " .";
				break;
			}else{
				result += " ";
				//在index处额外输出一个点
				if(i == index)
					result += ".";
				result += d.list.get(i);
			}
		}
		if(index == length && !d.list.get(0).equals("ε")){
			result += ".";
		}
		result += " ,";
		result += lr;
		return result;
	}
	
	public boolean equalTo(Sets lrd){
		if(d.equalTo(lrd.d)&&lr.hashCode()==lrd.lr.hashCode()&&index==lrd.index)
			return true;
		else 
			return false;
	}
	
	public void print(){
		System.out.println(this.toString());
	}
	
	public Object clone(){
		return new Sets(d,lr,index);
	}
}
