package com.freshroot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.freshroot.Activity.MainPage;
import com.freshroot.Model.OrderDetailsResponse;
import com.freshroot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class OrderDetailsProductAdapter extends RecyclerView.Adapter<OrderDetailsProductAdapter.MyViewHolder> {

    Context context;
    List<OrderDetailsResponse> orderDetailsResponseList;

    public OrderDetailsProductAdapter(Context context, List<OrderDetailsResponse> orderDetailsResponseList) {

        this.context = context;
        this.orderDetailsResponseList = orderDetailsResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderDetailsResponse orderDetailsResponse = orderDetailsResponseList.get(position);

        try {

            holder.textViews.get(0).setText(orderDetailsResponseList.get(position).getProductName());
            holder.textViews.get(1).setText(MainPage.currency + " " + orderDetailsResponseList.get(position).getProductPrice() + " * " + orderDetailsResponseList.get(position).getQty().replace(".00", ""));
            holder.textViews.get(2).setText(MainPage.currency + " " + Double.parseDouble(orderDetailsResponseList.get(position).getQty()) * Double.parseDouble(orderDetailsResponseList.get(position).getProductPrice()));

        }catch (Exception e){
            e.printStackTrace();
        }

        try{

            Picasso.with(context)
                    .load(""+orderDetailsResponseList.get(position).getProductPhoto())
                    .placeholder(R.drawable.defaultimage)
                    .fit()
                    .into(holder.imageView);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailsResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.productName, R.id.productQuantity, R.id.amount})
        List<TextView> textViews;

        @BindView(R.id.productImage)
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
