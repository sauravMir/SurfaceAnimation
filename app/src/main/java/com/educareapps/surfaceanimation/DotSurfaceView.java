package com.educareapps.surfaceanimation;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Rakib on 4/13/2017.
 */

public class DotSurfaceView extends SurfaceView {
    private SurfaceHolder surfaceHolder;
    private SurfaceThread surfaceThread;
    public static volatile boolean running = false;

    //******** animation variable  *******
    private Path animPath;
    private PathMeasure pathMeasure;
    private float pathLength;
    int Speed;            //distance each step
    float distance;        //distance moved
    float[] pos;
    float[] tan;
    Matrix matrix;
    private float bm_offsetX;
    private float bm_offsetY;
    //******** animation variable  end here*******


    public DotSurfaceView(Context context) {
        super(context);
        surfaceThread = new SurfaceThread(DotSurfaceView.this);
    }

    public DotSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceThread = new SurfaceThread(DotSurfaceView.this);

    }

    public DotSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceThread = new SurfaceThread(DotSurfaceView.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DotSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        surfaceThread = new SurfaceThread(DotSurfaceView.this);

    }
    //////////////////////*****************  THREAD INITIALIZATION START HERE ****************///////////////////////////

    public void init() {

        surfaceThread = new SurfaceThread(this);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(callback);
        setZOrderOnTop(true);    // necessary
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

    }
    //////////////////////*****************  THREAD INITIALIZATION END HERE ****************///////////////////////////

    //////////////////////*****************  SURFACE CALLBACK START HERE ****************///////////////////////////

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d("create", "create");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            running = false;
        }
    };

    //////////////////////*****************  SURFACE CALLBACK END HERE ****************///////////////////////////
/// main draw method
    public void drawEveryThing(Canvas canvas) {
        drawAllCircles(canvas);
        startAnimation(canvas);

    }

    /// draw all circles
    private void drawAllCircles(Canvas canvas) {
        for (int i = 0; i < circleList.size(); i++) {
            CircleModel circle = circleList.get(i);
            canvas.drawCircle(circle.getLeft(), circle.getTop(), circle.getRadius(), getPaint(circle.getSolidColor(), circle.getStrokeColor(), circle.isStroke()));

        }
    }

    /// animation method
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startAnimation(Canvas canvas) {
        if(counter==(final_size-1))
        dotAnimation(canvas, circleList.get(counter).getLeft(), circleList.get(counter).getTop(),
                circleList.get(0).getLeft(), circleList.get(0).getTop());
        else
            dotAnimation(canvas, circleList.get(counter).getLeft(), circleList.get(counter).getTop(),
                    circleList.get(counter+1).getLeft(), circleList.get(counter+1).getTop());

    }


    // method that animate the avatar
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void dotAnimation(Canvas canvas, float fromX, float fromY, float toX, float toY) {

        animPath = new Path();
        animPath.moveTo(fromX, fromY);
        animPath.lineTo(toX, toY);
        animPath.close();

        pathMeasure = new PathMeasure(animPath, false);
        pathLength = pathMeasure.getLength(); /// animation total path
        Log.e("Called", "flyStarAnimation");
        if (distance < pathLength / 2) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            drawAllCircles(canvas);
            pathMeasure.getPosTan(distance, pos, tan);

            matrix.reset();
            float degrees = (float) (Math.atan2(tan[1], tan[0]) * 0.0 / Math.PI);
            matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
            matrix.postTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);
            canvas.drawBitmap(bitmap, matrix, null);
            distance += Speed;

        } else {
            if(counter<final_size-1){
                counter++;
            }else{
                counter=0;
            }
            distance = 0;
            //running = false; // this shit handled the thread weather run or stop
            Log.e("asd","here");
        }


    }


    private Paint getPaint(int solidColor, int strokeColor, boolean stroke) {
        Paint paint = new Paint();
        if (stroke) {
            paint.setAntiAlias(true);
            paint.setStrokeWidth(6);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(strokeColor);
        } else {
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(solidColor);
        }
        return paint;
    }

    //////////////////////*****************  PROGRESS SET METHOD ARE START HERE ****************///////////////////////////
    private ArrayList<CircleModel> circleList;
    private Bitmap bitmap;

    int counter=0;
    int final_size=0;
    public void initDot(ArrayList<CircleModel> circleList, Bitmap bitmap, int finalDestination) {
        init();
        this.circleList = circleList;
        this.bitmap = bitmap;
        bm_offsetX = bitmap.getWidth() / 2;
        bm_offsetY = bitmap.getHeight() / 2;
        Speed = 6;  /// initial circle move step
        distance = 0;
        pos = new float[2];
        tan = new float[2];
        matrix = new Matrix();
        final_size=finalDestination;
        startTheThread();
    }

    /// thread control method
    void startTheThread() {
        if (surfaceThread != null) {
            running = true;
            surfaceThread.start();
        }
    }


    ///Calculating the threashold and lessening the screen
    int xpaddingThreasHold=50;
    int ypaddingThreasHold=50;
    public void drawBorderPadding(int width, int height){

    }

}
