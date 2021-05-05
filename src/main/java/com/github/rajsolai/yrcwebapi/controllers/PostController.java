package com.github.rajsolai.yrcwebapi.controllers;

import com.github.rajsolai.yrcwebapi.models.EventPost;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class PostController {
    private MongoDatabase database;

    PostController() {
        MongoClient mongoClient = MongoClients.create(
                "mongodb+srv://4WdMdJtVQbdh4FR:FU6bG4yLTdHYQ9l0@cluster0.udyz3.mongodb.net/yrcsrmvec?retryWrites=true&w=majority");
        database = mongoClient.getDatabase("yrcsrmvec");
    }

    // Get all the events
    @RequestMapping(path = "/events" , produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllEvents(HttpServletResponse response){
        JSONArray jsonArray = new JSONArray();
        database
                .getCollection("yrcevents")
                .find().forEach(document -> {
                    jsonArray.put(
                      new EventPost(
                              document.get("_id").toString(),
                              document.get("uploaddate").toString(),
                              document.get("title").toString(),
                              document.get("imgtag").toString(),
                              document.get("imgurl").toString(),
                              document.get("story").toString(),
                              document.get("desc").toString(),
                              document.get("links").toString()
                      ).getJson()
                    );
        });
        response.setContentType("application/json");
        return jsonArray.toString();
    }

    // get recent events
    @RequestMapping(path ="/recents", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRecentEvents(HttpServletResponse response){
        JSONArray jsonArray = new JSONArray();
        database
                .getCollection("yrcevents")
                .find().sort(Sorts.descending("uploaddate")).limit(5).forEach(document -> {
            jsonArray.put(
                    new EventPost(
                            document.get("_id").toString(),
                            document.get("uploaddate").toString(),
                            document.get("title").toString(),
                            document.get("imgtag").toString(),
                            document.get("imgurl").toString(),
                            document.get("story").toString(),
                            document.get("desc").toString(),
                            document.get("links").toString()
                    ).getJson()
            );
        });
        response.setContentType("application/json");
        return jsonArray.toString();
    }

    // Add a New Event
    @RequestMapping(path = "/events" , method = RequestMethod.POST ,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public void addEvent(@RequestBody MultiValueMap<String,String> data , HttpServletResponse response){
        Document eventModel = new Document();
        eventModel.put("uploaddate",data.get("uploaddate").get(0));
        eventModel.put("title",data.get("title").get(0));
        eventModel.put("imgtag",data.get("imgtag").get(0));
        eventModel.put("imgurl",data.get("imgurl").get(0));
        eventModel.put("story",data.get("story").get(0));
        eventModel.put("desc",data.get("desc").get(0));
        eventModel.put("links",data.get("links").get(0));
        database.getCollection("yrcevents").insertOne(eventModel);
        response.setStatus(200);
    }

    // Remove a Event by Id
    @RequestMapping(path = "/events/{id}" , method = RequestMethod.DELETE)
    public String removeEvent(@PathVariable String id,HttpServletResponse response){
        database.getCollection("yrcevents")
                .findOneAndDelete(Filters.eq("_id",new ObjectId(id)));
        response.setStatus(200);
        return new ObjectId(id).toString();
    }
}
