package com.example.hqmove.deepimpact;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.gsm.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.hqmove.deepimpact.Tab_Activity.location_string;
import static com.example.hqmove.deepimpact.Tab_Activity.phone_string;
import static com.example.hqmove.deepimpact.Tab_Activity.sms_string;




/**
 * Created by hqmove on 06/05/17.
 */

public class fragment_tab_detect extends Fragment implements SensorEventListener, LocationListener {

    Button btn_stop, btn_record;

    // constant G pour la terre
    public static final float GRAVITY_EARTH = 9.80665f;
    //declaration des variables pour le sensor
    private SensorManager senSensorManager_G;
    private Sensor senAccelerometer;

    //declaration variable pour le GPS
    private LocationManager locationManager;
    private Location location;
    private LocationListener locationListener;

    int i = 0, reste = 50;
    Float X, Y, Z;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // permet de construire le contenu de l'onglet adequat du fragment
            View rootView = inflater.inflate(R.layout.fragment_tab_detect, container, false);

            //assign textview
            Tab_Activity.text_G = (TextView) rootView.findViewById(R.id.gravity_text);
            Tab_Activity.text_V = (TextView) rootView.findViewById(R.id.speed_text);
            Tab_Activity.text_Move = (TextView) rootView.findViewById(R.id.moving_text);
            Tab_Activity.text_Alert  = (TextView) rootView.findViewById(R.id.alert_text);
            Tab_Activity.text_Location = (TextView) rootView.findViewById(R.id.location_text);
            Tab_Activity.text_State = (TextView) rootView.findViewById(R.id.state_text);

            //autocompletion des activités pratiquée

            // Get a reference to the AutoCompleteTextView in the layout
            //AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.editSport);
            // Get the string array
            //String[] sports = getResources().getStringArray(R.array.sports_array);
            // Create the adapter and set it to the AutoCompleteTextView
            // this (fragment) est remplacé par getActivity() activité
            //ArrayAdapter<String> adapter =
            //        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sports);
            //textView.setAdapter(adapter);

            // create sensor manager
            //appel depuis une activité
            //senSensorManager_G = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            // appel depuis un fragment
            senSensorManager_G = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            // sensor type linear acceleration
            senAccelerometer = senSensorManager_G.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            //senAccelerometer = senSensorManager_G.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            // Acquire a reference to the system Location Manager GPS
            //LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            //get Gps location service LocationManager object
            //appel depuis une activité
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // appel depuis un fragment
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            //register sensor listener
            //senSensorManager_G.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            //jemesmain: specification du delay en microseconde ici 1/10ieme de seconde
            senSensorManager_G.registerListener(this, senAccelerometer, 1000000);
            //senSensorManager.registerListener(Tab_Activity.this,
            //        senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            //        senSensorManager.SENSOR_DELAY_NORMAL);
            // Register the listener with the Location Manager to receive location updates
            //text_Location.setText("entering if location request");


            impact_position();
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 100, locationListener);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, this);
            // toutes les 3s avec 0m de variation de position
            //locationManager.requestLocationUpdates(this, 3000, 10, LocationManager.GPS_PROVIDER);

            btn_record = (Button) rootView.findViewById(R.id.button_record);
            btn_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(getApplicationContext(),Tab_Activity.class);
                    //startActivity(intent);
                    Intent intent = new Intent(getActivity(), FullscreenRecord.class);
                    startActivity(intent);
                    String toast_message ="G record!";
                    Toast msg = Toast.makeText(getContext(), toast_message, Toast.LENGTH_SHORT);
                    msg.show();
                }
            });

            btn_stop = (Button) rootView.findViewById(R.id.button_stop);

            btn_stop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String toast_message ="Impact Detection Stopped";
                    Toast msg = Toast.makeText(getContext(), toast_message, Toast.LENGTH_SHORT);
                    msg.show();
                    //finish();
                }
            });


            //Timer timer = new Timer();
//timer.schedule(task, delay, period)
//timer.schedule( new performClass(), 1000, 30000 );
// or you can write in another way
//timer.scheduleAtFixedRate(task, delay, period);
         //   timer.scheduleAtFixedRate( new performClass(), 1000, 1000 );

            return rootView;

        }




    @Override
    public void onSensorChanged(SensorEvent event) {
        // lié à l'accélérometre implement sensoreventlistener
        if (Tab_Activity.clear_alert){
            //on reset l'action d'alerte
            Tab_Activity.G = 0.0;
            Tab_Activity.Max_G = 0.0;
            i = 0;
            reste = 50;

            Tab_Activity.text_G.setText("alert_cleared");
            Tab_Activity.text_V.setText("alert_cleared");
            Tab_Activity.text_Move.setText("alert_cleared");
            Tab_Activity.text_Alert.setText("alert_cleared");
            Tab_Activity.text_Location.setText("alert_cleared");

            Tab_Activity.clear_alert=false;
            Tab_Activity.sms_once=true;
        }

        if (Tab_Activity.Max_G>Tab_Activity.G_limit){
            Tab_Activity.text_G.setText("Gravity alert" + Tab_Activity.Max_G +" (nombre de G)");
        }
        else {
            X = event.values[0];
            Y = event.values[1];
            Z = event.values[2];
            Tab_Activity.G = Math.sqrt(X * X + Y * Y + Z * Z) / GRAVITY_EARTH;
            //G_text =
            Tab_Activity.G = Double.valueOf(Math.round(Tab_Activity.G*1000))/1000;
            //G = Math.round(G*100)/100;
            //text_content.setText("X: "+ event.values[0] +" Y: " + event.values[1]+ " Z: " + event.values[2]);
        }
        if (Tab_Activity.G>Tab_Activity.Max_G) {
            Tab_Activity.Max_G=Tab_Activity.G;
        }
        Tab_Activity.text_G.setText("Actual G: " + Tab_Activity.G + " Max G: " + Tab_Activity.Max_G + " G limit: " + Tab_Activity.G_limit);
          /*  do
            {
                text_V.setText("Impacted, actual G" + G + " Max_G: " + Max_G);
                X = event.values[0];
                Y = event.values[1];
                Z = event.values[2];
                G = Math.sqrt(X*X + Y*Y + Z*Z)/GRAVITY_EARTH;

            }
            while (TRUE);*/

        if (Tab_Activity.Max_G>Tab_Activity.G_limit){
            X = event.values[0];
            Y = event.values[1];
            Z = event.values[2];
            Double G_temp = Math.sqrt(X*X + Y*Y + Z*Z)/GRAVITY_EARTH;
            Tab_Activity.G = Double.valueOf(Math.round(G_temp*1000))/1000;

            //G = Math.round(G*100)/100;
            Tab_Activity.text_V.setText("Impacted, actual G: " + Tab_Activity.G + " Max_G: " + Tab_Activity.Max_G);

            //G<1.1 ou 0,1 si G est mesuré avec l'accéléromètre qui donne 0G = immobile
            if (Tab_Activity.G<0.1) {
                Tab_Activity.text_Move.setText("Not moving for: " + i + " Actual  G " + Tab_Activity.G);
                i++;
                //attente d'une seconde pour le reste
                //SystemClock.sleep(5000);
                try {
                    Thread.sleep(2500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                //try {
                //    wait(1000);
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
                //Handler handler = new Handler();
                //handler.postDelayed((Runnable) this, 1500);

                if (reste > 0) {
                    Tab_Activity.text_Alert.setText("Sending Alert in: " + reste +" or clear alert");

                    reste--;
                }
                else {
                    Tab_Activity.alert_string = "Definitely, " + Tab_Activity.name_string +" you are unconscious! Alert sent to PGHM / CRS Alpes 04 76 22 22 22  and to number "+ phone_string +" with coordinate: " + location_string;
                    Tab_Activity.text_Alert.setText(Tab_Activity.alert_string);
                    sms_string = Tab_Activity.name_string +" has impacted during "+ Tab_Activity.sport_string +" activity. He seems to be uncouscious. Last known position is: http://maps.google.com/?" + location_string;
                    //envoi de sms
                    if (Tab_Activity.sms_once) {
                        //pour envoyer qu'un seul sms
                        Tab_Activity.sms_once = false;
                        send_sms();
                        //Toast msg = Toast.makeText(getBaseContext(), "sms sent to ", Toast.LENGTH_SHORT);
                        //msg.show();
                        Tab_Activity.text_State.setText("sms_sent to" + phone_string);
                    }

                }
            }
            else {
                //la personne bouge de nouveau...il faudra mettre un délai mini...
                //Handler handler2 = new Handler();
                //handler2.postDelayed((Runnable) this, 1000);
                try {
                    Thread.sleep(2500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                reste=60;
                //on reset le Max G
                Tab_Activity.Max_G=0.0;
                Tab_Activity.text_Alert.setText("You are moving again");

            }



        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // lié à l'accélérometre implement sensoreventlistener
        //not in use
    }

    //demande les permissions utilisateur @run time
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                // permission utilisateur pour le gps sur code 10
                impact_position();
                break;
            case 20:
                // permission utilisateur pour le sms sur code 20
                send_sms();
                break;
            default:
                break;
        }
    }


    void impact_position(){
        // first check for permissions
        //dans la ligne suivant this (fragment) est remplacé par getActivity l'activité
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

        //noinspection MissingPermission
        Tab_Activity.text_State.setText("Permission GPS Granted");
        //locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, this);

    }

    void send_sms() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}
                        , 20);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

        //noinspection MissingPermission
        Tab_Activity.text_State.setText("Permission SMS Granted");
        //PendingIntent pi = PendingIntent.getActivity(this, 0,
        //new Intent(this, SMS.class), 0);

        SmsManager sms = SmsManager.getDefault();

        //sms.sendTextMessage(phone_string, null, sms_string, pi, null);
        sms.sendTextMessage(phone_string, null, sms_string, null, null);



    }

    @Override
    public void onLocationChanged(Location location) {
        // lié au GPS implement LocationListener
        //text_Location.setText("Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude());
        Tab_Activity.text_State.setText("location changed");
        location_string = "ll="+location.getLatitude()+","+location.getLongitude();
        //text_Location.setText("Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude());
        Toast.makeText(getContext(), location_string, Toast.LENGTH_SHORT).show();
        Tab_Activity.text_Location.setText(location_string);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // lié au GPS implement LocationListener
        /******** Called when User off Gps *********/
        //text_Location.setText("Gps turned off ");
        Toast.makeText(getContext(), "Gps turned off veuillez l'activer", Toast.LENGTH_LONG).show();

        //appelle le réglage du gps si gps off
        //Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //startActivity(i);
    }

    @Override
    public void onProviderEnabled(String provider) {
        // lié au GPS implement LocationListener
        /******** Called when User on Gps  *********/
        //text_Location.setText("Gps turned on ");
        Toast.makeText(getContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    //private class performClass extends TimerTask {
        @Override
     //   public void run() {
     //       Toast.makeText(getContext(), "scheduled task ", Toast.LENGTH_LONG).show();

    //    }
   // }
//}
