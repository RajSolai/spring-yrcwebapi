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
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    @CrossOrigin
    public String getAllEvents(){
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
        return jsonArray.toString();
    }

    // get recent events
    @RequestMapping(path ="/recents", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public String getRecentEvents(){
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
        return jsonArray.toString();
    }

    // Add a New Event
    @RequestMapping(path = "/events/add" , method = RequestMethod.POST )
    @CrossOrigin
    public void addEvent(@RequestBody Map<String,String> data , HttpServletResponse response){
        Document eventModel = new Document();
        eventModel.put("uploaddate",data.get("uploaddate"));
        eventModel.put("title",data.get("title"));
        eventModel.put("imgtag",data.get("imgtag"));
        eventModel.put("imgurl",data.get("imgurl"));
        eventModel.put("story",data.get("story"));
        eventModel.put("desc",data.get("desc"));
        eventModel.put("links",data.get("links"));
        database.getCollection("yrcevents").insertOne(eventModel);
        response.setStatus(200);
    }

    // Get one Event by Id
    @RequestMapping(path = "/event/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAnEvent(@PathVariable String id) {
        Document eventDocument = database.getCollection("yrcevents")
                .find(Filters.eq("_id",new ObjectId(id)))
                .first();
        assert eventDocument != null;
        EventPost eventPost = new EventPost(
                eventDocument.get("_id").toString(),
                eventDocument.get("uploaddate").toString(),
                eventDocument.get("title").toString(),
                eventDocument.get("imgtag").toString(),
                eventDocument.get("imgurl").toString(),
                eventDocument.get("story").toString(),
                eventDocument.get("desc").toString(),
                eventDocument.get("links").toString()
        );
        return eventPost.getJson().toString();
    }


    // Remove a Event by Id
    @RequestMapping(path = "/events/{id}" , method = RequestMethod.DELETE)
    @CrossOrigin
    public String removeEvent(@PathVariable String id,HttpServletResponse response){
        database.getCollection("yrcevents")
                .findOneAndDelete(Filters.eq("_id",new ObjectId(id)));
        response.setStatus(200);
        return new ObjectId(id).toString();
    }
}
