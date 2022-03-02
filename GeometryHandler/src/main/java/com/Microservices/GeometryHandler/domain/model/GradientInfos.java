package com.Microservices.GeometryHandler.domain.model;

public class GradientInfos {
    
    private double min;
    private double max;
    private double average;
    private double median;
    private int faceNumber;


    public GradientInfos() {
    }

    public GradientInfos(double min, double max, double average, double median, int faceNumber) {
        this.min = min;
        this.max = max;
        this.average = average;
        this.median = median;
        this.faceNumber = faceNumber;
    }


    public double getMin() {
        return this.min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return this.max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getAverage() {
        return this.average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getMedian() {
        return this.median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public int getFaceNumber() {
        return this.faceNumber;
    }

    public void setFaceNumber(int faceNumber) {
        this.faceNumber = faceNumber;
    }


}
