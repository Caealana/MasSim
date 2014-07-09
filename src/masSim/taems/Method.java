package masSim.taems;

import java.util.Date;
import java.util.Iterator;
import java.util.Observable;

import raven.Main;
import masSim.schedule.Scheduler;

public class Method extends Node implements IMethod {

	private boolean debugFlag = false;
	private static int Index = 1;
	private int index;
	private Outcome outcome;//Change to Vector
	public int deadline = 0;
	public double x;
	public double y;
	
	// Constructor
	public Method(String nm, double outcomeQuality, double x2, double y2){
		this(nm,outcomeQuality, x2, y2, 0);
	}
	public Method(String nm, double outcomeQuality, double x2, double y2, int dl){
		label = nm;
		outcome = new Outcome(outcomeQuality, -1, 0);
		index = Index++;
		deadline = dl;
		this.x = x2;
		this.y = y2;
	}
	public Method(Method m){
		this(m.label,m.outcome.getQuality(), m.x, m.y, m.deadline);
	}
	public boolean IsTask(){return false;}
	
	@Override
	public void MarkCompleted()
	{
		super.MarkCompleted();
		//Only print if its an actual task, and not an FSM connective created by the scheduler
		this.NotifyAll();
	}
	
	public DijkstraDistance getPathUtilityRepresentedAsDistance(DijkstraDistance distanceTillPreviousNode)
	{
		//This is distance calculation for this step only. Previous distance used for calculation, but not appended
		DijkstraDistance d = new DijkstraDistance(0,0,this.x, this.y);
		//If task can be performed, return utility value through the function. But if its deadline has passed
		//then return an abnormally large negative utility value to force Dijkstra to reject it.
		if ((distanceTillPreviousNode.duration+this.outcome.duration)>deadline && deadline!=0) 
			d.distance = 10000;
		else
		{
			//This can be any formula combining different outcomes and objectively comparing them
			double one = distanceTillPreviousNode.vector.x-this.x;
			double two = distanceTillPreviousNode.vector.y-this.y;
			d.distance = Math.sqrt(Math.pow(one, 2)+Math.pow(two,2));
			d.duration = d.distance;
			Main.Message(debugFlag, "[Method 57] Distance from ("+this.x+","+this.y+") to (" + distanceTillPreviousNode.vector.x + ","+distanceTillPreviousNode.vector.y+")");
		}
		this.outcome.quality = (10000-d.distance);
		return d;
	}
	
	
	public int getIndex() {return index;}
	public Outcome getOutcome(){return outcome;}
	public int getDeadline(){return deadline;}
	
	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + index;
	    return result;//this.label.hashCode();//overriding
	  }
	  
	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    Node other = (Node) obj;
	    if (this.hashCode()==other.hashCode()){
	    //if (this.label.equals(other.label)){
	    	//Main.Message("[Method 85] " + this.label + " found to be equal to " + other.label);
	    	return true;
	    }
	    else{
	    	//Main.Message("[Method 89] " + this.label + " not found to be equal to " + other.label);
	    	return false;
	    }
	  }

	  @Override
	  public String toString() {
	    return label;
	  }
	  
	  public String toStringLong() {
		return label + "(" + hashCode() + ")";
	  }
	  
	@Override
	public Iterator<Node> getSubtasks() {
		return null;
	}
		
	
}
