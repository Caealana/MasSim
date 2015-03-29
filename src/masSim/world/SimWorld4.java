package masSim.world;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import raven.Main;
import raven.math.Vector2D;
import masSim.world.*;
import masSim.world.WorldEvent.TaskType;
import masSim.taems.*;

public class SimWorld4 implements WorldEventListener {

	private ArrayList<IAgent> agents;
	private ArrayList<Task> tasks;
	private ArrayList<WorldEventListener> listeners;
	private IAgent mainAgent;
	
	public SimWorld4(WorldEventListener eventListener)
	{
		agents = new ArrayList<IAgent>();
		tasks = new ArrayList<Task>();
		listeners = new ArrayList<WorldEventListener>();
		listeners.add(eventListener);
		listeners.add(this);
		
		//Initialize two agents, and specify their initial positions
		Agent agentOne = new Agent("Helicopter0", true, 40, 100, listeners);
		Agent agentTwo = new Agent("Helicopter1", false, 40, 200, listeners);
		mainAgent = agentOne;
		agentOne.AddChildAgent(agentTwo);
		agents.add(agentOne);
		//agents.add(agentTwo);
				
		eventListener.RegisterMainAgent(agentOne);
	}
	
	public void initAgents()
	{
		
						
							Task taskHierarchy = new Task("TaskTree", new SeqSumQAF(), mainAgent);
							
							Task task1 = new Task("Task2",new SumAllQAF(), mainAgent, new Method[]{
								new Method("M1",8,5,100,100,0,null),
								new Method("M2",10,10,200,100,0,null),
								new Method("M3",12,7,300,100,0,null)
							});
							
							Task task2 = new Task("Task1",new ExactlyOneQAF(), mainAgent, new Method[]{
								new Method("M4",10,0,400,50,0,null),
								new Method("M5",70,0,400,150,0,null)
							});		
							
							taskHierarchy.addTask(task1);
							taskHierarchy.addTask(task2);
							
							mainAgent.assignTask(taskHierarchy);						
							
							//Method destination1 = new Method("Visit Destination 1",10,-1, 300,50, -1, null);
							//destination1.AddInterrelationship(new Interrelationship(gasStation, destination1, new Outcome(100,1,1)));
							
		//Start Agents
		Iterator it = agents.iterator();
		while(it.hasNext())
		{
			Agent agent = (Agent) it.next();
			Thread agentThread = new Thread(agent,agent.label);
			agentThread.start();
		}
		//agentOne.assignTask(new Task("Emergency Station",new SumAllQAF(), new Method("Emergency Method",1,300,90), null));
	}
	
	public synchronized void addListener(WorldEventListener sl) {
        listeners.add(sl);
    }
 
    public synchronized void removeListener(WorldEventListener sl) {
        listeners.remove(sl);
    }

	@Override
	public void HandleWorldEvent(WorldEvent event) {
		if (event.taskType==TaskType.METHODCOMPLETED)
		{
			Main.Message(true, "[SimWorld4] " + event.methodId + " completed");
			//mainAgent.assignTask(new Task(event.methodId + " Task",new SumAllQAF(), new Method(event.methodId + " Method",1,1,event.xCoordinate,event.yCoordinate, 4, null), mainAgent));
		}
	
	}

	@Override
	public void RegisterMainAgent(IAgent agent) {
		// TODO Auto-generated method stub
		
	}
    
    

}