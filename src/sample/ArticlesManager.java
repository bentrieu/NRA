package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ArticlesManager extends Application {
    /* FROM HERE WILL BE THE FUNCTION FOR ZINGNEWS.VN
       There will be 3 function
       Get RSS list
       Get search keyword list
       Display the full article  */
    // Zing web list: https://zingnews.vn/the-gioi.html
    public ArrayList<Article> getZingWebList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> zingNewsList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all;
//        if (category.equals("Sports")) {
             all = document.select("section#news-latest div.article-list article.article-item.type-text, section#news-latest div.article-list article.article-item.type-picture, section#news-latest div.article-list article.article-item.type-hasvideo");
//        }
//        else {
//            all = document.select("section#news-latest div.article-list article.article-item.type-text, section#news-latest div.article-list article.article-item.type-picture");
//        }
        Elements thumb = all.select("p.article-thumbnail img");
        Elements titleAndLink = all.select("p.article-title a[href]");
        Elements description = all.select("p.article-summary");
        Elements dateAndCategory = all.select("p.article-meta");

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
            String date = timeToUnixString3(dateAndCategory.get(i).select("span.date").text() + " " + dateAndCategory.get(i).select("span.time").text());
            zingNewsList.get(i).setDate(date);
            // Set time ago for each object
            zingNewsList.get(i).setTimeAgo(timeDiff(date));
            // Set thumb for each object
            zingNewsList.get(i).setThumb(thumb.get(i).attr("abs:data-src"));
            // Set description (summarise) for each object
            zingNewsList.get(i).setDescription(description.get(i).text());
            // Set link to full article for each object
            zingNewsList.get(i).setLinkToFullArticles(titleAndLink.get(i).attr("abs:href"));
        }

        return zingNewsList;
    }

    // Zing search
    public ArrayList<Article> getZingSearchList(String keyWord, String category) throws IOException {
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
            String date = timeToUnixString3(all.get(i).select("p.article-meta span.date").text() + " " + all.get(i).select("p.article-meta span.time").text());
            searchZingList.get(i).setDate(date);
            // Set time ago for each object
            searchZingList.get(i).setTimeAgo(timeDiff(date));
        }

        return searchZingList;
    }

    // Zing full article scrape and display
    public void displayZingFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().clear();

        // Setup jsoup for each article
        final String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements all = document.select("article[article-id]");
        Elements fullDate = all.select("ul.the-article-meta li.the-article-publish");
        Elements body = all.select("div.the-article-body").select("> p, p ~ h3, td.pic img[src], td.pCaption.caption, div#innerarticle");
        Elements author = document.select("div.the-article-credit p.author");
        Elements originalCategory = all.select("p.the-article-category");

        // Set fullDate for each object
        article.setFullDate(fullDate.text());

        // Set author for each object
        article.setAuthor(author.first().text());

        // Set original category
        article.setOriginalCategory(originalCategory.text());

        // Display category
        Text text = new Text(article.getCategory());
        text.getStyleClass().add("textcategory");
        TextFlow textFlow = new TextFlow(text);
        vbox.getChildren().add(textFlow);

        // Display image source
        Image imageSource = new Image("resource/zingnews_big.png");
        ImageView imageViewSource = new ImageView(imageSource);
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
            // Add imageview
            if (!index.attr("data-src").isEmpty() || !index.attr("src").isEmpty()) {
                ImageView imageView = new ImageView();
                // Create new imageView
                if (index.attr("data-src").isEmpty()) {
                    imageView.setImage(new Image(index.attr("abs:src")));
                }
                else imageView.setImage(new Image(index.attr("abs:data-src")));
                imageView.setPreserveRatio(true);
                // Set the initial fitwidth for imageview
                if (Main.stage.getWidth() < 900) {
                    imageView.setFitWidth(Main.stage.getWidth() - 140);
                    vbox.setMaxWidth(Main.stage.getWidth() - 140);
                    vbox.setMinWidth(Main.stage.getWidth() - 140);
                }
                if (Main.stage.getWidth() >= 900) {
                    imageView.setFitWidth(800);
                    vbox.setMaxWidth(800);
                    vbox.setMinWidth(800);
                }
                // Bind the fitwidth property of imageView with stagewidth property
                Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (t1.doubleValue() < 900) {
                            imageView.setFitWidth(t1.doubleValue() - 140);
                            vbox.setMaxWidth(t1.doubleValue() - 140);
                            vbox.setMinWidth(t1.doubleValue() - 140);
                        }
                        if (t1.doubleValue() >= 900) {
                            imageView.setFitWidth(800);
                            vbox.setMaxWidth(800);
                            vbox.setMinWidth(800);
                        }
                    }
                });
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
            getHostServices().showDocument(article.getLinkToFullArticles());
        });
        Text text6 = new Text(".");
        textFlow5.getChildren().addAll(text5, articleLink, text6);
        textFlow5.setStyle("-fx-font-style: italic; -fx-font-size: 18; -fx-alignment: left;");
        vbox.getChildren().add(textFlow5);
    }

    /* FROM HERE WILL BE THE FUNCTION FOR VNEXPRESS.COM
       There will be 4 function
       Get RSS list
       Get search keyword list
       Get list open from web
       Display the full article  */
    // Vnexpress rss
    public ArrayList<Article> getVnexpressList(String urlToShortArticle, String category) throws IOException {
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
            vnexpressNewsList.get(k).setDate(timeToUnixString2(all.get(i).select("pubdate").text()));
            // Set timeago for each object
            vnexpressNewsList.get(k).setTimeAgo(timeDiff(vnexpressNewsList.get(k).getDate()));
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
    public ArrayList<Article> getVnexpressSearchList(String keyWord, String category) throws IOException {
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
            searchVnexpressList.get(k).setTimeAgo(timeDiff(searchVnexpressList.get(k).getDate()));
        }

        return searchVnexpressList;
    }

    // Vnexpress web link: https://vnexpress.net/thoi-su/chinh-tri
    public ArrayList<Article> getVnexpressWebList(String webURL, String category) throws IOException {
        // Create new list to store data then return
        ArrayList<Article> getVnexpressWebList = new ArrayList<>();

        // Set up jsoup
        Document document = Jsoup.connect(webURL).get();
        Elements doc = document.select("article:not(.off-thumb):has([data-publishtime])");
        Elements all = document.select("article:not(.off-thumb)");
        Elements links = all.select("div.thumb-art a[href]");
        Elements titles = all.select("div.thumb-art a[title]");
//        Elements descriptions = document.getElementsByClass("description");
        Elements pictures = all.select("div.thumb-art img[itemprop]");

        //Add scraped items into the class
        for(int i = 0, k = 0; i < 12; i++, k++){
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
            getVnexpressWebList.get(i).setTimeAgo(timeDiff(getVnexpressWebList.get(i).getDate()));
        }

        return getVnexpressWebList;
    }

    // Vnexpres full article scrape and display
    public void displayVnexpressFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().clear();

        // Setup jsoup for each article
        final String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements description = document.select("div.container p.description");
        Elements fck_detail = document.select("div.container article.fck_detail p, figcaption[itemprop], img[data-src], source[data-src-image], p.author_mail"); //div.fig-picture == img[data-src], source[data-src-image]
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
        Image imageSource = new Image("resource/vnexpress_big.png");
        ImageView imageViewSource = new ImageView(imageSource);
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
            // Image cap
            if (index.select("figcaption").attr("itemprop").equals("description")) { // image_cap
                Text imagecap = new Text(index.select("p.Image").text());
                imagecap.getStyleClass().add("textimagecap");
                textFlow3.getChildren().add(imagecap);
                textFlow3.getStyleClass().add("textflowcenter");
                continue;
            }
            // Add image
            if (index.select("img").hasAttr("data-src") || index.select("source").hasAttr("data-src-image")) {
                ImageView imageView;
                if (index.select("img").hasAttr("data-src")) {
                    imageView = new ImageView(new Image(index.select("img").attr("data-src")));
                }
                else imageView = new ImageView(new Image(index.select("source").attr("data-src-image")));
                imageView.setPreserveRatio(true);
                // Set the initial fitwidth for imageview
                if (Main.stage.getWidth() < 900) {
                    imageView.setFitWidth(Main.stage.getWidth() - 140);
                    vbox.setMaxWidth(Main.stage.getWidth() - 140);
                    vbox.setMinWidth(Main.stage.getWidth() - 140);
                }
                if (Main.stage.getWidth() >= 900) {
                    imageView.setFitWidth(800);
                    vbox.setMaxWidth(800);
                    vbox.setMinWidth(800);
                }
                // Bind the fitwidth property of imageView with stagewidth property
                Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (t1.doubleValue() < 900) {
                            imageView.setFitWidth(t1.doubleValue() - 140);
                            vbox.setMaxWidth(t1.doubleValue() - 140);
                            vbox.setMinWidth(t1.doubleValue() - 140);
                        }
                        if (t1.doubleValue() >= 900) {
                            imageView.setFitWidth(800);
                            vbox.setMaxWidth(800);
                            vbox.setMinWidth(800);
                        }
                    }
                });
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
                    textFlow3 = getHyperLink(index);
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
            getHostServices().showDocument(article.getLinkToFullArticles());
        });
        Text text6 = new Text(".");
        textFlow5.getChildren().addAll(text5, articleLink, text6);
        textFlow5.setStyle("-fx-font-style: italic; -fx-font-size: 18; -fx-alignment: left;");
        vbox.getChildren().add(textFlow5);
    }

    /* FROM HERE WILL BE THE FUNCTION FOR TUOITRE.VN
       There will be 4 function
       Get RSS list
       Get search keyword list
       Get list open from web
       Display the full article  */
    // Tuoitre rss
    public ArrayList<Article> getTuoiTreList(String urlToShortArticle, String category) throws IOException {
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
            String dateTemp = timeToUnixString4(date.get(i).text());
            tuoiTreList.get(i).setDate(dateTemp);
            // Set time ago for each object
            tuoiTreList.get(i).setTimeAgo(timeDiff(dateTemp));
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
    public ArrayList<Article> getTuoiTreWebList(String urlToShortArticle, String category) throws IOException {
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
            String dateTemp = timeToUnixString4(date.get(i).text());
            tuoiTreWebList.get(i).setDate(dateTemp);
            // Set time ago for each object
            tuoiTreWebList.get(i).setTimeAgo(timeDiff(dateTemp));
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
    public ArrayList<Article> getTuoiTreSearchList(String keyWord, String category) throws IOException {
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
            searchTuoiTreList.get(i).setTimeAgo(timeDiff(date));
        }

        return searchTuoiTreList;
    }

    // Tuoitre full article scrape and display
    public void displayTuoiTreFullArticle(Article article, VBox vbox) throws IOException {
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
        Image imageSource = new Image("resource/tuoitre_big.png");
        ImageView imageViewSource = new ImageView(imageSource);
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
            // Add imageview
            if (index.select("img").hasAttr("data-original")) {
                // Create new imageView
                ImageView imageView = new ImageView(new Image(index.select("img").attr("data-original")));
                imageView.setPreserveRatio(true);
                // Set the initial fitwidth for imageview
                if (Main.stage.getWidth() < 900) {
                    imageView.setFitWidth(Main.stage.getWidth() - 140);
                    vbox.setMaxWidth(Main.stage.getWidth() - 140);
                    vbox.setMinWidth(Main.stage.getWidth() - 140);
                }
                if (Main.stage.getWidth() >= 900) {
                    imageView.setFitWidth(800);
                    vbox.setMaxWidth(800);
                    vbox.setMinWidth(800);
                }
                // Bind the fitwidth property of imageView with stagewidth property
                Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (t1.doubleValue() < 900) {
                            imageView.setFitWidth(t1.doubleValue() - 140);
                            vbox.setMaxWidth(t1.doubleValue() - 140);
                            vbox.setMinWidth(t1.doubleValue() - 140);
                        }
                        if (t1.doubleValue() >= 900) {
                            imageView.setFitWidth(800);
                            vbox.setMaxWidth(800);
                            vbox.setMinWidth(800);
                        }
                    }
                });
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
                    textFlow3 = getHyperLink(index);
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
            getHostServices().showDocument(article.getLinkToFullArticles());
        });
        Text text6 = new Text(".");
        textFlow5.getChildren().addAll(text5, articleLink, text6);
        textFlow5.setStyle("-fx-font-style: italic; -fx-font-size: 18; -fx-alignment: left;");
        vbox.getChildren().add(textFlow5);
    }

    /* FROM HERE WILL BE THE FUNCTION FOR THANHNIEN.VN
       There will be 4 function
       Get RSS list
       Get search keyword list
       Get list open from web
       Display the full article  */
    // Thanhnien rss
    public ArrayList<Article> getThanhNienList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> thanhNienList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("item");
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
            String dateTemp = timeToUnixString2(date.get(i).text());
            thanhNienList.get(i).setDate(dateTemp);
            // Set time ago for each object
            thanhNienList.get(i).setTimeAgo(timeDiff(dateTemp));
            // Set thumb for each object
            thanhNienList.get(i).setThumb(thumb.get(i).text().replaceFirst("400x300", "2048"));
            // Set link to full article for each object
            thanhNienList.get(i).setLinkToFullArticles(link.get(i).text());
        }

        return thanhNienList;
    }

    // Thanhnien web list (https://thanhnien.vn/the-gioi/)
    public ArrayList<Article> getThanhNienWebList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> thanhNienWebList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("article.story.story--primary, div.l-grid article.story");
        Elements thumb = all.select("img");
        Elements titleAndLink = all.select("a.story__title");
        Elements date = all.select("time[rel]");

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0; i < 15; i++) {
            // Create new article object then add the object into the ArrayList
            thanhNienWebList.add(new Article());
            // Set source
            thanhNienWebList.get(i).setSource("thanhnien");
            // Set category manually
            thanhNienWebList.get(i).setCategory(category);
            // Set title for each object
            thanhNienWebList.get(i).setTitle(titleAndLink.get(i).text());
            // Set date for each object
            String dateTemp = date.get(i).attr("rel").substring(0, 10);
            thanhNienWebList.get(i).setDate(dateTemp);
            // Set time ago for each object
            thanhNienWebList.get(i).setTimeAgo(timeDiffThanhNien(dateTemp));
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
            thanhNienWebList.get(i).setThumb(thumbTemp);
            // Set link to full article for each object
            thanhNienWebList.get(i).setLinkToFullArticles(titleAndLink.get(i).attr("abs:href"));
        }

        return thanhNienWebList;
    }

    // Thanhnien full article scrape and display
    public void displayThanhNienFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().clear();

        // Setup jsoup for each article
        String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements all = document.select("div.l-content div.pswp-content");
        Elements description = all.select("div.sapo");
        Elements body = all.select("div#abody").select("> div, h2, div.pswp-content__wrapimage img, div.pswp-content__caption div.imgcaption, table.video");
        Elements author = document.select("div.details__author div.left h4");
        Elements originalCategory = document.select("div.breadcrumbs span[itemprop] a[href] span");
        Elements fullDate = document.select("div.details__meta div.meta time");
        Elements descriptionImage = all.select("div#contentAvatar");

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
        Image imageSource = new Image("resource/thanhnien_big.png");
        ImageView imageViewSource = new ImageView(imageSource);
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
            TextFlow textFlow2 = getHyperLink(description.first());
            textFlow2.getStyleClass().add("textflowjustify");
            HBox descriptionHbox = new HBox();
            descriptionHbox.getStyleClass().add("descriptionHbox");
            descriptionHbox.getChildren().add(textFlow2);
            vbox.getChildren().add(descriptionHbox);
        } else {
            Text text2 = new Text();
            if (!description.first().ownText().isEmpty()) {
                text2.setText(description.first().ownText());
            } else text2.setText(description.text());
            text2.getStyleClass().add("textdescription");
            TextFlow textFlow2 = new TextFlow(text2);
            textFlow2.getStyleClass().add("textflowjustify");
            HBox descriptionHbox = new HBox();
            descriptionHbox.getStyleClass().add("descriptionHbox");
            descriptionHbox.getChildren().add(textFlow2);
            vbox.getChildren().add(descriptionHbox);
        }

        // Display image description
        if (descriptionImage.select("img").hasAttr("data-src") || descriptionImage.select("img").hasAttr("src")) {
            // Create new imageView
            ImageView imageView0 = new ImageView();
            if (descriptionImage.select("img").attr("data-src").isEmpty()) {
                imageView0 = new ImageView(new Image(descriptionImage.select("img").attr("abs:src")));
            }
            else {
                imageView0 = new ImageView(new Image(descriptionImage.select("img").attr("abs:data-src")));
            }
            imageView0.setPreserveRatio(true);
            // Set the initial fitwidth for imageview
            if (Main.stage.getWidth() < 900) {
                imageView0.setFitWidth(Main.stage.getWidth() - 140);
                vbox.setMaxWidth(Main.stage.getWidth() - 140);
                vbox.setMinWidth(Main.stage.getWidth() - 140);
            }
            if (Main.stage.getWidth() >= 900) {
                imageView0.setFitWidth(800);
                vbox.setMaxWidth(800);
                vbox.setMinWidth(800);
            }
            // Bind the fitwidth property of imageView with stagewidth property
            ImageView finalImageView = imageView0;
            Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    if (t1.doubleValue() < 900) {
                        finalImageView.setFitWidth(t1.doubleValue() - 140);
                        vbox.setMaxWidth(t1.doubleValue() - 140);
                        vbox.setMinWidth(t1.doubleValue() - 140);
                    }
                    if (t1.doubleValue() >= 900) {
                        finalImageView.setFitWidth(800);
                        vbox.setMaxWidth(800);
                        vbox.setMinWidth(800);
                    }
                }
            });
            // Get image cap
            Text imagecap = new Text(descriptionImage.select("div.imgcaption").text());
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
            if (index.hasClass("details__morenews")) {
                vbox.getChildren().remove(vbox.getChildren().size() - 1);
                continue;
            }
            // Add video open link caption
            if (index.hasClass("video")) {
                Text text51 = new Text("Watch video on this ");
                text51.getStyleClass().add("textReadTheOriginalPost");
                Hyperlink articleLink1 = new Hyperlink("link");
                articleLink1.getStyleClass().add("texthyperlink");
                articleLink1.setOnAction(e -> {
                    getHostServices().showDocument(article.getLinkToFullArticles());
                });
                Text text61 = new Text(".");
                textFlow3.getChildren().addAll(text51, articleLink1, text61);
                textFlow3.setStyle("-fx-text-alignment: center; -fx-font-style: italic; -fx-font-size: 18;");
                continue;
            }
            // Add imageview
            if (index.select("img").hasAttr("data-src")) {
                // Create new imageView
                ImageView imageView = new ImageView(new Image(index.select("img").attr("data-src")));
                imageView.setPreserveRatio(true);
                // Set the initial fitwidth for imageview
                if (Main.stage.getWidth() < 900) {
                    imageView.setFitWidth(Main.stage.getWidth() - 140);
                    vbox.setMaxWidth(Main.stage.getWidth() - 140);
                    vbox.setMinWidth(Main.stage.getWidth() - 140);
                }
                if (Main.stage.getWidth() >= 900) {
                    imageView.setFitWidth(800);
                    vbox.setMaxWidth(800);
                    vbox.setMinWidth(800);
                }
                // Bind the fitwidth property of imageView with stagewidth property
                Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (t1.doubleValue() < 900) {
                            imageView.setFitWidth(t1.doubleValue() - 140);
                            vbox.setMaxWidth(t1.doubleValue() - 140);
                            vbox.setMinWidth(t1.doubleValue() - 140);
                        }
                        if (t1.doubleValue() >= 900) {
                            imageView.setFitWidth(800);
                            vbox.setMaxWidth(800);
                            vbox.setMinWidth(800);
                        }
                    }
                });
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
            // Add Body ( hyper link + bold + text normal text)
            if (index.hasText()) {
                // Hyper link
                if (index.select("a").hasAttr("href")) {
                    textFlow3 = getHyperLink(index);
                    vbox.getChildren().remove(vbox.getChildren().size() - 1);
                    vbox.getChildren().add(textFlow3);
                    continue;
                }
                // Bold text
                if (index.select("h2").hasText()) {
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
            getHostServices().showDocument(article.getLinkToFullArticles());
        });
        Text text6 = new Text(".");
        textFlow5.getChildren().addAll(text5, articleLink, text6);
        textFlow5.setStyle("-fx-font-style: italic; -fx-font-size: 18; -fx-alignment: left;");
        vbox.getChildren().add(textFlow5);
    }

    /* FROM HERE WILL BE THE FUNCTION FOR NHANDAN.VN
       There will be 4 function
       Get RSS list
       Get search keyword list
       Get list open from web
       Display the full article  */
    // Nhandan web list (https://nhandan.vn/chinhtri)
    public ArrayList<Article> getNhanDanWebList(String urlToShortArticle, String category) throws IOException {
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
                dateTemp = timeToUnixString5(dateTemp);
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
            nhanDanWebList.get(i).setTimeAgo(timeDiff(dateTemp));
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
    public ArrayList<Article> getNhanDanSearchList(String keyword, String category) throws IOException {
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
                dateTemp = timeToUnixString5(dateTemp);
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
            nhanDanWebList.get(i).setTimeAgo(timeDiff(dateTemp));
            // Set thumb for each object
            String thumbTemp = all.get(k).select("div.box-img img").attr("abs:data-src");
            thumbTemp = thumbTemp.replaceFirst("resize/[^/]+/", "");
            nhanDanWebList.get(i).setThumb(thumbTemp);
            // Set link to full article for each object
            nhanDanWebList.get(i).setLinkToFullArticles(all.get(k).select("div.box-title a").attr("abs:href"));
        }

        return nhanDanWebList;
    }

    // Thanhnien full article scrape and display
    public void displayNhanDanFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().clear();

        // Setup jsoup for each article
        String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements all = document.select("div.box-content-detail");
        Elements description = all.select("div.box-des-detail");
        Elements body = all.select("div.detail-content-body ").select("> p, figure");
        Elements author = all.select("div.box-author");
        Elements originalCategory = document.select("div.uk-breadcrumb li");
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
        Image imageSource = new Image("resource/nhandan_big.png");
        ImageView imageViewSource = new ImageView(imageSource);
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
            if (descriptionImage.select("img").attr("data-src").isEmpty()) {
                imageView0 = new ImageView(new Image(descriptionImage.select("img").attr("abs:src")));
            }
            else {
                imageView0 = new ImageView(new Image(descriptionImage.select("img").attr("abs:data-src")));
            }
            imageView0.setPreserveRatio(true);
            // Set the initial fitwidth for imageview
            if (Main.stage.getWidth() < 900) {
                imageView0.setFitWidth(Main.stage.getWidth() - 140);
                vbox.setMaxWidth(Main.stage.getWidth() - 140);
                vbox.setMinWidth(Main.stage.getWidth() - 140);
            }
            if (Main.stage.getWidth() >= 900) {
                imageView0.setFitWidth(800);
                vbox.setMaxWidth(800);
                vbox.setMinWidth(800);
            }
            // Bind the fitwidth property of imageView with stagewidth property
            ImageView finalImageView = imageView0;
            Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    if (t1.doubleValue() < 900) {
                        finalImageView.setFitWidth(t1.doubleValue() - 140);
                        vbox.setMaxWidth(t1.doubleValue() - 140);
                        vbox.setMinWidth(t1.doubleValue() - 140);
                    }
                    if (t1.doubleValue() >= 900) {
                        finalImageView.setFitWidth(800);
                        vbox.setMaxWidth(800);
                        vbox.setMinWidth(800);
                    }
                }
            });
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
//            if (index.hasClass("video")) {
//                Text text51 = new Text("Watch video on this ");
//                text51.getStyleClass().add("textReadTheOriginalPost");
//                Hyperlink articleLink1 = new Hyperlink("link");
//                articleLink1.getStyleClass().add("texthyperlink");
//                articleLink1.setOnAction(e -> {
//                    getHostServices().showDocument(article.getLinkToFullArticles());
//                });
//                Text text61 = new Text(".");
//                textFlow3.getChildren().addAll(text51, articleLink1, text61);
//                textFlow3.setStyle("-fx-text-alignment: center; -fx-font-style: italic; -fx-font-size: 18;");
//                continue;
//            }
            // Add imageview
            if (index.select("img").hasAttr("data-src") || index.select("img").hasAttr("src")) {
                // Create new imageView
                ImageView imageView = new ImageView();
                if (index.select("img").hasAttr("data-src")) imageView.setImage(new Image(index.select("img").attr("data-src")));
                if (index.select("img").hasAttr("src")) imageView.setImage(new Image(index.select("img").attr("src")));
                imageView.setPreserveRatio(true);
                // Set the initial fitwidth for imageview
                if (Main.stage.getWidth() < 900) {
                    imageView.setFitWidth(Main.stage.getWidth() - 140);
                    vbox.setMaxWidth(Main.stage.getWidth() - 140);
                    vbox.setMinWidth(Main.stage.getWidth() - 140);
                }
                if (Main.stage.getWidth() >= 900) {
                    imageView.setFitWidth(800);
                    vbox.setMaxWidth(800);
                    vbox.setMinWidth(800);
                }
                // Bind the fitwidth property of imageView with stagewidth property
                Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (t1.doubleValue() < 900) {
                            imageView.setFitWidth(t1.doubleValue() - 140);
                            vbox.setMaxWidth(t1.doubleValue() - 140);
                            vbox.setMinWidth(t1.doubleValue() - 140);
                        }
                        if (t1.doubleValue() >= 900) {
                            imageView.setFitWidth(800);
                            vbox.setMaxWidth(800);
                            vbox.setMinWidth(800);
                        }
                    }
                });
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
            getHostServices().showDocument(article.getLinkToFullArticles());
        });
        Text text6 = new Text(".");
        textFlow5.getChildren().addAll(text5, articleLink, text6);
        textFlow5.setStyle("-fx-font-style: italic; -fx-font-size: 18; -fx-alignment: left;");
        vbox.getChildren().add(textFlow5);
    }

    /* FROM HERE IS THE HELPER FUNCTION
       */
    // This function will get hyperlink
    public TextFlow getHyperLink(Element index) {
        TextFlow textFlow3 = new TextFlow();
        // Get all the text
        String string = index.text();
        // Add <hyper> before any text with a hyperlink (type: <Hyper>link<Text>)
        for (int i = 0; i < index.select("a[href]").size(); i++) {
            string = replacFirstFromIndex(index.select("a[href]").get(i).text(), "<Hyper>" + index.select("a[href]").get(i).attr("href") + "</" + index.select("a[href]").get(i).text() + "+>", string, i <= 0 ? 0 : string.indexOf("+>") + 2);
        }
        // Split string into sub string, delimiter <Hyper>
        String[] stringSplit = string.split("<Hyper>");
        // Add the first text (not include hyperlink into the textflow)
        Text textTemp0 = new Text(stringSplit[0]);
        textTemp0.getStyleClass().add("textnormal");
        textFlow3.getChildren().add(textTemp0);
        // Add the hyperlink + text of hyperlink
        for (int i = 1; i < stringSplit.length; i++) {
            // Set the hyperlink text
            Hyperlink hyperlink = new Hyperlink(stringSplit[i].substring(stringSplit[i].indexOf("</") + 2, stringSplit[i].indexOf("+>")));
            hyperlink.getStyleClass().add("texthyperlink");
            int finalI = i; // create new final variables to set action of lamb (required final variable)
            // Add link into the hyperlink
            hyperlink.setOnAction(e -> {
                getHostServices().showDocument(stringSplit[finalI].substring(0, stringSplit[finalI].indexOf("</")));
            });
            // Add the hyperlink into the flow text
            textFlow3.getChildren().add(hyperlink);
            // Add the next text behind hyperlink text into flow text ( <Hyper>link<Text>next text )
            Text textTemp1 = new Text(stringSplit[i].substring(stringSplit[i].indexOf("+>") + 2));
            textTemp1.getStyleClass().add("textnormal");
            textFlow3.getChildren().add(textTemp1);
        }
        textFlow3.getStyleClass().add("textflowjustify");
        return textFlow3;
    }

    // This function help to get hyperlink + bold text
    String replacFirstFromIndex(String regex, String replacement, String currentString, int fromIndex) {
        String temp = currentString.substring(fromIndex);
        temp = temp.replaceFirst(regex, replacement);
        String temp2 = currentString.substring(0, fromIndex);
        currentString = temp2 + temp;
        return currentString;
    }

    // This function will get in unix time in string then return time ago (time different) in string
    public static String timeDiff(String date) {
        StringBuilder res = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date startDate = sdf.parse(unixToTime(date));
            Date endDate = new Date();
            long difference_In_Time = endDate.getTime() - startDate.getTime();

            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            long difference_In_Months = (difference_In_Time / (1000L * 60 * 60 * 24 * 30));

            if (difference_In_Years != 0) {
                if (difference_In_Years != 1) {
                    res.append(difference_In_Years + " years ago");
                } else {
                    res.append(difference_In_Years + " year ago");
                }
            } else if (difference_In_Months != 0) {
                if (difference_In_Months != 1) {
                    res.append(difference_In_Months + " months ago");
                } else {
                    res.append(difference_In_Months + " month ago");
                }
            } else if (difference_In_Days != 0) {
                if (difference_In_Days != 1) {
                    res.append(difference_In_Days + " days ago");
                } else {
                    res.append(difference_In_Days + " day ago");
                }
            } else if (difference_In_Hours != 0) {
                if (difference_In_Hours != 1) {
                    res.append(difference_In_Hours + " hours ago");
                } else {
                    res.append(difference_In_Hours + " hour ago");
                }
            } else if (difference_In_Minutes != 0) {
                if (difference_In_Minutes != 1) {
                    res.append(difference_In_Minutes + " minutes ago");
                } else {
                    res.append(difference_In_Minutes + " minute ago");
                }
            } else if (difference_In_Seconds != 0) {
                if (difference_In_Seconds != 1) {
                    res.append(difference_In_Seconds + " seconds ago");
                } else {
                    res.append(difference_In_Seconds + " second ago");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res.toString();
    }
    public static String timeDiffThanhNien(String date)  {
        StringBuilder res = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date startDate = sdf.parse(unixToTime(date));
            Date endDate = new Date();
            long difference_In_Time = endDate.getTime() - (startDate.getTime() - 25200000);

            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            long difference_In_Months = (difference_In_Time / (1000L * 60 * 60 * 24 * 30));

            if(difference_In_Years != 0){
                if(difference_In_Years != 1){
                    res.append(difference_In_Years + " years ago");
                } else {
                    res.append(difference_In_Years + " year ago");
                }
            } else if(difference_In_Months != 0) {
                if(difference_In_Months != 1){
                    res.append(difference_In_Months + " months ago");
                } else {
                    res.append(difference_In_Months + " month ago");
                }
            } else if(difference_In_Days != 0){
                if(difference_In_Days != 1){
                    res.append(difference_In_Days + " days ago");
                } else {
                    res.append(difference_In_Days + " day ago");
                }
            } else if(difference_In_Hours != 0){
                if(difference_In_Hours != 1){
                    res.append(difference_In_Hours + " hours ago");
                } else {
                    res.append(difference_In_Hours + " hour ago");
                }
            } else if(difference_In_Minutes != 0){
                if(difference_In_Minutes != 1){
                    res.append(difference_In_Minutes + " minutes ago");
                } else {
                    res.append(difference_In_Minutes + " minute ago");
                }
            } else if(difference_In_Seconds != 0){
                if(difference_In_Seconds != 1){
                    res.append(difference_In_Seconds + " seconds ago");
                } else {
                    res.append(difference_In_Seconds + " second ago");
                }
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return res.toString();
    }

    // This function will convert unix time to readable date (dd/mm/yyyy hh:mm:ss)
    public static String unixToTime(String unixTime) {
        ZonedDateTime dateTime = Instant.ofEpochMilli(Long.parseLong(unixTime)*1000).atZone(ZoneId.of("GMT+7"));
        String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return formatted;
    }

    // This function will convert from readable date (dd/mm/yyyy hh:mm:ss) to unix time String
    public static String timeToUnixString(String time){
        long unixTime = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = sdf.parse(time);
            unixTime = date.getTime() / 1000;

        } catch (ParseException e){
            e.printStackTrace();
        }
        return String.valueOf(unixTime);
    }

    // This function will convert from readable date (E, dd MMM yyyy HH:mm:ss Z) to unix time String (Sun, 15 Aug 2021 19:00:00 +0700)
    public static String timeToUnixString2(String time){
        long unixTime = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
            Date date = sdf.parse(time);
            unixTime = date.getTime() / 1000;

        } catch (ParseException e){
            e.printStackTrace();
        }
        return String.valueOf(unixTime);
    }

    // This function will convert from readable date (dd/MM/yyyy HH:mm) to unix time String (15/8/2021 20:02)
    public static String timeToUnixString3(String time){
        long unixTime = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = sdf.parse(time);
            unixTime = date.getTime() / 1000;

        } catch (ParseException e){
            e.printStackTrace();
        }
        return String.valueOf(unixTime);
    }

    // This function will convert from readable date (E, dd MMM yyyy HH:mm:ss Z) to unix time String (Sun, 15 Aug 2021 19:53:00 GMT+7)
    public static String timeToUnixString4(String time){
        long unixTime = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
            Date date = sdf.parse(time);
            unixTime = date.getTime() / 1000;

        } catch (ParseException e){
            e.printStackTrace();
        }
        return String.valueOf(unixTime);
    }

    // // This function will convert from readable date (HH:mm dd/MM/yyyy) to unix time String (20:02 15/8/2021)
    public static String timeToUnixString5(String time){
        long unixTime = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            Date date = sdf.parse(time);
            unixTime = date.getTime() / 1000;

        } catch (ParseException e){
            e.printStackTrace();
        }
        return String.valueOf(unixTime);
    }

    // This function will convert from readable date (dd/mm/yyyy hh:mm:ss) to unix time long integer
    public static long timeToUnixLong(String time){
        long unixTime = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = sdf.parse(time);
            unixTime = date.getTime() / 1000;

        } catch (ParseException e){
            e.printStackTrace();
        }
        return unixTime;
    }

    // This 2 function to print out article
    public void printFullArticles(ArrayList<Article> vnexpressNewsList) {

    }

    public void printSortArticles(ArrayList<Article> vnexpressNewsList) {
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

//    public void displayVnexpressFullArticle(Article article, VBox vbox) throws IOException {
//        // Clear vbox
//        vbox.getChildren().remove(2, vbox.getChildren().size());
//
//        // Setup jsoup for each article
//        final String fullArticlesUrl = "https://vnexpress.net/lam-sao-de-71-1-71-1-71-dung-4338514.html" /*article.getLinkToFullArticles()*/; // link to full article
//        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
//        Elements all = document.select("div.container div.sidebar-1");
//        Elements description = all.select("p.description");
//        Elements fck_detail = all.select("article.fck_detail").select("p.Normal, p.Image, div.fig-picture, p.author_mail");
//        Elements time = all.select("span.date");
//
//        // Set fullDate for each object
//        article.setFullDate(time.text());
//
//        // CSS
//
//        // Display category
//        TextFlow textFlow = new TextFlow(new Text(article.getCategory()));
//        vbox.getChildren().add(textFlow);
//        textFlow.setStyle("-fx-font-size: 20; -fx-text-alignment: left; -fx-text-fill: grey;");
//
//        // Display fullDate
//        TextFlow textFlow0 = new TextFlow(new Text(article.getSource() + " * " + article.getFullDate()));
//        vbox.getChildren().add(textFlow0);
//        textFlow0.setStyle("-fx-font-size: 14; -fx-text-alignment: right; -fx-text-fill: grey;");
//
//        // Display title
//        TextFlow textFlow1 = new TextFlow(new Text(article.getTitle()));
//        vbox.getChildren().add(textFlow1);
//        textFlow1.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-alignment: justify;");
//
//        // Display description
//        if (description.select("span.location-stamp").hasText()) {
//            TextFlow textFlow2 = new TextFlow(new Text(description.select("span.location-stamp").text() + " - " + description.text().replaceFirst(description.select("span.location-stamp").text(), "")));
//            vbox.getChildren().add(textFlow2);
//            textFlow2.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
//        }
//        else {
//            TextFlow textFlow2 = new TextFlow(new Text(description.text()));
//            vbox.getChildren().add(textFlow2);
//            textFlow2.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
//        }
//
//        // Display all content
//        for (Element index : fck_detail) {
//            TextFlow textFlow3 = new TextFlow();
//            vbox.getChildren().add(textFlow3);
            // Add image
//            if (index.select("img").hasAttr("data-src") || index.select("source").hasAttr("data-src-image")) {
//                ImageView imageView;
//                if (index.select("img").hasAttr("data-src")) {
//                    imageView = new ImageView(new Image(index.select("img").attr("data-src")));
//                }
//                else imageView = new ImageView(new Image(index.select("source").attr("data-src-image")));
//                imageView.setPreserveRatio(true);
//                // Set the initial fitwidth for imageview
//                if (Main.stage.getWidth() < 1000) {
//                    imageView.setFitWidth(Main.stage.getWidth() - 100);
//                }
//                if (1000 <= Main.stage.getWidth() && Main.stage.getWidth() < 1400) {
//                    imageView.setFitWidth(Main.stage.getWidth() - 450);
//                }
//                if (Main.stage.getWidth() >= 1400) {
//                    imageView.setFitWidth(Main.stage.getWidth() - 850);
//                }
//                // Bind the fitwidth property of imageView with stagewidth property
//                Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
//                        if (t1.doubleValue() < 1000) {
//                            vbox.setPadding(new Insets(150, 30, 150, 30));
//                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(100));
//                        }
//                        if (1000 <= t1.doubleValue() && t1.doubleValue() < 1400 ) {
//                            vbox.setPadding(new Insets(150, 200, 150, 200));
//                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(450));
//                        }
//                        if (t1.doubleValue() > 1400) {
//                            vbox.setPadding(new Insets(150, 400, 150, 400));
//                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(850));
//                        }
//                    }
//                });
//                vbox.getChildren().add(imageView);
//                continue;
//            }
//            // Add image cap
//            if (index.select("p.image").hasText()) {
//                textFlow3.getChildren().add(new Text(index.text()));
//                textFlow3.setStyle("-fx-font-size: 12; -fx-text-alignment: left;");
//                continue;
//            }
//            // Add body
//            if (index.select("p").hasClass("Normal")) {
//                splitStringOfEachElementThenAddToTextFlow(index, textFlow3);
//                continue;
//            }
//            // Add author
//            if (!index.select("p.Normal").attr("style").isEmpty()) {
//                if (index.select("a").hasAttr("href")) {
//                    splitStringOfEachElementThenAddToTextFlow(index, textFlow3);
//                }
//                else {
//                    textFlow3.getChildren().add(new Text(index.text()));
//                    textFlow3.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-alignment: right;");
//                }
//                continue;
//            }
//            // Add author in some special case
//            if (index.select("p.author_mail").hasText()) { // tác giả (có bài viết tác giả phải lấy kiểu này, không lấy bằng text thông thường được)
//                textFlow3.getChildren().add(new Text(index.text()));
//                textFlow3.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-alignment: right;");
//                continue;
//            }
//            // If not adding anything then remove the last index element (the new TextFlow)
//            vbox.getChildren().remove(vbox.getChildren().size() - 1);
//        }
//    }
//
//    // This function will split string into substrings (href, bold, italic), then add text with the following style into the textFlow
//    public void splitStringOfEachElementThenAddToTextFlow(Element index, TextFlow textFlow3) {
//        // Default style of textFlow
//        textFlow3.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
//        // Convert content of each element into string
//        String string = index.toString();
//        // Delete head: <p class="Normal and end: </p> of each element
//        string = string.replaceAll("<p class=\"Normal\">", "");
//        string = string.replaceAll("</p>", "");
//        // Create regex for matching 3 special case <a href="|<strong>|<em>
//        Pattern pattern = Pattern.compile("(<a href=\"|<strong>|<em>)");
//        Matcher matcher = pattern.matcher(string);
//        // While still find the special word => keep split string and add the appropriate text into the textflow
//        while (matcher.find()) {
//            // Get start match
//            int start = matcher.start();
//            // If the first word is not the special word. Then add to textFlow (default style)
//            if (start != 0) {
//                textFlow3.getChildren().add(new Text(string.substring(0, start)));
//            }
//            // Cut string, start from the last index of word was added to textFlow
//            string = string.substring(start);
//            // <a href=" case
//            if (string.charAt(0) == '<' && string.charAt(1) == 'a') {
//                int startText = string.indexOf(">") + 1; // get start index of text in hyperlink
//                int endText = string.indexOf("</a>") - 1; // get end index of text in hyperlink
//                int startLink = string.indexOf("href=\"") + 6; // get start index of link
//                int endLink = string.indexOf("\"", startLink) - 1; // get end index of link
//                Hyperlink hyperlink = new Hyperlink(string.substring(startText, endText + 1)); // add text to hyperlink
//                // Add link to hyper link
//                String finalString = string;
//                hyperlink.setOnAction(e -> {
//                    getHostServices().showDocument(finalString.substring(startLink, endLink + 1));
//                });
//                hyperlink.setStyle("-fx-padding: 0;");
//                // Add hyper link to textFlow
//                textFlow3.getChildren().add(hyperlink);
//                // Cut string, start from the last index of special case was added. Ex: <em>java</em> new text => then start from " new text"
//                string = string.substring(endText + 5);
//                // Update matcher
//                matcher = pattern.matcher(string);
//                continue;
//            }
//            // <strong> case
//            if (string.charAt(0) == '<' && string.charAt(1) == 's') {
//                int startText = string.indexOf("<strong>") + 8; // get start index of text
//                int endText = string.indexOf("</strong>") - 1; // get end index of text
//                // Create new text and set style for text
//                Text text = new Text(string.substring(startText, endText + 1));
//                text.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-alignment: justify;");
//                // Add text into textFlow
//                textFlow3.getChildren().add(text);
//                // Cut string, start from the last index of special case was added. Ex: <strong>java</strong> new text => then start from " new text"
//                string = string.substring(endText + 10);
//                matcher = pattern.matcher(string);
//                continue;
//            }
//            // <em> case ~ <strong> case
//            if (string.charAt(0) == '<' && string.charAt(1) == 'e') {
//                int startText = string.indexOf("<em>") + 4;
//                int endText = string.indexOf("</em>") - 1;
//                Text text = new Text(string.substring(startText, endText + 1));
//                text.setStyle("-fx-font-size: 18; -fx-font-style: italic; -fx-text-alignment: justify;");
//                textFlow3.getChildren().add(text);
//                string = string.substring(endText + 6);
//                matcher = pattern.matcher(string);
//                continue;
//            }
//        }
//        // When not find any match then add the last default text into the textFlow
//        if (!matcher.find() && !string.isEmpty()) {
//            textFlow3.getChildren().add(new Text(string));
//        }
//    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
