package app.myproject.yujincoffee_app.Adapter;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.myproject.yujincoffee_app.Model.Product.DrinkModel;
import app.myproject.yujincoffee_app.Part2.OrderListListener;
import app.myproject.yujincoffee_app.R;

public class OrderListAdapter extends RecyclerView.Adapter <OrderListAdapter.ViewHolder>{
    private SQLiteDatabase db;
    private OrderListListener listener;
    private ArrayList<DrinkModel> item;
    private String series;


    public OrderListAdapter(SQLiteDatabase db, OrderListListener listener, ArrayList<DrinkModel> item) {
        this.db = db;
        this.listener = listener;
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productorder_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(item.get(position).getImage());
        holder.textSeries.setText(getProductSeriesName(item.get(position).getSeries()));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getAdapterPosition();
                listener.onClick(pos,item.get(pos).getSeries(),
                        item.get(pos).getTxtName(),item.get(pos).getTem(),
                        item.get(pos).getTxtCalorie(),item.get(pos).getTxtPrice(),
                        item.get(pos).getImage(),item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView textSeries;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image=itemView.findViewById(R.id.imageHolder);
            this.textSeries=itemView.findViewById(R.id.textSeries);
        }
    }

    //獲取產品系列的名稱
    public String getProductSeriesName(int series){
        switch(series){
            case 1:
                return "芝芝茗茶";
            case 2:
                return "奶茶";
            case 3:
                return "芝芝水果茶";
            case 4:
                return "滿杯水果家族";
            case 5:
                return "新式飲品";
            case 6:
                return  "啵啵家族";
            case 7:
                return "冬季飲品";
            case 8:
                return "咖啡家族";
            case 9:
                return "麵包";
            case 10:
                return "蛋糕";
            default:
                return "沒有這個品項";
        }
    }
}
