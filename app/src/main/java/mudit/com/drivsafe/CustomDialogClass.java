package mudit.com.drivsafe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.R.string.no;
import static android.app.Activity.RESULT_OK;

/**
 * Created by admin on 11/23/2017.
 */

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public TextView cancel, send,voiceInput;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        voiceInput= (TextView) findViewById(R.id.voiceInput);



        cancel = (TextView) findViewById(R.id.tvCancel);
        send = (TextView) findViewById(R.id.tvSend);
        cancel.setOnClickListener(this);
        send.setOnClickListener(this);


    }
    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");

        if(intent.resolveActivity(getContext().getPackageManager()) != null) {
            c.startActivityForResult(intent,10);
        }else{
            Toast.makeText(getContext(),"Sorry, this function is not available on your device!!",Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSend:
                c.finish();
                break;
            case R.id.tvCancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

}
