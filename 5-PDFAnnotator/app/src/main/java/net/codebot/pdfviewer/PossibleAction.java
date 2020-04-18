package net.codebot.pdfviewer;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;

public class PossibleAction {
    int index; Path path; Paint paint; ArrayList<Point> pointSet;
    MainActivity.ClickerState clickerState; int pageNumber;
    PossibleAction(int index, Path path, Paint paint, ArrayList<Point> pointSet,
                   MainActivity.ClickerState clickerState, int pageNumber) {
        this.index = index;
        this.path = path;
        this.paint = paint;
        this.pointSet = pointSet;
        this.clickerState = clickerState;
        this.pageNumber = pageNumber;
    }
}
