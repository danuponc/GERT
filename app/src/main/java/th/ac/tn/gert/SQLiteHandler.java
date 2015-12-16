package th.ac.tn.gert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danupon on 13/12/2558.
 */
public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";

    // For GERT Application
    private static final String PROFILE_PICTURE = "profile_picture";
    private static final String ID_NUMBER = "id_number";
    private static final String DOB = "dob";
    private static final String GENDER = "gender";
    private static final String NATIONALITY = "nationality";
    private static final String OCCUPATION = "occupation";
    private static final String HEIGHT = "height";
    private static final String WEIGHT = "weight";
    private static final String DISEASE = "disease";
    private static final String ALCOHOL = "alcohol";
    private static final String SMOKE = "smoke";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String MOBILE_PHONE = "mobile_phone";
    private static final String ADDRESS = "address";

    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table user
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + PROFILE_PICTURE + " TEXT," + ID_NUMBER + " TEXT,"
                + DOB + " TEXT," + GENDER + " TEXT,"
                + NATIONALITY + " TEXT," + OCCUPATION + " TEXT,"
                + HEIGHT + " TEXT," + WEIGHT + " TEXT,"
                + DISEASE + " TEXT," + ALCOHOL + " TEXT,"
                + SMOKE + " TEXT," + PHONE_NUMBER + " TEXT,"
                + MOBILE_PHONE + " TEXT," + ADDRESS + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        // Table appoint
        String CREATE_APPOINT_TABLE = "CREATE TABLE appoint ("+
                "id INTEGER PRIMARY KEY, ap_id TEXT,"+
                "ap_personal_id TEXT, ap_date TEXT, ap_time TEXT,"+
                "ap_subject TEXT, ap_detail TEXT, admin_name TEXT)";
        db.execSQL(CREATE_APPOINT_TABLE);



        Log.d(TAG, "Database tables created2");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String profile_picture,String id_number ,
            String dob,String gender,String nationality,String occupation,String height,String weight,String disease,String alcohol,String smoke,String phone_number,String mobile_phone,String address,String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At
        //For GERT
        values.put(PROFILE_PICTURE, profile_picture);
        values.put(ID_NUMBER, id_number);
        values.put(DOB, dob);
        values.put(GENDER, gender);
        values.put(NATIONALITY, nationality);
        values.put(OCCUPATION, occupation);
        values.put(HEIGHT, height);
        values.put(WEIGHT, weight);
        values.put(DISEASE, disease);
        values.put(ALCOHOL, alcohol);
        values.put(SMOKE, smoke);
        values.put(PHONE_NUMBER, phone_number);
        values.put(MOBILE_PHONE, mobile_phone);
        values.put(ADDRESS, address);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addAppoint(String ap_id,String ap_personal_id, String ap_date, String ap_time,String ap_subject, String ap_detail, String admin_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("ap_id",ap_id);
        v.put("ap_personal_id", ap_personal_id);
        v.put("ap_date", ap_date);
        v.put("ap_time", ap_time);
        v.put("ap_subject", ap_subject);
        v.put("ap_detail", ap_detail);
        v.put("admin_name", admin_name);
        long id = db.insert("appoint",null,v);
        db.close();
        Log.d(TAG, "New appoint inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));

            // For GERT
            user.put("profile_picture", cursor.getString(4));
            user.put("id_number", cursor.getString(5));
            user.put("dob", cursor.getString(6));
            user.put("gender", cursor.getString(7));
            user.put("nationality", cursor.getString(8));
            user.put("occupation", cursor.getString(9));
            user.put("height", cursor.getString(10));
            user.put("weight", cursor.getString(11));
            user.put("disease", cursor.getString(12));
            user.put("alcohol", cursor.getString(13));
            user.put("smoke", cursor.getString(14));
            user.put("phone_number", cursor.getString(15));
            user.put("mobile_phone", cursor.getString(16));
            user.put("address", cursor.getString(17));

            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.delete("appoint", null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public List<AppointData> getAllAppoint()
    {
        List<AppointData> dataList = new ArrayList<AppointData>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM appoint", null);

        if (cursor.moveToFirst())
        {
            do
            {
                AppointData data = new AppointData(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)); dataList.add(data);
            }

            while (cursor.moveToNext());
        }
        return dataList;
    }


}
