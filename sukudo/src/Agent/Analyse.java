package Agent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Donnee.Case;
import Donnee.GroupeCase;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Analyse extends Agent {
	
	protected void setup() {
		 // Printout a welcome message
		 //System.out.println(getAID().getName()+"is installed");
		 addBehaviour(new recieveRequest());
		 addBehaviour(new subscription());
		 addBehaviour(new recieveCancel());
		 
	}
	
	private class recieveRequest extends CyclicBehaviour{
		@Override
		public void action() {
			MessageTemplate mtEnv = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage reqCalcul = receive(mtEnv);
			GroupeCase target = new GroupeCase();
			if (reqCalcul != null) {
				target = target.deserialisation(reqCalcul.getContent());
				//traitement
				target = calcul(target);
				ACLMessage result = reqCalcul.createReply();
				result.setPerformative(ACLMessage.INFORM);
				//serialisation
				result.setContent(target.toJson());
				//System.out.println("result return by agent "+target.toJson());
				send(result);
			}
		}
	}
	
	private GroupeCase calcul(GroupeCase groupe){
		groupe.getGroupe().stream().forEach(cellule ->{
			ArrayList<Integer> selfList = cellule.getListPoss();
			//règle 1
			if (selfList.size()==1){
				cellule.setValue(selfList.get(0));
				cellule.getListPoss().clear();
			}else{
				//règle 2 ok
				groupe.getGroupe().stream().forEach(cellule2 ->{
					int otherValue = cellule2.getValue();
					if (otherValue!=0){
						if (selfList.contains(otherValue)){
							selfList.remove(selfList.indexOf(otherValue));
							cellule.setListPoss(selfList);
						}
					}
				});
				//règle 3 
				selfList.stream().forEach(possib->{
					if (groupe.getGroupe().stream().filter(c -> (!c.getListPoss().isEmpty())&&c.getListPoss().contains(possib)).collect(Collectors.toList()).isEmpty()){
						cellule.setValue(possib);
						cellule.getListPoss().clear();
					}
				});
			}
		});
		//règle 4
		List<Case> groupe2 = new ArrayList<Case>();
		groupe2 = groupe.getGroupe().stream().filter(c -> c.getListPoss().size()==2).collect(Collectors.toList());
		if (groupe2.size()==2){
			if (groupe2.get(0).getListPoss().equals(groupe2.get(1).getListPoss())){
				int value1 = groupe2.get(0).getListPoss().get(0);
				int value2 = groupe2.get(0).getListPoss().get(1);
				List<Case> toRemove = groupe.getGroupe().stream().filter(c -> (c.getListPoss().size()>2)).collect(Collectors.toList());
				if (!toRemove.isEmpty()){
					toRemove.stream().forEach(c1 ->{
						ArrayList<Integer> selfList = c1.getListPoss();
						int index1 = c1.getListPoss().indexOf(value1);
						int index2 = c1.getListPoss().indexOf(value2);
						if (selfList.contains(index1))
							selfList.remove(selfList.indexOf(index1));
						if (selfList.contains(index2))
							selfList.remove(selfList.indexOf(index2));
						c1.setListPoss(selfList);
					});	
				}
			}
		}
		return groupe;
	}
	
	
	private class subscription extends OneShotBehaviour{
		@Override
		public void action() {
			ACLMessage sub = new ACLMessage(ACLMessage.SUBSCRIBE);
			sub.setContent("already installed "+getAID());
			sub.addReceiver(getAID("SIMULATION"));
			send(sub);
		}
	}
	
	private class recieveCancel extends CyclicBehaviour{
		@Override
		public void action() {
			MessageTemplate mtEnv = MessageTemplate.MatchPerformative(ACLMessage.CANCEL);
			ACLMessage cancel = receive(mtEnv);
			GroupeCase target = new GroupeCase();
			if (cancel != null) {
				this.myAgent.doDelete();
			}
		}
	}
}
