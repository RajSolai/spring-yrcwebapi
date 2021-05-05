package com.github.rajsolai.yrcwebapi.models;

import org.json.JSONObject;

public class Volunteer {
    private String id;
    private String avatarurl;
    private String name;
    private String contact;
    private String dept;
    private String year;

    public Volunteer (String id,String avatarurl,String name,String contact,String dept,String year) {
        this.id = id;
        this.avatarurl = avatarurl;
        this.name = name;
        this.contact = contact;
        this.dept = dept;
        this.year = year;
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("avatarurl",avatarurl);
        jsonObject.put("name",name);
        jsonObject.put("contact",contact);
        jsonObject.put("dept",dept);
        jsonObject.put("year",year);
        return jsonObject;
    }

}
