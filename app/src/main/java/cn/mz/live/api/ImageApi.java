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

import cn.mz.live.bean.ImagesBean;

/**
 * Created by Admin on 2018/5/4.
 */

public class ImageApi {

    static String baseUrl = "http://www.umei.cc/meinvtupian/xingganmeinv/";

    public static final String ImageGroupsListType = "ImageGroupsListType";
    public static final String ImagesListType = "ImagesListType";

    public static void test()
    {
        try {
            Connection connection = Jsoup.connect(baseUrl);
            Connection.Response response = connection.execute();

            Log.e("heard",response.headers().toString());
            Log.e("cookie",response.cookies().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ImagesBean> getImagesList(int page, String type)
    {
        List<ImagesBean> imagesBeanList = new ArrayList<>();
        String url = null;
        switch (type)
        {
            case ImageGroupsListType:
                url = String.format("%s%d.htm",baseUrl,page);
                break;
            case ImagesListType:
                break;
        }
        try{
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("TypeList");
            Elements elementsLi = elements.select("li");
            for(Element element:elementsLi)
            {
                Element elementA = element.selectFirst("a");
                String nextPage = elementA.attr("href");
                Element elementImg = element.selectFirst("img");
                String imgUrl = elementImg.attr("src");
                Element elementTitle = element.selectFirst("div");
                String title = elementTitle.ownText();
                Log.e("title",title);
                Log.e("imgUrl",imgUrl);
                Log.e("nextPage",nextPage);
                ImagesBean imagesBean = new ImagesBean(title,imgUrl,url,null,nextPage);
                imagesBeanList.add(imagesBean);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagesBeanList;
    }

}
