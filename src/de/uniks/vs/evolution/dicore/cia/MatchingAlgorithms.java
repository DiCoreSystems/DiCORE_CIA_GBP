package de.uniks.vs.evolution.dicore.cia;

import de.uniks.vs.evolution.graphmodels.GraphEdge;
import de.uniks.vs.evolution.graphmodels.GraphItem;
import de.uniks.vs.evolution.graphmodels.GraphModel;
import de.uniks.vs.evolution.graphmodels.GraphNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by alex on 16/8/9.
 */
public class MatchingAlgorithms {


	public HashMap<GraphEdge, String> compareEdges(ArrayList<GraphEdge> n_edges, ArrayList<GraphEdge> o_edges) {
        HashMap<GraphEdge,GraphEdge> matching1 = compare(n_edges, o_edges);
        HashMap<GraphEdge,GraphEdge> matching2 = compare(o_edges, n_edges);

//        printResult(matching1);
//        printResult(matching2);

        HashMap<GraphEdge, String> results = new HashMap<>();

        for (GraphEdge graphEdge: matching1.keySet()) {

            if (matching1.get(graphEdge) == null) {
                //new
                results.put(graphEdge, GraphItem.NEW);
                graphEdge.withStatus(GraphItem.NEW);
            }
            else {
                //changed
                results.put(graphEdge, GraphItem.CHANGED);
                graphEdge.withStatus(GraphItem.NEW);
                matching1.get(graphEdge).withStatus(GraphItem.DELETED);
            }
        }

        for (GraphEdge graphEdge: matching2.keySet()) {

            if (matching2.get(graphEdge) == null) {
                //removed
                results.put(graphEdge, GraphItem.DELETED);
                graphEdge.withStatus(GraphItem.DELETED);
            }
            else if(!(matching1.get(matching2.get(graphEdge)) == graphEdge)) {
                //changed
                results.put(graphEdge, GraphItem.CHANGED);
                graphEdge.withStatus(GraphItem.DELETED);
                matching2.get(graphEdge).withStatus(GraphItem.NEW);
            }
        }

//        printResult4(results);

        return results;
    }
	
	public void printPatternMatchingResult(HashMap<String, String> patternMatchingResult) {
		
		for (String key : patternMatchingResult.keySet()) {
			System.out.println(key + " " + patternMatchingResult.get(key));
		}
	}

    private void printResult4(HashMap<GraphEdge, String> results) {
        System.out.println("--------------------------- Changed Edges -------------------------------------");
        for (GraphEdge edge:results.keySet()) {
            System.out.println(edge.getId() + ": " + edge.getStatus()  );
        }
        System.out.println("---------------------------------------------------------------------");
    }

    private HashMap<GraphEdge, GraphEdge> compare(ArrayList<GraphEdge> nEdges, ArrayList<GraphEdge> oEdges) {
        ArrayList<GraphEdge> o_edges = (ArrayList<GraphEdge>) nEdges.clone();
        ArrayList<GraphEdge> n_edges = (ArrayList<GraphEdge>) oEdges.clone();

        HashMap<GraphEdge, GraphEdge> matching = new HashMap<>();
        ArrayList<GraphEdge> save = new ArrayList<GraphEdge>();

        ArrayList<GraphEdge> remove = new ArrayList<>();
        ArrayList<GraphEdge> insert = new ArrayList<>();

        for (int iter = 100; iter > 0; iter-=50)

            for (GraphEdge sourceEdge : o_edges) {

                for (GraphEdge graphEdge : n_edges) {
                    int compare = compare(sourceEdge, graphEdge);

                    if(compare < iter)
                        continue;

                    if (!matching.containsKey(sourceEdge)) {
                        matching.put(sourceEdge, graphEdge);
                        remove.add(graphEdge);
                    }
                    else {
                        GraphEdge graphEdge1 = matching.get(sourceEdge);

                        if (compare > compare(sourceEdge, graphEdge1)) {
                            matching.put(sourceEdge, graphEdge);
                            insert.add(graphEdge1);
                            remove.add(graphEdge);
                        }
                    }

                    if (compare == 100)
                        save.add(sourceEdge);
                }

                for (GraphEdge graphEdge: insert) {
                    n_edges.add(graphEdge);
                }
                insert.clear();

                for (GraphEdge graphEdge: remove) {
                    n_edges.remove(graphEdge);
                }
                remove.clear();

                if (!matching.containsKey(sourceEdge)){
                    matching.put(sourceEdge, null);
                }
            }

        for (GraphEdge graphEdge: save) {
            matching.remove(graphEdge);
        }
        return matching;
    }

    public int compare(GraphEdge sourceEdge, GraphEdge graphEdge) {

        int result = 0;

        if(graphEdge == null)
            return result;

        GraphNode source1 = sourceEdge.getSource();
        GraphNode source2 = graphEdge.getSource();
        GraphNode target1 = sourceEdge.getTarget();
        GraphNode target2 = graphEdge.getTarget();

        if (source1.getType().equals(source2.getType())) {
            result += 20;
        }

        if (target1.getType().equals(target2.getType())) {
            result += 20;
        }

        if (source1.getInEdges().size() == (source2.getInEdges().size())) {
            result += 10;
        }

        if (target1.getInEdges().size() == (target2.getInEdges().size())) {
            result += 10;
        }

        if (source1.getOutEdges().size() == (source2.getOutEdges().size())) {
            result += 10;
        }

        if (target1.getOutEdges().size() == (target2.getOutEdges().size())) {
            result += 10;
        }

        if (source1.getName().equals(source2.getName())) {
            result += 10;
        }

        if (target1.getName().equals(target2.getName())) {
            result += 10;
        }

        return result;
    }

    public HashMap<GraphItem, String> compareProcessGraphs(GraphModel newModel, GraphModel oldModel) {
        HashMap<GraphItem,String> results = new HashMap<>();

        ArrayList<GraphEdge> n_edges = newModel.getEdges();
        ArrayList<GraphNode> n_nodes = newModel.getNodes();

        ArrayList<GraphEdge> o_edges = oldModel.getEdges();
        ArrayList<GraphNode> o_nodes = oldModel.getNodes();

        HashMap<GraphEdge, String> edgeResults = compareEdges(n_edges, o_edges);
        HashMap<GraphNode, String> nodeResults = compareNodes(n_nodes, o_nodes);

        results.putAll(edgeResults);
        results.putAll(nodeResults);

        return results;
    }

    private HashMap<GraphNode,String> compareNodes(ArrayList<GraphNode> n_nodes, ArrayList<GraphNode> o_nodes) {
        HashMap<GraphNode,GraphNode> matching1 = compareNodeLists(n_nodes, o_nodes);
        HashMap<GraphNode,GraphNode> matching2 = compareNodeLists(o_nodes, n_nodes);

        printResult2(matching1);
        printResult2(matching2);

        HashMap<GraphNode, String> results = new HashMap<>();

        for (GraphNode graphNode: matching1.keySet()) {

            if (matching1.get(graphNode) == null) {
                //new
                results.put(graphNode, GraphItem.NEW);
                graphNode.withStatus(GraphItem.NEW);
            }
            else {
                //changed
                results.put(graphNode, GraphItem.CHANGED);
                graphNode.withStatus(GraphItem.DELETED);
                matching1.get(graphNode).withStatus(GraphItem.NEW);
            }
        }

        for (GraphNode graphNode: matching2.keySet()) {

            if (matching2.get(graphNode) == null) {
                //removed
                results.put(graphNode, GraphItem.REMOVED);
                graphNode.withStatus(GraphItem.DELETED);
            }
            else if(!(matching1.get(matching2.get(graphNode)) == graphNode)) {
                //changed
                results.put(graphNode, GraphItem.CHANGED);
                graphNode.withStatus(GraphItem.DELETED);
                matching2.get(graphNode).withStatus(GraphItem.NEW);
            }
        }

        //TODO: FIX workaround !!!!!
        for ( GraphNode node :n_nodes) {

            if (node.getInEdges().size()>0 && node.getOutEdges().size()>0) {

                CopyOnWriteArrayList<GraphEdge> inEdges = node.getInEdges();
                String status2 = inEdges.get(0).getStatus();
                CopyOnWriteArrayList<GraphEdge> outEdges = node.getOutEdges();
                String status1 = outEdges.get(0).getStatus();

                if (GraphItem.NEW.equals(status1) && GraphItem.NEW.equals(status2)
                        && results.get(node) == null) {
//                    GraphNode graphNode = matching1.get(node);
//                    graphNode.withStatus(GraphItem.DELETED);
//                    results.put(graphNode, GraphItem.REMOVED);

                    node.withStatus(GraphItem.NEW);
                    results.put(node, GraphItem.NEW);
                }
            }

        }

        printResult3(results);

        return results;
    }

    private void printResult3(HashMap<GraphNode, String> results) {

        System.out.println("--------------------------- Changed Nodes -------------------------------------");
        for (GraphNode node:results.keySet()) {
                System.out.println(node.getId() + ": " + node.getStatus()  );
        }
        System.out.println("---------------------------------------------------------------------");

    }


    private HashMap<GraphNode,GraphNode> compareNodeLists(ArrayList<GraphNode> nNodes, ArrayList<GraphNode> oNodes) {
        ArrayList<GraphNode> o_nodes = (ArrayList<GraphNode>) nNodes.clone();
        ArrayList<GraphNode> n_nodes = (ArrayList<GraphNode>) oNodes.clone();

        HashMap<GraphNode, GraphNode> matching = new HashMap<>();
        ArrayList<GraphNode> save = new ArrayList<>();

        ArrayList<GraphNode> remove = new ArrayList<>();
        ArrayList<GraphNode> insert = new ArrayList<>();

        for (int iter = 100; iter > 0; iter-=50)

            for (GraphNode sourceNode : o_nodes) {

                for (GraphNode graphNode : n_nodes) {
                    int compare = compare(sourceNode, graphNode);

                    if(compare < iter)
                        continue;

                    if (!matching.containsKey(sourceNode)) {
                        matching.put(sourceNode, graphNode);
                        remove.add(graphNode);
                    }
                    else {
                        GraphNode graphNode1 = matching.get(sourceNode);

                        if (compare > compare(sourceNode, graphNode1)) {
                            matching.put(sourceNode, graphNode);
                            insert.add(graphNode1);
                            remove.add(graphNode);
                        }
                    }

                    if (compare == 100)
                        save.add(sourceNode);
                }

                for (GraphNode graphNode: insert) {
                    n_nodes.add(graphNode);
                }
                insert.clear();

                for (GraphNode graphNode: remove) {
                    n_nodes.remove(graphNode);
                }
                remove.clear();

                if (!matching.containsKey(sourceNode)){
                    matching.put(sourceNode, null);
                }
            }

        for (GraphNode graphNode: save) {
            matching.remove(graphNode);
        }
        return matching;
    }

    private int compare(GraphNode source, GraphNode target) {
        int result = 0;

        if(target == null)
            return result;

        if (source.getType().equals(target.getType())) {
            result += 40;
        }

        if (source.getInEdges().size() == (target.getInEdges().size())) {
            result += 20;
        }

        if (source.getOutEdges().size() == (target.getOutEdges().size())) {
            result += 20;
        }

        if (source.getName().equals(target.getName())) {
            result += 20;
        }

        return result;
    }

    private void printResult(HashMap<GraphEdge, GraphEdge> matching) {
        System.out.println("---------------------------Edges-------------------------------------");
        for (GraphEdge graphEdge:matching.keySet()) {
            if (matching.get(graphEdge) != null) {
                System.out.println(graphEdge.getId() + " - " + matching.get(graphEdge).getId() + "   "
                        + compare(graphEdge, matching.get(graphEdge)) +
                        "     " + graphEdge.getSource().getName() +"-" + matching.get(graphEdge).getSource().getName() +
                        "     " + graphEdge.getTarget().getName() +"-" + matching.get(graphEdge).getTarget().getName() );
            }
            else {
                System.out.println(graphEdge.getId() + " - NULL" +
                        "     " + graphEdge.getSource().getName() + "     " + graphEdge.getTarget().getName());
            }
        }
        System.out.println("---------------------------------------------------------------------");
    }

    private void printResult2(HashMap<GraphNode, GraphNode> matching) {
        System.out.println("---------------------------Nodes-------------------------------------");
        for (GraphNode graphNode:matching.keySet()) {
            if (matching.get(graphNode) != null) {
                System.out.println(graphNode.getId() + " - " + matching.get(graphNode).getId() + "   "
                        + compare(graphNode, matching.get(graphNode)));
            }
            else {
                System.out.println(graphNode.getId() + " - NULL");
            }
        }
        System.out.println("---------------------------------------------------------------------");
    }

	public HashMap<String, String> matchPattern(ChangeImpactPattern insertPattern, GraphModel model) {
		HashMap<String, String> result = null;
//		ArrayList<GraphNode> ruleNodes = insertPattern.getModels().get(0).getNodes();
		
//		long[][] matrix = getMatrixForGraph(model.getNodes());
		
		result = graphPatternMatching(model, insertPattern.getModels().get(0));
		
//		for (GraphNode ruleNode : ruleNodes) {
//			ArrayList<GraphNode> modelNodes = model.getNodes();
//			
//			for (GraphNode graphNode : modelNodes) {
//				
//				if ((ruleNode.getType().equals(graphNode.getType()) 
//							|| ruleNode.getType().equals("a") && graphNode.getType().equals("abstract_sub_graph"))
//						&& ruleNode.getStatus().equals(graphNode.getStatus())) {
//					System.out.println(ruleNode.getName()  + "   " + graphNode.getName()+"("+graphNode.getId()+")");
//					
//					ArrayList<GraphEdge> inEdges = ruleNode.getInEdges();
//					ArrayList<GraphEdge> outEdges = ruleNode.getOutEdges();
//					
//				}
//			}
//		}
		return result;
	}

	private long[][] getMatrixForGraph(ArrayList<GraphNode> nodes) {
		long matrix[][] = new long [nodes.size()+1] [nodes.size()+1];
		matrix[0][0] = -1;
		HashMap<Long, Integer> map = new HashMap<>();
		
		for (int i = 0; i < nodes.size(); i++) {
			matrix[i+1][0] = nodes.get(i).getId();
			matrix[0][i+1] = nodes.get(i).getId();
			map.put(nodes.get(i).getId(), i+1);
		}
		
		for (GraphNode node : nodes) {
            CopyOnWriteArrayList<GraphEdge> outEdges = node.getOutEdges();
			
			for (GraphEdge graphEdge : outEdges) {
				matrix[map.get(node.getId())][map.get(graphEdge.getTarget().getId())] = 1;
			}
		}
		printMatrix(matrix);
		
		return matrix;
	}

	private void printMatrix(long[][] matrix) {
		
		for (int i = 0; i < matrix.length; i++) {
			
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
//			System.out.println();
		}
	}

	public HashMap<String, String> graphPatternMatching(GraphModel model, GraphModel pattern) {
		return graphPatternMatching(model, pattern, null);
	}
	
	public HashMap<String, String> graphPatternMatching(GraphModel model, GraphModel pattern, GraphNode ruleStartNode) {

		//  patternNode, graphNode
		HashMap<GraphNode, GraphNode> matching = new HashMap<>();
		HashMap<String, String> result = new HashMap<>();
		
		ArrayList<GraphNode> patternNodes = pattern.getNodes();
		GraphNode patternNode = ruleStartNode;
		
		if (patternNode == null)
			patternNode = patternNodes.get(0);
		
		GraphNode graphNode = null;
		
		for (GraphNode nextGraphNode : model.getNodes()) {
			
			if (!matching.values().contains(nextGraphNode) 
					&& comparePatternNode(patternNode, nextGraphNode) == 100) {
				graphNode = nextGraphNode;
				matching.put(patternNode, graphNode);
				break;
			}
		}
		
		int nextMatching = nextMatching(matching, patternNode, graphNode);
		
		if (nextMatching == 1) {
//			System.err.println("Rule "+ pattern.getName() + " matched :)");
			result.put(pattern.getName(), "matched");
		}
		else {
//			System.err.println("Rule "+ pattern.getName() + " dont matched");
			result.put(pattern.getName(), "dont_matched");
		}
			
		// TODO: break if current rule not matched
		ArrayList<GraphModel> subRules = pattern.getModels();
		
		for (GraphModel rule : subRules) {
			
			ArrayList<GraphNode> nodes = rule.getNodes();
			
			for (GraphNode ruleNode : nodes) {

				if(comparePatternNode(patternNode, ruleNode) == 100) 
					result.putAll(graphPatternMatching(model, rule, ruleNode));
					
			}
			
		}
		
		return result;
		
//		long[][] patternMatrix = getMatrixForGraph(pattern.getNodes());
//		HashMap<Long, Integer> patternMapping = new HashMap<>();
//		
//		for (int i = 1; i < patternMatrix.length; i++) {
//			patternMapping.put(patternMatrix[0][i], i);
//		}
//		
//		long[][] graphMatrix = getMatrixForGraph(model.getNodes());
//		HashMap<Long, Integer> graphMapping = new HashMap<>();
//		
//		for (int i = 1; i < graphMatrix.length; i++) {
//			graphMapping.put(graphMatrix[0][i], i);
//		}
//
//		// for (int p_i = 1; p_i < patternMatrix.length; p_i++) {
//		int p_i = 1;
//		long patterNodeID = patternMatrix[p_i][0];
//		GraphNode patternNode = (GraphNode) pattern.getGraphItemWithID(patterNodeID);
//
//		// for (int g_i = 1; g_i < graphMatrix.length; g_i++) {
//		int g_i = 1;
//		long graphNodeID = graphMatrix[g_i][0];
//		GraphNode graphNode = (GraphNode) model.getGraphItemWithID(graphNodeID);
//
//		if (comparePatternNode(patternNode, graphNode) == 100) {
//			ArrayList<GraphEdge> outEdges = patternNode.getOutEdges();
//			
//			if (outEdges.size() == 0)
//				return;
//				
//			p_i = patternMapping.get(outEdges.get(0).getId());
//		}
		
		// for (int g_j = 1; g_j < graphMatrix.length; g_j++) {
		//
		// }
		// }
		// }

		// First step : match empty V with empty graph V'.

		// Second step : try to math one node in V with one node in V'

		// Nth step : try to match one node in V with one new node in V',
		// if you cannot go back one step in the tree and try a new match
		// in the previous step.. and if you cant go back again etc...

		// END : when you find a leaf, i.e when you went through all the nodes
		// in V' or when you went through the all tree without finding a leaf.
	}

	private int nextMatching(HashMap<GraphNode, GraphNode> matching, GraphNode patternNode, GraphNode graphNode) {
		
		int comp1 = comp(matching, patternNode, graphNode, patternNode.getOutEdges(), graphNode.getOutEdges());
		int comp2 = comp(matching, patternNode, graphNode, patternNode.getInEdges(), graphNode.getInEdges());
		
		return comp1 + comp2 > -1 ? 1 : -1;
	}

	private int comp(HashMap<GraphNode, GraphNode> matching, GraphNode patternNode, GraphNode graphNode,
                     CopyOnWriteArrayList<GraphEdge> patternNodeEdges, CopyOnWriteArrayList<GraphEdge> graphNodeEdges) {
		
		int result = 1;
		
		for (GraphEdge pGraphEdge : patternNodeEdges) {
			GraphNode newPatternNode = pGraphEdge.getTarget();
			GraphNode nextGraphNode = null;
			
			for (GraphEdge graphEdge : graphNodeEdges) {
				GraphNode newGraphNode = graphEdge.getTarget();
				
				if ((!matching.values().contains(newGraphNode) /*|| graphEdge.getStatus().equals(GraphItem.DELETED)*/) && comparePatternNode(newPatternNode, newGraphNode) == 100) {
					nextGraphNode = newGraphNode;
					matching.put(patternNode, graphNode);
					
					int nextMatching = nextMatching(matching, newPatternNode, nextGraphNode);
					
					if (nextMatching == -1)
						matching.remove(patternNode);
				}
			}
			
			if(nextGraphNode == null) 
				return -1;
		}
		
		return result;
	}

	private int comparePatternNode(GraphNode patternNode, GraphNode graphNode) {
		
		int result = 0;

        if (patternNode.getType().equals(graphNode.getType()) && graphNode.getType().equals("a") )
        		result += 60;
        
        if (patternNode.getType().equals(graphNode.getType()) 
        		&& graphNode.getType().equals("g") 
        		&&  (patternNode.getName().equals(graphNode.getName()))
        		|| patternNode.getType().equals("a") && graphNode.getType().equals("abstract_sub_graph")) {
        			result += 60;
        }

        if (patternNode.getStatus().equals(graphNode.getStatus()) ) {
            result += 40;
        }

//        System.out.println(result + "   " + patternNode.getStatus() + " " + patternNode.getType() + "       " + graphNode.getStatus()+ " " + graphNode.getType());
        return result;
	}
}
