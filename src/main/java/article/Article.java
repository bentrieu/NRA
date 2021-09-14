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
