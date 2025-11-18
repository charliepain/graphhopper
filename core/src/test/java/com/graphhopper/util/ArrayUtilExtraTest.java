package com.graphhopper.util;

import com.carrotsearch.hppc.IntArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayUtilExtraTest {
    @Test
    public void testRemoveConsecutiveDuplicatesDupesOnlyAfterEnd() {
//        int[] dupesOnlyAfterEnd = {0, 1, 2, 3, 4, 7, 7, 7};
//        assertEquals(5, ArrayUtil.removeConsecutiveDuplicates(dupesOnlyAfterEnd, 5));
//        assertEquals(IntArrayList.from(0, 1, 2, 3, 4, 7, 7, 7), IntArrayList.from(dupesOnlyAfterEnd));
    }

    @Test
    public void testTransformMapTooSmall() {
//        IntArrayList arrWithLargeNum = IntArrayList.from(1, 2, 3, 4, 9, 3);
//        IntArrayList smallMap = IntArrayList.from(10, 11, 12, 13, 14);
//        assertThrows(Error.class, () -> ArrayUtil.transform(arrWithLargeNum, smallMap));
    }

//    @Test
//    public void testMergeSameElems() {
//        int[] arr1 = {-2, -1, 0, 1, 2};
//        int[] arrWithSameElems = arr1.clone();
//        assertArrayEquals(arr1, ArrayUtil.merge(arr1, arrWithSameElems));
//    }

}
