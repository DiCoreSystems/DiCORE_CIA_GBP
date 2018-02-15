package de.uniks.vs.evolution.tests.graphmodels.bpmn;

import de.uniks.vs.evolution.factories.BPMNGraphFactory;
import de.uniks.vs.evolution.graphmodels.GraphEdge;
import de.uniks.vs.evolution.graphmodels.GraphModel;
import de.uniks.vs.evolution.graphmodels.bpmn.Activity;
import de.uniks.vs.evolution.graphmodels.bpmn.Gateway;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 16/6/10.
 */
public class BPMNGraphModelTest {

    @Test
    public void testCreateModel() throws Exception {

        BPMNGraphFactory factory = new BPMNGraphFactory();
        GraphModel graphModel = new GraphModel();
        factory.setModel(graphModel);

        Activity activity0 = factory.createActivity();
        Activity activity1 = factory.createActivity();
        Activity activity2 = factory.createActivity();
        Activity activity3 = factory.createActivity();
        Gateway gateway0 = factory.createGateway();
        Gateway gateway1= factory.createGateway();

        GraphEdge edge0 = factory.createFlow().withSource(activity0).withTarget(gateway0);
        GraphEdge edge1 = factory.createFlow().withSource(gateway0).withTarget(activity1);
        GraphEdge edge2 = factory.createFlow().withSource(gateway0).withTarget(activity2);
        factory.createFlow() .withSource(activity1) .withTarget(gateway1);
        factory.createFlow() .withSource(activity2) .withTarget(gateway1);
        factory.createFlow() .withSource(gateway1) .withTarget(activity3);

        assertEquals( activity0.getInEdges().size(), 0 );
        assertEquals( activity0.getOutEdges().size(), 1 );
        assertEquals( activity0.getOutEdges().get(0), edge0 );
        assertEquals( edge0.getTarget(), gateway0 );
        assertEquals( gateway0.getOutEdges().size(), 2 );
        assertEquals( gateway0.getOutEdges().get(0), edge1 );
        assertEquals( gateway0.getOutEdges().get(1), edge2 );
    }
}