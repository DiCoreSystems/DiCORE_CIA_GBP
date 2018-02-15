package de.uniks.vs.evolution.graphmodels;

import de.uniks.vs.evolution.graph.analysis.DependencyAnalysis;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedGraph.Edge;
import de.uniks.vs.evolution.graph.analysis.graphs.DirectedGraph.Node;
import de.uniks.vs.evolution.graph.analysis.manager.BPMNManager;
import org.yaoqiang.graph.view.Graph;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GraphTools {


	public HashMap<ArrayList<GraphItem>, HashMap<GraphEdge, String>> findSubGraphs(ArrayList<GraphItem> items) {
        ArrayList<ArrayList<GraphItem>> parts = new ArrayList<>();
        ArrayList<GraphItem> part = new ArrayList<>();
        parts.add(part);

        for (GraphItem item: items) {

            if (containsItem(parts, item)) {
                if (part.size()> 0 && !parts.contains(part)) {
                    parts.add(part);
                }
                part = new ArrayList<>();
                continue;
            }

            handleItem(items, part, item);
        }

        HashMap<ArrayList<GraphItem>, HashMap<GraphEdge, String>> result = new HashMap<>();
        for (ArrayList<GraphItem> subGraph: parts) {

            HashMap<GraphEdge, String> border = new HashMap<>();

            for (GraphItem item :subGraph) {

                if (item instanceof GraphEdge) {
                    GraphEdge edge = (GraphEdge) item;

                    if (!items.contains(edge.getSource())  )
                        border.put(edge, "source");
                    else if ( !items.contains(edge.getTarget()) )
                        border.put(edge, "target");
                }
            }

            result.put(subGraph, border);
        }

        return result;
    }
	
	public boolean containsItem(ArrayList<ArrayList<GraphItem>> parts, GraphItem item) {
        for (ArrayList<GraphItem> part: parts) {
            if (part.contains(item))
                return true;
        }
        return false;
    }
	
    public void handleItem(ArrayList<GraphItem> items, ArrayList<GraphItem> part, GraphItem item) {

        if (part.contains(item))
            return;

        part.add(item);

        if (item instanceof GraphEdge) {
            GraphEdge edge = (GraphEdge) item;
            GraphNode source = edge.getSource();

            if (items.contains(source)) {
                handleItem(items, part, source);

            }

            GraphNode target = edge.getTarget();

            if (items.contains(target)) {
                handleItem(items, part, target);
            }
        } else if (item instanceof GraphNode) {
            GraphNode node = (GraphNode) item;
            CopyOnWriteArrayList<GraphEdge> inEdges = node.getInEdges();

            for (GraphEdge edge:inEdges) {

                if (items.contains(edge)) {
                    handleItem(items, part, edge);
                }
            }
            CopyOnWriteArrayList<GraphEdge> outEdges = node.getOutEdges();

            for (GraphEdge edge:outEdges) {

                if (items.contains(edge)) {
                    handleItem(items, part, edge);
                }
            }
        }
    }
    
    public ArrayList<GraphItem> findItemsWithStatus(String status, HashMap<GraphItem, String> results) {
        ArrayList<GraphItem> list = new ArrayList<>();

        for (GraphItem item: results.keySet()) {

            if (results.get(item) == status ) {
                list.add(item);
            }
        }

        return list;
    }

    public DirectedGraph getDependencyGraph(ModelManager modelManager) {
        DependencyAnalysis analyseDependency = new DependencyAnalysis();
        DirectedGraph dependencyGraph = analyseDependency.computeDependencyGraph(modelManager);
		return dependencyGraph;
    }

    public ModelManager getPGModelManager(String fileName) {
    	return getModelManager(fileName);
    }

    public ModelManager getModelManager(String fileName) {
        Path path = Paths.get(fileName);

        BPMNManager bpmnManager = new BPMNManager();
        Graph graph = bpmnManager.loadBPMNGraph(path);

        if (graph == null)
            return null;

        ModelManager pGManager = bpmnManager.convertBPMNToProcessGraph(graph, fileName.substring(fileName.lastIndexOf("/")+1));
		return pGManager;
    }
    
    public void printResults(HashMap<GraphItem, String> results) {
        for (GraphItem graphItem:results.keySet()) {

            if (graphItem instanceof GraphEdge)
                System.out.println("edge "+ ((GraphEdge)graphItem).getId()+ "  "  + " " + graphItem.getStatus());
            else
                System.out.println("node "+ ((GraphNode)graphItem).getName()+ "  "  + " " + graphItem.getStatus());
        }
    }
    
    public void convertDPtoGraphModel(DirectedGraph dependencyGraph, ModelManager modelManager, String dgID) {
  		ArrayList<Node> nodes = dependencyGraph.getNodes();
  		GraphModel graphModel = modelManager.createGraphModel(dgID);
  		graphModel.withType(GraphModel.Type.DG);
  		
  		HashMap<String, Long> idMapping = new HashMap<>();
  		
  		for (Node node : nodes) {			
  			long graphModelNodeID = modelManager.createGraphNote("dg_"+node.getId(), node.getId(), dgID);
  			idMapping.put(node.getId(), graphModelNodeID);
  		}
  		
  		ArrayList<Edge> edges = dependencyGraph.getEdges();
  		
  		for (Edge edge : edges) {
  			String sourceID = edge.getSourceNode().getId();
  			String targetID = edge.getTargetNode().getId();
  			String type = edge.getRelationshipEdge().getType();
  			modelManager.createGraphEdge(idMapping.get(sourceID), idMapping.get(targetID), type, dgID);
  		}
  	}
    
    public void removeOldSubGraphItems(GraphModel oldPGModel, GraphModel clone, ArrayList<GraphItem> key, GraphNode subGraphNode) {
		for (GraphItem graphItem : key) {
			GraphItem itemClone = clone.getGraphItemWithID(graphItem.getId());
			
			if (!subGraphNode.getInEdges().contains(itemClone) && !subGraphNode.getOutEdges().contains(itemClone)) {
				System.out.println("remove old " + itemClone.getId());
				clone.removeFromGraphItems(itemClone);
			}
			else if (itemClone.getStatus().equals(GraphItem.DELETED)) {
				itemClone.withStatus(GraphItem.NEW);
				
				GraphEdge removedEdge = (GraphEdge) oldPGModel.getGraphItemWithID(itemClone.getId());
				GraphNode sourceCloneNode = (GraphNode) clone.getGraphItemWithID(removedEdge.getSource().getId());
				GraphNode targetCloneNode = (GraphNode) clone.getGraphItemWithID(removedEdge.getTarget().getId());
				
				if(sourceCloneNode != null && targetCloneNode != null) {
					GraphEdge edgeClone = new GraphEdge()
					.withId(itemClone.getId())
					.withSource(sourceCloneNode)
					.withTarget(targetCloneNode)
					.withStatus(GraphItem.DELETED);
					
					clone.addEdge(edgeClone);
					edgeClone.setId(-itemClone.getId());
					
					System.out.println(clone.getGraphItemWithID(-itemClone.getId()));
				}
			}
		}
	}

    public GraphNode creatAndInsertSubGraphNode(GraphModel clone, HashMap<GraphEdge, String> graphBorder) {
		GraphNode subGraphNode = new GraphNode()
				.withName("SubGraph")
				.withStatus("new")
				.withType("abstract_sub_graph")
				.withId(clone.createUUID());

		//find clone items represented borderitems
		for (GraphItem borderItem : graphBorder.keySet()) {
			long borderItemId = borderItem.getId();
			GraphEdge borderItemClone = (GraphEdge)clone.getGraphItemWithID(borderItemId);

			String order = graphBorder.get(borderItem);
			
			//insert subgraph node into clone
			if ("source".equals(order) && borderItemClone != null)
				// TODO: fix stackoverflow
//            		borderItemClone.withSource(subGraphNode);
				subGraphNode.addToInEdges(borderItemClone);
			if ("target".equals(order) && borderItemClone != null)
				// TODO: fix stackoverflow
//            		borderItemClone.withTarget(subGraphNode);
				subGraphNode.addToOutEdges(borderItemClone);
		}
		
		clone.addToGraphItems(subGraphNode);
		
		return subGraphNode;
	}
    
    public void reduceGraph(GraphModel oldPGModel, GraphModel clone, HashMap<GraphItem, String> results) {
    	ArrayList<GraphItem> newItems = findItemsWithStatus(GraphItem.NEW, results);
		newItems.addAll(findItemsWithStatus(GraphItem.CHANGED, results));
		HashMap<ArrayList<GraphItem>, HashMap<GraphEdge, String>> subGraphs = findSubGraphs(newItems);

		HashMap<GraphItem, ArrayList<GraphItem>> subGraphNodes = new HashMap<>();

        for (ArrayList<GraphItem> key : subGraphs.keySet()) {
        	
        	if (key.size() < 4)
        		continue;
        	
        	HashMap<GraphEdge, String> graphBorder = subGraphs.get(key);
            GraphNode subGraphNode = creatAndInsertSubGraphNode(clone, graphBorder);
			subGraphNodes.put(subGraphNode, key);
			
			removeOldSubGraphItems(oldPGModel, clone, key, subGraphNode);
        }
	}
}
