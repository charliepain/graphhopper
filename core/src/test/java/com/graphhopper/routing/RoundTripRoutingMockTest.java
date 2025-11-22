
package com.graphhopper.routing;

import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.Snap;
import com.graphhopper.util.shapes.GHPoint;
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

        // Create valid Snaps for intermediate points
        Snap intermediateSnap1 = new Snap(50.1, 10.1);
        intermediateSnap1.setClosestNode(101);
        intermediateSnap1.setQueryDistance(5.0);

        Snap intermediateSnap2 = new Snap(50.2, 10.2);
        intermediateSnap2.setClosestNode(102);
        intermediateSnap2.setQueryDistance(5.0);

        // Set up mock LocationIndex to return our mocked Snaps (classe GraphHopper mockée #1)
        when(mockLocationIndex.findClosest(eq(startPoint.getLat()), eq(startPoint.getLon()), eq(mockEdgeFilter)))
                .thenReturn(startSnap);
        when(mockLocationIndex.findClosest(anyDouble(), anyDouble(), eq(mockEdgeFilter)))
                .thenReturn(intermediateSnap1, intermediateSnap2);

        // Set up mock EdgeFilter to accept all edges (classe GraphHopper mockée #2)
        when(mockEdgeFilter.accept(any())).thenReturn(true);

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

        // Set up mock EdgeFilter (classe GraphHopper mockée #2)
        when(mockEdgeFilter.accept(any())).thenReturn(true);

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

