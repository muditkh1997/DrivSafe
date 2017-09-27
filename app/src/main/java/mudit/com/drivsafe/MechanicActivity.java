package mudit.com.drivsafe;

import android.*;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mudit.com.drivsafe.Adapters.GarageAdapter;
import mudit.com.drivsafe.PojoClass.Garage;

import static java.security.AccessController.getContext;

public class MechanicActivity extends AppCompatActivity {
    LocationManager locMan;
    ArrayList<Garage> garages=new ArrayList<>();
    final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    RecyclerView rvMechanic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic);
        DatabaseReference mdatabase;
        rvMechanic=(RecyclerView)findViewById(R.id.rvMechanic);
        locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        final GarageAdapter garageAdapter=new GarageAdapter(garages,MechanicActivity.this);
        rvMechanic.setLayoutManager(new LinearLayoutManager(this));
        rvMechanic.setAdapter(garageAdapter);
        if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) &&

                (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this,
                    new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    }, 234);
        } else {
            Location location=locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            mdatabase= FirebaseDatabase.getInstance().getReference();
            mdatabase.child("Mechanic").child("lat").setValue(location.getLatitude());
            mdatabase.child("Mechanic").child("long").setValue(location.getLongitude());
            String address=getAddress(location.getLatitude(),location.getLongitude());
            mdatabase.child("Mechanic").child("address").setValue(address);
            mdatabase.child("Mechanic").child("flag").setValue(1);

        }
        databaseReference.child("Mechanic_data").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Garage garage=dataSnapshot.getValue(Garage.class);
                garages.add(garage);
                garageAdapter.notifyDataSetChanged();
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

    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
//            add = add +  " "+obj.getCountryCode();
//            add = add +  " "+obj.getAdminArea();
            add = add +  " "+obj.getPostalCode();
//            add = add + " "+obj.getSubAdminArea();
            add = add + " "+obj.getLocality();
            add = add +  " "+obj.getCountryName();

            return add.toString();
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "no data";
        }
    }

    @Override
    protected void onPause() {
        databaseReference.child("Mechanic_data").removeValue();
        super.onPause();
    }
}


