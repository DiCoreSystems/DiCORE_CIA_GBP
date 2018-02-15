package de.uniks.vs.evolution.graphmodels;

import java.beans.PropertyChangeEvent;

/**
 * Created by alex on 16/6/8.
 */
public class GraphEdge extends GraphItem {

    /* -------- target node -------- */
    GraphNode target;

    public GraphEdge withTarget(GraphNode target) {
        if (this.target == target)
            return this;

        propertyChange(new PropertyChangeEvent(this, "newTarget", this.target, target));

//        if (this.target != null) 
//        	this.target.removeFromInEdges(this);
        
        this.target = target;

        if (this.target != null)
            this.target.addToInEdges(this);

        return this;
    }

    public GraphNode getTarget() {
        return target;
    }
    

    @Override
    public GraphEdge withStatus(String status) {
        super.withStatus(status);
        return this;
    }

    /* -------- source node -------- */
    GraphNode source;

    public GraphEdge withSource(GraphNode source) {
        if (this.source == source)
            return this;

        listeners.firePropertyChange("<-", this.source, source);
        propertyChange(new PropertyChangeEvent(this, "newSource", this.source, source));


//        if (this.source != null)
//        	this.source.removeFromOutEdges(this);
        
        this.source = source;

        if (this.source != null)
        	this.source.addToOutEdges(this);

        return this;
    }

    public GraphNode getSource() {
        return source;
    }

    @Override
    public void removeThis() {
        if (this.target != null)
            this.target.removeFromInEdges(this);
        this.target = null;

        if ( this.source != null)
            this.source.removeFromOutEdges(this);
        this.source = null;
    }

    @Override
    public String getCreationCode(CodeGeneratorTools.Syntax syntax) {
        StringBuilder stringBuilder = new StringBuilder();

        long sourceID = getSource().getId();
        long targetID = getTarget().getId();

        switch (syntax) {
            case JAVA:
                stringBuilder.append("        modelManager.createGraphEdge(" + sourceID + ", " + targetID + ", #modelID#);\n");
                break;
            case JSON:
                stringBuilder.append( "\""+CodeGeneratorTools.getNewID()+"\": { \n"
                        +  "\"id\": \""+getId()+"\", \n"
                        +  "\"source\": \""+sourceID+"\", \n"
                        +  "\"target\": \""+targetID+"\", \n"
                        +  "\"status\": \""+getStatus()+"\", \n"
                        +  "\"class\": \""+this.getClass()+"\" \n"
                        + "},\n");
                break;
        }

        return stringBuilder.toString();
    }

	@Override
	public GraphEdge clone() {
		GraphEdge edge = new GraphEdge()
				.withStatus(status)
				.withId(id);
		
		return edge;
	}

    @Override
	public GraphEdge withId(long id) {
        setId( id);
//    	this.id = id;
		return this;
	}
}
