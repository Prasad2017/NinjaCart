package com.ninjacart.Fragment;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andreabaccega.widget.FormEditText;
import com.ninjacart.Activity.MainPage;
import com.ninjacart.Adapter.ProductAdapter;
import com.ninjacart.Extra.DetectConnection;
import com.ninjacart.Model.AllList;
import com.ninjacart.Model.ProductResponse;
import com.ninjacart.R;
import com.ninjacart.Retrofit.Api;
import com.ninjacart.Retrofit.ApiInterface;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {

    View view;
    @BindView(R.id.productRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyProductLayout)
    LinearLayout emptyProductLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.searchProduct)
    FormEditText searchProduct;
    List<ProductResponse> productResponseList = new ArrayList<>();
    List<ProductResponse> searchProductResponseList = new ArrayList<>();
    public static ProductAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        //MainPage.title.setText("");

        searchProduct.addTextChangedListener(new TextWatcher() {
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

        return view;

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
       // MainPage.title.setVisibility(View.VISIBLE);
        ((MainPage) getActivity()).lockUnlockDrawer(0);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getProductList();
        } else {
            Toasty.warning(getActivity(), "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
        }
    }

    private void getProductList() {

        Call<AllList> call = Api.getClient().getProductList("1");
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