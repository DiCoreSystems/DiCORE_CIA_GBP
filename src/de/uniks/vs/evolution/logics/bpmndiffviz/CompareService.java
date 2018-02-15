//package de.uniks.vs.evolution.logics.bpmndiffviz;
//
//import com.google.common.base.Preconditions;
//import org.camunda.bpm.model.bpmn.Bpmn;
//import org.camunda.bpm.model.bpmn.BpmnModelInstance;
//import org.camunda.bpm.model.bpmn.instance.*;
//import org.camunda.bpm.model.bpmn.instance.Process;
//import org.camunda.bpm.model.xml.instance.ModelElementInstance;
//import org.springframework.context.annotation.Scope;
//import org.springframework.context.annotation.ScopedProxyMode;
//import org.springframework.stereotype.Service;
//import ru.pais.vkr.comparator.config.BPMNComparator;
//import ru.pais.vkr.comparator.entities.ComparisonResult;
//import ru.pais.vkr.comparator.entities.CostConfig;
//import ru.pais.vkr.comparator.entities.MatchingPair;
//import ru.pais.vkr.comparator.entities.PairType;
//import ru.pais.vkr.utils.BPMNUtils;
//import ru.pais.vkr.utils.Utils;
//
//import java.io.File;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * Created by alex on 16/6/9.
// */
//
//public class CompareService {
//
//    private int recCounter = 0;
//    private CostConfig costConfig;
//    private int counter = 0;
//
//    public static List<Class> BPMN_COMPARING_TYPES = new ArrayList<Class>() {
//        {
//            //Containers
//            add(Process.class);
//            add(Lane.class);
//            add(SubProcess.class);
//
//            add(Participant.class);
//            add(StartEvent.class);
//            add(Task.class);
//            add(ExclusiveGateway.class);
//            add(ParallelGateway.class);
//            add(InclusiveGateway.class);
//            add(EventBasedGateway.class);
//            add(ComplexGateway.class);
//            add(EndEvent.class);
//
////            add(LaneSet.class);
////            add(Definitions.class);
////            add(ErrorEventDefinition.class);
////            add(EscalationEventDefinition.class);
////            add(Documentation.class);
////            add(Operation.class);
////            add(Artifact.class);
////            add(Activity.class);
////            add(ThrowEvent.class);
////            add(DataOutput.class);
////            add(DataInput.class);
////            add(ResourceRole.class);
////            add(CallableElement.class);
////            add(DataAssociation.class);
////            add(Collaboration.class);
////            add(HumanPerformer.class);
////            add(Expression.class);
////            add(FormalExpression.class);
////            add(Performer.class);
////            add(ResourceAssignmentExpression.class);
////            add(InputDataItem.class);
////            add(CorrelationSubscription.class);
////            add(ConversationAssociation.class);
////            add(Property.class);
////            add(CategoryValue.class);
////            add(Import.class);
////            add(CallConversation.class);
////            add(SendTask.class);
////            add(Resource.class);
////            add(Conversation.class);
////            add(Text.class);
////            add(ServiceTask.class);
////            add(TimeCycle.class);
////            add(ActivationCondition.class);
////            add(CompensateEventDefinition.class);
////            add(DataInputAssociation.class);
////            add(DataOutputAssociation.class);
////            add(CancelEventDefinition.class);
////            add(ResourceParameterBinding.class);
////            add(DataObjectReference.class);
////            add(UserTask.class);
////            add(ConditionExpression.class);
////            add(ExtensionElements.class);
////            add(CorrelationPropertyBinding.class);
////            add(ResourceParameter.class);
////            add(ComplexBehaviorDefinition.class);
////            add(Assignment.class);
////            add(PotentialOwner.class);
////            add(DataObject.class);
////            add(TimeDuration.class);
////            add(ReceiveTask.class);
////            add(CorrelationPropertyRetrievalExpression.class);
////            add(OutputSet.class);
////            add(ItemDefinition.class);
////            add(Monitoring.class);
////            add(TimeDate.class);
////            add(MessageEventDefinition.class);
////            add(MultiInstanceLoopCharacteristics.class);
////            add(Escalation.class);
////            add(ConditionalEventDefinition.class);
////            add(CorrelationKey.class);
////            add(IntermediateCatchEvent.class);
////            add(Rendering.class);
////            add(Message.class);
////            add(Auditing.class);
////            add(ParticipantMultiplicity.class);
////            add(IoBinding.class);
////            add(Condition.class);
////            add(Signal.class);
////            add(GlobalConversation.class);
////            add(SubConversation.class);
////            add(Error.class);
////            add(BusinessRuleTask.class);
////            add(LoopCardinality.class);
////            add(Interface.class);
////            add(CorrelationProperty.class);
////            add(OutputDataItem.class);
////            add(LinkEventDefinition.class);
////            add(Script.class);
////            add(SignalEventDefinition.class);
////            add(InputSet.class);
////            add(BoundaryEvent.class);
////            add(CallActivity.class);
////            add(Relationship.class);
////            add(EndPoint.class);
////            add(CompletionCondition.class);
////            add(TerminateEventDefinition.class);
////            add(IntermediateThrowEvent.class);
////            add(DataState.class);
////            add(Extension.class);
////            add(ManualTask.class);
////            add(TextAnnotation.class);
////            add(TimerEventDefinition.class);
////            add(IoSpecification.class);
////            add(ParticipantAssociation.class);
////            add(ScriptTask.class);
////            add(MessageFlowAssociation.class);
////
//            //With source and target elements
//            add(Association.class);
//            add(ConversationLink.class);
//            add(MessageFlow.class);
//            add(Flow.class);
//        }
//    };
//
//    private PriorityQueue<ComparisonResult> comparisonResultQueue;
//    private List<Class> modelMatchingElementTypes;
//    private List<Class> modelElementTypes;
//
//    //Constructor
//    public CompareService() {
//        modelMatchingElementTypes = new ArrayList<Class>();
//        modelElementTypes = new ArrayList<Class>();
//        comparisonResultQueue = new PriorityQueue<ComparisonResult>();
//    }
//
//    /**
//     * @param fileName1 file name of the first model
//     * @param fileName2 file name of the second model
//     * @return list of differences
//     */
//    public ComparisonResult compareModels(String fileName1, String fileName2, CostConfig costConfig) throws Exception {
//        this.costConfig = costConfig;
//        return compareModels(Bpmn.readModelFromFile(new File(fileName1)), Bpmn.readModelFromFile(new File(fileName2)));
//    }
//
//    /**
//     * @param bpmnModelInstance1 the first model
//     * @param bpmnModelInstance2 the second model
//     * @return list of differences
//     */
//    public ComparisonResult compareModels(BpmnModelInstance bpmnModelInstance1,
//                                          BpmnModelInstance bpmnModelInstance2) throws Exception {
//
//        //Generate element types
//        generateModelElementTypes(bpmnModelInstance1, bpmnModelInstance2);
//
//        //Compare elements
//        Collection<ModelElementInstance> modelElementInstances2 = getModelElementsByType(bpmnModelInstance2, modelMatchingElementTypes.get(0));
//
//        ModelElementInstance modelElementInstance1 = (ModelElementInstance) getModelElementsByType(bpmnModelInstance1, modelMatchingElementTypes.get(0)).toArray()[0];
//
//        //Find correspondences
//        for (ModelElementInstance modelElementInstance2 : modelElementInstances2) {
//            ComparisonResult comparisonResult = new ComparisonResult(bpmnModelInstance1, bpmnModelInstance2, this.modelMatchingElementTypes, this.modelElementTypes, costConfig);
//            BPMNComparator bpmnComparator = costConfig.getValidComparator(modelMatchingElementTypes.get(0));
//            MatchingPair matchingPair = bpmnComparator.compare(
//                    comparisonResult,
//                    bpmnModelInstance1,
//                    bpmnModelInstance2,
//                    modelElementInstance1,
//                    modelElementInstance2);
//            matchingPair.setType(PairType.MATCH);
//            if (BPMNUtils.isFlowElement(modelMatchingElementTypes.get(0))) {
//                if (matchingPair.isLinkSave()) {
//                    comparisonResult.updateMatching(matchingPair);
//                    comparisonResultQueue.add(comparisonResult);
//                }
//            } else {
//                comparisonResult.updateMatching(matchingPair);
//                comparisonResultQueue.add(comparisonResult);
//            }
//        }
//
//        //Delete this element
//        ComparisonResult comparisonResult = new ComparisonResult(bpmnModelInstance1, bpmnModelInstance2, this.modelMatchingElementTypes, this.modelElementTypes, costConfig);
//        double d = comparisonResult.getCostConfig().getDeleteCostByType(modelElementInstance1.getElementType());
//        MatchingPair matchingPair = new MatchingPair(modelElementInstance1, null, PairType.DELETE);
//        matchingPair.setDifferentLabel(d);
//        comparisonResult.updateDeleting(matchingPair);
//        comparisonResultQueue.add(comparisonResult);
//
//        return compareElementsRecursive(comparisonResultQueue.peek(), bpmnModelInstance1, bpmnModelInstance2);
//    }
//
//    public ComparisonResult compareElementsRecursive(ComparisonResult comparisonResult,
//                                                     BpmnModelInstance bpmnModelInstance1,
//                                                     BpmnModelInstance bpmnModelInstance2) throws Exception {
//        recCounter++;
//        comparisonResult.setRecCounter(comparisonResult.getRecCounter() + 1);
//        if (comparisonResult.getComparingClassList().isEmpty()) {
//            //Add elements from the second model, that were not matching with elements from the first model
//            //Remove the extra classes
//            Optional<ModelElementInstance> modelElementInstanceInner1 = null;
//            Optional<ModelElementInstance> modelElementInstanceInner2 = null;
//
//            for (Iterator<Class> iterator = comparisonResult.getAddedClassList().iterator(); iterator.hasNext(); ) {
//                Class aClass1 = iterator.next();
//                Collection<ModelElementInstance> collection1 = getModelElementsByType(bpmnModelInstance1, aClass1);
//                Collection<ModelElementInstance> collection2 = getModelElementsByType(bpmnModelInstance2, aClass1);
//
//                modelElementInstanceInner1 = collection1.stream().filter(x -> !comparisonResult.containsInKeysDeletedOrKeysAddedOrKeysMatchingSets(x)).findFirst();
//                modelElementInstanceInner2 = collection2.stream().filter(x -> !comparisonResult.containsInKeysDeletedOrKeysAddedOrValuesMatchingSets(x)).findFirst();
//
//                if (!modelElementInstanceInner1.isPresent() && !modelElementInstanceInner2.isPresent()) {
//                    iterator.remove();
//                } else {
//                    break;
//                }
//            }
//            //All elements are matched, deleted or added
//            if (comparisonResult.getAddedClassList().isEmpty()) {
//                if (comparisonResult == comparisonResultQueue.peek()) {
//                    return comparisonResult;
//                } else {
//                    return compareElementsRecursive(comparisonResultQueue.peek(), bpmnModelInstance1, bpmnModelInstance2);
//                }
//            }
//
//            Preconditions.checkArgument(!(modelElementInstanceInner1.isPresent() && modelElementInstanceInner2.isPresent()));
//
//            if (modelElementInstanceInner1.isPresent()) {
//                //Delete this element
//                ComparisonResult comparisonResultNew = new ComparisonResult(comparisonResult, costConfig);
//                double d = comparisonResultNew.getCostConfig().getDeleteCostByType(modelElementInstanceInner1.get().getElementType());
//                MatchingPair matchingPair = new MatchingPair(modelElementInstanceInner1.get(), null, PairType.DELETE);
//                matchingPair.setDifferentLabel(d);
//                comparisonResultNew.updateDeleting(matchingPair);
//                comparisonResultNew.updateHeuristic();
//
//                comparisonResultQueue.remove(comparisonResult);
//                comparisonResultQueue.add(comparisonResultNew);
//
//                return compareElementsRecursive(comparisonResultQueue.peek(), bpmnModelInstance1, bpmnModelInstance2);
//            } else {
//                //Add this element
//                ComparisonResult comparisonResultNew = new ComparisonResult(comparisonResult, costConfig);
//                double d = comparisonResultNew.getCostConfig().getAddCostByType(modelElementInstanceInner2.get().getElementType());
//                MatchingPair matchingPair = new MatchingPair(modelElementInstanceInner2.get(), null, PairType.INSERT);
//                matchingPair.setDifferentLabel(d);
//                comparisonResultNew.updateAdding(matchingPair);
//                comparisonResultNew.updateHeuristic();
//
//                comparisonResultQueue.remove(comparisonResult);
//                comparisonResultQueue.add(comparisonResultNew);
//
//                return compareElementsRecursive(comparisonResultQueue.peek(), bpmnModelInstance1, bpmnModelInstance2);
//            }
//        }
//        //getComparingClassList is not empty
//        else {
//            Class aClass = comparisonResult.getComparingClassList().get(0);
//            Collection<ModelElementInstance> collection = getModelElementsByType(bpmnModelInstance1, aClass);
//
//            //Delete full compared class
//            if (!collection.stream().filter(modelElementInstance ->
//                    !comparisonResult.containsInKeysDeletedOrKeysAddedOrKeysMatchingSets(modelElementInstance)).findAny().isPresent()) {
//                comparisonResult.removeFromComparingClassList(aClass);
//                //deleteResultsWithHighPenalty(bpmnModelInstance1, comparisonResult, aClass);
//            }
//
//            //Comparing class list is empty
//            if (comparisonResult.getComparingClassList().isEmpty()) {
//                return compareElementsRecursive(comparisonResult, bpmnModelInstance1, bpmnModelInstance2);
//            } else {
//                aClass = comparisonResult.getComparingClassList().get(0);
//            }
//
//            //Compare elements
//            ModelElementInstance modelElementInstance1;
//            Collection<ModelElementInstance> modelElementInstances1 = getModelElementsByType(bpmnModelInstance1, aClass);
//            //Find element that not have been matched
//            modelElementInstance1 = modelElementInstances1.stream().filter(x -> !comparisonResult.containsInKeysDeletedOrKeysAddedOrKeysMatchingSets(x)).findFirst().get();
//
//            Collection<ModelElementInstance> modelElementInstances2 = getModelElementsByType(bpmnModelInstance2, aClass);
//            //Find correspondences
//            for (ModelElementInstance modelElementInstance2 : modelElementInstances2) {
//                if (!comparisonResult.getAllValuesFromMatchingSet().contains(modelElementInstance2)) {
//                    ComparisonResult comparisonResultNew = new ComparisonResult(comparisonResult, costConfig);
//                    MatchingPair matchingPair = costConfig.getValidComparator(aClass).compare(
//                            comparisonResultNew,
//                            bpmnModelInstance1,
//                            bpmnModelInstance2,
//                            modelElementInstance1,
//                            modelElementInstance2);
//                    matchingPair.setType(PairType.MATCH);
//                    if (BPMNUtils.isFlowElement(aClass)) {
//                        if (matchingPair.isLinkSave()) {
//                            comparisonResultNew.updateMatching(matchingPair);
//                            comparisonResultQueue.add(comparisonResultNew);
//                        }
//                    } else {
//                        comparisonResultNew.updateMatching(matchingPair);
//                        comparisonResultQueue.add(comparisonResultNew);
//                    }
//                }
//            }
//
//            //Delete this element
//            ComparisonResult comparisonResultNew = new ComparisonResult(comparisonResult, costConfig);
//            double d = comparisonResultNew.getCostConfig().getDeleteCostByType(modelElementInstance1.getElementType());
//            MatchingPair matchingPair = new MatchingPair(modelElementInstance1, null, PairType.DELETE);
//            matchingPair.setDifferentLabel(d);
//            comparisonResultNew.updateDeleting(matchingPair);
//            comparisonResultNew.updateHeuristic();
//            comparisonResultQueue.add(comparisonResultNew);
//
//            //Delete the exhaust comparisonResult
//            comparisonResultQueue.remove(comparisonResult);
//
//            return compareElementsRecursive(comparisonResultQueue.peek(), bpmnModelInstance1, bpmnModelInstance2);
//        }
//    }
//
//    public void generateModelElementTypes(BpmnModelInstance bpmnModelInstance1, BpmnModelInstance bpmnModelInstance2) {
//        for (Class aClass : BPMN_COMPARING_TYPES) {
//            if (!getModelElementsByType(bpmnModelInstance1, aClass).isEmpty() &&
//                    !getModelElementsByType(bpmnModelInstance2, aClass).isEmpty()) {
//                modelMatchingElementTypes.add(aClass);
//            }
//        }
//
//        for (Class aClass : BPMN_COMPARING_TYPES) {
//            if (!getModelElementsByType(bpmnModelInstance1, aClass).isEmpty() ||
//                    !getModelElementsByType(bpmnModelInstance2, aClass).isEmpty()) {
//                modelElementTypes.add(aClass);
//            }
//        }
//    }
//
//    public int getRecCounter() {
//        return recCounter;
//    }
//
//    public void setRecCounter(int recCounter) {
//        this.recCounter = recCounter;
//    }
//
//    public static Collection<ModelElementInstance> getModelElementsByType(BpmnModelInstance bpmnModelInstance, Class aClass) {
//        Collection<ModelElementInstance> modelElementInstances = bpmnModelInstance.getModelElementsByType(aClass);
//        for (Iterator<ModelElementInstance> iterator = modelElementInstances.iterator(); iterator.hasNext(); ) {
//            ModelElementInstance modelElementInstance = iterator.next();
//            if (!modelElementInstance.getClass().getSimpleName().equals(aClass.getSimpleName().concat("Impl"))) {
//                iterator.remove();
//            }
//        }
//        return modelElementInstances;
//    }
//
//
//}
