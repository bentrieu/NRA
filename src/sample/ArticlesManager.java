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

    @Override
    public void start(Stage stage) throws Exception {

    }
}