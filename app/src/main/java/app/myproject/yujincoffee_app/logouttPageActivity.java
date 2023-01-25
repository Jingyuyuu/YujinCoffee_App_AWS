package app.myproject.yujincoffee_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import app.myproject.yujincoffee_app.databinding.ActivityLogouttPageBinding;

public class logouttPageActivity extends AppCompatActivity {
    SharedPreferences memberDataPre;
    ActivityLogouttPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLogouttPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberDataPre= getSharedPreferences("memberDataPre", MODE_PRIVATE);
                SharedPreferences.Editor editor=memberDataPre.edit();
                editor.remove("name");
                editor.remove("points");
                editor.remove("phone");
                editor.remove("email");
                editor.apply();
                Log.e("JSON","已經移除memberDataPre資料");
                Intent intent = new Intent(logouttPageActivity.this, logPageActivity.class);
                startActivity(intent);


            }
        });

    }
}