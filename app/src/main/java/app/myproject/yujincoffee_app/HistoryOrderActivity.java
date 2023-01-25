package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityHistoryOrderBinding;

public class HistoryOrderActivity extends AppCompatActivity {
    //從我的訂單頁面(MyOrderActivity)抓取資料放置於歷史訂單頁面(HistoryOrderActivity)
    //歷史訂單資料不會被清除
    ActivityHistoryOrderBinding binding;
    SharedPreferences memberDataPre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHistoryOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //返回鍵
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        //用id判斷點了哪個選項
        if(id == R.id.membersetting){
            Intent intent=new Intent(HistoryOrderActivity.this,memberdataaPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(HistoryOrderActivity.this,MyOrderActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(HistoryOrderActivity.this, MenuListActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(HistoryOrderActivity.this,HistoryOrderActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myfavorite){
            Intent intent=new Intent(HistoryOrderActivity.this,MyFavoriteActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.storelists){
            Intent intent=new Intent(HistoryOrderActivity.this,storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(HistoryOrderActivity.this,PointChangeActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.logout){

            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(HistoryOrderActivity.this);
            logoutbtn.setTitle("登出");
            logoutbtn.setMessage("確定要登出嗎?");
            logoutbtn.setNegativeButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    memberDataPre= getSharedPreferences("memberDataPre", MODE_PRIVATE);
                    SharedPreferences.Editor editor=memberDataPre.edit();
                    editor.remove("name");
                    editor.remove("points");
                    editor.remove("phone");
                    editor.remove("email");
                    editor.apply();
                }
            });
            logoutbtn.setPositiveButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = logoutbtn.create();
            dialog.show();
        }
        else if(id ==android.R.id.home){
            //返回鍵動作
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}