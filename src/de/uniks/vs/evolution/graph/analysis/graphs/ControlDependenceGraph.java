package de.uniks.vs.evolution.graph.analysis.graphs;

/**
 * Created by alex on 16/6/18.
 */
public class ControlDependenceGraph extends DirectedGraph {

    enum type {
        common_control_dependency,
        parallel_dependency,
        synchronized_dependency;
    }


//    //ψ2 : E2 → Ψ2 is a function that maps edges to types: common-control dependency, parallel dependency, synchronized dependency
//    HashMap<String, type> edgeType = new HashMap<>();
//
//    //θ2 : V2 → Θ2 is a function that maps vertices to labels (numbering function)
//    HashMap<Integer, String> verticeLabel = new HashMap<>();
//
//    //ω2 : E2 → Ω2 is a function that maps edges to labels
//    HashMap<String, String> edgeLabel = new HashMap<>();



    //E2 represent a set of edges
//    public DependencyEdge getEdge(GraphNode i, GraphNode j) {
//        System.out.println("ControlDependenceGraph::getEdge" + i + "  " + j );
//        return null;
//    }


//    public void setEdgeLabel(String s, String s1) {
//        System.out.println("ControlDependenceGraph::setEdgeLabel" + s + "  " + s1 );
//
//    }

}
