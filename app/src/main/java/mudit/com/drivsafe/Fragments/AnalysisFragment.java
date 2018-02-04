package mudit.com.drivsafe.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import mudit.com.drivsafe.PojoClass.Info;
import mudit.com.drivsafe.PojoClass.Road;
import mudit.com.drivsafe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisFragment extends Fragment {


    public AnalysisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.new_statistics, container, false);

//        DatabaseReference mdatabase;
//
//        final GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
//        final float[] x = {0};
//
////        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
////                new DataPoint(0, 1),
////                new DataPoint(1, 5),
////                new DataPoint(2, 3)
////        });
//        final LineGraphSeries<DataPoint> series=new LineGraphSeries<>(new DataPoint[]{
//        });
//        mdatabase= FirebaseDatabase.getInstance().getReference();
//        mdatabase.child("Readings").limitToLast(10).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Info info=dataSnapshot.getValue(Info.class);
//                Float pressure=Float.valueOf(info.getPressure());
//                Log.d("My", "onChildAdded: "+info.getPressure()+" "+ info.getTemperature());
//                if(x[0] < pressure) {
//                    series.appendData(new DataPoint(Double.parseDouble(info.getPressure()), Double.parseDouble(info.getTemperature())), true, 1000);
//                    x[0] =pressure;
//                    graph.addSeries(series);
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
        Button btRoadCondition;
        final TextView tvSpeed,tvDist,tvPressure,tvTemp;
        tvSpeed= (TextView) rootView.findViewById(R.id.tvSpeed);
        tvDist= (TextView) rootView.findViewById(R.id.tvDist);
        tvPressure= (TextView) rootView.findViewById(R.id.tvPressure);
        tvTemp= (TextView) rootView.findViewById(R.id.tvTemp);
        final GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        final GraphView graph1 = (GraphView) rootView.findViewById(R.id.graph1);

        final Spinner dropdown = (Spinner)rootView.findViewById(R.id.spinner1);
        String[] items = new String[]{"Excellent", "Good", "Poor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        final DatabaseReference mdatabase;
        mdatabase= FirebaseDatabase.getInstance().getReference();

        final LineGraphSeries<DataPoint> series=new LineGraphSeries<DataPoint>();
        final LineGraphSeries<DataPoint> series1=new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        graph1.addSeries(series1);

        final float[] x = {0};

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String roadCondition = null;
                if(position==0)
                    roadCondition="Excellent";
                if (position==1)
                    roadCondition="Good";
                if (position==2)
                    roadCondition="Poor";
                final int[] i = {0};
                final float[] sumSpeed = {0};
                final float[] sumDist = {0};
                final float[] sumPressure = {0};
                final float[] sumTemp = {0};
                series.resetData(new DataPoint[]{});
                series1.resetData(new DataPoint[]{});
                graph.removeAllSeries();
                graph1.removeAllSeries();
                final int[] flag = {0};

                mdatabase.child("Roads").child(roadCondition).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Road road=dataSnapshot.getValue(Road.class);
                        sumSpeed[0] = sumSpeed[0] +road.getSpeed();
                        sumDist[0] = sumDist[0] +road.getDistance();
                        sumPressure[0] = sumPressure[0] +road.getPressure();
                        sumTemp[0] = sumTemp[0] +road.getTemperature();
                        i[0] = i[0] +1;

                        flag[0] =1;
                        if(x[0] < road.getSpeed()) {
                            series.appendData(new DataPoint(road.getSpeed(), road.getPressure()), true, 1000);
                            series1.appendData(new DataPoint(road.getSpeed(), road.getTemperature()), true, 1000);
                            x[0] =road.getSpeed();

                        }
                        Log.d("My","onChildAdded: "+sumSpeed[0]+" "+sumDist[0]);
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
                    Handler handler=new Handler();
                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            tvSpeed.setText(String.valueOf(sumSpeed[0]/i[0]));
                            tvDist.setText(String.valueOf(sumDist[0]/i[0]));
                            tvPressure.setText(String.valueOf(sumPressure[0]/i[0]));
                            tvTemp.setText(String.valueOf(sumTemp[0]/i[0]));
                            graph.addSeries(series);
                            graph1.addSeries(series1);
                        }
                    };
                    handler.postDelayed(runnable,3000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        btRoadCondition.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int spninnerId=dropdown.getId();
//                String roadCondition = null;
//                if(spninnerId==0)
//                    roadCondition="Excellent";
//                if (spninnerId==1)
//                    roadCondition="Good";
//                if (spninnerId==2)
//                    roadCondition="Poor";
//                final int[] i = {0};
//                final float[] sumSpeed = {0};
//                final float[] sumDist = {0};
//                final float[] sumPressure = {0};
//                final float[] sumTemp = {0};
//
//                final int[] flag = {0};
//
//                mdatabase.child("Roads").child(roadCondition).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        Road road=dataSnapshot.getValue(Road.class);
//                        sumSpeed[0] = sumSpeed[0] +road.getSpeed();
//                        sumDist[0] = sumDist[0] +road.getDistance();
//                        sumPressure[0] = sumPressure[0] +road.getPressure();
//                        sumTemp[0] = sumTemp[0] +road.getTemperature();
//                        i[0] = i[0] +1;
//                        flag[0] =1;
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//                if(flag[0]==1){
//                    Handler handler=new Handler();
//                    Runnable runnable=new Runnable() {
//                        @Override
//                        public void run() {
//                            tvSpeed.setText(String.valueOf(sumSpeed[0]/i[0]));
//                            tvDist.setText(String.valueOf(sumDist[0]/i[0]));
//                            tvPressure.setText(String.valueOf(sumPressure[0]/i[0]));
//                            tvTemp.setText(String.valueOf(sumTemp[0]/i[0]));
//                        }
//                    };
//                }
//            }
//        });

        return rootView;


    }

}
