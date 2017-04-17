package com.educareapps.surfaceanimation;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DotSurfaceView sfDot;
    private ArrayList<CircleModel> circleList;
    CircleModel circleModel;
    CircleModel circleModel1;
    CircleModel circleModel2;
    CircleModel circleModel3;
    CircleModel circleModel4;
    CircleModel circleModel5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sfDot = (DotSurfaceView) findViewById(R.id.sfDot);
        fiilDummyData();

    }

    private void fiilDummyData() {
        circleModel = new CircleModel(100, 100, 30, Color.BLACK, Color.WHITE, true, "1");
        circleModel1 = new CircleModel(270, 100, 30, Color.GREEN, Color.WHITE, true, "2");

        circleModel2 = new CircleModel(440, 100, 30, Color.MAGENTA, Color.WHITE, true, "3");
        circleModel3 = new CircleModel(270, 200, 30, Color.BLUE, Color.WHITE, true, "4");

        circleModel4 = new CircleModel(100, 300, 30, Color.RED, Color.WHITE, true, "5");
        circleModel5 = new CircleModel(270, 300, 30, Color.GRAY, Color.WHITE, true, "6");

        circleList = new ArrayList<>();
        circleList.add(circleModel);
        circleList.add(circleModel1);
        circleList.add(circleModel2);
        circleList.add(circleModel3);
        circleList.add(circleModel4);
        circleList.add(circleModel5);
        sfDot.initDot(circleList, BitmapFactory.decodeResource(getResources(), R.drawable.circle), 6);

    }


}
