package de.uniks.vs.evolution.graph.analysis.graphs;

import de.uniks.vs.evolution.graph.analysis.Paths;
import de.uniks.vs.evolution.graphmodels.ModelManager;

import java.util.List;
import java.util.Vector;

/**
 * Created by alex on 16/8/8.
 */
public class PDTGraph {

    Vector<String> nodes = new Vector<String>();
    DirectedGraph gr = new DirectedGraph();
    DirectedTreeMapGraph g_PostDomTree = new DirectedTreeMapGraph();
    private Paths paths;
    private ControlFlowGraph controlFlowGraph;


    public PDTGraph(Paths paths, ControlFlowGraph controlFlowGraph) {

        this.paths = paths;
        this.controlFlowGraph = controlFlowGraph;
    }

    public void computePostDomTree(ModelManager manager) {
        // create a JGraphT graph
        paths.computeAllPaths(manager);
        List<List<String>> dpost = paths.getPostdom();
        Vector<String> nonDesigned = new Vector<String>() ;
        Vector<String> NV = new Vector<String>();
        Vector<String> LastErased = new Vector<String>();
        init_nodeSet();
        nonDesigned= nodes;
        nonDesigned.remove("exit");
        LastErased.add("exit");
        int exit_condition= (dpost.size())-1;

        while (exit_condition>0)
        {
            //	System.out.println("not empty");
            Vector<String> nodesErased = new Vector<String>(LastErased);
            for (String i : nodesErased) {

                //boolean remove = false;

                for (String j : nonDesigned) {
                    int l = init(j);

                    //	pause();

                    //	while(remove==true)
                    //	remove=false;

                    if (dpost.get(l).contains(i))
                    {

                        System.out.println(l + " contains " + i);
                        System.out.println((dpost.get(l).size()));
                        System.out.println(dpost.get(l).remove(i));
                        System.out.println((dpost.get(l).size()));
                        System.out.println(dpost.get(l));

                        dpost.get(l).remove(i);
                        if ((dpost.get(l)).size() == 1)

                        {

                            //nonDesigned.remove(j);
                            gr.addVertex(j);
                            gr.addVertex(i);
                            gr.addEdge(i, j);
                            g_PostDomTree.addEdge(i, j);
                            NV.add(j);
                            //System.out.println("gr= "+gr);
                            // pause();
                            exit_condition--;

                            // System.out.println("s=" + exit_condition);

                        }



                    }
                }

            }
            LastErased=NV;
            //System.out.println("new while");
            //pause();
        }
        System.out.println("PDT (Post Domain Tree) = " + gr.toStringAsTree(manager, controlFlowGraph.getNodeCorrespondences()));
    }

    public DirectedTreeMapGraph getPDT() {
        return g_PostDomTree;
    }

    public DirectedGraph getPDT_Info() {
        return gr;
    }

    private void init_nodeSet() {
        nodes = paths.getNodes();
    }


    private int init(String i) {
        int j;
        if(i=="entry")
            j = 0;
        else if (i=="exit")
            j = nodes.size();
        else
            j= Integer.parseInt(i);
        return j;
    }

    public DirectedTreeMapGraph getG() {
        return g_PostDomTree;
    }

    public Vector<String> getNodes() {
        return nodes;
    }
}
