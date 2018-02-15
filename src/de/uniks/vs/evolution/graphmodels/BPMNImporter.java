package de.uniks.vs.evolution.graphmodels;

/**
 * Created by alex on 08/11/2016.
 */
public class BPMNImporter implements Importer {
    @Override
    public ModelManager handle(String filePath) {
        GraphTools graphTools = new GraphTools();

        ModelManager pgModelManager = graphTools.getPGModelManager(filePath);

        if (pgModelManager == null || pgModelManager.getGraphModels().size() == 0)
            return null;

        return pgModelManager;
    }
}
