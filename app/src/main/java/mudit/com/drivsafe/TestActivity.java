package mudit.com.drivsafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.CircleProgress;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tyreinfo1);
        ArcProgress arcProgress= (ArcProgress) findViewById(R.id.arc_progress);
        CircleProgress c;
    }
}
