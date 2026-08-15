package com.example.glowgreendatabase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glowgreendatabase.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etForgetUserName,etNewPassword,etConfirmNewPassword;

    Button btnForgetPasswordForgetPassword;

    ProgressDialog progressDialog;

    TextView txtLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);


        etForgetUserName = findViewById(R.id.etUser);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmPassword);
        btnForgetPasswordForgetPassword = findViewById(R.id.btnReset);
        txtLogin = findViewById(R.id.txtLogin);


        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        btnForgetPasswordForgetPassword.setOnClickListener(v -> {
            if (etForgetUserName.getText().toString().isEmpty()) {
                etForgetUserName.setError("Please enter Username");
            } else if (etForgetUserName.getText().length() < 8) {
                etForgetUserName.setError("UserName Must be Greater than 8");
            } else if (etNewPassword.getText().toString().isEmpty()) {
                etNewPassword.setError("Please enter NewPassword");
            } else if (etNewPassword.getText().toString().length() < 8) {
                etNewPassword.setError("Password Must be Greater than 8");
            } else if (etConfirmNewPassword.getText().toString().isEmpty()) {
                etConfirmNewPassword.setError("Please enter Confirm NewPassword");
            } else if (etConfirmNewPassword.getText().toString().length() < 8) {
                etConfirmNewPassword.setError("Password Must be Greater than 8");
            } else if (!etNewPassword.getText().toString().equals(etConfirmNewPassword.getText().toString())) {
                etConfirmNewPassword.setError("Password and confirm password Should be match");
            } else {

                progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                progressDialog.setTitle("Forget Password");
                progressDialog.setMessage("Please Wait ");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();

                forgetPassword();

            }

        });
    }

    private void forgetPassword() {
        AsyncHttpClient client = new AsyncHttpClient();  // sending and managing network request
        RequestParams params = new RequestParams();  //collect and put the data

        params.put("UserName",etForgetUserName.getText().toString());
        params.put("newPassword",etNewPassword.getText().toString());

        client.post(Urls.forgetPasswordAPI,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                try {
                    String status = response.getString("Success");
                    String message = response.getString("message");

                    if(status.equals("1")){
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ForgotPasswordActivity.this,"Server Error",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }
}