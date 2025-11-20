
package com.graphhopper.util;

import com.carrotsearch.hppc.IntArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for ArrayUtil using Mockito mocks.
 * 
 * Using Mockito to mock the Random class so we can control what values it returns.
 * This makes tests deterministic - instead of getting different random numbers each time,
 * we can specify exactly what the mock should return and verify the code works correctly.
 * 
 * Mocked class: Random
 * Reason: Random produces different values each time, making tests unpredictable.
 * By mocking it, we can control the values and test that shuffle works properly.
 */
@ExtendWith(MockitoExtension.class)
public class ArrayUtilMockTest {

    @Mock
    private Random mockRandom;

    /**
     * Test shuffle method with mocked Random.
     * 
     * Tests that ArrayUtil.shuffle works correctly when we control what Random returns.
     * This makes the test deterministic instead of relying on actual random values.
     */
    @Test
    public void testShuffleWithMockedRandom() {
        // Create a list with values 0-9
        IntArrayList list = ArrayUtil.iota(10);

        // Set up the mock to return specific values when nextInt is called
        // Looking at the shuffle code, it calls nextInt(maxHalf) where maxHalf = size/2
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
        // Can't check exact order since it depends on the swapping, but all numbers should be there
        for (int i = 0; i < 10; i++) {
            assertTrue(result.contains(i), "Result should contain " + i);
        }
    }

    /**
     * Test permutation method with mocked Random.
     * 
     * Tests that ArrayUtil.permutation creates a valid permutation when we control Random.
     * The permutation method uses shuffle internally, so we need to mock Random for that too.
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

}

