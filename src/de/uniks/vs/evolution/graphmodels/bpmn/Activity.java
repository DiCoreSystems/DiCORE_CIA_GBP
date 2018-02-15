package de.uniks.vs.evolution.graphmodels.bpmn;

import de.uniks.vs.evolution.graphmodels.GraphEdge;
import de.uniks.vs.evolution.graphmodels.GraphNode;

/**
 * Created by alex on 16/6/8.
 */
public class Activity extends GraphNode {

    @Override
    public void addToInEdges(GraphEdge edge) {
        if(getInEdges().contains(edge))
            return;

        getInEdges().clear();
        super.addToInEdges(edge);

        edge.withTarget(this);
    }

    @Override
    public void addToOutEdges(GraphEdge edge) {
        if(getOutEdges().contains(edge))
            return;

        getOutEdges().clear();
        super.addToOutEdges(edge);

        edge.withSource(this);
    }
}
