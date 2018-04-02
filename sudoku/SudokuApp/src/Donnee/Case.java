package Donnee;
import java.util.ArrayList;

public class Case {
	
	private int value;
	private ArrayList<Integer> listPoss = new ArrayList<Integer>();
	private int position;
	
	//constructor without parameters
	public Case() {
		super();
		for (int i=1;i<10;i++){
			listPoss.add(i);
		}
	}
	
	//constructor with parameters
	
	public Case(int value, int pos) {
		super();
		this.value = value;
		this.position = pos;
		
		//initialization of list of possibilities with value
		if (this.value == 0){
			for (int i = 1; i <= 9; ++i){
				listPoss.add(i);
			}
		}else{
			listPoss.clear();
		}
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public ArrayList<Integer> getListPoss() {
		return listPoss;
	}

	public void setListPoss(ArrayList<Integer> listPoss) {
		this.listPoss = listPoss;
	}
	
}
