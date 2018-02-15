//package de.uniks.vs.evolution.graph.analysis.old;
//
//
//import de.uniks.vs.evolution.graph.analysis.graphs.ControlDependenceGraph;
//import de.uniks.vs.evolution.graph.analysis.graphs.PDTGraph;
//import de.uniks.vs.evolution.graph.analysis.graphs.ProcessGraph;
//import de.uniks.vs.evolution.graphmodels.GraphNode;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Created by alex on 16/6/18.
// */
//public class Dependency {
//
//    enum Type {
//        S,
//        L,
//        CD,
//        Marked
//    }
//
//    public void GeneratingControlDependencyGraph(ProcessGraph pg) {
//        ControlDependenceGraph cdg = new ControlDependenceGraph();
//        HashMap<GraphNode, ArrayList<ArrayList<GraphNode>>> postDom = computePostdoms(pg);
//        PDTGraph PDT = generatePDT(postDom, pg);
//        ArrayList<ArrayList<GraphNode>> S = constructSLMarkedCD(Type.S, PDT, pg);
//        ArrayList<ArrayList<GraphNode>> L = constructSLMarkedCD(Type.L, PDT, pg);
//        ArrayList<ArrayList<GraphNode>> CD = constructSLMarkedCD(Type.CD, PDT, pg);
//        ArrayList<ArrayList<GraphNode>> Marked = constructSLMarkedCD(Type.Marked, PDT, pg);
////        List SGs ←− SearchSubgraphs(PG,(′g′,”AND − Split”), (′g′, ”AND − Join”))
////        V2(CDG) ←− V2(CDG) ∪ {”entry”}
////        V2(CDG) ←− V1(P G)\ {marked(i)\CD(i)}
////      for all v ∈ V2(CDG) do
////            E2(CDG) ←− E2(CDG) ∪ {(”entry”, v)}
////      end for
////      for all i ∈ CD do
////          if CD(i) ∈ V2(CDG) then
////              for all j ∈ Marked(i) do
////                  V2(CDG) ←− V2(CDG) ∪ {j} E2(CDG) ←− E2(CDG) ∪ {(CD(i), j)}
////                  if(j∈V(SG)|SG⊂SGs) then
////                      CDG.ω2((CD(i), j)) = ”parallel − dependency”
////                  else
////                      CDG.ω2((CD(i), j)) = ”commoncontrol − dependency”
////                  end if
////              end for
////          end if
////      end for
////      for all SG ∈ SGs do
////          {P1, P2} ←− Searchpaths(SG, (′g′, ”AND − Split”), (′g′, ”AND − Join”))
////          E2(CDG) ←− (P1(0), P2(0))
////          CDG.ω2((P1(0), P2(0))) = ”synchronized − dependency”
////          E2(CDG) ←− (P1(P1.length), P2(P2.length))
////          CDG.ω2((P1(P1.length − 1),P2(P2.length − 1))) =
////          ”synchronized − dependency”
////      end for
//    }
//
//    private PDTGraph generatePDT(HashMap<GraphNode, ArrayList<ArrayList<GraphNode>>> postDom, ProcessGraph pg) {
//        // Generating Post-dominators Tree:
//        // The second step creates the Post-Dominators Tree PDT.
//        // PDT involves all descendants of a node that are immediately post-dominated
//        // by this latter. As a consequence, the tree root is always the node ”exit”.
//        // In order to generate the PDT, we define Algorithm 2 taking as input the
//        // list of post-dominators of each node. In each iteration, we remove the last
//        // added node from Postdom list (Line 6,7). If the postdominators of the
//        // corresponding node are already added to PDT (Line 8) then it will be in turn
//        // added to the vertex set of PDT and linked to the last erased node (Line 9-13).
//        // The running node will be removed from nonDesigned and added to LastErased.
//
//        PDTGraph PDT = new PDTGraph();
////        SetnonDesingned ←− V (PG) \ {exit}
//        ArrayList<GraphNode> SetnonDesingned = pg.getNodes();
////        LastErased ←− {exit}
//        ArrayList<GraphNode> LastErased = new ArrayList<>();
//        GraphNode exit = SetnonDesingned.remove(SetnonDesingned.size() - 1);
//        LastErased.add(exit);
////      while nonDesingned ̸= ∅ do
//        while (SetnonDesingned.size() > 0) {
////          for all i ∈ LastErased do
//            for (GraphNode i :LastErased) {
////              for all j ∈ nonDesingned( do
//                for (GraphNode j :SetnonDesingned) {
////                  if (i ∈ Postdom(j)) then
//                    if (existInPostdom(j, i) ) {
////                      Postdom(j) ←− Postdom(j) \ {i}
////                      if (sizeof(Postdom(j)) = 1) then
//                        if (sizeOfPostdom(j) == 1) {
////                          nonDesingned ←− nonDesingned \ {j}
////                          V ( P D T ) ←− V ( P D T ) ∪ { ( i ) , ( j ) }
////                          E ( P D T ) ←− E ( P D T ) ∪ { ( i , j ) }
////                          N V ←− N V ∪ { j }
////                      end if
//                        }
////                  end if
//                    }
////              end for
//                }
////          end for
//            }
////          LastErased ←− NV
////      end while
//        }
//
//        return PDT;
//    }
//
//    private boolean existInPostdom(GraphNode j ,GraphNode i) {
//        return false;
//    }
//
//    private int sizeOfPostdom(GraphNode j) {
//        return -1;
//    }
//
//    private HashMap<GraphNode, ArrayList<ArrayList<GraphNode>>> computePostdoms(ProcessGraph pg) {
//        //Computing post-dominators:
//        //The first step towards identifying common control dependencies consists in com-
//        //puting post-dominators of each node v in PG. Based on Definition 4,
//        // the post-dominators of a node v represent the intersection of all paths
//        // containing no cycles from v to the exit node.
//
//        HashMap<GraphNode, ArrayList<ArrayList<GraphNode>>> edgePathes = new HashMap<>();
//        ArrayList<GraphNode> path = new ArrayList<>();
//
//        for (GraphNode node : pg.getNodes()) {
//            ArrayList<ArrayList<GraphNode>> pathes = new ArrayList<>();
//            nextStep(pathes, path, node);
//            edgePathes.put(node, pathes);
//        }
//
//        return edgePathes;
//    }
//
//    private void nextStep(ArrayList<ArrayList<GraphNode>> pathes, ArrayList<GraphNode> path,GraphNode node) {
//        ArrayList<GraphNode> next = node.getNext();
//        path.add(next.get(0));
//        nextStep(pathes, path, next.get(0));
//
//        if (next.size() > 1) {
//
//            for (int i = 1; i < next.size(); i++) {
//               GraphNode pathNode = next.get(i);
//                ArrayList<GraphNode> newPath = copyPath(path);
//                pathes.add(newPath);
//                nextStep(pathes, newPath, next.get(i));
//            }
//        }
//    }
//
//    private ArrayList<GraphNode> copyPath(ArrayList<GraphNode> path) {
//        ArrayList<GraphNode> copy = new ArrayList<>();
//        for (GraphNode node: path) {
//            copy.add(node);
//        }
//
//        return copy;
//    }
//
//
//
//    public ArrayList<ArrayList<GraphNode>> constructSLMarkedCD(Type type, PDTGraph PDT, ProcessGraph pg) {
//        ArrayList<ArrayList<GraphNode>> nodes = new ArrayList<>();
//        ArrayList<ArrayList<GraphNode>> S = new ArrayList<>();
////      for all i ∈GraphNodeSet do
//        for (GraphNode i : pg.getNodes()) {
////            for all j ∈ PG.adjacentTo(i) do
//            for (GraphNode j : pg.adjacentTo(i)) {
////                 if (¬Ancestor(pdt, j, i)) then
//                if (ancestor(PDT, j, i)){
////                      S ←− ( i , j )
////              end if
//                }
////          end for
//            }
////      end for
//        }
////      for (i = 0 → sizeof(S)) do
//        for (int i = 0; i < S.size(); i++) {
////            A←−S[i][0]
//           GraphNode A = S.get(i).get(0);
////          B ←− S[i][1]
//           GraphNode B = S.get(i).get(1);
////          L(i) ←− Least(A, B)
//
////          Bool inclus
////          if (L(i) = A) then
////            if (L(i) = A) {
////              inclus ←− true else
////              inclus ←− false
////          end if
////            }
////          m i=1
////          {CDoni}, L = m
////          {Li}
////          Marked(i) ←− mark(inclus, L(i), B)
////          CD(i) ←− A
//        }
////       end for
//
//        return nodes;
//    }
//
//    private boolean ancestor(PDTGraph pdt,GraphNode j,GraphNode i) {
//        return false;
//    }
//
//    public void generatingDataDependencyGraph() {
////        for all x ∈ P rocessV ariables do
////            Find {vd} ∈ V (DUG) such that vd.θ.l contains Def(x) Find {vu} ∈ V (DUG) such that vu.θ.l contains Use(x)
////        end for
////        for all i ∈ {vu} do
////            Find j ∈ {vd} such that vd.θ.n < vu.θ.n and vd.θ.n = Max(v.θ.n)|v ∈ {vd}
////        V2(DDG) ←− V2(DDG) ∪ {vd, vu}
////        E2(DDG) ←− E2(DDG) ∪ {(vd, vu)}
////        end for
//    }
//
//// ---------------------------------------------------------------------------------------------------------------------
//
//}
