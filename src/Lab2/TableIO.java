package Lab2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TableIO {
	//将LRTable写入文件
	public static void saveObjToFile(LRTable t){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/LRtable.bin"));
			oos.writeObject(t);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	//从文件中读出对象，并且返回LRTable对象
	public static LRTable getObjFromFile(){
		try{
			@SuppressWarnings("resource")
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/LRtable.bin"));
			LRTable table = (LRTable)ois.readObject();
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
