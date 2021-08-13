package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ArticlesManager extends Application {
    /* FROM HERE WILL BE THE FUNCTION FOR ZINGNEWS.VN
       There will be 3 function
       Get RSS list
       Get search keyword list
       Display the full article  */
    public ArrayList<Article> getZingList(String urlToShortArticle, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> zingNewsList = new ArrayList<>();

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = urlToShortArticle;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all;
        if (category.equals("Thể Thao")) {
             all = document.select("section#news-latest div.article-list article.article-item.type-text, section#news-latest div.article-list article.article-item.type-picture, section#news-latest div.article-list article.article-item.type-hasvideo");
        }
        else {
            all = document.select("section#news-latest div.article-list article.article-item.type-text, section#news-latest div.article-list article.article-item.type-picture");
        }
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
            zingNewsList.get(i).setDate(dateAndCategory.get(i).select("span.time").text() + " " + dateAndCategory.get(i).select("span.date").text());
            // Set thumb for each object
            zingNewsList.get(i).setThumb(thumb.get(i).attr("abs:data-src"));
            // Set description (summarise) for each object
            zingNewsList.get(i).setDescription(description.get(i).text());
            // Set link to full article for each object
            zingNewsList.get(i).setLinkToFullArticles(titleAndLink.get(i).attr("abs:href"));
        }

        return zingNewsList;
    }

    public ArrayList<Article> getSearchListZing(String keyWord, String category) throws IOException {
        // Create new arraylist of article for return
        ArrayList<Article> searchZingList = new ArrayList<>();

        // Convert keyWord into link that can scrape // https://zingnews.vn/dịch-covid-tim-kiem.html?content_type=0
        String convertedKeyWord = "https://zingnews.vn/" + keyWord.trim().replaceAll("\\s", "-").toLowerCase() + "-tim-kiem.html";

        /* Bắt đầu từ đây là add dữ liệu cho sort article
         */
        // Setup jsoup for scraping data
        final String url = convertedKeyWord;
        Document document = Jsoup.connect(url).userAgent("Mozilla").get();
        Elements all = document.select("div.article-list article.article-item.type-text");
        // There are no date and original category when searching with tuoitre

        // Add data to vnexpressNewsList (Title + date + thumb + link)
        for (int i = 0, k = 0; k < 15; i++, k++) {
            // Create new article object then add the object into the ArrayList
            searchZingList.add(new Article());
            // Set source
            searchZingList.get(i).setSource("tuoitre");
            // Set category manually
            searchZingList.get(i).setCategory(category);
            // Set title for each object
            searchZingList.get(i).setTitle(all.get(i).select("header p.article-title").text());
            // Set thumb for each object
            searchZingList.get(i).setThumb(all.get(i).select("p.article-thumbnail img").attr("abs:src"));
            // Set description for each object
            searchZingList.get(i).setDescription(all.get(i).select("p.article-summary").text());
            // Set link to full article for each object
            searchZingList.get(i).setLinkToFullArticles(all.get(i).select("header p.article-title a").attr("abs:href"));
            // Set date for each object
            searchZingList.get(i).setDate(all.get(i).select("p.article-meta span.date").text() + all.get(i).select("p.article-meta span.time").text());
        }

        return searchZingList;
    }

    public void displayZingFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().remove(2, vbox.getChildren().size());

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

        // CSS

        // Display category
        TextFlow textFlow = new TextFlow(new Text(article.getCategory()));
        vbox.getChildren().add(textFlow);
        textFlow.setStyle("-fx-font-size: 20; -fx-text-alignment: left; -fx-text-fill: grey;");

        // Display fullDate
        TextFlow textFlow0 = new TextFlow(new Text(article.getSource() + " * " + article.getFullDate()));
        vbox.getChildren().add(textFlow0);
        textFlow0.setStyle("-fx-font-size: 14; -fx-text-alignment: right; -fx-text-fill: grey;");

        // Display title
        TextFlow textFlow1 = new TextFlow(new Text(article.getTitle()));
        vbox.getChildren().add(textFlow1);
        textFlow1.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-alignment: justify;");

        // Display description
        TextFlow textFlow2 = new TextFlow(new Text(article.getDescription()));
        vbox.getChildren().add(textFlow2);
        textFlow2.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");

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
                if (Main.stage.getWidth() < 1000) {
                    imageView.setFitWidth(Main.stage.getWidth() - 100);
                }
                if (1000 <= Main.stage.getWidth() && Main.stage.getWidth() < 1400) {
                    imageView.setFitWidth(Main.stage.getWidth() - 400);
                }
                if (Main.stage.getWidth() >= 1400) {
                    imageView.setFitWidth(Main.stage.getWidth() - 850);
                }
                // Bind the fitwidth property of imageView with stagewidth property
                Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        System.out.println(imageView.getFitWidth());
                        System.out.println(Main.stage.getWidth());
                        System.out.println(vbox.getWidth());
                        if (t1.doubleValue() < 1000) {
                            vbox.setPadding(new Insets(150, 30, 150, 30));
                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(100));
                        }
                        if (1000 <= t1.doubleValue() && t1.doubleValue() < 1400 ) {
                            vbox.setPadding(new Insets(150, 200, 150, 200));
                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(400));
                        }
                        if (t1.doubleValue() > 1400) {
                            vbox.setPadding(new Insets(150, 400, 150, 400));
                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(850));
                        }
                        System.out.println();
                        System.out.println(imageView.getFitWidth());
                        System.out.println(Main.stage.getWidth());
                        System.out.println(vbox.getWidth());
                    }
                });
                vbox.getChildren().add(imageView);
                continue;
            }
            // Add image cap
            if (index.hasClass("pCaption") && index.hasText()) {
                textFlow3.getChildren().add(new Text(index.select("p").text()));
                textFlow3.setStyle("-fx-font-size: 12; -fx-text-alignment: left;");
                continue;
            }
            // Add Body ( h3 bold + text thuong)
            if (index.select("h3").hasText() && !index.parent().hasClass("z-corona-header")) {
                textFlow3.getChildren().add(new Text(index.text()));
                textFlow3.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-alignment: justify;");
                continue;
            }
            if (index.select("p").hasText() && !index.parent().parent().hasClass("inner-article")) {
                textFlow3.getChildren().add(new Text(index.text()));
                textFlow3.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
                continue;
            }
            // If not adding anything then remove the last index element (the new TextFlow)
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
        }

        // Display author
        TextFlow textFlow4 = new TextFlow();
        textFlow4.getChildren().add(new Text(article.getAuthor()));
        vbox.getChildren().add(textFlow4);
        textFlow4.setStyle("-fx-font-family: \"Arial\"; -fx-font-size: 22; -fx-font-weight: bold; -fx-text-alignment: right;");
    }

    /* FROM HERE WILL BE THE FUNCTION FOR VNEXPRESS.COM
       There will be 4 function
       Get RSS list
       Get search keyword list
       Get list open from web
       Display the full article  */
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
            vnexpressNewsList.get(k).setDate(all.get(i).select("pubdate").text());
            // Set link for object
            vnexpressNewsList.get(k).setLinkToFullArticles(all.get(i).select("link").text());
            // Set category
            vnexpressNewsList.get(k).setCategory(category);
            // Set source
            vnexpressNewsList.get(k).setSource("vnexpress");
        }

        return vnexpressNewsList;
    }

    public ArrayList<Article> getSearchListVnexpress(String keyWord, String category) throws IOException {
        ArrayList<Article> searchVnexpressList = new ArrayList<Article>();
        String var10000 = keyWord.trim();
        String convertedKeyWord = "https://timkiem.vnexpress.net/?q=" + var10000.replaceAll("\\s", "%20").toLowerCase();
        Document document = Jsoup.connect(convertedKeyWord).userAgent("Mozilla").get();
        Elements all = document.select("div.width_common.list-news-subfolder article[data-url]");
        Elements thumbAndTitleAndLink = all.select("div.thumb-art");
        int i = 0;

        for(int k = 0; k < 15; ++k) {
            searchVnexpressList.add(new Article());
            ((Article)searchVnexpressList.get(k)).setSource("vnexpress");
            ((Article)searchVnexpressList.get(k)).setCategory(category);
            ((Article)searchVnexpressList.get(k)).setTitle(((Element)thumbAndTitleAndLink.get(i)).select("a").attr("title"));
            if (((Element)thumbAndTitleAndLink.get(i)).select("picture img").hasAttr("data-src")) {
                ((Article)searchVnexpressList.get(k)).setThumb(((Element)thumbAndTitleAndLink.get(i)).select("picture img").attr("data-src"));
                ((Article)searchVnexpressList.get(k)).setDate(this.unixToTime(((Element)all.get(i)).attr("data-publishtime")));
                ((Article)searchVnexpressList.get(k)).setLinkToFullArticles(((Element)thumbAndTitleAndLink.get(i)).select("a").attr("href"));
            } else {
                searchVnexpressList.remove(k);
                --k;
            }
            ++i;
        }

        return searchVnexpressList;
    }

    public ArrayList<Article> getVnexpressWebList(String webURL, String category) throws IOException {
        ArrayList<Article> getVnexpressWebList = new ArrayList<>();
        Document document = Jsoup.connect(webURL).get();

        //Links + titles + description + pictures
        Elements links = document.getElementsByClass("thumb-art").select("a[href]");
        Elements titles = document.getElementsByClass("thumb-art").select("[title]");
//        Elements descriptions = document.getElementsByClass("description");
        Elements pictures = document.getElementsByClass("thumb-art").select("img[itemprop]");

        //Add scraped items into the class
        for(int i = 0; i < 15; i++){
            // Create new object
            getVnexpressWebList.add(new Article());
            // Set source
            getVnexpressWebList.get(i).setSource("vnexpress");
            // Set category
            getVnexpressWebList.get(i).setCategory(category);
            // Set title
            getVnexpressWebList.get(i).setTitle(titles.get(i).attr("title"));
            // Set link to full article
            getVnexpressWebList.get(i).setLinkToFullArticles(links.get(i).attr("abs:href"));
//            // Set description
//            getVnexpressWebList.get(i).setDescription(descriptions.get(i).text());
            // Set thumb
            String linkThumb;
            if(pictures.get(i).getElementsByTag("img").attr("abs:data-src").isEmpty()){
                linkThumb = pictures.get(i).getElementsByTag("img").attr("abs:src");
                getVnexpressWebList.get(i).setThumb(pictures.get(i).getElementsByTag("img").attr("abs:src"));
            } else {
                linkThumb = pictures.get(i).getElementsByTag("img").attr("abs:data-src");
                getVnexpressWebList.get(i).setThumb(pictures.get(i).getElementsByTag("img").attr("abs:data-src"));
            }
            // Set date
            getVnexpressWebList.get(i).setDate(unixToTime(linkThumb.substring(linkThumb.indexOf(".jpg") - 10, linkThumb.indexOf(".jpg"))));
        }

        return getVnexpressWebList;
    }

    public void displayVnexpressFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().remove(2, vbox.getChildren().size());

        // Setup jsoup for each article
        final String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document2 = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
        Elements description = document2.select("div.container p.description");
        Elements fck_detail = document2.select("div.container article.fck_detail p, figcaption[itemprop], img[data-src], source[data-src-image], p.author_mail"); //div.fig-picture == img[data-src], source[data-src-image]
        Elements time = document2.select("div.container span.date");

        // Set fullDate for each object
        article.setFullDate(time.text());

        // CSS

        // Display category
        TextFlow textFlow = new TextFlow(new Text(article.getCategory()));
        vbox.getChildren().add(textFlow);
        textFlow.setStyle("-fx-font-size: 20; -fx-text-alignment: left; -fx-text-fill: grey;");

        // Display fullDate
        TextFlow textFlow0 = new TextFlow(new Text(article.getSource() + " * " + article.getFullDate()));
        vbox.getChildren().add(textFlow0);
        textFlow0.setStyle("-fx-font-size: 14; -fx-text-alignment: right; -fx-text-fill: grey;");

        // Display title
        TextFlow textFlow1 = new TextFlow(new Text(article.getTitle()));
        vbox.getChildren().add(textFlow1);
        textFlow1.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-alignment: justify;");

        // Display description
        if (description.select("span.location-stamp").hasText()) {
            TextFlow textFlow2 = new TextFlow(new Text(description.select("span.location-stamp").text() + " - " + description.text().replaceFirst(description.select("span.location-stamp").text(), "")));
            vbox.getChildren().add(textFlow2);
            textFlow2.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
        }
        else {
            TextFlow textFlow2 = new TextFlow(new Text(description.text()));
            vbox.getChildren().add(textFlow2);
            textFlow2.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
        }

        // Display all content
        for (Element index : fck_detail) {
            TextFlow textFlow3 = new TextFlow();
            vbox.getChildren().add(textFlow3);
            // Image cap
            if (index.select("figcaption").attr("itemprop").equals("description")) { // image_cap
                textFlow3.getChildren().add(new Text(index.select("p.Image").text()));
                textFlow3.setStyle("-fx-font-size: 12; -fx-text-alignment: left;");
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
                if (Main.stage.getWidth() < 1000) {
                    imageView.setFitWidth(Main.stage.getWidth() - 100);
                }
                if (1000 <= Main.stage.getWidth() && Main.stage.getWidth() < 1400) {
                    imageView.setFitWidth(Main.stage.getWidth() - 450);
                }
                if (Main.stage.getWidth() >= 1400) {
                    imageView.setFitWidth(Main.stage.getWidth() - 850);
                }
                // Bind the fitwidth property of imageView with stagewidth property
                Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (t1.doubleValue() < 1000) {
                            vbox.setPadding(new Insets(150, 30, 150, 30));
                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(100));
                        }
                        if (1000 <= t1.doubleValue() && t1.doubleValue() < 1400 ) {
                            vbox.setPadding(new Insets(150, 200, 150, 200));
                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(450));
                        }
                        if (t1.doubleValue() > 1400) {
                            vbox.setPadding(new Insets(150, 400, 150, 400));
                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(850));
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
            //
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
                    textFlow3.getChildren().add(new Text(index.text()));
                    textFlow3.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-alignment: right;");
                    continue;
                }
                // Strong text
                if (index.getElementsByTag("strong").hasText()) {
                    if (index.hasClass("author_mail") || index.hasClass("author")) {
                        textFlow3.getChildren().add(new Text(index.text()));
                        textFlow3.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-alignment: right;");
                        continue;
                    }
                    else { // làm tương tự như hyper link
                        String string = index.text().replaceAll(index.select("strong").text(), "<strong>" + index.select("strong").text() + "</strong>");
                        String[] stringSplit = string.split("<strong>");
                        if (!stringSplit[0].isEmpty()) {
                            textFlow3.getChildren().add(new Text(stringSplit[0]));
                            textFlow3.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
                        }
                        for (int i = 1; i < stringSplit.length; i++) {
                            Text text = new Text(stringSplit[i].substring(0, stringSplit[i].indexOf("</strong>")));
                            text.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-alignment: justify;");
                            Text text1 = new Text(stringSplit[i].substring(stringSplit[i].indexOf("</strong>") + 9));
                            text1.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
                            textFlow3.getChildren().addAll(text, text1);
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
                    textFlow3.getChildren().add(new Text(index.text()));
                    textFlow3.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
                }
                continue;
            }
            // Add author in some special case
            if (index.select("p.author_mail").hasText()) { // tác giả (có bài viết tác giả phải lấy kiểu này, không lấy bằng text thông thường được)
                textFlow3.getChildren().add(new Text(index.text()));
                textFlow3.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-alignment: right;");
                continue;
            }
            // If not adding anything then remove the last index element (the new TextFlow)
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
        }
    }

    // Tuoitre
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
            tuoiTreList.get(i).setDate(date.get(i).text());
            // Set thumb and description for each object
            String string = thumbAndDescription.get(i).text();
            int startLink = string.indexOf("src=\"") + 5;
            int endLink = string.indexOf("\"", startLink) - 1;
            int startDescription = string.indexOf("</a>") + 4;
            tuoiTreList.get(i).setThumb(string.substring(startLink, endLink + 1));
            tuoiTreList.get(i).setDescription(string.substring(startDescription));
            // Set link to full article for each object
            tuoiTreList.get(i).setLinkToFullArticles(link.get(i).text());
        }

        return tuoiTreList;
    }

    public ArrayList<Article> getSearchListTuoiTre(String keyWord, String category) throws IOException {
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
            searchTuoiTreList.get(i).setThumb(linkThumb);
            // Set description for each object
            searchTuoiTreList.get(i).setDescription(all.get(i).select("div.name-news p.sapo").text());
            // Set link to full article for each object
            searchTuoiTreList.get(i).setLinkToFullArticles(all.get(i).select("a").first().attr("abs:href"));
            // Set date
            searchTuoiTreList.get(i).setDate(unixToTime(linkThumb.substring(linkThumb.indexOf("crop-") + 5, linkThumb.indexOf("crop-") + 16)));
        }

        return searchTuoiTreList;
    }

    public void displayTuoiTreFullArticle(Article article, VBox vbox) throws IOException {
        // Clear vbox
        vbox.getChildren().remove(2, vbox.getChildren().size());

        // Setup jsoup for each article
        String fullArticlesUrl = article.getLinkToFullArticles(); // link to full article
        Document document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
//        // Handle video check
//        if (document.select("div.VCSortableInPreviewMode").attr("type").equals("VideoStream")) {
//            System.out.println(1);
//            System.out.println(Main.tuoiTreSportsList);
//            Main.tuoiTreSportsList.remove(article);
//            System.out.println(Main.tuoiTreSportsList);
//            article = Main.tuoiTreSportsList.get(Controller.currentArticle);
//            fullArticlesUrl = Main.tuoiTreSportsList.get(Controller.currentArticle).getLinkToFullArticles(); // link to full article
//            document = Jsoup.connect(fullArticlesUrl).userAgent("Mozilla").get();
//        }
        Elements all = document.select("div.content-detail");
        Elements fullDate = document.select("div.date-time");
        Elements body = all.select("div.main-content-body div.content.fck").select("> p, div.VCSortableInPreviewMode");
        Elements author = all.select("div.author");

//        System.out.println(body.parents());

        // Set fullDate for each object
        if (fullDate.hasText()) {
            article.setFullDate(fullDate.first().text());
        }

        // Set author for each object
        if (author.hasText()) {
            article.setAuthor(author.first().text());
        }

        // CSS

        // Display category
        TextFlow textFlow = new TextFlow(new Text(article.getCategory()));
        vbox.getChildren().add(textFlow);
        textFlow.setStyle("-fx-font-size: 20; -fx-text-alignment: left; -fx-text-fill: grey;");

        // Display fullDate
        TextFlow textFlow0 = new TextFlow(new Text(article.getSource() + " * " + article.getFullDate()));
        vbox.getChildren().add(textFlow0);
        textFlow0.setStyle("-fx-font-size: 14; -fx-text-alignment: right; -fx-text-fill: grey;");

        // Display title
        TextFlow textFlow1 = new TextFlow(new Text(article.getTitle()));
        vbox.getChildren().add(textFlow1);
        textFlow1.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-alignment: justify;");

        // Display description
        TextFlow textFlow2 = new TextFlow(new Text(article.getDescription()));
        vbox.getChildren().add(textFlow2);
        textFlow2.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");

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
                if (Main.stage.getWidth() < 1000) {
                    imageView.setFitWidth(Main.stage.getWidth() - 100);
                }
                if (1000 <= Main.stage.getWidth() && Main.stage.getWidth() < 1400) {
                    imageView.setFitWidth(Main.stage.getWidth() - 450);
                }
                if (Main.stage.getWidth() >= 1400) {
                    imageView.setFitWidth(Main.stage.getWidth() - 850);
                }
                // Bind the fitwidth property of imageView with stagewidth property
                Main.stage.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (t1.doubleValue() < 1000) {
                            vbox.setPadding(new Insets(150, 30, 150, 30));
                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(100));
                        }
                        if (1000 <= t1.doubleValue() && t1.doubleValue() < 1400 ) {
                            vbox.setPadding(new Insets(150, 200, 150, 200));
                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(450));
                        }
                        if (t1.doubleValue() > 1400) {
                            vbox.setPadding(new Insets(150, 400, 150, 400));
                            imageView.fitWidthProperty().bind(Main.stage.widthProperty().subtract(850));
                        }
                    }
                });
                // Add image cap
                textFlow3.getChildren().add(new Text(index.select("div.PhotoCMS_Caption").text()));
                textFlow3.setStyle("-fx-font-size: 12; -fx-text-alignment: left;");
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
                        textFlow3.getChildren().add(new Text(stringSplit[0]));
                        textFlow3.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
                    }
                    for (int i = 1; i < stringSplit.length; i++) {
                        Text text = new Text(stringSplit[i].substring(0, stringSplit[i].indexOf("</strong>")));
                        text.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-alignment: justify;");
                        Text text1 = new Text(stringSplit[i].substring(stringSplit[i].indexOf("</strong>") + 9));
                        text1.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
                        textFlow3.getChildren().addAll(text, text1);
                    }
                    continue;
                }
                else  {
                    textFlow3.getChildren().add(new Text(index.text()));
                    textFlow3.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
                }
//                else {
//
//                }
                continue;
            }
            // If not adding anything then remove the last index element (the new TextFlow)
            vbox.getChildren().remove(vbox.getChildren().size() - 1);
        }

        // Display author
        TextFlow textFlow4 = new TextFlow();
        textFlow4.getChildren().add(new Text(article.getAuthor()));
        vbox.getChildren().add(textFlow4);
        textFlow4.setStyle("-fx-font-family: \"Arial\"; -fx-font-size: 22; -fx-font-weight: bold; -fx-text-alignment: right;");
    }

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
        textFlow3.getChildren().add(new Text(stringSplit[0]));
        // Add the hyperlink + text of hyperlink
        for (int i = 1; i < stringSplit.length; i++) {
            // Set the hyperlink text
            Hyperlink hyperlink = new Hyperlink(stringSplit[i].substring(stringSplit[i].indexOf("</") + 2, stringSplit[i].indexOf("+>")));
            int finalI = i; // create new final variables to set action of lamb (required final variable)
            // Add link into the hyperlink
            hyperlink.setOnAction(e -> {
                getHostServices().showDocument(stringSplit[finalI].substring(0, stringSplit[finalI].indexOf("</")));
            });
            hyperlink.setStyle("-fx-padding: 0; -fx-font-size: 18;");
            // Add the hyperlink into the flow text
            textFlow3.getChildren().add(hyperlink);
            // Add the next text behind hyperlink text into flow text ( <Hyper>link<Text>next text )
            textFlow3.getChildren().add(new Text(stringSplit[i].substring(stringSplit[i].indexOf("+>") + 2)));
        }
        textFlow3.setStyle("-fx-font-size: 18; -fx-text-alignment: justify;");
        return textFlow3;
    }

    // This method help to get hyperlink + bold text
    String replacFirstFromIndex(String regex, String replacement, String currentString, int fromIndex) {
        String temp = currentString.substring(fromIndex);
        temp = temp.replaceFirst(regex, replacement);
        String temp2 = currentString.substring(0, fromIndex);
        currentString = temp2 + temp;
        return currentString;
    }

    // This function will convert unix time to readable date
    public String unixToTime(String unixTime) {
        ZonedDateTime dateTime = Instant.ofEpochMilli(Long.parseLong(unixTime)*1000).atZone(ZoneId.of("GMT+7"));
        String formatted = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return formatted;
    }

    // This 2 function to print out article
    public void printFullArticle(ArrayList<Article> vnexpressNewsList, int i) {

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
