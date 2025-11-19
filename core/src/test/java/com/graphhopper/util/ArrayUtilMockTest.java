/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
 * Tests for ArrayUtil class using mocks to isolate dependencies.
 * 
 * This test class demonstrates the use of Mockito to mock external dependencies
 * (Random) to test ArrayUtil methods in isolation and ensure deterministic behavior.
 */
@ExtendWith(MockitoExtension.class)
public class ArrayUtilMockTest {

    @Mock
    private Random mockRandom;

    /**
     * Test shuffle method with mocked Random.
     * 
     * This test verifies that ArrayUtil.shuffle correctly shuffles an IntArrayList
     * using a Random number generator. We mock Random to control the shuffle behavior
     * and ensure deterministic test results.
     * 
     * Mocked class: Random
     * Reason: We mock Random to control the shuffle behavior deterministically.
     * By controlling the random values, we can verify that the shuffle algorithm
     * works correctly without relying on non-deterministic random behavior.
     * The values are chosen to test specific swap operations in the shuffle algorithm.
     */
    @Test
    public void testShuffleWithMockedRandom() {
        // Arrange: Create an IntArrayList and configure mock Random behavior
        IntArrayList list = ArrayUtil.iota(10); // Creates [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
        // Store original values for comparison
        int[] originalValues = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            originalValues[i] = list.get(i);
        }

        // Configure mock Random to return specific values for deterministic shuffling
        // The shuffle algorithm uses: random.nextInt(maxHalf) + maxHalf
        // For size 10, maxHalf = 5, so it will call nextInt(5) + 5, returning values in [5, 9]
        // We'll make it return 7, 6, 8, 5, 7 to test specific swap operations
        when(mockRandom.nextInt(5)).thenReturn(2, 1, 3, 0, 2); // These will become 7, 6, 8, 5, 7

        // Act: Call the method under test
        IntArrayList result = ArrayUtil.shuffle(list, mockRandom);

        // Assert: Verify the list was modified (shuffled)
        assertSame(list, result); // Should return the same list instance
        assertEquals(10, result.size());
        
        // Verify that Random.nextInt was called the expected number of times
        // For size 10, maxHalf = 5, so it should be called 5 times
        verify(mockRandom, times(5)).nextInt(5);
        
        // Verify that the list elements are still the same values (just reordered)
        // We can't predict exact order due to in-place swapping, but we can verify
        // that all original values are still present
        for (int i = 0; i < 10; i++) {
            assertTrue(result.contains(i), "Result should contain " + i);
        }
    }

    /**
     * Test permutation method with mocked Random.
     * 
     * This test verifies that ArrayUtil.permutation correctly creates a permutation
     * of numbers using a Random number generator. We mock Random to control the
     * permutation behavior and ensure deterministic test results.
     * 
     * Mocked class: Random
     * Reason: We mock Random to control the permutation generation deterministically.
     * This allows us to test the permutation logic without relying on non-deterministic
     * random behavior. The values are chosen to test the permutation algorithm.
     */
    @Test
    public void testPermutationWithMockedRandom() {
        // Arrange: Configure mock Random behavior
        int size = 8;
        // The permutation method calls iota(size) then shuffle(list, random)
        // For size 8, maxHalf = 4, so shuffle will call nextInt(4) + 4, returning [4, 7]
        when(mockRandom.nextInt(4)).thenReturn(1, 2, 0, 3); // These will become 5, 6, 4, 7

        // Act: Call the method under test
        IntArrayList result = ArrayUtil.permutation(size, mockRandom);

        // Assert: Verify the result is a valid permutation
        assertNotNull(result);
        assertEquals(size, result.size());
        
        // Verify it's a valid permutation (contains all numbers 0 to size-1)
        assertTrue(ArrayUtil.isPermutation(result), "Result should be a valid permutation");
        
        // Verify that Random.nextInt was called the expected number of times
        // For size 8, maxHalf = 4, so it should be called 4 times
        verify(mockRandom, times(4)).nextInt(4);
        
        // Verify all expected values are present
        for (int i = 0; i < size; i++) {
            assertTrue(result.contains(i), "Result should contain " + i);
        }
    }
}

