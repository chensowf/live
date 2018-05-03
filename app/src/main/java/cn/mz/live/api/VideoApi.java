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

    public static final String SKVideoType = "SKVideoType";
    public static final String MeiMiVideoType = "MeiMiVideoType";

    static String baseUrl = "http://www.lengmi.cc/page/";

    static String skBaseUrl = "http://www.ppdytt.com/ll-hg/p-";

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

    public static List<VideoBean> getVideoList(int page, String type) {
        String url = null;
        switch (type)
        {
            case SKVideoType:
                url = String.format("%s%d",skBaseUrl,page);
                break;
            case MeiMiVideoType:
                url = String.format("%s%d",baseUrl);
                break;
        }
        Document document = null;
        List<VideoBean> datas = null;
        try {
            document = Jsoup.connect(url).get();
            switch (type){
                case MeiMiVideoType:
                    datas = parserMeiMi(document);
                    break;
                case SKVideoType:
                    datas = parserSK(document);
                    break;
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

    private static List<VideoBean> parserSK(Document document)
    {
        List<VideoBean> datas = new ArrayList<>();
        Elements elementImgs = document.getElementsByClass("v_img");
        Elements elementTitles = document.getElementsByClass("v_title");

        for(int i = 0; i < elementImgs.size(); i++)
        {
            Element elementImg = elementImgs.get(i);
            Element elementTitle = elementTitles.get(i);
            Elements a = elementImg.select("a");
            Elements img = elementImg.select("img");
            Elements aa = elementTitle.select("a");
            String imgUrl = img.attr("data-original");
            String nextPage = a.attr("href");
            String title = aa.text();
            VideoBean imageItem = new VideoBean(title, "http://www.ppdytt.com"+nextPage,imgUrl);
            datas.add(imageItem);
        }
        return datas;
    }

    private static List<VideoBean> parserMeiMi(Document document)
    {
        List<VideoBean> datas = new ArrayList<>();
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
        return datas;
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
