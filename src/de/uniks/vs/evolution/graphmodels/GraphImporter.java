package de.uniks.vs.evolution.graphmodels;

import java.util.ArrayList;

/**
 * Created by alex on 08/11/2016.
 */
public class GraphImporter {

    ArrayList<Importer> importer = new ArrayList<>();
    private ModelManager modelManager;

    public GraphImporter(ModelManager modelManager) {
        this.modelManager = modelManager;
        importer.add(new BPMNImporter());
        importer.add(new JSONImporter());
    }

    public void handle(String filePath) {

        ModelManager manager = null;

        for (Importer iMporter: importer) {
            manager = iMporter.handle(filePath);

            if (manager != null)
                break;
        }

        if (manager == null) {
            System.out.println("can not import !!!!!!");
            return;
        }

        for (String modelKey: manager.getGraphModels().keySet()) {
            modelManager.addGraphModel(manager.getModel(modelKey), modelKey);

        }
    }
}
