package cmsc434.ia08;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;

import static java.lang.Math.max;

public class DoodleView extends View {

    private Paint mPaintDoodle = new Paint();
    private Path mPath = new Path();
    private PaintAction mPaintAction = new PaintAction();
    private Canvas mCanvas = new Canvas();
    private VelocityTracker mVelocityTracker;
    private ArrayList<PaintAction> paintActions = new ArrayList<>();

    float size = 50;
    int color = Color.BLACK;
    boolean vTracker = false;
    float lastx, lasty;

    class PaintAction {
        private ArrayList<Path> paths = new ArrayList<>();
        private ArrayList<Paint> paints = new ArrayList<>();
    }

    public DoodleView(Context context) {
        super(context);
        init(null,0);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);

    }

    public DoodleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs,defStyle);
    }


    private void init(AttributeSet attrs, int defStyle){
        setBackgroundColor(Color.WHITE);
        mPaintDoodle.setColor(Color.BLACK);
        mPaintDoodle.setAntiAlias(true);
        mPaintDoodle.setStyle(Paint.Style.STROKE);
        mPaintDoodle.setStrokeWidth(size);
        mPaintDoodle.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < paintActions.size(); i++) {
            PaintAction pAi = paintActions.get(i);
            for (int j = 0; j < pAi.paths.size(); j++) {
                Path path = pAi.paths.get(j);
                Paint paint = pAi.paints.get(j);
                canvas.drawPath(path, paint);
            }
        }

        canvas.drawPath(mPath, mPaintDoodle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x,y);
                if(vTracker) {
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        mVelocityTracker.clear();
                    }
                    mVelocityTracker.addMovement(motionEvent);
                    lastx = x;
                    lasty=y;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if(vTracker) {
                    mVelocityTracker.addMovement(motionEvent);
                    mVelocityTracker.computeCurrentVelocity(10);
                    Double v = Math.sqrt(Math.pow(mVelocityTracker.getXVelocity(),2) + Math.pow(mVelocityTracker.getYVelocity(),2));
                    if (v == null || v < 1) {
                        v = 1.0;
                    }
                    mPaintDoodle = new Paint();
                    mPaintDoodle.setColor(color);
                    mPaintDoodle.setStyle(Paint.Style.STROKE);
                    mPaintDoodle.setStrokeCap(Paint.Cap.ROUND);
                    mPath = new Path();
                    mPath.moveTo(lastx,lasty);
                    mPaintDoodle.setStrokeWidth(Math.min((float) (v / 1), (float) 100));
                    mPath.lineTo(x, y);
                    mPaintAction.paths.add(mPath);
                    mPaintAction.paints.add(mPaintDoodle);
                    mPath = new Path();
                    mPaintDoodle = new Paint();
                    lastx=x;
                    lasty=y;
                } else {
                    mPath.lineTo(x, y);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPaintAction.paths.add(mPath);
                mPaintAction.paints.add(mPaintDoodle);
                mPath = new Path();
                mPaintDoodle = new Paint();
                mPaintDoodle.setStyle(Paint.Style.STROKE);
                mPaintDoodle.setStrokeCap(Paint.Cap.ROUND);
                mPaintDoodle.setStrokeWidth(size);
                mPaintDoodle.setColor(color);
                paintActions.add(mPaintAction);
                break;
        }

        invalidate();
        return true;
    }

    public void clear() {
        paintActions = new ArrayList<>();
        mPaintAction = new PaintAction();
        invalidate();
    }

    public void changeBrush(Intent intent) {
        Bundle bundle = intent.getExtras();
        size = bundle.getFloat("size");
        vTracker = bundle.getBoolean("vTracker", false);
        int a = bundle.getInt("alpha");
        int r = bundle.getInt("red");
        int g = bundle.getInt("green");
        int b = bundle.getInt("blue");
        color = Color.argb(a,r,g,b);
        mPaintDoodle.setStrokeWidth(size);
        mPaintDoodle.setColor(color);
    }
}
