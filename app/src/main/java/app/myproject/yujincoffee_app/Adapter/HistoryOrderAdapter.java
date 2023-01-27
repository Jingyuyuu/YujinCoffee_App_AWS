package app.myproject.yujincoffee_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.myproject.yujincoffee_app.Model.Product.ProductModel;
import app.myproject.yujincoffee_app.R;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder>{

    ArrayList<ProductModel> item;
    public HistoryOrderAdapter(ArrayList<ProductModel> item) {
        this.item = item;
    }

    @NonNull
    @Override
    public HistoryOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.historyorder_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderAdapter.ViewHolder holder, int position) {

        holder.orderedNo.setText(Integer.toString(position+1));
        holder.orderedName.setText(item.get(position).getName());
        holder.orderedAmount.setText(Integer.toString(item.get(position).getAmount()));
        holder.orderedDollar.setText(Integer.toString(item.get(position).getDollar() ));
        holder.orderedSugar.setText(item.get(position).getSugar());
        holder.orderedIce.setText(item.get(position).getIce());
        /*
        //用來判斷非飲品的商品 不須顯示甜度冰量
        if(item.get(position).getTem()>0){
            holder.orderedNo.setText(Integer.toString(position+1));
            holder.orderedName.setText(item.get(position).getName());
            holder.orderedAmount.setText(Integer.toString(item.get(position).getAmount()));
            holder.orderedDollar.setText(Integer.toString(item.get(position).getDollar() ));
            holder.orderedSugar.setText(item.get(position).getSugar());
            holder.orderedIce.setText(item.get(position).getIce());
        }else{
            holder.orderedNo.setText(Integer.toString(position+1));
            holder.orderedName.setText(item.get(position).getName());
            holder.orderedAmount.setText(Integer.toString(item.get(position).getAmount()));
            holder.orderedDollar.setText(Integer.toString(item.get(position).getDollar()));
            holder.orderedSugar.setVisibility(View.INVISIBLE);
            holder.orderedIce.setVisibility(View.INVISIBLE);
        }

         */


    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView orderedNo;
        private TextView orderedName;
        private TextView orderedSugar;
        private TextView orderedIce;
        private TextView orderedAmount;
        private TextView orderedDollar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.orderedNo=itemView.findViewById(R.id.orderedNo);
            this.orderedName=itemView.findViewById(R.id.orderedName);
            this.orderedSugar=itemView.findViewById(R.id.orderedSugar);
            this.orderedIce=itemView.findViewById(R.id.orderedIce);
            this.orderedAmount=itemView.findViewById(R.id.orderedAmount);
            this.orderedDollar=itemView.findViewById(R.id.orderedPrice);

        }
    }
}
