package mudit.com.drivsafe;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

import mudit.com.drivsafe.Adapters.MyFragmentPageAdapter;
import mudit.com.drivsafe.Fragments.Tyresinfo;
import mudit.com.drivsafe.PojoClass.Road;
import mudit.com.drivsafe.PojoClass.SQLiteAsyncTask;

import static mudit.com.drivsafe.R.id.parent;

public class Main2Activity extends AppCompatActivity {
    ViewPager mviewPager;
    FloatingActionButton fabSearch,fabMusic;
    private BroadcastReceiver smsReceiver;
    TextToSpeech t1;
    private TabLayout allTabs;
    String phoneno="";
    MyFragmentPageAdapter myFragmentPageAdapter;
    DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fabSearch= (FloatingActionButton) findViewById(R.id.fab);
        fabMusic= (FloatingActionButton) findViewById(R.id.fab1);

        allTabs=(TabLayout)findViewById(R.id.tabs);
        mviewPager=(ViewPager)findViewById(R.id.viewPager);
        mviewPager.setOffscreenPageLimit(2);
        myFragmentPageAdapter=new MyFragmentPageAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(myFragmentPageAdapter);
        allTabs.setupWithViewPager(mviewPager);
        allTabs.getTabAt(0).setIcon(R.drawable.ic_library_books_pink_800_24dp);
        allTabs.getTabAt(1).setIcon(R.drawable.ic_equalizer_pink_800_24dp);
        allTabs.getTabAt(2).setIcon(R.drawable.ic_place_pink_800_24dp);
        t1=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        Log.d("My", "textToSpeach: ");
        initializeSMSReceiver();
        registerSMSReceiver();

        mdatabase= FirebaseDatabase.getInstance().getReference();

        //accelerometer

//        updateCondition();

        SongsDataBase mDataBase=new SongsDataBase(this);
        SQLiteAsyncTask task = new SQLiteAsyncTask(this);
        task.execute(getApplicationContext());
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                LayoutInflater li=Main2Activity.this.getLayoutInflater();
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
                nbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            }
        });

        fabMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(Main2Activity.this, PlaylistActivity.class);
//                i.putExtra("allSongs",1);
//                startActivity(i);
            }
        });
//        allTabs.getTabAt(0).setIcon(R.drawable.camera);
//        allTabs.getTabAt(1).setIcon(R.drawable.newsfeed);
//        allTabs.getTabAt(2).setIcon(R.drawable.chat);
//        allTabs.getTabAt(3).setIcon(R.drawable.myprofile);
    }
    private void initializeSMSReceiver(){
        smsReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        Log.d("My", "onReceive: "+msgs.length+"  pdus:"+pdus.length);
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            final String msgBody = msgs[i].getMessageBody();
                            Log.d("My", "onReceive: msg_from : "+msg_from);
                            phoneno=msg_from;
                            String text="Message recieved from";
                            Log.d("My", "onReceive: ");
                            String contactName=getContactName(msg_from);
                            Log.d("My", "onReceive: ");
                            String mesg=text+contactName;
                            Log.d("My", "onReceive: ");
                            t1.speak(mesg, TextToSpeech.QUEUE_FLUSH, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                            builder.setTitle("Yes");
                            builder.setMessage("Do you want to listen the message?");
//                            builder.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_logout_aler));
                            builder.setCancelable(false);
                            builder.setNegativeButton("CANCEL", null);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("My", "onReceive: msg_body : "+msgBody);
                                    t1.speak(msgBody, TextToSpeech.QUEUE_FLUSH, null);
                                    isTTSSpeaking();

                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                            nbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                            pbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

                        }
                    } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                    }
                }

            }
        };
    }

    public void isTTSSpeaking(){

        final Handler h =new Handler();

        Runnable r = new Runnable() {

            public void run() {

                if (!t1.isSpeaking()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                    builder.setTitle("Yes");
                    builder.setMessage("Do you want to reply to this message?");
//                            builder.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_logout_aler));
                    builder.setCancelable(false);
                    builder.setNegativeButton("CANCEL", null);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("My", "onClick: ");
                            Bundle args=new Bundle();
                            args.putString("phoneno",phoneno);
                            DialogFragment1 dialogFragment1=new DialogFragment1();
                            dialogFragment1.setArguments(args);
                            dialogFragment1.show(getFragmentManager(),"");

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

                }
                else {
                    h.postDelayed(this, 1000);
                }

            }
        };

        h.postDelayed(r, 1000);
    }


    private void registerSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }
    private String getContactName(String phone){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }else {
            return "unknown number";
        }
    }

    public void updateCondition(){
        final float[] initialspeed = {0};
        final float[] speed = new float[1];
        final float[] dist = new float[1];
        final Handler h =new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                final int[] flag = {0};
                final SensorManager sm= (SensorManager) getSystemService(SENSOR_SERVICE);
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
                            TextView pressure= (TextView) myFragmentPageAdapter.getItem(2).getActivity().findViewById(R.id.tvPressure1);
                            Float pressure1=Float.valueOf(pressure.getText().toString().replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
                            TextView temp= (TextView) myFragmentPageAdapter.getItem(2).getActivity().findViewById(R.id.tvTemperature1);
                            Float temp1=Float.valueOf(temp.getText().toString().replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
                            Road road=new Road(speed[0],dist[0],pressure1,temp1);
                            Log.d("My", "onSensorChanged: "+y);
                            if(y<=0.5){
                                String key=mdatabase.child("Roads").child("Excellent").push().getKey();
                                mdatabase.child("Roads").child("Excellent").child(key).setValue(road);
                            }
                            if(y>0.5 && y<=1.5){
                                String key=mdatabase.child("Roads").child("Good").push().getKey();
                                mdatabase.child("Roads").child("Good").child(key).setValue(road);
                            }
                            if(y>1.5){
                                String key=mdatabase.child("Roads").child("Poor").push().getKey();
                                mdatabase.child("Roads").child("Poor").child(key).setValue(road);
                            }
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    }
                };
                sm.registerListener(sel,accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
                h.postDelayed(this, 1000);

            }
        };
        h.postDelayed(runnable,1000);


    }


}


//class TTSActivity extends Activity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
//private TextToSpeech mTts;
//
//
//
//    private void speak(String text) {
//        if(text != null) {
//        HashMap<String, String> myHashAlarm = new HashMap<String, String>();
//        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
//        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");
//        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
//        }
//        }
//// Fired after TTS initialization
//public void onInit(int status) {
//        if(status == TextToSpeech.SUCCESS) {
//        mTts.setOnUtteranceCompletedListener(this);
//        }
//        }
//// It's callback
//public void onUtteranceCompleted(String utteranceId) {
//         //utteranceId == "SOME MESSAGE"
//        }
//}
