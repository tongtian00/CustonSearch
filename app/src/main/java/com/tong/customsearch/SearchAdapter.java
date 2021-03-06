package com.tong.customsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<String> list;
    //保存原有的数据
    private List<String> originalList;

    private OnItemListener onItemListener;
    void setOnItemListener(OnItemListener onItemListener){
        this.onItemListener = onItemListener;
    }

    private SearchFilter filter;

    SearchAdapter(Context context,List<String> list){
        this.context =context;
        this.list = list;
        originalList = list;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_search,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemListener!=null){
                    onItemListener.onItem(list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null){
            filter = new SearchFilter();
        }
        return filter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    interface OnItemListener{
        void onItem(String name);
    }


    class SearchFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //输入框传来的数据 constraint

            //用于保存过滤的结果
            FilterResults filterResults = new FilterResults();

            if (constraint==null || constraint.length()==0){
                filterResults.values = originalList;
                filterResults.count = originalList.size();
            }else {
                List<String> fList = new ArrayList<>();
                String cons = constraint.toString().trim().toLowerCase();
                for (String s : originalList) {
                    //从首位开始匹配
                    if (s.startsWith(cons)){
                        fList.add(s);
                    }
                }
                filterResults.values = fList;
                filterResults.count = fList.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (List<String>) results.values;
            notifyDataSetChanged();
        }
    }


}
