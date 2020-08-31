package com.freshroot.Adapter;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.freshroot.Activity.MainPage;
import com.freshroot.Fragment.Home;
import com.freshroot.Model.LoginResponse;
import com.freshroot.Model.ProductResponse;
import com.freshroot.R;
import com.freshroot.Retrofit.Api;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    List<ProductResponse> productResponseList;

    public ProductAdapter(Context context, List<ProductResponse> productResponseList) {

        this.context = context;
        this.productResponseList = productResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProductResponse productResponse = productResponseList.get(position);

        holder.textView.get(0).setText(productResponseList.get(position).getProductName());
        holder.textView.get(1).setText(MainPage.currency +" "+productResponseList.get(position).getProductSellPrice()+" /"+productResponseList.get(position).getProductUnit());
        holder.textView.get(2).setText(productResponseList.get(position).getMinPurchaseQty()+" "+productResponseList.get(position).getProductUnit());
        holder.textView.get(4).setText(""+productResponseList.get(position).getQty());
        holder.textView.get(5).setText(""+MainPage.currency+" "+Double.parseDouble(productResponseList.get(position).getQty()) * Double.parseDouble(productResponseList.get(position).getProductSellPrice()));
        holder.textView.get(6).setText(""+productResponseList.get(position).getQty());

        if (Double.parseDouble(productResponseList.get(position).getQty())>1){
            holder.amtQtyLinearLayout.setVisibility(View.VISIBLE);
        } else {
            holder.amtQtyLinearLayout.setVisibility(View.GONE);
        }

        /*double discountPercentage = Double.parseDouble(productResponseList.get(position).getProductMrp()) - Double.parseDouble(productResponseList.get(position).getProductSellPrice());
        Log.d("percentage", discountPercentage + "");
        discountPercentage = (discountPercentage / Double.parseDouble(productResponseList.get(position).getProductSellPrice())) * 100;
        if ((int) Math.round(discountPercentage) > 0) {
            holder.textView.get(3).setVisibility(View.GONE);
            holder.textView.get(3).setText(((int) Math.round(discountPercentage) + "% Off"));
        } else {
            holder.textView.get(3).setText("0% Off");
            holder.textView.get(3).setVisibility(View.GONE);
        }*/

        Log.e("Pic", "http://www.rssas.in/market/uploads/product/"+productResponseList.get(position).getProductPhoto());

        try{

            Picasso.with(context)
                    .load("http://www.rssas.in/market/uploads/product/"+productResponseList.get(position).getProductPhoto())
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.imageViews.get(0));

        } catch (Exception e){
            e.printStackTrace();
        }

        holder.imageViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = Integer.parseInt(holder.textView.get(4).getText().toString().trim().replace(".00", "")) - Integer.parseInt(productResponseList.get(position).getMinPurchaseQty().replace(".00", ""));

                if (quantity < Integer.parseInt(productResponseList.get(position).getMinPurchaseQty())){

                    quantity = 0;

                    Call<LoginResponse> call = Api.getClient().deleteCart(productResponseList.get(position).getProductId(), MainPage.userId);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                            if (response.body().getSuccess().equalsIgnoreCase("true")){
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                ((MainPage) context).loadFragment(new Home(), false);
                            } else if (response.body().getSuccess().equalsIgnoreCase("false")){
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e("cartError", ""+t.getMessage());
                        }
                    });

                } else if (quantity >= Integer.parseInt(productResponseList.get(position).getMinPurchaseQty())){

                    Call<LoginResponse> call = Api.getClient().updateCart(productResponseList.get(position).getProductId(), MainPage.userId, ""+quantity);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                            if (response.body().getSuccess().equalsIgnoreCase("true")){
                             //   Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                Home.adapter.notifyDataSetChanged();
                                Home.adapter.notifyItemChanged(Integer.parseInt(productResponseList.get(position).getProductId()));
                                ((MainPage) context).loadFragment(new Home(), false);
                            } else if (response.body().getSuccess().equalsIgnoreCase("false")){
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e("cartError", ""+t.getMessage());
                        }
                    });

                }

            }
        });

        holder.imageViews.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = Integer.parseInt(holder.textView.get(4).getText().toString().trim().replace(".00", ""));
                if (quantity<1){
                    quantity = Integer.parseInt(productResponseList.get(position).getMinPurchaseQty());
                    quantity = quantity;
                }else if (quantity == Integer.parseInt(productResponseList.get(position).getMinPurchaseQty())){
                    quantity = Integer.parseInt(productResponseList.get(position).getMinPurchaseQty());
                    quantity = quantity + Integer.parseInt(productResponseList.get(position).getMinPurchaseQty());
                } else {
                    quantity = Integer.parseInt(holder.textView.get(4).getText().toString().trim().replace(".00",""));
                    quantity = quantity + Integer.parseInt(productResponseList.get(position).getMinPurchaseQty());
                }

                    Log.e("quantity", ""+quantity);

                if (quantity>=Integer.parseInt(productResponseList.get(position).getMinPurchaseQty())){

                    Call<LoginResponse> call = Api.getClient().addToCart(productResponseList.get(position).getProductId(), MainPage.userId, ""+quantity);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                            if (response.body().getSuccess().equalsIgnoreCase("true")) {
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                Home.adapter.notifyDataSetChanged();
                                Home.adapter.notifyItemChanged(Integer.parseInt(productResponseList.get(position).getProductId()));
                                ((MainPage) context).loadFragment(new Home(), false);
                            } else if (response.body().getSuccess().equalsIgnoreCase("1")) {
                                ((MainPage) context).loadFragment(new Home(), false);
                            } else if (response.body().getSuccess().equalsIgnoreCase("false")) {
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                Home.adapter.notifyDataSetChanged();
                                Home.adapter.notifyItemChanged(Integer.parseInt(productResponseList.get(position).getProductId()));
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e("cartError", ""+t.getMessage());
                        }

                    });

                } else {
                    holder.textView.get(4).setText("0");
                    Home.adapter.notifyDataSetChanged();
                    Home.adapter.notifyItemChanged(Integer.parseInt(productResponseList.get(position).getProductId()));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return productResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.productName, R.id.productSellPrice, R.id.productMrpPrice, R.id.productOffer, R.id.quantity, R.id.totalPrice, R.id.qty})
        List<TextView> textView;
        @BindViews({R.id.productImage, R.id.minus, R.id.add})
        List<ImageView> imageViews;
        @BindView(R.id.amtQtyLinearLayout)
        LinearLayout amtQtyLinearLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
