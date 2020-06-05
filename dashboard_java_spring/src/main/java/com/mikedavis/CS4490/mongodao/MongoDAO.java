package com.mikedavis.CS4490.mongodao;

import com.mikedavis.CS4490.model.SensorMeta;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoDAO {
    public SensorMeta findSensorMetaById(String id);
    public List<DBObject> getSensorLogByID(String id, String date);
    public List<SensorMeta> findSensorMetasById(String id);
}
