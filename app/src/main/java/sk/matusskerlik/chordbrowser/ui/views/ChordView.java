/*
 * Copyright (c) 2019 Matúš Škerlík.
 * All rights reserved.
 */

package sk.matusskerlik.chordbrowser.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.matusskerlik.chordbrowser.R;
import sk.matusskerlik.chordbrowser.model.Chord;

public class ChordView extends View {

    private Chord chordToDraw;

    private int stringsCount = 6;
    private int[] stringPositions = new int[stringsCount];

    private int stringStrokeWidth = 10;
    private int fretStrokeWidth = 4;
    private int fingerTipRadius = 25;

    private int fontSize = 16;
    private int headerFontSize = 32;
    private int leftPadding = 128;
    private int rightPadding = 64;
    private int topPadding = 128;
    private int topMargin = 128;

    private int firstFret;
    private int lastFret;

    private Paint stringsPaint;
    private Paint stringsErrorPaint;
    private Paint fretPaint;
    private Paint textPaint;
    private Paint backgroundPaint;
    private Paint fingerTipPaint;
    private Paint headerTextPaint;

    public ChordView(Context context) {
        super(context);
        init(null, 0);
    }

    public ChordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ChordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ChordView, defStyle, 0);

        //TODO attributes

        /*
        stringStrokeWidth = (int) dpToPx(10, getContext());
        fretStrokeWidth = (int) dpToPx(4, getContext());
        fingerTipRadius = (int) dpToPx(25, getContext());

        fontSize = (int) dpToPx(16, getContext());
        leftOffset = (int) dpToPx(32, getContext());
        topOffset = (int) dpToPx(128, getContext());
       */

        a.recycle();

        stringsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stringsErrorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fretPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint = new Paint();
        fingerTipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        headerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        stringsPaint.setStrokeWidth(stringStrokeWidth);
        stringsErrorPaint.setStrokeWidth(stringStrokeWidth);
        stringsPaint.setColor(Color.WHITE);
        stringsErrorPaint.setColor(Color.RED);

        fretPaint.setStrokeWidth(fretStrokeWidth);
        fretPaint.setColor(Color.WHITE);

        textPaint.setTextSize(ChordView.dpToPx(fontSize, getContext()));
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        fingerTipPaint.setColor(Color.YELLOW);

        backgroundPaint.setColor(Color.DKGRAY);

        headerTextPaint.setTextSize(ChordView.dpToPx(headerFontSize, getContext()));
        headerTextPaint.setColor(Color.WHITE);
        headerTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Update TextPaint and text measurements from attributes
        invalidateView();
    }

    private void invalidateView() {

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce

        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

        if (chordToDraw != null){

            //draw frets first
            int fretsCount = lastFret - firstFret + 1;
            for (int fretIndex = 0; fretIndex < fretsCount; fretIndex++) {

                // FRET LINE
                int [] fretLineCoordinates = ChordView.mapFretCoordinates(
                        fretIndex,
                        fretsCount, stringsCount,
                        getWidth(), getHeight(),
                        leftPadding, rightPadding, topPadding, topMargin
                );
                canvas.drawLine(
                        fretLineCoordinates[0],
                        fretLineCoordinates[2],
                        fretLineCoordinates[1],
                        fretLineCoordinates[3],
                        fretPaint
                );

                //NUMBER FRETS
                int currentFretNumber = firstFret + fretIndex;
                int [] fretNumberCoordinates = ChordView.mapTextToFret(
                    fretIndex,
                    fretsCount,
                    stringsCount,
                    getWidth(), getHeight(),
                    leftPadding, rightPadding, topPadding, topMargin,
                    fontSize
                );
                if (fretIndex < fretsCount)
                    canvas.drawText(
                            String.valueOf(currentFretNumber),
                            fretNumberCoordinates[0],
                            fretNumberCoordinates[1],
                            textPaint
                    );
            }

            //draw strings
            for (int stringIndex = 0; stringIndex < stringsCount; stringIndex++) {

                //STRING LINES
                int [] stringLineCoordinates = ChordView.mapStringCoordinates(
                        stringIndex,
                        stringsCount,
                        getWidth(), getHeight(),
                        leftPadding, rightPadding, topPadding, topMargin
                );
                canvas.drawLine(
                        stringLineCoordinates[0],
                        stringLineCoordinates[2],
                        stringLineCoordinates[1],
                        stringLineCoordinates[3],
                        stringPositions[stringIndex] == -1 ?  stringsErrorPaint : stringsPaint
                );

                // if string position is not 0 or -1
                if (stringPositions[stringIndex] >= 0) {

                    if (stringPositions[stringIndex] == 0){
                        // O TEXT
                        int [] oCoordinates = mapTextToString(
                                stringIndex,
                                stringsCount,
                                getWidth(),
                                leftPadding, rightPadding, topPadding, topMargin,
                                fontSize,
                                stringStrokeWidth
                        );

                        canvas.drawText("o", oCoordinates[0], oCoordinates[1], textPaint);
                    } else {
                        //FINGER TIP DOT
                        int[] fingerDotCoordinates = ChordView.mapFingerToStringFret(
                                stringIndex,
                                stringPositions[stringIndex],
                                firstFret,
                                stringsCount,
                                fretsCount,
                                getWidth(), getHeight(),
                                leftPadding, rightPadding, topPadding, topMargin
                        );
                        canvas.drawCircle(
                                fingerDotCoordinates[0],
                                fingerDotCoordinates[1],
                                fingerTipRadius,
                                fingerTipPaint
                        );
                    }


                } else if (stringPositions[stringIndex] == -1) { // draw x

                    // X FOR STRING
                    int [] xCoordinates = mapTextToString(
                            stringIndex,
                            stringsCount,
                            getWidth(),
                            leftPadding, rightPadding, topPadding, topMargin,
                            fontSize,
                            stringStrokeWidth
                    );

                    canvas.drawText("x", xCoordinates[0], xCoordinates[1], textPaint);
                }
            }

            int [] headerCoordinates = mapHeaderText(
                    getWidth(),
                    leftPadding, rightPadding, topPadding, topMargin,
                    headerFontSize
            );
            canvas.drawText(
                    chordToDraw.getKey().getLabel(),
                    headerCoordinates[0],
                    headerCoordinates[1],
                    headerTextPaint
            );
            canvas.drawText(
                    chordToDraw.getType().getLabel(),
                    headerCoordinates[0] + headerFontSize,
                    headerCoordinates[1] + headerFontSize,
                    textPaint
            );
        }
    }

    public void setChordToDraw(@NonNull Chord chordToDraw) {
        this.chordToDraw = chordToDraw;

        List<Integer> frets = new ArrayList<>();

        try {
            int E = Integer.parseInt(chordToDraw.getE());
            stringPositions[0] = E;
            frets.add(E);
        } catch (NumberFormatException ignore){
            stringPositions[0] = -1;
        }

        try {
            int A = Integer.parseInt(chordToDraw.getA());
            stringPositions[1] = A;
            frets.add(A);
        } catch (NumberFormatException ignore){
            stringPositions[1] = -1;
        }

        try {
            int D = Integer.parseInt(chordToDraw.getD());
            stringPositions[2] = D;
            frets.add(D);
        } catch (NumberFormatException ignore){
            stringPositions[2] = -1;
        }

        try {
            int G = Integer.parseInt(chordToDraw.getG());
            stringPositions[3] = G;
            frets.add(G);
        } catch (NumberFormatException ignore){
            stringPositions[3] = -1;
        }

        try {
            int B = Integer.parseInt(chordToDraw.getB());
            stringPositions[4] = B;
            frets.add(B);
        } catch (NumberFormatException ignore){
            stringPositions[4] = -1;
        }

        try {
            int E2 = Integer.parseInt(chordToDraw.getE2());
            stringPositions[5] = E2;
            frets.add(E2);
        } catch (NumberFormatException ignore){
            stringPositions[5] = -1;
        }

        firstFret = Collections.min(frets) <= 0 ? 1 : Collections.min(frets);
        lastFret = Collections.max(frets);

        invalidateView();
    }

    /**
     * Maps x_start, x_end, y_start, y_end coordinates to string number
     * @param num guitar string number
     * @return int[0] x_start, int[1] x_end, int[2] y_start, int[3] y_end
     */
    private static int[] mapStringCoordinates(int num, int stringCount, int width, int height, int leftOffset, int rightOffset, int topOffset, int topMargin){

        int stringSpace = (width - leftOffset - rightOffset) / (stringCount - 1);

        return new int[]{
                leftOffset + (num * stringSpace),
                leftOffset + (num * stringSpace),
                topMargin + topOffset,
                height
        };
    }

    /**
     * Maps x_start, x_end, y_start, y_end coordinates to fret number
     * @param num fret number
     * @return int[0] x_start, int[1] x_end, int[2] y_start, int[3] y_end
     */
    private static int[] mapFretCoordinates(int num, int fretsCount, int stringsCount, int width, int height, int leftOffset, int rightOffset, int topOffset, int topMargin){

        int stringSpace = (width - leftOffset - rightOffset) / (stringsCount - 1);
        int fretSpace = (height - topOffset - topMargin) / fretsCount;

        return new int[]{
                leftOffset,
                leftOffset + (stringSpace * (stringsCount - 1)),
                topMargin + topOffset + ((num + 1) * fretSpace) - fretSpace,
                topMargin + topOffset + ((num + 1) * fretSpace) - fretSpace
        };
    }

    private static int[] mapFingerToStringFret(int stringNum, int position, int fretStart, int stringsCount, int fretsCount, int width, int height, int leftOffset, int rightOffset, int topOffset, int topMargin){

        int stringSpace = (width - leftOffset - rightOffset) / (stringsCount - 1);
        int fretSpace = (height - topOffset - topMargin) / fretsCount;

        return new int[] {
                leftOffset + (stringNum * stringSpace),
                topMargin + topOffset + ((position - fretStart) * fretSpace) + (fretSpace / 2),
        };
    }

    private static int[] mapTextToFret(int num, int fretsCount, int stringsCount, int width, int height, int leftOffset, int rightOffset, int topOffset, int topMargin, int fontSize){

        int fretSpace = (height - topOffset - topMargin) / fretsCount;

        return new int[] {
                (leftOffset / 2) - fontSize,
                topMargin + topOffset + ((num + 1) * fretSpace) - fretSpace + (fretSpace / 2) + fontSize
        };
    }

    private static int[] mapTextToString(int stringNum, int stringsCount, int width, int leftOffset, int rightOffset, int topOffset, int topMargin, int fontSize, int stringStrokeWidth){

        int stringSpace = (width - leftOffset - rightOffset) / (stringsCount - 1);

        return new int[] {
                leftOffset + (stringNum * stringSpace) - fontSize + (stringStrokeWidth / 2),
                topMargin + (topOffset / 2) + fontSize,
        };
    }

    private static int[] mapHeaderText(int width, int leftOffset, int rightOffset, int topOffset,  int topMargin, int fontSize){

        return new int[] {
                (width / 2) - fontSize,
                (topMargin / 2) + fontSize
        };
    }

    private static float dpToPx(int dp, Context context){

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }
}
