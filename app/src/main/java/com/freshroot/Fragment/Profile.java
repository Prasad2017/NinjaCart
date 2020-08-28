package com.freshroot.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.andreabaccega.widget.FormEditText;
import com.freshroot.Activity.MainPage;
import com.freshroot.Extra.DetectConnection;
import com.freshroot.Model.AllList;
import com.freshroot.Model.LoginResponse;
import com.freshroot.Model.ProfileResponse;
import com.freshroot.R;
import com.freshroot.Retrofit.Api;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile extends Fragment {


    View view;
    @BindViews({R.id.shopName, R.id.customerName, R.id.customerMobile, R.id.customerAddress})
    List<FormEditText> formEditTexts;
    List<ProfileResponse> profileResponseList = new ArrayList<>();


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        MainPage.title.setText("My Profile");

        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);

        return view;

    }

    @OnClick({R.id.submit})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.submit:

                if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity() && formEditTexts.get(2).testValidity() && formEditTexts.get(3).testValidity()){

                    updateProfile(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString().trim(), formEditTexts.get(2).getText().toString(),
                            formEditTexts.get(3).getText().toString());

                }

                break;

        }
    }

    private void updateProfile(String shopName, String customerName, String customerMobile, String customerAddress) {

        Call<LoginResponse> call = Api.getClient().updateProfile(MainPage.userId, customerName, customerMobile, customerAddress);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().equalsIgnoreCase("true")){
                    Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                } else if (response.body().getSuccess().equalsIgnoreCase("false")){
                    Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("customerError", ""+t.getMessage());
            }
        });

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        // MainPage.title.setVisibility(View.VISIBLE);
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getProfile();
        } else {
            Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
        }
    }

    private void getProfile() {

        Call<AllList> call = Api.getClient().getProfile(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                profileResponseList = allList.getProfileResponseList();

                if (profileResponseList.size()>0){

                    MainPage.userName = profileResponseList.get(0).getcName();
                    MainPage.userNumber = profileResponseList.get(0).getcMobile();
                    MainPage.userAddress = profileResponseList.get(0).getcAddress();

                    formEditTexts.get(0).setText(profileResponseList.get(0).getShop_name());
                    formEditTexts.get(1).setText(MainPage.userName);
                    formEditTexts.get(2).setText(MainPage.userNumber);
                    formEditTexts.get(3).setText(MainPage.userAddress);

                    formEditTexts.get(0).setSelection(formEditTexts.get(0).getText().toString().length());
                    formEditTexts.get(1).setSelection(formEditTexts.get(1).getText().toString().length());
                    formEditTexts.get(2).setSelection(formEditTexts.get(2).getText().toString().length());
                    formEditTexts.get(3).setSelection(formEditTexts.get(3).getText().toString().length());


                } else {

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("profileError", ""+t.getMessage());
            }
        });

    }
}