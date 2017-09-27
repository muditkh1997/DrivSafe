package mudit.com.drivsafe.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import mudit.com.drivsafe.Main2Activity;
import mudit.com.drivsafe.MapsActivity;
import mudit.com.drivsafe.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    public GoogleApiClient mGoogleApiClient;
    //    public GeoDataClient mGeoDataClient;
    TextView etStart, etDest;
    ImageButton imgMyLocation,imgStartNavigation;
    LocationManager locMan;
    LocationListener locLis;
    GoogleMap mMap;
    Place start,destination;
    Marker stMarker=null,dtMarker=null,currentMarker=null;
    Circle myLocation=null;
    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        etStart = (TextView) rootView.findViewById(R.id.etstName);
        etDest = (TextView) rootView.findViewById(R.id.etdtName);
        imgMyLocation = (ImageButton) rootView.findViewById(R.id.imgMyLocation);
        imgStartNavigation = (ImageButton) rootView.findViewById(R.id.imgStartNavigation);

        locMan = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, 2);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });
        etDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, 3);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

            }
        });
        imgMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) &&

                        (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] {
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            }, 234);
                } else {
                    Location location=locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Log.d("My", "onClick: "+location.getLatitude()+" "+location.getLongitude());
                    LatLng sydney = new LatLng(location.getLatitude(),location.getLongitude());
                    if(myLocation!=null)
                        myLocation.remove();
                    myLocation=mMap.addCircle(new CircleOptions().center(sydney).radius(40).fillColor(Color.rgb(72, 133, 237)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            try {
                                LatLngBounds latlangbounds=new LatLngBounds(latLng,latLng);
                                PlacePicker.IntentBuilder intentBuilder =
                                        new PlacePicker.IntentBuilder();
                                intentBuilder.setLatLngBounds(latlangbounds);
                                Intent intent = intentBuilder.build(getActivity());
                                startActivityForResult(intent,1);

                            } catch (GooglePlayServicesRepairableException
                                    | GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }


            }
        });
        imgStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri gmmIntentUri = Uri.parse("google.navigation:q="+etDest.getText().toString());
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+start.getLatLng().latitude+","+start.getLatLng().longitude
                                +"&daddr="+destination.getLatLng().latitude+","+destination.getLatLng().longitude));
                startActivity(intent);
            }
        });
        locLis=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("My", "lat: "+location.getLatitude());
                Log.d("My", "long: "+location.getLongitude());
                Log.d("My", "provider: "+location.getProvider());
                Log.d("My", "accuracy: "+location.getAccuracy());
                Log.d("My", "alt: " + location.getAltitude());
                Log.d("My", "speed: " + location.getSpeed());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        return rootView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    LatLngBounds latlangbounds=new LatLngBounds(latLng,latLng);
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(latlangbounds);
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent,1);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {

        if (requestCode == 1
                && resultCode == RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, getActivity());

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }
            Log.d("My", "onActivityResult: "+name+" "+"address"+" "+address+" "+ Html.fromHtml(attributions));
//            mViewName.setText(name);
//            mViewAddress.setText(address);
//            mViewAttributions.setText(Html.fromHtml(attributions));
            etDest.setText(name);
            destination=place;
            LatLng sydney = place.getLatLng();
            if(dtMarker!=null)
                dtMarker.remove();
            dtMarker=mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

        }
        if (requestCode == 2|| requestCode==3) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                String attributions = PlacePicker.getAttributions(data);
                if (attributions == null) {
                    attributions = "";
                }
                Log.d("My", "onActivityResult: "+name+" "+"address"+" "+address+" "+Html.fromHtml(attributions));
//            mViewName.setText(name);
//            mViewAddress.setText(address);
//            mViewAttributions.setText(Html.fromHtml(attributions));
                if(requestCode==2){
                    start=place;
                    etStart.setText(name);
                    LatLng sydney = place.getLatLng();
                    if(stMarker!=null)
                        stMarker.remove();
                    stMarker=mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
                }
                else {
                    destination=place;
                    etDest.setText(name);
                    LatLng sydney = place.getLatLng();
                    if(dtMarker!=null)
                        dtMarker.remove();
                    dtMarker=mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
                }

            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("My", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
