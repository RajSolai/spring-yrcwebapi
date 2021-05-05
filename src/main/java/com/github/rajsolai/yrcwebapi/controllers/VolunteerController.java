package com.github.rajsolai.yrcwebapi.controllers;

import com.github.rajsolai.yrcwebapi.models.Volunteer;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class VolunteerController {
    private MongoDatabase database;

    VolunteerController() {
        MongoClient mongoClient = MongoClients.create(
                "mongodb+srv://4WdMdJtVQbdh4FR:FU6bG4yLTdHYQ9l0@cluster0.udyz3.mongodb.net/yrcsrmvec?retryWrites=true&w=majority");
        database = mongoClient.getDatabase("yrcsrmvec");
    }

    @RequestMapping(path = "/volunteers", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public String getVolunteers(HttpServletResponse response) {
        JSONArray jsonArray = new JSONArray();
        database.getCollection("yrc_vols")
                .find()
                .forEach(document -> {
                    jsonArray.put(
                            new Volunteer(
                                    document.get("_id").toString(),
                                    document.get("avatarurl").toString(),
                                    document.get("name").toString(),
                                    document.get("contact").toString(),
                                    document.get("dept").toString(),
                                    document.get("year").toString()
                            ).getJson()
                    );
                });
        response.setContentType("application/json");
        return jsonArray.toString();
    }

    // Add a New Volunteer
    @RequestMapping(path = "/volunteers/add" , method = RequestMethod.POST )
    @CrossOrigin
    public void addVolunteer(@RequestBody Map<String,String> data , HttpServletResponse response){
        Document volunteerModel = new Document();
        volunteerModel.put("avatarurl",data.get("avatarurl"));
        volunteerModel.put("name",data.get("name"));
        volunteerModel.put("contact",data.get("contact"));
        volunteerModel.put("dept",data.get("dept"));
        volunteerModel.put("year",data.get("year"));
        database.getCollection("yrc_vols").insertOne(volunteerModel);
        response.setStatus(200);
    }

    @RequestMapping(path="/volunteers/rm/{id}", method = RequestMethod.DELETE)
    @CrossOrigin
    public String removeVolunteer(@PathVariable String id, HttpServletResponse response){
        database.getCollection("yrc_vols")
                .findOneAndDelete(Filters.eq("_id",new ObjectId(id)));
        response.setStatus(200);
        return new ObjectId(id).toString();
    }

}
