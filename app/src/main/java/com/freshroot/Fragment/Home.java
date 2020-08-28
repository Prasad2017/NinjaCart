package com.freshroot.Fragment;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andreabaccega.widget.FormEditText;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.freshroot.Activity.MainPage;
import com.freshroot.Adapter.ProductAdapter;
import com.freshroot.Extra.Common;
import com.freshroot.Extra.DetectConnection;
import com.freshroot.Model.AllList;
import com.freshroot.Model.ProductResponse;
import com.freshroot.Model.ProfileResponse;
import com.freshroot.R;
import com.freshroot.Retrofit.Api;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;


public class Home extends Fragment {

    View view;
    @BindView(R.id.relativeLayout)
    RelativeLayout linearLayout;
    @BindView(R.id.productRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyProductLayout)
    LinearLayout emptyProductLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    List<ProductResponse> productResponseList = new ArrayList<>();
    List<ProductResponse> searchProductResponseList = new ArrayList<>();
    public static ProductAdapter adapter;
    List<ProfileResponse> profileResponseList = new ArrayList<>();
    // Creates an instance of the manager.
    private AppUpdateManager mAppUpdateManager;
    // Returns an intent object that you use to check for an update.
    private Task<AppUpdateInfo> appUpdateInfo;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private int MY_REQUEST_CODE = 700;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (DetectConnection.checkInternetConnection(getActivity())){
                    getProductList();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });

        MainPage.searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try{

                    searchProductList(editable.toString());

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


        mAppUpdateManager = AppUpdateManagerFactory.create(getActivity());
        appUpdateInfo = mAppUpdateManager.getAppUpdateInfo();
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState state) {
                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                } else if (state.installStatus() == InstallStatus.INSTALLED) {
                    if (mAppUpdateManager != null) {
                        mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                    }

                } else {
                    Log.i("TAG", "InstallStateUpdatedListener: state: " + state.installStatus());
                }
            }
        };

        mAppUpdateManager.registerListener(installStateUpdatedListener);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, getActivity(), MY_REQUEST_CODE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e("", "checkForAppUpdateAvailability: something else");
            }
        });


        return view;

    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(linearLayout, "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }


    // Checks that the update is not stalled during 'onResume()'.
    // However, you should execute this check at all entry points into the app.
    @Override
    public void onResume() {
        super.onResume();

        mAppUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    mAppUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            getActivity(),
                                            MY_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }


    private void searchProductList(String s) {

        searchProductResponseList = new ArrayList<>();
        searchProductResponseList.clear();

        if (s.length() > 0) {

            for (int i = 0; i < productResponseList.size(); i++)
                if (productResponseList.get(i).getProductName().toLowerCase().contains(s.toLowerCase().trim())) {
                    searchProductResponseList.add(productResponseList.get(i));
                }

            if (searchProductResponseList.size() < 1) {
                emptyProductLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

            } else {
                emptyProductLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        } else {

            searchProductResponseList = new ArrayList<>();
            for (int i = 0; i < productResponseList.size(); i++) {
                searchProductResponseList.add(productResponseList.get(i));
            }

            emptyProductLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }

        adapter = new ProductAdapter(getActivity(), searchProductResponseList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        adapter.notifyItemInserted(searchProductResponseList.size() - 1);
        adapter.notifyDataSetChanged();

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(0);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getProfile();
            getProductList();
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

                    Common.saveUserData(getActivity(), "userName", MainPage.userName);
                    Common.saveUserData(getActivity(), "userNumber", MainPage.userNumber);
                    Common.saveUserData(getActivity(), "userAddress", MainPage.userAddress);


                } else {

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("profileError", ""+t.getMessage());
            }
        });

    }

    private void getProductList() {

        Call<AllList> call = Api.getClient().getProductList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                productResponseList = allList.getProductResponseList();

                if (productResponseList.size()==0){
                    Toasty.normal(getActivity(), "No Product Found", Toasty.LENGTH_SHORT).show();
                    emptyProductLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else {

                    adapter = new ProductAdapter(getActivity(), productResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    adapter.notifyItemInserted(productResponseList.size() - 1);
                    adapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);
                    emptyProductLayout.setVisibility(View.GONE);


                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("productError", ""+t.getMessage());
                emptyProductLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });

    }
}