package com.educareapps.surfaceanimation;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private Paint paint;

    public static volatile boolean running = false;

    //******** animation variable  *******
    private Path animPath;
    private PathMeasure pathMeasure;
    private float pathLength;
    int step;            //distance each step
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
    //////////////////////*****************  PAINT INITIALIZATION END HERE ****************///////////////////////////

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

    public void drawEveryThing(Canvas canvas) {
        for (int i = 0; i < circleList.size(); i++) {
            CircleModel circle = circleList.get(i);
            canvas.drawCircle(circle.getLeft(), circle.getTop(), circle.getRadius(), getPaint(circle.getSolidColor(), circle.getStrokeColor(), circle.isStroke()));

        }
        fillPoints(canvas);

    }


    private ArrayList<CircleModel> startPoint;
    private ArrayList<CircleModel> endPoint;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void fillPoints(Canvas canvas) {
        startPoint = new ArrayList<>();
        endPoint = new ArrayList<>();
        for (int i = 0; i < circleList.size(); i = i + 2) {
            CircleModel circleModel = circleList.get(i);
            startPoint.add(circleModel);
        }
        for (int j = 1; j < circleList.size(); j = j + 2) {
            CircleModel circleModel = circleList.get(j);
            endPoint.add(circleModel);
        }

        AnimPoints animPoints = new AnimPoints(startPoint, endPoint);
        for (int k = 0; k < animPoints.getStartPoint().size(); k++) {
            dotAnimation(canvas, animPoints.getStartPoint().get(k).getLeft(), animPoints.getStartPoint().get(k).getTop(), animPoints.getEndPoint().get(k).getLeft(), animPoints.getEndPoint().get(k).getTop());
        }


    }


    // method that animate the avatar
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void dotAnimation(Canvas canvas, float fromX, float fromY, float toX, float toY) {

        animPath = new Path();
        animPath.moveTo(fromX, fromY);
        animPath.lineTo(toX, toY);
        animPath.close();

        pathMeasure = new PathMeasure(animPath, false);
        pathLength = pathMeasure.getLength();
//        canvas.drawPath(animPath, getPaint(Color.RED, Color.WHITE, false));
        Log.e("Called", "flyStarAnimation");
        if (distance < pathLength/2) {
//            canvas.drawColor(0, PorterDuff.Mode.LIGHTEN);
            pathMeasure.getPosTan(distance, pos, tan);

            matrix.reset();
            float degrees = (float) (Math.atan2(tan[1], tan[0]) * 0.0 / Math.PI);
            matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
            matrix.postTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);
            canvas.drawBitmap(bitmap, matrix, null);
            distance += step;

        } else {
            distance = 0;
            running = false;
        }


    }

/*
    Paint getDashedLinePaint() {
        Paint mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
        return mPaint;
    }*/

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

    public void initDot(ArrayList<CircleModel> circleList, Bitmap bitmap) {
        init();
        this.circleList = circleList;
        this.bitmap = bitmap;
        bm_offsetX = bitmap.getWidth() / 2;
        bm_offsetY = bitmap.getHeight() / 2;
        step = 5;
        distance = 0;
        pos = new float[2];
        tan = new float[2];
        matrix = new Matrix();
        startTheThread();
    }

    void startTheThread() {
        if (surfaceThread != null) {
            running = true;
            surfaceThread.start();
        }
    }

    //////////////////////*****************  BITMAP RESIZED METHOD ARE START HERE ****************///////////////////////////
    /*  image resize and get circular method*/
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.e("Progress: ", String.valueOf(height) + "," + String.valueOf(width));

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Log.e("New Progress: ", String.valueOf(scaleHeight) + "," + String.valueOf(scaleWidth));
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width <= 1 ? 10 : width, height <= 1 ? 10 : height, matrix, false);
        //  bm.recycle();
        bm = null;
        return resizedBitmap;
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    //////////////////////*****************  BITMAP RESIZED METHOD ARE END HERE ****************///////////////////////////

}
