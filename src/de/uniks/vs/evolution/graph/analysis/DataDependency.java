package de.uniks.vs.evolution.graph.analysis;

import de.uniks.vs.evolution.graph.analysis.graphs.ControlFlowGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedGraph;
import de.uniks.vs.evolution.graphmodels.GraphEdge;
import de.uniks.vs.evolution.graphmodels.GraphItem;
import de.uniks.vs.evolution.graphmodels.GraphNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alex on 16/8/8.
 */
public class DataDependency {

    HashMap<Long, GraphItem> tasks = new HashMap<>();
    HashMap<Long, GraphItem> events = new HashMap<>();
    HashMap<Long, GraphItem> gateways = new HashMap<>();
    HashMap<Long, GraphItem> sequenceFlows = new HashMap<>();

    ControlFlowGraph controlFlowGraph;
    //    HashMap<String, String> cf = nodeCorrespandence;
    HashMap<String, String> cf;
    List<String> ddList = new ArrayList<String>();
    DirectedGraph ddg = new DirectedGraph();
    ArrayList<Dependency> dependencies = new ArrayList<>();

    public DataDependency(ControlFlowGraph controlFlowGraph) {
        this.controlFlowGraph = controlFlowGraph;
        events = controlFlowGraph.getEvents();
        tasks = controlFlowGraph.getTasks();
        gateways = controlFlowGraph.getGateways();
        sequenceFlows = controlFlowGraph.getSequenceFlows();
    }


    public void computeDataDependency(){

        cf = controlFlowGraph.getNodeCorrespondences();

        for(Long entry : tasks.keySet()) {
            GraphItem task = tasks.get(entry);
            GraphNode t = (GraphNode) task;
            int i = t.getInEdges().size();
            //  System.out.println("i"+i);
            while (i > 0){
                GraphEdge datainput = (GraphEdge) t.getInEdges().get(i-1);
                String de = String.valueOf(datainput.getSource().getId());

                for (Long dest : tasks.keySet()) {
                    GraphItem task2 = tasks.get(dest);
                    GraphNode t2 = (GraphNode) task2;
                    int j = t2.getOutEdges().size();

                    while (j>0){
                        GraphEdge dataoutput = (GraphEdge) t2.getOutEdges().get(j-1);
                        String a = String.valueOf(dataoutput.getTarget().getId());
                        // TODO: check
                        if (a.equalsIgnoreCase(de) && !(cf.get(String.valueOf(entry)).equalsIgnoreCase(cf.get(String.valueOf(dest))))) {
                            System.out.println("Data Dependency: " + cf.get(String.valueOf(entry))+ " --> " + cf.get(String.valueOf(dest)));
                            ddList.add(cf.get(String.valueOf(entry)));
                            ddList.add(cf.get(String.valueOf(dest)));
                            dependencies.add(new Dependency(String.valueOf(entry), String.valueOf(dest), Dependency.Type.DATA_DEPENDENCY));
                        }
                        j--;
                    }

                }
                i--;
            }
        }

        for (int i = 0; i< ddList.size(); i++){
            ddg.addVertex(ddList.get(i));
            ddg.addVertex(ddList.get(i+1));
//            DDG.addEdge(DDList.get(i+1),DDList.get(i), DDList.get(i+1)+ " "+ DDList.get(i) + " DD on");
            ddg.addEdge(ddList.get(i+1),ddList.get(i), "DD on");
            i++;
        }
    }

    public DirectedGraph getDdg() {
        return ddg;
    }
}
