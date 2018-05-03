package cn.mz.live.api;

import android.util.Log;

import com.lzy.okgo.OkGo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.mz.live.bean.VideoBean;

/**
 * Created by molu_ on 2018/4/22.
 */

public class VideoApi {


    static String baseUrl = "http://www.lengmi.cc/page/";


    private static String TAG = "VideoApi";


    public static String getVideoUrl(String url) {


        System.out.println("url = [" + url + "]");

        try {
            Document document = Jsoup.connect(url).get();


            Elements script = document.select("script");


            for (Element element : script) {

                if (element.html().contains("var player = ")) {

                    //element.toString()


                    String videourl = getSubUtilSimple(element.toString(), file);

                    System.out.println("file = [" + videourl + "]");


                    return videourl.substring(2, videourl.length() - 1);
                }


            }


        } catch (IOException e) {
            e.printStackTrace();

            System.out.println("IOException = [" + e.toString() + "]");
        }


        return "";


    }

    public static List<VideoBean> getVideoList(int page, int type)
    {
        return null;
    }


    public static List<VideoBean> getVideoList(int page) {

        String url = String.format("%s%d", baseUrl, page);

        List<VideoBean> datas = new ArrayList<>();
        Document document = null;
        try {
            document = Jsoup.connect(url).get();

            Elements elements = document.getElementsByClass("m-lp");


            for (Element element : elements) {


                Element imgElement = element.getElementsByClass("img").get(0);

                Elements a = imgElement.select("a");

                Elements img = imgElement.select("img");


                String nextPage = a.attr("href");


                String title = a.attr("title");

                String imageurl = img.attr("src");

                Log.e(TAG, "imageURL:" + imageurl);

                VideoBean imageItem = new VideoBean(title, nextPage, "http://www.lengmi.cc/" + getSubUtilSimple(imageurl, rgex));


                //imageItem.setVideoUrl(getVideoUrl(nextPage));

                datas.add(imageItem);

            }


        } catch (IOException e) {
            e.printStackTrace();
            if (document == null)
                Log.e("document", "wei null");
            else
                Log.e("document", document.outerHtml());
        }

        return datas;
    }

    static String rgex = "src=(.*?)&h=";

    static String file = "file:(.*?),";

    //static String rgex_num = "\\[(.*?)P\\]";

    public static String getSubUtilSimple(String soap, String rgex) {
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }


}
