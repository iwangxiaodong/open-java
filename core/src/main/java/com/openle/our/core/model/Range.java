package com.openle.our.core.model;

import java.io.Serializable;

public class Range implements Serializable{
    private Float min;
    private Float max;
    private Float defaultMin;
    private Float defaultMax;

    public Range(){}

    public Range(Float min, Float max, Float defaultMax){
        this.min = min;
        this.max = max;
        this.defaultMax = defaultMax;
    }

    public Range(Float min, Float max, Float defaultMin, Float defaultMax){
        this.min = min;
        this.max = max;
        this.defaultMin = defaultMin;
        this.defaultMax = defaultMax;
    }

    public Float getMin() {
        return min;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    public Float getMax() {
        return max;
    }

    public void setMax(Float max) {
        this.max = max;
    }

    public Float getDefaultMin() {
        return defaultMin;
    }

    public void setDefaultMin(Float defaultMin) {
        this.defaultMin = defaultMin;
    }

    public Float getDefaultMax() {
        return defaultMax;
    }

    public void setDefaultMax(Float defaultMax) {
        this.defaultMax = defaultMax;
    }
}
