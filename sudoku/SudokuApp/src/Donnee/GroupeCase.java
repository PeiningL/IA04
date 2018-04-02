package Donnee;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GroupeCase {
	
	private ArrayList<Case> groupe = new ArrayList<Case>();

	public GroupeCase() {
		super();
	}
	
	public GroupeCase(ArrayList<Case> groupe) {
		super();
		this.groupe = groupe;
	}
	
	public ArrayList<Case> getGroupe() {
		return groupe;
	}

	public void setGroupe(ArrayList<Case> groupe) {
		this.groupe = groupe;
	}

	public String toJson(){
		ObjectMapper mapper = new ObjectMapper();
		String s = null;
		GroupeCase cellules = new GroupeCase(this.groupe);
		try {
			s = mapper.writeValueAsString(cellules);
			return s;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	public void add(Case c){
		this.groupe.add(c);
	}
	public GroupeCase deserialisation(String s){
		ObjectMapper mapper = new ObjectMapper();
		GroupeCase ort = null;
		try {
			ort = mapper.readValue(s, GroupeCase.class);
			return ort;
			}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
