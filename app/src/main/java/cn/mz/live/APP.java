package cn.mz.live;

import android.app.Application;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import cn.jiguang.share.android.api.JShareInterface;
import cn.mz.live.api.VideoApi;
import okhttp3.OkHttpClient;

public class APP extends Application{
    public static APP app;

    public static APP get() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initOkgo();
        initJGShare();
    }

    private void initJGShare(){
        JShareInterface.setDebugMode(false);
        JShareInterface.init(getApplicationContext());
    }

    private void initOkgo(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();//构建OkHttpClient.Builder

       /* HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);*/

        //全局的读取超时时间
        builder.readTimeout(10, TimeUnit.SECONDS);
        //全局的写入超时时间
        builder.writeTimeout(10, TimeUnit.SECONDS);
        //全局的连接超时时间
        builder.connectTimeout(10, TimeUnit.SECONDS);

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setRetryCount(2);
        //全局统一超时重连次数，默认为三次，不需要可以设置为0

    }

}
