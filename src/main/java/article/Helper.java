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

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/** This class will have some static functions that do some subtasks of ArticlesManager's functions **/

public class Helper extends Application {

    /* This class will have many static functions
    that will do some subtask
    */

    private static Helper myInstance = new Helper();

    public static Helper getInstance() {
        return myInstance;
    }

    // This function will get hyperlink
    public static TextFlow getHyperLink(Element index) {
        TextFlow textFlow3 = new TextFlow();
        // Get all the text
        String string = index.text();
        string = string.replaceAll("[\\(\\)\\*\\~\\+\\^\\.\\$]+", "");
        // Add <hyper> before any text with a hyperlink (type: <Hyper>link<Text>)
        for (int i = 0; i < index.select("a[href]").size(); i++) {
            string = replaceFirstFromIndex(index.select("a[href]").get(i).text(), "<Hyper>" + index.select("a[href]").get(i).attr("href") + "</" + index.select("a[href]").get(i).text() + "+>", string, i <= 0 ? 0 : string.indexOf("+>") + 2);
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
            hyperlink.setMaxSize(800, 800);
            int finalI = i; // create new final variables to set action of lamb (required final variable)
            // Add link into the hyperlink
            hyperlink.setOnAction(e -> {
                HostServices services = Helper.getInstance().getHostServices();
                services.showDocument(stringSplit[finalI].substring(0, stringSplit[finalI].indexOf("</")));
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
    public static String replaceFirstFromIndex(String regex, String replacement, String currentString, int fromIndex) {
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

    @Override
    public void start(Stage stage) throws Exception {

    }
}
