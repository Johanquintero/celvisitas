package com.celar.celvisitas.ui.home;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.celar.celvisitas.Activities.DashboardActivity;
import com.celar.celvisitas.Activities.ImageDetailActivity;
import com.celar.celvisitas.R;

import java.util.List;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder>
    implements View.OnClickListener{

    private List<VisitElement> mData;
    private LayoutInflater mInflater;
    private Context context;
    private View.OnClickListener listener;
    public Button btnAceptar;
    public Button btnCancelar;
    public Button btnExit;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    public VisitAdapter(List<VisitElement> visit, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = visit;
    }

    @Override
    public int getItemCount() { return mData.size();}

    @Override
    public VisitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.visit_element, null);
        view.setOnClickListener(this);
        btnAceptar = view.findViewById(R.id.buttonAcept);
        btnCancelar = view.findViewById(R.id.buttonCancel);
        btnExit = view.findViewById(R.id.buttonExit);
        return new VisitAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VisitAdapter.ViewHolder holder,final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<VisitElement> items) {mData = items;}

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageUser;
        public TextView name;
        public TextView id;
        public TextView status;
        ViewHolder(View itemView){
            super(itemView);
            imageUser = itemView.findViewById(R.id.imageView2);
            name = itemView.findViewById(R.id.nameRequestCard);
            id = itemView.findViewById(R.id.idVisit);
//            status = itemView.findViewById(R.id.status);
        }

        void bindData(final VisitElement item){
            Glide.with(context).load(item.img).placeholder(R.drawable.avatar).apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, 15, 2, "#7D9067", 10))).centerCrop().into(imageUser);
            name.setText(item.getNombre());
            id.setText(item.getId());
//            status.setText(item.getStatus());

            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof DashboardActivity) {
                        ((DashboardActivity)context).requestPermissionSend(item.id,"allowed");
                    }
                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof DashboardActivity) {
                        ((DashboardActivity)context).requestPermissionSend(item.id,"rejected");
                    }
                }
            });

            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof DashboardActivity) {
                        ((DashboardActivity)context).requestOutPermission(item.id);
                    }
                }
            });

            imageUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity contextImg = new ImageDetailActivity();
                    if (contextImg instanceof ImageDetailActivity) {
                        Intent intent = new Intent(context, ImageDetailActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, item.getImg());
                        intent.putExtra("nameUser",item.getNombre());
                        context.startActivity(intent);
                    }
                }
            });

//            validar estado para ocultar y mostrar botones
            String statusString = item.getStatus();
            if(statusString.equals("allowed")){
                btnAceptar.setVisibility(View.INVISIBLE);
                btnCancelar.setVisibility(View.INVISIBLE);
                btnExit.setVisibility(View.VISIBLE);
            }

        }

    }
}
