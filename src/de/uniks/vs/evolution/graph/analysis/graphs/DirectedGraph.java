package de.uniks.vs.evolution.graph.analysis.graphs;

import de.uniks.vs.evolution.graphmodels.GraphItem;
import de.uniks.vs.evolution.graphmodels.GraphModel;
import de.uniks.vs.evolution.graphmodels.GraphNode;
import de.uniks.vs.evolution.graphmodels.ModelManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by alex on 16/7/19.
 */
public class DirectedGraph {

    ArrayList<Node> nodes = new ArrayList<>();
    ArrayList<Edge> edges = new ArrayList<>();

    public ArrayList<Edge> getEdges() {
		return edges;
	}
    
    public ArrayList<Node> getNodes() {
		return nodes;
	}
    
    public void addVertex(String entry) {
        Node node = getNode(entry);

        if (node != null)
            return;
//        System.out.println("DirectedGraph::addVertex: "  + entry );
        nodes.add(new Node(entry));
    }

    public void addEdge(String edgeSource, String edgeTarget) {
//        System.out.println("DirectedGraph::addEdge: "  + edgeSource + " " + edgeTarget  );
        Node edgeSourceNode = getNode(edgeSource);
        Node edgeTargetNode =  getNode(edgeTarget);
        edges.add(new Edge(edgeSourceNode, edgeTargetNode));
    }

    private Node getNode(String id) {

        for (Node n: nodes) {

            if (n.id.equals(id))
                return n;
        }

        return null;
    }

    //V2 represents the nodes of P G graph
    public void addEdge(String edgeSource, String edgeTarget, String stringRelationshipEdge) {
//        System.out.println("DirectedGraph::addEdge: "  + edgeSource + " " + edgeTarget + " " + stringRelationshipEdge);
        Node edgeSourceNode = getNode(edgeSource);
        Node edgeTargetNode =  getNode(edgeTarget);
        edges.add(new Edge(edgeSourceNode, edgeTargetNode, new EdgeRelationship(edgeSourceNode, edgeTargetNode, stringRelationshipEdge)));
    }

    // StringDescription
    public Edge getEdge(String edgeSource, String edgeTarget) {
//        System.out.println("DirectedGraph::getEdge: "  + edgeSource + " " + edgeTarget );
        for (Edge e: edges ) {
            if (e.edgeSourceNode.id.endsWith(edgeSource) && e.edgeTargetNode.id.endsWith(edgeTarget))
                return e;
        }
        return null;
    }


    @Override
    public String toString() {
        String string = "";
        for (Node n: nodes) {
            string += n.id + ", ";
        }

        string = string.substring(0, string.length()-2) + "\n    ";

        for (Edge e: edges) {
            if(e.relationshipEdge != null)
                string += e.edgeSourceNode.id + " -" + e.relationshipEdge.stringRelationshipEdge +"- "+ e.edgeTargetNode.id + ", ";
            else
                string += e.edgeSourceNode.id + " -- "+ e.edgeTargetNode.id + ", ";
        }

        return string.substring(0, string.length()-2);
    }

    public String toStringRev() {
        String string = "";
        for (int i = nodes.size()-1; i > -1; i--) {
            Node n = nodes.get(i);
            string += n.id + ", ";
        }

        string = string.substring(0, string.length()-2) + "\n    ";

        for (int i = edges.size()-1; i > -1; i--) {
            Edge e = edges.get(i);
            if(e.relationshipEdge != null)
                string += e.edgeTargetNode.id + " -" + e.relationshipEdge.stringRelationshipEdge +"- "+ e.edgeSourceNode.id + ", ";
            else
                string += e.edgeTargetNode.id + " -- "+ e.edgeSourceNode.id + ", ";
        }

        return string.substring(0, string.length()-2);
    }

    public String toStringAsTree(ModelManager manager, HashMap<String, String> corespondenceNodes) {
        GraphModel model = manager.getModel(manager.getLastModelID());

        String string = "\n";
//        for (Node n: nodes) {
//            String name = "";
//
//            String corespondent = getCorespondentKey(n, corespondenceNodes);
//
//            if (corespondent != null && corespondent.matches("[0-9]+")) {
//                GraphItem graphItem = model.getGraphItemWithID(Long.valueOf(corespondent));
//                name = ((GraphNode)graphItem).getType() + " " + ((GraphNode)graphItem).getName();
//            }
//
//            string += corespondent+ "(" + n.id +  ") ("+ name +"), ";
//        }
//
//        string = string.substring(0, string.length()-2) + "\n";

        Node root = edges.get(0).edgeSourceNode;

        for (Edge e: edges) {
            Node edgeSourceNode = e.edgeSourceNode;
            edgeSourceNode.addchild(e.edgeTargetNode);

            if (e.edgeTargetNode == root) {
                root = edgeSourceNode;
            }
        }

        string += print(root,"", true, model, corespondenceNodes);

        return string;
    }

    private String print(Node node, String prefix, boolean isTail, GraphModel model, HashMap<String, String> corespondenceNodes) {
        String name = "";
        String corespondent = getCorespondentKey(node, corespondenceNodes);

        if (corespondent != null && corespondent.matches("[0-9]+")) {
            GraphItem graphItem = model.getGraphItemWithID(Long.valueOf(corespondent));
            name = ((GraphNode)graphItem).getType() + " " + ((GraphNode)graphItem).getName();
        }
        else
            name = node.id;

        String s = prefix + (!prefix.isEmpty() ? (isTail ? "└── " : "├── ") : "") + corespondent + "(" + node.id + ")    ("+ name +")" + "\n";

        for (int i = 0; i < node.getChildren().size() - 1; i++) {
            s += print(node.getChildren().get(i), prefix + (isTail ? "    " : "│   "), false, model, corespondenceNodes);
        }

        if (node.getChildren().size() > 0) {
            s += print(node.getChildren().get(node.getChildren().size() - 1), prefix + (isTail ?"    " : "│   "), true, model, corespondenceNodes);
        }
        return s;
    }

    private String getCorespondentKey(Node node, HashMap<String, String> corespondenceNodes) {
        Collection<String> values = corespondenceNodes.values();
        String corespondent = "";

        for (String s : corespondenceNodes.keySet()) {

            if (corespondenceNodes.get(s).equals(node.id))
                corespondent = s;
        }
        return corespondent;
    }

//------------------------ internal classes ------------------------------------------------
   
    public class Edge {
        private EdgeRelationship relationshipEdge;
        private Node edgeSourceNode;
        private Node edgeTargetNode;

        public EdgeRelationship getRelationshipEdge() {
            return relationshipEdge;
        }

        public Edge(Node edgeSourceNode, Node edgeTargetNode) {
            this.edgeSourceNode = edgeSourceNode;
            this.edgeTargetNode = edgeTargetNode;
        }
        
        public Edge(Node edgeSourceNode, Node edgeTargetNode, EdgeRelationship relationshipEdge) {
            this.edgeSourceNode = edgeSourceNode;
            this.edgeTargetNode = edgeTargetNode;
            this.relationshipEdge = relationshipEdge;
        }
        
        public Node getSourceNode() {
			return edgeSourceNode;
		}
        
        public Node getTargetNode() {
			return edgeTargetNode;
		}

    }


    public class Node {
    	private String id;
    	private ArrayList<Node> children = new ArrayList<>();
    	
    	public Node(String id) {
    		this.id = id;
    	}
    	
    	public String getId() {
			return id;
		}
    	
    	public void addchild(Node n) {
    		this.children.add(n);
    	}
    	
    	public ArrayList<Node> getChildren() {
    		return children;
    	}
    }

    
    public class EdgeRelationship {
    	private  Node edgeSourceNode;
    	private  Node edgeTargetNode;
    	private  String stringRelationshipEdge;
    	
    	public String getStringRelationshipEdge() {
    		return stringRelationshipEdge;
    	}
    	
    	public String getType() {
    		return getStringRelationshipEdge();
    	}
    	
    	public Node getEdgeSourceNode() {
			return edgeSourceNode;
		}
    	
    	public Node getEdgeTargetNode() {
			return edgeTargetNode;
		}
    	
    	public EdgeRelationship(Node edgeSourceNode, Node edgeTargetNode, String stringRelationshipEdge) {
    		this.edgeSourceNode = edgeSourceNode;
    		this.edgeTargetNode = edgeTargetNode;
    		this.stringRelationshipEdge = stringRelationshipEdge;
    	}
    }
}
