package com.example.assgment_ph43396_rest_api_and;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;



public class AdapterSanpham extends RecyclerView.Adapter<AdapterSanpham.ViewHolder> {
    private List<SanphamModel> list;
    private Context context;
    private Home home;

    public AdapterSanpham(List<SanphamModel> list, Context context, Home home) {
        this.list = list;
        this.context = context;
        this.home = home;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanphamModel sanphamModel = list.get(position);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        holder.tvten.setText(sanphamModel.getTen());
        holder.tvgia.setText(numberFormat.format(sanphamModel.getGia()) + " đ");
        holder.tvsoluong.setText(sanphamModel.getSoluong());

        Glide.with(context)
                .load(sanphamModel.getAnh())
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .into(holder.avatar);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.xoa(sanphamModel.get_id());
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.them(context, 1, sanphamModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvten, tvgia, tvsoluong;
        Button btnDelete, btnUpdate;
        ImageView avatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            tvten = itemView.findViewById(R.id.tvTen);
            tvgia = itemView.findViewById(R.id.tvGia);
            tvsoluong = itemView.findViewById(R.id.tvSoluong);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }

}
