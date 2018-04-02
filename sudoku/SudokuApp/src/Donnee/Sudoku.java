package Donnee;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Sudoku {
	private ArrayList<Case> sudoku = new ArrayList<Case>();
	
	public ArrayList<Case> getSudoku() {
		return sudoku;
	}
	
	public Sudoku(String filepath){
		Scanner sc;
		try {
			sc = new Scanner(new File(filepath));
			int i=0;
			for(i=0;i<81;i++){
					sudoku.add(new Case(sc.nextInt(),i));
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
