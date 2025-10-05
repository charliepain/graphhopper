package com.graphhopper.util;

import com.carrotsearch.hppc.IntArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Additional test cases for utility classes that need more comprehensive edge case coverage.
 * This file contains 7 additional test cases for BitUtil, ArrayUtil, and AngleCalc classes
 * covering areas not already tested in their existing test files.
 * 
 * @author Test Enhancement
 */
public class EdgeCaseTests {

    // ==================== BITUTIL TESTS ====================

    /**
     * Test Case 1: BitUtil - Error Handling for Invalid Input Parameters
     * 
     * Nom du test: testBitUtilErrorHandlingForInvalidInputParameters
     * Intention: Test BitUtil.countBitValue method behavior with invalid negative input parameters
     * Motivation: Existing tests only cover positive values but don't verify that negative values 
     *             properly throw IllegalArgumentException as documented in the method
     * Oracle: Verify that IllegalArgumentException is thrown with appropriate message for negative input
     */
    @Test
    public void testBitUtilErrorHandlingForInvalidInputParameters() {
        // Test negative input to countBitValue
        assertThrows(IllegalArgumentException.class, () -> BitUtil.countBitValue(-1));
        assertThrows(IllegalArgumentException.class, () -> BitUtil.countBitValue(-100));
        assertThrows(IllegalArgumentException.class, () -> BitUtil.countBitValue(Integer.MIN_VALUE));
        
        // Verify the exception message contains the expected information
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> BitUtil.countBitValue(-5));
        assertTrue(exception.getMessage().contains("maxTurnCosts cannot be negative"));
        assertTrue(exception.getMessage().contains("-5"));
    }

    /**
     * Test Case 2: BitUtil - Edge Cases for Bit String Operations with Empty and Null-like Inputs
     * 
     * Nom du test: testBitUtilEdgeCasesForBitStringOperationsWithEmptyAndNullLikeInputs
     * Intention: Test BitUtil bit string operations with edge cases like empty strings, single characters, 
     *             and boundary values not covered in existing tests
     * Motivation: Existing tests use moderate-length bit strings but don't test edge cases like empty strings,
     *             single bit operations, or very long bit strings that could cause issues
     * Oracle: Verify that bit string operations handle edge cases correctly and return expected results
     */
    @Test
    public void testBitUtilEdgeCasesForBitStringOperationsWithEmptyAndNullLikeInputs() {
        BitUtil bitUtil = BitUtil.LITTLE;
        
        // Test empty bit string
        byte[] emptyBytes = bitUtil.fromBitString("");
        assertEquals(0, emptyBytes.length);
        
        // Test single bit strings
        byte[] singleBit = bitUtil.fromBitString("1");
        assertEquals(1, singleBit.length);
        assertEquals("10000000", bitUtil.toBitString(singleBit));
        
        // Test very long bit string (64+ bits)
        String longBitString = "1".repeat(100);
        byte[] longBytes = bitUtil.fromBitString(longBitString);
        assertTrue(longBytes.length > 8); // Should be more than 8 bytes for 100 bits
        
        // Test toLastBitString with edge cases
        assertEquals("", bitUtil.toLastBitString(0L, 0));
        assertEquals("0", bitUtil.toLastBitString(0L, 1));
        assertEquals("1", bitUtil.toLastBitString(1L, 1));
    }

    // ==================== ARRAYUTIL TESTS ====================

    /**
     * Test Case 3: ArrayUtil - Error Handling for Invalid Array Operations
     * 
     * Nom du test: testArrayUtilErrorHandlingForInvalidArrayOperations
     * Intention: Test ArrayUtil methods with invalid parameters that should throw exceptions
     * Motivation: Existing tests cover normal operations but don't verify error handling for invalid
     *             parameters like negative indices, mismatched array sizes, or invalid ranges
     * Oracle: Verify that appropriate exceptions are thrown for invalid operations
     */
    @Test
    public void testArrayUtilErrorHandlingForInvalidArrayOperations() {
        // Test removeConsecutiveDuplicates with negative end parameter
        int[] arr = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.removeConsecutiveDuplicates(arr, -1));
        
        // Test calcSortOrder with mismatched array lengths
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5}; // Shorter array
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.calcSortOrder(arr1, arr2, 3));
        
        // Test applyOrder with order longer than array
        int[] shortArray = {1, 2};
        int[] longOrder = {0, 1, 2}; // Longer order
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.applyOrder(shortArray, longOrder));
        
        // Test invert with invalid array elements
        int[] invalidArray = {0, 1, 5}; // Element 5 >= array length 3
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayUtil.invert(invalidArray));
    }

    /**
     * Test Case 4: ArrayUtil - Edge Cases for Array Manipulation with Boundary Values
     * 
     * Nom du test: testArrayUtilEdgeCasesForArrayManipulationWithBoundaryValues
     * Intention: Test ArrayUtil array manipulation methods with boundary values and edge cases
     * Motivation: Existing tests cover normal cases but don't test boundary conditions like empty arrays,
     *             single-element arrays, or arrays with extreme values
     * Oracle: Verify that array manipulation methods handle boundary cases correctly
     */
    @Test
    public void testArrayUtilEdgeCasesForArrayManipulationWithBoundaryValues() {
        // Test constant with size 0
        // valid
        IntArrayList emptyConstant = ArrayUtil.constant(0, 5);
        assertEquals(0, emptyConstant.size());
        
        // Test range with start == end (empty range)
        IntArrayList emptyRange = ArrayUtil.range(5, 5);
        assertEquals(0, emptyRange.size());
        
        // Test rangeClosed with start == end (single element)
        IntArrayList singleRange = ArrayUtil.rangeClosed(5, 5);
        assertEquals(1, singleRange.size());
        assertEquals(5, singleRange.get(0));
        
        // Test reverse with single element
        IntArrayList singleElement = IntArrayList.from(42);
        IntArrayList reversed = ArrayUtil.reverse(singleElement);
        assertEquals(1, reversed.size());
        assertEquals(42, reversed.get(0));
        
        // Test merge with one empty array
        int[] empty = {};
        int[] nonEmpty = {1, 2, 3};
        int[] merged = ArrayUtil.merge(empty, nonEmpty);
        assertArrayEquals(nonEmpty, merged);
    }

    /**
     * Test Case 5: ArrayUtil - Complex Array Operations with Duplicate and Edge Data
     * 
     * Nom du test: testArrayUtilComplexArrayOperationsWithDuplicateAndEdgeData
     * Intention: Test ArrayUtil complex operations with arrays containing duplicates, negative values,
     *             and edge data patterns not covered in existing tests
     * Motivation: Existing tests use simple data patterns but don't test complex scenarios like
     *             all duplicates, alternating patterns, or negative values
     * Oracle: Verify that complex array operations handle edge data correctly
     */
    @Test
    public void testArrayUtilComplexArrayOperationsWithDuplicateAndEdgeData() {
        // Test removeConsecutiveDuplicates with all same elements
        // valid
        int[] allSame = {5, 5, 5, 5, 5};
        int newSize = ArrayUtil.removeConsecutiveDuplicates(allSame, allSame.length);
        assertEquals(1, newSize);
        assertEquals(5, allSame[0]);
        
        // Test withoutConsecutiveDuplicates with negative values
        IntArrayList withNegatives = IntArrayList.from(-3, -3, -1, 0, 0, 2, 2, 2);
        IntArrayList noDups = new IntArrayList(ArrayUtil.withoutConsecutiveDuplicates(withNegatives));
        assertEquals(IntArrayList.from(-3, -1, 0, 2), noDups);
        
        // Test isPermutation with negative values (should return false)
        IntArrayList withNegative = IntArrayList.from(-1, 0, 1);
        assertFalse(ArrayUtil.isPermutation(withNegative));
        
        // Test isPermutation with values >= size (should return false)
        IntArrayList tooLarge = IntArrayList.from(0, 1, 5); // 5 >= size 3
        assertFalse(ArrayUtil.isPermutation(tooLarge));
    }

    // ==================== ANGLECALC TESTS ====================

    /**
     * Test Case 6: AngleCalc - Error Handling for Invalid Azimuth Values
     * 
     * Nom du test: testAngleCalcErrorHandlingForInvalidAzimuthValues
     * Intention: Test AngleCalc.convertAzimuth2xaxisAngle method with invalid azimuth values
     * Motivation: Existing tests only cover valid azimuth ranges but don't verify that invalid
     *             values (negative, > 360) properly throw IllegalArgumentException
     * Oracle: Verify that IllegalArgumentException is thrown with appropriate message for invalid azimuth
     */
    @Test
    public void testAngleCalcErrorHandlingForInvalidAzimuthValues() {
        AngleCalc angleCalc = AngleCalc.ANGLE_CALC;
        
        // Test negative azimuth
        assertThrows(IllegalArgumentException.class, () -> angleCalc.convertAzimuth2xaxisAngle(-1));
        assertThrows(IllegalArgumentException.class, () -> angleCalc.convertAzimuth2xaxisAngle(-90));
        
        // Test azimuth > 360
        assertThrows(IllegalArgumentException.class, () -> angleCalc.convertAzimuth2xaxisAngle(361));
        assertThrows(IllegalArgumentException.class, () -> angleCalc.convertAzimuth2xaxisAngle(450));
        
        // Test boundary values (should be valid)
        assertDoesNotThrow(() -> angleCalc.convertAzimuth2xaxisAngle(0));
        assertDoesNotThrow(() -> angleCalc.convertAzimuth2xaxisAngle(360));
        
        // Verify exception message
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> angleCalc.convertAzimuth2xaxisAngle(-45));
        assertTrue(exception.getMessage().contains("Azimuth -45 must be in (0, 360)"));
    }

    /**
     * Test Case 7: AngleCalc - Edge Cases for Orientation Calculations with Extreme Coordinates
     * 
     * Nom du test: testAngleCalcEdgeCasesForOrientationCalculationsWithExtremeCoordinates
     * Intention: Test AngleCalc orientation and azimuth calculations with extreme coordinate values
     *             and edge cases not covered in existing tests
     * Motivation: Existing tests use moderate coordinate values but don't test edge cases like
     *             identical points, very small differences, or extreme coordinate values
     * Oracle: Verify that orientation calculations handle extreme coordinates correctly and remain stable
     */
    @Test
    public void testAngleCalcEdgeCasesForOrientationCalculationsWithExtremeCoordinates() {
        AngleCalc angleCalc = AngleCalc.ANGLE_CALC;
        
        // Test identical points (should handle gracefully)
        double orientation = angleCalc.calcOrientation(0, 0, 0, 0);
        assertTrue(Double.isFinite(orientation));
        
        // Test very small coordinate differences
        double tinyDiff = angleCalc.calcOrientation(0, 0, 0.000001, 0.000001);
        assertTrue(Double.isFinite(tinyDiff));
        
        // Test extreme coordinate values
        double extremeOrientation = angleCalc.calcOrientation(89, 179, -89, -179);
        assertTrue(Double.isFinite(extremeOrientation));
        
        // Test azimuth calculation with identical points
        double azimuth = angleCalc.calcAzimuth(0, 0, 0, 0);
        assertTrue(Double.isFinite(azimuth));
        
        // Test azimuth calculation with very small differences
        double tinyAzimuth = angleCalc.calcAzimuth(0, 0, 0.000001, 0.000001);
        assertTrue(Double.isFinite(tinyAzimuth));
        
        // Test alignOrientation with extreme values
        double aligned = angleCalc.alignOrientation(Math.PI, -Math.PI);
        assertTrue(Double.isFinite(aligned));
        
        // Test isClockwise with collinear points (edge case)
        boolean clockwise = angleCalc.isClockwise(0, 0, 1, 1, 2, 2);
        // Collinear points should return false (not clockwise)
        assertFalse(clockwise);
    }
}
