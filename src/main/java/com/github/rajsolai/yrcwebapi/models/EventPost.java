package com.github.rajsolai.yrcwebapi.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.json.JSONObject;

public class EventPost {
    private String id;
    private String uploaddate;
    private String title;
    private String imgurl;
    private String imgtag;
    private String story;
    private String desc;
    private String links;

    public EventPost () {}
    public EventPost(String id,String uploaddate, String title, String imgtag, String imgurl, String story, String desc, String links) {
        this.id = id;
        this.desc = desc;
        this.imgtag = imgtag;
        this.links = links;
        this.imgurl = imgurl;
        this.story = story;
        this.uploaddate = uploaddate;
        this.title = title;
    }

    public JSONObject getJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("upploaddate",uploaddate);
        jsonObject.put("title",title);
        jsonObject.put("imgurl",imgurl);
        jsonObject.put("imgtag",imgtag);
        jsonObject.put("story",story);
        jsonObject.put("desc",desc);
        jsonObject.put("links",links);
        return  jsonObject;
    }

}
