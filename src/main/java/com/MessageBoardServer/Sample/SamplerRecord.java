package com.MessageBoardServer.Sample;

/**
 * Created by DELL on 14-3-8.
 */
public class SamplerRecord {
    private int id =0;
    private String name;
    private int birthDay=0;//19900101
    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String from) {
        location = from;
    }


    @Override
    public String toString() {
        return "SamplerRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDay=" + birthDay +
                ", location='" + location + '\'' +
                '}';
    }
}
