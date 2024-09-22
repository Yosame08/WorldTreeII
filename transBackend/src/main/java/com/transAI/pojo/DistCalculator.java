package com.transAI.pojo;

public class DistCalculator {

    private static final double EARTH_RADIUS = 6371000; // Earth radius in meters

    public static double haversine(double lon1, double lat1, double lon2, double lat2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    public static void main(String[] args) {
        double lon1 = 121.50209;
        double lat1 = 31.30552;
        double lon2 = 121.50777;
        double lat2 = 31.303138;
        double distance = haversine(lon1, lat1, lon2, lat2);
        System.out.println("Distance: " + distance + " meters");
    }
}