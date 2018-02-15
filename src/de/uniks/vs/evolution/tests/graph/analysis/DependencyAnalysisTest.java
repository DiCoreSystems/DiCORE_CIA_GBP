package de.uniks.vs.evolution.tests.graph.analysis;

import de.uniks.vs.evolution.graph.analysis.DependencyAnalysis;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedTreeMapGraph;
import de.uniks.vs.evolution.graph.analysis.manager.BPMNManager;
import de.uniks.vs.evolution.graphmodels.ModelManager;
import org.junit.jupiter.api.Test;
import org.yaoqiang.graph.view.Graph;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by alex on 16/6/10.
 */
public class DependencyAnalysisTest {

    @Test
    public void testDependencyGraph1() {

        String fileName2 = "simple_example.bpmn.20.xml";

        DirectedGraph dg = getDependencyGraph(fileName2);
        System.out.println("DG (Dependency Graph): " + dg);
    }

    @Test
    public void testDependencyGraph2() {

        String fileName2 = "DAT_Paper_Example.bpmn.20.xml";

        DirectedGraph dg = getDependencyGraph(fileName2);
        System.out.println("DG (Dependency Graph): " + dg);
    }

    @Test
    public void testDependencyGraph3() {

        String fileName2 = "ExecutablePurchaseorderprocess.bpmn.20.xml";

        DirectedGraph dg = getDependencyGraph(fileName2);
        System.out.println("DG (Dependency Graph): " + dg);
    }

    @Test
    public void testDependencyGraph4() {

        String fileName1 = "DiCORE_Paper_Example.bpmn.20.xml";

        DirectedGraph dg = getDependencyGraph(fileName1);
        System.out.println("DG (Dependency Graph): " + dg);
    }

    @Test
    public void testDependencyGraph5() {

        String fileName1 = "DAT_Paper_Example_modified_2start.bpmn.20.xml";

        DirectedGraph dg = getDependencyGraph(fileName1);
        System.out.println("DG (Dependency Graph): " + dg);
    }

    @Test
    public void testAncestor() {
        DependencyAnalysis analysis = new DependencyAnalysis();
        analysis.computePostDomTree(getModelManager("DAT_Paper_Example.bpmn.20.xml"));

        DirectedTreeMapGraph pdt = analysis.getPDT();
        System.out.println("Is 7 ancestor of 4 in pdt? " + analysis.ancestor(pdt,"7","4"));
//        Assert.assertFalse(analysis.Ancestor2(pdt,"7","4"));
        System.out.println("Is 4 ancestor of 7 in pdt? " + analysis.ancestor(pdt,"4","7"));
//        Assert.assertFalse(analysis.Ancestor2(pdt,"4","7"));

//        System.out.println("Is 6 ancestor of 2 in pdt? " + analysis.Ancestor2(pdt,"6","2"));
////        Assert.assertFalse(analysis.Ancestor2(pdt,"6","2"));
//        System.out.println("Is 2 ancestor of 6 in pdt? " + analysis.Ancestor2(pdt,"2","6"));
////        Assert.assertFalse(analysis.Ancestor2(pdt,"2","6"));
//
//        System.out.println("Is 16 ancestor of 9 in pdt? " + analysis.Ancestor2(pdt,"16","9"));
////        Assert.assertTrue(analysis.Ancestor2(pdt,"16","9"));
//        System.out.println("Is 9 ancestor of 16 in pdt? " + analysis.Ancestor2(pdt,"9","16"));
////        Assert.assertFalse(analysis.Ancestor2(pdt,"9","16"));


//        DirectedTreeMapGraph pdt = analysis.getPDT();
//        System.out.println("Is 7 ancestor of 4 in pdt? " + analysis.Ancestor2(pdt,"7","4"));
////        Assert.assertFalse(analysis.Ancestor2(pdt,"7","4"));
//        System.out.println("Is 4 ancestor of 7 in pdt? " + analysis.Ancestor2(pdt,"4","7"));
////        Assert.assertFalse(analysis.Ancestor2(pdt,"4","7"));
//
//        System.out.println("Is 6 ancestor of 2 in pdt? " + analysis.Ancestor2(pdt,"6","2"));
////        Assert.assertFalse(analysis.Ancestor2(pdt,"6","2"));
//        System.out.println("Is 2 ancestor of 6 in pdt? " + analysis.Ancestor2(pdt,"2","6"));
////        Assert.assertFalse(analysis.Ancestor2(pdt,"2","6"));
//
//        System.out.println("Is 16 ancestor of 9 in pdt? " + analysis.Ancestor2(pdt,"16","9"));
////        Assert.assertTrue(analysis.Ancestor2(pdt,"16","9"));
//        System.out.println("Is 9 ancestor of 16 in pdt? " + analysis.Ancestor2(pdt,"9","16"));
////        Assert.assertFalse(analysis.Ancestor2(pdt,"9","16"));
    }

    @Test
    public void testInitGraph() {
	/*	g.addEdge("entry", "1");
		g.addEdge("1", "2");
		g.addEdge("1", "3");
		g.addEdge("2", "4");
		g.addEdge("2", "5");
		g.addEdge("3", "5");
		g.addEdge("3", "7");
		g.addEdge("4", "6");
		g.addEdge("5", "6");
		g.addEdge("6", "7");
		g.addEdge("7", "exit");
		*/
        DirectedTreeMapGraph g = new DirectedTreeMapGraph();
        g.addEdge("entry", "1");
        g.addEdge("1", "2");
        g.addEdge("2", "3");
        g.addEdge("3", "10");
        g.addEdge("3", "4");
        g.addEdge("4", "5");
        g.addEdge("5", "6");
        g.addEdge("5", "7");
        g.addEdge("6", "8");
        g.addEdge("7", "8");
        g.addEdge("8", "9");
        g.addEdge("9", "10");
        g.addEdge("10", "11");
        g.addEdge("11", "12");
        g.addEdge("11", "14");
        g.addEdge("12", "13");
        g.addEdge("13", "16");
        g.addEdge("14", "15");
        g.addEdge("15", "16");
        g.addEdge("16", "17");
        g.addEdge("17", "18");
        g.addEdge("18", "19");
        g.addEdge("19", "18");
        g.addEdge("19", "exit");

        System.out.println(g);

    }

    public void testDependencyGraph6() {

        String fileName1 = "DAT_Paper_Example_modified_2end.bpmn.20.xml";

        DirectedGraph dg = getDependencyGraph(fileName1);
        System.out.println("DG (Dependency Graph): " + dg);
    }


    private DirectedGraph getDependencyGraph(String fileName) {
        ModelManager modelManager = getModelManager(fileName);
        DependencyAnalysis analyseDependency = new DependencyAnalysis();
        return analyseDependency.computeDependencyGraph(modelManager);
    }

    private ModelManager getModelManager(String fileName) {
        Path path = Paths.get("./src/de/uniks/vs/evolution/tests/graph/analysis/" + fileName);

        BPMNManager bpmnManager = new BPMNManager();
        Graph graph = bpmnManager.loadBPMNGraph(path);
        return bpmnManager.convertBPMNToProcessGraph(graph, fileName);
    }


}