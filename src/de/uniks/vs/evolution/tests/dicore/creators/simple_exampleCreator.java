package de.uniks.vs.evolution.tests.dicore.creators;

import de.uniks.vs.evolution.graphmodels.GraphNode;
import de.uniks.vs.evolution.graphmodels.GraphEdge;
import de.uniks.vs.evolution.graphmodels.GraphModel;
import de.uniks.vs.evolution.graphmodels.ModelManager;

import java.util.ArrayList;

/**
 * Created by alex jahl code generation.
 */
public class simple_exampleCreator {

    public static ModelManager createModel() {
        ModelManager modelManager = new ModelManager();
        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), "simple_example.bpmn.20.xml");
        modelManager.createGraphNote("e", "startevent_start", "simple_example.bpmn.20.xml");
        modelManager.createGraphNote("e", "endevent_end", "simple_example.bpmn.20.xml");
        modelManager.createGraphNote("a", "wait_state", "simple_example.bpmn.20.xml");
        modelManager.createGraphNote("a", "", "simple_example.bpmn.20.xml");
        modelManager.createGraphNote("a", "", "simple_example.bpmn.20.xml");
        modelManager.createGraphEdge(0, 3, "simple_example.bpmn.20.xml");
        modelManager.createGraphEdge(3, 2, "simple_example.bpmn.20.xml");
        modelManager.createGraphEdge(2, 4, "simple_example.bpmn.20.xml");
        modelManager.createGraphEdge(4, 1, "simple_example.bpmn.20.xml");
        modelManager.setLastModelID("simple_example.bpmn.20.xml");
        return modelManager;
    }
}
