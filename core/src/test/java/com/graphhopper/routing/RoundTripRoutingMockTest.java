
package com.graphhopper.routing;

import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.Snap;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.shapes.GHPoint3D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests for RoundTripRouting using Mockito mocks.
 * 
 * This test demonstrates mocking TWO different GraphHopper classes:
 * 1. LocationIndex - interface from GraphHopper to control location lookups
 * 2. EdgeFilter - interface from GraphHopper to control edge filtering
 * 
 * Mocked classes: LocationIndex and EdgeFilter (2 classes GraphHopper différentes)
 * Reason: 
 * - LocationIndex requires a real graph, which is complex to set up. Mocking it allows
 *   testing the logic of RoundTripRouting without graph infrastructure.
 * - EdgeFilter controls edge filtering logic. Mocking it allows testing different filtering
 *   scenarios without creating real edge filters.
 */
@ExtendWith(MockitoExtension.class)
public class RoundTripRoutingMockTest {

    @Mock
    private LocationIndex mockLocationIndex;

    @Mock
    private EdgeFilter mockEdgeFilter;

    /**
     * Test RoundTripRouting.lookup with mocked LocationIndex and EdgeFilter.
     * 
     * This test mocks both LocationIndex and EdgeFilter (2 GraphHopper classes) to test
     * the lookup logic without needing real graph data.
     */
    @Test
    public void testLookupWithMockedLocationIndexAndEdgeFilter() {
        // Create a valid Snap for the start point
        GHPoint startPoint = new GHPoint(50.0, 10.0);
        Snap startSnap = new Snap(startPoint.getLat(), startPoint.getLon());
        startSnap.setClosestNode(100);
        startSnap.setQueryDistance(0.0);
        // Set snapped point directly (needed because RoundTripRouting.lookup calls getSnappedPoint())
        startSnap.setSnappedPoint(new GHPoint3D(startPoint.getLat(), startPoint.getLon(), 0.0));

        // Create valid Snap for intermediate point
        // Default Params generates 1 intermediate point (roundTripPointCount = 2, so 2-1 = 1 point)
        GHPoint intermediatePoint = new GHPoint(50.1, 10.1);
        Snap intermediateSnap = new Snap(intermediatePoint.getLat(), intermediatePoint.getLon());
        intermediateSnap.setClosestNode(101);
        intermediateSnap.setQueryDistance(5.0);
        // Set snapped point directly (needed because RoundTripRouting.lookup calls getSnappedPoint())
        intermediateSnap.setSnappedPoint(new GHPoint3D(intermediatePoint.getLat(), intermediatePoint.getLon(), 0.0));

        // Set up mock LocationIndex to return our mocked Snaps (classe GraphHopper mockée #1)
        // The lookup method calls findClosest first for the start point, then for intermediate points
        // We need to ensure the first call returns startSnap, and subsequent calls return intermediateSnap
        when(mockLocationIndex.findClosest(anyDouble(), anyDouble(), eq(mockEdgeFilter)))
                .thenReturn(startSnap)  // First call for start point
                .thenReturn(intermediateSnap);  // Subsequent calls for intermediate points

        // Create params
        RoundTripRouting.Params params = new RoundTripRouting.Params();

        // Call lookup - this uses both mocked LocationIndex and EdgeFilter
        List<Snap> result = RoundTripRouting.lookup(
                List.of(startPoint),
                mockEdgeFilter,        // ← Classe GraphHopper mockée #2 utilisée
                mockLocationIndex,     // ← Classe GraphHopper mockée #1 utilisée
                params
        );

        // Verify that LocationIndex was called (classe GraphHopper mockée #1)
        verify(mockLocationIndex, atLeastOnce()).findClosest(anyDouble(), anyDouble(), eq(mockEdgeFilter));

        // Verify the result structure
        assertNotNull(result);
        assertTrue(result.size() >= 2); // At least start and end
        assertEquals(startSnap, result.get(0)); // First should be start
        assertEquals(startSnap, result.get(result.size() - 1)); // Last should be start again (round trip)
    }

    /**
     * Test RoundTripRouting with mocked LocationIndex returning invalid snap and EdgeFilter.
     * 
     * This test mocks both LocationIndex and EdgeFilter (2 GraphHopper classes) to test
     * error handling without needing to set up a real graph scenario.
     */
    @Test
    public void testLookupWithInvalidSnapFromLocationIndex() {
        // Create params
        RoundTripRouting.Params params = new RoundTripRouting.Params();

        GHPoint startPoint = new GHPoint(50.0, 10.0);
        Snap invalidSnap = new Snap(startPoint.getLat(), startPoint.getLon());
        // invalidSnap is invalid by default (closestNode is INVALID_NODE)

        // Set up mock LocationIndex to return invalid snap (classe GraphHopper mockée #1)
        when(mockLocationIndex.findClosest(eq(startPoint.getLat()), eq(startPoint.getLon()), eq(mockEdgeFilter)))
                .thenReturn(invalidSnap);

        // Note: We don't stub mockEdgeFilter.accept() because it's never called.
        // When the snap is invalid, RoundTripRouting.lookup throws an exception immediately
        // at line 80, before the EdgeFilter would be used.

        // Call lookup - should throw PointNotFoundException
        Exception exception = assertThrows(Exception.class, () -> {
            RoundTripRouting.lookup(
                    List.of(startPoint),
                    mockEdgeFilter,        // ← Classe GraphHopper mockée #2 utilisée
                    mockLocationIndex,     // ← Classe GraphHopper mockée #1 utilisée
                    params
            );
        });
        
        // Verify exception was thrown
        assertNotNull(exception);

        // Verify that LocationIndex was called (classe GraphHopper mockée #1)
        verify(mockLocationIndex, atLeastOnce()).findClosest(anyDouble(), anyDouble(), eq(mockEdgeFilter));
    }
}

