package com.example.glowgreendatabase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.glowgreendatabase.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {


    Button btnLogout,btnEdit;

    TextView ivMyProfileUSerName,ivMyProfileMobileNo,ivMyProfileEmail,ivMyProfileId;

    SharedPreferences preferences;

    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        editor = preferences.edit();


        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);
        ivMyProfileUSerName = findViewById(R.id.txtName);
        ivMyProfileMobileNo = findViewById(R.id.txtMobile);
        ivMyProfileEmail = findViewById(R.id.txtEmail);
        ivMyProfileId = findViewById(R.id.txtUserId);



        Toast.makeText(ProfileActivity.this,"Myprofile Activity",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getMyDetailes();
    }

    private void getMyDetailes() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        //params.put("UserName",ivMyProfileUSerName.getText().toString());

        params.put("UserName",preferences.getString("UserName",""));


        client.post(com.example.glowgreendatabase.common.Urls.getMyDetailesAPI,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("getMyDetailes");

                    for(int i = 0 ; i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray. getJSONObject (i);

                        String id = jsonObject.getString("Id");
                        String username = jsonObject.getString("UserName");
                        String mobileno = jsonObject.getString("MobileNo");
                        String email = jsonObject.getString("Email");

                        ivMyProfileId.setText(id);
                        ivMyProfileUSerName.setText(username);
                        ivMyProfileMobileNo.setText(mobileno);
                        ivMyProfileEmail.setText(email);



                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
            }
        });



    }
}