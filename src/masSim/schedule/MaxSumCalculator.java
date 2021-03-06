package masSim.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.AbstractMap.SimpleEntry;

import raven.Main;
import raven.MeasureTime;

public class MaxSumCalculator extends BestAgentCalculatorBase {

	
	//public Map<Integer,String> agentsIndex = new HashMap<Integer,String>();
	
	int agentsIdIndex = 0;
	
	public MaxSumCalculator(String instanceName, int numberOfAgentsBeingNegotiatedWith, int numberOfTasks)
	{
		super(instanceName,numberOfAgentsBeingNegotiatedWith, numberOfTasks);
	}
	/*
	public String GetBestAgentPlain()
	{
		MeasureTime.Timer2.Start();
		String selectedAgent = "";
		int maxImprovement = -9999999;
		ScheduleQualities selectedQuality = null;
		boolean compareIdleAgentsOnly = false;
		for(ScheduleQualities ql : this.scheduleQualities)
		{
			if (!compareIdleAgentsOnly && ql.base==0)
			{
				//Found an idle agent, so this agent must be given preference over non idle ones
				compareIdleAgentsOnly = true;
			}
			int improvement = ql.incremental - ql.base;
			if (improvement > maxImprovement)
			{
				if (compareIdleAgentsOnly)
				{	
					if (ql.base==0)
					{
						maxImprovement = improvement;
						selectedQuality = ql;	
					}
				}
				else
				{
					maxImprovement = improvement;
					selectedQuality = ql;
				}
			}
		}
		MeasureTime.Timer2.Stop();
		System.out.println("Plain Calculation Time " + MeasureTime.Timer2.GetTotal());
		return this.agentsIndex.get(selectedQuality.agentVariableId);
	}*/
	
	/*
	public String GetBestAgent()
	{
		Main.Message(this.debugFlag, "Current Timer value: " + MeasureTime.Timer1.GetTotal());
		MeasureTime.Timer1.Start();
		Map<Integer,String> localAgentsIndex = new HashMap<Integer,String>();
		String selectedAgent = "";
		ArrayList<ScheduleQualities> scheduleQualities = new ArrayList<ScheduleQualities>();
		boolean compareIdleAgentsOnly = false;
		for(ScheduleQualities ql : this.scheduleQualities)
		{			
			if (!compareIdleAgentsOnly && ql.base==0)
			{
				//Found an idle agent, so this agent must be given preference over non idle ones
				compareIdleAgentsOnly = true;
			}
		}
		int newZeroBasedIndexForLocalSanitizedAgentList = 0;
		for(ScheduleQualities ql : this.scheduleQualities)
		{
			if (compareIdleAgentsOnly)
			{
				if (ql.base==0){
					scheduleQualities.add(ql);
					localAgentsIndex.put(newZeroBasedIndexForLocalSanitizedAgentList++, this.agentsIndex.get(ql.agentVariableId));
				}
			}
			else{
				scheduleQualities.add(ql);
				localAgentsIndex.put(newZeroBasedIndexForLocalSanitizedAgentList++, this.agentsIndex.get(ql.agentVariableId));
			}
		}
		//If all are idle, then add all
		if (scheduleQualities.size()==0){
			scheduleQualities.addAll(this.scheduleQualities);
			localAgentsIndex = this.agentsIndex;
		}
		
		//test.Main jmaxMain = new test.Main();
		//ArrayList<SimpleEntry<String,String>> result = jmaxMain.CalculateMaxSumAssignments(this.BuildMaxsumInput(scheduleQualities));
		String fileInput = this.booleanOptimizer.BuildOPBInputSingle(scheduleQualities);
		MeasureTime.Timer1.Stop();
		String filename = "E:\\EclipseWorkspace\\RoverSim\\TaskRepository\\problemDynamic.opb";
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	        new FileOutputStream(filename), "US-ASCII"))) {
			writer.write(fileInput);
		}
		catch(Exception ex)
		{
			System.out.print(ex);
		}
		MeasureTime.Timer1.Start();
		int[] result = this.booleanOptimizer.Solve(filename);
		/*for(SimpleEntry<String,String> ent : result)
		{
			if (ent.getValue().equals("1"))
			{
				selectedAgent = ent.getKey().replace("NodeVariable_", "");
			}
		}*/
		//if (selectedAgent=="")
		//	return null;
		//return this.agentsIndex.get(Integer.parseInt(selectedAgent));
		/*MeasureTime.Timer1.Stop();
		System.out.println("Boolean Optimization Time: " + MeasureTime.Timer1.GetTotal());
		return localAgentsIndex.get(result[0]);
	}*/
	
	/*
	 * This is the old method which does not use maxsum
	 * public String GetBestAgentSimpleWithoutMaxSum()
	{
		int maxImprovement = -9999999;
		ScheduleQualities selectedQuality = null;
		boolean compareIdleAgentsOnly = false;
		for(ScheduleQualities ql : this.scheduleQualities)
		{
			if (!compareIdleAgentsOnly && ql.base==0)
			{
				//Found an idle agent, so this agent must be given preference over non idle ones
				compareIdleAgentsOnly = true;
			}
			int improvement = ql.incremental - ql.base;
			Main.Message(debugFlag, "Quality Improvement for " + this.negotiatedTaskName + " with " + this.agentsIndex.get(ql.agentVariableId) + " is " + improvement);
			if (improvement > maxImprovement)
			{
				if (compareIdleAgentsOnly)
				{	
					if (ql.base==0)
					{
						maxImprovement = improvement;
						selectedQuality = ql;
					}
				}
				else
				{
					maxImprovement = improvement;
					selectedQuality = ql;
				}
			}
		}
		return this.agentsIndex.get(selectedQuality.agentVariableId);
	}*/
	
	public String getTaskName()
	{
		return this.instanceName;
	}
	
	private void AddLine(StringBuilder b, String line)
	{
		b.append(line + System.lineSeparator());
	}
	
	private String GetVariablesBlock(ArrayList<ScheduleQualities> scheduleQualities)
	{
		StringBuilder variables = new StringBuilder();
		for(ScheduleQualities ql : scheduleQualities)
		{
			//Number of agents (maxsum agent, not rover agents) is fixed, 
			//as well as variable states i.e. 2, assign task or not assign task
			AddLine(variables, "VARIABLE " + ql.agentVariableId + " 1 2");
		}
		return variables.toString();
	}
	
	
	private String GetFunctionsBlock(ArrayList<ScheduleQualities> scheduleQualities)
	{
		StringBuilder f = new StringBuilder();
		int totalVariables = scheduleQualities.size();
		
		String c = "CONSTRAINT 0 1";
		for(int i = 0; i<totalVariables; i++)
		{
			c += " " + i;
		}
		AddLine(f, c);
		
		
		GenerateLines(0, totalVariables, "", f, 0, scheduleQualities);
		
		
		return f.toString();
	}
	
	private int GetQualityForAgent(int agentVariable, ArrayList<ScheduleQualities> scheduleQualities)
	{
		for(ScheduleQualities ql : scheduleQualities)
		{
			if (agentVariable==(scheduleQualities.size() - 1 - ql.agentVariableId))//compare with inverted agent index because shorted decimal number points to first agent but should point to last
			{
				return ql.incremental;
			}
		}
		return 0;
	}
	
	private String SpaceOut(String input)
	{
		StringBuilder builder = new StringBuilder();
		char[] chars = input.toCharArray();
		for(char c : chars)
		{
			builder.append(c);
			builder.append(" ");
		}
		return builder.substring(0, builder.length()-1);
	}
	
	private int LogBase2(int variable)
	{
		return (int) (Math.log(variable)/Math.log(2));
	}
	
	private void GenerateLines(int currentVariableRepresentingAgentName, int totalVariables, String prefixBuiltSoFar, 
			StringBuilder f, int numberOfOnes, ArrayList<ScheduleQualities> scheduleQualities)
	{
		List<Integer> singleVariableValues = new ArrayList<Integer>();
		for(int i=0;i<totalVariables;i++)
		{
			singleVariableValues.add( (int) Math.pow(2, i));
		}
		for(int i=0;i<(Math.pow(2,totalVariables));i++)
		{
			int quality = 0;
			if (singleVariableValues.contains(i))
				quality = GetQualityForAgent(LogBase2(i), scheduleQualities);
			String line = "F" + String.format("%0"+totalVariables+"d", Integer.parseInt(Integer.toString(i, 2)));
			f.append(SpaceOut(line) + " " + quality);//Don't want quality to be spaced out
			f.append(System.lineSeparator());
		}
		/*//if (totalVariables)
		{
			
			
			
			f.append(prefixBuiltSoFar + " ");
			f.append("0");
			numberOfOnes = numberOfOnes + 0;
			if (numberOfOnes!=1) f.append(" 0");//schedule quality is also zero for this case
			else f.append(" " + quality);
			
			
			f.append("F");
			f.append(prefixBuiltSoFar + " ");
			f.append("1");
			numberOfOnes = numberOfOnes + 1;
			
			if (numberOfOnes!=1) f.append(" 0");//schedule quality is also zero for this case
			else f.append(" " + quality);
			f.append(System.lineSeparator());
		}
		else
		{
			GenerateLines(currentVariableRepresentingAgentName+1, totalVariables, prefixBuiltSoFar + " 0", f, numberOfOnes+0);
			GenerateLines(currentVariableRepresentingAgentName+1, totalVariables, prefixBuiltSoFar + " 1", f, numberOfOnes+1);
		}
		*/
	}
	

	
	
	//Deprecated in favor of BuildPseudoBooleanOptimizationInput
	public String BuildMaxsumInput(ArrayList<ScheduleQualities> scheduleQualities)
	{
		StringBuilder cop = new StringBuilder();
		AddLine(cop,"AGENT 1");
		cop.append(GetVariablesBlock(scheduleQualities));
		cop.append(GetFunctionsBlock(scheduleQualities));
		return cop.toString();
	}
}
