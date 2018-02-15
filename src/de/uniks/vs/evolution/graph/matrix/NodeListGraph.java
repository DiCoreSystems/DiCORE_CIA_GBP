package de.uniks.vs.evolution.graph.matrix;

/**
 * Created by alex on 16/6/7.
 */
public class NodeListGraph {

    private int[] nodeList = {0,0};

    public int addNode() {
        int[] newNodeList =    new int[nodeList.length+1]; System.arraycopy(nodeList,0, newNodeList,0,nodeList.length);
        newNodeList[newNodeList.length-1]=0;
        nodeList = newNodeList;
        nodeList[0]++;
        return nodeList[0];
    }
}
