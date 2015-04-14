package com.matih.auctionsystem.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.matih.auctionsystem.Classes.User;
import com.matih.auctionsystem.Managers.InitManager;
import com.matih.auctionsystem.Managers.ReceiverBot;
import com.matih.auctionsystem.R;

public class LoginActivity extends Activity {

    private static final String DEBUG_TAG = LoginActivity.class.getSimpleName();
    private static final int REGISTER_ACTIVITY_REQUEST_CODE = 1;

    // Shared preferences keys
    public static final String LOGIN_PREF_NAME = "login";
    public static final String LOGIN_PREF_KEY_USER_ID = "userid";
    private static final String LOGIN_PREF_KEY_FIRST_RUN = "firstRun";

    // Intents
    private PendingIntent pendingIntent;

    // View Objects
    private EditText editText_Username;
    private EditText editText_Password;
    private Button btn_Login;
    private Button btn_Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Database
        InitManager.getSharedInstance().initInstance(getApplicationContext());

        // Get View objects
        editText_Username = (EditText)findViewById(R.id.editText_Username);
        editText_Password = (EditText)findViewById(R.id.editText_Password);
        btn_Login = (Button)findViewById(R.id.button_Login);
        btn_Register = (Button)findViewById(R.id.button_create_new_account);

        // Set Click Listeners
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editText_Username.getText().toString();
                String password = editText_Password.getText().toString();

                long userId = -1;

                if((userId = authenticateUser(username, password)) != -1){
                    loginUser(userId);
                }
                else{
                    Log.i(DEBUG_TAG, "Invalid credentials");
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_login_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegisterActivity();
            }
        });

        // Read app preferences
        SharedPreferences loginPreferences = getSharedPreferences(LOGIN_PREF_NAME, Context.MODE_PRIVATE);
        if(loginPreferences != null) {

            // Is it first run
            if(loginPreferences.getBoolean(LOGIN_PREF_KEY_FIRST_RUN, true)){
                // Create Tables and Data from SQL schema file
                InitManager.getSharedInstance().getDbManager().executeSchemaFile(InitManager.getSharedInstance().getDatabase(), getApplicationContext(), "schema.sql");

                // Set app has been run once
                loginPreferences.edit().putBoolean(LOGIN_PREF_KEY_FIRST_RUN, false).apply();

                // Schedule bidding bot
                Intent alarmIntent = new Intent(getApplicationContext(), ReceiverBot.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
                startBot();
            }
            long userIdLoggedIn = -1;

            try{
                userIdLoggedIn = loginPreferences.getLong(LOGIN_PREF_KEY_USER_ID, -1);
            }
            catch (Exception ex){
                Log.e(DEBUG_TAG, "Error getting userId");
            }
            if(userIdLoggedIn != -1){
                loginUser(userIdLoggedIn);
            }
        }
    }

    /*
    * Start Bidding bot
    * */
    public void startBot() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Every 30 seconds
        long interval = 30000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Bidding bot initiated", Toast.LENGTH_SHORT).show();
    }

    /*
    * Authenticate user from DB
    * */
    private long authenticateUser(String username, String password){
        return InitManager.getSharedInstance().getDbManager().authenticateUser(InitManager.getSharedInstance().getDatabase(), username, password);
    }

    /*
    * Perform Login
    * */
    private void loginUser(long userId){
        Log.i(DEBUG_TAG, "Logging in userId: " + userId);

        SharedPreferences loginPreferences = getSharedPreferences(LOGIN_PREF_NAME, Context.MODE_PRIVATE);
        loginPreferences.edit().putLong(LOGIN_PREF_KEY_USER_ID, userId).apply();

        InitManager.getSharedInstance().setCurrentUserId(userId);

        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainActivityIntent.putExtra("userId", userId);
        startActivity(mainActivityIntent);
        finish();
    }

    /*
    * Launch User Registeration Activity
    * */
    private void launchRegisterActivity(){
        Intent registerActivityIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(registerActivityIntent, REGISTER_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==  REGISTER_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                long userId = data.getLongExtra("userId", -1);
                User user = InitManager.getSharedInstance().getDbManager().getUser(InitManager.getSharedInstance().getDatabase(), userId);
                if((user != null) && (user.getUserId() != -1)){
                    loginUser(userId);
                }
                else{
                    Log.e(DEBUG_TAG, "Error logging in");
                    Toast.makeText(this, getResources().getString(R.string.msg_login_error_other), Toast.LENGTH_SHORT);
                }
            }
        }
    }
}
