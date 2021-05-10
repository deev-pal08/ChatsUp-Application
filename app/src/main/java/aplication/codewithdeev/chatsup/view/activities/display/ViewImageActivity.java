package aplication.codewithdeev.chatsup.view.activities.display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.ImageButton;

import com.jsibbold.zoomage.ZoomageView;

import aplication.codewithdeev.chatsup.R;
import aplication.codewithdeev.chatsup.common.Common;
import aplication.codewithdeev.chatsup.databinding.ActivityViewImageBinding;

public class ViewImageActivity extends AppCompatActivity {

    private ActivityViewImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_image);

        binding.imageView.setImageBitmap(Common.IMAGE_BITMAP);
    }
}