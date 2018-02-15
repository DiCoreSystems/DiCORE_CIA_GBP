package de.uniks.vs.evolution.graph.analysis.graphs;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by alex on 16/7/19.
 */
public class DirectedTreeMapGraph {

    // symbol table of linked lists
    private TreeMap<String, TreeSet<String>> st;

    // create an empty digraph
    public DirectedTreeMapGraph() {
        st = new TreeMap<String, TreeSet<String>>();
    }

    // add v to w's list of neighbors; self-loops allowed
    public void addEdge(String v, String w) {

        if (!st.containsKey(v))
            addVertex(v);

        if (!st.containsKey(w))
            addVertex(w);

        if (!st.get(w).contains(v)) {
            st.get(v).add(w);

            System.out.println("add Edge: " + v + "<-" + w + "    [" + v + "]" + st.get(v));
        }
        else
            System.out.println("add Edge: " + v + "<-" + w + "    failed! " + v +" contains " + w);
    }

    // add a new vertex v with no neighbors if vertex does not yet exist
    public void addVertex(String v) {
        System.out.println("new Vertex: "+v);

        if (!st.containsKey(v))
            st.put(v, new TreeSet<String>());
    }

    // return the degree of vertex v
    public int degree(String v) {
        if (!st.containsKey(v))
            return 0;
        else
            return st.get(v).size();
    }

    // return the array of vertices incident to v
    public Iterable<String> adjacentTo(String v) {
        if (!st.containsKey(v))
            return new TreeSet<String>();
        else
            return st.get(v);
    }

    // not very efficient, intended for debugging only
    public String toString() {
        String s = "";
        for (String v : st.keySet()) {
            s += v + ": " + st.get(v) + "\n";
        }
        return s;
    }

    public static void main(String[] args) {
        DirectedTreeMapGraph G = new DirectedTreeMapGraph();
        G.addEdge("A", "B");
        G.addEdge("A", "C");
        G.addEdge("C", "D");
        G.addEdge("D", "E");
        G.addEdge("D", "G");
        G.addEdge("E", "G");
        G.addVertex("H");
        System.out.println(G);
    }

    public TreeSet<String> getSt(String s) {
        return st.get(s);
    }
}
