package Agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import Donnee.Case;
import Donnee.GroupeCase;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Environement extends Agent{
	private int compteur = 0;
	private ArrayList<Case> sudoku = new ArrayList<Case>();
	private boolean run=true;
	private HashMap<String,ArrayList<Integer>> carre;
	//private List<Integer> init = new ArrayList<Integer>(Arrays.asList(5,0,0,0,0,4,0,0,8,0,1,0,9,0,7,0,0,0,0,9,2,8,5,0,7,0,6,7,0,0,3,0,1,0,0,4,0,0,0,0,0,0,0,0,0,6,0,0,2,0,8,0,0,1,1,0,8,0,3,2,4,9,0,0,0,0,1,0,6,0,5,0,3,0,0,7,0,0,0,0,2));
	//private List<Integer> init = new ArrayList<Integer>(Arrays.asList(5,0,0,0,2,0,0,0,0,0,0,0,0,8,0,6,0,0,4,0,0,3,0,5,0,0,0,0,0,0,7,0,0,2,0,4,0,8,0,9,5,2,0,1,0,3,0,6,0,0,1,0,0,0,0,0,0,5,0,3,0,0,9,0,0,1,0,6,0,0,0,0,0,0,0,0,1,0,0,0,7));
	//private List<Integer> init = new ArrayList<Integer>(Arrays.asList(0,8,1,0,9,0,5,3,0,9,0,0,7,0,0,0,0,6,6,0,0,0,2,1,0,0,8,0,0,4,0,0,0,0,6,0,3,0,5,0,0,0,2,0,1,0,7,0,0,0,0,8,0,0,5,0,0,3,4,0,0,0,7,1,0,0,0,0,5,0,0,9,0,2,7,0,6,0,3,8,0));
	private List<Integer> init = new ArrayList<Integer>(Arrays.asList(1,6,0,2,0,0,0,0,0,2,0,0,0,0,5,0,0,7,9,0,7,0,4,0,3,0,0,0,0,0,3,2,9,0,0,0,5,0,3,4,1,7,6,0,2,0,0,0,8,5,6,0,0,0,0,0,9,0,3,0,8,0,1,7,0,0,1,0,0,0,0,6,0,0,0,0,0,2,0,5,4));
	/*
	 * function for initializing Sudoku from json
	 * json: {0,0,5,0,2,3,0,..}
	 */
	public void initialisation() {
		for (int i=0;i<81;i++){
			this.sudoku.add(new Case(init.get(i),i));
		}
		display();
	}
	
	
	public ArrayList<Case> getSudoku() {
		return sudoku;
	}


	protected void setup() {
		 // Printout a welcome message
		 System.out.println(getAID().getName()+"is installed");
		 initialisation();
		 addBehaviour(new recieveRequest());
		 addBehaviour(new check());
		 addBehaviour(new recieveInform());
		 
	 }
	
	//recieve aid of analyse agent from simulation and should distribute a task to this analyse agent
	private class recieveRequest extends CyclicBehaviour{
		@Override
		public void action() {
			MessageTemplate mtSim = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage aidAlys = receive(mtSim);
			GroupeCase tache = new GroupeCase();
			if (aidAlys != null) {
				compteur++;
				tache = matchCptCases(compteur);
				ACLMessage req = aidAlys.createReply();
				req.setContent(tache.toJson());
				send(req);
			}
		}
	}
	
	
	private GroupeCase matchCptCases(int compteur){
		GroupeCase tache = new GroupeCase();
		if (compteur<10){
			int indice;
			for (int i=0;i<9;i++){
				indice = (compteur-1)*9+i;
				tache.add(sudoku.get(indice));
			}						
		}
		else if (compteur<19){
			int indice;
			for (int i=0;i<9;i++){
				indice = (compteur-10)+i*9;
				tache.add(sudoku.get(indice));
			}						
		}else if (compteur<22){
			int indice;
			for (int i=0;i<3;i++){
				indice = 3*(compteur-19);
				indice = indice+i*9;
				for (int j=0;j<3;j++){
					tache.add(sudoku.get(indice + j));	
				}
			}	
		}
		else if (compteur<25){
			int indice;
			for (int i=0;i<3;i++){
				indice = 3*(compteur-13);
				indice = indice+i*9;
				for (int j=0;j<3;j++){
					tache.add(sudoku.get(indice + j));	
				}
			}	
		}
		else{
			int indice;
			for (int i=0;i<3;i++){
				indice = 3*(compteur-25)+54;
				indice = indice+i*9;
				for (int j=0;j<3;j++){
					tache.add(sudoku.get(indice + j));	
				}
			}	
		}
		return tache;	
	}
	
	private class recieveInform extends CyclicBehaviour{
		@Override
		public void action() {
			MessageTemplate mtAlys = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage result = receive(mtAlys);
			ArrayList<Case> calculResult = new ArrayList<Case>();
			if (result != null) {
				int numeroAlys = 0;//compteur or AID d'agent d'analyse
				String sender = result.getSender().getName().toString();
				Pattern p = Pattern.compile("\\d+");
				Matcher m = p.matcher(sender);
				if (m.find()){
					numeroAlys = Integer.parseInt(m.group(0));
				}
				GroupeCase update = new GroupeCase();
				update = update.deserialisation(result.getContent());
				//update
				for (Case c : update.getGroupe()){
					//extract the intersection
					if (sudoku.get(c.getPosition()).getValue()==0){
						sudoku.get(c.getPosition()).getListPoss().retainAll(c.getListPoss());
						sudoku.get(c.getPosition()).setValue(c.getValue());
					}
					c.setListPoss(sudoku.get(c.getPosition()).getListPoss());
					c.setValue(sudoku.get(c.getPosition()).getValue());
				};
				//display();
				//System.out.println();
				if (update.getGroupe().stream().filter(c -> c.getValue()==0).collect(Collectors.toList()).isEmpty()){
					System.out.println("AGENT FINISH!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+result.getSender().getName());
					ACLMessage finish = result.createReply();
					finish.setPerformative(ACLMessage.CANCEL);
					send(finish);	
				}else{
					ACLMessage req = result.createReply();
					req.setPerformative(ACLMessage.REQUEST);
					//take same cases in new sudoku and resend
					req.setContent(update.toJson());
					send(req);
				}
			}
		}
	}
	private class sendFin extends OneShotBehaviour{

		@Override
		public void action() {
			ACLMessage terminate = new ACLMessage(ACLMessage.INFORM);
			display();
			terminate.addReceiver(getAID("SIMULATION"));
			send(terminate);
		}
	
	}
	private class check extends Behaviour{
		private boolean done = false;
		@Override
		public void action() {
			//check if all case is terminated
			for (Case c: sudoku){
				if (c.getValue() == 0){
					run = true;
					break;
				}else{
					run = false;
				}
			}
			if (run==false){
				addBehaviour(new sendFin());
				done=true;
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return done;
		}
	}
	public void display(){
		for (int i=0;i<9;i++){
			for (int j=0;j<9;j++){
				System.out.print(sudoku.get(i*9+j).getValue()+" ");
			}
			System.out.println();
		}
	}

}

