package com.mikedavis.CS4490.controller.api;

import com.mikedavis.CS4490.model.SensorData;
import com.mikedavis.CS4490.model.SensorMeta;
import com.mikedavis.CS4490.service.mongodb.SensorLogService;
import com.mikedavis.CS4490.mongodao.MongoDAO;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/logs")
public class SensorLogController {

    @Autowired
    SensorLogService sensorLogService;

    @Autowired
    MongoDAO mongoDAO;

    @RequestMapping("/data/single/{id}")
    public SensorData getSensorDataById(@PathVariable String id, @RequestParam String date){

        return sensorLogService.getSensorDataByID(id, date);
    }

    @RequestMapping("/data/multi/{id}")
    public SensorData getSensorDataByDateRange(@PathVariable String id, @RequestParam String startDate, @RequestParam String endDate){
        return sensorLogService.getSensorDataByDateRange(id, startDate, endDate);
    }

    @RequestMapping("/data/all/{id}")
    public SensorData getAllSensorDataById(@PathVariable String id){
        return sensorLogService.getAllSensorDataByID(id);
    }

    @RequestMapping("/meta/{id}")
    public SensorMeta getSensorMetaById(@PathVariable String id){
        return sensorLogService.getSensorMetaDataByID(id);
    }

    @RequestMapping("/data/json/{id}")
    public JSONArray getSensorJSONDataById(@PathVariable String id, @RequestParam String date){
        return sensorLogService.getSensorJSONDataByID(id, date);
    }

    @RequestMapping("/find/{id}")
    public List<SensorMeta> findSensorMetasById(@PathVariable String id){
        return sensorLogService.findSensorMetasById(id);
    }
}
