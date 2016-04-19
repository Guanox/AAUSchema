package com.printz.guano.aauschema;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by guano on 07/03/16.
 */
public class HTMLParser {

    private static final String LOG_TAG = HTMLParser.class.getSimpleName();
    private String mHTMLContent;
    private ArrayList<SchemaDay> mSchemdaDays;

    public HTMLParser(String XMLContents) {
        this.mHTMLContent = XMLContents;
        this.mSchemdaDays = new ArrayList<>();
    }

    public ArrayList<SchemaDay> getSchemaDays() {
        return mSchemdaDays;
    }

    public boolean process() {
        DateTime currDate = getDate();

        try {
            Document document = Jsoup.parse(mHTMLContent);
            Elements elements = document.getElementsByClass("day");

            for (Element element : elements) {
                String dateString = element.getElementsByClass("date").text();

                DateTime schemaDate = getDateFromString(dateString);

                // we are only interested in today's date and onwards
                if (DateTimeComparator.getDateOnlyInstance().compare(schemaDate, currDate) < 0) {
                    continue;
                }

                SchemaDay day = new SchemaDay();

                day.set_weekday(element.getElementsByClass("dayname").text());
                day.set_date(dateString);

                Elements courses = element.getElementsByClass("event");
                for (Element course : courses) {
                    Course currCourse = new Course();

                    // selects fx <a href="/course/view.php?id=15658">[F16] Advanced Algorithms (DAT6, SW6, DE8, MI8)</a>
                    Element link = course.select("a").first();
                    String courseStr = link.text();

                    // trim string of unnecessary info
                    courseStr = courseStr.replaceAll(" [(].*", "");
                    courseStr = courseStr.replaceAll("^\\S+ ", "");

                    currCourse.set_name(courseStr);
                    currCourse.set_time(course.getElementsByClass("time").text());
                    currCourse.setLocation(course.getElementsByClass("location").text());
                    currCourse.set_note(course.getElementsByClass("note").text());
                    day.addCourse(currCourse);
                }

                mSchemdaDays.add(day);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private DateTime getDateFromString(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");

        return dateFormatter.parseDateTime(date);
    }

    @NonNull
    private DateTime getDate() {
        TimeZone timeZone = TimeZone.getTimeZone("CET");

        return new DateTime(DateTimeZone.forTimeZone(timeZone));
    }
}
