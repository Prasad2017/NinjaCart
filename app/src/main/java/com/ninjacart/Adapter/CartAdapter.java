package com.ninjacart.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ninjacart.Activity.MainPage;
import com.ninjacart.Fragment.Home;
import com.ninjacart.Fragment.MyCart;
import com.ninjacart.Model.CartResponse;
import com.ninjacart.Model.LoginResponse;
import com.ninjacart.R;
import com.ninjacart.Retrofit.Api;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    Context context;
    List<CartResponse> cartResponseList;
    public  double amountPayable=0f, taxAmount=0f;
    public  double totalAmount =0f;
    public static String totalAmountPayable;

    public CartAdapter(Context context, List<CartResponse> cartResponseList) {

        this.context = context;
        this.cartResponseList = cartResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CartResponse cartResponse = cartResponseList.get(position);

        totalAmount = totalAmount + (Double.parseDouble(cartResponseList.get(position).getProductSellPrice()) * Double.parseDouble(cartResponseList.get(position).getProductQuantity()));

        if (position==cartResponseList.size()-1){

            holder.totalAmount.setVisibility(View.VISIBLE);
            //holder.textView.get(5).setText(Html.fromHtml(context.getResources().getString(R.string.secure_payment_text)));

            holder.textView.get(7).setText("Price (" + cartResponseList.size() + " items)");
            holder.textView.get(5).setText(MainPage.currency + " " + totalAmount);
            holder.textView.get(6).setText(MainPage.currency + " " + totalAmount);

            MyCart.checkOut.setText("SubTotal ( " + cartResponseList.size() + " items ) "+MainPage.currency+ " "+totalAmount);


        }else
            holder.totalAmount.setVisibility(View.GONE);

        holder.textView.get(0).setText(cartResponseList.get(position).getProductName());
        holder.textView.get(1).setText(MainPage.currency +" "+cartResponseList.get(position).getProductSellPrice());
        holder.textView.get(2).setText(cartResponseList.get(position).getProductMrp()+"/"+cartResponseList.get(position).getProductUnit());
        holder.textView.get(4).setText(""+cartResponseList.get(position).getProductQuantity());

        double discountPercentage = Double.parseDouble(cartResponseList.get(position).getProductMrp()) - Double.parseDouble(cartResponseList.get(position).getProductSellPrice());
        Log.d("percentage", discountPercentage + "");
        discountPercentage = (discountPercentage / Double.parseDouble(cartResponseList.get(position).getProductSellPrice())) * 100;
        if ((int) Math.round(discountPercentage) > 0) {
            holder.textView.get(3).setVisibility(View.VISIBLE);
            holder.textView.get(3).setText(((int) Math.round(discountPercentage) + "% Off"));
        } else {
            holder.textView.get(3).setText("0% Off");
            holder.textView.get(3).setVisibility(View.GONE);
        }

        try{

            Picasso.with(context)
                    .load("http://www.rssas.in/"+cartResponseList.get(position).getProductPhoto())
                    .fit()
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.imageViews.get(0));

        } catch (Exception e){
            e.printStackTrace();
        }

        holder.imageViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = Integer.parseInt(holder.textView.get(4).getText().toString().trim());
                if (quantity<1){
                    quantity = Integer.parseInt(cartResponseList.get(position).getMinPurchaseQty());
                    quantity = quantity;
                }else if (quantity == Integer.parseInt(cartResponseList.get(position).getMinPurchaseQty())){
                    //  quantity = Integer.parseInt(productResponseList.get(position).getMinPurchaseQty());
                    quantity = 0;

                    Call<LoginResponse> call = Api.getClient().deleteCart(cartResponseList.get(position).getProductId(), MainPage.userId);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                            if (response.body().getSuccess().equalsIgnoreCase("true")){
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                ((MainPage) context).removeCurrentFragmentAndMoveBack();
                                ((MainPage) context).loadFragment(new MyCart(), true);
                            } else if (response.body().getSuccess().equalsIgnoreCase("false")){
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e("cartError", ""+t.getMessage());
                        }
                    });

                } else {
                    quantity = Integer.parseInt(holder.textView.get(4).getText().toString().trim());
                    quantity = quantity - 1;
                }

                Log.e("quantity", ""+quantity);

                if (quantity>=Integer.parseInt(cartResponseList.get(position).getMinPurchaseQty())){

                    holder.textView.get(4).setText(""+quantity);
                    Home.adapter.notifyDataSetChanged();

                    Call<LoginResponse> call = Api.getClient().updateCart(cartResponseList.get(position).getProductId(), MainPage.userId, ""+quantity);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                            if (response.body().getSuccess().equalsIgnoreCase("true")){
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                Home.adapter.notifyDataSetChanged();
                                Home.adapter.notifyItemChanged(Integer.parseInt(cartResponseList.get(position).getProductId()));
                                ((MainPage) context).removeCurrentFragmentAndMoveBack();
                                ((MainPage) context).loadFragment(new MyCart(), true);
                            } else if (response.body().getSuccess().equalsIgnoreCase("false")){
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
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
                    //  Home.adapter.notifyItemChanged(Integer.parseInt(productResponseList.get(position).getProductId()));
                }


            }
        });

        holder.imageViews.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = Integer.parseInt(holder.textView.get(4).getText().toString().trim());
                if (quantity<1){
                    quantity = Integer.parseInt(cartResponseList.get(position).getMinPurchaseQty());
                    quantity = quantity;
                }else if (quantity == Integer.parseInt(cartResponseList.get(position).getMinPurchaseQty())){
                    quantity = Integer.parseInt(cartResponseList.get(position).getMinPurchaseQty());
                    quantity = quantity + 1;
                } else {
                    quantity = Integer.parseInt(holder.textView.get(4).getText().toString().trim());
                    quantity = quantity + 1;
                }

                Log.e("quantity", ""+quantity);

                if (quantity>=Integer.parseInt(cartResponseList.get(position).getMinPurchaseQty())){

                    Call<LoginResponse> call = Api.getClient().addToCart(cartResponseList.get(position).getProductId(), MainPage.userId, ""+quantity);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                            if (response.body().getSuccess().equalsIgnoreCase("true")){
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                Home.adapter.notifyDataSetChanged();
                                Home.adapter.notifyItemChanged(Integer.parseInt(cartResponseList.get(position).getProductId()));
                                ((MainPage) context).removeCurrentFragmentAndMoveBack();
                                ((MainPage) context).loadFragment(new MyCart(), true);
                            } else if (response.body().getSuccess().equalsIgnoreCase("false")){
                                Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                Home.adapter.notifyDataSetChanged();
                                Home.adapter.notifyItemChanged(Integer.parseInt(cartResponseList.get(position).getProductId()));
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
                    Home.adapter.notifyItemChanged(Integer.parseInt(cartResponseList.get(position).getProductId()));
                }

            }
        });

        holder.imageViews.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<LoginResponse> call = Api.getClient().deleteCart(cartResponseList.get(position).getProductId(), MainPage.userId);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.body().getSuccess().equalsIgnoreCase("true")){
                            Toasty.normal(context, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                            ((MainPage) context).removeCurrentFragmentAndMoveBack();
                            ((MainPage) context).loadFragment(new MyCart(), true);
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
        });

    }

    @Override
    public int getItemCount() {
        return cartResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.productName, R.id.productSellPrice, R.id.productMrpPrice, R.id.productOffer, R.id.quantity, R.id.price, R.id.amountPayable, R.id.txtPrice})
        List<TextView> textView;

        @BindViews({R.id.productImage, R.id.minus, R.id.add, R.id.delete})
        List<ImageView> imageViews;

        @BindView(R.id.totalAmount)
        LinearLayout totalAmount;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
