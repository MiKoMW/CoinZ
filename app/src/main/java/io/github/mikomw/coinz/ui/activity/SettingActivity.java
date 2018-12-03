package io.github.mikomw.coinz.ui.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import io.github.mikomw.coinz.R;

public class SettingActivity extends AppCompatActivity {
    String tag = "SettingActivity";
    QMUIGroupListView mGroupListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_bar);
        toolbar.setTitle("Setting");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click");
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
}
