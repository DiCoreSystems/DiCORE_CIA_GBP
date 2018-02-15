package de.uniks.vs.evolution.graph.analysis.graphs;

import de.uniks.vs.evolution.graph.analysis.manager.BPMNManager;
import de.uniks.vs.evolution.graphmodels.GraphEdge;
import de.uniks.vs.evolution.graphmodels.GraphItem;
import de.uniks.vs.evolution.graphmodels.GraphNode;
import de.uniks.vs.evolution.graphmodels.ModelManager;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by alex on 16/7/8.
 */
public class ControlFlowGraph {

    HashMap<Long, GraphItem> tasks = new HashMap<>();
    HashMap<Long, GraphItem> events = new HashMap<>();
    HashMap<Long, GraphItem> gateways = new HashMap<>();
    HashMap<Long, GraphItem> sequenceFlows = new HashMap<>();

    GraphItem currentGraphItem;
    String start = "";
    String end = "";
    int count = 1;
    Vector<String> nodes = new Vector<String>();
    DirectedTreeMapGraph g_ControlFlow = new DirectedTreeMapGraph();
    DirectedGraph ecfg = new DirectedGraph();

    HashMap<Long, String> names = new HashMap<Long, String>();
    Vector<String> sync_list = new Vector<String>();
    Vector<String> parallel_list = new Vector<String>();
    Stack<String> gw = new Stack<String>();

    private HashMap<String,String> nodeCorrespondence = new HashMap<>();
    HashMap<String, String> suffix = new HashMap<>();


    public void computeControlFlowGraph(ModelManager manager) {
        computeParserOutput(manager);
        // ENTRY & STOP

        for (Long entry : events.keySet()) {
            String cle = entry.toString();

//            if (events.get(entry).getClass().toString().endsWith("TStartEventImpl")) {
            if (((GraphNode)events.get(entry)).getName().startsWith("startevent")) {

                // TODO: fix it workaround
                if (nodeCorrespondence.containsValue("entry"))
                    continue;

                setStart(cle);
                nodeCorrespondence.put(getStart(), "entry");
            }
//            else if (events.get(entry).getClass().toString().endsWith("TEndEventImpl")) {
            else if (((GraphNode)events.get(entry)).getName().startsWith("endevent")) {
                setEnd(cle);
                nodeCorrespondence.put(cle, "exit");

            }
        }

        setCurrentGraphItem(corresponds(start));
        setCurrentGraphItem(Next(getCurrentGraphItem()));
        // TF= Next(TF);
        setCompteur(1);
        // The REST of extCFGNodes
        
        if (String.valueOf(currentGraphItem.getId()) == null)
        	System.err.println("value error!!!");
        
        while (currentGraphItem != null && getMeter() < events.size() + tasks.size() + gateways.size() - 1
                & !String.valueOf(currentGraphItem.getId()).equals(getEnd())) {

            while (BPMNManager.GATEWAY.equals(((GraphNode)currentGraphItem).getType())) {

//                if (TF.getClass().toString().endsWith("TParallelGatewayImpl"))
                if (((GraphNode)currentGraphItem).getName().startsWith(BPMNManager.PARALLEL))
                    sync_list.add(String.valueOf(getMeter()));

                //TODO : workaround
//                                GraphItem value = Gateway(getCurrentGraphItem());
                GraphItem value = getCurrentGraphItem();
                if (!(((GraphNode)currentGraphItem).getName().contains("-" + BPMNManager.JOIN))) {
                    value = Gateway(getCurrentGraphItem());
                }


//                if (TF.getClass().toString().endsWith("TParallelGatewayImpl"))
                if (((GraphNode)currentGraphItem).getName().startsWith(BPMNManager.PARALLEL))
                    sync_list.add(String.valueOf(getMeter()));

                setCurrentGraphItem(value);
                nodeCorrespondence.put(String.valueOf(currentGraphItem.getId()), String.valueOf(getMeter()));
                setCompteur(getMeter() + 1);
                setCurrentGraphItem(Next(getCurrentGraphItem()));
            }
            nodeCorrespondence.put(String.valueOf(currentGraphItem.getId()), String.valueOf(getMeter()));
            setCompteur(getMeter() + 1);
            setCurrentGraphItem(Next(getCurrentGraphItem()));

        }
        init_G();
        init_NodesSet();
        getParallel_list();
    }


    private Vector<String> getParallel_list() {

        for (int i = 0; i < sync_list.size(); i = +2) {
            int de = Integer.parseInt(sync_list.get(i));
            int a = Integer.parseInt(sync_list.get(i + 1)) - 1;
            while (de < a) {
                de++;
                parallel_list.add(String.valueOf(de));

            }
        }
        return parallel_list;
    }

    public void init_NodesSet() {
        nodes.removeAll(nodes);
        for (Map.Entry<String, String> entry : nodeCorrespondence.entrySet()) {
            String Value = entry.getValue();
            nodes.add(Value);
        }
    }

    private void init_G() {

        for (Long entry : sequenceFlows.keySet()) {
            GraphItem event = sequenceFlows.get(entry);
            GraphEdge arc = (GraphEdge) event;
            GraphNode cle_de = arc.getSource();
            GraphNode cle_a = arc.getTarget();
            String Value_de = nodeCorrespondence.get(String.valueOf(cle_de.getId()));
            String Value_a = nodeCorrespondence.get(String.valueOf(cle_a.getId()));

            if (Value_a == null || Value_de == null)
                continue;
            g_ControlFlow.addEdge(Value_de, Value_a);
        }
    }

    private GraphItem Gateway(GraphItem tf2) {
        nodeCorrespondence.put(String.valueOf(currentGraphItem.getId()), String.valueOf(getMeter()));
        setCompteur(getMeter() + 1);
        GraphNode TFn = (GraphNode) tf2;
        GraphItem value = getCurrentGraphItem();
        int z = 0;

        for (Long entry : sequenceFlows.keySet()) {
            GraphItem TFe = sequenceFlows.get(entry);
            GraphEdge arc = (GraphEdge) TFe;
            GraphNode cle_de = arc.getSource();
            GraphNode cle_a = arc.getTarget();

            if (TFn.getId() == cle_de.getId()) {
                z++;
                gw.push(String.valueOf(cle_a.getId()));
            }
        }
        // /liste pleine des suivants

        int i = 0;
        setCurrentGraphItem(corresponds(gw.pop()));

        while (i < z) {

            if (BPMNManager.GATEWAY.equals(((GraphNode)currentGraphItem).getType())) {
                GraphNode T = (GraphNode) getCurrentGraphItem();
//                TGatewayImpl gate = (TGatewayImpl) T;
//                TGatewayDirection direction = gate.getGatewayDirection();
                if (T.getName().contains("-" + BPMNManager.JOIN)) {
//                if (direction.toString().equalsIgnoreCase("Converging")) {
                    i++;
                    if (i == (z - 1)) {
                        setCurrentGraphItem(corresponds(gw.pop()));
                    }

                } else {
                    value = (Gateway(getCurrentGraphItem()));
                    setCurrentGraphItem(value);
                    nodeCorrespondence.put(String.valueOf(currentGraphItem.getId()), String.valueOf(getMeter()));
                    setCompteur(getMeter() + 1);
                    setCurrentGraphItem(Next(getCurrentGraphItem()));

                }
            } else {
                nodeCorrespondence.put(String.valueOf(currentGraphItem.getId()), String.valueOf(getMeter()));
                setCompteur(getMeter() + 1);
                setCurrentGraphItem(Next(getCurrentGraphItem()));
            }

        }
        return getCurrentGraphItem();
    }

    private void computeParserOutput(ModelManager manager) {

        CopyOnWriteArrayList<GraphItem> graphItems = manager.getModel(manager.getLastModelID()).getGraphItems();
//        ArrayList<GraphItem> graphItems = manager.getModel(manager.getLastModelID()).getGraphItems();

        for (GraphItem graphItem: graphItems ) {

//            String test = element.getClass().getSimpleName();
            names.put(graphItem.getId(), graphItem.getClass().getSimpleName());

            if ( graphItem instanceof GraphEdge) {
                GraphEdge graphEdge = (GraphEdge) graphItem;
                sequenceFlows.put(graphEdge.getId(), graphEdge);
            }

            else if ( graphItem instanceof GraphNode) {
                GraphNode graphNode = (GraphNode) graphItem;


                if (BPMNManager.ACTIVITY.equals(graphNode.getType())) {
                    tasks.put(graphNode.getId(), graphNode);
                }

                else if (BPMNManager.GATEWAY.equals(graphNode.getType())) {
                    gateways.put(graphNode.getId(), graphNode);
                }

                else if (BPMNManager.EVENT.equals(graphNode.getType()))
                    events.put(graphNode.getId(), graphNode);
            }

        }

    }


    private  GraphItem Next(GraphItem tF) {
        // returns only one successor of tF
        for (Long entry : sequenceFlows.keySet()) {
            GraphItem TFi = sequenceFlows.get(entry);
            GraphEdge arc = (GraphEdge) TFi;
            GraphNode de = arc.getSource();
            GraphNode a = arc.getTarget();

            if (tF.getId() == de.getId()) {
                return (corresponds(a.getId()));
            }
        }
        return null;
    }

    private GraphItem corresponds(long id) {
        // TODO : change all to Long
        return corresponds(String.valueOf(id));
    }

    private GraphItem corresponds(String string) {

        for (Long entry : tasks.keySet()) {
            if (entry.toString().equals(string)) {
                return tasks.get(entry);
            }
        }
        for (Long entry2 : gateways.keySet()) {
            if (entry2.toString().equals(string)) {
                return gateways.get(entry2);
            }
        }
        for (Long entry3 : events.keySet()) {
            if (entry3.toString().equals(string)) {
                return events.get(entry3);
            }
        }

        return null;
    }


    public HashMap<String, String> getNodeCorrespondences() {
        return nodeCorrespondence;
    }

    public String getSuffixForName(String name) {
        return suffix.get(name);
    }

    public Vector<String> getNodes() {
        return nodes;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public GraphItem getCurrentGraphItem() {
        return currentGraphItem;
    }

    private void setCurrentGraphItem(GraphItem item) {
        this.currentGraphItem = item;
    }

    public int getMeter() {
        return count;
    }

    private void setCompteur(int i) {
        this.count = i;
    }

    public Vector<String> getParallelList() {
        return parallel_list;
    }

    public Vector<String> getSynclist() {
        return sync_list;
    }

    public HashMap<Long, GraphItem> getEvents() {
        return events;
    }

    public HashMap<Long, GraphItem> getTasks() {
        return tasks;
    }

    public HashMap<Long, GraphItem> getGateways() {
        return gateways;
    }

    public HashMap<Long, GraphItem> getSequenceFlows() {
        return sequenceFlows;
    }

    public DirectedTreeMapGraph getG() {
        return g_ControlFlow;
    }
}
