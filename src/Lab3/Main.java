package Lab3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {
	public static void main(String[] args) {
		//String file_name = "Lab3-data/test3-1.txt";
		String file_name = "Lab3-data/test3-2.txt";
		//0表示新建分析表
		int iscached = 1;
		
		List<Stack<Symbol>> table = new ArrayList<Stack<Symbol>>();
		List<String> three_addr = new ArrayList<String>();
		List<FourAddr> four_addr = new ArrayList<FourAddr>();
		List<String> errors = new ArrayList<String>();
		
		Smantic se = new Smantic(file_name,table,three_addr,four_addr,errors,iscached);
		se.writeToFile();
	}
}
