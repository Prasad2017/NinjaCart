package com.ninjacart.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.ninjacart.Model.LoginResponse;
import com.ninjacart.R;
import com.ninjacart.Retrofit.Api;
import com.ninjacart.Retrofit.ApiInterface;
import com.ninjacart.helper.AppSignatureHelper;
import com.ninjacart.interfaces.OtpReceivedInterface;
import com.ninjacart.receiver.SmsBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {


    @BindView(R.id.mobileNumber)
    FormEditText formEditText;
    @BindView(R.id.otpView)
    OtpView otpView;
    @BindViews({R.id.sendotpLayout, R.id.verifyotpLayout})
    List<LinearLayout> linearLayouts;
    @BindView(R.id.resendOTP)
    TextView resendOTP;
    @BindView(R.id.forgotLayout)
    RelativeLayout forgotLayout;
    GoogleApiClient mGoogleApiClient;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    CountDownTimer cTimer = null;
    String otpCode, OTP;
    private String HASH_KEY;
    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // init broadcast receiver
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();

        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        forgotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                try {
                    otpCode = otp;
                    int length = otpCode.trim().length();
                    if (length == 4) {

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        login();

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        File file = new File("data/data/com.ninjacart/shared_prefs/user.xml");
        if (file.exists()) {
            Intent intent = new Intent(Login.this, MainPage.class);
            startActivity(intent);
            finish();
        }

        formEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try{
                    if (formEditText.getText().toString().length()>9){

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    @OnClick({R.id.submit, R.id.verify, R.id.resendOTP, R.id.changeNumber})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (formEditText.testValidity()) {
                    sendOTP(formEditText.getText().toString().trim());
                }
                break;

            case R.id.resendOTP:
                sendOTP(formEditText.getText().toString().trim());
                break;

            case R.id.changeNumber:
                linearLayouts.get(1).setVisibility(View.GONE);
                linearLayouts.get(0).setVisibility(View.VISIBLE);
                break;

            case R.id.verify:

                if (otpCode != null) {

                    if (otpCode.equalsIgnoreCase(OTP)) {
                        login();
                    } else {
                        Toasty.error(Login.this, "OTP Not Match", Toasty.LENGTH_LONG, true).show();
                    }

                } else {
                    Toasty.warning(Login.this, "Please Enter OTP", Toasty.LENGTH_LONG, true).show();
                }

                break;
        }
    }

    private void login() {

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Account is verifying");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Call<LoginResponse> call = Api.getClient().Login(formEditText.getText().toString().trim());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });


    }


    private void sendOTP(String mobileNumber) {

        HASH_KEY = (String) new AppSignatureHelper(this).getAppSignatures().get(0);
        HASH_KEY = HASH_KEY.replace("+", "%252B");
        OTP = new DecimalFormat("0000").format(new Random().nextInt(9999));
        String message = "<#> Your  verification OTP code is "+ OTP +". Please DO NOT share this OTP with anyone.\n" + HASH_KEY;
        String encoded_message= URLEncoder.encode(message);
        Log.e("Message", ""+encoded_message);

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("OTP is sending");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        RequestParams requestParams = new RequestParams();
        requestParams.put("number", mobileNumber);
        requestParams.put("message", encoded_message);

        asyncHttpClient.get("http://graminvikreta.com/androidApp/Supplier/sendSMS.php", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String s = new String(responseBody);

                try {
                    JSONObject jsonObject= new JSONObject(s);
                    if (jsonObject.getString("success").equals("1")){
                        progressDialog.dismiss();

                        linearLayouts.get(0).setVisibility(View.GONE);
                        linearLayouts.get(1).setVisibility(View.VISIBLE);
                        Toast.makeText(Login.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                        startSMSListener();
                    } else {
                        progressDialog.dismiss();

                        linearLayouts.get(1).setVisibility(View.GONE);
                        linearLayouts.get(0).setVisibility(View.VISIBLE);
                        Toast.makeText(Login.this, "Please use a valid phone number", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();

                linearLayouts.get(1).setVisibility(View.GONE);
                linearLayouts.get(0).setVisibility(View.VISIBLE);
            }
        });


    }

    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {
                linearLayouts.get(0).setVisibility(View.GONE);
                linearLayouts.get(1).setVisibility(View.VISIBLE);
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onOtpReceived(String otp) {
        otpView.setText(otp);
    }

    @Override
    public void onOtpTimeout() {

    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
    }

    private void requestPermission() {

        Dexter.withActivity(Login.this)
                .withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(Login.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Login.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}