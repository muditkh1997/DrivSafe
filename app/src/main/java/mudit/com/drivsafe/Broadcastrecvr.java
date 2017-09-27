package mudit.com.drivsafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by admin on 9/24/2017.
 */

public class Broadcastrecvr {
    Context context;
    TextToSpeech t1;
    BroadcastReceiver smsReceiver;
    public Broadcastrecvr() {

    }

    public Broadcastrecvr(Context context) {
        this.context = context;
        t1=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        initializeSMSReceiver();
        registerSMSReceiver();
    }

    public void initializeSMSReceiver(){
        BroadcastReceiver smsReceiver = new BroadcastReceiver() {
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
                        Log.d("My", "onReceive: " + msgs.length + "  pdus:" + pdus.length);
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            final String msgBody = msgs[i].getMessageBody();
                            Log.d("My", "onReceive: msg_from : " + msg_from);
                            String text = "Message recieved from";
                            Log.d("My", "onReceive: ");
                            String contactName = getContactName(msg_from);
                            Log.d("My", "onReceive: ");
                            String mesg = text + contactName;
                            Log.d("My", "onReceive: ");
                            t1.speak(mesg, TextToSpeech.QUEUE_FLUSH, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Yes");
                            builder.setMessage("Do you want to listen the message?");
//                            builder.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_logout_aler));
                            builder.setCancelable(false);
                            builder.setNegativeButton("CANCEL", null);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("My", "onReceive: msg_body : " + msgBody);

                                    t1.speak(msgBody, TextToSpeech.QUEUE_FLUSH, null);

                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                            nbutton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                            pbutton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

                        }
                    } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                    }
                }

            }
        };
    }
    public void registerSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        context.registerReceiver(smsReceiver, intentFilter);
    }
    private String getContactName(String phone){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }else {
            return "unknown number";
        }
    }
}