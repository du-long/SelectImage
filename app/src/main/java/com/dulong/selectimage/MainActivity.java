package com.dulong.selectimage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> mResults = new ArrayList<>();
    private static final int REQUEST_CODE = 123;
    private PictureAdapter pictureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        pictureAdapter = new PictureAdapter(this, mResults);
        RecyclerView rv_picture = (RecyclerView) findViewById(R.id.rv_picture);
        rv_picture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_picture.setAdapter(pictureAdapter);
        pictureAdapter.setOnItemClickListener(new RecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, int itemType) {
                if (itemType == PictureAdapter.TYPE_ADD) {
                    Intent intent = new Intent(MainActivity.this, ImagesSelectorActivity.class);
                    // max number of images to be selected
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
                    // min size of image which will be shown; to filter tiny images (mainly icons)
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                    // show camera or not
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                    // pass current selected images as the initial value
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                    // start the selector
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Toast.makeText(MainActivity.this, "图片", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS) != null;
                mResults.clear();
                mResults.addAll(data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS));
                pictureAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
