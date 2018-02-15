package de.uniks.vs.evolution.graph.analysis.graphs;

import de.uniks.vs.evolution.graphmodels.GraphNode;

import java.util.ArrayList;

/**
 * Created by alex on 16/6/18.
 */
public class ProcessGraph {

    ArrayList<GraphNode> start = new ArrayList<>();
    //V2 represents the extCFGNodes of PG graph
    //V1 is a set of vertices

    public ArrayList<GraphNode> getNodes() {
        ArrayList<GraphNode> nodes = collectNodes(start);
        return nodes;
    }

    private ArrayList<GraphNode> collectNodes(ArrayList<GraphNode> nodes) {
        ArrayList<GraphNode> collectedNodes = new ArrayList<>();

        for (GraphNode node: nodes) {
            collectedNodes.add(node);
            ArrayList<GraphNode> next = node.getNext();
            collectedNodes.addAll(collectNodes(next));
        }
        return collectedNodes;
    }

    public ArrayList<GraphNode> adjacentTo(GraphNode i) {
        System.out.println("ProcessGraph::adjacentTo  " + i);
        return null;
    }
}


