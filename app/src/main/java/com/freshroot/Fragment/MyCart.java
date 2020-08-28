package com.freshroot.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freshroot.Activity.MainPage;
import com.freshroot.Adapter.CartAdapter;
import com.freshroot.Extra.DetectConnection;
import com.freshroot.Model.AllList;
import com.freshroot.Model.CartResponse;
import com.freshroot.R;
import com.freshroot.Retrofit.Api;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyCart extends Fragment {

    View view;
    @BindView(R.id.categoryRecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.noCategorytxt)
    TextView noCategorytxt;
    public  static TextView checkOut;
    @BindView(R.id.linearLayout)
    RelativeLayout linearLayout;
    List<CartResponse> cartResponseList = new ArrayList<>();
    public static CartAdapter adapter;
    String customerType, customerId,customerName, customerState;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        ButterKnife.bind(this, view);

        MainPage.title.setText("My Cart");

        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);

        checkOut = view.findViewById(R.id.checkout);


        return view;

    }

    @OnClick({R.id.checkout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.checkout:
                ((MainPage) getActivity()).loadFragment(new Checkout(), true);
                break;

        }

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getCartList();
        } else {
            Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
        }
    }

    private void getCartList() {

        Call<AllList> call = Api.getClient().getCartList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                cartResponseList = allList.getCartResponseList();

                if (cartResponseList.size()<1){
                    noCategorytxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    checkOut.setVisibility(View.GONE);
                } else {

                    adapter = new CartAdapter(getActivity(), cartResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(cartResponseList.size() - 1);
                    recyclerView.setHasFixedSize(true);

                    linearLayout.setVisibility(View.VISIBLE);
                    noCategorytxt.setVisibility(View.GONE);
                    checkOut.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("cartError", ""+t.getMessage());
                checkOut.setVisibility(View.GONE);
            }
        });


    }
}