package com.ninjacart.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ninjacart.Activity.MainPage;
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

        holder.textViews.get(1).setText(orderResponseList.get(position).getOrder_number());
        holder.textViews.get(2).setText(orderResponseList.get(position).getOrder_date());
        holder.textViews.get(3).setText(MainPage.currency+" "+orderResponseList.get(position).getGrand_amount());

        holder.textViews.get(0).setText(orderResponseList.get(position).getOrder_status());

        if (orderResponseList.get(position).getOrder_status().equalsIgnoreCase("order_raised")){
            holder.textViews.get(0).setBackgroundTintList(context.getResources().getColorStateList(R.color.yellow_800));
        } else if (orderResponseList.get(position).getOrder_status().equalsIgnoreCase("order_accepted")){
            holder.textViews.get(0).setBackgroundTintList(context.getResources().getColorStateList(R.color.green_600));
        } else if (orderResponseList.get(position).getOrder_status().equalsIgnoreCase("order_dispatched")){
            holder.textViews.get(0).setBackgroundTintList(context.getResources().getColorStateList(R.color.green_700));
        } else if (orderResponseList.get(position).getOrder_status().equalsIgnoreCase("order_delivered")){
            holder.textViews.get(0).setBackgroundTintList(context.getResources().getColorStateList(R.color.green_800));
        }

    }

    @Override
    public int getItemCount() {
        return orderResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.orderStatus, R.id.orderNumber, R.id.orderDate, R.id.orderAmount})
        List<TextView> textViews;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}
