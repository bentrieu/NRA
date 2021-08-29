package sample;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/* This class will have static functions that manipulate articles*/

public class ArticlesManager extends Application {
    // This list to save the listeners that create during the process of responsive design. So this listeners need to be removed later to avoid memory leaking
    public static ArrayList<ChangeListener<Number>> changeListenerList = new ArrayList<>();

    /* FROM HERE WILL BE THE FUNCTION FOR ZINGNEWS.VN
       There will be 3 function
       Get RSS list
       Get search keyword list
       Display the full article  */
    // Zing web list: https://zingnews.vn/the-gioi.html
    public static ArrayList<Article> getZingWebList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> zingNewsList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all;
        Elements thumb;
        Elements titleAndLink;
        Elements description;
        Elements dateAndCategory;
        if (!category.equals("News")) {
//        if (category.equals("Sports")) {
            all = document.select("section#news-latest div.article-list article.article-item.type-text, section#news-latest div.article-list article.article-item.type-picture, section#news-latest div.article-list article.article-item.type-hasvideo");
//        }
//        else {
//            all = document.select("section#news-latest div.article-list article.article-item.type-text, section#news-latest div.article-list article.article-item.type-picture");
//        }
            thumb = all.select("p.article-thumbnail img");
            titleAndLink = all.select("p.article-title a[href]");
            description = all.select("p.article-summary");
            dateAndCategory = all.select("p.article-meta");
        } else {
            all = document.select("section.section-featured article, div.article-list article");
            thumb = all.select("p.article-thumbnail img");
            titleAndLink = all.select("p.article-title a[href]");
            description = all.select("p.article-summary");
            dateAndCategory = all.select("p.article-meta");
        }


        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0; i < 15; i++) {
            // Create new article object then add the object into the ArrayList
            zingNewsList.add(new Article());
            // Set source
            zingNewsList.get(i).setSource("zingnews");
            // Set category manually
            zingNewsList.get(i).setCategory(category);
            // Set orginalCategory for each object
            zingNewsList.get(i).setOriginalCategory(dateAndCategory.get(i).select("span.category").text());
            // Set title for each object
            zingNewsList.get(i).setTitle(titleAndLink.get(i).text());
            // Set date for each object
            String date = Helper.timeToUnixString3(dateAndCategory.get(i).select("span.date").text() + " " + dateAndCategory.get(i).select("span.time").text());
            zingNewsList.get(i).setDate(date);
            // Set time ago for each object
            zingNewsList.get(i).setTimeAgo(Helper.timeDiff(date));
            // Set thumb for each object
            if (thumb.get(i).hasAttr("data-src")) zingNewsList.get(i).setThumb(thumb.get(i).attr("abs:data-src"));
            else {
                if (thumb.get(i).hasAttr("src")) zingNewsList.get(i).setThumb(thumb.get(i).attr("abs:src"));
            }
            // Set description (summarise) for each object
            zingNewsList.get(i).setDescription(description.get(i).text());
            // Set link to full article for each object
            zingNewsList.get(i).setLinkToFullArticles(titleAndLink.get(i).attr("abs:href"));
        }

        return zingNewsList;
    }

    // Zing search
    public static ArrayList<Article> getZingSearchList(String keyWord, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> searchZingList = new ArrayList<>();

        // Convert keyWord into link that can scrape // https://zingnews.vn/dịch-covid-tim-kiem.html?content_type=0
        String convertedKeyWord = "https://zingnews.vn/" + keyWord.trim().replaceAll("\\s", "-").toLowerCase() + "-tim-kiem.html";

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = convertedKeyWord;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("div.article-list article.article-item:not(.type-hasvideo)");
        // There are no date and original category when searching with tuoitre

        // Add data to searchZingList
        for (int i = 0; i < 12; i++) {
            // Create new article object then add the object into the ArrayList
            searchZingList.add(new Article());
            // Set source
            searchZingList.get(i).setSource("zingnews");
            // Set category manually
            searchZingList.get(i).setCategory(category);
            // Set title for each object
            searchZingList.get(i).setTitle(all.get(i).select("header p.article-title").text());
            // Set thumb for each object
            searchZingList.get(i).setThumb(all.get(i).select("p.article-thumbnail img").attr("abs:data-src"));
            // Set description for each object
            searchZingList.get(i).setDescription(all.get(i).select("p.article-summary").text());
            // Set link to full article for each object
            searchZingList.get(i).setLinkToFullArticles(all.get(i).select("header p.article-title a").attr("abs:href"));
            // Set date for each object
            String date = Helper.timeToUnixString3(all.get(i).select("p.article-meta span.date").text() + " " + all.get(i).select("p.article-meta span.time").text());
            searchZingList.get(i).setDate(date);
            // Set time ago for each object
            searchZingList.get(i).setTimeAgo(Helper.timeDiff(date));
        }

        return searchZingList;
    }

    // Zing full article scrape and display
    public static void displayZingFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().clear();

        // Setup jsoup for each article
        final String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements all = document.select("article[article-id]");
        Elements fullDate = all.select("ul.the-article-meta li.the-article-publish");
        Elements body = all.select("div.the-article-body").select("> p, p ~ h3, td.pic img[src], td.pCaption.caption, div#innerarticle, figure.video");
        Elements author = document.select("div.the-article-credit p.author");
        Elements originalCategory = all.select("p.the-article-category");

        // Set fullDate for each object
        article.setFullDate(fullDate.text());

        // Set author for each object
        if (author.hasText()) article.setAuthor(author.first().text());

        // Set original category
        if (originalCategory.hasText()) article.setOriginalCategory(originalCategory.text());

        // Display category
        Text text = new Text(article.getCategory());
        text.getStyleClass().add("textcategory");
        TextFlow textFlow = new TextFlow(text);
        vbox.getChildren().add(textFlow);

        // Display image source
        Image imageSource = new Image("resource/zingnews_big.png", 200, 200, true, false, true);
        ImageView imageViewSource = new ImageView();
        imageViewSource.setCache(true);
        imageViewSource.setCacheHint(CacheHint.SPEED);
        imageViewSource.setImage(imageSource);
        imageViewSource.setPreserveRatio(true);
        imageViewSource.setFitHeight(60);
        vbox.getChildren().add(imageViewSource);

        // Display fullDate
        Text text0 = new Text(article.getFullDate());
        text0.getStyleClass().add("textfulldate");
        TextFlow textFlow0 = new TextFlow(text0);
        textFlow0.getStyleClass().add("textflowcenter");
        vbox.getChildren().add(textFlow0);

        // Display title
        Text text1 = new Text(article.getTitle());
        text1.getStyleClass().add("texttitle");
        TextFlow textFlow1 = new TextFlow(text1);
        textFlow1.getStyleClass().add("textflowjustify");
        vbox.getChildren().add(textFlow1);

        // Display description
        Text text2 = new Text(article.getDescription());
        text2.getStyleClass().add("textdescription");
        TextFlow textFlow2 = new TextFlow(text2);
        textFlow2.getStyleClass().add("textflowjustify");
        HBox descriptionHbox = new HBox();
        descriptionHbox.getStyleClass().add("descriptionHbox");
        descriptionHbox.getChildren().add(textFlow2);
        vbox.getChildren().add(descriptionHbox);

        // Display all content
        for (Element index : body) {
            TextFlow textFlow3 = new TextFlow();
            vbox.getChildren().add(textFlow3);
            // Add video open link caption
            if (index.hasClass("video")) {
                Text text51 = new Text("Watch video on this ");
                text51.getStyleClass().add("textReadTheOriginalPost");
                Hyperlink articleLink1 = new Hyperlink("link");
                articleLink1.getStyleClass().add("texthyperlink");
                articleLink1.setOnAction(e -> {
                    HostServices services = Helper.getInstance().getHostServices();
                    services.showDocument(article.getLinkToFullArticles());
                });
                Text text61 = new Text(".");
                textFlow3.getChildren().addAll(text51, articleLink1, text61);
                textFlow3.getStyleClass().add("textflowcenteritalic");
                // Add video cap
                Text text71 = new Text(index.text());
                text71.getStyleClass().add("textimagecap");
                TextFlow textFlowTemp0 = new TextFlow();
                textFlowTemp0.getChildren().add(text71);
                textFlowTemp0.getStyleClass().add("textflowcenter");
                vbox.getChildren().add(textFlowTemp0);
                continue;
            }
            // Add imageview
            if (!index.attr("data-src").isEmpty() || !index.attr("src").isEmpty()) {
                ImageView imageView = new ImageView();
                imageView.setCache(true);
                imageView.setCacheHint(CacheHint.SPEED);
                // Create new imageView
                if (index.attr("data-src").isEmpty()) {
                    imageView.setImage(new Image(index.attr("abs:src"), 600, 600, true, false, true));
                }
                else imageView.setImage(new Image(index.attr("abs:data-src"), 600, 600, true, false, true));
                imageView.setPreserveRatio(true);
                // Set the initial fitwidth for imageview
                if (Main.stage.getWidth() < 900) {
                    imageView.setFitWidth(Main.stage.getWidth() - 140);
                }
                if (Main.stage.getWidth() >= 900) {
                    imageView.setFitWidth(800);
                }
                // Bind the fitwidth property of imageView with stagewidth property
                ChangeListener<Number> changeListener = new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (t1.doubleValue() < 900) {
                            imageView.setFitWidth(t1.doubleValue() - 140);
                        }
                        if (t1.doubleValue() >= 900) {
                            imageView.setFitWidth(800);
                        }
                    }
                };
                changeListenerList.add(changeListener);
                Main.stage.widthProperty().addListener(changeListener);
                vbox.getChildren().remove(vbox.getChildren().size() - 1);
                vbox.getChildren().add(imageView);
                continue;
            }
            // Add image cap
            if (index.hasClass("pCaption") && index.hasText()) {
                Text imagecap = new Text(index.select("p").text());
                imagecap.getStyleClass().add("textimagecap");
                textFlow3.getChildren().add(imagecap);
                textFlow3.getStyleClass().add("textflowcenter");
                continue;
            }
            // Add Body ( h3 bold + text thuong)
            if (index.select("h3").hasText() && !index.parent().hasClass("z-corona-header")) { // h3 text bold ca dong
                Text boldtext = new Text(index.text());
                boldtext.getStyleClass().add("textbold");
                textFlow3.getChildren().add(boldtext);
                textFlow3.getStyleClass().add("textflowjustify");
                continue;
            }
            if (index.select("p").hasText() && !index.parent().parent().hasClass("inner-article")) { // text thuong
                Text normaltext = new Text(index.text());
                normaltext.getStyleClass().add("textnormal");
                textFlow3.getChildren().add(normaltext);
                textFlow3.getStyleClass().add("textflowjustify");
                continue;
            }
            // If not adding anything then remove the last index element (the new TextFlow)
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
        }

        // Display author
        TextFlow textFlow4 = new TextFlow();
        Text text4 = new Text(article.getAuthor());
        text4.getStyleClass().add("textauthor");
        textFlow4.getChildren().add(text4);
        textFlow4.getStyleClass().add("textflowright");
        vbox.getChildren().add(textFlow4);

        // Link to full article (read original post here.)
        TextFlow textFlow5 = new TextFlow();
        Text text5 = new Text("Read the original post ");
        text5.getStyleClass().add("textReadTheOriginalPost");
        Hyperlink articleLink = new Hyperlink("here");
        articleLink.getStyleClass().add("texthyperlink");
        articleLink.setOnAction(e -> {
            HostServices services = Helper.getInstance().getHostServices();
            services.showDocument(article.getLinkToFullArticles());
        });
        Text text6 = new Text(".");
        textFlow5.getChildren().addAll(text5, articleLink, text6);
        textFlow5.setStyle("-fx-font-style: italic; -fx-font-size: 18; -fx-alignment: left;");
        vbox.getChildren().add(textFlow5);

        document = null;
        all = null;
        fullDate = null;
        body = null;
        author = null;
        originalCategory = null;
    }

    /* FROM HERE WILL BE THE FUNCTION FOR VNEXPRESS.COM
       There will be 4 function
       Get RSS list
       Get search keyword list
       Get list open from web
       Display the full article  */
    // Vnexpress rss
    public static ArrayList<Article> getVnexpressList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> vnexpressNewsList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("item");

        // Add data to vnexpressNewsList (Title + date + thumb + link + category + source)
        for (int i = 0, k = 0; k < 15; i++, k++) {
            // Eliminate the elements do not have thumb
            if (!all.get(i).select("description").text().contains("img")) {
                k--;
                continue;
            }
            // Create new article object then add the object into the ArrayList
            vnexpressNewsList.add(new Article());
            // Get the description (link + thumb) of each object => Then cut into link + thumb
            String description = all.get(i).select("description").text();
            int startLink = description.indexOf("\"");
            int endLink = description.indexOf("\"", startLink + 1);
            int startThumb = description.indexOf("\"", endLink + 1);
            int endThumb = description.indexOf("\"", startThumb + 1);
            // Set thumb for object
            if (endThumb > 2) {
                vnexpressNewsList.get(k).setThumb(description.substring(startThumb + 1, endThumb));
            }
            else {
                vnexpressNewsList.remove(k);
                k--;
                continue;
            }
            // Set title for each object
            vnexpressNewsList.get(k).setTitle(all.get(i).select("title").text());
            // Set date for each object
            vnexpressNewsList.get(k).setDate(Helper.timeToUnixString2(all.get(i).select("pubdate").text()));
            // Set timeago for each object
            vnexpressNewsList.get(k).setTimeAgo(Helper.timeDiff(vnexpressNewsList.get(k).getDate()));
            // Set link for object
            vnexpressNewsList.get(k).setLinkToFullArticles(all.get(i).select("link").text());
            // Set category
            vnexpressNewsList.get(k).setCategory(category);
            // Set source
            vnexpressNewsList.get(k).setSource("vnexpress");
        }

        return vnexpressNewsList;
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}