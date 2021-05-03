package Lab1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class Main{
	public Main(){
		String filename = "test.txt";
		String filename2 = "lexer.out";
		String filename3 = "error.out";
		File file = new File(filename);
		
		String text = "";
		MyList list = new MyList();
		try{
			InputStream in = new FileInputStream(file);
			int tempbyte;
			while ((tempbyte=in.read()) != -1){
				text += ("" + (char)tempbyte);
			}
			in.close();
		}catch(Exception event){
			event.printStackTrace();
		}
		
		Lexer text_lex = new Lexer(text, list);
		text_lex.lex();
		//list.print();
		
		FileWriter file2;
		try {
			file2 = new FileWriter(filename2);
			file2.write(list.print());
			file2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileWriter file3;
		try {
			file3 = new FileWriter(filename3);
			file3.write(list.printError());
			file3.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static void main(String[] agrs) throws IOException{
        new Main();
    }
}

