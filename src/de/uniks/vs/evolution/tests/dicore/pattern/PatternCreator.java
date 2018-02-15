package de.uniks.vs.evolution.tests.dicore.pattern;

import de.uniks.vs.evolution.dicore.cia.ChangeImpactPattern;
import de.uniks.vs.evolution.graphmodels.CodeGeneratorTools;
import de.uniks.vs.evolution.graphmodels.GraphItem;
import de.uniks.vs.evolution.graphmodels.GraphModel;
import de.uniks.vs.evolution.graphmodels.ModelManager;

public class PatternCreator {

    public static String PATTERN_PATH = "./src/de/uniks/vs/evolution/tests/dicore/pattern/";

    public ChangeImpactPattern createInsertPattern() {
        ModelManager modelManager = new ModelManager();

		// defined graph rules
        String ruleID = "GTR1";
        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), ruleID);
        modelManager.createGraphNote("a", "B", GraphItem.NEW, ruleID);


        ruleID = "GTR2";
        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), ruleID);
        long a1 = modelManager.createGraphNote("a", "A", GraphItem.UNCHANGED, ruleID);
        long a2 = modelManager.createGraphNote("a", "C", GraphItem.UNCHANGED, ruleID);

        long b = modelManager.createGraphNote("a", "B", GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(a1, b, GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(b, a2, GraphItem.NEW, ruleID);


        ruleID = "GTR3";
        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), ruleID);
        long e1 = modelManager.createGraphNote("g", "and-split", GraphItem.UNDEFINED, ruleID);
        long e2 = modelManager.createGraphNote("g", "and-join", GraphItem.UNDEFINED, ruleID);

        b = modelManager.createGraphNote("a", "B", GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(e1, b, GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(b, e2, GraphItem.NEW, ruleID);

        System.out.println(modelManager.getModel("GTR1"));
        System.out.println(modelManager.getModel("GTR2"));
        System.out.println(modelManager.getModel("GTR3"));

        ruleID = "GTR4";
        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), ruleID);
        e1 = modelManager.createGraphNote("g", "or-split", GraphItem.UNDEFINED, ruleID);
        e2 = modelManager.createGraphNote("g", "or-join", GraphItem.UNDEFINED, ruleID);

        b = modelManager.createGraphNote("a", "B", GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(e1, b, GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(b, e2, GraphItem.NEW, ruleID);

        System.out.println(modelManager.getModel("GTR1"));
        System.out.println(modelManager.getModel("GTR2"));
        System.out.println(modelManager.getModel("GTR3"));

        ruleID = "GTR5";
        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), ruleID);
         a1 = modelManager.createGraphNote("a", "A", GraphItem.UNCHANGED, ruleID);
         a2 = modelManager.createGraphNote("a", "C", GraphItem.UNCHANGED, ruleID);

         b = modelManager.createGraphNote("a", "B", GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(a1, b, GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(b, a2, GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(a1, a2, GraphItem.DELETED, ruleID);

        ruleID = "GTR6";
        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), ruleID);
        e1 = modelManager.createGraphNote("g", "or-split", GraphItem.UNDEFINED, ruleID);
        e2 = modelManager.createGraphNote("g", "or-join", GraphItem.UNDEFINED, ruleID);

        b = modelManager.createGraphNote("a", "B", GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(e1, b, GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(b, e2, GraphItem.NEW, ruleID);
        modelManager.createGraphEdge(e1, e2, GraphItem.DELETED, ruleID); // IMPORTENT: must be deleted not undefined


//        System.out.println(modelManager.getModel("GTR1"));
//        System.out.println(modelManager.getModel("GTR2"));
//        System.out.println(modelManager.getModel("GTR3"));
//        System.out.println(modelManager.getModel("GTR4"));
//        System.out.println(modelManager.getModel("GTR5"));
//        System.out.println(modelManager.getModel("GTR6"));


        // Insert Pattern: GTR1->GTR2->GTR5 or GTR1->GTR3 or GTR1->GTR4->GTR6
        ChangeImpactPattern insertPattern = new ChangeImpactPattern();
        insertPattern.setName("InsertPattern");

        GraphModel gtr1RuleGraph = modelManager.getModel("GTR1");
        GraphModel gtr2RuleGraph = modelManager.getModel("GTR2");
        GraphModel gtr4RuleGraph = modelManager.getModel("GTR4");
        
        gtr1RuleGraph.addToGraphItems(modelManager.getModel("GTR3"));
        gtr2RuleGraph.addToGraphItems(modelManager.getModel("GTR5"));
        gtr4RuleGraph.addToGraphItems(modelManager.getModel("GTR6"));

        gtr1RuleGraph.addToGraphItems(gtr2RuleGraph);
        gtr1RuleGraph.addToGraphItems(gtr4RuleGraph);

        insertPattern.addPattern(gtr1RuleGraph);
        
        // -- set model types -----
        insertPattern.withType(GraphModel.Type.PATTERN);
        GraphModel model = modelManager.getModel("GTR1");
        model.withType(GraphModel.Type.RULE);
        model = modelManager.getModel("GTR2");
        model.withType(GraphModel.Type.RULE);
        model = modelManager.getModel("GTR3");
        model.withType(GraphModel.Type.RULE);
        model = modelManager.getModel("GTR4");
        model.withType(GraphModel.Type.RULE);
        model = modelManager.getModel("GTR5");
        model.withType(GraphModel.Type.RULE);
        model = modelManager.getModel("GTR6");
        model.withType(GraphModel.Type.RULE);

        CodeGeneratorTools generatorTools = new CodeGeneratorTools();
        generatorTools.exportModelToJson(modelManager, insertPattern, PATTERN_PATH);
        
		return insertPattern;
	}
}
