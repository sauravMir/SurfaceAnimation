package com.educareapps.surfaceanimation;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Rakib on 4/13/2017.
 */

public class SurfaceThread extends Thread {
    DotSurfaceView dotSurfaceView;


    public SurfaceThread(DotSurfaceView dotSurfaceView) {
        this.dotSurfaceView = dotSurfaceView;
    }

    public static void setRunning(boolean runnings) {
        DotSurfaceView.running = runnings;
    }

    @Override
    public void run() {
        Log.e("running: ", String.valueOf(DotSurfaceView.running));
        while (DotSurfaceView.running) {

            Log.e("running to2: ", String.valueOf(DotSurfaceView.running));
            Canvas canvas = dotSurfaceView.getHolder().lockCanvas();
            if (canvas != null) {
                synchronized (dotSurfaceView.getHolder()) {
                    Log.e("from draw Thread: ", "from thread draw method");
                    dotSurfaceView.drawEveryThing(canvas);
                }
                dotSurfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
