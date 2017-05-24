package com.example.hqmove.deepimpact;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.SharedPreferences;





/**
 * Created by hqmove on 06/05/17.
 */

public class fragment_tab_setting  extends  Fragment  {


    Button btn_get, btn_save;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // permet de construire le contenu de l'onglet adequat du fragment
        //final View rootView = inflater.inflate(R.layout.fragment_tab_setting, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_tab_setting, container, false);

//assign textEdit
        Tab_Activity.input_Number = (EditText) rootView.findViewById(R.id.editNumber);
        Tab_Activity.input_Name = (EditText) rootView.findViewById(R.id.editName);
        Tab_Activity.input_Sport = (EditText) rootView.findViewById(R.id.editSport);
        Tab_Activity.input_Gravity = (EditText) rootView.findViewById(R.id.editGravity);


        btn_get = (Button) rootView.findViewById(R.id.button_get);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // récupération des préférences utilisateurs
                //SharedPreferences settings = getSharedPreferences("DeepImpact_pref", 0);

                SharedPreferences settings = getActivity().getSharedPreferences("DeepImpact_pref", Context.MODE_PRIVATE);
                //String loginemail = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(PREF_USER_NAME, "");;
                Tab_Activity.input_Number.setText(settings.getString("Number", "").toString());
                Tab_Activity.input_Name.setText(settings.getString("Name", "").toString());
                Tab_Activity.input_Sport.setText(settings.getString("Sport", "").toString());
                Tab_Activity.input_Gravity.setText(settings.getString("Gravity_Limit", "").toString());

                Tab_Activity.phone_string = Tab_Activity.input_Number.getText().toString();
                Tab_Activity.name_string = Tab_Activity.input_Name.getText().toString();
                Tab_Activity.sport_string = Tab_Activity.input_Sport.getText().toString();

                String gravity_limit = Tab_Activity.input_Gravity.getText().toString();
                if (gravity_limit.equals("")){
                    Tab_Activity.G_limit=3.0;
                }
                else {
                    Tab_Activity.G_limit = Double.valueOf(gravity_limit);
                }

                String toast_message ="Settings retrieved";
                Toast msg = Toast.makeText(getContext(), toast_message, Toast.LENGTH_SHORT);
                msg.show();

            }
        });

        btn_save = (Button) rootView.findViewById(R.id.button_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String gravity_limit = Tab_Activity.input_Gravity.getText().toString();
                if (gravity_limit.equals("")){
                    Tab_Activity.G_limit=3.0;
                }
                else {
                    Tab_Activity.G_limit = Double.valueOf(gravity_limit);
                }

                Tab_Activity.phone_string = Tab_Activity.input_Number.getText().toString();
                Tab_Activity.name_string = Tab_Activity.input_Name.getText().toString();
                Tab_Activity.sport_string = Tab_Activity.input_Sport.getText().toString();

                // enregistrement des préférences utilisateurs
                //SharedPreferences settings = getSharedPreferences("DeepImpact_pref", 0);

                SharedPreferences settings = getActivity().getSharedPreferences("DeepImpact_pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Number",Tab_Activity.input_Number.getText().toString());
                editor.putString("Name",Tab_Activity.input_Name.getText().toString());
                editor.putString("Sport",Tab_Activity.input_Sport.getText().toString());
                editor.putString("Gravity_Limit",Tab_Activity.input_Gravity.getText().toString());
                editor.commit();


                String toast_message ="Settings saved";
                Toast msg = Toast.makeText(getContext(), toast_message, Toast.LENGTH_SHORT);
                msg.show();
            }
        });

        return rootView;

    }
}
