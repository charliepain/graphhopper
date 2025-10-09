package com.graphhopper.util;

import com.graphhopper.util.shapes.GHPoint;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.algorithm.Distance;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceCalcEuclideanExtraTest {
    @Test
    public void testCalcDist3DNormalXYZ() {
        DistanceCalcEuclidean distCalc = new DistanceCalcEuclidean();
        assertEquals(12.328828, distCalc.calcDist3D(
                1, -3, 2,
                5, 7, 8
        ), 1e-6);
    }

    @Test
    public void testIntermediatePointNormalInputs() {
        DistanceCalcEuclidean distCalc = new DistanceCalcEuclidean();
        GHPoint ghPoint = distCalc.intermediatePoint(0.7, 1, 3, 11, 8);
        assertEquals(8, ghPoint.lat);
        assertEquals(6.5, ghPoint.lon);
    }

}
