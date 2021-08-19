package sample;

import java.io.IOException;
import java.util.ArrayList;

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
        vnexpressNewsList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/tin-moi-nhat.rss", "News");
        zingNewsList = ArticlesManager.getZingWebList("https://zingnews.vn/", "News");
        tuoiTreNewsList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/tin-moi-nhat.rss", "News");
        thanhNienNewsList = ArticlesManager.getThanhNienList("https://thanhnien.vn/rss/home.rss", "News");
        nhanDanNewsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/", "News");
        return newsList = ArticlesManager.sortArticle(vnexpressNewsList, zingNewsList, tuoiTreNewsList, thanhNienNewsList, nhanDanNewsList);
    }

    public static ArrayList<Article> getCovidList() throws IOException {
        vnexpressCovidList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/chu-de/covid-19-tp-hcm-3273", "Covid");
        zingCovidList = ArticlesManager.getZingSearchList("covid", "Covid");
        tuoiTreCovidList = ArticlesManager.getTuoiTreSearchList("covid", "Covid");
        thanhNienCovidList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/dich-covid-19/", "Politics");
        nhanDanCovidList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/tag/Covid19-53", "Covid");
        return covidList = ArticlesManager.sortArticle(vnexpressCovidList, zingCovidList, tuoiTreCovidList, thanhNienCovidList, nhanDanCovidList);
    }

    public static ArrayList<Article> getPoliticsList() throws IOException {
        vnexpressPoliticsList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/thoi-su/chinh-tri", "Politics");
        zingPoliticsList = ArticlesManager.getZingWebList("https://zingnews.vn/chinh-tri.html", "Politics");
        tuoiTrePoliticsList = ArticlesManager.getTuoiTreSearchList("chính trị", "Politics");
        thanhNienPoliticsList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/thoi-su/chinh-tri/", "Politics");
        nhanDanPoliticsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/chinhtri", "Politics");
        return politicsList = ArticlesManager.sortArticle(vnexpressPoliticsList, zingPoliticsList, tuoiTrePoliticsList, thanhNienPoliticsList, nhanDanPoliticsList);
    }

    public static ArrayList<Article> getBusinessList() throws IOException {
        vnexpressBusinessList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/kinh-doanh.rss", "Business");
        zingBusinessList = ArticlesManager.getZingWebList("https://zingnews.vn/kinh-doanh-tai-chinh.html", "Business");
        tuoiTreBusinessList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/kinh-doanh.rss", "Business");
        thanhNienBusinessList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/tai-chinh-kinh-doanh/", "Business");
        nhanDanBusinessList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/kinhte", "Business");
        return businessList = ArticlesManager.sortArticle(vnexpressBusinessList, zingBusinessList, tuoiTreBusinessList, thanhNienBusinessList, nhanDanBusinessList);
    }

    public static ArrayList<Article> getTechnologyList() throws IOException {
        vnexpressTechnologyList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/so-hoa.rss", "Technology");
        zingTechnologyList = ArticlesManager.getZingWebList("https://zingnews.vn/cong-nghe.html", "Technology");
        tuoiTreTechnologyList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/nhip-song-so.rss", "Technology");
        thanhNienTechnologyList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/cong-nghe/", "Technology");
        nhanDanTechnologyList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/khoahoc-congnghe", "Technology");
        return technologyList = ArticlesManager.sortArticle(vnexpressTechnologyList, zingTechnologyList, tuoiTreTechnologyList, thanhNienTechnologyList, nhanDanTechnologyList);
    }

    public static ArrayList<Article> getHealthList() throws IOException {
        vnexpressHealthList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/suc-khoe.rss", "Health");
        zingHealthList = ArticlesManager.getZingWebList("https://zingnews.vn/suc-khoe.html", "Health");
        tuoiTreHealthList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/suc-khoe.rss", "Health");
        thanhNienHealthList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/suc-khoe/", "Health");
        nhanDanHealthList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/y-te", "Health");
        return healthList = ArticlesManager.sortArticle(vnexpressHealthList, zingHealthList, tuoiTreHealthList, thanhNienHealthList, nhanDanHealthList);
    }

    public static ArrayList<Article> getSportsList() throws IOException {
        vnexpressSportsList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/the-thao.rss", "Sports");
        zingSportsList = ArticlesManager.getZingWebList("https://zingnews.vn/the-thao.html", "Sports");
        tuoiTreSportsList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-thao.rss", "Sports");
        thanhNienSportsList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/the-thao/", "Sports");
        nhanDanSportsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/thethao", "Sports");
        return sportsList = ArticlesManager.sortArticle(vnexpressSportsList, zingSportsList, tuoiTreSportsList, thanhNienSportsList, nhanDanSportsList);
    }

    public static ArrayList<Article> getEntertainmentList() throws IOException {
        vnexpressEntertainmentList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/cuoi.rss", "Entertainment");
        zingEntertainmentList = ArticlesManager.getZingWebList("https://zingnews.vn/giai-tri.html", "Entertainment");
        tuoiTreEntertainmentList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/giai-tri.rss", "Entertainment");
        thanhNienEntertainmentList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/giai-tri/", "Entertainment");
        nhanDanEntertainmentList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/vanhoa", "Entertainment");
        return entertainmentList = ArticlesManager.sortArticle(vnexpressEntertainmentList, zingEntertainmentList, tuoiTreEntertainmentList, thanhNienEntertainmentList, nhanDanEntertainmentList);
    }

    public static ArrayList<Article> getWorldList() throws IOException {
        vnexpressWorldList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/the-gioi.rss", "World");
        zingWorldList = ArticlesManager.getZingWebList("https://zingnews.vn/the-gioi.html", "World");
        tuoiTreWorldList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-gioi.rss", "World");
        thanhNienWorldList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/the-gioi/", "World");
        nhanDanWorldList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/thegioi", "World");
        return worldList = ArticlesManager.sortArticle(vnexpressWorldList, zingWorldList, tuoiTreWorldList, thanhNienWorldList, nhanDanWorldList);
    }
}
