package org.jjcouple.drug.etc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.jjcouple.drug.Fragment.FragmentUpdateBulletin;
import org.jjcouple.drug.Fragment.FragmentUploadBulletin;
import org.jjcouple.drug.R;


import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> implements Filterable{

    private ArrayList<String> unFilteredlist;
    private ArrayList<String> filteredList;
    private String[] update_info;
    private Context context;

    public CustomAdapter(ArrayList<String> arrayList, Context context) {
        this.unFilteredlist = arrayList;
        this.filteredList = arrayList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
        holder.mountain_address.setText(filteredList.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null){
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(v, position);
                    update_info = filteredList.get(position).trim().split("\n");
                    for(int i = 0; i < update_info.length; i++){
                        update_info[i] = update_info[i].split(" : ")[1];
                    }

                    FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
                    FragmentTransaction tran = fm.beginTransaction();
                    FragmentUpdateBulletin fragmentUpdateBulletin = new FragmentUpdateBulletin();
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("update_info", update_info);
                    fragmentUpdateBulletin.setArguments(bundle);
                    tran.replace(R.id.frameLayout, fragmentUpdateBulletin);
                    tran.commit();
//                    Intent intent = new Intent(context, FragmentUpdateBulletin.class);
//                    intent.putExtra("update_info", update_info);
//                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView mountain_address;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mountain_address = itemView.findViewById(R.id.mountain_address);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                Log.d("CustomAdapter", charString);
                if(charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<String> filteringList = new ArrayList<>();
                    for(String name : unFilteredlist) {
                        if(name.contains(charString)) {
                            Log.d("CustomAdapter", name);
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<String>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}

