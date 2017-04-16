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
        circleModel = new CircleModel (100, 100, 10, Color.BLACK, Color.WHITE, true);
        circleModel1 = new CircleModel(270, 100, 10, Color.GREEN, Color.WHITE, true);

        circleModel2 = new CircleModel(300, 200, 10, Color.MAGENTA, Color.WHITE, true);
        circleModel3 = new CircleModel(470, 200, 10, Color.BLUE, Color.WHITE, true);

        circleModel4 = new CircleModel(500, 300, 10, Color.RED, Color.WHITE, true);
        circleModel5 = new CircleModel(680, 300, 10, Color.GRAY, Color.WHITE, true);

        circleList = new ArrayList<>();
        circleList.add(circleModel);
        circleList.add(circleModel1);
        circleList.add(circleModel2);
        circleList.add(circleModel3);
        circleList.add(circleModel4);
        circleList.add(circleModel5);
        sfDot.initDot(circleList, BitmapFactory.decodeResource(getResources(), R.drawable.circle));


    }
}
