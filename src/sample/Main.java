package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    List<String> newsText = new ArrayList<String>();
    public void extractNews(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements info = doc.getElementsByClass("fck_detail").select("p , img");

        for(Element newText : info) {
            if(newText.hasText()) {
                newsText.add(newText.text());
            } else {
                newsText.add(newText.getElementsByTag("img").attr("abs:data-src"));
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        VBox vbox = new VBox(20);
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setBottomAnchor(vbox,50.0);
        AnchorPane.setTopAnchor(vbox,50.0);
        AnchorPane.setRightAnchor(vbox,480.0);
        AnchorPane.setLeftAnchor(vbox,480.0);
        anchorPane.getChildren().add(vbox);
        ScrollPane scrollPane = new ScrollPane(anchorPane);

        //Url to connect
        final String url = "https://vnexpress.net/them-hon-1-2-trieu-lieu-vaccine-astrazeneca-ve-viet-nam-4329275.html";

        //Basic list
        List<Label> label = new ArrayList<>();
        List<Image> image = new ArrayList<>();
        List<ImageView> imageView = new ArrayList<>();

        //Run the method
        extractNews(url);

        //Regex
        Pattern http = Pattern.compile("^https");
        Matcher matcher;

        //Adding elements to the flowpane
        int textCount = 0, imgCount = 0;
        for (int i = 0; i < newsText.size(); i++) {
            matcher = http.matcher(newsText.get(i));
            System.out.println(newsText.get(i));
            if (matcher.find()) {
                image.add(new Image(newsText.get(i)));
                imageView.add(new ImageView(image.get(imgCount)));

                vbox.getChildren().add(imageView.get(imgCount));
                imgCount++;
            } else {
                label.add(new Label(newsText.get(i)));
                label.get(textCount).setFont(new Font("Arial", 15));
                label.get(textCount).setWrapText(true);
                label.get(textCount).setMaxSize(750, 1000);

                vbox.getChildren().add(label.get(textCount));
                textCount++;
            }
        }


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(scrollPane,500,500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
