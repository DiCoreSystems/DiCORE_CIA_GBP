package de.uniks.vs.evolution.tests.graph.analysis;

import de.uniks.vs.evolution.graph.analysis.manager.BPMNManager;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by alex on 16/6/10.
 */
public class BPMNManagerTest {

    @Test
    public void testConvertToBPMNTest() {
        Path folder = Paths.get(".");

        if(!Files.isDirectory(folder)) {
            System.out.println(folder + " is no folder");
            return;
        }

        try {
            Files.walk(folder).filter((p) -> p.toString().endsWith(".bpmn.20.xml") /*||p.toString().endsWith(".bpmn")*/ ||p.toString().endsWith(".bpmn2")).forEach(BPMNManager::createBPMNImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}