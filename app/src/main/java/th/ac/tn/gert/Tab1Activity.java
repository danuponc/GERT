package th.ac.tn.gert;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.ac.tn.gert.SQLiteHandler;
import th.ac.tn.gert.SessionManager;
import th.ac.tn.gert.DownloadImageTask;

import th.ac.tn.gert.BackgroundService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tab1Activity extends AppCompatActivity {
    //private static final String TAG = "BackgroundService";
    //private int counter = 1;
    private SessionManager session;
    private SQLiteHandler db;
    private TextView pName;
    private TextView pAge;
    private TextView pWeight;
    private TextView pHeight;
    private TextView pOccupation;
    private TextView pAddress;

    private Button btnLogout;

    private ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1);

        this.setTitle("GERT | โรงเรียนเตรียมอุดมศึกษา ภาคเหนือ");

        /*iv = (ImageView) findViewById(R.id.imageView2);
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        params.width = 200;
        params.height = 200;
        iv.setLayoutParams(params);*/

        pName = (TextView) findViewById(R.id.pName);
        pAge = (TextView) findViewById(R.id.pAge);
        pWeight = (TextView) findViewById(R.id.pWeight);
        pHeight = (TextView) findViewById(R.id.pHeight);
        pOccupation = (TextView) findViewById(R.id.pOccupation);
        pAddress = (TextView) findViewById(R.id.pAddress);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        List<AppointData> appoint_data = db.getAllAppoint();

        // Get Image
        String url = "http://www.danupon.net/tn4/images/personal/"+user.get("uid")+".jpg";
        new DownloadImageTask((ImageView)findViewById(R.id.imageView2)).execute(url);

        String name = "ชื่อ : "+user.get("name");
        String age = "อายุ : "+user.get("id_number")+ " ปี";
        String weight = "น้ำหนัก : "+user.get("weight")+" ก.ก.";
        String height = "ส่วนสูง : "+user.get("height")+" ซ.ม.";
        String occupation = "อาชีพ : "+user.get("occupation");
        String address = "ที่อยู่ : "+user.get("address");
        //String email = user.get("email");

        // Displaying the user details on the screen
        pName.setText(name);
        pAge.setText(age);;
        pWeight.setText(weight);
        pHeight.setText(height);
        pOccupation.setText(occupation);
        pAddress.setText(address);


        // Layout prepare
        LinearLayout ou = (LinearLayout) findViewById(R.id.layout_x);


        // Appoint List
        if(!(appoint_data == null)) {
            for (AppointData a : appoint_data) {
                //appoint_data.get(i);

                TextView tv_subject = new TextView(this);
                TextView tv_ap_date = new TextView(this);
                TextView tv_ap_time = new TextView(this);
                TextView tv_ap_detail = new TextView(this);
                TextView tv_admin = new TextView(this);
                TextView blank = new TextView(this);

                tv_subject.setText(a.getApSubject());
                tv_ap_date.setText(a.getApDate());
                tv_ap_time.setText(a.getAptime());
                tv_ap_detail.setText(a.getApDetail());
                tv_admin.setText(a.getAdminName());
                blank.setText("");

                ou.addView(tv_subject);
                ou.addView(tv_ap_date);
                ou.addView(tv_ap_time);
                ou.addView(tv_ap_detail);
                ou.addView(tv_admin);
                ou.addView(blank);
            }
        }

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                logoutUser();
                finish();
            }
        });

        getAppoint();
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Tab1Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getAppoint(){

    }

}
