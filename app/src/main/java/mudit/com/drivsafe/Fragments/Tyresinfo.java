package mudit.com.drivsafe.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import mudit.com.drivsafe.MechanicActivity;
import mudit.com.drivsafe.PojoClass.Info;
import mudit.com.drivsafe.R;


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

        View rootView= inflater.inflate(R.layout.fragment_tyresinfo, container, false);
        DatabaseReference mdatabase;
        final TextView tvTemp1,tvTemp2,tvTemp3,tvTemp4,tvPressure1,tvPressure2,tvPressure3,tvPressure4,tvStatus;

        tvPressure1= (TextView) rootView.findViewById(R.id.tvPressure1);
        tvPressure2= (TextView) rootView.findViewById(R.id.tvPressure2);
        tvPressure3= (TextView) rootView.findViewById(R.id.tvPressure3);
        tvPressure4= (TextView) rootView.findViewById(R.id.tvPressure4);
        tvTemp1= (TextView) rootView.findViewById(R.id.tvTemperature1);
        tvTemp2= (TextView) rootView.findViewById(R.id.tvTemperature2);
        tvTemp3= (TextView) rootView.findViewById(R.id.tvTemperature3);
        tvTemp4= (TextView) rootView.findViewById(R.id.tvTemperature4);
        tvStatus=(TextView)rootView.findViewById(R.id.tvStatus);
        final int[] flag1 = {0};
        final int[] flag = {0};
        mdatabase= FirebaseDatabase.getInstance().getReference();
        mdatabase.child("Readings").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Info info=dataSnapshot.getValue(Info.class);
                tvPressure1.setText("Pressure : "+info.getPressure());
                tvPressure2.setText("Pressure : "+info.getPressure());
                tvPressure3.setText("Pressure : "+info.getPressure());
                tvPressure4.setText("Pressure : "+info.getPressure());

                tvTemp1.setText("Temp : "+info.getTemperature());
                tvTemp2.setText("Temp : "+info.getTemperature());
                tvTemp3.setText("Temp : "+info.getTemperature());
                tvTemp4.setText("Temp : "+info.getTemperature());
                if(flag[0]==1){
                    tvStatus.setTextColor(Color.parseColor("#AD1457"));
                    tvStatus.setText("Low Health");
                }
                else{
                    tvStatus.setText("Healthy");
                    tvStatus.setTextColor(Color.parseColor("#1B5E20"));
                }
                int pressure= info.getPressure();
                if(pressure<20 && flag1[0] ==0){
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
