package com.Microservices.GeometryHandler.domain.model;

public class IndexedTin {

    public double[][] Points;
    public int[] Triangles;

    public IndexedTin(double[][] points, int[] triangles) {
        Points = points;
        Triangles = triangles;
    }
    
}
