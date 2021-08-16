package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.util.ArrayList;

public class Main extends Application {
    public static Stage stage;

    public static ArticlesManager articlesManager = new ArticlesManager();

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

    double initialX, initialY;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage stage = new Stage();
        Main.stage = stage;
        // Vnexpress list (no description in sort list)
        vnexpressNewsList = articlesManager.getVnexpressList("https://vnexpress.net/rss/tin-moi-nhat.rss", "News");
        vnexpressCovidList = articlesManager.getVnexpressWebList("https://vnexpress.net/chu-de/covid-19-tp-hcm-3273", "Covid");
        vnexpressPoliticsList = articlesManager.getVnexpressWebList("https://vnexpress.net/thoi-su/chinh-tri", "Politics");
        vnexpressBusinessList = articlesManager.getVnexpressList("https://vnexpress.net/rss/kinh-doanh.rss", "Business");
        vnexpressTechnologyList = articlesManager.getVnexpressList("https://vnexpress.net/rss/so-hoa.rss", "Technology");
        vnexpressHealthList = articlesManager.getVnexpressList("https://vnexpress.net/rss/suc-khoe.rss", "Health");
        vnexpressSportsList = articlesManager.getVnexpressList("https://vnexpress.net/rss/the-thao.rss", "Sports");
        vnexpressEntertainmentList = articlesManager.getVnexpressList("https://vnexpress.net/rss/cuoi.rss", "Entertainment");
        vnexpressWorldList = articlesManager.getVnexpressList("https://vnexpress.net/rss/the-gioi.rss", "World");

        // ZingNews list
        zingNewsList = articlesManager.getZingList("https://zingnews.vn/thoi-su.html", "News");
        zingCovidList = articlesManager.getSearchListZing("covid", "Covid");
        zingPoliticsList = articlesManager.getZingList("https://zingnews.vn/chinh-tri.html", "Politics");
        zingBusinessList = articlesManager.getZingList("https://zingnews.vn/kinh-doanh-tai-chinh.html", "Business");
        zingTechnologyList = articlesManager.getZingList("https://zingnews.vn/cong-nghe.html", "Technology");
        zingHealthList = articlesManager.getZingList("https://zingnews.vn/suc-khoe.html", "Health");
        zingSportsList = articlesManager.getZingList("https://zingnews.vn/the-thao.html", "Sports");
        zingEntertainmentList = articlesManager.getZingList("https://zingnews.vn/giai-tri.html", "Entertainment");
        zingWorldList = articlesManager.getZingList("https://zingnews.vn/the-gioi.html", "World");

        // TuoiTre list
//        tuoiTreNewsList = articlesManager.getTuoiTreList("https://tuoitre.vn/rss/tin-moi-nhat.rss", "News");
//        tuoiTreCovidList = articlesManager.getSearchListTuoiTre("covid", "Covid");
//        tuoiTrePoliticsList = articlesManager.getSearchListTuoiTre("chính trị", "Politics");
//        tuoiTreBusinessList = articlesManager.getTuoiTreList("https://tuoitre.vn/rss/kinh-doanh.rss", "Business");
//        tuoiTreTechnologyList = articlesManager.getTuoiTreList("https://tuoitre.vn/rss/nhip-song-so.rss", "Technology");
//        tuoiTreHealthList = articlesManager.getTuoiTreList("https://tuoitre.vn/rss/suc-khoe.rss", "Health");
//        tuoiTreSportsList = articlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-thao.rss", "Sports");
//        tuoiTreEntertainmentList = articlesManager.getTuoiTreList("https://tuoitre.vn/rss/giai-tri.rss", "Entertainment");
//        tuoiTreWorldList = articlesManager.getTuoiTreList("https://tuoitre.vn/rss/the-gioi.rss", "World");
//        articlesManager.printSortArticles(tuoiTreBusinessList);

        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        DraggableHelper.addDraggableNode(root);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
