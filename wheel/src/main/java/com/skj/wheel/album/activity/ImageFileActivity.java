package com.skj.wheel.album.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.skj.wheel.R;
import com.skj.wheel.album.adapter.FolderAdapter;
import com.skj.wheel.album.utils.CacheActivityUtil;

/**
 * Created by 孙科技 on 2017/7/17.
 */

public class ImageFileActivity extends AppCompatActivity implements View.OnClickListener {
    private FolderAdapter folderAdapter;
    private TextView title, back, other;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(this);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.plugin_camera_image_file);
        title = (TextView) findViewById(R.id.title);
        back = (TextView) findViewById(R.id.back);
        other = (TextView) findViewById(R.id.other);
        GridView gridView = (GridView) findViewById(R.id.fileGridView);
        folderAdapter = new FolderAdapter(this);
        gridView.setAdapter(folderAdapter);
        title.setText("选择相册");
    }

    private void initData() {
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();
        } else if (i == R.id.other) {
            CacheActivityUtil.finishOtherActivity(ImageFileActivity.class);
            finish();
        }
    }
}
