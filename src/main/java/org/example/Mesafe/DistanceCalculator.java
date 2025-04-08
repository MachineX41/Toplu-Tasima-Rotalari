package org.example.Mesafe;

// Mesafe hesaplayıcı arayüzü
public interface DistanceCalculator {
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
}