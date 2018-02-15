package de.uniks.vs.evolution.tests.dicore;

import de.uniks.vs.evolution.dicore.cia.ChangeImpactPattern;
import de.uniks.vs.evolution.dicore.cia.MatchingAlgorithms;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedGraph;
import de.uniks.vs.evolution.graphmodels.*;
import de.uniks.vs.evolution.tests.dicore.creators.DAT_Paper_Example2Creator;
import de.uniks.vs.evolution.tests.dicore.creators.DAT_Paper_ExampleCreator;
import de.uniks.vs.evolution.tests.dicore.pattern.PatternCreator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alex on 16/8/8.
 */


public class CIAPaper1CaseTest {

    public static final String PATH = "./src/de/uniks/vs/evolution/tests/graph/analysis/";
    public static final String CREATOR_PATH = "./src/de/uniks/vs/evolution/tests/dicore/creators/";


    String oldBPMN      = "DAT_Paper_Example.bpmn.20.xml";
    String newBPMN      = "DAT_Paper_Example2.bpmn.20.xml";
    String simpleBPMN   = "simple_example.bpmn.20.xml";

    CodeGeneratorTools generatorTools = new CodeGeneratorTools();
    GraphTools graphTools = new GraphTools();

    @Test
    public void createProcessGraphCreators() throws InterruptedException {
        // generate PG1
    	
        ModelManager pgModelManager = graphTools.getPGModelManager(PATH + oldBPMN);
        GraphModel model = pgModelManager.getModel(pgModelManager.getLastModelID());
        generatorTools.generateCreaterClass(pgModelManager, model, CREATOR_PATH);
        generatorTools.exportModelToJson(pgModelManager, model, CREATOR_PATH);

        ModelGraphManager.getInstance().add(pgModelManager);

        pgModelManager = graphTools.getPGModelManager(PATH + newBPMN);
        model = pgModelManager.getModel(pgModelManager.getLastModelID());
        generatorTools.generateCreaterClass(pgModelManager, model, CREATOR_PATH);
        
        ModelGraphManager.getInstance().add(pgModelManager);

        pgModelManager = graphTools.getPGModelManager(PATH + simpleBPMN);
        model = pgModelManager.getModel(pgModelManager.getLastModelID());
        generatorTools.generateCreaterClass(pgModelManager, model, CREATOR_PATH);
        
        ModelGraphManager.getInstance().add(pgModelManager);
    }


    @Test
    public void step1() throws InterruptedException {
        // generate PG1
//        ModelManager pgModelManager = getPGModelManager(oldBPMN);
//        GraphModel model = pgModelManager.getModel(pgModelManager.getLastModelID());

//        System.out.println(model);
        ModelManager modelManager = DAT_Paper_ExampleCreator.createModel();
//        GraphModel model = pgModelManager.getModel(pgModelManager.getLastModelID());
        GraphModel model = modelManager.getModel(modelManager.getLastModelID());

//        System.out.println(model);

        ModelGraphManager.getInstance().add(modelManager);


        // generate DG1
        DirectedGraph dg = graphTools.getDependencyGraph(modelManager);
        System.out.println("DG (Dependency Graph): " + dg);
        graphTools.convertDPtoGraphModel(dg, modelManager, "DG1");

        ModelGraphManager.getInstance().add(modelManager);

    }


	@Test
    public void step2() throws InterruptedException  {
        // generate PG2
        ModelManager pgModelManager = graphTools.getPGModelManager(PATH + newBPMN);
        GraphModel model = pgModelManager.getModel(pgModelManager.getLastModelID());

//        System.out.println(pgModelManager.getModel(pgModelManager.getLastModelID()));
        
        ModelGraphManager.getInstance().add(pgModelManager);
    }

    @Test
    public void step3() throws InterruptedException  {
        // analyse PG1 PG2
        ModelManager modelManager = graphTools.getPGModelManager(PATH + oldBPMN);
        modelManager.getConnectionMatrix(modelManager.getLastModelID());

        ModelManager pgModelManager = graphTools.getPGModelManager(PATH + newBPMN);
		GraphModel newModel = pgModelManager.getLastModel();
        GraphModel oldModel = modelManager.getLastModel();

        MatchingAlgorithms matchingAlgorithms = new MatchingAlgorithms();
        HashMap<GraphItem, String> results = matchingAlgorithms.compareProcessGraphs(newModel, oldModel);

        graphTools.printResults(results);
        
        ModelGraphManager.getInstance().add(modelManager);
        ModelGraphManager.getInstance().add(pgModelManager);
    }

    @Test
    public void step4() throws InterruptedException  {
        // identify changes
    	PatternCreator patternCreator = new PatternCreator();
        ChangeImpactPattern insertPattern = patternCreator.createInsertPattern();
        
//        ModelManager patternModelManager = new ModelManager();
//        patternModelManager.addGraphModel(insertPattern, "InsertPattern");
//        ModelGraphManager.getInstance().add(patternModelManager);
//        GUITest guiTest = new GUITest();
//        guiTest.startGUI();

        // find changed items
        ModelManager modelManager1 = DAT_Paper_ExampleCreator.createModel();
        ModelManager modelManager2 = DAT_Paper_Example2Creator.createModel();

        GraphModel newPGModel = modelManager2.getLastModel();
        GraphModel oldPGModel = modelManager1.getLastModel();

        System.out.println("----------- Results -----------");
        MatchingAlgorithms matchingAlgorithms = new MatchingAlgorithms();
        HashMap<GraphItem, String> results = matchingAlgorithms.compareProcessGraphs(newPGModel, oldPGModel);

        // identify change types
        // get all new items
        ArrayList<GraphItem> newItems = graphTools.findItemsWithStatus(GraphItem.NEW, results);
        
        newItems.addAll(graphTools.findItemsWithStatus(GraphItem.CHANGED, results));
        
        ArrayList<GraphItem> deleteItems = graphTools.findItemsWithStatus(GraphItem.DELETED, results);

        // find graph structure in results
        HashMap<ArrayList<GraphItem>, HashMap<GraphEdge, String>> subGraphs = graphTools.findSubGraphs(newItems);

        // insert subgraph into graph -----------------------
        GraphModel clone = newPGModel.clone();
       
        ModelManager modelManager = new ModelManager();
        modelManager.addGraphModel(oldPGModel, "oldModel");
        ModelGraphManager.getInstance().add(modelManager);
//        guiTest.startGUI2(800, 500);
        
        modelManager.addGraphModel(clone, "Clone");
        ModelGraphManager.getInstance().add(modelManager);
//        guiTest.startGUI2(10, 750);
        
        // foreach subgraph
        HashMap<GraphItem, ArrayList<GraphItem>> subGraphNodes = new HashMap<>();

        for (ArrayList<GraphItem> key : subGraphs.keySet()) {
        	HashMap<GraphEdge, String> graphBorder = subGraphs.get(key);
//		}
//        for (HashMap<GraphEdge, String> graphBorder : subGraphs.values()) {
            GraphNode subGraphNode = graphTools.creatAndInsertSubGraphNode(clone, graphBorder);
			subGraphNodes.put(subGraphNode, key);
			graphTools.removeOldSubGraphItems(oldPGModel, clone, key, subGraphNode);
        }
        
//        newPGModel.printGraph();
//        clone.printGraph();

     // -------------------------------------- change pattern matching -------------------------------------------
        
        HashMap<String, String> patternMatchingResult = matchingAlgorithms.matchPattern(insertPattern, clone);
        matchingAlgorithms.printPatternMatchingResult(patternMatchingResult);

        modelManager.addGraphModel(clone, "CloneReduced");
        ModelGraphManager.getInstance().add(modelManager);
//        guiTest.startGUI(1000000);
    }

    @Test
    public void step5() throws InterruptedException  {
        // generate DG2
        ModelManager modelManager = DAT_Paper_Example2Creator.createModel();
    	
        DirectedGraph dg = graphTools.getDependencyGraph(modelManager);
        System.out.println("DG (Dependency Graph): " + dg);
        graphTools.convertDPtoGraphModel(dg, modelManager, "DG2");

        ModelGraphManager.getInstance().add(modelManager);
    }

    @Test
    public void step6() {
        // use DG1 and DG2 affected activities
//        Assert.fail();
    }
}
