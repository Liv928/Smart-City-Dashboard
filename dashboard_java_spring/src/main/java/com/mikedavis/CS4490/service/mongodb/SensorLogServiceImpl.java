package com.mikedavis.CS4490.service.mongodb;

import com.mikedavis.CS4490.model.*;
import com.mikedavis.CS4490.mongodao.MongoDAO;
import com.mikedavis.CS4490.service.sql.EventService;
import com.mikedavis.CS4490.service.sql.SensorService;
import com.mongodb.DBObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SensorLogServiceImpl implements SensorLogService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SensorService sensorService;

    @Autowired
    EventService eventService;

    @Autowired
    MongoDAO mongoDAO;

    public JSONArray getSensorJSONDataByID(String id, String date){
        List<DBObject> sensorLogDoc = mongoTemplate.find(new Query(Criteria.where("_id").is(id + "-" + date)), DBObject.class, "5765_tls");
        JSONArray data = new JSONArray();
        for(DBObject object : sensorLogDoc){
            for(String key : object.keySet()){
                if(key.matches("^\\d{2}:\\d{2}:\\d{2}$")){
                    JSONArray dataPoint = new JSONArray();
                    dataPoint.put(key);
                    dataPoint.put(object.get(key));
                    data.put(dataPoint);
                }
            }
        }
        return data;
    }

    public void addJSONDataByID(String id, String date, List<JSONArray> jsonArray){
        List<DBObject> sensorLogDoc = mongoTemplate.find(new Query(Criteria.where("_id").is(id + "-" + date)), DBObject.class, "5765_tls");
        SimpleDateFormat simpleDateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Long timeStamp;
        for(DBObject object : sensorLogDoc){
            for(String key : object.keySet()){
                if(key.matches("^\\d{2}:\\d{2}:\\d{2}$")){
                    JSONArray dataPoint = new JSONArray();
                    String start = date + " " + key;
                    try{
                        timeStamp = simpleDateFormat.parse(start).getTime();
                        dataPoint.put(timeStamp);
                        dataPoint.put(object.get(key));
                        jsonArray.add(dataPoint);
                    } catch(ParseException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public SensorLog getSensorLogByID(String id, String date){
        List<DBObject> sensorLogDoc = mongoTemplate.find(new Query(Criteria.where("_id").is(id + "-" + date)), DBObject.class, "5765_tls");
        SensorLog sensorLog = new SensorLog();
        sensorLog.setId(id);
        sensorLog.setDate(date);
        Map<String, String> trendLogData = new LinkedHashMap<String, String>();
        for(DBObject object : sensorLogDoc){
            for(String key : object.keySet()){
                if(key.matches("^\\d{2}:\\d{2}:\\d{2}$")){
                    trendLogData.put(key, object.get(key).toString());
                }
            }
        }
        sensorLog.setData(trendLogData);
        return sensorLog;
    }

    public SensorData getSensorDataByID(String id, String date){
        SensorData sensorData = new SensorData();
        JSONArray data = getSensorJSONDataByID(id, date);
        sensorData.setData(data.toString());
        sensorData.setSensor(sensorService.getSensor(id));
        sensorData.setSensorMeta(getSensorMetaDataByID(id));
        return sensorData;
    }

    public SensorData getSensorDataByDateRange(String id, String start, String end){
        SensorData sensorData = new SensorData();
        sensorData.setSensor(sensorService.getSensor(id));
        sensorData.setSensorMeta(getSensorMetaDataByID(id));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);
        List<JSONArray> data = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
        {
            addJSONDataByID(id, date.toString(), data);
        }

        sensorData.setData(data.toString());

        return sensorData;
    }

    public SensorData getAllSensorDataByID(String id){
        SensorData sensorData = new SensorData();

        //set sensor
        Sensor sensor = sensorService.getSensor(id);
        sensorData.setSensor(sensor);

        //set additional metadata
        sensorData.setAdditionalMetadata(sensorService.getAdditionalMetadata(id));//list of data

        //get sensormeta from MongoDB
        sensorData.setSensorMeta(getSensorMetaDataByID(id));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String start = sensorData.getSensorMeta().getFirst();

        String end = sensorData.getSensorMeta().getLast();


        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);
        List<JSONArray> data = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
        {
            addJSONDataByID(id, date.toString(), data);
        }

        Collections.sort(data, new Comparator<JSONArray>(){
            @Override
            public int compare(JSONArray jsonArrayA, JSONArray jsonArrayB){
                int compare = 0;
                try{
                    long keyA = jsonArrayA.getLong(0);
                    long keyB = jsonArrayB.getLong(0);
                    compare = Long.compare(keyA, keyB);
                } catch(Exception e){
                    e.printStackTrace();
                }
                return compare;
            }
        });
        sensorData.setData(data.toString());
        List<Event> events = eventService.getEvents(sensor.getBuildingId(), id);
        sensorData.setEvents(events);
        return sensorData;
    }

    public SensorMeta getSensorMetaDataByID(String id) {
        SensorMeta sensorMeta = mongoDAO.findSensorMetaById(id);
        return sensorMeta;
    }

    public List<SensorMeta> findSensorMetasById(String id){
        System.out.println("list function");
        List<SensorMeta> sensorMetas = mongoDAO.findSensorMetasById(id);
        return sensorMetas;
    }
}
