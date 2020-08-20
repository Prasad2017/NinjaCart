package com.ninjacart.Fragment;

import android.app.ProgressDialog;
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


public class Checkout extends Fragment {

    View view;
    @BindViews({R.id.customerName, R.id.customerMobile, R.id.customerAddress, R.id.totalAmount})
    List<FormEditText> formEditTexts;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_checkout, container, false);
        ButterKnife.bind(this, view);

        MainPage.title.setText("Buy Now");

        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);

        try{

            formEditTexts.get(0).setText(MainPage.userName);
            formEditTexts.get(1).setText(MainPage.userNumber);
            formEditTexts.get(3).setText(""+MyCart.adapter.totalAmount);

        } catch (Exception e){
            e.printStackTrace();
        }

        return view;

    }

    @OnClick({R.id.placeOrder})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.placeOrder:

                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.setTitle("Order is placing");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                Call<LoginResponse> call = Api.getClient().placeOrder(MainPage.userId);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.body().getSuccess().equalsIgnoreCase("true")){
                            progressDialog.dismiss();
                            Toasty.success(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                            ((MainPage) getActivity()).loadFragment(new Home(), true);
                        } else if (response.body().getSuccess().equalsIgnoreCase("false")){
                            progressDialog.dismiss();
                            Toasty.error(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("orderError", ""+t.getMessage());
                    }
                });

                break;
        }

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