package de.uniks.vs.evolution.graphmodels;

import java.util.ArrayList;

public class ModelGraphManager {
	
	private static ModelGraphManager instance = new ModelGraphManager();

	private ModelGraphManager() { }

	public static ModelGraphManager getInstance() {
		return instance;
	}

	
	ArrayList<ModelManager> models = new ArrayList<>();

	public void add(ModelManager modelManager) {
		models.add(modelManager);
	}

	public ModelManager get(int index) {
		return models.get(index);
	}
	
	public ModelManager getLast() {
		for (ModelManager modelManager : models) {
			System.out.println("model: " + modelManager.getLastModelID() + "   " + models.size());
		}
		return models.size()>0 ? models.get(models.size()-1) : null;
	}
}
