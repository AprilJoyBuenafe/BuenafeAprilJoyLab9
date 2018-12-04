package com.buenafe.apriljoy;

public class Person {

        String fullname, gender;
        Integer age;

    public Person(String fullname, Integer age, String gender) {
        this.fullname = fullname;
        this.age = age;
        this.gender = gender;
    }

    public String getFullname()  {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
