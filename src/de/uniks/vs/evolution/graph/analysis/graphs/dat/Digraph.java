package de.uniks.vs.evolution.graph.analysis.graphs.dat;

public class Digraph {

    // symbol table of linked lists
    private ST<String, SET<String>> st;

    // create an empty digraph
    public Digraph() {
        st = new ST<String, SET<String>>();
    }

    // add v to w's list of neighbors; self-loops allowed
    public void addEdge(String v, String w) {

        if (!st.contains(v))
            addVertex(v);

        if (!st.contains(w))
            addVertex(w);
        st.get(v).add(w);
    }

    // add a new vertex v with no neighbors if vertex does not yet exist
    public void addVertex(String v) {

        if (!st.contains(v))
            st.put(v, new SET<String>());
    }

    // return the degree of vertex v
    public int degree(String v) {
        if (!st.contains(v)) return 0;
        else                 return st.get(v).size();
    }

    // return the array of vertices incident to v
    public Iterable<String> adjacentTo(String v) {
        if (!st.contains(v)) return new SET<String>();
        else                 return st.get(v);
    }

    // not very efficient, intended for debugging only
    public String toString() {
        String s = "";
        for (String v : st) {
            s += v + ": " + st.get(v) + "\n";
        }
        return s;
    }


    public static void main(String[] args) {
        Digraph G = new Digraph();
        G.addEdge("A", "B");
        G.addEdge("A", "C");
        G.addEdge("C", "D");
        G.addEdge("D", "E");
        G.addEdge("D", "G");
        G.addEdge("E", "G");
        G.addVertex("H");
        System.out.println(G);
    }

}
