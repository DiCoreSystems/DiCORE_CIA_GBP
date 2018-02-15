package de.uniks.vs.evolution.factories;

import de.uniks.vs.evolution.graphmodels.GraphModel;
import de.uniks.vs.evolution.graphmodels.bpmn.Activity;
import de.uniks.vs.evolution.graphmodels.bpmn.Flow;
import de.uniks.vs.evolution.graphmodels.bpmn.Gateway;

/**
 * Created by alex on 16/6/10.
 */
public class BPMNGraphFactory {

    private GraphModel graphModel;

    public BPMNGraphFactory setModel(GraphModel graphModel) {
        this.graphModel = graphModel;

        return this;
    }

    public Activity createActivity() {
        Activity activity = new Activity();
        graphModel.addToGraphItems(activity);

        return activity;
    }

    public Gateway createGateway() {
        Gateway gateway = new Gateway();
        graphModel.addToGraphItems(gateway);

        return gateway;
    }

    public Flow createFlow() {
        Flow flow = new Flow();
        graphModel.addToGraphItems(flow);

        return flow;
    }
}
