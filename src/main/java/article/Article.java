/*
    RMIT University Vietnam
    Course: INTE2512 Object-Oriented Programming
    Semester: 2021B
    Assessment: Final Project
    Created  date: 15/07/2021
    Author: Ngo Ngoc Thinh - s3879364, Trieu Hoang Khang - s3878466, Nguyen Van Quy - s3878636, Nguyen Tran Duy Khang - s3836280
    Last modified date: 19/9/2021
    Acknowledgement:
    [1] J. Hedley, "jsoup: Java HTML parser, built for HTML editing, cleaning, scraping, and XSS safety", Jsoup.org, 2021. [Online]. Available: https://jsoup.org/. [Accessed: 11- Sep- 2021].
    [2] "VnExpress - Báo tiếng Việt nhiều người xem nhất", vnexpress.net, 2021. [Online]. Available: https://vnexpress.net/. [Accessed: 11- Sep- 2021].
    [3] Zingnews.vn, 2021. [Online]. Available: https://zingnews.vn/. [Accessed: 11- Sep- 2021].
    [4] "Tin tức 24h mới nhất, tin nhanh, tin nóng hàng ngày", Thanh Niên, 2021. [Online]. Available: https://thanhnien.vn/. [Accessed: 11- Sep- 2021].
    [5] "Báo Nhân Dân điện tử", Báo Nhân Dân, 2021. [Online]. Available: https://nhandan.vn/. [Accessed: 11- Sep- 2021].
    [6] T. ONLINE, "Tin tức, tin nóng, đọc báo điện tử - Tuổi Trẻ Online", TUOI TRE ONLINE, 2021. [Online]. Available: https://tuoitre.vn/. [Accessed: 11- Sep- 2021].
    [7] "Download OpenJDK builds of Liberica JDK, Java 8, 11, Java 16 Linux, Windows, macOS", BellSoft LTD, 2021. [Online]. Available: https://bell-sw.com/pages/downloads/. [Accessed: 11- Sep- 2021].
    [8] Tutorials.jenkov.com,2021.[Online].Available:http://tutorials.jenkov.com/javafx/index.html?fbclid=IwAR00PfMH6tVx6fBOUIzHP3LbNIwAeW-o-DlaxO9zMyD8YijM_Z3i2XqtYIY. [Accessed: 12- Sep- 2021].
    [9] J. Redmond, "Maven – Maven Documentation", Maven.apache.org, 2021. [Online]. Available: https://maven.apache.org/guides/?fbclid=IwAR2J6wZokvEVKj-RqRTw8qH3xJN1kr5TQY_WqnfzBsv4JoCXN1bOqEX6Bzw. [Accessed: 12- Sep- 2021].
    [10] All lecture and lab slides from RMIT univeristy: https://rmit.instructure.com/courses/88207
*/

package article;

public class Article {
    private String date;
    private String fullDate;
    private String timeAgo;
    private String thumb;
    private String title;
    private String linkToFullArticles;
    private String category;
    private String originalCategory;
    private String description;
    private String source;
    private String author;

    Article() {
        this.date = "";
        this.fullDate = "";
        this.timeAgo = "";
        this.thumb = "";
        this.title = "";
        this.linkToFullArticles = "";
        this.category = "";
        this.originalCategory = "";
        this.description = "";
        this.source = "";
        this.author = "";
    }

    // Setter
    public void setDate(String date) {
        this.date = date;
    }
    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }
    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setLinkToFullArticles(String fullArticles) {
        this.linkToFullArticles = fullArticles;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setOriginalCategory(String originalCategory) {
        this.originalCategory = originalCategory;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    // Getter
    public String getDate() {
        return this.date;
    }
    public String getFullDate() {
        return this.fullDate;
    }
    public String getTimeAgo() {
        return timeAgo;
    }
    public String getThumb() {
        return this.thumb;
    }
    public String getTitle() {
        return this.title;
    }
    public String getLinkToFullArticles() {
        return this.linkToFullArticles;
    }
    public String getCategory() {
        return category;
    }
    public String getOriginalCategory() {
        return originalCategory;
    }
    public String getDescription() {
        return description;
    }
    public String getSource() {
        return source;
    }
    public String getAuthor() {
        return author;
    }

}
