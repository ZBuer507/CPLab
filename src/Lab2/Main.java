package Lab2;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		String file_name = "test2.txt";
		//0��ʾ�½�������
		int iscached = 1;
		List<String> result = new ArrayList<String>();
		List<String> errors = new ArrayList<String>();
		SyntaxParser se = new SyntaxParser(file_name, result, errors, iscached);
		se.writeToFile();
	}
}
