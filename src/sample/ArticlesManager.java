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
import java.util.regex.Pattern;

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
            String pattern = "[0-9]{10}";
            if (!Pattern.matches(pattern, getVnexpressWebList.get(i).getDate())) {
                getVnexpressWebList.remove(i);
                i--;
                continue;
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
        Elements titleFolder = document.select("div.title-folder");
        Elements audioContainer = document.select("div.audioContainter");

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
        if (titleFolder.hasText()) {
            Text text0 = new Text(titleFolder.text() + "\n" + article.getFullDate());
            text0.getStyleClass().add("textfulldate");
            TextFlow textFlow0 = new TextFlow(text0);
            textFlow0.getStyleClass().add("textflowcenter");
            vbox.getChildren().add(textFlow0);
        } else {
            Text text0 = new Text(article.getOriginalCategory() + "\n" + article.getFullDate());
            text0.getStyleClass().add("textfulldate");
            TextFlow textFlow0 = new TextFlow(text0);
            textFlow0.getStyleClass().add("textflowcenter");
            vbox.getChildren().add(textFlow0);
        }

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

        if (audioContainer.select("audio").hasAttr("src") || audioContainer.select("audio").hasAttr("data-src")) {
            Text text510 = new Text("Listen audio on this ");
            text510.getStyleClass().add("textReadTheOriginalPost");
            Hyperlink articleLink10 = new Hyperlink("link");
            articleLink10.getStyleClass().add("texthyperlink");
            articleLink10.setOnAction(e -> {
                HostServices services = Helper.getInstance().getHostServices();
                services.showDocument(article.getLinkToFullArticles());
            });
            Text text610 = new Text(".");
            text610.getStyleClass().add("textReadTheOriginalPost");
            TextFlow textFlow30 = new TextFlow();
            textFlow30.getChildren().addAll(text510, articleLink10, text610);
            textFlow30.getStyleClass().add("textflowcenteritalic");
            vbox.getChildren().add(textFlow30);
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
                text61.getStyleClass().add("textReadTheOriginalPost");
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
                try {
                    if (index.getElementsByTag("a").hasText()) {
                        textFlow3 = Helper.getHyperLink(index);
                        vbox.getChildren().remove(vbox.getChildren().size() - 1);
                        vbox.getChildren().add(textFlow3);
                        continue;
                    }
                } catch (Exception e) {

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
            if (index.attr("type").equals("insertembedcode") || index.attr("type").equals("videostream")) {
                Text text51 = new Text("Watch video on this ");
                text51.getStyleClass().add("textReadTheOriginalPost");
                Hyperlink articleLink1 = new Hyperlink("link");
                articleLink1.getStyleClass().add("texthyperlink");
                articleLink1.setOnAction(e -> {
                    HostServices services = Helper.getInstance().getHostServices();
                    services.showDocument(article.getLinkToFullArticles());
                });
                Text text61 = new Text(".");
                text61.getStyleClass().add("textReadTheOriginalPost");
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
                try {
                    if (index.select("a").hasAttr("href")) {
                        textFlow3 = Helper.getHyperLink(index);
                        vbox.getChildren().remove(vbox.getChildren().size() - 1);
                        vbox.getChildren().add(textFlow3);
                        continue;
                    }
                } catch (Exception e) {

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

    @Override
    public void start(Stage stage) throws Exception {

    }
}