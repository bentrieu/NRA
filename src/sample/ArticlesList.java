package sample;

import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*This class will have many static array list*/

public class ArticlesList {
    public static ArrayList<Article> vnexpressNewsList = new ArrayList<>();
    public static ArrayList<Article> vnexpressCovidList = new ArrayList<>();
    public static ArrayList<Article> vnexpressPoliticsList = new ArrayList<>();
    public static ArrayList<Article> vnexpressBusinessList = new ArrayList<>();
    public static ArrayList<Article> vnexpressTechnologyList = new ArrayList<>();
    public static ArrayList<Article> vnexpressHealthList = new ArrayList<>();
    public static ArrayList<Article> vnexpressSportsList = new ArrayList<>();
    public static ArrayList<Article> vnexpressEntertainmentList = new ArrayList<>();
    public static ArrayList<Article> vnexpressWorldList = new ArrayList<>();

    public static ArrayList<Article> zingNewsList = new ArrayList<>();
    public static ArrayList<Article> zingCovidList = new ArrayList<>();
    public static ArrayList<Article> zingPoliticsList = new ArrayList<>();
    public static ArrayList<Article> zingBusinessList = new ArrayList<>();
    public static ArrayList<Article> zingTechnologyList = new ArrayList<>();
    public static ArrayList<Article> zingHealthList = new ArrayList<>();
    public static ArrayList<Article> zingSportsList = new ArrayList<>();
    public static ArrayList<Article> zingEntertainmentList = new ArrayList<>();
    public static ArrayList<Article> zingWorldList = new ArrayList<>();

    public static ArrayList<Article> tuoiTreNewsList = new ArrayList<>();
    public static ArrayList<Article> tuoiTreCovidList = new ArrayList<>();
    public static ArrayList<Article> tuoiTrePoliticsList = new ArrayList<>();
    public static ArrayList<Article> tuoiTreBusinessList = new ArrayList<>();
    public static ArrayList<Article> tuoiTreTechnologyList = new ArrayList<>();
    public static ArrayList<Article> tuoiTreHealthList = new ArrayList<>();
    public static ArrayList<Article> tuoiTreSportsList = new ArrayList<>();
    public static ArrayList<Article> tuoiTreEntertainmentList = new ArrayList<>();
    public static ArrayList<Article> tuoiTreWorldList = new ArrayList<>();

    public static ArrayList<Article> thanhNienNewsList = new ArrayList<>();
    public static ArrayList<Article> thanhNienCovidList = new ArrayList<>();
    public static ArrayList<Article> thanhNienPoliticsList = new ArrayList<>();
    public static ArrayList<Article> thanhNienBusinessList = new ArrayList<>();
    public static ArrayList<Article> thanhNienTechnologyList = new ArrayList<>();
    public static ArrayList<Article> thanhNienHealthList = new ArrayList<>();
    public static ArrayList<Article> thanhNienSportsList = new ArrayList<>();
    public static ArrayList<Article> thanhNienEntertainmentList = new ArrayList<>();
    public static ArrayList<Article> thanhNienWorldList = new ArrayList<>();

    public static ArrayList<Article> nhanDanNewsList = new ArrayList<>();
    public static ArrayList<Article> nhanDanCovidList = new ArrayList<>();
    public static ArrayList<Article> nhanDanPoliticsList = new ArrayList<>();
    public static ArrayList<Article> nhanDanBusinessList = new ArrayList<>();
    public static ArrayList<Article> nhanDanTechnologyList = new ArrayList<>();
    public static ArrayList<Article> nhanDanHealthList = new ArrayList<>();
    public static ArrayList<Article> nhanDanSportsList = new ArrayList<>();
    public static ArrayList<Article> nhanDanEntertainmentList = new ArrayList<>();
    public static ArrayList<Article> nhanDanWorldList = new ArrayList<>();

    public static ArrayList<Article> newsList = new ArrayList<>();
    public static ArrayList<Article> covidList = new ArrayList<>();
    public static ArrayList<Article> politicsList = new ArrayList<>();
    public static ArrayList<Article> businessList = new ArrayList<>();
    public static ArrayList<Article> technologyList = new ArrayList<>();
    public static ArrayList<Article> healthList = new ArrayList<>();
    public static ArrayList<Article> sportsList = new ArrayList<>();
    public static ArrayList<Article> entertainmentList = new ArrayList<>();
    public static ArrayList<Article> worldList = new ArrayList<>();

    public static ArrayList<Article> getNewsList() throws IOException {
        StopWatch stopWatch = new StopWatch("News");
        stopWatch.start("vnexpress");
        vnexpressNewsList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/tin-moi-nhat.rss", "News");
        stopWatch.stop();
        stopWatch.start("zing");
        zingNewsList = ArticlesManager.getZingWebList("https://zingnews.vn/", "News");
        stopWatch.stop();
        stopWatch.start("tuoitre");
        tuoiTreNewsList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/tin-moi-nhat.rss", "News");
        stopWatch.stop();
        stopWatch.start("thanhnien");
        thanhNienNewsList = ArticlesManager.getThanhNienList("https://thanhnien.vn/rss/home.rss", "News");
        stopWatch.stop();
        stopWatch.start("nhandan");
        nhanDanNewsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/", "News");
        stopWatch.stop();
        newsList = ArticlesManager.sortArticle(vnexpressNewsList, zingNewsList, tuoiTreNewsList, thanhNienNewsList, nhanDanNewsList);
        System.out.println(stopWatch.prettyPrint());
        return newsList;
    }

    public static ArrayList<Article> getCovidList() throws IOException {
        StopWatch stopWatch = new StopWatch("covid");
        stopWatch.start("vnexpress");
        vnexpressCovidList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/covid-19/tin-tuc", "Covid");
        stopWatch.stop();
        stopWatch.start("zing");
        zingCovidList = ArticlesManager.getZingSearchList("covid", "Covid");
        stopWatch.stop();
        stopWatch.start("tuoitre");
        tuoiTreCovidList = ArticlesManager.getTuoiTreSearchList("covid", "Covid");
        stopWatch.stop();
        stopWatch.start("thanhnien");
        thanhNienCovidList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/dich-covid-19/", "Politics");
        stopWatch.stop();
        stopWatch.start("nhandan");
        nhanDanCovidList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/tag/Covid19-53", "Covid");
        stopWatch.stop();
        covidList = ArticlesManager.getSortedArticlesList(vnexpressCovidList, zingCovidList, tuoiTreCovidList, thanhNienCovidList, nhanDanCovidList);
        System.out.println(stopWatch.prettyPrint());
        return covidList;
    }

    public static ArrayList<Article> getPoliticsList() throws IOException {
        StopWatch stopWatch = new StopWatch("Politics");
        stopWatch.start("vnexpress");
        vnexpressPoliticsList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/thoi-su/chinh-tri", "Politics");
        stopWatch.stop();
        stopWatch.start("zing");
        zingPoliticsList = ArticlesManager.getZingWebList("https://zingnews.vn/chinh-tri.html", "Politics");
        stopWatch.stop();
        stopWatch.start("tuoitre");
        tuoiTrePoliticsList = ArticlesManager.getTuoiTreSearchList("chính trị", "Politics");
        stopWatch.stop();
        stopWatch.start("thanhnien");
        thanhNienPoliticsList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/thoi-su/chinh-tri/", "Politics");
        stopWatch.stop();
        stopWatch.start("nhandan");
        nhanDanPoliticsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/chinhtri", "Politics");
        stopWatch.stop();
        politicsList = ArticlesManager.getSortedArticlesList(vnexpressPoliticsList, zingPoliticsList, tuoiTrePoliticsList, thanhNienPoliticsList, nhanDanPoliticsList);
        System.out.println(stopWatch.prettyPrint());
        return politicsList;
    }

    public static ArrayList<Article> getBusinessList() throws IOException {
        StopWatch stopWatch = new StopWatch("Business");
        stopWatch.start("vnexpress");
        vnexpressBusinessList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/kinh-doanh.rss", "Business");
        stopWatch.stop();
        stopWatch.start("zing");
        zingBusinessList = ArticlesManager.getZingWebList("https://zingnews.vn/kinh-doanh-tai-chinh.html", "Business");
        stopWatch.stop();
        stopWatch.start("tuoitre");
        tuoiTreBusinessList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/kinh-doanh.rss", "Business");
        stopWatch.stop();
        stopWatch.start("thanhnien");
        thanhNienBusinessList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/tai-chinh-kinh-doanh/", "Business");
        stopWatch.stop();
        stopWatch.start("nhandan");
        nhanDanBusinessList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/kinhte", "Business");
        stopWatch.stop();
        businessList = ArticlesManager.getSortedArticlesList(vnexpressBusinessList, zingBusinessList, tuoiTreBusinessList, thanhNienBusinessList, nhanDanBusinessList);
        System.out.println(stopWatch.prettyPrint());
        return businessList;
    }

    public static ArrayList<Article> getTechnologyList() throws IOException {
        StopWatch stopWatch = new StopWatch("Technology");
        stopWatch.start("vnexpress");
        vnexpressTechnologyList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/so-hoa.rss", "Technology");
        stopWatch.stop();
        stopWatch.start("zing");
        zingTechnologyList = ArticlesManager.getZingWebList("https://zingnews.vn/cong-nghe.html", "Technology");
        stopWatch.stop();
        stopWatch.start("tuoitre");
        tuoiTreTechnologyList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/nhip-song-so.rss", "Technology");
        stopWatch.stop();
        stopWatch.start("thanhnien");
        thanhNienTechnologyList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/cong-nghe/", "Technology");
        stopWatch.stop();
        stopWatch.start("nhandan");
        nhanDanTechnologyList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/khoahoc-congnghe", "Technology");
        stopWatch.stop();
        technologyList = ArticlesManager.getSortedArticlesList(vnexpressTechnologyList, zingTechnologyList, tuoiTreTechnologyList, thanhNienTechnologyList, nhanDanTechnologyList);
        System.out.println(stopWatch.prettyPrint());
        return technologyList;
    }

    public static ArrayList<Article> getHealthList() throws IOException {
        StopWatch stopWatch = new StopWatch("Health");
        stopWatch.start("vnexpress");
//        vnexpressHealthList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/suc-khoe.rss", "Health");
        vnexpressHealthList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/suc-khoe", "Health");
        stopWatch.stop();
        stopWatch.start("zing");
        zingHealthList = ArticlesManager.getZingWebList("https://zingnews.vn/suc-khoe.html", "Health");
        stopWatch.stop();
        stopWatch.start("tuoitre");
        tuoiTreHealthList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/suc-khoe.rss", "Health");
        stopWatch.stop();
        stopWatch.start("thanhnien");
        thanhNienHealthList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/suc-khoe/", "Health");
        stopWatch.stop();
        stopWatch.start("nhandan");
        nhanDanHealthList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/y-te", "Health");
        stopWatch.stop();
        healthList = ArticlesManager.getSortedArticlesList(vnexpressHealthList, zingHealthList, tuoiTreHealthList, thanhNienHealthList, nhanDanHealthList);
        System.out.println(stopWatch.prettyPrint());
        return healthList;
    }

    public static ArrayList<Article> getSportsList() throws IOException {
        StopWatch stopWatch = new StopWatch("Sports");
        stopWatch.start("vnexpress");
        vnexpressSportsList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/the-thao.rss", "Sports");
        stopWatch.stop();
        stopWatch.start("zing");
        zingSportsList = ArticlesManager.getZingWebList("https://zingnews.vn/the-thao.html", "Sports");
        stopWatch.stop();
        stopWatch.start("tuoitre");
        tuoiTreSportsList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-thao.rss", "Sports");
        stopWatch.stop();
        stopWatch.start("thanhnien");
        thanhNienSportsList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/the-thao/", "Sports");
        stopWatch.stop();
        stopWatch.start("nhandan");
        nhanDanSportsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/thethao", "Sports");
        stopWatch.stop();
        sportsList = ArticlesManager.getSortedArticlesList(vnexpressSportsList, zingSportsList, tuoiTreSportsList, thanhNienSportsList, nhanDanSportsList);
        System.out.println(stopWatch.prettyPrint());
        return sportsList;
    }

    public static ArrayList<Article> getEntertainmentList() throws IOException {
        StopWatch stopWatch = new StopWatch("Entertainment");
        stopWatch.start("vnexpress");
        vnexpressEntertainmentList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/cuoi.rss", "Entertainment");
        stopWatch.stop();
        stopWatch.start("zing");
        zingEntertainmentList = ArticlesManager.getZingWebList("https://zingnews.vn/giai-tri.html", "Entertainment");
        stopWatch.stop();
        stopWatch.start("tuoitre");
        tuoiTreEntertainmentList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/giai-tri.rss", "Entertainment");
        stopWatch.stop();
        stopWatch.start("thanhnien");
        thanhNienEntertainmentList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/giai-tri/", "Entertainment");
        stopWatch.stop();
        stopWatch.start("nhandan");
        nhanDanEntertainmentList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/vanhoa", "Entertainment");
        stopWatch.stop();
        entertainmentList = ArticlesManager.getSortedArticlesList(vnexpressEntertainmentList, zingEntertainmentList, tuoiTreEntertainmentList, thanhNienEntertainmentList, nhanDanEntertainmentList);
        System.out.println(stopWatch.prettyPrint());
        return entertainmentList;
    }

    public static ArrayList<Article> getWorldList() throws IOException {
        StopWatch stopWatch = new StopWatch("World");
        stopWatch.start("vnexpress");
        vnexpressWorldList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/the-gioi.rss", "World");
        stopWatch.stop();
        stopWatch.start("zing");
        zingWorldList = ArticlesManager.getZingWebList("https://zingnews.vn/the-gioi.html", "World");
        stopWatch.stop();
        stopWatch.start("tuoitre");
        tuoiTreWorldList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-gioi.rss", "World");
        stopWatch.stop();
        stopWatch.start("thanhnien");
        thanhNienWorldList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/the-gioi/", "World");
        stopWatch.stop();
        stopWatch.start("nhandan");
        nhanDanWorldList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/thegioi", "World");
        stopWatch.stop();
        worldList = ArticlesManager.getSortedArticlesList(vnexpressWorldList, zingWorldList, tuoiTreWorldList, thanhNienWorldList, nhanDanWorldList);
        System.out.println(stopWatch.prettyPrint());
        return worldList;
    }
}
