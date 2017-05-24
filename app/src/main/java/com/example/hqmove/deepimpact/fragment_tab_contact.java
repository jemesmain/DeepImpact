package com.example.hqmove.deepimpact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static java.text.DateFormat.getDateTimeInstance;
import static java.util.Calendar.SHORT;


/**
 * Created by hqmove on 06/05/17.
 */


public class fragment_tab_contact extends Fragment {

    public TextView version_log;


            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

                // permet de construire le contenu de l'onglet adequat du fragment
                View rootView = inflater.inflate(R.layout.fragment_tab_contact, container, false);

                version_log = (TextView) rootView.findViewById(R.id.version_log);
                version_log.setText("version 10.0:\ngraphique d'enregistrement de G\nverion 9.0:\nimplementation graphique des fragments ");

                return rootView;




             }

/*
        @Override
        public void onAttach(Activity activity) {
                super.onAttach(activity);
                ((Tab_Activity) activity).onSectionAttached(
                        getArguments().getInt(Tab_Activity.ARG_SECTION_NUMBER));
        }
*/

/*
        @Override
        public void onAttach(Activity activity) {
                super.onAttach(activity);

                ((Tab_Activity) activity).onSectionAttached(1);

        }
*/


// reste de test graphview

/*
        private DataPoint[] generateData() {
                int count = 30;
                DataPoint[] values = new DataPoint[count];
                for (int i=0; i<count; i++) {
                        double x = i;
                        double f = mRand.nextDouble()*0.15+0.3;
                        double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
                        DataPoint v = new DataPoint(x, y);
                        values[i] = v;
                }
                return values;
        }

        double mLastRandom = 2;
        Random mRand = new Random();
        private double getRandom() {
                return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
        }
*/
}
