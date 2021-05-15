package Lab3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class States implements Serializable{
	public ArrayList<State> states = new ArrayList<State>();
	public State get(int i){
		return states.get(i);
	}
	
	public int size(){
		return states.size();
	}
	
	public void writefile(){		
        String path = "Lab3-data/Sets.txt";
        try{
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            
    		int size = states.size();
    		for(int i = 0;i < size;i++){
    			bw.write("\n"+"I"+i+":"+"\n"); 
    			bw.write(states.get(i).toString());
    			bw.write("\n");
    		} 
            bw.close(); 
        }catch (IOException e){
            e.printStackTrace();
        }
	}
}
