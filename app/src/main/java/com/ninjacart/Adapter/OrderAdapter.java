package com.ninjacart.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ninjacart.Activity.MainPage;
import com.ninjacart.Fragment.OrderDetails;
import com.ninjacart.Model.OrderResponse;
import com.ninjacart.R;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context context;
    List<OrderResponse> orderResponseList;

    public OrderAdapter(Context context, List<OrderResponse> orderResponseList) {

        this.context = context;
        this.orderResponseList = orderResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderResponse orderResponse = orderResponseList.get(position);

        holder.textViews.get(1).setText(""+orderResponseList.get(position).getOrder_number());
        holder.textViews.get(2).setText(""+orderResponseList.get(position).getOrder_date());
        holder.textViews.get(3).setText(""+MainPage.currency+" "+orderResponseList.get(position).getGrand_amount());

        try {

            if (orderResponseList.get(position).getOrder_status().equalsIgnoreCase("order_raised")) {
                holder.textViews.get(0).setBackgroundTintList(context.getResources().getColorStateList(R.color.yellow_800));
                holder.textViews.get(0).setText("Order Placed");
            } else if (orderResponseList.get(position).getOrder_status().equalsIgnoreCase("order_accepted")) {
                holder.textViews.get(0).setBackgroundTintList(context.getResources().getColorStateList(R.color.green_600));
                holder.textViews.get(0).setText("Order Accepted");
            } else if (orderResponseList.get(position).getOrder_status().equalsIgnoreCase("order_dispatched")) {
                holder.textViews.get(0).setBackgroundTintList(context.getResources().getColorStateList(R.color.green_700));
                holder.textViews.get(0).setText("Order Dispatched");
            } else if (orderResponseList.get(position).getOrder_status().equalsIgnoreCase("order_delivered")) {
                holder.textViews.get(0).setBackgroundTintList(context.getResources().getColorStateList(R.color.green_800));
                holder.textViews.get(0).setText("Order Completed");
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        holder.textViews.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OrderDetails orderDetails = new OrderDetails();
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderResponseList.get(position).getFinal_order_pk());
                Log.e("orderId", ""+orderResponseList.get(position).getFinal_order_pk());
                orderDetails.setArguments(bundle);
                ((MainPage) context).loadFragment(orderDetails, true);

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.orderStatus, R.id.orderNumber, R.id.orderDate, R.id.orderAmount, R.id.view})
        List<TextView> textViews;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
