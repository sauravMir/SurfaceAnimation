package com.educareapps.surfaceanimation;

import java.util.ArrayList;

/**
 * Created by Rakib on 4/16/2017.
 */

public class AnimPoints {
    private ArrayList<CircleModel> startPoint;
    private ArrayList<CircleModel> endPoint;

    public AnimPoints(ArrayList<CircleModel> startPoint, ArrayList<CircleModel> endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public ArrayList<CircleModel> getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(ArrayList<CircleModel> startPoint) {
        this.startPoint = startPoint;
    }

    public ArrayList<CircleModel> getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(ArrayList<CircleModel> endPoint) {
        this.endPoint = endPoint;
    }
}
