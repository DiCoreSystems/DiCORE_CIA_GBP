package de.uniks.vs.evolution.graph.analysis;

import de.uniks.vs.evolution.graph.analysis.graphs.ControlFlowGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedTreeMapGraph;
import de.uniks.vs.evolution.graphmodels.ModelManager;

import java.util.*;

/**
 * Created by alex on 16/8/8.
 */
public class Paths {

    Vector<String> intersec  = new Vector<String>();
    List<List<String>> postdom = new ArrayList<List<String>>();
    Stack<String> path  = new Stack<String>();   // the current path
    TreeSet<String> onPath  = new TreeSet<String>();  // the set of vertices on the path
//    Vector<String> allPathsNodes = new Vector<String>();
    List<String> chemin  = new ArrayList<String>();
    List<List<String>> trace  = new ArrayList<List<String>>();
    DirectedTreeMapGraph g_Path = new DirectedTreeMapGraph();
    boolean empty;
    private ControlFlowGraph controlFlowGraph;
    private Vector<String> nodes;

    public Paths(ControlFlowGraph controlFlowGraph) {

        this.controlFlowGraph = controlFlowGraph;
    }

    public boolean AllPath(DirectedTreeMapGraph g, String s, String t) {
        // public static Vector<String> intersec  = new Vector<String>();
        //public static Vector<String> intersecBase  = new Vector<String>();
        Vector<String> postdomini  = new Vector<String>();
        //public static Vector<Vector<String>> postdom= new Vector<Vector<String>>();

        //Vector<String> intersecBase  = new Vector<String>();
        //	System.out.println("intersec : "+intersec);
        //System.out.println("postdom : "+ postdom);
        init_intersec();
        //System.out.println(intersec);
        empty = false;
        enumerate(g, s, t);
        //System.out.println("exixste un chemein? "+ empty);
        postdomini = intersec;
        ///System.out.println("postdomini avant: "+ postdomini);

        ///System.out.println("postdom : juste avant"+ postdom);
        postdom.add(copy(postdomini));
        return empty;
    }

    public void computeAllPaths(ModelManager manager) {
        //init_Edges();
        controlFlowGraph.computeControlFlowGraph(manager);
//        extCFGNodes = Extended_Control_Flow_Graph.NodeSet;
        nodes = controlFlowGraph.getNodes();
        g_Path = controlFlowGraph.getG();
//        allPathsNodes = extCFGNodes;
//        G=Extended_Control_Flow_Graph.G;
        //System.out.println(G);
        // AllPaths allpaths1 = new AllPaths(G, "1", "exit");
        //  System.out.println("POSTDOM(1) = " + intersec);
        String[] str = new String[nodes.size()+1];
        str[0]= "entry"; //,"1","2","3","4","5","6","7","exit"};
        //List<String> str= new ArrayList<String>();

        for (int i = 1; i< nodes.size()-1; i++)
        {
            str[i]=String.valueOf(i);

        }
        str[nodes.size()-1]="exit";
        //String[] str = {"entry","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"
        //		,"16","17","18","19","exit"};



        for (int i=0; i<str.length-1;i++)
        {
            AllPath(g_Path, str[i], "exit");
            //System.out.println();

        }
        System.out.println("List of postdoms : "+postdom);
			/*int i= 0;
			while (i<trace.size())
			{	if (trace.get(i).isEmpty())
				trace.remove(i);
			else
			i++;
			}
			// System.out.println("Trace " + trace );
			 //System.out.println(i + "vs" + trace.size());

            // System.out.println("*******" + AllPath2(G,"6","exit"));
            // System.out.println("*******" + AllPath(G,"6","exit"));
*/
    }


    private void init_intersec() {
        intersec.removeAllElements();
        for (String i : nodes) {
            intersec.add(i);
        }
    }

    private List<String> copy(Vector<String> postdomini) {
        List<String> listCopy = new ArrayList<String>();
        for(int i=0; i<postdomini.size(); i++)
            listCopy.add(postdomini.get(i));
        return listCopy;
    }

    // use DFS
    private void enumerate(DirectedTreeMapGraph g, String v, String t) {

        // add node v to current path from s
        path.push(v);
        onPath.add(v);

        // found path from s to t - currently prints in reverse order because of stack
        if (v.equals(t))
        {

            System.out.println("The path is " + path);
            //	Vector <String> chemin3 = new Vector <String> ();
            //	chemin3 = intersection((Vector<String>) (path), (Vector<String>) (path));
            //	trace.add(chemin3);
            // System.out.println("tace "+ trace);
            //System.out.println(oldPath.intersects(path));
            if (!path.isEmpty())
            {
                empty = true;

                //rrrrr.removeAllElements();

            }
            intersec = intersection(intersec, (Vector<String>) (path));
        }
        // consider all neighbors that would continue path with repeating a node
        else {
            for (String w : g.adjacentTo(v)) {
                if (!onPath.contains(w))
                    enumerate( g, w, t);
            }
        }
        // done exploring from v, so remove from path
        //trace.add((Vector<String>)path);
        path.pop();
        onPath.remove(v);
    }

    private <T> Vector<T> intersection(Vector<T> v1, Vector<T> v2) {
        Vector<T> vector = new Vector<T>();

        for (T t : v1) {
            if(v2.contains(t)) {
                vector.add(t);
            }
        }

        return vector;
    }

    public  List <String> AllPath2 (DirectedTreeMapGraph g, String s, String t) {
        enumerate2(g, s, t);
        //System.out.println("xchemin"+ chemin);
        return chemin;
    }

    private  List <String> enumerate2(DirectedTreeMapGraph g, String v, String t) {

        List<String> chemin2 = new ArrayList<String>();
        path.push(v);
        onPath.add(v);
        if (v.equals(t)) {
            chemin2 = (List<String>) (path);
            ///System.out.println( "chemin2 == "+chemin2);

            if (!chemin2.isEmpty()) {    ///System.out.println("cc");
                ///System.out.println( "chemin2 == "+chemin2);
                chemin = copy((Vector<String>) (path));
                trace.add(chemin);
            }
        } else {
            for (String w : g.adjacentTo(v)) {
                if (!onPath.contains(w)) enumerate2(g, w, t);
            }
        }
        path.pop();
        onPath.remove(v);
        return chemin2;
    }

    public DirectedTreeMapGraph getG() {
        return g_Path;
    }

    public List<List<String>> getPostdom() {
        return postdom;
    }

    public Vector<String> getNodes() {
        return nodes;
    }
}
