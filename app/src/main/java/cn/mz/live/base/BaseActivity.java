package cn.mz.live.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/11.
 */

public abstract class BaseActivity extends AppCompatActivity{

    private Unbinder binder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);
        setContentView(getLayoutId());
        binder = ButterKnife.bind(this);
        initViews();
        initToolbar();
    }

    public abstract int getLayoutId();

    public abstract void initViews();

    public abstract void initData(Bundle savedInstanceState);

    public abstract void initToolbar();


    /** 初始化 Toolbar */
    public void setToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    public void setToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        setToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    public void toNextActivity(Class<?> clz, boolean CanFinish) {
        startActivity(new Intent(this, clz));
        if (CanFinish){
            this.finish();
        }
    }

    public void toNextActivityForResult(Class<?> clz, int requestCode){
        startActivityForResult(new Intent(this, clz), requestCode);
    }

    @SuppressLint("RestrictedApi")
    public void toNextActivityForResult(Class<?> clz, int requestCode, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode, bundle);
    }

    public void toNextActivity(Class<?> clz, Bundle bundle, boolean CanFinish) {
        Intent intent = new Intent(this,clz);
        if (!bundle.isEmpty()) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (CanFinish){
            this.finish();
        }
    }

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binder.unbind();
    }

    protected <T extends View> T findView(int id) {
        return (T)findViewById(id);
    }

}
