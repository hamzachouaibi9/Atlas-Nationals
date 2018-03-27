package com.fbla.atlas.atlas.models;

/**
 * Created by hamza on 2/12/18.
 */

public class UserRequest {

    String name, school, pic;

    public UserRequest(String name, String school, String pic) {
        this.name = name;
        this.school = school;
        this.pic = pic;
    }

    public UserRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
