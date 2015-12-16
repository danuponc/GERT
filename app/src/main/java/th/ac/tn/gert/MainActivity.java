package th.ac.tn.gert;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.WindowDecorActionBar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import th.ac.tn.gert.R;
import th.ac.tn.gert.AppConfig;
import th.ac.tn.gert.AppController;
import th.ac.tn.gert.SQLiteHandler;
import th.ac.tn.gert.SessionManager;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageButton btnLogin;
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        inputUsername = (EditText) findViewById(R.id.userName);
        inputPassword = (EditText) findViewById(R.id.passWord);
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        btnLogin.setBackgroundColor(Color.TRANSPARENT);

        //Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(MainActivity.this, Tab1Activity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!username.isEmpty()) {
                    // login user
                    checkLogin(username, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "ป้อนข้อมูลไม่ครบ !", Toast.LENGTH_LONG)
                            .show();
                }
                /*
                Intent i = new Intent(MainActivity.this,AppActivity.class);
                startActivity(i);
                finish();*/
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN_TMP+"?user="+email+"&pass="+password, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");

                        // For GERT appoint
                        //JSONObject ap = jObj.getJSONObject("apObject");
                        JSONArray ap = jObj.getJSONArray("apObject");
                        Log.d(TAG, "-----------------------"+String.valueOf(ap.length()));

                        for(int i=0;i<ap.length();i++){
                            JSONObject rec = ap.getJSONObject(i);
                            String ap_id = rec.getString("apId");
                            String ap_personal_id = rec.getString("apPersonalId");
                            String ap_date = rec.getString("apAppointDate");
                            String ap_time = rec.getString("apAppointTime");
                            String ap_subject = rec.getString("apAppointSubject");
                            String ap_detail = rec.getString("apAppointDetail");
                            String admin_name = rec.getString("adminName");
                            //Log.d(TAG, "+++ " + i + " : ");

                            db.addAppoint(ap_id,ap_personal_id,ap_date,ap_time,ap_subject,ap_detail,admin_name);
                        }

                        // For GERT
                        String profile_picture = jObj.getString("profilePicture");
                        String id_number = jObj.getString("age");
                        String dob = jObj.getString("dateOfBirth");
                        String gender = jObj.getString("gender");
                        String nationality = jObj.getString("nationality");
                        String occupation = jObj.getString("occupation");
                        String height = jObj.getString("height");
                        String weight = jObj.getString("weight");
                        String disease = jObj.getString("disease");
                        String alcohol = jObj.getString("alcohol");
                        String smoke = jObj.getString("smoke");
                        String phone_number = jObj.getString("phoneNumber");
                        String mobile_phone = jObj.getString("mobilePhone");
                        String address = jObj.getString("address");

                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, profile_picture ,id_number ,dob ,gender ,nationality ,occupation ,height ,weight ,disease ,alcohol ,smoke ,phone_number ,mobile_phone ,address ,created_at);

                        // Launch main activity
                        Intent intent = new Intent(MainActivity.this,
                                Tab1Activity.class);
                        startActivity(intent);
                        MainActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        //AppController.getInstance().addToRequestQueue(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}