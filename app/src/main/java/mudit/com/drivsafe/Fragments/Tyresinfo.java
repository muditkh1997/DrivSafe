package mudit.com.drivsafe.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import mudit.com.drivsafe.Main2Activity;
import mudit.com.drivsafe.MechanicActivity;
import mudit.com.drivsafe.PojoClass.Info;
import mudit.com.drivsafe.PojoClass.Road;
import mudit.com.drivsafe.R;

import static android.content.Context.SENSOR_SERVICE;
import static java.lang.Double.parseDouble;


public class Tyresinfo extends Fragment {

    public Tyresinfo() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView= inflater.inflate(R.layout.fragment_tyreinfo1, container, false);
        DatabaseReference mdatabase = null;
        final TextView tvTemp1;
        final TextView tvTemp2;
        final TextView tvTemp3;
        final TextView tvTemp4;
        final TextView tvPressure1;
        final TextView tvPressure2;
        final TextView tvPressure3;
        final TextView tvPressure4;
        final TextView tvStatus;
        final View tvSearch,tvMaps;
        final SwitchCompat tvSpeedAnalysis;
        ArcProgress progress,progress1 = null;
        tvPressure1= (TextView) rootView.findViewById(R.id.tvPressure1);
//        tvPressure2= (TextView) rootView.findViewById(R.id.tvPressure2);
//        tvPressure3= (TextView) rootView.findViewById(R.id.tvPressure3);
//        tvPressure4= (TextView) rootView.findViewById(R.id.tvPressure4);
        tvTemp1= (TextView) rootView.findViewById(R.id.tvTemperature1);
//        tvTemp2= (TextView) rootView.findViewById(R.id.tvTemperature2);
//        tvTemp3= (TextView) rootView.findViewById(R.id.tvTemperature3);
//        tvTemp4= (TextView) rootView.findViewById(R.id.tvTemperature4);
        tvStatus=(TextView)rootView.findViewById(R.id.tvStatus);
        tvSpeedAnalysis= (SwitchCompat) rootView.findViewById(R.id.tvSpeedAnalysis);
        tvSearch=rootView.findViewById(R.id.tvSearch);
        tvMaps=rootView.findViewById(R.id.tvMaps);
        progress= (ArcProgress) rootView.findViewById(R.id.arc_progress);
        progress1= (ArcProgress) rootView.findViewById(R.id.arc_progress1);

        mdatabase= FirebaseDatabase.getInstance().getReference();

        final DatabaseReference finalMdatabase = mdatabase;
        tvSpeedAnalysis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                final Handler h =new Handler();
                Runnable runnable = null;
                if(isChecked){
                    Log.d("My", "onCheckedChanged: ");
                    final float[] initialspeed = {0};
                    final float[] speed = new float[1];
                    final float[] dist = new float[1];
                    runnable=new Runnable() {
                        @Override
                        public void run() {
                            final int[] flag = {0};
                            final SensorManager sm= (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
                            final Sensor accelSensor=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                            final SensorEventListener sel =new SensorEventListener() {
                                @Override
                                public void onSensorChanged(SensorEvent event) {
                                    if(flag[0] ==0) {
                                        float[] v = event.values;
                                        Log.d("My", "onSensorChanged: " + v[0] + "," + v[1] + "," + v[2]);
                                        speed[0] = initialspeed[0] +Math.abs(v[2])*1;
                                        dist[0] =speed[0];
                                        initialspeed[0] =speed[0];
                                        flag[0] =1;
                                        float y=Math.abs(v[1]);
                                        y = Math.abs(y - 10);
                                        TextView pressure= tvPressure1;
                                        Float pressure1=Float.valueOf(pressure.getText().toString().replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
                                        TextView temp= tvTemp1;
                                        Float temp1=Float.valueOf(temp.getText().toString().replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
                                        Road road=new Road(speed[0],dist[0],pressure1,temp1);
                                        Log.d("My", "onSensorChanged: "+y);
                                        if(y<=0.5){
                                            String key= finalMdatabase.child("Roads").child("Excellent").push().getKey();
                                            finalMdatabase.child("Roads").child("Excellent").child(key).setValue(road);
                                        }
                                        if(y>0.5 && y<=1.5){
                                            String key= finalMdatabase.child("Roads").child("Good").push().getKey();
                                            finalMdatabase.child("Roads").child("Good").child(key).setValue(road);
                                        }
                                        if(y>1.5){
                                            String key= finalMdatabase.child("Roads").child("Poor").push().getKey();
                                            finalMdatabase.child("Roads").child("Poor").child(key).setValue(road);
                                        }
                                    }
                                }

                                @Override
                                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                }
                            };
                            sm.registerListener(sel,accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
                            Log.d("hander", "run: ");
                            if(tvSpeedAnalysis.isChecked()){
                                h.postDelayed(this, 1000);
                            }

                        }
                    };
                    h.postDelayed(runnable,1000);


                }
                if(!isChecked){
                    h.removeCallbacksAndMessages(null);

                }
            }
        });
//        tvSpeedAnalysis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                }
//
//        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater li=getActivity().getLayoutInflater();
                final View itemView=li.inflate(R.layout.activity_custom_dialog,null);
                builder.setView(itemView)
                        .setPositiveButton("Find", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText etPlace=(EditText)itemView.findViewById(R.id.etPlace);
                                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+etPlace.getText().toString());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }
                        })
                        .setNegativeButton("Cancel",null);
                AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            }
        });
        tvMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 28.630594, 77.371857);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getActivity().startActivity(intent);

            }
        });
        final int[] flag1 = {0};
        final int[] flag = {0};
        final ArcProgress finalProgress = progress;
        final ArcProgress finalProgress1 = progress1;
        mdatabase.child("Readings").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Info info=dataSnapshot.getValue(Info.class);
                Float pressure1= Float.valueOf(info.getPressure());
                Float temp1= Float.valueOf(info.getTemperature());
                finalProgress.setProgress(Math.round(pressure1));
                finalProgress1.setProgress(Math.round(temp1));
//                finalProgress1.setProgress(Integer.parseInt(String.valueOf(Math.ceil(parseDouble(info.getTemperature())))));

//                finalProgress1.setProgress(Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(info.getTemperature().toString())))));

                tvPressure1.setText(info.getPressure());

//                tvPressure2.setText("Pressure : "+info.getPressure());
//                tvPressure3.setText("Pressure : "+info.getPressure());
//                tvPressure4.setText("Pressure : "+info.getPressure());

                tvTemp1.setText(info.getTemperature());
//                tvTemp2.setText("Temp : "+info.getTemperature());
//                tvTemp3.setText("Temp : "+info.getTemperature());
//                tvTemp4.setText("Temp : "+info.getTemperature());
                if(flag[0]==1){
                    tvStatus.setTextColor(Color.parseColor("#AD1457"));
                    tvStatus.setText("Low Health");
                }
                else{
                    tvStatus.setText("Healthy");
                    tvStatus.setTextColor(Color.parseColor("#1B5E20"));
                }
                Float pressure= Float.valueOf(info.getPressure());
                if((pressure1<140 || temp1>340) && flag1[0] ==0){
                    flag1[0] =1;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Alert: Low Health of Tyres");
                    builder.setMessage("Do you want to search for garages?");
//                            builder.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_logout_aler));
                    builder.setCancelable(false);
                    builder.setNegativeButton("CANCEL", null);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i=new Intent(getActivity(), MechanicActivity.class);
                            startActivity(i);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    tvStatus.setTextColor(Color.parseColor("#AD1457"));
                    tvStatus.setText("Low Health");
                }
                else
                    flag[0]=1;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;

    }


}
