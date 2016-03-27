package com.printz.guano.aauschema;

import java.util.ArrayList;

/**
 * Created by guano on 07/03/16.
 */
public class SchemaDay {

    private String _weekday;
    private String _date;
    private ArrayList<Course> courses = new ArrayList<>();

    public String get_weekday() {
        return _weekday;
    }

    public void set_weekday(String _weekday) {
        this._weekday = _weekday;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    @Override
    public String toString() {
        String courseString = "\n";

        for(Course course : courses) {
            courseString = courseString.concat(course.toString());
        }

        return _weekday + " " +
                _date +
                courseString;
    }
}
