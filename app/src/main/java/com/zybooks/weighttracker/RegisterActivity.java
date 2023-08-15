package com.zybooks.weighttracker;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zybooks.weighttracker.database.Database;
import com.zybooks.weighttracker.helpers.UserHelper;

import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    EditText mUsername;
    EditText mPassword;
    EditText mConfirmPassword;
    Button register;

    Database db;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new Database(RegisterActivity.this);
        sharedPref = getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.registerBtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();

                // Get error strings
                String usernameLength = getResources().getString(R.string.errorUserlength);
                String passwordLength = getResources().getString(R.string.errorPasswordlength);
                String confirmPasswordMismatch = getResources().getString(R.string.errorPasswordmismatch);

                // Check values
                if (username.length() < 5) {
                    showMessage(usernameLength);
                } else if (password.length() < 8) {
                    showMessage(passwordLength);
                } else if (!password.equals(confirmPassword)) {
                    showMessage(confirmPasswordMismatch);
                } else {
                    try {
                        register(username, password);
                    } catch (Exception e) {
                        Log.d(TAG, "onClick: " + e.toString());
                    }
                }
            }
        });
    }

    private void showMessage(String text) {
        Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void register(String username, String password) throws NoSuchAlgorithmException {
        String userRegistered = getResources().getString(R.string.userRegistered);
        String errorUserExists = getResources().getString(R.string.errorUsernameExists);

        // Check if user exists
        Boolean userExists = db.doesUserExist(username);

        if (userExists) {
            showMessage(errorUserExists);
            return;
        }

        String passwordHash = UserHelper.hash(password);
        db.addUser(username, passwordHash);
        showMessage(userRegistered);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.apply();

        // Load MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}