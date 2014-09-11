package masSim.schedule;
import masSim.taems.*;
import raven.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Graph {
  private boolean debugFlag = false;
  private final List<Method> methods;
  private final List<MethodTransition> transitions;

  public Graph(List<Method> methods, List<MethodTransition> transitions) {
    this.methods = methods;
    this.transitions = transitions;
  }

  public List<Method> getMethods() {
    return methods;
  }

  public List<MethodTransition> getTransitions() {
    return transitions;
  }
  
  public void Print()
  {
	  String o = "digraph finite_state_machine {" + System.lineSeparator();
	  o += "rankdir=LR;" + System.lineSeparator();
	  o += "size=\"" + (6*this.getMethods().size()) + "," + (4*this.getMethods().size()) + "\"" + System.lineSeparator();
	  o += "node [shape = doublecircle]; S;" + System.lineSeparator();
	  o += "node [shape = point ]; qi" + System.lineSeparator();
	  o += "node [shape = circle];" + System.lineSeparator();
	  for (Method m : methods) {
	  }
	  for (MethodTransition t : transitions) {
		  Method s = t.getSource();
		  Method d = t.getDestination();
		  o += s.label.replaceAll(" ", "_") + s.getIndex() + "->" + d.label.replaceAll(" ", "_") + d.getIndex() + " [ label = \"\" ];";
	  }
	  o += "}";
	  File graphFile = new File("graph.gv");
	  try {
		  graphFile.createNewFile();
		  FileWriter writer = new FileWriter(graphFile, true);
			writer.write(o);
			writer.close();
		} catch (IOException ex) {
			System.err.println("Failed to write to log!");
		}
  }
  
} 