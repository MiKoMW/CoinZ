package io.github.mikomw.coinz.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.ui.activity.ui.mainactivity2.MainActivity2Fragment;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity2_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainActivity2Fragment.newInstance())
                    .commitNow();
        }
    }
}
