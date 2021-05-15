package Lab3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ToFile {
	//将LRTable写入文件
	public static void saveObjToFile(LR1 t){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Lab3-data/Lab3_LR1.model"));
			oos.writeObject(t);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	//从文件中读出对象，并且返回LRTable对象
	public static LR1 getObjFromFile(){
		try{
			@SuppressWarnings("resource")
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Lab3-data/Lab3_LR1.model"));
			LR1 table = (LR1)ois.readObject();
			return table;
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		
		return null;
	}

}
