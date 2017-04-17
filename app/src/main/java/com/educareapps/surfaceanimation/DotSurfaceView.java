package com.educareapps.surfaceanimation;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Movie;
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

import java.io.InputStream;
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
    private InputStream gifInputStream;
    private Movie gifMovie;
    private Context context;
    private Bitmap doneBitmap;

    public DotSurfaceView(Context context) {
        super(context);
        this.context = context;
        surfaceThread = new SurfaceThread(DotSurfaceView.this);
    }

    public DotSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        surfaceThread = new SurfaceThread(DotSurfaceView.this);

    }

    public DotSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        surfaceThread = new SurfaceThread(DotSurfaceView.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DotSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;

        surfaceThread = new SurfaceThread(DotSurfaceView.this);

    }
    //////////////////////*****************  THREAD INITIALIZATION START HERE ****************///////////////////////////

    public void init() {

        surfaceThread = new SurfaceThread(this);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(callback);
        setZOrderOnTop(true);    // necessary
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        gifInputStream = context.getResources().openRawResource(R.raw.walk_animation);
        gifMovie = Movie.decodeStream(gifInputStream);

        doneBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.success);


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

    boolean isCircleDrawn = false;

    //////////////////////*****************  SURFACE CALLBACK END HERE ****************///////////////////////////
/// main draw method
    public void drawEveryThing(Canvas canvas) {
        drawAllCircles(canvas);
        drawLabel(canvas);
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
        if (counter == (final_size - 1)) {
            dotAnimation(canvas, circleList.get(counter).getLeft(), circleList.get(counter).getTop(),
                    circleList.get(0).getLeft(), circleList.get(0).getTop());
            ///// draw done bitmap when all path gone by gif
            for (int i = 0; i < circleList.size() - 1; i++) {
                canvas.drawBitmap(doneBitmap, circleList.get(i).getLeft() - doneBitmap.getWidth() / 2,
                        circleList.get(i).getTop() - doneBitmap.getHeight() / 2, getPaint(Color.WHITE, Color.WHITE, false));

            }
            gifMovie.draw(canvas, circleList.get(counter).getLeft() - gifMovie.width() / 2, circleList.get(counter).getTop() - gifMovie.height() / 2);
            running = false;
        } else {
            dotAnimation(canvas, circleList.get(counter).getLeft(), circleList.get(counter).getTop(),
                    circleList.get(counter + 1).getLeft(), circleList.get(counter + 1).getTop());

            ///// draw done bitmap when one at a time  path gone by gif
            for (int i = 0; i < counter + 1; i++) {
//                canvas.drawBitmap(doneBitmap, circleList.get(i).getLeft() - doneBitmap.getWidth() / 2,
//                        circleList.get(i).getTop() - doneBitmap.getHeight() / 2, getPaint(Color.WHITE, Color.WHITE, false));
                gifMovie.draw(canvas, circleList.get(i).getLeft() - gifMovie.width() / 2, circleList.get(i).getTop() - gifMovie.height() / 2);
            }
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
        pathLength = pathMeasure.getLength(); /// animation total path
        Log.e("Called", "flyStarAnimation");
        if (distance < pathLength / 2) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            drawAllCircles(canvas);
            drawLabel(canvas);
            if (counter == (final_size - 1)) {
                //// when the gif is in the last circle
            } else {
                startMovie(canvas, pos[0], pos[1]);
            }
            pathMeasure.getPosTan(distance, pos, tan);

            matrix.reset();
            float degrees = (float) (Math.atan2(tan[1], tan[0]) * 0.0 / Math.PI);
            matrix.postRotate(degrees, bm_offsetX, bm_offsetY);
            matrix.postTranslate(pos[0] - bm_offsetX, pos[1] - bm_offsetY);
//            canvas.drawBitmap(bitmap, matrix, null);

            distance += Speed;

        } else {

            if (counter < final_size - 1) {
                counter++;
            } else {
                counter = 0;
            }
            distance = 0;
            //running = false; // this shit handled the thread weather run or stop
            Log.e("asd", "here");


        }


    }

    private void drawLabel(Canvas canvas) {
        for (int i = 0; i < circleList.size(); i++) {
            CircleModel circle = circleList.get(i);
            canvas.drawText(circle.getCircleText(), circle.getLeft(), circle.getTop(), getPaint(Color.WHITE, circle.getStrokeColor(), false));
        }
    }

    private long mMovieStart;

    void startMovie(Canvas canvas, float pos1, float pos2) {
        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) {   // first time
            mMovieStart = now;
        }

        if (gifMovie != null) {

            int dur = gifMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((now - mMovieStart) % dur);
            gifMovie.setTime(relTime);
            gifMovie.draw(canvas, pos1 - gifMovie.width() / 2, pos2 - gifMovie.height() / 2);

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

    int counter = 0;
    int final_size = 0;

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
        final_size = finalDestination;
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
    int xpaddingThreasHold = 50;
    int ypaddingThreasHold = 50;

    public void drawBorderPadding(int width, int height) {

    }

}
