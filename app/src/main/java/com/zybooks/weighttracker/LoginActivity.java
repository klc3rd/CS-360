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

import com.zybooks.weighttracker.DAO.User;
import com.zybooks.weighttracker.database.Database;
import com.zybooks.weighttracker.helpers.UserHelper;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    Button registerBtn;

    EditText mUsername;
    EditText mPassword;

    Database db;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new Database(LoginActivity.this);
        sharedPref = getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                String enterUsername = getResources().getString(R.string.errorEnterUsername);
                String enterPassword = getResources().getString(R.string.errorEnterPassword);

                if (username.length() == 0) {
                    showMessage(enterUsername);
                } else if (password.length() == 0) {
                    showMessage(enterPassword);
                } else {
                    try {
                        login(username, password);
                    } catch (Exception e) {
                        Log.d(TAG, "onClick: " + e.toString());
                    }
                }
            }
        });
    }

    public void startRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void showMessage(String text) {
        Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void login(String username, String password) throws NoSuchAlgorithmException {
        String loginError = getResources().getString(R.string.errorLoginIncorrect);
        String hash = UserHelper.hash(password);

        User foundUser = db.getUserByUsername(username);

        if (foundUser == null) {
            showMessage(loginError);
            return;
        }

        if (!foundUser.getPassword().equals(hash)) {
            showMessage(loginError);
            return;
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", foundUser.getUsername());
        editor.apply();

        // Load MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}