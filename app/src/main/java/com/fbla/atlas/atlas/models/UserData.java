package com.fbla.atlas.atlas.models;

/**
 * Created by Hamza on 11/20/2017.
 */

public class UserData {

    String name;
    String phone;
    String school;
    String grade;
    String sex;
    String pic;
    String age;


    public UserData() {

    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public UserData(String name, String phone, String school, String grade, String sex, String age, String pic) {
        this.name = name;
        this.phone = phone;
        this.school = school;
        this.grade = grade;
        this.sex = sex;
        this.pic = pic;
        this.age = age;
    }
}

