package com.printz.guano.aauschema;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by guano on 07/03/16.
 */
public class HTMLParser {

    private String _HTMLContent;
    private ArrayList<SchemaDay> _schemaDays;
    private String TAG = HTMLParser.class.getSimpleName();
    private boolean isToday;

    public HTMLParser(String XMLContents) {
        this._HTMLContent = XMLContents;
        this._schemaDays = new ArrayList<>();
    }

    public ArrayList<SchemaDay> getSchemaDays() {
        return _schemaDays;
    }

    public boolean process() {
        String currDate = getDate();

        try {
            Document document = Jsoup.parse(_HTMLContent);
            Elements elements = document.getElementsByClass("day");

            for (Element element : elements) {
                String dateString = element.getElementsByClass("date").text();

                // we are not interested in displaying earlier dates than today
                if (dateString.compareTo(currDate) < 0 ) {
                    continue;
                }

                SchemaDay day = new SchemaDay();

                setDayDate(day, currDate, element, dateString);

                Elements courses = element.getElementsByClass("event");
                for (Element course : courses) {
                    Course currCourse = new Course();

                    // selects fx <a href="/course/view.php?id=15658">[F16] Advanced Algorithms (DAT6, SW6, DE8, MI8)</a>
                    Element link = course.select("a").first();
                    String courseStr = link.text();

//                    if (courseStr.toLowerCase().contains("theory")) {
//                        currCourse.setType(CourseType.THEORY_OF_SCIENCE);
//                        currCourse.set_name("VIT");
//                    } else if (courseStr.toLowerCase().contains("software")) {
//                        currCourse.setType(CourseType.SOFTWARE);
//                        currCourse.set_name("SW6");
//                    } else if (courseStr.toLowerCase().contains("database")) {
//                        currCourse.setType(CourseType.DATABASES);
//                        currCourse.set_name("DBS");
//                    } else if (courseStr.toLowerCase().contains("algorithms")) {
//                        currCourse.setType(CourseType.ADVANCED_ALGORTIHMS);
//                        currCourse.set_name("AALG");
//                    } else if (courseStr.toLowerCase().contains("semantic")) {
//                        currCourse.setType(CourseType.SEMANTICS);
//                        currCourse.set_name("SV");
//                    }

                    currCourse.set_name(courseStr);

                    currCourse.set_time(course.getElementsByClass("time").text());
                    currCourse.setLocation(course.getElementsByClass("location").text());
                    currCourse.set_note(course.getElementsByClass("note").text());
                    day.addCourse(currCourse);
                }

                _schemaDays.add(day);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "*********************************************");

        return true;
    }

    private void setDayDate(SchemaDay currDay, String currDate, Element element, String dateString) {
        if (dateString.compareTo(currDate) == 0) {
            currDay.set_weekday("Today");
            currDay.set_date(dateString);
            isToday = true;
        } else if(isToday) {
            currDay.set_weekday("Tomorrow");
            currDay.set_date(dateString);
            isToday = false;
        } else {
            currDay.set_weekday(element.getElementsByClass("dayname").text());
            currDay.set_date(dateString);
        }
    }

    @NonNull
    private String getDate() {
        TimeZone timeZone = TimeZone.getTimeZone("CET");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(new Date());
    }
}
