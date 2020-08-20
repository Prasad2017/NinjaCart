package com.ninjacart.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.andreabaccega.widget.FormEditText;
import com.ninjacart.Activity.MainPage;
import com.ninjacart.Extra.DetectConnection;
import com.ninjacart.Model.LoginResponse;
import com.ninjacart.R;
import com.ninjacart.Retrofit.Api;

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
    @BindViews({R.id.customerName, R.id.customerMobile, R.id.customerAddress})
    List<FormEditText> formEditTexts;


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

                if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity() && formEditTexts.get(2).testValidity()){

                    updateProfile(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString().trim(), formEditTexts.get(2).getText().toString());

                }

                break;

        }
    }

    private void updateProfile(String customerName, String customerMobile, String customerAddress) {

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

        } else {
            Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
        }
    }
}