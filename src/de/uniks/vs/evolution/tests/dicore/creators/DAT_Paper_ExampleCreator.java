package de.uniks.vs.evolution.tests.dicore.creators;

import de.uniks.vs.evolution.graphmodels.GraphNode;
import de.uniks.vs.evolution.graphmodels.GraphEdge;
import de.uniks.vs.evolution.graphmodels.GraphModel;
import de.uniks.vs.evolution.graphmodels.ModelManager;

import java.util.ArrayList;

/**
 * Created by alex jahl code generation.
 */
public class DAT_Paper_ExampleCreator {

    public static ModelManager createModel() {
        ModelManager modelManager = new ModelManager();
        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("e", "endevent_endevent_08orzt6", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("e", "startevent_startevent_1", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "check_availability", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "send_order_and_invoice", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "compute_initial_price", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "receive_products", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "choose_shipper", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "choose_spplier", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "compute_shipping_price", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "receive_delivery_notification", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "contact_supplier_2", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "receive_order", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "compute_retouch_price", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "compute_total_price", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("a", "contact_supplier_1", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("g", "or-split", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("g", "or-join", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("g", "or-split", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("g", "or-join", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("g", "and-join", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphNote("g", "and-split", "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(14, 18, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(1, 11, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(11, 2, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(5, 16, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(19, 13, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(20, 6, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(10, 18, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(2, 15, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(6, 8, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(17, 10, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(15, 7, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(17, 14, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(13, 3, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(18, 5, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(9, 0, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(15, 16, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(12, 19, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(20, 4, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(7, 17, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(3, 9, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(16, 20, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(8, 19, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.createGraphEdge(4, 12, "DAT_Paper_Example.bpmn.20.xml");
        modelManager.setLastModelID("DAT_Paper_Example.bpmn.20.xml");
        return modelManager;
    }
}
