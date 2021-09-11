package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.util.StopWatch;

public class ArticlesList {
    public static ArrayList<Article> vnexpressNewsList = new ArrayList();
    public static ArrayList<Article> vnexpressCovidList = new ArrayList();
    public static ArrayList<Article> vnexpressPoliticsList = new ArrayList();
    public static ArrayList<Article> vnexpressBusinessList = new ArrayList();
    public static ArrayList<Article> vnexpressTechnologyList = new ArrayList();
    public static ArrayList<Article> vnexpressHealthList = new ArrayList();
    public static ArrayList<Article> vnexpressSportsList = new ArrayList();
    public static ArrayList<Article> vnexpressEntertainmentList = new ArrayList();
    public static ArrayList<Article> vnexpressWorldList = new ArrayList();
    public static ArrayList<Article> vnexpressOthersList = new ArrayList();
    public static ArrayList<Article> vnexpressSearchList = new ArrayList();

    public static ArrayList<Article> zingNewsList = new ArrayList();
    public static ArrayList<Article> zingCovidList = new ArrayList();
    public static ArrayList<Article> zingPoliticsList = new ArrayList();
    public static ArrayList<Article> zingBusinessList = new ArrayList();
    public static ArrayList<Article> zingTechnologyList = new ArrayList();
    public static ArrayList<Article> zingHealthList = new ArrayList();
    public static ArrayList<Article> zingSportsList = new ArrayList();
    public static ArrayList<Article> zingEntertainmentList = new ArrayList();
    public static ArrayList<Article> zingWorldList = new ArrayList();
    public static ArrayList<Article> zingOthersList = new ArrayList();
    public static ArrayList<Article> zingSearchList = new ArrayList();

    public static ArrayList<Article> tuoiTreNewsList = new ArrayList();
    public static ArrayList<Article> tuoiTreCovidList = new ArrayList();
    public static ArrayList<Article> tuoiTrePoliticsList = new ArrayList();
    public static ArrayList<Article> tuoiTreBusinessList = new ArrayList();
    public static ArrayList<Article> tuoiTreTechnologyList = new ArrayList();
    public static ArrayList<Article> tuoiTreHealthList = new ArrayList();
    public static ArrayList<Article> tuoiTreSportsList = new ArrayList();
    public static ArrayList<Article> tuoiTreEntertainmentList = new ArrayList();
    public static ArrayList<Article> tuoiTreWorldList = new ArrayList();
    public static ArrayList<Article> tuoiTreOthersList = new ArrayList();
    public static ArrayList<Article> tuoiTreSearchList = new ArrayList();

    public static ArrayList<Article> thanhNienNewsList = new ArrayList();
    public static ArrayList<Article> thanhNienCovidList = new ArrayList();
    public static ArrayList<Article> thanhNienPoliticsList = new ArrayList();
    public static ArrayList<Article> thanhNienBusinessList = new ArrayList();
    public static ArrayList<Article> thanhNienTechnologyList = new ArrayList();
    public static ArrayList<Article> thanhNienHealthList = new ArrayList();
    public static ArrayList<Article> thanhNienSportsList = new ArrayList();
    public static ArrayList<Article> thanhNienEntertainmentList = new ArrayList();
    public static ArrayList<Article> thanhNienWorldList = new ArrayList();
    public static ArrayList<Article> thanhNienOthersList = new ArrayList();
    public static ArrayList<Article> thanhNienSearchList = new ArrayList();

    public static ArrayList<Article> nhanDanNewsList = new ArrayList();
    public static ArrayList<Article> nhanDanCovidList = new ArrayList();
    public static ArrayList<Article> nhanDanPoliticsList = new ArrayList();
    public static ArrayList<Article> nhanDanBusinessList = new ArrayList();
    public static ArrayList<Article> nhanDanTechnologyList = new ArrayList();
    public static ArrayList<Article> nhanDanHealthList = new ArrayList();
    public static ArrayList<Article> nhanDanSportsList = new ArrayList();
    public static ArrayList<Article> nhanDanEntertainmentList = new ArrayList();
    public static ArrayList<Article> nhanDanWorldList = new ArrayList();
    public static ArrayList<Article> nhanDanOthersList = new ArrayList();
    public static ArrayList<Article> nhanDanSearchList = new ArrayList();

    public static ArrayList<Article> newsList = new ArrayList();
    public static ArrayList<Article> covidList = new ArrayList();
    public static ArrayList<Article> politicsList = new ArrayList();
    public static ArrayList<Article> businessList = new ArrayList();
    public static ArrayList<Article> technologyList = new ArrayList();
    public static ArrayList<Article> healthList = new ArrayList();
    public static ArrayList<Article> sportsList = new ArrayList();
    public static ArrayList<Article> entertainmentList = new ArrayList();
    public static ArrayList<Article> worldList = new ArrayList();
    public static ArrayList<Article> othersList = new ArrayList();
    public static ArrayList<Article> searchList = new ArrayList();

    public ArticlesList() {
    }

    public static void getNewsList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressNewsList.clear();
            try {
                vnexpressNewsList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/tin-moi-nhat.rss", "News");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingNewsList.clear();
            try {
                zingNewsList = ArticlesManager.getZingWebList("https://zingnews.vn/", "News");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreNewsList.clear();
            try {
                tuoiTreNewsList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/tin-moi-nhat.rss", "News");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienNewsList.clear();
            try {
                thanhNienNewsList = ArticlesManager.getThanhNienList("https://thanhnien.vn/rss/home.rss", "News");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanNewsList.clear();
            try {
                nhanDanNewsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/", "News");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        newsList.clear();
        newsList.addAll(ArticlesManager.getSortedArticlesList(vnexpressNewsList, zingNewsList, tuoiTreNewsList, thanhNienNewsList, nhanDanNewsList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getCovidList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressCovidList.clear();
            try {
                vnexpressCovidList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/covid-19/tin-tuc", "Covid");
                vnexpressCovidList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/covid-19/tin-tuc-p2", "Covid"));
                vnexpressCovidList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/covid-19/tin-tuc-p3", "Covid"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingCovidList.clear();
            try {
                zingCovidList = ArticlesManager.getZingSearchList("covid", "Covid");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreCovidList.clear();
            try {
                tuoiTreCovidList = ArticlesManager.getTuoiTreSearchList("covid", "Covid");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienCovidList.clear();
            try {
                thanhNienCovidList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/dich-covid-19/", "Politics");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanCovidList.clear();
            try {
//                nhanDanCovidList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/tag/Covid19-53", "Covid");
                nhanDanCovidList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/Article/PagingByTag?tagId=53&pageSize=50&pageIndex=1&fromDate=&toDate=&displayView=PagingPartial", "Covid");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        covidList.clear();
        covidList.addAll(ArticlesManager.getSortedArticlesList(vnexpressCovidList, zingCovidList, tuoiTreCovidList, thanhNienCovidList, nhanDanCovidList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getPoliticsList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressPoliticsList.clear();
            try {
                vnexpressPoliticsList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/thoi-su/chinh-tri", "Politics");
                vnexpressPoliticsList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/thoi-su/chinh-tri-p2", "Politics"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingPoliticsList.clear();
            try {
                zingPoliticsList = ArticlesManager.getZingWebList("https://zingnews.vn/chinh-tri.html/?page=1", "Politics");
                zingPoliticsList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/chinh-tri.html/?page=2", "Politics"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTrePoliticsList.clear();
            try {
                tuoiTrePoliticsList = ArticlesManager.getTuoiTreSearchList("chính trị", "Politics");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienPoliticsList.clear();
            try {
                thanhNienPoliticsList = ArticlesManager.getThanhNienList("https://thanhnien.vn/rss/thoi-su/chinh-tri.rss", "Politics");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanPoliticsList.clear();
            try {
//                nhanDanPoliticsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/chinhtri", "Politics");
                nhanDanPoliticsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1171&pageIndex=1&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Politics");
                nhanDanPoliticsList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1171&pageIndex=2&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Politics"));
                nhanDanPoliticsList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1171&pageIndex=3&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Politics"));
                nhanDanPoliticsList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1171&pageIndex=4&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Politics"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        politicsList.clear();
        politicsList.addAll(ArticlesManager.getSortedArticlesList(vnexpressPoliticsList, zingPoliticsList, tuoiTrePoliticsList, thanhNienPoliticsList, nhanDanPoliticsList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getBusinessList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressBusinessList.clear();
            try {
                vnexpressBusinessList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/kinh-doanh", "Business");
                vnexpressBusinessList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/kinh-doanh-p2", "Business"));
                vnexpressBusinessList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/kinh-doanh-p3", "Business"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingBusinessList.clear();
            try {
                zingBusinessList = ArticlesManager.getZingWebList("https://zingnews.vn/kinh-doanh-tai-chinh.html", "Business");
                zingBusinessList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/kinh-doanh-tai-chinh.html/?page=2", "Business"));
                zingBusinessList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/kinh-doanh-tai-chinh.html/?page=3", "Business"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreBusinessList.clear();
            try {
                tuoiTreBusinessList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/kinh-doanh.rss", "Business");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienBusinessList.clear();
            try {
                thanhNienBusinessList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/tai-chinh-kinh-doanh/", "Business");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanBusinessList.clear();
            try {
//                nhanDanBusinessList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/kinhte", "Business");
                nhanDanBusinessList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1185&pageIndex=1&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Business");
                nhanDanBusinessList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1185&pageIndex=2&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Business"));
                nhanDanBusinessList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1185&pageIndex=3&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Business"));
                nhanDanBusinessList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1185&pageIndex=4&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Business"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        businessList.clear();
        businessList.addAll(ArticlesManager.getSortedArticlesList(vnexpressBusinessList, zingBusinessList, tuoiTreBusinessList, thanhNienBusinessList, nhanDanBusinessList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getTechnologyList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressTechnologyList.clear();
            try {
//                vnexpressTechnologyList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/so-hoa.rss", "Technology");
                vnexpressTechnologyList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/so-hoa/cong-nghe", "Technology");
                vnexpressTechnologyList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/so-hoa/cong-nghe-p2", "Technology"));
                vnexpressTechnologyList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/so-hoa/cong-nghe-p3", "Technology"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingTechnologyList.clear();
            try {
//                zingTechnologyList = ArticlesManager.getZingWebList("https://zingnews.vn/cong-nghe.html", "Technology");
                zingTechnologyList = ArticlesManager.getZingWebList("https://zingnews.vn/cong-nghe.html/?page=1", "Technology");
                zingTechnologyList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/cong-nghe.html/?page=2", "Technology"));
                zingTechnologyList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/cong-nghe.html/?page=3", "Technology"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreTechnologyList.clear();
            try {
//                tuoiTreTechnologyList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/nhip-song-so.rss", "Technology");
                tuoiTreTechnologyList.addAll(ArticlesManager.getTuoiTreWebList("https://congnghe.tuoitre.vn/", "Technology"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienTechnologyList.clear();
            try {
                thanhNienTechnologyList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/cong-nghe/", "Technology");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanTechnologyList.clear();
            try {
//                nhanDanTechnologyList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/khoahoc-congnghe", "Technology");
                nhanDanTechnologyList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1292&pageIndex=1&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Technology");
                nhanDanTechnologyList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1292&pageIndex=2&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Technology"));
                nhanDanTechnologyList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1292&pageIndex=3&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Technology"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        technologyList.clear();
        technologyList.addAll(ArticlesManager.getSortedArticlesList(vnexpressTechnologyList, zingTechnologyList, tuoiTreTechnologyList, thanhNienTechnologyList, nhanDanTechnologyList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getHealthList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressHealthList.clear();
            try {
                vnexpressHealthList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/suc-khoe", "Health");
                vnexpressHealthList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/suc-khoe-p2", "Health"));
                vnexpressHealthList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/suc-khoe-p3", "Health"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingHealthList.clear();
            try {
                zingHealthList = ArticlesManager.getZingWebList("https://zingnews.vn/suc-khoe.html", "Health");
                zingHealthList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/suc-khoe.html/?page=2", "Health"));
                zingHealthList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/suc-khoe.html/?page=3", "Health"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreHealthList.clear();
            try {
                tuoiTreHealthList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/suc-khoe.rss", "Health");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienHealthList.clear();
            try {
                thanhNienHealthList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/suc-khoe/", "Health");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanHealthList.clear();
            try {
//                nhanDanHealthList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/y-te", "Health");
                nhanDanHealthList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1309&pageIndex=1&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Health");
                nhanDanHealthList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1309&pageIndex=2&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Health"));
                nhanDanHealthList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1309&pageIndex=3&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Health"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        healthList.clear();
        healthList.addAll(ArticlesManager.getSortedArticlesList(vnexpressHealthList, zingHealthList, tuoiTreHealthList, thanhNienHealthList, nhanDanHealthList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getSportsList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressSportsList.clear();
            try {
                vnexpressSportsList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/the-thao.rss", "Sports");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingSportsList.clear();
            try {
                zingSportsList = ArticlesManager.getZingWebList("https://zingnews.vn/the-thao.html", "Sports");
                zingSportsList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/the-thao.html/?page=2", "Sports"));
                zingSportsList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/the-thao.html/?page=3", "Sports"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreSportsList.clear();
            try {
                tuoiTreSportsList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-thao.rss", "Sports");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienSportsList.clear();
            try {
                thanhNienSportsList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/the-thao/", "Sports");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanSportsList.clear();
            try {
//                nhanDanSportsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/thethao", "Sports");
                nhanDanSportsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1224&pageIndex=1&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Sports");
                nhanDanSportsList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1224&pageIndex=2&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Sports"));
                nhanDanSportsList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1224&pageIndex=3&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Sports"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        sportsList.clear();
        sportsList.addAll(ArticlesManager.getSortedArticlesList(vnexpressSportsList, zingSportsList, tuoiTreSportsList, thanhNienSportsList, nhanDanSportsList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getEntertainmentList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressEntertainmentList.clear();
            try {
                vnexpressEntertainmentList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/giai-tri", "Entertainment");
                vnexpressEntertainmentList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/giai-tri-p2", "Entertainment"));
                vnexpressEntertainmentList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/giai-tri-p3", "Entertainment"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingEntertainmentList.clear();
            try {
                zingEntertainmentList = ArticlesManager.getZingWebList("https://zingnews.vn/giai-tri.html", "Entertainment");
                zingEntertainmentList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/giai-tri.html/?page=2", "Entertainment"));
                zingEntertainmentList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/giai-tri.html/?page=3", "Entertainment"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreEntertainmentList.clear();
            try {
                tuoiTreEntertainmentList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/giai-tri.rss", "Entertainment");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienEntertainmentList.clear();
            try {
                thanhNienEntertainmentList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/giai-tri/", "Entertainment");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanEntertainmentList.clear();
            try {
//                nhanDanEntertainmentList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/vanhoa", "Entertainment");
                nhanDanEntertainmentList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1251&pageIndex=1&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Entertainment");
                nhanDanEntertainmentList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1251&pageIndex=2&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Entertainment"));
                nhanDanEntertainmentList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1251&pageIndex=3&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "Entertainment"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        entertainmentList.clear();
        entertainmentList.addAll(ArticlesManager.getSortedArticlesList(vnexpressEntertainmentList, zingEntertainmentList, tuoiTreEntertainmentList, thanhNienEntertainmentList, nhanDanEntertainmentList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getWorldList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressWorldList.clear();
            try {
                vnexpressWorldList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/the-gioi.rss", "World");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingWorldList.clear();
            try {
                zingWorldList = ArticlesManager.getZingWebList("https://zingnews.vn/the-gioi.html", "World");
                zingWorldList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/the-gioi.html/?page=2", "World"));
                zingWorldList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/the-gioi.html/?page=3", "World"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreWorldList.clear();
            try {
                tuoiTreWorldList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-gioi.rss", "World");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienWorldList.clear();
            try {
                thanhNienWorldList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/the-gioi/", "World");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanWorldList.clear();
            try {
//                nhanDanWorldList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/thegioi", "World");
                nhanDanWorldList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1231&pageIndex=1&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "World");
                nhanDanWorldList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1231&pageIndex=2&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "World"));
                nhanDanWorldList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/article/Paging?categoryId=1231&pageIndex=3&pageSize=15&fromDate=&toDate=&displayView=PagingPartial", "World"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        worldList.clear();
        worldList.addAll(ArticlesManager.getSortedArticlesList(vnexpressWorldList, zingWorldList, tuoiTreWorldList, thanhNienWorldList, nhanDanWorldList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getOthersList() throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressOthersList.clear();
            try {
                vnexpressOthersList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/phap-luat.rss", "Others");
                vnexpressOthersList.addAll(ArticlesManager.getVnexpressList("https://vnexpress.net/rss/tam-su.rss", "Others"));
//                vnexpressOthersList.addAll(ArticlesManager.getVnexpressList("https://vnexpress.net/rss/oto-xe-may.rss", "Others"));
                vnexpressOthersList.addAll(ArticlesManager.getVnexpressList("https://vnexpress.net/rss/du-lich.rss", "Others"));
//                vnexpressOthersList.addAll(ArticlesManager.getVnexpressList("https://vnexpress.net/rss/gia-dinh.rss", "Others"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingOthersList.clear();
            try {
                zingOthersList = ArticlesManager.getZingWebList("https://zingnews.vn/du-hoc.html", "Others");
                zingOthersList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/oto.html", "Others"));
                zingOthersList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/xu-huong.html", "Others"));
                zingOthersList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/am-thuc.html", "Others"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreOthersList.clear();
            try {
                tuoiTreOthersList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/du-lich.rss", "Others");
                tuoiTreOthersList.addAll(ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/xe.rss", "Others"));
                tuoiTreOthersList.addAll(ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/nhip-song-tre.rss", "Others"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienOthersList.clear();
            try {
                thanhNienOthersList = ArticlesManager.getThanhNienList("https://thanhnien.vn/game/tin-tuc-game.rss", "Others");
                thanhNienOthersList.addAll(ArticlesManager.getThanhNienList("https://thanhnien.vn/rss/doi-song/am-thuc.rss", "Others"));
                thanhNienOthersList.addAll(ArticlesManager.getThanhNienList("https://thanhnien.vn/rss/du-lich.rss", "Others"));
                thanhNienOthersList.addAll(ArticlesManager.getThanhNienList("https://thanhnien.vn/rss/giao-duc/du-hoc.rss", "Others"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanOthersList.clear();
            try {
                nhanDanOthersList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/du-lich", "Others");
                nhanDanOthersList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/bandoc", "Others"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        othersList.clear();
        othersList.addAll(ArticlesManager.getSortedArticlesList(vnexpressOthersList, zingOthersList, tuoiTreOthersList, thanhNienOthersList, nhanDanOthersList));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    public static void getSearchList(String keyword) throws IOException {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressSearchList.clear();
            try {
                vnexpressSearchList = ArticlesManager.getVnexpressSearchList(keyword, "Search");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingSearchList.clear();
            try {
                zingSearchList = ArticlesManager.getZingSearchList(keyword, "Search");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreSearchList.clear();
            try {
                tuoiTreSearchList = ArticlesManager.getTuoiTreSearchList(keyword, "Search");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanSearchList.clear();
            try {
                nhanDanSearchList = ArticlesManager.getNhanDanSearchList(keyword, "Search");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        StopWatch stopWatch = new StopWatch("total");
        stopWatch.start("Scrape");
        while (!es.isTerminated()) {
        }
        stopWatch.stop();

        stopWatch.start("Sort");
        try {
            searchList.clear();
            searchList.addAll(ArticlesManager.getSortedArticlesList(vnexpressSearchList, zingSearchList, tuoiTreSearchList, thanhNienSearchList, nhanDanSearchList));
        } catch (Exception e) {
            System.out.println("There is nothing to show try-catch (search list: zero element)");
        }

        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}
