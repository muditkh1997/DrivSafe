package mudit.com.drivsafe.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import mudit.com.drivsafe.PojoClass.Info;
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
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment, container, false);
        DatabaseReference mdatabase;

        final GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        final int[] x = {0};

//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3)
//        });
        final LineGraphSeries<DataPoint> series=new LineGraphSeries<>(new DataPoint[]{
        });
        mdatabase= FirebaseDatabase.getInstance().getReference();
        mdatabase.child("Readings").limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Info info=dataSnapshot.getValue(Info.class);
                int pressure=info.getPressure();
                Log.d("My", "onChildAdded: "+info.getPressure()+" "+ info.getTemperature());
                if(x[0] < pressure) {
                    series.appendData(new DataPoint(info.getPressure(), Double.parseDouble(info.getTemperature())), true, 1000);
                    x[0] =pressure;
                    graph.addSeries(series);
                }

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
