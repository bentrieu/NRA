package article;

import controller.HomeSceneController;
import controller.WelcomeSceneController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** This class will have some static functions that combine all the lists get from ArticlesManager **/

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
        HomeSceneController.newsProgressBar.setProgress(0.0);
        HomeSceneController.progressBar.setVisible(false);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressNewsList.clear();
            try {
                vnexpressNewsList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/tin-moi-nhat.rss", "News");
                getCovidList(); getPoliticsList();
                WelcomeSceneController.progressBar.setProgress(WelcomeSceneController.progressBar.getProgress() + 0.2);
                HomeSceneController.newsProgressBar.setProgress(HomeSceneController.newsProgressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingNewsList.clear();
            try {
                zingNewsList = ArticlesManager.getZingWebList("https://zingnews.vn/", "News");
                getBusinessList(); getTechnologyList();
                WelcomeSceneController.progressBar.setProgress(WelcomeSceneController.progressBar.getProgress() + 0.2);
                HomeSceneController.newsProgressBar.setProgress(HomeSceneController.newsProgressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreNewsList.clear();
            try {
                tuoiTreNewsList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/tin-moi-nhat.rss", "News");
                getHealthList(); getSportsList();
                WelcomeSceneController.progressBar.setProgress(WelcomeSceneController.progressBar.getProgress() + 0.2);
                HomeSceneController.newsProgressBar.setProgress(HomeSceneController.newsProgressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienNewsList.clear();
            try {
                thanhNienNewsList = ArticlesManager.getThanhNienList("https://thanhnien.vn/rss/home.rss", "News");
                getEntertainmentList(); getWorldList();
                WelcomeSceneController.progressBar.setProgress(WelcomeSceneController.progressBar.getProgress() + 0.2);
                HomeSceneController.newsProgressBar.setProgress(HomeSceneController.newsProgressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanNewsList.clear();
            try {
                nhanDanNewsList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/", "News");
                getOthersList();
                WelcomeSceneController.progressBar.setProgress(WelcomeSceneController.progressBar.getProgress() + 0.2);
                HomeSceneController.newsProgressBar.setProgress(HomeSceneController.newsProgressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        newsList.clear();
//        newsList.addAll(getSortedArticlesList(vnexpressNewsList, zingNewsList, tuoiTreNewsList, thanhNienNewsList, nhanDanNewsList));
        newsList.addAll(vnexpressNewsList);
        newsList.addAll(zingNewsList);
        newsList.addAll(tuoiTreNewsList);
        newsList.addAll(thanhNienNewsList);
        newsList.addAll(nhanDanNewsList);
        newsList.addAll(covidList);
        newsList.addAll(politicsList);
        newsList.addAll(businessList);
        newsList.addAll(technologyList);
        newsList.addAll(healthList);
        newsList.addAll(sportsList);
        newsList.addAll(worldList);
        newsList.addAll(entertainmentList);
        newsList.addAll(othersList);
        sortList(newsList);
        removeDuplicateArticle(newsList);

        HomeSceneController.progressBar.setVisible(true);
    }

    public static void getCovidList() throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressCovidList.clear();
            try {
                vnexpressCovidList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/covid-19/tin-tuc", "Covid");
                vnexpressCovidList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/covid-19/tin-tuc-p2", "Covid"));
                vnexpressCovidList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/covid-19/tin-tuc-p3", "Covid"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingCovidList.clear();
            try {
                zingCovidList = ArticlesManager.getZingSearchList("covid", "Covid");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreCovidList.clear();
            try {
                tuoiTreCovidList = ArticlesManager.getTuoiTreSearchList("covid", "Covid");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienCovidList.clear();
            try {
                thanhNienCovidList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/dich-covid-19/", "Politics");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanCovidList.clear();
            try {
//                nhanDanCovidList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/tag/Covid19-53", "Covid");
                nhanDanCovidList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/Article/PagingByTag?tagId=53&pageSize=50&pageIndex=1&fromDate=&toDate=&displayView=PagingPartial", "Covid");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        covidList.clear();
        covidList.addAll(getSortedArticlesList(vnexpressCovidList, zingCovidList, tuoiTreCovidList, thanhNienCovidList, nhanDanCovidList));
    }

    public static void getPoliticsList() throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressPoliticsList.clear();
            try {
                vnexpressPoliticsList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/thoi-su/chinh-tri", "Politics");
                vnexpressPoliticsList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/thoi-su/chinh-tri-p2", "Politics"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingPoliticsList.clear();
            try {
                zingPoliticsList = ArticlesManager.getZingWebList("https://zingnews.vn/chinh-tri.html/?page=1", "Politics");
                zingPoliticsList.addAll(ArticlesManager.getZingWebList("https://zingnews.vn/chinh-tri.html/?page=2", "Politics"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTrePoliticsList.clear();
            try {
                tuoiTrePoliticsList = ArticlesManager.getTuoiTreSearchList("chính trị", "Politics");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienPoliticsList.clear();
            try {
                thanhNienPoliticsList = ArticlesManager.getThanhNienList("https://thanhnien.vn/rss/thoi-su/chinh-tri.rss", "Politics");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        politicsList.clear();
        politicsList.addAll(getSortedArticlesList(vnexpressPoliticsList, zingPoliticsList, tuoiTrePoliticsList, thanhNienPoliticsList, nhanDanPoliticsList));
    }

    public static void getBusinessList() throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressBusinessList.clear();
            try {
                vnexpressBusinessList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/kinh-doanh", "Business");
                vnexpressBusinessList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/kinh-doanh-p2", "Business"));
                vnexpressBusinessList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/kinh-doanh-p3", "Business"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreBusinessList.clear();
            try {
                tuoiTreBusinessList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/kinh-doanh.rss", "Business");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienBusinessList.clear();
            try {
                thanhNienBusinessList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/tai-chinh-kinh-doanh/", "Business");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        businessList.clear();
        businessList.addAll(getSortedArticlesList(vnexpressBusinessList, zingBusinessList, tuoiTreBusinessList, thanhNienBusinessList, nhanDanBusinessList));
    }

    public static void getTechnologyList() throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressTechnologyList.clear();
            try {
//                vnexpressTechnologyList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/so-hoa.rss", "Technology");
                vnexpressTechnologyList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/so-hoa/cong-nghe", "Technology");
                vnexpressTechnologyList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/so-hoa/cong-nghe-p2", "Technology"));
                vnexpressTechnologyList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/so-hoa/cong-nghe-p3", "Technology"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreTechnologyList.clear();
            try {
//                tuoiTreTechnologyList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/nhip-song-so.rss", "Technology");
                tuoiTreTechnologyList.addAll(ArticlesManager.getTuoiTreWebList("https://congnghe.tuoitre.vn/", "Technology"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienTechnologyList.clear();
            try {
                thanhNienTechnologyList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/cong-nghe/", "Technology");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        technologyList.clear();
        technologyList.addAll(getSortedArticlesList(vnexpressTechnologyList, zingTechnologyList, tuoiTreTechnologyList, thanhNienTechnologyList, nhanDanTechnologyList));
    }

    public static void getHealthList() throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressHealthList.clear();
            try {
                vnexpressHealthList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/suc-khoe", "Health");
                vnexpressHealthList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/suc-khoe-p2", "Health"));
                vnexpressHealthList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/suc-khoe-p3", "Health"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreHealthList.clear();
            try {
                tuoiTreHealthList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/suc-khoe.rss", "Health");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienHealthList.clear();
            try {
                thanhNienHealthList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/suc-khoe/", "Health");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        healthList.clear();
        healthList.addAll(getSortedArticlesList(vnexpressHealthList, zingHealthList, tuoiTreHealthList, thanhNienHealthList, nhanDanHealthList));
    }

    public static void getSportsList() throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressSportsList.clear();
            try {
                vnexpressSportsList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/the-thao.rss", "Sports");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreSportsList.clear();
            try {
                tuoiTreSportsList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-thao.rss", "Sports");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienSportsList.clear();
            try {
                thanhNienSportsList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/the-thao/", "Sports");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        sportsList.clear();
        sportsList.addAll(getSortedArticlesList(vnexpressSportsList, zingSportsList, tuoiTreSportsList, thanhNienSportsList, nhanDanSportsList));
    }

    public static void getEntertainmentList() throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressEntertainmentList.clear();
            try {
                vnexpressEntertainmentList = ArticlesManager.getVnexpressWebList("https://vnexpress.net/giai-tri", "Entertainment");
                vnexpressEntertainmentList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/giai-tri-p2", "Entertainment"));
                vnexpressEntertainmentList.addAll(ArticlesManager.getVnexpressWebList("https://vnexpress.net/giai-tri-p3", "Entertainment"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreEntertainmentList.clear();
            try {
                tuoiTreEntertainmentList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/giai-tri.rss", "Entertainment");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienEntertainmentList.clear();
            try {
                thanhNienEntertainmentList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/giai-tri/", "Entertainment");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        entertainmentList.clear();
        entertainmentList.addAll(getSortedArticlesList(vnexpressEntertainmentList, zingEntertainmentList, tuoiTreEntertainmentList, thanhNienEntertainmentList, nhanDanEntertainmentList));
    }

    public static void getWorldList() throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressWorldList.clear();
            try {
                vnexpressWorldList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/the-gioi.rss", "World");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreWorldList.clear();
            try {
                tuoiTreWorldList = ArticlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-gioi.rss", "World");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            thanhNienWorldList.clear();
            try {
                thanhNienWorldList = ArticlesManager.getThanhNienWebList("https://thanhnien.vn/the-gioi/", "World");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        worldList.clear();
        worldList.addAll(getSortedArticlesList(vnexpressWorldList, zingWorldList, tuoiTreWorldList, thanhNienWorldList, nhanDanWorldList));
    }

    public static void getOthersList() throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressOthersList.clear();
            try {
                vnexpressOthersList = ArticlesManager.getVnexpressList("https://vnexpress.net/rss/phap-luat.rss", "Others");
                vnexpressOthersList.addAll(ArticlesManager.getVnexpressList("https://vnexpress.net/rss/tam-su.rss", "Others"));
//                vnexpressOthersList.addAll(ArticlesManager.getVnexpressList("https://vnexpress.net/rss/oto-xe-may.rss", "Others"));
                vnexpressOthersList.addAll(ArticlesManager.getVnexpressList("https://vnexpress.net/rss/du-lich.rss", "Others"));
//                vnexpressOthersList.addAll(ArticlesManager.getVnexpressList("https://vnexpress.net/rss/gia-dinh.rss", "Others"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
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
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanOthersList.clear();
            try {
                nhanDanOthersList = ArticlesManager.getNhanDanWebList("https://nhandan.vn/du-lich", "Others");
                nhanDanOthersList.addAll(ArticlesManager.getNhanDanWebList("https://nhandan.vn/bandoc", "Others"));
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        othersList.clear();
        othersList.addAll(getSortedArticlesList(vnexpressOthersList, zingOthersList, tuoiTreOthersList, thanhNienOthersList, nhanDanOthersList));
    }

    public static void getSearchList(String keyword) throws IOException {
        HomeSceneController.progressBar.setProgress(0.0);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(() -> {
            vnexpressSearchList.clear();
            try {
                vnexpressSearchList = ArticlesManager.getVnexpressSearchList(keyword, "Search");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.25);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            zingSearchList.clear();
            try {
                zingSearchList = ArticlesManager.getZingSearchList(keyword, "Search");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.25);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            tuoiTreSearchList.clear();
            try {
                tuoiTreSearchList = ArticlesManager.getTuoiTreSearchList(keyword, "Search");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.25);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        es.execute(() -> {
            nhanDanSearchList.clear();
            try {
                nhanDanSearchList = ArticlesManager.getNhanDanSearchList(keyword, "Search");
                HomeSceneController.progressBar.setProgress(HomeSceneController.progressBar.getProgress() + 0.25);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();

        while (!es.isTerminated()) {
        }

        try {
            searchList.clear();
            searchList.addAll(getSortedArticlesList(vnexpressSearchList, zingSearchList, tuoiTreSearchList, thanhNienSearchList, nhanDanSearchList));
        } catch (Exception e) {
            System.out.println("There is nothing to show try-catch (search list: zero element)");
        }
    }

    public static void sortList(ArrayList<Article> list) {
        list.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
    }

    public static void removeDuplicateArticle(ArrayList<Article> list) {
        HashSet<String> hashSet = new HashSet<>();
        ArrayList<Article> toRemove = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (hashSet.contains(list.get(i).getLinkToFullArticles())) {
                toRemove.add(list.get(i));
            } else {
                hashSet.add(list.get(i).getLinkToFullArticles());
            }
        }
        list.removeAll(toRemove);
    }

    public static ArrayList<Article> getSortedArticlesList(ArrayList<Article> list1, ArrayList<Article> list2, ArrayList<Article> list3, ArrayList<Article> list4, ArrayList<Article> list5) {
        ArrayList<Article> sortedArticles = new ArrayList<>();

        sortedArticles.addAll(list1);
        sortedArticles.addAll(list2);
        sortedArticles.addAll(list3);
        sortedArticles.addAll(list4);
        sortedArticles.addAll(list5);

        sortedArticles.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));

        return sortedArticles;
    }
}
