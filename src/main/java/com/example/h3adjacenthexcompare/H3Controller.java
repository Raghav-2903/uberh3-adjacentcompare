package com.example.h3adjacenthexcompare;

import com.uber.h3core.H3Core;
import com.uber.h3core.util.GeoCoord;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class H3Controller {

    @GetMapping("/areAdjacent")
    public boolean areHexagonsAdjacent(
            @RequestParam long hex1,
            @RequestParam long hex2,
            @RequestParam int resolution
    ) throws IOException {
        H3Core h3 = H3Core.newInstance();

        GeoCoord coordinates1 = h3.h3ToGeo(hex1);
        GeoCoord coordinates2 = h3.h3ToGeo(hex2);

        double tolerance = calculateTolerance(resolution);

        List<Long> potentialNeighbors = h3.kRing(hex1, 1);

        for (long neighbor : potentialNeighbors) {
            GeoCoord neighborCoordinates = h3.h3ToGeo(neighbor);
            double neighborLat = neighborCoordinates.lat;
            double neighborLon = neighborCoordinates.lng;

            if (Math.abs(coordinates2.lat - neighborLat) < tolerance &&
                    Math.abs(coordinates2.lng - neighborLon) < tolerance) {
                return true;
            }
        }

        return false;
    }

    private double calculateTolerance(int resolution) {
        return 0.001 / Math.pow(10, resolution);
    }
}




