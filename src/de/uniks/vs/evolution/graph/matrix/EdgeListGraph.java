package de.uniks.vs.evolution.graph.matrix;

/**
 * Created by alex on 16/6/7.
 */
public class EdgeListGraph {

    private int[] edgeList = {0,0};

    public int addNode() {
        return ++edgeList[0];
    }
}
