package de.uniks.vs.evolution.graphmodels;


import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by alex on 16/6/8.
 */
public class GraphNode extends GraphItem {

    private String type;
    private String name;
    private int order;

    public  GraphNode withType(String type){
        this.type = type;
        return this;
    }

    public  GraphNode withName(String name){
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    /* --------  incomming edges -------- */
//    ArrayList<GraphEdge> inEdges = new ArrayList<>();
    CopyOnWriteArrayList<GraphEdge> inEdges = new CopyOnWriteArrayList<>();

    public <T> void addToInEdges(GraphEdge edge) {
        if (inEdges.contains(edge))
            return;

        inEdges.add(edge);

        propertyChange(new PropertyChangeEvent(this, "addInEdge", null, edge));
        edge.withTarget(this);
    }

    public void removeFromInEdges(GraphEdge edge) {
    	if (!inEdges.contains(edge))
            return;
    	
    	edge.withTarget(null);
    	
        propertyChange(new PropertyChangeEvent(this, "removeInEdge", edge, null));
        inEdges.remove(edge);
    }

    public CopyOnWriteArrayList<GraphEdge> getInEdges() {
        return inEdges;
    }


    /* --------  outgoing edges -------- */
//    ArrayList<GraphEdge> outEdges = new ArrayList<>();
    CopyOnWriteArrayList<GraphEdge> outEdges = new CopyOnWriteArrayList<>();

    public <T> void addToOutEdges(GraphEdge edge) {
        if (outEdges.contains(edge))
            return;

        outEdges.add(edge);

        propertyChange(new PropertyChangeEvent(this, "addOutEdge", null, edge));
        edge.withSource(this);
    }

    public void removeFromOutEdges(GraphEdge edge) {
    	if (!outEdges.contains(edge))
            return;
    	
    	edge.withSource(null);
        propertyChange(new PropertyChangeEvent(this, "removeOutEdge", edge, null));

        outEdges.remove(edge);
                
    }

    public CopyOnWriteArrayList<GraphEdge> getOutEdges() {
        return outEdges;
    }

    @Override
    public void removeThis() {
    	status = GraphItem.DELETED;
//        for (GraphEdge edge : inEdges ) {
//            edge.removeYou();
//        }
//
//        for (GraphEdge edge : outEdges ) {
//            edge.removeYou();
//        }
    }

    @Override
    public String getCreationCode(CodeGeneratorTools.Syntax syntax) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (syntax) {
            case JAVA:
                stringBuilder.append("        modelManager.createGraphNote(\"" + type + "\", \"" + name + "\", #modelID#);\n");
                break;
            case JSON:
                stringBuilder.append( "\""+CodeGeneratorTools.getNewID()+"\": { \n"
                        +  "\"id\": \""+getId()+"\", \n"
                        +  "\"name\": \""+getName()+"\", \n"
                        +  "\"status\": \""+getStatus()+"\", \n"
                        +  "\"type\": \""+getType().toString()+"\", \n"
                        +  "\"class\": \""+this.getClass()+"\" \n"
                        + "},\n");
                break;
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        String s = getNodeName();
        s+="\n";
        for (GraphEdge edge: getOutEdges()) {

            if (edge.getTarget() != null)
                s += edge.getTarget().toString();
        }
        return s;
    }

    public String getNodeName() {
		String s = "<(" + type + ", " + name + "), " + id + ">";
		return s;
	}

    public ArrayList<GraphNode> getNext() {
        ArrayList<GraphNode> nodes = new ArrayList<>();
        for (GraphEdge graphEdge: getOutEdges()) {
            nodes.add( graphEdge.getTarget());
        }
        return nodes;
    }

    @Override
    public GraphNode withStatus(String status) {
        super.withStatus(status);
        return this;
    }

	@Override
	public GraphNode clone() {
		GraphNode node = new GraphNode()
				.withName(name)
				.withStatus(status)
				.withType(type)
				.withId(id);
		return node;
	}
	
	@Override
	public GraphNode withId(long id) {
        setId(id);
//    	this.id = id;
		return this;
	}

    public void setDrawOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
