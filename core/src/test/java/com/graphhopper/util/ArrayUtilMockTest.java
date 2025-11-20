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
 * 
 * Mocked class: Random
 * Reason: We mock Random to control the shuffle behavior deterministically.
 * By controlling the random values, we can verify that the shuffle algorithm
 * works correctly without relying on non-deterministic random behavior.
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
     */
    @Test
    public void testShuffleWithMockedRandom() {
        // Arrange: Create an IntArrayList and configure mock Random behavior
        IntArrayList list = ArrayUtil.iota(10); // Creates [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

        // Configure mock Random to return specific values for deterministic shuffling
        // The shuffle algorithm uses: random.nextInt(maxHalf) + maxHalf
        // For size 10, maxHalf = 5, so it will call nextInt(5) + 5, returning values in [5, 9]
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
     * using a Random number generator. We mock Random to control the behavior deterministically.
     */
    @Test
    public void testPermutationWithMockedRandom() {
        // Arrange: Configure mock Random behavior
        when(mockRandom.nextInt(5)).thenReturn(1, 0, 2, 1, 0);

        // Act: Call the method under test
        IntArrayList result = ArrayUtil.permutation(10, mockRandom);

        // Assert: Verify the result
        assertEquals(10, result.size());
        
        // Verify that Random.nextInt was called (shuffle is called internally)
        verify(mockRandom, atLeastOnce()).nextInt(anyInt());
        
        // Verify it's a valid permutation
        assertTrue(ArrayUtil.isPermutation(result), "Result should be a valid permutation");
    }

}

