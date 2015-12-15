package th.ac.tn.gert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import th.ac.tn.gert.BackgroundService;

public class Tab1Activity extends AppCompatActivity {
    private static final String TAG = "BackgroundService";
    private int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1);
    }


}
