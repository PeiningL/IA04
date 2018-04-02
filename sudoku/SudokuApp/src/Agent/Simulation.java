package Agent;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import Donnee.Case;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Simulation extends Agent{
	private  HashMap<AID,ArrayList<Case>> solution = new HashMap();
	protected void setup() {
		 // Printout a welcome message
		 addBehaviour(new recieveSubscribe());
		 addBehaviour(new recieveInform());
		 
	 } 
	private class recieveSubscribe extends CyclicBehaviour{
		@Override
		public void action() {
			MessageTemplate mtAlys = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
			ACLMessage subscribe = receive(mtAlys);
			if (subscribe != null) {
				ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
				//send aid of analyse agent to ENV
				req.addReplyTo(subscribe.getSender());
				req.addReceiver(getAID("ENV"));
				send(req);
			}
		}
	}
	private class recieveInform extends CyclicBehaviour{
		@Override
		public void action() {
			MessageTemplate mtEnv = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage fin = receive(mtEnv);
			if (fin != null) {
				System.out.println("SIMULATION Finish!!!!!!!!!!!!!!!!!!!!!");
				this.done();
			}
		}
	}

}
