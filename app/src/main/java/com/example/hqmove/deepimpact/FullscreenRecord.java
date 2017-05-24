package com.example.hqmove.deepimpact;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenRecord extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> G_Series;
    private LineGraphSeries<DataPoint> G_lim_Series;
    private LineGraphSeries<DataPoint> G_max_Series;
    private LineGraphSeries<DataPoint> mSeries2;
    private double graph2LastXValue = 0d;

    //final long millis_start = new Date().getTime()/1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_record);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);



        GraphView graph = (GraphView) findViewById(R.id.graph);
        //mSeries1 = new LineGraphSeries<>(generateData());
        //mSeries1 = new LineGraphSeries<>(new DataPoint[] {
        //        new DataPoint(0, 0)
        //});
        G_Series = new LineGraphSeries<>(new DataPoint[]{});
        G_max_Series = new LineGraphSeries<>(new DataPoint[]{});
        G_lim_Series = new LineGraphSeries<>(new DataPoint[]{});



        graph.addSeries(G_Series);
        graph.addSeries(G_max_Series);
        graph.addSeries(G_lim_Series);
        // set date label formatter
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(15); // only 4 because of the space

        // titre du graphique
        //graph.setTitle("G record");
        //nombre de label horizontal
        graph.getGridLabelRenderer().setNumHorizontalLabels(6);

        // styling serie
        G_Series.setTitle("G");
        G_lim_Series.setTitle("G limit");
        G_lim_Series.setColor(Color.BLACK);
        G_max_Series.setTitle("G max");
        G_max_Series.setColor(Color.RED);
        //series.setDrawDataPoints(true);
        //series.setDataPointsRadius(10);
        //series.setThickness(8);
        //rendre les légendes visibles
        graph.getLegendRenderer().setVisible(true);

        // custom paint to make a dotted line
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        //paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        G_lim_Series.setCustomPaint(paint);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxX(10.0);

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(5.0);

        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);
        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);
        // activate horizontal and vertical zooming and scrolling
        //graph.getViewport().setScalableY(true);
        // activate vertical scrolling
        //graph.getViewport().setScrollableY(true);



/*
                GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph2);
                //mSeries2 = new LineGraphSeries<>();


                LineGraphSeries<DataPoint> mSeries2 = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, 1),
                    new DataPoint(10, 5),
                    new DataPoint(20, 3),
                    new DataPoint(30, 2),
                    new DataPoint(40, 6)
                });
                //graph.addSeries(series);
                graph2.addSeries(mSeries2);
                graph2.getViewport().setXAxisBoundsManual(true);
                graph2.getViewport().setMinX(0);
                graph2.getViewport().setMaxX(40);
*/


    }





    @Override
    public void onResume() {
        super.onResume();
 /*
                mTimer1 = new Runnable() {
                        @Override
                        public void run() {
                                mSeries1.resetData(generateData());
                                //mHandler.postDelayed(this, 300);
                                mHandler.postDelayed(this, 1000);
                        }
                };
                //mHandler.postDelayed(mTimer1, 300);
                mHandler.postDelayed(mTimer1, 1000);
*/


        mTimer2 = new Runnable() {
            @Override
            public void run() {
                graph2LastXValue += 0.1d;
                //mSeries2.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 40);
                // generate Dates
                //Calendar calendar = Calendar.getInstance();
                //Date date = calendar.getTime();
                //int i=0;


                //long millis = new Date().getTime()/1000;


                //millis représente un chrono depuis le début de l'enregistrement
                //millis = millis - millis_start;



                G_Series.appendData(new DataPoint(graph2LastXValue, Tab_Activity.G), true, 100);
                G_lim_Series.appendData(new DataPoint(graph2LastXValue, Tab_Activity.G_limit), true, 100);
                G_max_Series.appendData(new DataPoint(graph2LastXValue, Tab_Activity.Max_G), true, 100);
                //mHandler.postDelayed(this, 200);
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(mTimer2, 1000);
    }



/*
        @Override
        public void onPause() {
                mHandler.removeCallbacks(mTimer1);
                mHandler.removeCallbacks(mTimer2);
                super.onPause();
        }
*/


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
