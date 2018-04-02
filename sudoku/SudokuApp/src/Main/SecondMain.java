package Main;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class SecondMain {
	//local-port de conteneur principal est le port du secondaire
	private static String SECONDARY_PROPERTIES_FILE = "Main/propriete2";
	private static String FILE_PATH = "src/Donnee/Grille5";
	
	public static void main(String[] args){
		Runtime rt = Runtime.instance();
		Profile p = null;
		try {
			Object path = FILE_PATH;
			p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
			ContainerController cc = rt.createAgentContainer(p);
			AgentController ac = cc.createNewAgent("SIMULATION",
					"Agent.Simulation", null);
			ac.start();
			ac = cc.createNewAgent("ENV",
					"Agent.Environement", new Object[]{path});
			ac.start();
			for (int i=1;i<28;i++){
				ac = cc.createNewAgent("ANALYSE"+i,
						"Agent.Analyse", null);
				ac.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
