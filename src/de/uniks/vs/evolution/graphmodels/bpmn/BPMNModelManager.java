package de.uniks.vs.evolution.graphmodels.bpmn;

import de.uniks.vs.evolution.factories.BPMNGraphFactory;
import de.uniks.vs.evolution.graphmodels.GraphModel;
import de.uniks.vs.evolution.graphmodels.ModelManager;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.HashMap;

/**
 * Created by alex on 16/6/8.
 */
public class BPMNModelManager extends ModelManager {

    BPMNGraphFactory graphFactory;


    private static BPMNModelManager instance;

    public static BPMNModelManager getInstance () {
        if (BPMNModelManager.instance == null) {
            BPMNModelManager.instance = new BPMNModelManager();
        }
        return BPMNModelManager.instance;
    }

    private BPMNModelManager () {
        super();
        graphFactory = new BPMNGraphFactory().setModel(new GraphModel());
    }

//    public JFXRectViewController createActivity() {
//
//        Activity activity = graphFactory.createActivity();
//
//        Rectangle rectangle = new Rectangle();
//        rectangle.setWidth(1);
//        rectangle.setHeight(1);
//        JFXRectViewController controller = (JFXRectViewController) new JFXRectViewController().setItem(activity).setShape(rectangle);
//        vController.put(rectangle, controller);
//
//        return controller;
//    }
//
//    public JFXLineViewController createFlow() {
//
//        Flow flow = graphFactory.createFlow();
//        Line line = new Line();
//        JFXLineViewController controller = (JFXLineViewController) new JFXLineViewController().setItem(flow).setShape(line);
//        vController.put(line, controller);
//
//        return controller;
//    }
//
//    public JFXViewController getControllerForNode(Node node) {
//        return vController.get(node);
//    }
}
