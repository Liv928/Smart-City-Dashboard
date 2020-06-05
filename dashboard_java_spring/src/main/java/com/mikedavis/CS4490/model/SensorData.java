package com.mikedavis.CS4490.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SensorData implements Serializable {
    private Sensor sensor;
    private String data;
    private SensorMeta sensorMeta;
    private List<AdditionalMetadata> additionalMetadata;
    private List<Event> events;
}
