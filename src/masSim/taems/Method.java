package masSim.taems;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Observable;

import raven.Main;
import raven.math.Vector2D;
import masSim.schedule.Scheduler;
import masSim.world.AgentMode;
import masSim.world.WorldState;

public class Method extends Node implements IMethod {

	private boolean debugFlag = false;
	private static int Index = 3;
	private int index;
	private Outcome outcome;//Change to Vector
	public int deadline = 0;
	public double x;
	public double y;
	private double heuristicQuality = 40000;
	public static String FinalPoint = "Finish";
	public static String StartingPoint = "Start";
	public ArrayList<Interrelationship> Interrelationships;
	public double DijkstraSavedQualityTillThisStep = 0;
	// Constructor
	public Method(String nm, double outcomeQuality, double x2, double y2){
		this(nm,outcomeQuality, 0, x2, y2, 0, null);
	}
	
	public Method(String nm, double outcomeQuality, double outcomeDuration, double x2, double y2, int dl, ArrayList<Interrelationship> ir){
		label = nm;
		if (nm.contains("-")) Main.Message(this, this.debugFlag, "Error: Method name cannot contain a dash");
		outcome = new Outcome(outcomeQuality, outcomeDuration, 0);
		if (nm == StartingPoint) index = 1;
		else if (nm == FinalPoint) index = 2;
		else index = Index++;
		deadline = dl;
		this.x = x2;
		this.y = y2;
		this.Interrelationships = new ArrayList<Interrelationship>();
		if (ir!=null) this.Interrelationships = ir;
	}
	
	public boolean isStartMethod()
	{
		return index == 1;
	}
	
	public boolean isEndMethod()
	{
		return index == 2;
	}
	
	public Method(String nm, double outcomeQuality, double outcomeDuration, double x2, double y2, int dl)
	{
		this(nm,outcomeQuality, outcomeDuration, x2, y2, 0, null);
	}
	public Method(Method m){
		this(m.label,m.outcome.getQuality(), m.outcome.getDuration(), m.x, m.y, m.deadline, m.Interrelationships);
	}
	public boolean IsTask(){return false;}
	@Override
	public void AddInterrelationship(Interrelationship relationship)
	{
		this.Interrelationships.add(relationship);
	}
	public Vector2D getPosition()
	{
		return new Vector2D(this.x, this.y);
	}
	@Override
	public synchronized void MarkCompleted()
	{
		super.MarkCompleted();
		WorldState.CompletedMethods.add(this);
	}
	
	public DijkstraDistance getPathUtilityRepresentedAsDistance(DijkstraDistance distanceTillPreviousNode, Vector2D agentPos)
	{
		//This is distance calculation for this step only. Previous distance used for calculation, but not appended
		DijkstraDistance d = new DijkstraDistance(1,0,this.x, this.y, this.label);
		if (this.label==Method.FinalPoint)
		{
			return d;
		}
		//If task can be performed, return utility value through the function. But if its deadline has passed
		//then return an abnormally large negative utility value to force Dijkstra to reject it.
		double totalDurationTillNow = distanceTillPreviousNode.duration+this.outcome.duration;
		if ((totalDurationTillNow)>deadline && deadline!=0) 
		{
			d.quality = Long.MIN_VALUE;
			Main.Message(debugFlag, "[Method 54] Using infinitely negative utility because of " + deadline + " deadline breakage by duration " + totalDurationTillNow);
		}
		else
		{
			//Main.Message(debugFlag, "[Method 54] Deadline " + deadline + " will be met by " + totalDurationTillNow);
			double distance = Math.round(distanceTillPreviousNode.position.distance(new Vector2D(this.x, this.y)));
			d.quality = this.outcome.quality - distance;
			Main.Message(false, "task distance = " + distance + " total quality = " + d.quality);
			if (d.quality>heuristicQuality)
			{
				//Revisit heuristic logic
				d.quality = Long.MIN_VALUE;
			}
			d.duration = distance;
			this.outcome.quality = d.quality;
			Main.Message(false, "[Method 57] Distance from (" + distanceTillPreviousNode.position.x + ","+distanceTillPreviousNode.position.y+ ") to " + this.label + " ("+this.x+","+this.y+") ");
		}
		Main.Message(debugFlag, "[Method 66] Quality determined for " + this.label + " is " + d.quality );
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
