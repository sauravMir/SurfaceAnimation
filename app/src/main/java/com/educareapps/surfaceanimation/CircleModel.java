package com.educareapps.surfaceanimation;

/**
 * Created by Rakib on 4/13/2017.
 */

public class CircleModel {
    private float left;
    private float top;
    private float radius;
    private int solidColor;
    private int strokeColor;
    private boolean isStroke;
    private String circleText;

    public CircleModel(float left, float top, float radius, int solidColor, int strokeColor, boolean isStroke, String circleText) {
        this.left = left;
        this.top = top;
        this.radius = radius;
        this.solidColor = solidColor;
        this.strokeColor = strokeColor;
        this.isStroke = isStroke;
        this.circleText = circleText;
    }

    public CircleModel() {
    }

    public String getCircleText() {
        return circleText;
    }

    public void setCircleText(String circleText) {
        this.circleText = circleText;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getSolidColor() {
        return solidColor;
    }

    public void setSolidColor(int solidColor) {
        this.solidColor = solidColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public boolean isStroke() {
        return isStroke;
    }

    public void setStroke(boolean stroke) {
        isStroke = stroke;
    }
}
