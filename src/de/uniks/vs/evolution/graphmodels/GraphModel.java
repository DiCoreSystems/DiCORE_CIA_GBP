package de.uniks.vs.evolution.graphmodels;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by alex on 16/6/10.
 */
public class GraphModel extends GraphItem {

    public enum Type {
		PG("PG"),
		DG("DG"),
		PATTERN("PATTERN"),
		RULE("RULE"),
		UNDEFINED("UNDEFINED");

        private final String stringValue;

        private Type(String value) {
            stringValue = value;
        }

        public String getStringValue() {
            return stringValue;
        }

        public Type getEnumFromString (String s) {
            return Type.valueOf(s.toUpperCase(Locale.ENGLISH));
        }
	}
	
    private long uuid_counter;
    private long uuid_edgecounter;
    private Type type;
    private String name;

    /* --------  graphitems -------- */
//    ArrayList<GraphItem> graphItems = new ArrayList<>();
    CopyOnWriteArrayList<GraphItem> graphItems = new CopyOnWriteArrayList<>();

    public GraphModel() {
        uuid_edgecounter = 0;
        uuid_counter = 0;
        type = Type.UNDEFINED;
    }
    
    public GraphModel withUuidEdgecounter(long counter) {
		uuid_edgecounter = counter;
    	return this;
    }
    

     public GraphModel withUuidCounter( long counter) {
    	 uuid_counter = counter;
    	return this;
    }

     public GraphModel withType(Type type) {
    	 this.type = type;
    	 return this;
     }

    public Type getType() {
		return type;
	}
    
    public void removeFromGraphItems(GraphItem item) {
    	graphItems.remove(item);
    	item.removeYou();
        propertyChange(new PropertyChangeEvent(this, "deleteGraphItem", item, null));

    }
    
    public <T> void addToGraphItems(GraphItem item) {
        if (graphItems.contains(item))
            return;

        item.setModel(this);
        item.addPropertyChangeListener(this);
        graphItems.add(item);
        propertyChange(new PropertyChangeEvent(this, "newGraphItem", null, item));
    }

//    public ArrayList<GraphItem> getGraphItems() {
//        return graphItems;
//    }
    public CopyOnWriteArrayList<GraphItem> getGraphItems() {
        return graphItems;
    }


    @Override
    public void removeThis() { }

    @Override
    public GraphModel clone() {

        GraphModel model = new GraphModel()
        		.withUuidCounter(uuid_counter)
        		.withUuidEdgecounter(uuid_edgecounter)
                .withName(name);

        // all nodes
        for (GraphItem item : graphItems) {
        	
        	if (item instanceof GraphNode) {
                GraphItem clone = item.clone();
                model.addToGraphItems(clone);
        	}
        }

        // all edges
        for (GraphItem item : graphItems) {
        	
        	if (item instanceof GraphEdge) {
        		GraphEdge clone = (GraphEdge)item.clone();
				
				long sourceID = ((GraphEdge) item).getSource().getId();
				GraphNode source = (GraphNode)model.getGraphItemWithID(sourceID);
				
				long targetID = ((GraphEdge) item).getTarget().getId();
				GraphNode target = (GraphNode)model.getGraphItemWithID(targetID);
								
				clone.withSource(source);
				clone.withTarget(target);
				
				model.addToGraphItems(clone);
			}
        }
        
        return model;
    }

    private GraphModel withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getCreationCode(CodeGeneratorTools.Syntax syntax) {
        StringBuilder stringBuilder = new StringBuilder();

       switch (syntax) {
           case JAVA:
                stringBuilder.append("        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), #modelID#);\n");

               for (GraphNode graphNode : getNodes()) {
                   stringBuilder.append(graphNode.getCreationCode(syntax));
               }

               for (GraphEdge graphEdge : getEdges()) {
                   stringBuilder.append(graphEdge.getCreationCode(syntax));
               }
               break;

           case JSON:
               if (this.getGraphModel() == null)
                    stringBuilder.append( "{");

               stringBuilder.append( "\""+ CodeGeneratorTools.getNewID()+"\": { \n"
                     +  "\"id\": \""+getId()+"\", \n"
                     +  "\"name\": \""+getName()+"\", \n"
                     +  "\"status\": \""+getStatus()+"\", \n"
                     +  "\"type\": \""+getType().toString()+"\", \n"
                       +  "\"class\": \""+this.getClass()+"\", \n"
                    +   "\"items\": {\n");
//               + "},\n");

               for (GraphNode graphNode : getNodes()) {
                   stringBuilder.append(graphNode.getCreationCode(syntax));
               }

               for (GraphEdge graphEdge : getEdges()) {
                   stringBuilder.append(graphEdge.getCreationCode(syntax));
               }

               for (GraphModel graphModel : getModels()) {
                   stringBuilder.append(graphModel.getCreationCode(syntax));
                   stringBuilder.append( "},\n");
               }


               stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.lastIndexOf(",") + 1, "");
               stringBuilder.append( "}\n");

               if (this.getGraphModel() == null) {
//                   stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.lastIndexOf(",") + 1, "}");
                   stringBuilder.append( "}\n");

//                   if (getModels().size() == 0) {
                       stringBuilder.append( "}\n");
//                   }
               }
               break;
       }

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        String s = this.getClass().getSimpleName();
        s+="\n";

        for (GraphItem graphItem: graphItems) {

            if (graphItem instanceof GraphNode)
                s += graphItem.getId() + " " + ((GraphNode)graphItem).getType() + " " + ((GraphNode)graphItem).getName() + " " + graphItem.getStatus() +  "\n";
            else if (graphItem instanceof GraphEdge)
                s += graphItem.getId() + " " + ((GraphEdge)graphItem).getSource().getId() + " " + ((GraphEdge)graphItem).getTarget().getId() + " " + graphItem.getStatus() +  "\n";
        }
        return s;
    }

    public long addNode(GraphNode graphNode) {
        graphNode.setId(uuid_counter++);
        graphNode.setModel(this);
        this.addToGraphItems(graphNode);

        getPropertyChangeSupport().firePropertyChange("addNode", null, graphNode);
        return graphNode.getId();
    }

    public GraphItem getGraphItemWithID(String id) {

        if (id.matches(".*\\D.*")) {
            int indexOf = id.lastIndexOf("_");
            String[] split = new String[] {id.substring(0, indexOf), id.substring(indexOf+1)};
            return findItem(split);
        }
        else
            return getGraphItemWithID(Long.valueOf(id));
    }

    public GraphItem getGraphItemwithIDEndsWith(String string) {
        synchronized (graphItems){
            for (GraphItem graphItem : graphItems) {

                if (graphItem instanceof GraphNode && ((GraphNode) graphItem).getName().equals(string))
                    return graphItem;
            }
        }
        System.err.println("GraphModel::getGraphItemWithID Item not find "+ string);
        return null;
//
//
//        if (id.matches(".*\\D.*")) {
//            int indexOf = id.lastIndexOf("_");
//            String[] split = new String[] {id.substring(0, indexOf), id.substring(indexOf+1)};
//            return findItem(split);
//        }
//        else
//            return getGraphItemWithID(Long.valueOf(id));
    }


    private GraphItem findItem(String[] split) {

        if (this.getName().endsWith(split[0])) {
            return getGraphItemWithID(Long.valueOf(split[1]));
        }
        else {
            for (GraphModel model : getModels()) {
                GraphItem item = model.findItem(split);

                if (item != null)
                    return item;
            }
        }
        return null;
    }


    public GraphItem getGraphItemWithID(long itemId) {

        for (GraphItem graphItem : graphItems) {

            if ( graphItem.getId() == itemId)
                return graphItem;
        }
        return null;
    }

    public long addEdge(GraphEdge graphEdge) {
//        graphEdge.setId(uuid_edgecounter++);
        graphEdge.setId(uuid_counter++);
        graphEdge.setModel(this);
        this.addToGraphItems(graphEdge);
        return graphEdge.getId();
    }

    public ArrayList<GraphEdge> getEdges(){
        return getGraphItemsByClass(GraphEdge.class);
    }

    public ArrayList<GraphNode> getNodes(){ return getGraphItemsByClass(GraphNode.class); }

    public ArrayList<GraphModel> getModels(){
    	return getGraphItemsByClass(GraphModel.class);
    }

    public <T> ArrayList<T> getGraphItemsByClass(Class<T> clazz){
        ArrayList<T> items = new ArrayList<>();

        for (GraphItem graphItem:graphItems) {

            if(clazz.isInstance(graphItem))
                items.add((T) graphItem);
        }

        return items;
    }

    public GraphNode getNode(long sourceID) {

        for (GraphNode graphNode: getNodes()) {

            if(graphNode.getId() == sourceID)
                return graphNode;
        }
        return null;
    }

    public ArrayList<GraphNode> getNodesByType(String type) {
        ArrayList<GraphNode> nodes = new ArrayList<>();
        ArrayList<GraphNode> graphNodes = getNodes();

        for (GraphNode node: graphNodes) {

            if (node.getType().equals(type))
                nodes.add(node);
        }
        return nodes;
    }


    @Override
    public GraphModel withId(long id) {
    	    	this.id = id;
    			return this;
    }

	public long createUUID() {
		return uuid_counter++;
	}
	
	
	private String print(GraphNode graphNode, String prefix, boolean isTail) { //, GraphModel model, HashMap<String, String> corespondenceNodes) {

	        String s = prefix + (!prefix.isEmpty() ? (isTail ? "└── " : "├── ") : "") + /*corespondent +*/ "(" + graphNode.getName() + ")"  + "\n";

	        for (GraphEdge graphEdge : graphNode.getOutEdges()) {
	            s += print(graphEdge.getTarget(), prefix + (isTail ? "    " : "│   "), false);
	        }

	        if (graphNode.getOutEdges().size() > 0) {
	            s += print(graphNode.getOutEdges().get(graphNode.getOutEdges().size() - 1).getTarget(), prefix + (isTail ?"    " : "│   "), true); 
	        }
	        System.out.println(s);
	        return s;
	    }

	public void printGraph() {
		ArrayList<GraphNode> startEvents = getStartEvents();
		
		for (GraphNode graphNode : startEvents) {
//			System.out.println(print(graphNode, "", true));
			System.out.println(print(graphNode));
		}
	}

	public void printNamedGraph() {
		ArrayList<GraphNode> startEvents = getStartEvents();

		for (GraphNode graphNode : startEvents) {
//			System.out.println(print(graphNode, "", true));
			System.out.println(printNodeName(graphNode));
		}
	}

	private String print(GraphNode graphNode) {
		String s = ""+graphNode.getId();
		if (graphNode.getType().startsWith("abstract_sub_graph"))
			s += "_SG";

		if(graphNode.getOutEdges().size() > 0) {
			GraphEdge edge = graphNode.getOutEdges().get(graphNode.getOutEdges().size()-1);
			s += " -("+edge.getId()+")-> ";
			s += print(edge.getTarget());
		}
		return s;
	}

	private String printNodeName(GraphNode graphNode) {
		String s = ""+graphNode.getName();
		if (graphNode.getType().startsWith("abstract_sub_graph"))
			s += "_SG";

		if(graphNode.getOutEdges().size() > 0) {
			GraphEdge edge = graphNode.getOutEdges().get(graphNode.getOutEdges().size()-1);
			s += " -("+edge.getId()+")-> ";
			s += printNodeName(edge.getTarget());
		}
		return s;
	}

	private ArrayList<GraphNode> getStartEvents() {
		ArrayList<GraphNode> startEvents = new ArrayList<>();
		
		for (GraphItem graphItem : graphItems) {
			
			if (graphItem instanceof GraphNode && ((GraphNode)graphItem).getName().startsWith("startevent_")) {
				startEvents.add((GraphNode) graphItem);
			}
		}
		
		return startEvents;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
