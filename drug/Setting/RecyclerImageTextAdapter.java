package org.jjcouple.drug.Setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jjcouple.drug.R;

import java.util.ArrayList;

public class RecyclerImageTextAdapter extends RecyclerView.Adapter<RecyclerImageTextAdapter.CustomViewHolder> {
    private ArrayList<RecyclerItem> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected ImageView icon;
        protected TextView title;
        protected TextView desc;

        public CustomViewHolder(View view){
            super(view);
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.desc = (TextView) view.findViewById(R.id.desc);
            this.title=(TextView)view.findViewById(R.id.title);

        }
    }

    public RecyclerImageTextAdapter(ArrayList<RecyclerItem> list) {
        this.mList=list;
    }

    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder ViewHolder, int position) {
        RecyclerItem item = mList.get(position) ;

        ViewHolder.icon.setImageDrawable(item.getIcon()) ;
        ViewHolder.title.setText(item.getTitle()) ;
        ViewHolder.desc.setText(item.getDesc()) ;
    }

    @Override
    public int getItemCount() {
        return mList.size() ;
    }

}
