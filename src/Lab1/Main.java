package Lab1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main{
	public Main(){
		String filename = "src/Lab1/test.txt";
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
		list.print();
	}
	
    public static void main(String[] agrs){
        new Main();
    }
}

