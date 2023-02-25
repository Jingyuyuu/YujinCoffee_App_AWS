package app.myproject.yujincoffee_app.Adapter;

import android.content.res.Resources;
import android.database.Cursor;
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
import app.myproject.yujincoffee_app.Part2.DrinkMenuClickListener;
import app.myproject.yujincoffee_app.R;


public class DrinkMenuAdapter extends RecyclerView.Adapter <DrinkMenuAdapter.ViewHolder>{
    //製作資料SQLite drinkmenu
    private SQLiteDatabase db;
    private DrinkMenuClickListener listener;
    private ArrayList<DrinkModel> item;
    private Resources resources;
    //放置圖片的陣列
    private int [] resArray=new int[74] ;
    //圖片ID
    private int resID;


    public DrinkMenuAdapter(SQLiteDatabase db, DrinkMenuClickListener listener, ArrayList<DrinkModel> item, Resources resources) {
        this.db = db;
        this.listener = listener;
        this.item = item;
        this.resources = resources;

    }

    public void getAllProductData() {
        Cursor cursor = db.rawQuery("select * from product;", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int i = 0;
            do {
                resID = resources.getIdentifier(cursor.getString(6), "drawable"
                        , "app.myproject.yujincoffee_app");
                resArray[i] = resID;
                DrinkModel drinkmodel = new DrinkModel(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        resID
                );
                item.add(drinkmodel);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void setSeries(String series){
        item.clear();
        Cursor cursor=null;
        switch (series){
            case "【全系列商品】":
                cursor = db.rawQuery("select * from product;" , null );
                break;
            case "【芝芝茗茶】系列":
                cursor = db.rawQuery("select * from product where series=1;" , null );
                break;
            case "【奶茶】系列":
                cursor = db.rawQuery("select * from product where series=2;" , null );
                break;
            case "【芝芝水果茶】系列":
                cursor = db.rawQuery("select * from product where series=3;" , null );
                break;
            case "【滿杯水果家族】系列":
                cursor = db.rawQuery("select * from product where series=4;" , null );
                break;
            case "【新式茶飲】系列":
                cursor = db.rawQuery("select * from product where series=5;" , null );
                break;
            case "【啵啵家族】系列":
                cursor = db.rawQuery("select * from product where series=6;" , null );
                break;
            case "【咖啡家族】系列":
                cursor = db.rawQuery("select * from product where series=8;" , null );
                break;
            case "【麵包】系列":
                cursor = db.rawQuery("select * from product where series=9;" , null );
                break;
            case "【蛋糕】系列":
                cursor = db.rawQuery("select * from product where series=10;" , null );
                break;
        }
        if(cursor!= null || cursor.getCount()>0){
            cursor.moveToFirst();
            int i=0;
            do{
                resID=resources.getIdentifier(cursor.getString(6),"drawable"
                        ,"app.myproject.yujincoffee_app");
                resArray[i]=resID;
                DrinkModel a = new DrinkModel(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        resID
                );
                item.add(a);
                i++;
            }while(cursor.moveToNext());
        } else {
            //無資料
            item.clear();
        }
        cursor.close();
    }






    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drinkitem_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imagView.setImageResource(resArray[position]);
        holder.txtName.setText(item.get(position).getTxtName());
        holder.txtPrice.setText(item.get(position).getTxtPrice()+" 元");
        holder.txtCarlorie.setText(item.get(position).getTxtCalorie()+" 卡");
        holder.txt1.setText(item.get(position).getTxt1());
        holder.txt2.setText(item.get(position).getTxt2());
        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = holder.getAdapterPosition();
                int series = item.get(pos).getSeries();
                String name = item.get(pos).getTxtName();
                int tem= item.get(pos).getTem();
                int price = item.get(pos).getTxtPrice();
                int carlorie = item.get(pos).getTxtCalorie();
                listener.onClick((pos+1),series,name,tem,carlorie,price,resArray[pos]);
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    //自己建立一個自己的ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagView;
        TextView txtName;
        TextView txtPrice;
        TextView txtCarlorie;
        TextView txt1;
        TextView txt2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txt1=itemView.findViewById(R.id.txt1);
            this.txt2=itemView.findViewById(R.id.txt2);
            this.imagView = itemView.findViewById(R.id.imagView);
            this.txtName=itemView.findViewById(R.id.txtName);
            this.txtPrice=itemView.findViewById(R.id.txtPrice);
            this.txtCarlorie=itemView.findViewById(R.id.txtCarlorie);
        }
    }
}
