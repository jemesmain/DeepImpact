package com.example.hqmove.deepimpact;


import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.TextView;
import android.Manifest;

import android.content.Context;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.telephony.gsm.SmsManager;
import android.os.Build;


import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.AutoCompleteTextView;


import java.lang.Boolean;
import java.lang.Double;

import android.widget.Toast;

public class Tab_Activity extends AppCompatActivity  {

    //debug
    //private static final String TAG = Tab_Activity.class.getSimpleName();
    //Log.d(TAG, "test_log");
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;



    public static TextView text_G, text_V, text_Move, text_Alert, text_Location, text_State;

    public static EditText input_Number,input_Name, input_Sport, input_Gravity;



    public static Double G = 0.0, Max_G = 0.0, G_limit = 3.0;
    public static String phone_string="", name_string="", sport_string ="", sms_string="", alert_string="", location_string ="";
    public static Boolean sms_once = true, clear_alert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        //initialisation
        //G=0;
        //Max_G=0;

        // store G limit input to a variable
        //input_Gravity.addTextChangedListener();
        //str = input_Gravity.getText().toString();
        //G_limit = Double.valueOf(input_Gravity.getText().toString());
        //G_limit = Double.valueOf(str);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Snackbar.make(view, "Alert Cleared message canceled", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Tab_Activity.clear_alert=true;
            }
        });









    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*
    //jemesmain: placeholderfragment géré par les classe java des onglets
    /**
     * A placeholder fragment containing a simple view.
     */
    //public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
       // private static final String ARG_SECTION_NUMBER = "section_number";

        //public PlaceholderFragment() {
       // }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
       /** public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


                    View rootView = inflater.inflate(R.layout.fragment_tab_setting, container, false);
                    TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                    return rootView;

        }
    }
*/

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // jemesmain: cette partie n'est plus géré par place holderfragment mais par un switch
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    fragment_tab_setting tab1 = new fragment_tab_setting();
                    return tab1;
                case 1:
                    fragment_tab_detect tab2 = new fragment_tab_detect();
                    return tab2;
                case 2:
                    fragment_tab_contact tab3 = new fragment_tab_contact();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //jemesmain: défini le nom des onglets
            switch (position) {
                case 0:
                    return "Settings";
                case 1:
                    return "Detection";
                case 2:
                    return "Contact";
            }
            return null;
        }
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //host.getCurrentTab(R.id.tab1);
            //host.setCurrentTabByTag ("Tab One");
            //spec.getTabAt(0).select();
            //host.getTabWidget().setCurrentTab(0);
            //findViewById(R.id.tab1).show();
            //host.getTabWidget().getChildAt(0).setVisibility(View.VISIBLE);
            //host.getTabAt(0).select();
            return true;
        }
        if (id == R.id.action_impact) {
            //host.setCurrentTab(1);
            //findViewById(R.id.tab2).getDisplay();
            //host.getTabWidget().getChildAt(1).setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

*/

}



