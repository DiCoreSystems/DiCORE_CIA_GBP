package de.uniks.vs.evolution.graph.analysis.old;


import de.uniks.vs.evolution.graph.analysis.graphs.ControlDependenceGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.ControlFlowGraph;
import de.uniks.vs.evolution.graph.analysis.graphs.ProcessGraph;
import de.uniks.vs.evolution.graphmodels.GraphNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 16/6/18.
 */
public class Dependency_t {

    enum Type {
        S,
        L,
        CD,
        Marked
    }

    HashMap<String, GraphNode> tasks = new HashMap<>();
    HashMap<String, GraphNode> events = new HashMap<>();
    HashMap<String, GraphNode> gateways = new HashMap<>();

    private ProcessGraph createProcessGraph(String file)
    {
        ProcessGraph PG = new ProcessGraph();
        //String file= null;
//        DependencyAnalysis.computeDependencyGraph(file);
//        Vector<String> Nodes = Extended_Control_Flow_Graph.NodeSet;
//
//        for (String k:Nodes){
//            String Vertex=Edgefy(k);
//
//            PG.bpmnGraph.addVertex(Vertex);
//        }
//
//        G=PG.bpmnGraph;
//        return G;
        return PG;
    }

    private ControlDependenceGraph createDependenceGraph(ProcessGraph pg)
    {
        ControlDependenceGraph dependenceGraph = new ControlDependenceGraph();
        ArrayList<GraphNode> nodes = pg.getNodes();

        for (GraphNode i: nodes)
        {
            String EdgeSource= getEdgeName(i);

            for (GraphNode j: nodes){

                    if(!(dependenceGraph.getEdge(String.valueOf(i.getId()), String.valueOf(j.getId()))).toString().isEmpty()){
                        String EdgeTarget= getEdgeName(j);
                        dependenceGraph.addEdge(EdgeSource,EdgeTarget);
                        dependenceGraph.addEdge(EdgeSource, EdgeTarget, dependenceGraph.getEdge(String.valueOf(i.getId()), String.valueOf(j.getId())).toString());
                    }
            }
        }
        return dependenceGraph;
    }

    public  void ComputeAllPaths(String file) {
//        //init_Edges();
//        ControlFlowGraph controlFlowGraph = computeControlFlowGraph(file);
//        NodeSet=ExtendedControl_Flow_Graph.NodeSet;
//        G=ExtendedControl_Flow_Graph.G;
//
//        //System.out.println(G);
//        // AllPaths allpaths1 = new AllPaths(G, "1", "exit");
//        //  System.out.println("POSTDOM(1) = " + intersec);
//        String[] str = new String[NodeSet.size()+1];
//        str[0]= "entry"; //,"1","2","3","4","5","6","7","exit"};
//        //List<String> str= new ArrayList<String>();
//
//        for (int i=1; i<NodeSet.size()-1;i++)
//        {
//            str[i]=String.valueOf(i);
//
//        }
//        str[NodeSet.size()-1]="exit";
//        //String[] str = {"entry","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"
//        //		,"16","17","18","19","exit"};
//
//
//
//        for (int i=0; i<str.length-1;i++)
//        {
//            AllPath(G,str[i],"exit");
//            //System.out.println();
//
//        }
//        System.out.println("List of postdoms : "+postdom);
//			/*int i= 0;
//			while (i<trace.size())
//			{	if (trace.get(i).isEmpty())
//				trace.remove(i);
//			else
//			i++;
//			}
//			// System.out.println("Trace " + trace );
//			 //System.out.println(i + "vs" + trace.size());
//
//            // System.out.println("*******" + AllPath2(G,"6","exit"));
//            // System.out.println("*******" + AllPath(G,"6","exit"));
//*/
//    }
//
//    private static ControlFlowGraph computeControlFlowGraph(String file) {
//        computeParserOutput(file);
//        // ENTRY & STOP
//
//        for (Entry<String, Node> entry : e.entrySet()) {
//            String cle = entry.getKey();
//
//            if (entry.getValue().getClass().toString()
//                    .endsWith("TStartEventImpl")) {
//                setStart(cle);
//                NodeCorrespandence.put(getStart(), "entry");
//            } else if (entry.getValue().getClass().toString()
//                    .endsWith("TEndEventImpl")) {
//                setEnd(cle);
//                NodeCorrespandence.put(cle, "exit");
//
//            }
//        }
//
//        setTF(corresponds(start));
//        setTF(Next(getCurrentGraphItem()));
//        // TF= Next(TF);
//        setCompteur(1);
//        // The REST of extCFGNodes
//        while (getCompteur() < e.size() + t.size() + g.size() - 1
//                & !TF.getId().equals(getEnd())) {
//
//            while (TF.getClass().toString().endsWith("GatewayImpl")) {
//                if (TF.getClass().toString().endsWith("TParallelGatewayImpl"))
//                    Sync_list.add(String.valueOf(getCompteur()));
//                TFlowElement value = Gateway(getCurrentGraphItem());
//                if (TF.getClass().toString().endsWith("TParallelGatewayImpl"))
//                    Sync_list.add(String.valueOf(getCompteur()));
//                setTF(value);
//                NodeCorrespandence.put(TF.getId(),
//                        String.valueOf(getCompteur()));
//                setCompteur(getCompteur() + 1);
//                setTF(Next(getCurrentGraphItem()));
//            }
//            NodeCorrespandence.put(TF.getId(), String.valueOf(getCompteur()));
//            setCompteur(getCompteur() + 1);
//            setTF(Next(getCurrentGraphItem()));
//
//        }
//        //System.out.println();
//        //hashprint(NodeCorrespandence);
//        init_G();
//        init_NodesSet();
//        getParallel_list();
//
//    }
//
//    private static void computeParserOutput(String file) {
//        DocumentRootImpl DocumentRoot;
//        if (file.equals(null))
//
//            DocumentRoot=BPMNParser.parseDescription("purchase.bpmn.20.xml");
//
//        else{
//            DocumentRoot=BPMNParser.parseDescription(file);
//        }
//        EList<TRootElement> proc = DocumentRoot.getDefinitions().getRootElement();
//        int i=0;
//        while(!(proc.get(i).getClass().toString().contains("TProcessImpl")))
//        {
//            i++;
//        }
//        TProcessImpl process = (TProcessImpl) (proc.get(i));
//
//        EList<TFlowElement> flowElements = process.getFlowElement();
//        Iterator<TFlowElement> flowElement = flowElements.iterator();
//
//        while(flowElement.hasNext()){
//
//            TFlowElement element = flowElement.next();
//            // System.out.println("cc "+element.getId()+" "+ element.getName());
//            Names.put(element.getId(), element.getName());
//            String test = element.getClass().getSimpleName();
//            if ((test.contains("Task")))
//            {tasks.put(element.getId(),element);
//            }
//            else if (test.contains("Gateway"))
//            {gateways.put(element.getId(),element);}
//            else if (test.contains("SequenceFlow"))
//                sequenceFlow.put(element.getId(),element);
//            else if (test.contains("Event"))
//                events.put(element.getId(),element);
//            else if (test.contains("DataObject"))
//                dataObjects.put(element.getId(),element);
//
//
//
//        }
//
//    }
//
//    public void computePostDomTree(String file) {
//        // create a JGraphT graph
//        AllPaths.ComputeAllPaths(file);
//        List<List<String>> dpost=AllPaths.postdom;
//        Vector<String> nonDesigned = new Vector<String>() ;
//        Vector<String> NV = new Vector<String>();
//        Vector<String> LastErased = new Vector<String>();
//        init_nodeSet();
//        nonDesigned=NodeSet;
//        nonDesigned.remove("exit");
//        LastErased.add("exit");
//        int exit_condition= (dpost.size())-1;
//
//        while (exit_condition>0)
//
//        {
//            //	System.out.println("not empty");
//            Nodes nodesErased = new Nodes(LastErased);
//            for (String i : nodesErased) {
//
//
//                //boolean remove = false;
//
//                for (String j : nonDesigned) {
//                    int l=init(j);
//
//                    //	pause();
//
//                    //	while(remove==true)
//                    //	remove=false;
//
//                    if (dpost.get(l).contains(i))
//                    {
//
//              			   /* System.out.println("contains " + i);
//            				System.out.println((dpost.get(l).size()));
//            				System.out.println(dpost.get(l).remove(i));
//            				System.out.println((dpost.get(l).size()));
//            				System.out.println(dpost.get(l)); */
//
//                        dpost.get(l).remove(i);
//                        if ((dpost.get(l)).size() == 1)
//
//                        {
//
//                            //nonDesigned.remove(j);
//                            gr.addVertex(j);
//                            gr.addVertex(i);
//                            gr.addEdge(i, j);
//                            G.addEdge(i, j);
//                            NV.add(j);
//                            //System.out.println("gr= "+gr);
//                            // pause();
//                            exit_condition--;
//
//                            // System.out.println("s=" + exit_condition);
//
//                        }
//
//
//
//                    }
//                }
//
//            }
//            LastErased=NV;
//            //System.out.println("new while");
//            //pause();
//        }
//        System.out.println("PDT= " + gr);


    }

    private String getEdgeName(GraphNode node) {
        ControlFlowGraph controlFlowGraph = new ControlFlowGraph();
        String cle = null;
        String vertexPreffix = "x";

        for (Map.Entry<String, String> entry : controlFlowGraph.getNodeCorrespondences().entrySet())

            if(entry.getValue().equals(node))
                cle = entry.getKey();

        String gatewayType = "Xor";

        String vertexSuffix= controlFlowGraph.getSuffixForName(cle);

        if (tasks.containsKey(cle)) {
            vertexPreffix= "a";
        }
        else if (events.containsKey(cle)){
            vertexPreffix = "e";
        }
        else if (gateways.containsKey(cle)){
            vertexPreffix = "g";
            // TODO // FIXME: 16/7/8
//            Node node = getCorrespondences(cle);
//            TFlowNodeImpl T = (TFlowNodeImpl) GTF;
//            TGatewayImpl gate = (TGatewayImpl) T;
//            TGatewayDirection direction = gate.getGatewayDirection();
//
//            if(gate.getClass().getName().endsWith("TParallelGatewayImpl"))
//                gatewayType = "join";
//
//            if (direction.toString().equalsIgnoreCase("Converging")) {
//                vertexSuffix = gatewayType + "-converge";
//            }
//            else  vertexSuffix = gatewayType + "-diverge";
        }
        String Vertex = "("+vertexPreffix+","+vertexSuffix+")";
        return Vertex;
    }

    private GraphNode getCorrespondences(String string) {
        // TODO Refactoring
        for (String entry : tasks.keySet()) {
            if (entry.equals(string)) {
                return tasks.get(entry);
            }
        }
        for (Map.Entry<String, GraphNode> entry2 : gateways.entrySet()) {
            if (entry2.getKey().equals(string)) {
                return entry2.getValue();
            }
        }
        for (Map.Entry<String, GraphNode> entry3 : events.entrySet()) {
            if (entry3.getKey().equals(string)) {
                return entry3.getValue();
            }
        }
        return null;
    }
}
