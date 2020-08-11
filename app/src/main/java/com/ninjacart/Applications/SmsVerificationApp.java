package com.ninjacart.Applications;

import android.app.Application;

import com.ninjacart.helper.AppSignatureHelper;


public class SmsVerificationApp extends Application {


  @Override
  public void onCreate() {
    super.onCreate();
    AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
    appSignatureHelper.getAppSignatures();
  }

}
