package app.myproject.yujincoffee_app.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.myproject.yujincoffee_app.Model.Product.ProductModel;
import app.myproject.yujincoffee_app.Part2.ItemTouchAdapter;
import app.myproject.yujincoffee_app.R;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> implements ItemTouchAdapter {
    ArrayList<ProductModel> item;

    public MyOrderAdapter(ArrayList<ProductModel> item) {
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(item.get(position).getTem()>0){
            holder.no.setText(Integer.toString(position+1));
            holder.shopName.setText(item.get(position).getName());
            holder.shopAmount.setText(Integer.toString(item.get(position).getAmount()));
            holder.shopDollar.setText(Integer.toString(item.get(position).getDollar() ));
            holder.shopSugar.setText(item.get(position).getSugar());
            holder.shopIce.setText(item.get(position).getIce());
        }else{
            holder.no.setText(Integer.toString(position+1));
            holder.shopName.setText(item.get(position).getName());
            holder.shopAmount.setText(Integer.toString(item.get(position).getAmount()));
            holder.shopDollar.setText(Integer.toString(item.get(position).getDollar()));
            holder.shopSugar.setText(null);
            holder.shopIce.setText(null);
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onItemDissmiss(int position) {
        item.remove(position);
        for(int i=0;i<item.size();i++){
            String name=item.get(i).getName();
            String ice = item.get(i).getIce();
            String sugar =item.get(i).getSugar();
            int amount = item.get(i).getAmount();
            Log.i("刪除後訂單:",name+" ,"+ice+" ,"+sugar+" ,"+amount+" ,");
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder{
       private TextView no;
       private TextView shopName;
       private TextView shopSugar;
       private TextView shopIce;
       private TextView shopAmount;
       private TextView shopDollar;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           this.no=itemView.findViewById(R.id.no);
           this.shopName=itemView.findViewById(R.id.orderedName);
           this.shopSugar=itemView.findViewById(R.id.orderedSugar);
           this.shopIce=itemView.findViewById(R.id.orderedIce);
           this.shopAmount=itemView.findViewById(R.id.orderedAmount);
           this.shopDollar=itemView.findViewById(R.id.orderedPrice);

       }
   }
}
