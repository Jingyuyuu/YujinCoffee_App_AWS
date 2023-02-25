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
import android.view.View;

import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityLogPageBinding;
import app.myproject.yujincoffee_app.databinding.ActivitySeasonnewBinding;

public class seasonnew extends AppCompatActivity {

    ActivitySeasonnewBinding binding;
    SharedPreferences memberDataPre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySeasonnewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //返回鍵
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(seasonnew.this,indextPageActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //製作Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        //用id判斷點了哪個選項
        if(id == R.id.indext){
            Intent intent=new Intent(seasonnew.this,indextPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.membersetting){
            Intent intent=new Intent(seasonnew.this,memberdataaPageActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.myorder){
            Intent intent=new Intent(seasonnew.this,MyOrderActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.itemmenu){
            Intent intent=new Intent(seasonnew.this, MenuListActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.historyorder){
            Intent intent=new Intent(seasonnew.this,HistoryOrderActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.storelists){
            Intent intent=new Intent(seasonnew.this,storelistActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.pointchange){
            Intent intent=new Intent(seasonnew.this,PointChangeActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.logout){

            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(seasonnew.this);
            logoutbtn.setTitle("登出");
            logoutbtn.setMessage("確定要登出嗎?");
            logoutbtn.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    memberDataPre= getSharedPreferences("memberDataPre", MODE_PRIVATE);
                    SharedPreferences.Editor editor=memberDataPre.edit();
                    editor.remove("name");
                    editor.remove("points");
                    editor.remove("phone");
                    editor.remove("email");
                    editor.apply();
                    Intent intent = new Intent(seasonnew.this, logPageActivity.class);
                    startActivity(intent);
                }
            });
            logoutbtn.setNegativeButton("否", new DialogInterface.OnClickListener() {
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