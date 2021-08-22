package sample;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Pagination;
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
        Elements videoWeb = document.select("div.video-container");

        // Set fullDate for each object
        if (videoWeb.select("p.video-meta span").hasClass("publish")) {
            article.setFullDate(videoWeb.select("span.publish").text());
        } else {
            article.setFullDate(fullDate.text());
        }

        // Set author for each object
        if (videoWeb.select("p.video-meta span").hasClass("video-author")) {
            article.setAuthor(videoWeb.select("p.video-meta span.video-author").text());
        } else {
            if (author.hasText()) article.setAuthor(author.first().text());
        }

        // Set original category
        if (videoWeb.select("span.video-breadcrumb").hasText()) {
            article.setOriginalCategory(videoWeb.select("span.video-breadcrumb").text());
        }
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

        // Add video open link caption for video web
        if (videoWeb.select("div").hasClass("video-player")) {
            Text text510 = new Text("Watch video on this ");
            text510.getStyleClass().add("textReadTheOriginalPost");
            Hyperlink articleLink10 = new Hyperlink("link");
            articleLink10.getStyleClass().add("texthyperlink");
            articleLink10.setOnAction(e -> {
                HostServices services = Helper.getInstance().getHostServices();
                services.showDocument(article.getLinkToFullArticles());
            });
            Text text610 = new Text(".");
            TextFlow textFlow30 = new TextFlow();
            textFlow30.getChildren().addAll(text510, articleLink10, text610);
            textFlow30.getStyleClass().add("textflowcenteritalic");
            vbox.getChildren().add(textFlow30);
        }

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

    // Vnexpress search
    public static ArrayList<Article> getVnexpressSearchList(String keyWord, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> searchVnexpressList = new ArrayList<>();

        // Convert keyWord into link that can scrape
        String convertedKeyWord = "https://timkiem.vnexpress.net/?q=" + keyWord.trim().replaceAll("\\s", "%20").toLowerCase();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = convertedKeyWord;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("div.width_common.list-news-subfolder article[data-publishtime]");
        Elements thumbAndTitleAndLink = all.select("div.thumb-art");
        // There are no original category when searching with vnexpress

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0, k = 0; k < 15; i++, k++) {
            // Create new article object then add the object into the ArrayList
            searchVnexpressList.add(new Article());
            // Set source
            searchVnexpressList.get(k).setSource("vnexpress");
            // Set category manually
            searchVnexpressList.get(k).setCategory(category);
            // Set title for each object
            searchVnexpressList.get(k).setTitle(thumbAndTitleAndLink.get(i).select("a").attr("title"));
            // Set thumb for each object
            if (thumbAndTitleAndLink.get(i).select("picture img").hasAttr("data-src")) {
                searchVnexpressList.get(k).setThumb(thumbAndTitleAndLink.get(i).select("picture img").attr("data-src"));
            }
            else {
                searchVnexpressList.remove(k);
                k--;
                continue;
            }
            // Set link to full article for each object
            searchVnexpressList.get(k).setLinkToFullArticles(thumbAndTitleAndLink.get(i).select("a").attr("href"));
            // Set date for each object
            searchVnexpressList.get(k).setDate(all.get(i).attr("data-publishtime"));
            // Set time ago for each object
            searchVnexpressList.get(k).setTimeAgo(Helper.timeDiff(searchVnexpressList.get(k).getDate()));
        }

        return searchVnexpressList;
    }

    // Vnexpress web link: https://vnexpress.net/thoi-su/chinh-tri
    public static ArrayList<Article> getVnexpressWebList(String webURL, String category) throws IOException {
        // Create new list to store data then return
        ArrayList<Article> getVnexpressWebList = new ArrayList<>();

        // Set up jsoup
        Document document = Jsoup.connect(webURL).userAgent("mozilla").get();
        Elements all = document.select("article:not(.off-thumb).item-news-common");
        Elements links = all.select("div.thumb-art a[href]");
        Elements titles = all.select("div.thumb-art a[title]");
//        Elements descriptions = document.getElementsByClass("description");
        Elements pictures = all.select("div.thumb-art img[itemprop]");

        int maxSize = pictures.size() - all.select("ins.adsbyeclick").size();
        if (maxSize > 15) maxSize = 15;

        //Add scraped items into the class
        for(int i = 0, k = 0; i < maxSize; i++, k++){
            // Remove ads elements
            if (all.get(k).select("ins").hasClass("adsbyeclick")) {
                i--;
                continue;
            }
            // Create new object
            getVnexpressWebList.add(new Article());
            // Set source
            getVnexpressWebList.get(i).setSource("vnexpress");
            // Set category
            getVnexpressWebList.get(i).setCategory(category);
            // Set title
            getVnexpressWebList.get(i).setTitle(titles.get(k).attr("title"));
            // Set link to full article
            getVnexpressWebList.get(i).setLinkToFullArticles(links.get(k).attr("abs:href"));
//            // Set description
//            getVnexpressWebList.get(i).setDescription(descriptions.get(i).text());
            // Set thumb
            String linkThumb;
            if(pictures.get(k).getElementsByTag("img").attr("abs:data-src").isEmpty()){
                linkThumb = pictures.get(k).getElementsByTag("img").attr("abs:src");
                getVnexpressWebList.get(i).setThumb(pictures.get(k).getElementsByTag("img").attr("abs:src"));
            } else {
                linkThumb = pictures.get(k).getElementsByTag("img").attr("abs:data-src");
                getVnexpressWebList.get(i).setThumb(pictures.get(k).getElementsByTag("img").attr("abs:data-src"));
            }
            // Set date
            if (all.hasAttr("data-publishtime")) {
                getVnexpressWebList.get(i).setDate(all.get(k).attr("data-publishtime"));
            } else {
                if (linkThumb.indexOf(".jpg") > 0) getVnexpressWebList.get(i).setDate(linkThumb.substring(linkThumb.indexOf(".jpg") - 10, linkThumb.indexOf(".jpg")));
                if (linkThumb.indexOf(".png") > 0) getVnexpressWebList.get(i).setDate(linkThumb.substring(linkThumb.indexOf(".png") - 10, linkThumb.indexOf(".png")));
                if (linkThumb.indexOf(".jpeg") > 0) getVnexpressWebList.get(i).setDate(linkThumb.substring(linkThumb.indexOf(".jpeg") - 10, linkThumb.indexOf(".jpeg")));
                if (linkThumb.indexOf(".gif") > 0) getVnexpressWebList.get(i).setDate(linkThumb.substring(linkThumb.indexOf(".gif") - 10, linkThumb.indexOf(".gif")));
            }
            // Set time ago
            getVnexpressWebList.get(i).setTimeAgo(Helper.timeDiff(getVnexpressWebList.get(i).getDate()));
        }

        return getVnexpressWebList;
    }

    // Vnexpres full article scrape and display
    public static void displayVnexpressFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().clear();

        // Setup jsoup for each article
        String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements description = document.select("div.container p.description");
        Elements fck_detail = document.select("div.container article.fck_detail p, figcaption, img[data-src], source[data-src-image], p.author_mail, div.videoContainter"); //div.fig-picture == img[data-src], source[data-src-image]
        Elements time = document.select("div.container span.date");
        Elements originalCategory = document.select("div.container ul.breadcrumb li");

        // Set fullDate for each object
        article.setFullDate(time.text());

        // Set original category
        article.setOriginalCategory("");
        int k = 0;
        for (Element index : originalCategory) {
            if (k != originalCategory.size() - 1) article.setOriginalCategory(article.getOriginalCategory() + index.text() + " - ");
            else article.setOriginalCategory(article.getOriginalCategory() + index.text());
            k++;
        }

        // Display category
        Text text = new Text(article.getCategory());
        text.getStyleClass().add("textcategory");
        TextFlow textFlow = new TextFlow(text);
        vbox.getChildren().add(textFlow);

        // Display image source
        Image imageSource = new Image("resource/vnexpress_big.png", 200, 200, true, false, true);
        ImageView imageViewSource = new ImageView();
        imageViewSource.setCache(true);
        imageViewSource.setCacheHint(CacheHint.SPEED);
        imageViewSource.setImage(imageSource);
        imageViewSource.setPreserveRatio(true);
        imageViewSource.setFitHeight(60);
        vbox.getChildren().add(imageViewSource);

        // Display original category + fullDate
        Text text0 = new Text(article.getOriginalCategory() + "\n" + article.getFullDate());
        text0.getStyleClass().add("textfulldate");
        TextFlow textFlow0 = new TextFlow(text0);
        textFlow0.getStyleClass().add("textflowcenter");
        vbox.getChildren().add(textFlow0);

        // Display title
        Text text1 = new Text(article.getTitle());
        text1.getStyleClass().add("texttitle");
        TextFlow textFlow1 = new TextFlow(text1);
        textFlow1.setStyle("-fx-text-alignment: justify");
        vbox.getChildren().add(textFlow1);

        // Display description
        if (description.select("span.location-stamp").hasText()) {
            article.setDescription(description.select("span.location-stamp").text() + " - " + description.text().replaceFirst(description.select("span.location-stamp").text(), ""));
            Text text2 = new Text(article.getDescription());
            text2.getStyleClass().add("textdescription");
            TextFlow textFlow2 = new TextFlow(text2);
            textFlow2.getStyleClass().add("textflowjustify");
            HBox descriptionHbox = new HBox();
            descriptionHbox.getStyleClass().add("descriptionHbox");
            descriptionHbox.getChildren().add(textFlow2);
            vbox.getChildren().add(descriptionHbox);
        }
        else {
            article.setDescription(description.text());
            Text text2 = new Text(article.getDescription());
            text2.getStyleClass().add("textdescription");
            TextFlow textFlow2 = new TextFlow(text2);
            textFlow2.getStyleClass().add("textflowjustify");
            HBox descriptionHbox = new HBox();
            descriptionHbox.getStyleClass().add("descriptionHbox");
            descriptionHbox.getChildren().add(textFlow2);
            vbox.getChildren().add(descriptionHbox);
        }

        // Display all content
        for (Element index : fck_detail) {
            TextFlow textFlow3 = new TextFlow();
            vbox.getChildren().add(textFlow3);
            // Add video open link caption
            if (index.hasClass("videoContainter")) {
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
                continue;
            }
            // Image cap
            if (index.select("figcaption").hasText()) { // image_cap
                Text imagecap = new Text(index.select("p.Image").text());
                imagecap.getStyleClass().add("textimagecap");
                textFlow3.getChildren().add(imagecap);
                textFlow3.getStyleClass().add("textflowcenter");
                continue;
            }
            // Add image
            if (index.select("img").hasAttr("data-src") || index.select("source").hasAttr("data-src-image")) {
                ImageView imageView = new ImageView();
                imageView.setCache(true);
                imageView.setCacheHint(CacheHint.SPEED);
                if (index.select("img").hasAttr("data-src")) {
                    imageView.setImage(new Image(index.select("img").attr("data-src"), 600, 600, true, false, true));
                }
                else imageView.setImage(new Image(index.select("source").attr("data-src-image"), 600, 600, true, false, true));
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
            // Eliminate the p.Normal tag with their parent has class "box_brief_info"
            if (index.parent().hasClass("box_brief_info")) {
                vbox.getChildren().remove(vbox.getChildren().size() - 1);
                continue;
            }
            // Eliminate the p.Normal tag with their parent has class "item_slide_show"
            if (index.parent().parent().parent().hasClass("item_slide_show")) {
                vbox.getChildren().remove(vbox.getChildren().size() - 1);
                continue;
            }
            // Add body (text)
            if (index.hasText()) {
                // Hyper link
                if (index.getElementsByTag("a").hasText()) {
                    textFlow3 = Helper.getHyperLink(index);
                    vbox.getChildren().remove(vbox.getChildren().size() - 1);
                    vbox.getChildren().add(textFlow3);
                    continue;
                }
                // Add author
                if (index.attr("style").contains("right") || index.hasAttr("align")) {
                    Text author = new Text(index.text());
                    author.getStyleClass().add("textauthor");
                    textFlow3.getChildren().add(author);
                    textFlow3.getStyleClass().add("textflowright");
                    continue;
                }
                // Strong text
                if (index.getElementsByTag("strong").hasText()) {
                    if (index.hasClass("author_mail") || index.hasClass("author")) {
                        Text author2 = new Text(index.text());
                        author2.getStyleClass().add("textbold");
                        textFlow3.getChildren().add(author2);
                        textFlow3.getStyleClass().add("textflowright");
                        continue;
                    }
                    else { // làm tương tự như hyper link
                        String string = index.text().replaceAll(index.select("strong").text(), "<strong>" + index.select("strong").text() + "</strong>");
                        String[] stringSplit = string.split("<strong>");
                        if (!stringSplit[0].isEmpty()) {
                            Text tempText = new Text(stringSplit[0]);
                            tempText.getStyleClass().add("textnormal");
                            textFlow3.getChildren().add(tempText);
                            textFlow3.getStyleClass().add("textflowjustify");
                        }
                        for (int i = 1; i < stringSplit.length; i++) {
                            Text textTemp = new Text(stringSplit[i].substring(0, stringSplit[i].indexOf("</strong>")));
                            textTemp.getStyleClass().add("textbold");
                            Text textTemp1 = new Text(stringSplit[i].substring(stringSplit[i].indexOf("</strong>") + 9));
                            textTemp1.getStyleClass().add("textnormal");
                            textFlow3.getChildren().addAll(textTemp, textTemp1);
                            textFlow3.getStyleClass().add("textflowjustify");
                        }
                        continue;
                    }
                }
                // normal text
                else {
                    if (index.parent().hasAttr("itemprop")) {
                        vbox.getChildren().remove(vbox.getChildren().size() - 1);
                        continue;
                    }
                    Text textNormal = new Text(index.text());
                    textNormal.getStyleClass().add("textnormal");
                    textFlow3.getChildren().add(textNormal);
                    textFlow3.getStyleClass().add("textflowjustify");
                }
                continue;
            }
            // Add author in some special case
            if (index.select("p.author_mail").hasText()) { // tác giả (có bài viết tác giả phải lấy kiểu này, không lấy bằng text thông thường được)
                Text author3 = new Text(index.text());
                author3.getStyleClass().add("textbold");
                textFlow3.getChildren().add(author3);
                textFlow3.getStyleClass().add("textflowright");
                continue;
            }
            // If not adding anything then remove the last index element (the new TextFlow)
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
        }

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

        fullArticlesUrl = null;
        document = null;
        description = null;
        fck_detail = null;
        time = null;
        originalCategory = null;
    }

    /* FROM HERE WILL BE THE FUNCTION FOR TUOITRE.VN
       There will be 4 function
       Get RSS list
       Get search keyword list
       Get list open from web
       Display the full article  */
    // Tuoitre rss
    public static ArrayList<Article> getTuoiTreList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> tuoiTreList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("item");
        Elements thumbAndDescription = all.select("description");
        Elements title = all.select("title");
        Elements link = all.select("link");
        Elements date = all.select("pubDate");

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0; i < 15; i++) {
            // Create new article object then add the object into the ArrayList
            tuoiTreList.add(new Article());
            // Set source
            tuoiTreList.get(i).setSource("tuoitre");
            // Set category manually
            tuoiTreList.get(i).setCategory(category);
            // Set title for each object
            tuoiTreList.get(i).setTitle(title.get(i).text());
            // Set date for each object
            String dateTemp = Helper.timeToUnixString4(date.get(i).text());
            tuoiTreList.get(i).setDate(dateTemp);
            // Set time ago for each object
            tuoiTreList.get(i).setTimeAgo(Helper.timeDiff(dateTemp));
            // Set thumb and description for each object
            String string = thumbAndDescription.get(i).text();
            int startLink = string.indexOf("src=\"") + 5;
            int endLink = string.indexOf("\"", startLink) - 1;
            int startDescription = string.indexOf("</a>") + 4;
            tuoiTreList.get(i).setThumb(string.substring(startLink, endLink + 1).replaceFirst("zoom/[_0-9]+/", "thumb_w/586/"));
            tuoiTreList.get(i).setDescription(string.substring(startDescription));
            // Set link to full article for each object
            tuoiTreList.get(i).setLinkToFullArticles(link.get(i).text());
        }

        return tuoiTreList;
    }

    // Tuoitre web list: https://tuoitre.vn/thoi-su.htm (not yet done)
    public static ArrayList<Article> getTuoiTreWebList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> tuoiTreWebList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("item");
        Elements thumbAndDescription = all.select("description");
        Elements title = all.select("title");
        Elements link = all.select("link");
        Elements date = all.select("pubDate");

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0; i < 15; i++) {
            // Create new article object then add the object into the ArrayList
            tuoiTreWebList.add(new Article());
            // Set source
            tuoiTreWebList.get(i).setSource("tuoitre");
            // Set category manually
            tuoiTreWebList.get(i).setCategory(category);
            // Set title for each object
            tuoiTreWebList.get(i).setTitle(title.get(i).text());
            // Set date for each object
            String dateTemp = Helper.timeToUnixString4(date.get(i).text());
            tuoiTreWebList.get(i).setDate(dateTemp);
            // Set time ago for each object
            tuoiTreWebList.get(i).setTimeAgo(Helper.timeDiff(dateTemp));
            // Set thumb and description for each object
            String string = thumbAndDescription.get(i).text();
            int startLink = string.indexOf("src=\"") + 5;
            int endLink = string.indexOf("\"", startLink) - 1;
            int startDescription = string.indexOf("</a>") + 4;
            tuoiTreWebList.get(i).setThumb(string.substring(startLink, endLink + 1).replaceFirst("zoom/[_0-9]+/", "thumb_w/586/"));
            tuoiTreWebList.get(i).setDescription(string.substring(startDescription));
            // Set link to full article for each object
            tuoiTreWebList.get(i).setLinkToFullArticles(link.get(i).text());
        }

        return tuoiTreWebList;
    }

    // Tuoitre search
    public static ArrayList<Article> getTuoiTreSearchList(String keyWord, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> searchTuoiTreList = new ArrayList<>();

        // Convert keyWord into link that can scrape
        String convertedKeyWord = "https://tuoitre.vn/tim-kiem.htm?keywords=" + keyWord.trim().replaceAll("\\s", "%20").toLowerCase();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = convertedKeyWord;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("ul.list-news-content li.news-item");
        // There are no date and original category when searching with tuoitre

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0; i < 15; i++) {
            // Create new article object then add the object into the ArrayList
            searchTuoiTreList.add(new Article());
            // Set source
            searchTuoiTreList.get(i).setSource("tuoitre");
            // Set category manually
            searchTuoiTreList.get(i).setCategory(category);
            // Set title for each object
            searchTuoiTreList.get(i).setTitle(all.get(i).select("h3.title-news").text());
            // Set thumb for each object
            String linkThumb = all.get(i).select("img").attr("abs:data-src");
            searchTuoiTreList.get(i).setThumb(linkThumb.replaceFirst("zoom/[_0-9]+/", "thumb_w/586/"));
            // Set description for each object
            searchTuoiTreList.get(i).setDescription(all.get(i).select("div.name-news p.sapo").text());
            // Set link to full article for each object
            searchTuoiTreList.get(i).setLinkToFullArticles(all.get(i).select("a").first().attr("abs:href"));
            // Set date
            String date = linkThumb.substring(linkThumb.indexOf("crop-") + 5, linkThumb.indexOf("crop-") + 15);
            searchTuoiTreList.get(i).setDate(date);
            // Set time ago for each object
            searchTuoiTreList.get(i).setTimeAgo(Helper.timeDiff(date));
        }

        return searchTuoiTreList;
    }

    // Tuoitre full article scrape and display
    public static void displayTuoiTreFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().clear();

        // Setup jsoup for each article
        String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements all = document.select("div.content-detail");
        Elements fullDate = document.select("div.date-time");
        Elements body = all.select("div.main-content-body div.content.fck").select("> p, div.VCSortableInPreviewMode");
        Elements author = all.select("div.author");
        Elements originalCategory = document.select("div.bread-crumbs li");
        System.out.println(body);

        // Set fullDate for each object
        if (fullDate.hasText()) {
            article.setFullDate(fullDate.first().text());
        }

        // Set author for each object
        if (author.hasText()) {
            article.setAuthor(author.first().text());
        }

        // Set original category for each object
        article.setOriginalCategory("");
        int k = 0;
        for (Element index : originalCategory) {
            if (k != originalCategory.size() - 1) article.setOriginalCategory(article.getOriginalCategory() + index.text() + " - ");
            else article.setOriginalCategory(article.getOriginalCategory() + index.text());
            k++;
        }

        // Display category
        Text text = new Text(article.getCategory());
        text.getStyleClass().add("textcategory");
        TextFlow textFlow = new TextFlow(text);
        vbox.getChildren().add(textFlow);

        // Display image source
        Image imageSource = new Image("resource/tuoitre_big.png", 200, 200, true, false, true);
        ImageView imageViewSource = new ImageView();
        imageViewSource.setCache(true);
        imageViewSource.setCacheHint(CacheHint.SPEED);
        imageViewSource.setImage(imageSource);
        imageViewSource.setPreserveRatio(true);
        imageViewSource.setFitHeight(60);
        vbox.getChildren().add(imageViewSource);

        // Display original category + fullDate
        Text text0 = new Text(article.getOriginalCategory() + "\n" + article.getFullDate());
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
            if (index.attr("type").equals("insertembedcode")) {
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
                continue;
            }
            // Add imageview
            if (index.select("img").hasAttr("data-original")) {
                // Create new imageView
                ImageView imageView = new ImageView();
                imageView.setCache(true);
                imageView.setCacheHint(CacheHint.SPEED);
                imageView.setImage(new Image(index.select("img").attr("data-original"), 600, 600, true, false, true));
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
                // Add image cap
                Text imagecap = new Text(index.select("div.PhotoCMS_Caption").text());
                imagecap.getStyleClass().add("textimagecap");
                textFlow3.getChildren().add(imagecap);
                textFlow3.getStyleClass().add("textflowcenter");
                vbox.getChildren().remove(vbox.getChildren().size() - 1);
                vbox.getChildren().addAll(imageView, textFlow3);
                continue;
            }
            // Add Body ( hyper link + bold + text normal text)
            if (index.hasText() && !index.attr("type").equals("RelatedOneNews") && !index.attr("type").equals("RelatedNews")) {
                // Hyper link
                if (index.select("a").hasAttr("href")) {
                    textFlow3 = Helper.getHyperLink(index);
                    vbox.getChildren().remove(vbox.getChildren().size() - 1);
                    vbox.getChildren().add(textFlow3);
                    continue;
                }
                // Bold text
                if (index.select("b").hasText()) {
                    String string = index.text().replaceAll(index.select("b").text().replaceAll("\\*", ""), "<strong>" + index.select("b").text().replaceAll("\\*", "") + "</strong>");
                    String[] stringSplit = string.split("<strong>");
                    if (!stringSplit[0].isEmpty()) {
                        Text textTemp = new Text(stringSplit[0]);
                        textTemp.getStyleClass().add("textnormal");
                        textFlow3.getChildren().add(textTemp);
                        textFlow3.getStyleClass().add("textflowjustify");
                    }
                    for (int i = 1; i < stringSplit.length; i++) {
                        Text textTemp0 = new Text(stringSplit[i].substring(0, stringSplit[i].indexOf("</strong>")));
                        textTemp0.getStyleClass().add("textbold");
                        Text textTemp1 = new Text(stringSplit[i].substring(stringSplit[i].indexOf("</strong>") + 9));
                        textTemp1.getStyleClass().add("textnormal");
                        textFlow3.getChildren().addAll(textTemp0, textTemp1);
                        textFlow3.getStyleClass().add("textflowjustify");
                    }
                    continue;
                }
                else  {
                    Text textTemp3 = new Text(index.text());
                    textTemp3.getStyleClass().add("textnormal");
                    textFlow3.getChildren().add(textTemp3);
                    textFlow3.getStyleClass().add("textflowjustify");
                }
                continue;
            }
            // If not adding anything then remove the last index element (the new TextFlow)
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
        }

        // Display author
        TextFlow textFlow4 = new TextFlow();
        Text author1 = new Text(article.getAuthor());
        author1.getStyleClass().add("textauthor");
        textFlow4.getChildren().add(author1);
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

        fullArticlesUrl = null;
        document = null;
        all = null;
        fullDate = null;
        body = null;
        author = null;
        originalCategory = null;
    }

    /* FROM HERE WILL BE THE FUNCTION FOR THANHNIEN.VN
       There will be 4 function
       Get RSS list
       Get search keyword list
       Get list open from web
       Display the full article  */
    // Thanhnien rss
    public static ArrayList<Article> getThanhNienList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> thanhNienList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("item");
        Elements description = all.select("description");
        Elements thumb = all.select("image");
        Elements title = all.select("title");
        Elements link = all.select("link");
        Elements date = all.select("pubDate");

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0; i < 15; i++) {
            // Create new article object then add the object into the ArrayList
            thanhNienList.add(new Article());
            // Set source
            thanhNienList.get(i).setSource("thanhnien");
            // Set category manually
            thanhNienList.get(i).setCategory(category);
            // Set title for each object
            thanhNienList.get(i).setTitle(title.get(i).text());
            // Set date for each object
            String dateTemp = Helper.timeToUnixString2(date.get(i).text());
            thanhNienList.get(i).setDate(dateTemp);
            // Set time ago for each object
            thanhNienList.get(i).setTimeAgo(Helper.timeDiff(dateTemp));
            // Set thumb for each object
            if (thumb.size() != 0) thanhNienList.get(i).setThumb(thumb.get(i).text().replaceFirst("400x300", "2048"));
            else {
                String descriptionText = description.get(i).text();
                int startThumb = descriptionText.indexOf("src=\"") + 5;
                int endThumb = descriptionText.indexOf("\"", startThumb) - 1;
                String thumbLink = descriptionText.substring(startThumb, endThumb + 1);
                if (thumbLink.contains("180")) thumbLink = thumbLink.replaceFirst("/180/", "/2048/");
                if (thumbLink.contains("400x300")) thumbLink = thumbLink.replaceFirst("/400x300/", "/2048/");
                thanhNienList.get(i).setThumb(thumbLink);
            }
            // Set link to full article for each object
            thanhNienList.get(i).setLinkToFullArticles(link.get(i).text());
        }

        return thanhNienList;
    }

    // Thanhnien web list (https://thanhnien.vn/the-gioi/)
    public static ArrayList<Article> getThanhNienWebList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> thanhNienWebList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("article.story.story--primary, div.l-grid div.relative article.story, div.highlight article.story, div#timeline article.story");
        Elements thumb = all.select("img");
        Elements titleAndLink = all.select("a.story__title");
        Elements date = all.select("time[rel]");

        int maxSize = 15;
        if (category.equals("Sports")) maxSize = 10;

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0, k = 0; k < maxSize; i++, k++) {
            if (all.get(i).hasClass("story--video") || all.get(i).parent().hasClass("feature")) {
                k--;
                continue;
            }
            // Create new article object then add the object into the ArrayList
            thanhNienWebList.add(new Article());
            // Set source
            thanhNienWebList.get(k).setSource("thanhnien");
            // Set category manually
            thanhNienWebList.get(k).setCategory(category);
            // Set title for each object
            thanhNienWebList.get(k).setTitle(titleAndLink.get(i).text());
            // Set date for each object
            String dateTemp = date.get(i).attr("rel").substring(0, 10);
            long dateTemp0 = Long.valueOf(dateTemp) - 25200;
            dateTemp = String.valueOf(dateTemp0);
            thanhNienWebList.get(k).setDate(dateTemp);
            // Set time ago for each object
            thanhNienWebList.get(k).setTimeAgo(Helper.timeDiff(dateTemp));
            // Set thumb for each object
            String thumbTemp;
            if (thumb.get(i).attr("data-src").isEmpty()) {
                thumbTemp = thumb.get(i).attr("src");
            } else {
                thumbTemp = thumb.get(i).attr("data-src");
            }
            if (thumbTemp.contains("c150x100")) thumbTemp = thumbTemp.replaceFirst("c150x100/[,0-9]+/", "2048/"); // c150x100/8,0,92,0
            else {
                if (thumbTemp.contains("150x100")) thumbTemp = thumbTemp.replaceFirst("150x100", "2048"); // 150x100
            }
            thanhNienWebList.get(k).setThumb(thumbTemp);
            // Set link to full article for each object
            thanhNienWebList.get(k).setLinkToFullArticles(titleAndLink.get(i).attr("abs:href"));
        }

        return thanhNienWebList;
    }

    // Thanhnien full article scrape and display
    public static void displayThanhNienFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().clear();

        // Setup jsoup for each article
        String fullArticlesUrl = article.getLinkToFullArticles();
//        String fullArticlesUrl = "https://thanhnien.vn/the-thao/tuong-thuat/udinese-juventus-533880.html";
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements all = document.select("div.l-content div.pswp-content, div.l-grid, section.container");
        Elements description = all.select("div.sapo");
        Elements body = all.select("div#abody").select("div, h2, p");
        Elements author = document.select("div.details__author div.left h4");
        Elements originalCategory = document.select("div.breadcrumbs span[itemprop]");
        Elements fullDate = document.select("div.details__meta div.meta time");
        Elements descriptionImage = all.select("div#contentAvatar");
        Elements originalCategory2 = document.select("div.breadcrumbs span[itemprop]");

        // Set fullDate for each object
        if (fullDate.hasText()) {
            article.setFullDate(fullDate.first().text());
        }

        // Set author for each object
        if (author.hasText()) {
            article.setAuthor(author.first().text());
        }

        // Set original category for each object
        int size = 0;
        for (Element index : originalCategory) {
            if (index.attr("itemprop").equals("name")) {
                size++;
            }
        }
        article.setOriginalCategory("");
        int k = 0;
        for (Element index : originalCategory) {
            if (index.attr("itemprop").equals("name")) {
                if (k != size - 1)
                    article.setOriginalCategory(article.getOriginalCategory() + index.text() + " - ");
                else article.setOriginalCategory(article.getOriginalCategory() + index.text());
                k++;
            }
        }

        // Display category
        Text text = new Text(article.getCategory());
        text.getStyleClass().add("textcategory");
        TextFlow textFlow = new TextFlow(text);
        vbox.getChildren().add(textFlow);

        // Display image source
        Image imageSource = new Image("resource/thanhnien_big.png", 200, 200, true, false, true);
        ImageView imageViewSource = new ImageView();
        imageViewSource.setCache(true);
        imageViewSource.setCacheHint(CacheHint.SPEED);
        imageViewSource.setImage(imageSource);
        imageViewSource.setPreserveRatio(true);
        imageViewSource.setFitHeight(60);
        vbox.getChildren().add(imageViewSource);

        // Display original category + fullDate
        Text text0 = new Text(article.getOriginalCategory() + "\n" + article.getFullDate());
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
        if (description.select("a").hasAttr("href")) {
            TextFlow textFlow2 = Helper.getHyperLink(description.first());
            textFlow2.getStyleClass().add("textflowjustify");
            HBox descriptionHbox = new HBox();
            descriptionHbox.getStyleClass().add("descriptionHbox");
            descriptionHbox.getChildren().add(textFlow2);
            vbox.getChildren().add(descriptionHbox);
        } else {
            Text text2 = new Text();
            if (description.size() > 0) {
                if (!description.first().ownText().isEmpty()) {
                    text2.setText(description.first().ownText());
                }
                if (description.hasText()) {
                    text2.setText(description.text());
                    text2.getStyleClass().add("textdescription");
                    TextFlow textFlow2 = new TextFlow(text2);
                    textFlow2.getStyleClass().add("textflowjustify");
                    HBox descriptionHbox = new HBox();
                    descriptionHbox.getStyleClass().add("descriptionHbox");
                    descriptionHbox.getChildren().add(textFlow2);
                    vbox.getChildren().add(descriptionHbox);
                }
            }
        }

        // Display image description
        if (descriptionImage.select("img").hasAttr("data-src") || descriptionImage.select("img").hasAttr("src")) {
            // Create new imageView
            ImageView imageView0 = new ImageView();
            imageView0.setCache(true);
            imageView0.setCacheHint(CacheHint.SPEED);
            if (descriptionImage.select("img").attr("data-src").isEmpty()) {
                imageView0.setImage(new Image(descriptionImage.select("img").attr("abs:src"), 600, 600, true, false, true));
            }
            else {
                imageView0.setImage(new Image(descriptionImage.select("img").attr("abs:data-src"), 600, 600, true, false, true));
            }
            imageView0.setPreserveRatio(true);
            // Set the initial fitwidth for imageview
            if (Main.stage.getWidth() < 900) {
                imageView0.setFitWidth(Main.stage.getWidth() - 140);
            }
            if (Main.stage.getWidth() >= 900) {
                imageView0.setFitWidth(800);
            }
            // Bind the fitwidth property of imageView with stagewidth property
            ChangeListener<Number> changeListener0 = new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    if (t1.doubleValue() < 900) {
                        imageView0.setFitWidth(t1.doubleValue() - 140);
                    }
                    if (t1.doubleValue() >= 900) {
                        imageView0.setFitWidth(800);
                    }
                }
            };
            changeListenerList.add(changeListener0);
            Main.stage.widthProperty().addListener(changeListener0);
            // Get image cap
            if (!descriptionImage.select("div.imgcaption").text().isEmpty()) {
                Text imagecap0 = new Text(descriptionImage.select("div.imgcaption").text());
                imagecap0.getStyleClass().add("textimagecap");
                TextFlow textFlowTemp = new TextFlow();
                textFlowTemp.getChildren().add(imagecap0);
                textFlowTemp.getStyleClass().add("textflowcenter");
                vbox.getChildren().addAll(imageView0, textFlowTemp);
            }
        }

        // Add video open link caption when scrape video article
        if (document.select("div.media-player div").hasClass("cms-video")) {
            Text text512 = new Text("Watch video on this ");
            text512.getStyleClass().add("textReadTheOriginalPost");
            Hyperlink articleLink12 = new Hyperlink("link");
            articleLink12.getStyleClass().add("texthyperlink");
            articleLink12.setOnAction(e -> {
                HostServices services = Helper.getInstance().getHostServices();
                services.showDocument(article.getLinkToFullArticles());
            });
            Text text612 = new Text(".");
            TextFlow textFlow31 = new TextFlow();
            textFlow31.getChildren().addAll(text512, articleLink12, text612);
            textFlow31.getStyleClass().add("textflowcenteritalic");
            vbox.getChildren().add(textFlow31);
        }

        ArrayList<String> repeatCheck = new ArrayList<>();
        // Display all content
        for (Element index : body) {
            TextFlow textFlow3 = new TextFlow();
            vbox.getChildren().add(textFlow3);
            // Eliminate details__morenews element
            if (index.hasClass("details__morenews")) {
                vbox.getChildren().remove(vbox.getChildren().size() - 1);
                continue;
            }
            // Add video open link caption
            if (index.hasAttr("data-video-src") /*|| index.hasAttr("clearfix")*/) {
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
                continue;
            }
            // Add imageview
            if (index.select("> img").hasAttr("data-src") /*&& !index.hasClass("cms-body")*/) {
                // Create new imageView
                ImageView imageView = new ImageView();
                imageView.setCache(true);
                imageView.setCacheHint(CacheHint.SPEED);
                imageView.setImage(new Image(index.select("img").attr("data-src"), 600, 600, true, false, true));
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
                vbox.getChildren().addAll(imageView);
                continue;
            }
            // Add image cap
            if (index.hasClass("imgcaption")) {
                Text imagecap = new Text(index.select("p").text());
                imagecap.getStyleClass().add("textimagecap");
                textFlow3.getChildren().add(imagecap);
                textFlow3.getStyleClass().add("textflowcenter");
                continue;
            }
            // Add body
            if ((!index.ownText().isEmpty() && !index.parent().hasClass("imgcaption") && !index.parent().hasClass("source")) || index.is("h2")) {
                // Hyper link
                if (index.select("a").hasAttr("href")) {
                    vbox.getChildren().remove(vbox.getChildren().size() - 1);
                    textFlow3 = Helper.getHyperLink(index);
                    vbox.getChildren().add(textFlow3);
                    continue;
                }
                // Bold text
                if (index.is("h2")) {
                    repeatCheck.add(index.select("h2").text());
                    if (repeatCheck.size() > 1) {
                        if (index.select("h2").text().equals(repeatCheck.get(repeatCheck.size() - 1 - 1))) {
                            vbox.getChildren().remove(vbox.getChildren().size() - 1);
                            continue;
                        }
                    }
                    String string = index.text().replaceAll(index.select("h2").text().replaceAll("\\*", ""), "<strong>" + index.select("h2").text().replaceAll("\\*", "") + "</strong>");
                    String[] stringSplit = string.split("<strong>");
                    if (!stringSplit[0].isEmpty()) {
                        Text textTemp = new Text(stringSplit[0]);
                        textTemp.getStyleClass().add("textnormal");
                        textFlow3.getChildren().add(textTemp);
                        textFlow3.getStyleClass().add("textflowjustify");
                    }
                    for (int i = 1; i < stringSplit.length; i++) {
                        Text textTemp0 = new Text(stringSplit[i].substring(0, stringSplit[i].indexOf("</strong>")));
                        textTemp0.getStyleClass().add("textbold");
                        Text textTemp1 = new Text(stringSplit[i].substring(stringSplit[i].indexOf("</strong>") + 9));
                        textTemp1.getStyleClass().add("textnormal");
                        textFlow3.getChildren().addAll(textTemp0, textTemp1);
                        textFlow3.getStyleClass().add("textflowjustify");
                    }
                    continue;
                }
                // Normal text
                Text textTemp3 = new Text(index.text());
                textTemp3.getStyleClass().add("textnormal");
                textFlow3.getChildren().add(textTemp3);
                textFlow3.getStyleClass().add("textflowjustify");
                continue;
            }
            // If not adding anything then remove the last index element (the new TextFlow)
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
        }
        repeatCheck = null;

        // Display author
        TextFlow textFlow4 = new TextFlow();
        Text author1 = new Text(article.getAuthor());
        author1.getStyleClass().add("textauthor");
        textFlow4.getChildren().add(author1);
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

        fullArticlesUrl = null;
        document = null;
        all = null;
        description = null;
        body = null;
        author = null;
        originalCategory = null;
        fullDate = null;
        descriptionImage = null;

    }

    /* FROM HERE WILL BE THE FUNCTION FOR NHANDAN.VN
       There will be 4 function
       Get RSS list
       Get search keyword list
       Get list open from web
       Display the full article  */
    // Nhandan web list (https://nhandan.vn/chinhtri)
    public static ArrayList<Article> getNhanDanWebList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> nhanDanWebList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("div.uk-grid-site article, div.featured-bottom article, div.uk-width-1-1 article");
        Elements thumb = all.select("div.box-img");
        Elements titleAndLink = all.select("div.box-title");

        int maxArticle = 15;
        if (category.equals("Covid")) maxArticle = 10;
        if (category.equals("News")) maxArticle = 9;

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0; i < maxArticle; i++) {
            // Create new article object then add the object into the ArrayList
            nhanDanWebList.add(new Article());
            // Set source
            nhanDanWebList.get(i).setSource("nhandan");
            // Set category manually
            nhanDanWebList.get(i).setCategory(category);
            // Set title for each object
            nhanDanWebList.get(i).setTitle(titleAndLink.get(i).text());
            // Set date for each object
            String dateTemp;
            if (all.get(i).select("div.box-meta-small").hasText()) {
                dateTemp = all.get(i).select("div.box-meta-small").text();
                dateTemp = Helper.timeToUnixString5(dateTemp);
                nhanDanWebList.get(i).setDate(dateTemp);
            } else {
                dateTemp = thumb.get(i).select("img").attr("data-src").toLowerCase();
                int endLink = 0;
                if (dateTemp.contains(".jpg")) endLink = dateTemp.indexOf(".jpg");
                if (dateTemp.contains(".png")) endLink = dateTemp.indexOf(".png");
                if (dateTemp.contains(".jpeg")) endLink = dateTemp.indexOf(".jpeg");
                if (dateTemp.contains(".gif")) endLink = dateTemp.indexOf(".gif");
                dateTemp = dateTemp.substring(endLink - 13, endLink - 3);
                nhanDanWebList.get(i).setDate(dateTemp);
            }
            // Set time ago for each object
            nhanDanWebList.get(i).setTimeAgo(Helper.timeDiff(dateTemp));
            // Set thumb for each object
            String thumbTemp = thumb.get(i).select("img").attr("data-src");
            thumbTemp = thumbTemp.replaceFirst("resize/[^/]+/", "");
            nhanDanWebList.get(i).setThumb(thumbTemp);
            // Set link to full article for each object
            nhanDanWebList.get(i).setLinkToFullArticles(titleAndLink.get(i).select("a").attr("abs:href"));
        }

        return nhanDanWebList;
    }

    // Nhandan seach list https://nhandan.vn/Search/%22h%E1%BB%8Dc%20vi%E1%BB%87n%22
    public static ArrayList<Article> getNhanDanSearchList(String keyword, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> nhanDanWebList = new ArrayList<>();

        // Bắt đầu từ đây là add dữ liệu cho sort article
        // Setup jsoup for scraping data
        keyword = keyword.toLowerCase().trim();
        keyword = keyword.replaceAll("\\s", "%20");
        final String url = "https://nhandan.vn/Search/" + keyword;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("div.uk-grid-site article, div.featured-bottom article, div.uk-width-1-1 article");
        Elements thumb = all.select("div.box-img");
        Elements titleAndLink = all.select("div.box-title");

        int maxArticle = 15;
        if (thumb.size() < 15) maxArticle = thumb.size();

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0, k = 0; i < maxArticle; i++, k++) {
            // Eliminate elements don't have thumb
            if (!all.select("div.box-img a").hasAttr("href")) {
                i--;
                continue;
            }
            // Create new article object then add the object into the ArrayList
            nhanDanWebList.add(new Article());
            // Set source
            nhanDanWebList.get(i).setSource("nhandan");
            // Set category manually
            nhanDanWebList.get(i).setCategory(category);
            // Set title for each object
            nhanDanWebList.get(i).setTitle(titleAndLink.get(k).text());
            // Set date for each object
            String dateTemp;
            if (all.get(i).select("div.box-meta-small").hasText()) {
                dateTemp = all.get(k).select("div.box-meta-small").text();
                dateTemp = Helper.timeToUnixString5(dateTemp);
                nhanDanWebList.get(i).setDate(dateTemp);
            } else {
                dateTemp = all.get(k).select("div.box-img img").attr("abs:data-src").toLowerCase();
                int endLink = 0;
                if (dateTemp.contains(".jpg")) endLink = dateTemp.indexOf(".jpg");
                if (dateTemp.contains(".png")) endLink = dateTemp.indexOf(".png");
                if (dateTemp.contains(".jpeg")) endLink = dateTemp.indexOf(".jpeg");
                if (dateTemp.contains(".gif")) endLink = dateTemp.indexOf(".gif");
                dateTemp = dateTemp.substring(endLink - 13, endLink - 3);
                nhanDanWebList.get(i).setDate(dateTemp);
            }
            // Set time ago for each object
            nhanDanWebList.get(i).setTimeAgo(Helper.timeDiff(dateTemp));
            // Set thumb for each object
            String thumbTemp = all.get(k).select("div.box-img img").attr("abs:data-src");
            thumbTemp = thumbTemp.replaceFirst("resize/[^/]+/", "");
            nhanDanWebList.get(i).setThumb(thumbTemp);
            // Set link to full article for each object
            nhanDanWebList.get(i).setLinkToFullArticles(all.get(k).select("div.box-title a").attr("abs:href"));
        }

        return nhanDanWebList;
    }

    // Nhandan full article scrape and display
    public static void displayNhanDanFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().clear();

        // Setup jsoup for each article
        String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements all = document.select("div.box-content-detail");
        Elements description = all.select("div.box-des-detail");
        Elements body = all.select("div.detail-content-body ").select("> p, figure");
        Elements author = all.select("div.box-author");
        Elements originalCategory = document.select("ul.uk-breadcrumb li");
        Elements fullDate = document.select("div.box-date.pull-left");
        Elements descriptionImage = document.select("div.box-detail-thumb");

        // Set fullDate for each object
        if (fullDate.hasText()) {
            article.setFullDate(fullDate.first().text());
        }

        // Set author for each object
        if (author.hasText()) {
            article.setAuthor(author.first().text());
        }

        // Set original category for each object
        article.setOriginalCategory("");
        int k = 0;
        for (Element index : originalCategory) {
            if (k != originalCategory.size() - 1) article.setOriginalCategory(article.getOriginalCategory() + index.text() + " - ");
            else article.setOriginalCategory(article.getOriginalCategory() + index.text());
            k++;
        }

        // Display category
        Text text = new Text(article.getCategory());
        text.getStyleClass().add("textcategory");
        TextFlow textFlow = new TextFlow(text);
        vbox.getChildren().add(textFlow);

        // Display image source
        Image imageSource = new Image("resource/nhandan_big.png", 200, 200, true, false, true);
        ImageView imageViewSource = new ImageView();
        imageViewSource.setCache(true);
        imageViewSource.setCacheHint(CacheHint.SPEED);
        imageViewSource.setImage(imageSource);
        imageViewSource.setPreserveRatio(true);
        imageViewSource.setFitHeight(60);
        vbox.getChildren().add(imageViewSource);

        // Display original category + fullDate
        Text text0 = new Text(article.getOriginalCategory() + "\n" + article.getFullDate());
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
        Text text2 = new Text(description.text());
        text2.getStyleClass().add("textdescription");
        TextFlow textFlow2 = new TextFlow(text2);
        textFlow2.getStyleClass().add("textflowjustify");
        HBox descriptionHbox = new HBox();
        descriptionHbox.getStyleClass().add("descriptionHbox");
        descriptionHbox.getChildren().add(textFlow2);
        vbox.getChildren().add(descriptionHbox);

        // Display image description
        if (descriptionImage.select("img").hasAttr("data-src") || descriptionImage.select("img").hasAttr("src")) {
            // Create new imageView
            ImageView imageView0 = new ImageView();
            imageView0.setCache(true);
            imageView0.setCacheHint(CacheHint.SPEED);
            if (descriptionImage.select("img").attr("data-src").isEmpty()) {
                imageView0.setImage(new Image(descriptionImage.select("img").attr("abs:src"), 600, 600, true, false, true));
            }
            else {
                imageView0.setImage(new Image(descriptionImage.select("img").attr("abs:data-src"), 600, 600, true, false, true));
            }
            imageView0.setPreserveRatio(true);
            // Set the initial fitwidth for imageview
            if (Main.stage.getWidth() < 900) {
                imageView0.setFitWidth(Main.stage.getWidth() - 140);
            }
            if (Main.stage.getWidth() >= 900) {
                imageView0.setFitWidth(800);
            }
            // Bind the fitwidth property of imageView with stagewidth property
            ChangeListener<Number> changeListener = new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    if (t1.doubleValue() < 900) {
                        imageView0.setFitWidth(t1.doubleValue() - 140);
                    }
                    if (t1.doubleValue() >= 900) {
                        imageView0.setFitWidth(800);
                    }
                }
            };
            changeListenerList.add(changeListener);
            Main.stage.widthProperty().addListener(changeListener);
            // Get image cap
            Text imagecap = new Text(descriptionImage.text());
            imagecap.getStyleClass().add("textimagecap");
            TextFlow textFlowTemp = new TextFlow();
            textFlowTemp.getChildren().add(imagecap);
            textFlowTemp.getStyleClass().add("textflowcenter");
            vbox.getChildren().addAll(imageView0, textFlowTemp);
        }

        // Display all content
        for (Element index : body) {
            TextFlow textFlow3 = new TextFlow();
            vbox.getChildren().add(textFlow3);
            // Eliminate details__morenews element
//            if (index.hasClass("details__morenews")) {
//                vbox.getChildren().remove(vbox.getChildren().size() - 1);
//                continue;
//            }
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
                textFlow3.setStyle("-fx-text-alignment: center; -fx-font-style: italic; -fx-font-size: 18;");
                continue;
            }
            // Add imageview
            if (index.select("img").hasAttr("data-src") || index.select("img").hasAttr("src")) {
                // Create new imageView
                ImageView imageView = new ImageView();
                imageView.setCache(true);
                imageView.setCacheHint(CacheHint.SPEED);
                if (index.select("img").hasAttr("data-src")) imageView.setImage(new Image(index.select("img").attr("data-src"), 600, 600, true, false, true));
                if (index.select("img").hasAttr("src")) imageView.setImage(new Image(index.select("img").attr("src"), 600, 600, true, false, true));
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
                // Add image cap
                if (index.hasText()) {
                    Text imagecap = new Text(index.text());
                    imagecap.getStyleClass().add("textimagecap");
                    textFlow3.getChildren().add(imagecap);
                    textFlow3.getStyleClass().add("textflowcenter");
                }
                vbox.getChildren().remove(vbox.getChildren().size() - 1);
                vbox.getChildren().addAll(imageView);
                vbox.getChildren().add(textFlow3);
                continue;
            }

            // Add Body ( hyper link + bold + text normal text)
            if (index.hasText()) {
                // Bold text
                if (index.select("strong").hasText()) {
                    String string = index.text().replaceAll(index.select("strong").text().replaceAll("\\*", ""), "<strong>" + index.select("strong").text().replaceAll("\\*", "") + "</strong>");
                    String[] stringSplit = string.split("<strong>");
                    if (!stringSplit[0].isEmpty()) {
                        Text textTemp = new Text(stringSplit[0]);
                        textTemp.getStyleClass().add("textnormal");
                        textFlow3.getChildren().add(textTemp);
                        textFlow3.getStyleClass().add("textflowjustify");
                    }
                    for (int i = 1; i < stringSplit.length; i++) {
                        Text textTemp0 = new Text(stringSplit[i].substring(0, stringSplit[i].indexOf("</strong>")));
                        textTemp0.getStyleClass().add("textbold");
                        Text textTemp1 = new Text(stringSplit[i].substring(stringSplit[i].indexOf("</strong>") + 9));
                        textTemp1.getStyleClass().add("textnormal");
                        textFlow3.getChildren().addAll(textTemp0, textTemp1);
                        textFlow3.getStyleClass().add("textflowjustify");
                    }
                    continue;
                }
                else  {
                    Text textTemp3 = new Text(index.text());
                    textTemp3.getStyleClass().add("textnormal");
                    textFlow3.getChildren().add(textTemp3);
                    textFlow3.getStyleClass().add("textflowjustify");
                }
                continue;
            }
            // If not adding anything then remove the last index element (the new TextFlow)
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
        }

        // Display author
        if (!article.getAuthor().isEmpty()) {
            TextFlow textFlow4 = new TextFlow();
            Text author1 = new Text(article.getAuthor());
            author1.getStyleClass().add("textauthor");
            textFlow4.getChildren().add(author1);
            textFlow4.getStyleClass().add("textflowright");
            vbox.getChildren().add(textFlow4);
        }

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

        fullArticlesUrl = null; // link to full article
        document = null;
        all = null;
        description = null;
        body = null;
        author = null;
        originalCategory = null;
        fullDate = null;
        descriptionImage = null;
    }

    /* FROM HERE IS SORT FUNCTION
     */
    // This function will sort the 5 article lists then return the sorted article (50 elements)
    public static ArrayList<Article> sortArticle(ArrayList<Article> list1, ArrayList<Article> list2, ArrayList<Article> list3, ArrayList<Article> list4, ArrayList<Article> list5) {
        ArrayList<Article> sortedArticles = new ArrayList<>();
        //sort each list
        Collections.sort(list1, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        Collections.sort(list2, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });Collections.sort(list3, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });Collections.sort(list4, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });Collections.sort(list5, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        //sort 5 list
        int a = 0;  //index của list1
        int b = 0;  //  index của list2
        int c = 0;  //          list3
        int d = 0;  //          list4
        int e = 0;  //          list5

        while (sortedArticles.size() < 50) {
            long date1 = 0;
            long date2 = 0;
            long date3 = 0;
            long date4 = 0;
            long date5 = 0;
            if (a < list1.size()) date1 = Long.parseLong(list1.get(a).getDate());
            if (b < list2.size()) date2 = Long.parseLong(list2.get(b).getDate());
            if (c < list3.size()) date3 = Long.parseLong(list3.get(c).getDate());
            if (d < list4.size()) date4 = Long.parseLong(list4.get(d).getDate());
            if (e < list5.size()) date5 = Long.parseLong(list5.get(e).getDate());

            //sort + chuyển vào arraylist theo unit time
            long maxUnitTime = maxNum(date1, date2, date3, date4, date5);
            if (date1 == maxUnitTime) {
                sortedArticles.add(list1.get(a));
                a++;
            } else if (date2 == maxUnitTime) {
                sortedArticles.add(list2.get(b));
                b++;
            } else if (date3 == maxUnitTime) {
                sortedArticles.add(list3.get(c));
                c++;
            } else if (date4 == maxUnitTime) {
                sortedArticles.add(list4.get(d));
                d++;
            } else if (date5 == maxUnitTime) {
                sortedArticles.add(list5.get(e));
                e++;
            }
        }
        return sortedArticles;
    }

    // This function will help the sort function: Find max number among 5 numbers
    public static long maxNum(long date1, long date2, long date3, long date4, long date5) {
        return Math.max(date1, Math.max(date2, Math.max(date3, Math.max(date4, date5))));
    }

    //
    public static ArrayList<Article> getSortedArticlesList(ArrayList<Article> list1, ArrayList<Article> list2, ArrayList<Article> list3, ArrayList<Article> list4, ArrayList<Article> list5) {
        ArrayList<Article> sortedArticles = new ArrayList<>();
        sortedArticles.addAll(list1);
        sortedArticles.addAll(list2);
        sortedArticles.addAll(list3);
        sortedArticles.addAll(list4);
        sortedArticles.addAll(list5);
        Collections.sort(sortedArticles, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                if (Long.parseLong(o1.getDate()) - Long.parseLong(o2.getDate()) == 0) return -1;
                return Long.parseLong(o1.getDate()) - Long.parseLong(o2.getDate()) > 0 ? -1 : 1;
            }
        });
        return sortedArticles;
    }

    /* FROM HERE IS PRINT FUNCTION
     */
    public static void printFullArticles(ArrayList<Article> vnexpressNewsList) {

    }

    public static void printSortArticles(ArrayList<Article> vnexpressNewsList) {
        int k = 0;
        for (Article i : vnexpressNewsList) {
            System.out.println(k);
            System.out.println("Date: " + i.getDate());
            System.out.println("fullDate: " + i.getFullDate());
            System.out.println("Time ago: " + i.getTimeAgo());
            System.out.println("Thumb link: " + i.getThumb());
            System.out.println("Title: " + i.getTitle());
            System.out.println("Link to full article: " + i.getLinkToFullArticles());
            System.out.println("Category: " + i.getCategory());
            System.out.println("Original category: " + i.getOriginalCategory());
            System.out.println("Descripton: " + i.getDescription());
            System.out.println("Soure: " + i.getSource());
            System.out.println("Author: " + i.getAuthor());
            System.out.println("---------------------------------------");
            System.out.println();
            k++;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
