package com.mikedavis.CS4490.service.sql;

import com.mikedavis.CS4490.mapper.BuildingMapper;
import com.mikedavis.CS4490.mapper.MetaMapper;
import com.mikedavis.CS4490.mapper.SensorMapper;
import com.mikedavis.CS4490.model.AdditionalMetadata;
import com.mikedavis.CS4490.model.Building;
import com.mikedavis.CS4490.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService{
    @Autowired
    private SensorMapper sensorMapper;

    @Autowired
    private MetaMapper metaMapper;

    @Override
    public Sensor getSensor(String id){
        Sensor test = sensorMapper.getSensor(id);
        return sensorMapper.getSensor(id);
    }

    @Override
    public List<Sensor> getSensorsByBuilding(String buildingId){
        return sensorMapper.getSensorsByBuilding(buildingId);
    }

    @Override
    public List<AdditionalMetadata> getAdditionalMetadata(String sensorId){
        return metaMapper.getAdditionalMetadata(sensorId);
    }

    @Override
    public void insertAdditionalMetadata(AdditionalMetadata additionalMetadata){
        metaMapper.insertAdditionalMetadata(additionalMetadata);
        System.out.println("insert meta");
    }

    @Override
    public void deleteAdditionalMetadata(String metaTitle){
        metaMapper.deleteMeta(metaTitle);
        System.out.println("delete meta");
    }

    @Override
    public void updateAdditionalMetadata(AdditionalMetadata additionalMetadata) {
        System.out.println("update meta Id " + additionalMetadata.getId());
        metaMapper.updateMeta(additionalMetadata);
    }
}
