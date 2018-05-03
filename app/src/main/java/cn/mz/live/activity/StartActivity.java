package cn.mz.live.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.mz.live.APP;
import cn.mz.live.R;
import cn.mz.live.base.BaseActivity;
import cn.mz.live.bean.ConfigBean;
import cn.mz.live.utils.NetWorkUtils;
import cn.mz.live.utils.SP;

public class StartActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    public void initViews() {
        getNetConfig();
        initConfig();

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initToolbar() {

    }

    private void initConfig(){
        DateFormat df = new SimpleDateFormat("y-M-d");
        if (!SP.getString("xgm","time").equals(df.format(new Date()))){
            SP.putString("xgm","time",df.format(new Date()));
            SP.putInt("xgm","share",0);
        }else {
            if (SP.getInt("xgm","share")>1){
                SP.putInt("xgm","share",2);
            }

        }
    }

    private void getNetConfig(){
        if (NetWorkUtils.isAvailable(APP.get())) {
            OkGo.<String>get("http://meizi01.com:2018")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            String s = response.body();
                            try {
                                Gson gson = new Gson();
                                ConfigBean bean = gson.fromJson(s, ConfigBean.class);
                                String version = bean.version;
                                String upUrl = bean.upUrl;
                                String shareUrl = bean.shareUrl;
                                String bigAd = bean.bigAD.toString().replaceAll("[\\[\\]]", "");
                                String miniAd = bean.miniAD.toString().replaceAll("[\\[\\]]", "");
                                SP.putString("xgm", "bigAD", bigAd);
                                SP.putString("xgm", "miniAD", miniAd);
                                SP.putString("xgm", "upUrl", upUrl);
                                SP.putString("xgm", "shareUrl", shareUrl);
                                haveUpdate(version);
                                //toNextActivity(MainActivity.class,true);

                            }catch (Exception e){
                                showToast("初始化失败");
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            showToast("网络出出错 (" + response.code() + ")");
                            toNextActivity(MainActivity.class,true);
                        }
                    });
        }else {
            showToast("网络出出错");
        }
    }

    private void haveUpdate(String version){
        try {
            PackageManager pm = getPackageManager();
            //getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            String thisVer = pi.versionName;
            //String webVer = SP.getString("xgm","version");

            if (!thisVer.equals(version)){

                Bundle bundle = new Bundle();
                bundle.putInt("code", 1);
                toNextActivityForResult(ExitAdDialog.class, 1, bundle);

            }else {
                toNextActivity(MainActivity.class,true);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getBooleanExtra("do", false)){
            //TODO
            openWebView(SP.getString("xgm", "upUrl"));
        }else {
            toNextActivity(MainActivity.class,true);
        }
    }

    private void openWebView(String url){
        try{
            url = url.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            url =  URLDecoder.decode(url, "UTF-8");
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
