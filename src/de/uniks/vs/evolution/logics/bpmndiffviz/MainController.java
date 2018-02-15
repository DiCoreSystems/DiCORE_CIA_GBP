//package de.uniks.vs.evolution.logics.bpmndiffviz;
//
//import com.google.common.base.Throwables;
//import org.camunda.bpm.model.bpmn.Bpmn;
//import org.camunda.bpm.model.bpmn.BpmnModelInstance;
//import org.camunda.bpm.model.bpmn.instance.*;
//import org.camunda.bpm.model.bpmn.instance.Process;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.web.servlet.support.RequestContextUtils;
//import ru.pais.vkr.comparator.entities.ComparisonResult;
//import ru.pais.vkr.comparator.entities.CostConfig;
//import ru.pais.vkr.models.Document;
//import ru.pais.vkr.models.Model;
//import ru.pais.vkr.services.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//import java.text.MessageFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created with IntelliJ IDEA.
// * User: Ivanov Sergey
// */
//@Controller
//@RequestMapping(value = "/")
//public class MainController {
//
//    @Autowired
//    ModelService modelService;
//
//    @Autowired
//    CompareService compareService;
//
//    @Autowired
//    DocumentService documentService;
//
//    @Autowired
//    ComparisonResultService comparisonResultService;
//
//    @Autowired
//    MatchingPairService matchingPairService;
//
//    @Autowired
//    ConfigService configService;
//
//    @RequestMapping()
//    public ModelAndView mainPage() {
//        ModelAndView modelAndView = new ModelAndView("main");
//        modelAndView.addObject("nM", String.valueOf(modelService.getAllModels().size()));
//        modelAndView.addObject("nR", String.valueOf(comparisonResultService.getAllResults().size()));
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "models")
//    public ModelAndView models() throws Exception {
//        ModelAndView modelAndView = new ModelAndView("models");
//        modelAndView.addObject(modelService.getAllModels());
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "uploadModel")
//    public ModelAndView uploadModel(HttpServletRequest request, HttpServletResponse response) {
//        ModelAndView modelAndView = new ModelAndView("uploadModel");
//        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
//        Long id = (long) 0;
//        String modelName = null;
//        String message = null;
//        if (inputFlashMap != null) {
//            id = (Long) inputFlashMap.get("id");
//            modelName = (String) inputFlashMap.get("modelName");
//        }
//        if (id == null) {
//            message = "This model can't be uploaded to our server. Please, contact to support.";
//            modelAndView.addObject("message", message);
//        } else {
//            message = "Model was successfully uploaded to our server.";
//            modelAndView.addObject("message", message);
//            modelAndView.addObject("id", id);
//            modelAndView.addObject("modelName", modelName);
//        }
//
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "saveModel")
//    public String saveModel(@RequestParam(value = "name") String name,
//                            @RequestParam(value = "file") MultipartFile file,
//                            RedirectAttributes redirectAttributes) throws IOException {
//        long id = 0;
//        String modelName = null;
//
//        if (!file.isEmpty()) {
//            Document document = documentService.saveFile(file);
//            Model model = new Model();
//            model.setName(name);
//            model.setDocument(document);
//            modelService.saveModel(model);
//            id = model.getId();
//            modelName = model.getName();
//        }
//
//        redirectAttributes.addFlashAttribute("id", id);
//        redirectAttributes.addFlashAttribute("modelName", modelName);
//        return "redirect:/uploadModel";
//    }
//
//    @RequestMapping(value = "model")
//    public ModelAndView saveModel(@RequestParam(value = "id") Long id) {
//        ModelAndView modelAndView = new ModelAndView("model");
//        Model model = modelService.getModelByID(id);
//        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromFile(new File(model.getDocument().getStorage().getPath() + model.getDocument().getSystemName()));
//        modelAndView.addObject("taskCount", CompareService.getModelElementsByType(bpmnModelInstance, Task.class).size());
//        modelAndView.addObject("sequenceFlowCount", CompareService.getModelElementsByType(bpmnModelInstance, Flow.class).size());
//        modelAndView.addObject("startEventCount", CompareService.getModelElementsByType(bpmnModelInstance, StartEvent.class).size());
//        modelAndView.addObject("endEventCount", CompareService.getModelElementsByType(bpmnModelInstance, EndEvent.class).size());
//        modelAndView.addObject("exclusiveGatewayCount", CompareService.getModelElementsByType(bpmnModelInstance, ExclusiveGateway.class).size());
//        modelAndView.addObject("parallelGatewayCount", CompareService.getModelElementsByType(bpmnModelInstance, ParallelGateway.class).size());
//        modelAndView.addObject("processCount", CompareService.getModelElementsByType(bpmnModelInstance, Process.class).size());
//        modelAndView.addObject("messageFlowCount", CompareService.getModelElementsByType(bpmnModelInstance, MessageFlow.class).size());
//        modelAndView.addObject("model", model);
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "comparison/first_step")
//    public ModelAndView comparisonFirstStep() {
//        ModelAndView modelAndView = new ModelAndView("comparison1");
//        modelAndView.addObject(modelService.getAllModels());
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "results")
//    public ModelAndView results() throws Exception {
//        ModelAndView modelAndView = new ModelAndView("results");
//        List<ComparisonResult> comparisonResults = comparisonResultService.getAllResults();
//        for (ComparisonResult result : comparisonResults) {
//            result.setFirstName(modelService.getModelByID(result.getFirstID()).getName());
//            result.setSecondName(modelService.getModelByID(result.getSecondID()).getName());
//        }
//        modelAndView.addObject("results", comparisonResults);
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "result")
//    public ModelAndView result(HttpServletRequest request,
//                               HttpServletResponse response,
//                               @RequestParam(value = "id") Long id) throws IllegalAccessException {
//        ModelAndView modelAndView = new ModelAndView("result");
//        ComparisonResult comparisonResult = comparisonResultService.getResultByID(id);
//        configService.updateConfigs(comparisonResult);
//        matchingPairService.updateMatchingPairsDelete(comparisonResult);
//        matchingPairService.updateMatchingPairsInsert(comparisonResult);
//        matchingPairService.updateMatchingPairsMatch(comparisonResult);
//        comparisonResult.setFirstName(modelService.getModelByID(comparisonResult.getFirstID()).getName());
//        comparisonResult.setSecondName(modelService.getModelByID(comparisonResult.getSecondID()).getName());
//        modelAndView.addObject("result", comparisonResult);
//
//        int sizeAll = comparisonResult.getMatchingSet().size() + comparisonResult.getDeletedSet().size() + comparisonResult.getAddedSet().size();
//        long percent2 = Math.round(((double) comparisonResult.getMatchingSet().size() / sizeAll) * 100);
//        modelAndView.addObject("percent2", percent2);
//        long percent3 = Math.round(((double) comparisonResult.getDeletedSet().size() / sizeAll) * 100);
//        modelAndView.addObject("percent3", percent3);
//        modelAndView.addObject("percent4", 100 - percent2 - percent3);
//
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "comparison/second_step")
//    public ModelAndView comparisonSecondStep(HttpServletRequest request,
//                                             HttpServletResponse response,
//                                             @RequestParam(value = "firstModelID") Long id) {
//        ModelAndView modelAndView = new ModelAndView("comparison2");
//        modelAndView.addObject(modelService.getAllModels());
//        modelAndView.addObject("firstModel", modelService.getModelByID(id));
//        CostConfig costConfig = new CostConfig();
//        request.getSession().setAttribute("costConfig", costConfig);
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "comparison/third_step")
//    public ModelAndView comparisonThirdStep(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            @RequestParam(value = "firstModelID") Long id1,
//                                            @RequestParam(value = "secondModelID") Long id2) throws IllegalAccessException {
//        ModelAndView modelAndView = new ModelAndView("comparison3");
//        modelAndView.addObject(modelService.getAllModels());
//        modelAndView.addObject("firstModel", modelService.getModelByID(id1));
//        modelAndView.addObject("secondModel", modelService.getModelByID(id2));
//        CostConfig costConfig = (CostConfig) request.getSession().getAttribute("costConfig");
//        modelAndView.addObject("configCost", costConfig);
//        modelAndView.addObject("configs", costConfig.getAllConfigs());
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "saveConfig", method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public String saveModel(HttpServletRequest request,
//                            HttpServletResponse response,
//                            @RequestParam(value = "delete") Double delete,
//                            @RequestParam(value = "insert") Double insert,
//                            @RequestParam(value = "diff") Double diff,
//                            @RequestParam(value = "config") String config) throws IOException, NoSuchFieldException, IllegalAccessException {
//        CostConfig costConfig = (CostConfig) request.getSession().getAttribute("costConfig");
//        String configName = config.replaceFirst("ru.pais.vkr.comparator.config.", "").replaceFirst("Config.*", "");
//        costConfig.setCosts(configName, delete, insert, diff);
//        return configName;
//    }
//
//    @RequestMapping(value = "comparison/fourth_step")
//    public ModelAndView comparisonFourthStep(HttpServletRequest request,
//                                             HttpServletResponse response,
//                                             @RequestParam(value = "firstModelID") Long id1,
//                                             @RequestParam(value = "secondModelID") Long id2,
//                                             @RequestParam(value = "Ldistance", required = false) Double Ldistance) throws Exception {
//        CostConfig costConfig = (CostConfig) request.getSession().getAttribute("costConfig");
//        costConfig.setFactorLevenshtein(Ldistance);
//        ModelAndView modelAndView = new ModelAndView("comparison4");
//        Model model1 = modelService.getModelByID(id1);
//        String file1 = model1.getDocument().getStorage().getPath() + model1.getDocument().getSystemName();
//        Model model2 = modelService.getModelByID(id2);
//        String file2 = model2.getDocument().getStorage().getPath() + model2.getDocument().getSystemName();
//        modelAndView.addObject("firstModel", model1);
//        modelAndView.addObject("secondModel", model2);
//        ComparisonResult comparisonResult = compareService.compareModels(file1, file2, costConfig);
//        comparisonResult.setFactorLevenshtein(Ldistance);
//        comparisonResult.setDate(new Date());
//        comparisonResult.setFirstID(model1.getId());
//        comparisonResult.setSecondID(model2.getId());
//
//        comparisonResultService.saveResult(comparisonResult);
//        matchingPairService.saveMatchingPairs(comparisonResult);
//        configService.saveConfigs(comparisonResult);
//
//        modelAndView.addObject("result", comparisonResult);
//        int sizeAll = comparisonResult.getMatchingSet().size() + comparisonResult.getDeletedSet().size() + comparisonResult.getAddedSet().size();
//        long percent2 = Math.round(((double) comparisonResult.getMatchingSet().size() / sizeAll) * 100);
//        modelAndView.addObject("percent2", percent2);
//        long percent3 = Math.round(((double) comparisonResult.getDeletedSet().size() / sizeAll) * 100);
//        modelAndView.addObject("percent3", percent3);
//        modelAndView.addObject("percent4", 100 - percent2 - percent3);
//        modelAndView.addObject("recCounter", compareService.getRecCounter());
//        return modelAndView;
//    }
//
//}