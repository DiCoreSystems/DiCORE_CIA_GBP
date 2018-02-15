package de.uniks.vs.evolution.graph.analysis.manager;

import com.mxgraph.util.mxCellRenderer;
import de.uniks.vs.evolution.graphmodels.GraphEdge;
import de.uniks.vs.evolution.graphmodels.GraphNode;
import de.uniks.vs.evolution.graphmodels.ModelManager;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.yaoqiang.graph.io.bpmn.BPMNCodec;
import org.yaoqiang.graph.model.GraphModel;
import org.yaoqiang.graph.swing.GraphComponent;
import org.yaoqiang.graph.view.Graph;
import org.yaoqiang.model.XMLElement;
import org.yaoqiang.model.bpmn.elements.core.common.FlowNode;
import org.yaoqiang.model.bpmn.elements.core.common.SequenceFlow;
import org.yaoqiang.model.bpmn.elements.events.Event;
import org.yaoqiang.model.bpmn.elements.gateways.Gateway;
import org.yaoqiang.model.bpmn.elements.process.activities.Task;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 16/7/8.
 */
public class BPMNManager {

    public static final String GATEWAY              = "g";
    public static final String EVENT                = "e";
    public static final String ACTIVITY             = "a";

    public static final String PARALLEL             = "AND";
    public static final String EXCLUSIVE            = "OR";
    public static final String JOIN                 = "join";
    public static final String SPLIT                = "split";

    public static final String PARALLEL_GATEWAY     = "ParallelGateway";
    public static final String EXCLUSIVE_GATEWAY    = "ExclusiveGateway";
    private String lastModelID;


    @Test
    public void testLoadBPMN() {
        String[] args = new String[1];
        args[0] = ".";

        Path folder = Paths.get(args[0]);

        if(!Files.isDirectory(folder)) {
            System.out.println(folder + " is no folder");
            return;
        }

        System.out.println(folder.getFileName());
        BPMNManager bpmnManager = new BPMNManager();

        try {
            Files.walk(folder).filter((p) -> p.toString().endsWith(".bpmn.20.xml") ||p.toString().endsWith(".bpmn2")).forEach(bpmnManager::createImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBPMNtoPG() {
        String fileName = "DiCORE_Paper_Example.bpmn.20.xml";
        Path path = Paths.get("./src/de/uniks/vs/evolution/graph/analysis/" + fileName);
        Graph graph = loadBPMNGraph(path);
        ModelManager modelManager = convertBPMNToProcessGraph(graph, fileName);

        de.uniks.vs.evolution.graphmodels.GraphModel model = modelManager.getModel(fileName);
        System.out.println("------------------------------------------------");
        System.out.println(model);
    }


    public void createImage(Path path) {

        printBPMN(path);
    }

    private void printBPMN(Path path) {
        GraphComponent graphComponent = this.loadBPMNGraphComponent(path);
        BufferedImage image = mxCellRenderer.createBufferedImage(graphComponent.getGraph(), null, 1, null, graphComponent.isAntiAlias(), null, true, graphComponent.getCanvas());

        Path bpmnImagePath = path.getParent().resolve(path.getFileName().toString() + ".png");
        File outputfile = bpmnImagePath.toFile();
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ModelManager convertBPMNToProcessGraph(Graph graph, String modelID) {
        this.lastModelID = modelID;
        Map<String, XMLElement> elementMap = graph.getBpmnElementMap();
        Set<Map.Entry<String, XMLElement>> entries = elementMap.entrySet();

        ArrayList<SequenceFlow> sequenceFlows = new ArrayList<>();
        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Gateway> gateways = new ArrayList<>();

        for (Map.Entry e : entries) {
            Object value = e.getValue();

            if (value instanceof SequenceFlow) {
                System.out.println("sequenceflow: " + (SequenceFlow) value);
                sequenceFlows.add((SequenceFlow) value);
            }
            else if (value instanceof Task) {
                System.out.println("task: " + e.getValue().getClass().getSimpleName());
                tasks.add((Task) value);
            }
            else if (value instanceof Event) {
                System.out.println("event: " + e.getValue().getClass().getSimpleName());
                events.add((Event) value);
            }
            else if (value instanceof Gateway) {
                System.out.println("gateway: " + e.getValue().getClass().getSimpleName());
                gateways.add((Gateway) value);
            }
        }

        ModelManager modelManager = new ModelManager();
        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), modelID);

        HashMap<String, Long> nodes = new HashMap<>();

        for (Event event : events) {
//            System.out.println("\n----------------------------------------");
//            System.out.println(event.getClass().getSimpleName());
            String value = event.get("id").toValue();
            System.out.println(value);
            String name = event.get("name").toValue();
            System.out.println(name);
//            System.out.println(event.getOutgoingSequenceFlows());
//            System.out.println(event.getIncomingSequenceFlows());
//            System.out.println(event.getOutgoingMessageFlows());
//            System.out.println(event.getIncomingMessageFlows());
//            System.out.println(event.getXMLAttributes());
//            System.out.println("----------------------------------------\n");


            String eventType = event.getClass().getSimpleName();

            long nodeID = modelManager.createGraphNote(BPMNManager.EVENT, eventType +"_" + event.get("id").toValue(), modelID);
            nodes.put(event.get("id").toValue(), nodeID);
        }

        for (Task task : tasks) {
//            System.out.println("\n----------------------------------------");
//            System.out.println(task.getClass().getSimpleName());
//            System.out.println(task.get("id").toValue());
//            System.out.println(task.get("name").toValue());
//            System.out.println(task.getOutgoingSequenceFlows());
//            System.out.println(task.getIncomingSequenceFlows());
//            System.out.println(task.getOutgoingMessageFlows());
//            System.out.println(task.getIncomingMessageFlows());
//            System.out.println(task.getXMLAttributes());
//            System.out.println("----------------------------------------\n");

            long nodeID = modelManager.createGraphNote(BPMNManager.ACTIVITY, task.get("name").toValue(), modelID);
            nodes.put(task.get("id").toValue(), nodeID);
        }

        for (Gateway gateway : gateways) {
//            System.out.println("\n----------------------------------------");
//            System.out.println(gateway.getClass().getSimpleName());
//            System.out.println(gateway.get("id").toValue());
//            System.out.println(gateway.get("name").toValue());
//            System.out.println(gateway.getOutgoingSequenceFlows());
//            System.out.println(gateway.getIncomingSequenceFlows());
//            System.out.println(gateway.getOutgoingMessageFlows());
//            System.out.println(gateway.getIncomingMessageFlows());
//            System.out.println(gateway.getXMLAttributes());
//            System.out.println("----------------------------------------\n");

            String type = "unspecified";
            if (PARALLEL_GATEWAY.equals(gateway.getClass().getSimpleName()))
                type = PARALLEL;

            else if (EXCLUSIVE_GATEWAY.equals(gateway.getClass().getSimpleName()))
                type = EXCLUSIVE;

            if (gateway.getIncomingSequenceFlows().size() == 1 && gateway.getOutgoingSequenceFlows().size() > 1)
                type += "-" + SPLIT;
            else if (gateway.getOutgoingSequenceFlows().size() == 1 && gateway.getIncomingSequenceFlows().size() > 1)
                type += "-" + JOIN;

            long nodeID = modelManager.createGraphNote(BPMNManager.GATEWAY, type, modelID);
            nodes.put(gateway.get("id").toValue(), nodeID);
        }

        for (SequenceFlow sequenceFlow : sequenceFlows) {
            FlowNode sourceFlowNode = sequenceFlow.getSourceFlowNode();
            FlowNode targetFlowNode = sequenceFlow.getTargetFlowNode();

            String sName = sourceFlowNode.get("id").toValue();
            String tName = targetFlowNode.get("id").toValue();

            long sourceID = nodes.get(sourceFlowNode.get("id").toValue());
            long targetID = nodes.get(targetFlowNode.get("id").toValue());

            GraphNode sourceItem = (GraphNode) modelManager.getModel(modelID).getGraphItemWithID(sourceID);
            GraphNode targetItem = (GraphNode) modelManager.getModel(modelID).getGraphItemWithID(targetID);

            long edgeID = modelManager.createGraphEdge(sourceItem, targetItem, modelID);

//            GraphEdge graphEdge = new GraphEdge().setSource(sourceItem).setTarget(targetItem);
//            modelManager.getModel(modelID).addToGraphItems(graphEdge);
        }

        de.uniks.vs.evolution.graphmodels.GraphModel model = modelManager.getModel(modelID);
        model.withType(de.uniks.vs.evolution.graphmodels.GraphModel.Type.PG);
        
        modelManager.setLastModelID(lastModelID);

        setDrawOrder(modelManager);

        return modelManager;
    }

    private void setDrawOrder(ModelManager modelManager) {
        de.uniks.vs.evolution.graphmodels.GraphModel model = modelManager.getLastModel();
        ArrayList<GraphNode> nodes = model.getNodes();

        for (GraphNode graphNode: nodes) {
            String name = graphNode.getName();
        }

        GraphNode startNode = getGraphNode("startevent_", nodes);
        recursiveOrder(startNode, 0);

    }

    private void recursiveOrder(GraphNode startNode, int order) {
        startNode.setDrawOrder(order);

        order+=2;
        for (GraphEdge graphEdge:startNode.getOutEdges()) {
            GraphNode target = graphEdge.getTarget();
            target.setDrawOrder(order);

            recursiveOrder(target, order +2);
        }
    }

    private GraphNode getGraphNode(String string, ArrayList<GraphNode> nodes) {

        for (GraphNode graphNode: nodes) {

            if (graphNode.getName().startsWith(string))
                return graphNode;
        }
        return null;
    }


    public Graph loadBPMNGraph(Path path) {
        Path bpmnImagePath = path.getParent().resolve(path.getFileName().toString() + ".png");
        System.out.println("creating bpmn image for path " + path + " at " + bpmnImagePath);
        String bpmnxmlfile = path.toString();

        DocumentBuilder parser = null;
        try {
            parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = parser.parse(new File(bpmnxmlfile));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            return  null;
        } catch (IOException e) {
            return null;
        }

        Graph graph = new Graph(new GraphModel());
        BPMNCodec codec = new BPMNCodec(graph);
        codec.decode(bpmnxmlfile);
        return graph;
    }

    private GraphComponent loadBPMNGraphComponent(Path path) {
        Path bpmnImagePath = path.getParent().resolve(path.getFileName().toString() + ".png");
        System.out.println("creating bpmn image for path " + path + " at " + bpmnImagePath);
        String bpmnxmlfile = path.toString();
        Graph graph = new Graph(new GraphModel());
        GraphComponent graphComponent = new GraphComponent(graph);
        BPMNCodec codec = new BPMNCodec(graph);
        codec.decode(bpmnxmlfile);
        return graphComponent;
    }


// ---------------------------------------------------------------------------------------------------------------------

    public static void createBPMNImage(Path path) {
        Path bpmnImagePath = path.getParent().resolve(path.getFileName().toString() + ".png");
        System.out.println("creating bpmn image for path " + path + " at " + bpmnImagePath);

        String bpmnxmlfile = path.toString();

        Graph graph = new Graph(new GraphModel());
        GraphComponent graphComponent = new GraphComponent(graph);

        BPMNCodec codec = new BPMNCodec(graph);
        codec.decode(bpmnxmlfile);

//        List<BPMNModelParsingErrors.ErrorMessage> errorMessages = codec.decode(bpmnxmlfile);
//        if (errorMessages.size() > 0) {
//            return;
//        }

        BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, null, graphComponent.isAntiAlias(), null, true, graphComponent.getCanvas());

        File outputfile = bpmnImagePath.toFile();
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLastModelID() {
        return lastModelID;
    }

    public void setLastModelID(String modelID) {
        this.lastModelID = modelID;
    }
}
