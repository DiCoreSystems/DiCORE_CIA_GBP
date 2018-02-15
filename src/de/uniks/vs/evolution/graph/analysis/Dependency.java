package de.uniks.vs.evolution.graph.analysis;

/**
 * Created by alex on 16/8/3.
 */
public class Dependency extends Tuple<String, String> {
    enum Type {
        DATA_DEPENDENCY,
        SYNC_DEPENDENCY,
        PARALLEL_DEPENDENCY,
        COMMON_CONRTOL_DEPENDENCY
    }

    public Type type;

    public Dependency(String x, String y) {
        super(x, y);
    }

    public Dependency(String x, String y, Type type) {
        super(x, y);
        this.type = type;
    }
}