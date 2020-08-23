package com.ninjacart.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ninjacart.Activity.MainPage;
import com.ninjacart.Adapter.CartAdapter;
import com.ninjacart.Adapter.OrderAdapter;
import com.ninjacart.Adapter.OrderDetailsProductAdapter;
import com.ninjacart.Extra.DetectConnection;
import com.ninjacart.Model.AllList;
import com.ninjacart.Model.OrderDetailsResponse;
import com.ninjacart.Model.OrderResponse;
import com.ninjacart.R;
import com.ninjacart.Retrofit.Api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderDetails extends Fragment {

    View view;
    @BindViews({R.id.orderDate, R.id.deliveryDate, R.id.orderAmount, R.id.grandAmount, R.id.orderNumber})
    List<TextView> textViews;
    @BindView(R.id.categoryRecyclerview)
    RecyclerView recyclerView;
    @BindViews({R.id.cardView, R.id.cardView1, R.id.cardView2})
    List<CardView> cardViews;
    public static String orderId;
    List<OrderDetailsResponse> orderDetailsResponseList = new ArrayList();
    public static OrderDetailsProductAdapter adapter;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_details, container, false);
        ButterKnife.bind(this, view);

        MainPage.title.setText("Order Details");

        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);

        Bundle bundle = getArguments();
        orderId = bundle.getString("orderId");

        return view;

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getOrderDetails();
        } else {
            Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
        }
    }

    private void getOrderDetails() {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Order details are loading.");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Call<AllList> call = Api.getClient().orderDetails(orderId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                orderDetailsResponseList = allList.getOrderDetailsResponseList();

                if (orderDetailsResponseList.size() > 0) {
                    progressDialog.dismiss();

                    try {
                        String dateStr = orderDetailsResponseList.get(0).getOrderDate();
                        DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                        // parse the date string into Date object
                        Date date = srcDf.parse(dateStr);
                        DateFormat destDf = new SimpleDateFormat("dd MMM yyyy");
                        // format the date into another format
                        dateStr = destDf.format(date);
                        textViews.get(0).setText("" + dateStr);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        String dateStr = orderDetailsResponseList.get(0).getDeliveryDate();
                        DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                        // parse the date string into Date object
                        Date date = srcDf.parse(dateStr);
                        DateFormat destDf = new SimpleDateFormat("dd MMM yyyy");
                        // format the date into another format
                        dateStr = destDf.format(date);
                        textViews.get(1).setText("" + dateStr);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }

                    textViews.get(2).setText("" + MainPage.currency + " " + orderDetailsResponseList.get(0).getGrandAmount());
                    textViews.get(3).setText("" + MainPage.currency + " " + orderDetailsResponseList.get(0).getGrandAmount());
                    textViews.get(4).setText("" +orderDetailsResponseList.get(0).getOrderNumber());

                    adapter = new OrderDetailsProductAdapter(getActivity(), orderDetailsResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(orderDetailsResponseList.size() - 1);
                    recyclerView.setHasFixedSize(true);

                    cardViews.get(0).setVisibility(View.VISIBLE);
                    cardViews.get(1).setVisibility(View.VISIBLE);
                    cardViews.get(2).setVisibility(View.VISIBLE);

                } else {
                    progressDialog.dismiss();
                    cardViews.get(0).setVisibility(View.GONE);
                    cardViews.get(1).setVisibility(View.GONE);
                    cardViews.get(2).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("orderDetailsError", ""+t.getMessage());
            }
        });

    }
}