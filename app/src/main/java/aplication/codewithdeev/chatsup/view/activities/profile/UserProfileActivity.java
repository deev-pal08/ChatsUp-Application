package aplication.codewithdeev.chatsup.view.activities.profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Objects;

import aplication.codewithdeev.chatsup.R;
import aplication.codewithdeev.chatsup.databinding.ActivityUserProfileBinding;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_profile);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String receiverID = intent.getStringExtra("userID");
        String userProfile = intent.getStringExtra("userProfile");


        if (receiverID!=null){

            binding.toolbar.setTitle(userName);
            if (userProfile != null) {
                if (userProfile.equals("")){
                    binding.imageProfile.setImageResource(R.drawable.user_image);  // set  default image when profile user is null
                } else {
                    Glide.with(this).load(userProfile).into( binding.imageProfile);
                }
            }
        }
        initToolbar();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initToolbar() {

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}