package com.fbla.atlas.atlas.models;

/**
 * Created by Hamza on 1/17/2018.
 */

public class EditUserData {
    String name;
    String phone;
    String school;
    String grade;
    String sex;
    String age;

    public EditUserData(String name, String phone, String school, String grade, String sex, String age) {
        this.name = name;
        this.phone = phone;
        this.school = school;
        this.grade = grade;
        this.sex = sex;
        this.age = age;
    }

    public EditUserData() {
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
