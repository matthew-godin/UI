package net.codebot.pdfviewer;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

class Model
{
    private static Model instance;
    static Model getInstance()
    {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
    private ArrayList<ArrayList<Path>> paths;
    private ArrayList<ArrayList<Paint>> paints;
    private ArrayList<ArrayList<ArrayList<Point>>> points;
    private Stack<PossibleAction> savedActions;
    int actionIndex;
    int numPages;
    boolean created;
    Model() {
        paths = new ArrayList<ArrayList<Path>>();
        paints = new ArrayList<ArrayList<Paint>>();
        points = new ArrayList<ArrayList<ArrayList<Point>>>();
        savedActions = new Stack<PossibleAction>();
        numPages = 0;
        actionIndex = -1;
        created = false;
    }

    public void setNumPages(int numPages) {
        if (!created) {
            this.numPages = numPages;
            for (int i = 0; i < numPages; ++i) {
                paths.add(new ArrayList<Path>());
                paints.add(new ArrayList<Paint>());
                points.add(new ArrayList<ArrayList<Point>>());
            }
            created = true;
        }
    }

    public ArrayList<Paint> getPaints(int pageNumber) {
        return paints.get(pageNumber);
    }

    public ArrayList<Path> getPath(int pageNumber) {
        return paths.get(pageNumber);
    }

    public ArrayList<ArrayList<Point>> getPoints(int pageNumber) {
        return points.get(pageNumber);
    }

    public void pushAction(PossibleAction action) {
        while (actionIndex < savedActions.size() - 1) {
            savedActions.pop();
        }
        savedActions.add(action);
        ++actionIndex;
    }

    public PossibleAction undo() {
        if (actionIndex > -1) {
            return savedActions.get(actionIndex--);
        } else {
            return null;
        }
    }

    public PossibleAction redo() {
        if (actionIndex < savedActions.size() - 1) {
            return savedActions.get(++actionIndex);
        } else {
            return null;
        }
    }
}