package com.example.kiwi.tpprogresstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kiwi.tpprogresstracker.httpmanager.apihandler;
import com.example.kiwi.tpprogresstracker.interfaces.internalCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, internalCallback {

    EditText txtEmail, txtPassword;
    Button btnLogin;
    JSONArray jarray = null;
    public static final String MyPreference = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(MyPreference, MODE_PRIVATE);
        String mToken = sharedPreferences.getString("token", null);
        if (mToken != null && !mToken.isEmpty()) {
            startActivity(new Intent(this, Dashboard.class));
        }
    }

    private void initViews() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    private void setListeners() {
        btnLogin.setOnClickListener(this);
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")) {
                    txtEmail.setCursorVisible(true);
                } else {
                    txtEmail.setCursorVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    txtEmail.setCursorVisible(false);
                } else {
                    txtEmail.setCursorVisible(true);
                }
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")) {
                    txtPassword.setCursorVisible(true);
                } else {
                    txtPassword.setCursorVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    txtPassword.setCursorVisible(false);
                } else {
                    txtPassword.setCursorVisible(true);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (txtEmail.getText().toString().isEmpty()) {
            showSnackbarMessageBox("Email address cannot be empty.");
            txtEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            showSnackbarMessageBox("Please enter a valid email address.");
            txtEmail.requestFocus();
            return;
        } else if (txtPassword.getText().toString().isEmpty()) {
            showSnackbarMessageBox("Password cannot be empty.");
            txtPassword.requestFocus();
            return;
        }
        String str = txtEmail.getText() + ":" + txtPassword.getText();
        String baseString = Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
        apihandler.getInstance().callAPI("Authentication", null, baseString, null, this);
    }

    private void showMessageBox(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbarMessageBox(String message) {
        Snackbar.make(findViewById(R.id.relativeLayout), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String url, String requestId, Response response) {
        try {
            String json = ((ResponseBody) response.body()).string();
            JSONObject obj = new JSONObject(json);
            String token = obj.getString("Token");
            Log.d("Login Activity : ", "onSuccess: " + token);
            showMessageBox("Login Successfull.");
            SharedPreferences.Editor editor = getSharedPreferences(MyPreference, MODE_PRIVATE).edit();
            editor.putString("token", token);
            editor.commit();
            if (!token.isEmpty()) {
                startActivity(new Intent(this, Dashboard.class));
            }
//            authenticationResponse result = (authenticationResponse) obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFaliure(String url, String requestId, Response response) {
        Log.d("Login Activity", "onFaliure: " + response);
    }
}
