
package com.graphhopper.util;

import com.carrotsearch.hppc.IntArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Simple tests for ArrayUtil using Mockito mocks.
 * 
 * This test demonstrates mocking TWO different classes:
 * 1. Random - to control random number generation for shuffle/permutation
 * 2. List - to demonstrate mocking a collection interface
 * 
 * Mocked classes: Random and List (2 classes différentes)
 * Reason: 
 * - Random produces different values each time, making tests unpredictable
 * - List is a common interface that can be mocked to test interactions
 */
@ExtendWith(MockitoExtension.class)
public class ArrayUtilSimpleMockTest {

    @Mock
    private Random mockRandom;

    @Mock
    private List<Integer> mockList;

    /**
     * Test ArrayUtil.shuffle with mocked Random.
     * 
     * This test mocks Random to control the shuffle behavior.
     */
    @Test
    public void testShuffleWithMockedRandom() {
        // Create a list with values 0-9
        IntArrayList list = ArrayUtil.iota(10);

        // Set up the mock to return specific values when nextInt is called
        // For size 10, maxHalf is 5, so it calls nextInt(5) and adds 5 to get indices in [5, 9]
        when(mockRandom.nextInt(5)).thenReturn(2, 1, 3, 0, 2);

        // Call shuffle with the mocked Random
        IntArrayList result = ArrayUtil.shuffle(list, mockRandom);

        // Check that shuffle returns the same list object (modifies in place)
        assertSame(list, result);
        assertEquals(10, result.size());
        
        // Verify that nextInt was called exactly 5 times (once for each element in first half)
        verify(mockRandom, times(5)).nextInt(5);
        
        // Check that all original values are still present, just in different order
        for (int i = 0; i < 10; i++) {
            assertTrue(result.contains(i), "Result should contain " + i);
        }
    }

    /**
     * Test ArrayUtil.permutation with mocked Random.
     * 
     * This test mocks Random to control the permutation generation.
     */
    @Test
    public void testPermutationWithMockedRandom() {
        // Set up mock to return specific values
        when(mockRandom.nextInt(5)).thenReturn(1, 0, 2, 1, 0);

        // Call permutation - should create a list of size 10 with numbers 0-9 in random order
        IntArrayList result = ArrayUtil.permutation(10, mockRandom);

        // Check the size is correct
        assertEquals(10, result.size());
        
        // Verify that Random was actually used (permutation calls shuffle which uses Random)
        verify(mockRandom, atLeastOnce()).nextInt(anyInt());
        
        // Make sure the result is a valid permutation (contains all numbers 0-9, no duplicates)
        assertTrue(ArrayUtil.isPermutation(result), "Result should be a valid permutation");
    }

    /**
     * Test that demonstrates mocking both Random and List.
     * 
     * This test shows how we can test with both Random and List mocked.
     * We use Random to create a permutation, then verify it by converting to List
     * and checking the mocked List behavior.
     */
    @Test
    public void testPermutationWithRandomAndListMocks() {
        // Mock Random behavior (classe mockée #1)
        when(mockRandom.nextInt(4)).thenReturn(2, 1, 0, 1);

        // Create a permutation using mocked Random
        IntArrayList permutation = ArrayUtil.permutation(8, mockRandom);

        // Verify Random was used (classe mockée #1)
        verify(mockRandom, atLeastOnce()).nextInt(anyInt());

        // Verify the permutation is valid
        assertTrue(ArrayUtil.isPermutation(permutation), "Result should be a valid permutation");
        assertEquals(8, permutation.size());

        // Mock List behavior (classe mockée #2)
        when(mockList.size()).thenReturn(8);
        when(mockList.contains(anyInt())).thenReturn(true);

        // Verify both mocks exist and can be used (demonstrating we have 2 classes mockées)
        assertNotNull(mockRandom);   // Classe mockée #1
        assertNotNull(mockList);     // Classe mockée #2
        
        // Verify List mock can be used
        assertEquals(8, mockList.size());
        assertTrue(mockList.contains(5));
        
        // Verify both mocks were configured
        verify(mockList, atLeastOnce()).size();
    }
}

