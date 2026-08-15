package com.example.glowgreendatabase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class RegistrationActivity extends AppCompatActivity {

    EditText etRegistrationName,etEmailRegister,etPhoneRegister,etPasswordRegister,etConfirmPasswordRegister;
    Button btnRegister;
    TextView tvLogin;
    CheckBox cbRegisterShowHidePassword;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        etRegistrationName = findViewById(R.id.etName);
        etEmailRegister = findViewById(R.id.etEmail);
        etPhoneRegister = findViewById(R.id.etMobile);
        etPasswordRegister = findViewById(R.id.etPassword);
        etConfirmPasswordRegister = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.txtLogin);
        cbRegisterShowHidePassword = findViewById(R.id.cbLoginShowHidePassword);

        cbRegisterShowHidePassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etPasswordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                etConfirmPasswordRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                etPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                etConfirmPasswordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        btnRegister.setOnClickListener(v -> {
            if (etRegistrationName.getText().toString().isEmpty()) {
                etRegistrationName.setError("must be Required");
            } else if (!etRegistrationName.getText().toString().matches(".*[A-Z].*")) {
                etRegistrationName.setError("must contain at least one upper case");
            } else if (!etRegistrationName.getText().toString().matches(".*[a-z].*")) {
                etRegistrationName.setError("must contain at least one lower case");
            }
            // else if(!etRegistrationName.getText().toString().matches(".*[0-9].*")){
            //    etRegistrationName.setError("must contain at least one number");
            // }
            else if (etEmailRegister.getText().toString().isEmpty()) {
                etEmailRegister.setError("must be Required");
            } else if (!etEmailRegister.getText().toString().contains("@gmail.com")) {
                etEmailRegister.setError("enter valid email address");
            } else if (etPhoneRegister.getText().toString().isEmpty()) {
                etPhoneRegister.setError("must be Required");
            } else if (etPhoneRegister.getText().toString().length() != 10) {
                etPhoneRegister.setError("Please Enter valid phone number");
            } else if (etPasswordRegister.getText().toString().isEmpty()) {
                etPasswordRegister.setError("must be Required");
            } else if (!etPasswordRegister.getText().toString().matches(".*[A-Z].*")) {
                etPasswordRegister.setError("must contain at least one upper case");
            } else if (!etPasswordRegister.getText().toString().matches(".*[a-z].*")) {
                etPasswordRegister.setError("must contain at least one lower case");
            } else if (!etPasswordRegister.getText().toString().matches(".*[0-9].*")) {
                etPasswordRegister.setError("must contain at least one number");
            } else if (!etPasswordRegister.getText().toString().matches(".*[!@#$%^&*(){}?<>~].*")) {
                etPasswordRegister.setError("must contain at least one special symbol");
            } else if (!etConfirmPasswordRegister.getText().toString().equals(etPasswordRegister.getText().toString())) {
                etConfirmPasswordRegister.setError("password doesn't match");
            } else {


                progressDialog = new ProgressDialog(RegistrationActivity.this);
                progressDialog.setTitle("Registration Proccessing");
                progressDialog.setMessage("Pleas Wait");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                registerUser();
            }

        });

        tvLogin.setOnClickListener(v -> finish());

    }

    private void registerUser() {
        AsyncHttpClient client = new AsyncHttpClient(); //sending and messaging network communication
        RequestParams params = new RequestParams(); // collect the data

        params.put("UserName",etRegistrationName.getText().toString());
        params.put("MobileNo",etPhoneRegister.getText().toString());
        params.put("Email",etEmailRegister.getText().toString());
        params.put("Password",etPasswordRegister.getText().toString());

        client.post(Urls.registerUserAPI,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();


                try {
                    String status = response.getString("Success");
                    String message = response.getString("message");

                    if(status.equals("1")){
                        Toast.makeText(RegistrationActivity.this,"Registration Successfully Done",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();

                Toast.makeText(RegistrationActivity.this,"Error"+errorResponse,Toast.LENGTH_SHORT).show();
            }
        });


    }
}