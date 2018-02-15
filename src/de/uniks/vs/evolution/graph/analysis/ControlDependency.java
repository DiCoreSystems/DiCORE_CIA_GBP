package de.uniks.vs.evolution.graph.analysis;

import de.uniks.vs.evolution.graph.analysis.graphs.ControlDependenceGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.ControlFlowGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedGraph;
import de.uniks.vs.evolution.graphmodels.ModelManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by alex on 16/8/8.
 */
public class ControlDependency {


    Vector<String> nodes = new Vector<String>();
    Vector<String> nodeDrawn = new Vector<String>();
    ControlDependenceGraph cdg = new ControlDependenceGraph();
    SControlDependency sControlDependency;
    ControlFlowGraph controlFlowGraph;
    ArrayList<Dependency> dependencies = new ArrayList<>();


    public ControlDependency(SControlDependency sControlDependency, ControlFlowGraph controlFlowGraph){

        this.sControlDependency = sControlDependency;
        this.controlFlowGraph = controlFlowGraph;
    }

    public DirectedGraph computeControlGraphDependence(ModelManager model) {
        sControlDependency.computeS(model);
        controlFlowGraph.init_NodesSet();
        nodes = controlFlowGraph.getNodes();
        Vector <String> V = new Vector<String>();

        Vector<String> cd = sControlDependency.getCd();
        List<List<String>> marked = sControlDependency.getMarked();
        Vector<String> parallelList = controlFlowGraph.getParallelList();
        Vector<String> synclist = controlFlowGraph.getSynclist();

        if (cd.size() != 0){
            for (int i = 0; i< cd.size(); i++)
                for (int j=0; j<marked.get(i).size();j++)
                {
                    if(marked.get(i).get(j) != cd.get(i))
                        V.add(marked.get(i).get(j));
                }
            cdg.addVertex("entry");
            for (String i : nodes){
                if(!V.contains(i) & !i.equals("entry"))
                {		nodeDrawn.add(i);
                    cdg.addVertex(i);

//                    cdg.addEdge("entry", i, "entry "+ i +  " cd on");
                    cdg.addEdge("entry", i, "CD on");
                }}
            nodeDrawn.remove("entry");
            while (nodeDrawn.size() < nodes.size())
            {
                for (int i = 0; i< cd.size(); i++)
                    if (nodeDrawn.contains(cd.get(i)))
                    {	cdg.addVertex(cd.get(i));

                        for (int j=0; j<marked.get(i).size();j++)
                        {
                            nodeDrawn.add(marked.get(i).get(j));
                            //System.out.println(s.Marked.get(i).get(j)+" "+s.cd.get(i));
                            cdg.addVertex(marked.get(i).get(j));

                            if(synclist.contains(cd.get(i))) {
//                                cdg.addEdge(cd.get(i), Marked.get(i).get(j),cd.get(i)+" "+ Marked.get(i).get(j)+ "parallel");
                                cdg.addEdge(cd.get(i), marked.get(i).get(j), "parallel");
                                dependencies.add(new Dependency(cd.get(i), marked.get(i).get(j), Dependency.Type.PARALLEL_DEPENDENCY));

                            }
                            else
//                            cdg.addEdge(cd.get(i), Marked.get(i).get(j),cd.get(i)+" "+ Marked.get(i).get(j)+ "cd on");
                                cdg.addEdge(cd.get(i), marked.get(i).get(j), "CD on");

                        }
                    }
                //synchronized
                for (int i=0; i< parallelList.size()/2;i++){

                    //	System.out.println(Extended_Control_Flow_Graph.parallel_list.get(i)+" "+ Extended_Control_Flow_Graph.parallel_list.get(i+2));
//                    cdg.addEdge(parallel_list.get(i), parallel_list.get(i+2), new LabeledEdges.RelationshipEdge<String>(parallel_list.get(i), parallel_list.get(i+2), "synchronized-dependence"));
//                    cdg.addEdge(parallel_list.get(i), parallel_list.get(i+2), parallel_list.get(i) +" "+ parallel_list.get(i+2) + " synchronized-dependence");
                    cdg.addEdge(parallelList.get(i), parallelList.get(i+2),  "synchronized-dependence");
//                    cdg.addEdge(parallel_list.get(i+2), parallel_list.get(i), new LabeledEdges.RelationshipEdge<String>(parallel_list.get(i+2), parallel_list.get(i), "synchronized-dependence"));
//                    cdg.addEdge(parallel_list.get(i+2), parallel_list.get(i), parallel_list.get(i+2) +" "+ parallel_list.get(i)+ " synchronized-dependence");
                    cdg.addEdge(parallelList.get(i+2), parallelList.get(i),  "synchronized-dependence");

                    dependencies.add(new Dependency(parallelList.get(i), parallelList.get(i+2), Dependency.Type.SYNC_DEPENDENCY));
                    dependencies.add(new Dependency(parallelList.get(i+2), parallelList.get(i), Dependency.Type.SYNC_DEPENDENCY));

                }
            }}
        else{ // no control dependence
            cdg.addVertex("entry");

            for (String i : nodes){
                if (!i.equals("entry")){
                    cdg.addVertex(i);
//                    cdg.addEdge("entry", i, new LabeledEdges.RelationshipEdge<String>("entry", i, "cd on"));
//                    cdg.addEdge("entry", i, "entry "+ i + " cd on");
                    cdg.addEdge("entry", i,  "CD on");

                }
            }
        }
        System.out.println("CDG (Control Dependency Graph): " + cdg);
        return cdg;
    }

}
