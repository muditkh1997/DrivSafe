package mudit.com.drivsafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class actii extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actii);
        Broadcastrecvr broadcastrecvr=new Broadcastrecvr(this);
        broadcastrecvr.initializeSMSReceiver();
        broadcastrecvr.registerSMSReceiver();
    }
}
