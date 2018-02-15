package de.uniks.vs.evolution.graphmodels;

import javafx.scene.paint.Color;

public class VisualProperties {
    public Color color = Color.GRAY;
    public Type type = Type.STANDARD;
    public Status status = Status.UNCHANGED;
    public String label = null;

    public enum Type { STANDARD, ROUND }
    public enum Status { NEW, DELETED, REMOVED, UNCHANGED, CHANGED }
}
