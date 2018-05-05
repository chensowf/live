package cn.mz.live.api;

import android.util.Log;


import org.jsoup.Connection;
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

    static String skBaseUrl = "https://www.77dvd.cc/";

    private static String TAG = "VideoApi";


    public static String getSKVideoUrl(String url)
    {
        try{
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("script");
            for(Element element:elements)
            {
                Log.e("video",element.html());
                if(element.html().contains("var VideoInfoList=\"M云播$$中字$"))
                {
                    String text = element.html();
                    return text.substring(text.indexOf("http"), text.length() - 4);
                }
                else if(element.html().contains("var VideoInfoList=\"M云播$$备用$"))
                {
                    String text = element.html();
                    if(text.contains("$se#"))
                    {
                        Log.e("video",text);
                        return text.substring(text.indexOf("http"), text.indexOf("$se#"));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getVideoUrl(String url) {
        System.out.println("url = [" + url + "]");
        try {
            Document document = Jsoup.connect(url).get();
            Elements script = document.select("script");
            for (Element element : script) {
                if (element.html().contains("var player = ")) {
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
                url = String.format("%s%s",skBaseUrl,"html/35-"+page+".html");
                break;
            case MeiMiVideoType:
                url = String.format("%s%d",baseUrl,page);
                break;
        }
        Document document = null;
        List<VideoBean> datas = null;
        try {
            document = Jsoup.connect(url).get();
            Log.e("lentmi","123345");
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
        Elements elementsA = document.getElementsByClass("link");
        Elements elementsImg = document.getElementsByClass("lazy");
        for(int i = 0; i < elementsA.size(); i++)
        {
            Element a = elementsA.get(i);
            String nextPage = a.attr("href");
            nextPage = getSubUtilSimple(nextPage,numRegex);
            String title = a.attr("title");
            Element img = elementsImg.get(i);
            String imgUrl = img.attr("src");
            Log.e(TAG, "imageURL:" + nextPage);
            VideoBean imageItem = new VideoBean(title,skBaseUrl+"/v/"+nextPage+"-0-0.html",imgUrl);
            datas.add(imageItem);
        }
        return datas;
    }

    private static List<VideoBean> parserMeiMi(Document document)
    {
        Log.e("lentmi","123345123");
        List<VideoBean> datas = new ArrayList<>();
        Elements elements = document.getElementsByClass("m-lp");
        for (Element element : elements) {
            Element imgElement = element.getElementsByClass("img").get(0);
            Elements a = imgElement.select("a");
            Elements img = imgElement.select("img");
            String nextPage = a.attr("href");
            String title = a.attr("title");
            String imageurl = img.attr("src");

            VideoBean imageItem = new VideoBean(title, nextPage, "http://www.lengmi.cc/" + getSubUtilSimple(imageurl, rgex));
            //imageItem.setVideoUrl(getVideoUrl(nextPage));
            datas.add(imageItem);
        }
        Log.e("lentmi","1233453434");
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

    private static String numRegex = "(\\d+).*";

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
