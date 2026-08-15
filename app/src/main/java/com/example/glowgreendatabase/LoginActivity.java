package com.example.glowgreendatabase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.glowgreendatabase.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText etLoginUser,etLoginPassword;
    CheckBox cbLoginShowHidePassword;
    Button btnLoginSubmit;
    TextView tvSignup,tvForgotPassword;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        });

        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        editor = preferences.edit();

        if(preferences.getBoolean("isLogin",false)){
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }


        etLoginUser = findViewById(R.id.etLoginUser);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        cbLoginShowHidePassword = findViewById(R.id.cbLoginShowHidePassword);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);
        tvSignup = findViewById(R.id.tvSignup);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);


        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
        cbLoginShowHidePassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        btnLoginSubmit.setOnClickListener(v -> {



            if (etLoginUser.getText().toString().isEmpty()) {
                etLoginUser.setError("must be Required");
            } else if (etLoginUser.getText().toString().length() < 8) {
                etLoginUser.setError("UserName Must Be greater Than 8");
            }
//                else if(!etLoginUser.getText().toString().matches(".*[A-Z].*")){
//                    etLoginUser.setError("must contain at least one upper case");
//                }
//                else if(!etLoginUser.getText().toString().matches(".*[a-z].*")){
//                    etLoginUser.setError("must contain at least one lower case");
//                }


            else if (etLoginPassword.getText().toString().isEmpty()) {
                etLoginPassword.setError("must be Required");
            } else if (!etLoginPassword.getText().toString().matches(".*[A-Z].*")) {
                etLoginPassword.setError("must contain at least one upper case");
            } else if (!etLoginPassword.getText().toString().matches(".*[a-z].*")) {
                etLoginPassword.setError("must contain at least one lower case");
            } else if (!etLoginPassword.getText().toString().matches(".*[0-9].*")) {
                etLoginPassword.setError("must contain at least one number");
            } else if (!etLoginPassword.getText().toString().matches(".*[!@#$%^&*(){}?<>~].*")) {
                etLoginPassword.setError("must contain at least one special symbol");
            } else {

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Pleas Wait");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();

                loginUser();

            }

        });
        tvSignup.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i);
        });
    }

    private void loginUser() {

        AsyncHttpClient client = new AsyncHttpClient(); //sending and messaging network communication
        RequestParams params = new RequestParams(); // collect the data

        params.put("Login",etLoginUser.getText().toString());
        params.put("Password",etLoginPassword.getText().toString());



        client.post(Urls.loginUserAPI,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();


                try {
                    String status = response.getString("Success");
                    String message = response.getString("message");

                    if(status.equals("1")){

                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        editor.putBoolean("isLogin", true).commit();
                        editor.putString("UserName", etLoginUser.getText().toString()).commit();
                        startActivity(intent);
                        finishAffinity();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();

                Toast.makeText(LoginActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
