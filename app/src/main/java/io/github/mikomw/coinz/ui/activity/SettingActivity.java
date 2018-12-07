package io.github.mikomw.coinz.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import io.github.mikomw.coinz.R;

/**
 * The setting activity enables our user to set their preference and log out.
 *
 * @author Songbo Hu
 */
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
        mGroupListView=findViewById(R.id.group_list_setting);


        QMUICommonListItemView account_setting = mGroupListView.createItemView("Account and Security");
        account_setting.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView language_setting = mGroupListView.createItemView("Language");
        language_setting.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUIGroupListView.newSection(this)
                .addItemView(account_setting,null)
                .addItemView(language_setting,null)
                .addTo(mGroupListView);

        QMUICommonListItemView perference_setting = mGroupListView.createItemView("Preference");
        perference_setting.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUIGroupListView.newSection(this)
                .addItemView(perference_setting,null)
                .addTo(mGroupListView);

        QMUICommonListItemView accessible_mode_setting = mGroupListView.createItemView("Accessible Mode");
        accessible_mode_setting.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        accessible_mode_setting.setDetailText("Enable color blind mode.");

        QMUICommonListItemView enlarge_text_mode_setting = mGroupListView.createItemView("Enlarge Text Mode");
        enlarge_text_mode_setting.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);

        QMUIGroupListView.newSection(this)
                .addItemView(accessible_mode_setting,null)
                .addItemView(enlarge_text_mode_setting,null)
                .addTo(mGroupListView);

        QMUICommonListItemView ratting_setting = mGroupListView.createItemView("Rating CoinZ!");
        ratting_setting.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUIGroupListView.newSection(this)
                .addItemView(ratting_setting,null)
                .addTo(mGroupListView);

        QMUICommonListItemView legal_setting = mGroupListView.createItemView("Terms and Conditions");
        legal_setting.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUICommonListItemView about_setting = mGroupListView.createItemView("About");
        about_setting.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUIGroupListView.newSection(this)
                .addItemView(legal_setting,null)
                .addItemView(about_setting,null)
                .addTo(mGroupListView);


        // User can log out and try to log in with new account.
        QMUICommonListItemView logout = mGroupListView.createItemView("Log Out");
        logout.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUIGroupListView.newSection(this)
                .addItemView(logout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SettingActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        SettingActivity.this.finish();
                    }
                })
                .addTo(mGroupListView);


    }


}
