package com.printz.guano.aauschema;

/**
 * Created by guano on 07/03/16.
 */

enum CourseType { SEMANTICS, ADVANCED_ALGORTIHMS, SOFTWARE, DATABASES, THEORY_OF_SCIENCE };

public class Course {

    private String _name;
    private String _time;
    private String location;
    private String _note;
    private CourseType type;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String get_note() {
        return _note;
    }

    public void set_note(String _note) {
        this._note = _note;
    }

    public CourseType getColor() {
        return type;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return '\n' +
                _name + '\n' +
                _time + '\n' +
                location + '\n' +
                _note + '\n';
    }
}
