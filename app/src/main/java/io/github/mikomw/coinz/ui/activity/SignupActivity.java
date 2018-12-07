package io.github.mikomw.coinz.ui.activity;

import io.github.mikomw.coinz.R;
import io.github.mikomw.coinz.util.uploadUserData;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A sign up activity which ask user to sign up with their email and password.
 * Initialize the user data locally and upload them to the remote database.
 *
 * @author Songbo Hu
 * @author WZH
 */

public class SignupActivity  extends FragmentActivity implements View.OnClickListener  {
    String tag = "SignupActivity";
    private ImageView logo;
    private ScrollView scrollView;
    private EditText login_email;
    private EditText login_password;
    private ImageView iv_clean_phone;
    private ImageView clean_password;
    private ImageView iv_show_pwd;
    private Button btn_signup;
    private TextView back_login;


    private int screenHeight = 0;
    private int keyHeight = 0;
    private float scale = 0.6f;
    private View service,content;
    private int height = 0 ;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        initListener();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(tag, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(tag, "onAuthStateChanged:signed_out");
                }
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initView() {
        logo = (ImageView) findViewById(R.id.logo);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        login_email = (EditText) findViewById(R.id.et_mobile);
        login_password = (EditText) findViewById(R.id.et_password);
        iv_clean_phone = (ImageView) findViewById(R.id.iv_clean_phone);
        clean_password = (ImageView) findViewById(R.id.clean_password);
        iv_show_pwd = (ImageView) findViewById(R.id.iv_show_pwd);
        btn_signup = (Button) findViewById(R.id.btn_register);
        service = findViewById(R.id.service);
        content = findViewById(R.id.content);
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3
        back_login = (TextView) findViewById(R.id.back_login);
    }

    private void initListener() {
        iv_clean_phone.setOnClickListener(this);
        clean_password.setOnClickListener(this);
        iv_show_pwd.setOnClickListener(this);
        back_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);

        login_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.getVisibility() == View.GONE) {
                    iv_clean_phone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone.setVisibility(View.GONE);
                }
            }
        });


        login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && clean_password.getVisibility() == View.GONE) {
                    clean_password.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    clean_password.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(SignupActivity.this, R.string.please_input_limit_pwd, Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    login_password.setSelection(s.length());
                }
            }
        });


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        scrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
              /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    int dist = content.getBottom() - bottom;
                    if (dist>0){
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        zoomIn(logo, dist);
                    }
                    service.setVisibility(View.INVISIBLE);

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    if ((content.getBottom() - oldBottom)>0){
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", content.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                        zoomOut(logo);
                    }
                    service.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    /**
     * 缩小
     * @param view
     */
    public void zoomIn(final View view, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    /**
     * f放大
     * @param view
     */
    public void zoomOut(final View view) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_clean_phone:
                login_email.setText("");
                break;
            case R.id.clean_password:
                login_password.setText("");
                break;
            case R.id.iv_show_pwd:
                if (login_password.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_show_pwd.setImageResource(R.drawable.pass_visuable);
                } else {
                    login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_show_pwd.setImageResource(R.drawable.pass_gone);
                }
                String pwd = login_password.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    login_password.setSelection(pwd.length());
                break;
            case R.id.btn_register:
                createAccount(login_email.getText().toString(), login_password.getText().toString());

                break;
            case R.id.back_login:
                System.out.println("Login Again!");
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

        }


    }


    private void createAccount(String email, String password) {
        Log.d(tag, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(tag, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if(task.isSuccessful()){
                            String Uid = mAuth.getCurrentUser().getUid();
                            uploadUserData uploadUserData = new uploadUserData(SignupActivity.this);
                            uploadUserData.execute(Uid);
                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    private boolean validateForm() {
        boolean valid = true;

        String email = login_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            login_email.setError("Required.");
            valid = false;
        } else {
            login_email.setError(null);
        }

        String password = login_email.getText().toString();
        if (TextUtils.isEmpty(password)) {
            login_password.setError("Required.");
            valid = false;
        } else {
            login_password.setError(null);
        }
        if (password.length() <= 5) {
            login_password.setError("Try a longer password.");
        } else {
            login_password.setError(null);
        }
        if (!email.contains("@")) {
            login_email.setError("Not a valid email");
        } else {
            login_email.setError(null);
        }

        return valid;
    }


    public void onBackPressed() {
        finish();
    }

    public void jumpToMap(){
        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.putExtra("Uid",mAuth.getUid());
        startActivity(mapIntent);
        this.finish();
    }

}
