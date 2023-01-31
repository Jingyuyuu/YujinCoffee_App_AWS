package app.myproject.yujincoffee_app.Adapter;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.myproject.yujincoffee_app.Model.Product.StoreListModel;
import app.myproject.yujincoffee_app.R;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
    ArrayList<StoreListModel> item;
    SQLiteDatabase db;

    public StoreListAdapter(ArrayList<StoreListModel> item, SQLiteDatabase db) {
        this.item = item;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.storelist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.storename.setText(item.get(position).getStorename());
        holder.address.setText(item.get(position).getAddress());
        holder.tel.setText(item.get(position).getTel());
        holder.businesshour.setText(item.get(position).getBusinesshour());
        holder.a.setText("店家名稱:");
        holder.b.setText("店家地址:");
        holder.c.setText("電話:");
        holder.d.setText("營業時間:");

    }

    @Override
    public int getItemCount() {
        if(item==null){
            return 0;
        }
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView storename;
        private TextView address;
        private TextView tel;
        private TextView businesshour;

        private TextView a;
        private TextView b;
        private TextView c;
        private TextView d;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.storename=itemView.findViewById(R.id.storename);
            this.address=itemView.findViewById(R.id.address);
            this.tel=itemView.findViewById(R.id.tel);
            this.businesshour=itemView.findViewById(R.id.businesshour);
            this.a=itemView.findViewById(R.id.textView42);
            this.b=itemView.findViewById(R.id.textView43);
            this.c=itemView.findViewById(R.id.textView44);
            this.d=itemView.findViewById(R.id.textView45);
        }
    }
}
