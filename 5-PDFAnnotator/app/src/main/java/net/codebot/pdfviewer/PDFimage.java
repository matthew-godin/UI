package net.codebot.pdfviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class PDFimage extends ImageView {
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            scaleFactor *= scaleGestureDetector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);
            return true;
        }
    }

    class PanGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1,
                                MotionEvent e2,
                                float distanceX,
                                float distanceY) {
            moveX -= distanceX * 0.5f * scaleFactor;
            moveY -= distanceY * 0.5f * scaleFactor;
            setTranslationX(moveX);
            setTranslationY(moveY);
            return true;
        }
    }

    final String LOGNAME = "pdf_image";
    final float DRAW_WIDTH = 3, HIGHLIGHT_WIDTH = 30;
    final int HIGHLIGHT_ALPHA = 100;
    enum TouchState { STILL, MOVING };
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float moveX = 0, moveY = 0;
    private boolean panHold = false;
    private float scaleFactor = 1.0f;

    // drawing path
    Path path = null;
    ArrayList<Path> paths;
    ArrayList<Paint> paints;
    ArrayList<ArrayList<Point>> points;
    MainActivity.ClickerState clickerState;
    //private CoordinateCallback coordinateCallback;
    private Paint lastPaint;
    private Path lastPath;
    private ArrayList<Point> lastPoints;
    TouchState touchState = TouchState.STILL;
    Model model;

    // image to display
    Bitmap bitmap;
    // constructor
    public PDFimage(Context context, MainActivity.ClickerState clickerState) {
        super(context);
        this.clickerState = clickerState;
        model = Model.getInstance();
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new PanGestureListener());
    }

    int pageNumber;
    public void setPageIndex(int pageIndex) {
        paths = model.getPath(pageIndex);
        paints = model.getPaints(pageIndex);
        points = model.getPoints(pageIndex);
        pageNumber = pageIndex;
    }

    // capture touch events (down/move/up) to create a path
    // and use that to create a stroke that we can draw
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clickerState != MainActivity.ClickerState.NORMAL) {
            float x = event.getX();
            float y = event.getY();
            if (clickerState == MainActivity.ClickerState.ERASE) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        checkErase(x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        checkErase(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startingPath(x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updatePath(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                        model.pushAction(new PossibleAction(paths.size() -1, lastPath,
                                lastPaint, lastPoints, clickerState, pageNumber));
                        break;
                }
            }
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    panHold = true;
                    break;
                case MotionEvent.ACTION_UP:
                    panHold = false;
                    break;
            }
            scaleGestureDetector.onTouchEvent(event);
            gestureDetector.onTouchEvent(event);
        }
            /*switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(LOGNAME, "Action down");
                    path = new Path();
                    path.moveTo(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(LOGNAME, "Action move");
                    path.lineTo(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(LOGNAME, "Action up");
                    drawPaths.add(path);
                    break;
            }*/
        return true;
    }

    private void checkErase(float x, float y) {
        boolean done = false;
        double drawWidth = DRAW_WIDTH * 4.0, highlightWidth = HIGHLIGHT_WIDTH * 4.0;
        for (int i = 0; !done && i < points.size(); ++i) {
            for (int j = 0; !done && j < points.get(i).size() - 1; ++j) {
                double distance = lineDistance(new Point((int)x, (int)y), points.get(i).get(j), points.get(i).get(j + 1));
                Paint paint = paints.get(i);
                if (paint.getColor() == Color.BLUE) {
                    Log.v("MY_APP", distance + ";" + drawWidth);
                    if (distance <= drawWidth) {
                        model.pushAction(new PossibleAction(i, paths.get(i),
                                paints.get(i), points.get(i), clickerState, pageNumber));
                        points.remove(i);
                        paths.remove(i);
                        paints.remove(i);
                        done = true;
                    }
                } else if (paint.getColor() == Color.YELLOW) {
                    Log.v("MY_APP", distance + ";" + drawWidth);
                    if (distance <= highlightWidth) {
                        model.pushAction(new PossibleAction(i, paths.get(i),
                                paints.get(i), points.get(i), clickerState, pageNumber));
                        points.remove(i);
                        paths.remove(i);
                        paints.remove(i);
                        done = true;
                    }
                }
            }
        }
    }

    private double lineDistance(Point M, Point P0, Point P1) {
        Point c = closestPoint(M, P0, P1);
        return Math.sqrt(Math.pow(M.x - c.x, 2) + Math.pow(M.y - c.y, 2));
    }

    private Point closestPoint(Point M, Point P0, Point P1) {
        Point v = new Point(P1.x - P0.x, P1.y - P0.y);
        if (v.x * v.x + v.y * v.y < 0.5)
            return P0;
        Point u = new Point(M.x - P1.x, M.y - P1.y);
        double s = (u.x * v.x + u.y * v.y) / (v.x * v.x + v.y * v.y);
        if (s < 0)
            return P0;
        else if (s > 1)
            return P1;
        else {
            Point w = new Point((int)(s * v.x), (int)(s * v.y));
            Point I = new Point (P0.x + w.x, P0.y + w.y);
            return I;
        }
    }

    private void updatePath(float x, float y) {
        touchState = TouchState.MOVING;
        lastPath.lineTo(x, y);
        lastPoints.add(new Point((int)x, (int)y));
    }

    private void startingPath(float x, float y) {
        initPaint();
        lastPath.moveTo(x, y);
        lastPoints.add(new Point((int)x, (int)y));
    }

    private void initPaint() {
        switch (clickerState) {
            case DRAW:
                lastPaint = getDrawPaint();
                break;
            case HIGHLIGHT:
                lastPaint = getHighlightPaint();
                break;
        }
        lastPath = new Path();
        lastPoints = new ArrayList<Point>();
        paints.add(lastPaint);
        paths.add(lastPath);
        points.add(lastPoints);
    }

    private Paint getDrawPaint() {
        Paint paint = new Paint();
        paint.setStrokeWidth(DRAW_WIDTH);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.BLUE);
        return paint;
    }

    private Paint getHighlightPaint() {
        Paint paint = new Paint();
        paint.setStrokeWidth(HIGHLIGHT_WIDTH);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.YELLOW);
        paint.setAlpha(HIGHLIGHT_ALPHA);
        return paint;
    }

    // set image as background
    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    // set brush characteristics
    // e.g. color, thickness, alpha
    public void setClickerState(MainActivity.ClickerState clickerState) {
        this.clickerState = clickerState;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw background
        if (bitmap != null) {
            this.setImageBitmap(bitmap);
        }
        // draw lines over it
        /*for (Path path : paths) {
            canvas.drawPath(path, drawPaint);
        }*/
        super.onDraw(canvas);
        for (int i = 0; i < paints.size(); ++i) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
    }

    public void unperformAction(PossibleAction action) {
        if (action.clickerState == MainActivity.ClickerState.ERASE) {
            paints.add(action.index, action.paint);
            paths.add(action.index, action.path);
            points.add(action.index, action.pointSet);
        } else {
            points.remove(action.index);
            paths.remove(action.index);
            paints.remove(action.index);
        }
    }

    public void performAction(PossibleAction action) {
        if (action.clickerState == MainActivity.ClickerState.ERASE) {
            points.remove(action.index);
            paths.remove(action.index);
            paints.remove(action.index);
        } else {
            paints.add(action.paint);
            paths.add(action.path);
            points.add(action.pointSet);
        }
    }
}
