package de.uniks.vs.evolution.graphmodels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alex on 16/6/10.
 */
public class ModelManager implements PropertyChangeListener {

    protected HashMap<String,GraphModel> graphModels;
    private String lastModelID;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


    public ModelManager() {
        graphModels = new HashMap<>();
    }

    public void addPropertyChangListenerToModels(PropertyChangeListener listener) {
        for (GraphModel graphModel: graphModels.values() ) {
            graphModel.addPropertyChangeListener(listener);
        }
    }

    public void addPropertyChangListenerToModels(PropertyChangeListener listener, String modelID) {
        graphModels.get(modelID).addPropertyChangeListener(listener);
    }

    public GraphModel createGraphModel(String modelID) {
    	GraphModel model = new GraphModel();
    	addGraphModel(model, modelID);
    	return model;
    }

    public ModelManager withGraphModel(GraphModel graphModel, String modelID) {
        addGraphModel( graphModel,  modelID);
        return this;
    }

    public void addGraphModel(GraphModel graphModel, String modelID) {
    	graphModel.setName(modelID);
        graphModels.put(modelID,graphModel);
        setLastModelID(modelID);
//        saveModelAsJSON(graphModel);
        graphModel.addPropertyChangeListener(this);
        propertyChangeSupport.firePropertyChange("addModel", null, graphModel);
    }

    public long createGraphNote(String type, String name, String modelID) {
        return createGraphNote(type, name, GraphItem.UNDEFINED, modelID);
    }

    public long createGraphNote(String type, String name, String modelID, VisualProperties properties) {
        long id = createGraphNote(type, name, modelID);
        GraphItem graphItem = graphModels.get(modelID).getGraphItemWithID(id);
        graphItem.setProperties(properties);
        return id;
    }
    
    public long createGraphNote(String type, String name, String status, String modelID) {
        GraphModel model = getOrCreateModel(modelID);
        type = type.replaceAll(" ", "_").toLowerCase();
        name = name.replaceAll(" ", "_").toLowerCase();
        GraphItem item = model.getGraphItemWithID(type + "_" + name);

        if (item != null)
            return item.getId();
        GraphNode graphNode = new GraphNode()
                .withType(type)
                .withName(name)
                .withStatus(status);
        return model.addNode(graphNode);
    }

    public GraphModel getOrCreateModel(String modelID) {
		GraphModel model = getModel(modelID);
		
		if (model == null)
			model = createGraphModel(modelID);
		return model;
	}

	public GraphModel getModel(String modelID) {
        return graphModels.get(modelID);
    }
    
    

    public String getModelID(GraphModel model) {
        for( String key: graphModels.keySet()){

            if (graphModels.get(key) == model) {
                return key;
            }
        }
        return "nothing found";
    }

    public  ArrayList<GraphItem> getStartNodes(String modelID) {
        ArrayList<GraphItem> startNodes = new ArrayList<>();
        GraphModel model = getModel(modelID);
//        ArrayList<GraphItem> graphItems = model.getGraphItems();
        CopyOnWriteArrayList<GraphItem> graphItems = model.getGraphItems();

        for (GraphItem graphItem : graphItems) {

            if ( graphItem instanceof GraphNode) {
                GraphNode graphNode = (GraphNode) graphItem;

                if ("e".equals(graphNode.getType()) && graphNode.getName().startsWith("start")){
                    startNodes.add(graphItem);
                }
            }
        }

        return startNodes;
    }

    public long createGraphEdge(GraphNode sourceItem, GraphNode targetItem,  String modelID) {
        return createGraphEdge(sourceItem, targetItem, new VisualProperties(), GraphItem.UNDEFINED,modelID);
    }
    public long createGraphEdge(GraphNode sourceItem, GraphNode targetItem,  VisualProperties properties, String modelID) {
        return createGraphEdge(sourceItem, targetItem, properties, GraphItem.UNDEFINED, modelID);
    }

    public long createGraphEdge(long sourceID, long targetID, String modelID) {
        GraphModel model = getModel(modelID);
        return createGraphEdge(model.getNode(sourceID), model.getNode(targetID),new VisualProperties(), GraphItem.UNDEFINED, modelID);
    }

    public long createGraphEdge(long sourceID, long targetID, String status, String modelID) {
        GraphModel model = getModel(modelID);
        return createGraphEdge(model.getNode(sourceID), model.getNode(targetID), new VisualProperties(), status, modelID);
    }

    public long createGraphEdge(GraphNode sourceItem, GraphNode targetItem, VisualProperties properties, String status, String modelID) {
        GraphEdge graphEdge = new GraphEdge()
                .withSource(sourceItem)
                .withTarget(targetItem)
                .withStatus(status);
        graphEdge.setProperties(properties);

        return getModel(modelID).addEdge(graphEdge);
    }

    public void setLastModelID(String lastModelID) {
        this.lastModelID = lastModelID;
    }

    public String getLastModelID() {
        return lastModelID;
    }

    public GraphModel getLastModel() {
        return getModel(getLastModelID());
    }

    public int[][] getConnectionMatrix(String modelID) {
        GraphModel model = getModel(modelID);

//        ArrayList<GraphItem> graphItems = model.getGraphItems();
        CopyOnWriteArrayList<GraphItem> graphItems = model.getGraphItems();

        ArrayList<GraphEdge> edges = new ArrayList<>();
        ArrayList<GraphNode> nodes = new ArrayList<>();

        for (GraphItem graphItem: graphItems) {

            if(graphItem instanceof GraphEdge) {
                edges.add((GraphEdge)graphItem);
            }
            else
                nodes.add((GraphNode) graphItem);
        }

        int[][] matrix = new int[nodes.size()][nodes.size()];

        int x = -1;
        int y = -1;

        for (GraphEdge graphEdge:edges) {
            GraphNode source = graphEdge.getSource();
            GraphNode target = graphEdge.getTarget();

            for (int i = 0; i < nodes.size(); i++) {
                GraphNode graphNode = nodes.get(i);

                if (graphNode == source) {
                    y = i;
                }

                else if (graphNode == target) {
                    x = i;
                }
            }

            if(x > -1 && y > -1)
                matrix[x][y] = 1;
        }

        for (int a = 0; a < nodes.size(); a++) {

            for (int b = 0; b < nodes.size(); b++) {
                System.out.print(matrix[b][a]+" ");
            }
//            System.out.println("\n");
        }

        return matrix;
    }

    public String getCreationCode(String modelID) {
        StringBuilder stringBuilder = new StringBuilder();
        GraphModel model = getModel(modelID);

        stringBuilder.append("        ModelManager modelManager = new ModelManager();\n");
        stringBuilder.append(model.getCreationCode(CodeGeneratorTools.Syntax.JAVA));
        stringBuilder.append("        modelManager.setLastModelID(#modelID#);\n");


//        replaceAll(stringBuilder, "#modelID#", "\""+modelID+"\"");

//        int index = stringBuilder.indexOf("#modelID#");
//        stringBuilder.replace(index, index + "#modelID#".length(), modelID);

        return stringBuilder.toString().replaceAll("#modelID#", "\""+modelID+"\"");
    }

    // TODO: extract to Tools class
    private  void replaceAll(StringBuilder stringBuilder, String patternString, String replacement) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher m = pattern.matcher(stringBuilder);

        while(m.find()) {
            stringBuilder.replace(m.start(), m.end(), replacement);
        }
    }

    public void saveModelAsJSON(GraphModel model) {
        CodeGeneratorTools generatorTools = new CodeGeneratorTools();
//        generatorTools.exportModelToJson(this, model, "jsonmodels/");
        generatorTools.generateCreaterClass(this, model, "jsonmodels/");
        String jsonModel = exportModelAsJSON(model);
    }

    public String exportModelAsJSON(String modelID) {
        GraphModel model = getModel(modelID);
        return exportModelAsJSON(model);
    }

    public String exportModelAsJSON(GraphModel model) {
        return model.getCreationCode(CodeGeneratorTools.Syntax.JSON);
    }

    public HashMap<String, GraphModel> getGraphModels() {
        return graphModels;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        propertyChangeSupport.firePropertyChange(evt);
    }

    public void addPropertyChangListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void flashGraphNote(String agentID, String modelID) {
        GraphModel model = getModel(modelID);
        GraphItem graphItem = model.getGraphItemwithIDEndsWith(agentID);

        propertyChangeSupport.firePropertyChange("flashNode", null, graphItem);
    }
}
