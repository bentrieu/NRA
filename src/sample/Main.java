package sample;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    List<String> newsText;
    public int textNum(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements info = doc.getElementsByClass("fck_detail");

        for(Element newText : info) {
            newsText = newText.getElementsByClass("Normal").eachText();
        }
        return newsText.size();
    }

    String[] imageLink = new String[2];
    public void imageNum(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements pic = doc.select("[src]");
        int i = 0;
        for (Element imageUrl : pic){
            if(!imageUrl.attr("abs:data-src").isEmpty()) {
                imageLink[i] = imageUrl.attr("abs:data-src");
                i++;
            }
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception{

        final String url = "https://vnexpress.net/them-hon-1-2-trieu-lieu-vaccine-astrazeneca-ve-viet-nam-4329275.html";
        Label label[];
        Image image[];
        ImageView imageView[];

        imageNum(url);

        image = new Image[imageLink.length];
        imageView = new ImageView[imageLink.length];
        label = new Label[textNum(url)];

        FlowPane pane = new FlowPane();
        for(int i = 0; i < textNum(url);i++){
            label[i] = new Label(newsText.get(i));
            label[i].setFont(new Font("Arial",15));
            label[i].setWrapText(true);
            label[i].setMaxSize(750,500);
            pane.getChildren().add(label[i]);
        }

        for(int i = 0;i < imageLink.length;i++){
            image[i] = new Image(imageLink[i]);
            imageView[i] = new ImageView(image[i]);

            pane.getChildren().add(imageView[i]);
        }

        pane.setVgap(10);
        pane.setOrientation(Orientation.VERTICAL);


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(pane, 1920, 1080));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
