package app.myproject.yujincoffee_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;

import app.myproject.yujincoffee_app.Part2.MenuListActivity;
import app.myproject.yujincoffee_app.databinding.ActivityIndextPageBinding;
import app.myproject.yujincoffee_app.databinding.ActivityLogPageBinding;

public class indextPageActivity extends AppCompatActivity {

    ActivityIndextPageBinding binding;
    SharedPreferences memberDataPre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityIndextPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.memberDataImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(indextPageActivity.this,memberdataaPageActivity.class);
                startActivity(intent);
            }
        });
        binding.newsImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(indextPageActivity.this,seasonnew.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //用id判斷點了哪個選項

        if (id == R.id.membersetting) {

            Intent intent = new Intent(indextPageActivity.this, memberdataaPageActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.myorder) {
            Intent intent = new Intent(indextPageActivity.this, MyOrderActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.itemmenu) {
            Intent intent = new Intent(indextPageActivity.this, MenuListActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.historyorder) {
            Intent intent = new Intent(indextPageActivity.this, HistoryOrderActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.myfavorite) {
            Intent intent = new Intent(indextPageActivity.this, MyFavoriteActivity.class);
            startActivity(intent);

        }else if (id == R.id.pointchange) {
            Intent intent = new Intent(indextPageActivity.this, PointChangeActivity.class);
            startActivity(intent);

        }else if (id == R.id.logout) {
            AlertDialog.Builder logoutbtn = new AlertDialog.Builder(indextPageActivity.this);
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
                    Intent intent = new Intent(indextPageActivity.this, logPageActivity.class);
                    startActivity(intent);
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
        return true;
    }

}