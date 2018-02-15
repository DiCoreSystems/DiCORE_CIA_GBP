package de.uniks.vs.evolution.graph.analysis;

import de.uniks.vs.evolution.graph.analysis.graphs.ControlFlowGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedTreeMapGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.PDTGraph;
import de.uniks.vs.evolution.graphmodels.GraphModel;
import de.uniks.vs.evolution.graphmodels.ModelManager;

import java.util.Vector;

/**
 * Created by alex on 16/7/18.
 */
public class DependencyAnalysis {

    ControlDependency controlDependency;
    DataDependency dataDependency;
    ControlFlowGraph controlFlowGraph;
    SControlDependency sControlDependency;
    PDTGraph postDomainTree;
    Paths paths;



    enum Type {
        S,
        L,
        CD,
        MARKED;

    }

     Vector<String> nodes;
//    Vector<String> cgdNodes = new Vector<String>();
//    HashMap<String, String> nodeCorrespandence = new HashMap<>();

    DirectedTreeMapGraph g_Dependency = new DirectedTreeMapGraph();

    public DependencyAnalysis() {

        controlFlowGraph = new ControlFlowGraph();
        dataDependency = new DataDependency(controlFlowGraph);
        paths = new Paths(controlFlowGraph);
        postDomainTree = new PDTGraph(paths, controlFlowGraph);
        sControlDependency = new SControlDependency(postDomainTree, paths);
        controlDependency = new ControlDependency(sControlDependency, controlFlowGraph);
    }


    public DirectedGraph computeDependencyGraph(ModelManager manager) {

        GraphModel model = manager.getModel(manager.getLastModelID());
        System.out.println(model.toString());

        DirectedGraph dg = controlDependency.computeControlGraphDependence(manager);
        dataDependency.computeDataDependency();

        nodes = controlFlowGraph.getNodes();

        for (String i: nodes)

            for (String j: nodes){

                try {
//                    if(!(DDG.getEdge(i, j)).toString().isEmpty()){
                    if(!(dataDependency.getDdg().getEdge(i, j) == null)) {
                        dg.addVertex(i);
                        dg.addVertex(j);
//                        DG.addEdge(j, i, j+" "+ i +" "+ (DDG.getEdge(i, j)).toString());
//                        DG.addEdge(j, i, j+" "+ i +" "+ (DDG.getEdge(i, j)));
                        dg.addEdge(j, i, dataDependency.getDdg().getEdge(i, j).getRelationshipEdge().getStringRelationshipEdge());

                    }

                } catch (Exception e) { }
            }



        for (String i : nodes)
            for (String j : nodes){
                try {
//                    if(!(DG.getEdge(i, j)).toString().isEmpty()){
                    if(!(dg.getEdge(i, j) == null)){
                        g_Dependency.addEdge(i, j);
                    }}
                catch (Exception e) {
                    //if empty nothing to do
                }

            }

        return dg;
    }

    public void computePostDomTree(ModelManager manager) {
        controlDependency.computeControlGraphDependence(manager);
    }

    public DirectedTreeMapGraph getPDT() {
        return postDomainTree.getPDT();
    }

    public boolean ancestor(DirectedTreeMapGraph pdt, String s, String s1) {
        return sControlDependency.Ancestor(pdt,s,s1);
    }
}
