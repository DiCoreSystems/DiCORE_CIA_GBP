package de.uniks.vs.evolution.graph.analysis;

import de.uniks.vs.evolution.graph.analysis.graphs.DirectedTreeMapGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.PDTGraph;
import de.uniks.vs.evolution.graphmodels.ModelManager;

import java.util.*;

/**
 * Created by alex on 16/8/8.
 */
public class SControlDependency {

    Vector<String> cd = new Vector <String>();
    Vector <Vector<String>> s = new Vector <Vector<String>>();
    List<List<String>> marked  = new ArrayList<List<String>>();

    DirectedTreeMapGraph pdt = new DirectedTreeMapGraph();
    DirectedTreeMapGraph acfg = new DirectedTreeMapGraph();

//    boolean exists = false;
//    List<List<String>> postdom1 = new Vector<>();
//    Vector<String> inters  = new Vector<String>();
//    Stack<String> currentPath  = new Stack<String>();          // the current path
//    TreeSet<String> verticesInPath  = new TreeSet<String>();   // the set of vertices on the path

    PDTGraph postDomainTree;
    Paths paths;

    public SControlDependency(PDTGraph postDomainTree, Paths paths) {
        this.postDomainTree = postDomainTree;
        this.paths = paths;
    }

    public void computeS(ModelManager manager) {
        postDomainTree.computePostDomTree(manager);
        pdt = postDomainTree.getG(); //PostDomTree.G
        acfg = paths.getG(); // AllPaths.G;
        System.out.println("Is 4 ancestor of 7 in pdt? " + Ancestor(pdt,"4","7"));
        System.out.println("Is 7 ancestor of 4 in pdt? " + Ancestor(pdt,"7","4"));
        Init_S();
        System.out.println("S= {"+ s +"}" );
        // Calcul de L
        Vector<String> L= new Vector<String>();

        for (int i = 0; i< s.size() ; i++){
            String A= s.get(i).get(0);
            String B= s.get(i).get(1);
            L.add(Least(A,B));
            boolean inclus;

            if (L.get(i)==A){
                inclus= true;
            }
            else{
                inclus=false;

            }
            marked.add(mark(inclus,L.get(i),B));
            cd.add(A);
        }
        System.out.println("L="+L);
        System.out.println("Marked="+marked);
        System.out.println("cd="+ cd);
    }

    private void Init_S() {
        for (String i : postDomainTree.getNodes())
            for (String j : acfg.adjacentTo(i)) {
                if(!Ancestor(pdt,j,i))
                {
                    Vector<String> v = new Vector<String>();
                    v.add(i);
                    v.add(j);
                    s.add(v);
                }
            }

    }

    private List<String> mark(boolean t, String a, String b) {
        List <String> trace = new ArrayList<String>();
        trace = paths.AllPath2(pdt, a, b);

        if (!t & !trace.isEmpty())
            trace.remove(0);
        ///System.out.println(trace);
        return trace;
    }

    private String Least(String a, String b) {
        Vector<String> Lista= new Vector<String>() ;
        for (String i : postDomainTree.getNodes()){
            if( Ancestor(pdt,i,a) && Ancestor(pdt,i,b))
                Lista.add(i);
        }

        String c = Lista.get(0);
        for (String i : Lista)
            for (String j : Lista)
            {
                if(i!=j & Ancestor(pdt,i,j) & Ancestor(pdt, c, j))
                    c=j;
            }
        return c;
    }

    public boolean Ancestor(DirectedTreeMapGraph pdt, String a, String b)
    {
        /** (Ancestor(G,a,b)==true) => a is an ancestor of b
         *                          => a is a parent of b
         */

        return(paths.AllPath(pdt, a, b));
    }



//  -------------------------------------------


//    public boolean Ancestor2(DirectedTreeMapGraph pdt, String a, String b)
//    {
//        boolean exists = pathExists(pdt, a, b);
//        return exists;
//    }
//
//    private boolean pathExists(DirectedTreeMapGraph pdt, String a, String b) {
//        Vector<String> postdomain  = new Vector<String>();
//
////      init inters
//        inters.removeAllElements();
//
//        for (String i : extCFGNodes) {
//            inters.add(i);
//        }
////        boolean exists = false;
//        searchPath(pdt, a, b);
//        postdomain = inters;
//        postdom1.add(copy(postdomain));
//
//        return exists;
//    }
//
//
//
//    private void searchPath(DirectedTreeMapGraph g, String v, String t) {
//
//        // add node v to current path from s
//        currentPath.push(v);
//        verticesInPath.add(v);
//
//        // found path from s to t - currently prints in reverse order because of stack
//        if (v.equals(t))
//        {
//            System.out.println("The path is " + currentPath);
//            if (!currentPath.isEmpty())
//            {
//                exists = true;
//            }
//            inters = intersection(inters, (Vector<String>) (currentPath));
//        }
//        // consider all neighbors that would continue path with repeating a node
//        else {
//
//            for (String w : g.adjacentTo(v)) {
////                System.out.println(v + "   " + t);
//
//                //TODO: Workaround fix not depend on node ids
//                boolean excluded = false;
//                for (String node :currentPath ) {
//
//                    if ("exit".equals(w) || "exit".equals(node))
//                        break;
//
//                    if (Long.valueOf(node)> Long.valueOf(w)) {
//                        excluded = true;
//                        break;
//                    }
//                }
//                if (excluded)
//                    continue;
//
//
//                if (!verticesInPath.contains(w))
//                    searchPath( g, w, t);
//            }
//        }
//        // done exploring from v, so remove from path
//        //trace.add((Vector<String>)path);
//        currentPath.pop();
//        verticesInPath.remove(v);
//    }

    public Vector<String> getCd() {
        return cd;
    }

    public List<List<String>> getMarked() {
        return marked;
    }
}
