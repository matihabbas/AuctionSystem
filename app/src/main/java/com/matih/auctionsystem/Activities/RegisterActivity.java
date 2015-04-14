package com.matih.auctionsystem.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.matih.auctionsystem.Classes.User;
import com.matih.auctionsystem.Managers.InitManager;
import com.matih.auctionsystem.R;


public class RegisterActivity extends Activity {

    private static final String DEBUG_TAG = RegisterActivity.class.getSimpleName();

    private Button btn_Register;
    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_Register = (Button)findViewById(R.id.button_Register);
        editTextName = (EditText)findViewById(R.id.editText_registerName);
        editTextUsername = (EditText)findViewById(R.id.editText_registerUsername);
        editTextPassword = (EditText)findViewById(R.id.editText_Password);
        editTextPasswordConfirm = (EditText)findViewById(R.id.editText_registerPasswordConfirm);

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    /*
    * Register New user
    * */
    private void registerUser(){
        String name = editTextName.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordConfirm = editTextPasswordConfirm.getText().toString();

        if(name.isEmpty()) {
            editTextName.setError(getResources().getString(R.string.error_no_name));
            return;
        }

        if(username.isEmpty() ||  !Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            editTextUsername.setError(getResources().getString(R.string.error_invalid_email));
            return;
        }

        if(password.isEmpty() || passwordConfirm.isEmpty() || (!password.equals(passwordConfirm))){
            editTextPasswordConfirm.setError(getResources().getString(R.string.error_no_password_match));
            return;
        }

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setUserId(InitManager.getSharedInstance().getDbManager().addUser(InitManager.getSharedInstance().getDatabase(), user));

        if(user.getUserId() > -1){
            Toast.makeText(this, getResources().getString(R.string.msg_register_success), Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("userId", user.getUserId());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        else if(user.getUserId() == -2){
            Log.i(DEBUG_TAG, "User already exists");
            editTextUsername.setError(getResources().getString(R.string.msg_register_fail_already_exists));
            Toast.makeText(this, getResources().getString(R.string.msg_register_fail_already_exists), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, getResources().getString(R.string.msg_register_fail), Toast.LENGTH_SHORT).show();
        }
    }
}
