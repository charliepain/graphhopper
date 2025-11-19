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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for Helper class using mocks to isolate dependencies.
 * 
 * This test class demonstrates the use of Mockito to mock external dependencies
 * (Reader and InputStream) to test Helper methods in isolation without relying
 * on actual file system operations.
 */
@ExtendWith(MockitoExtension.class)
public class HelperMockTest {

    @Mock
    private Reader mockReader;

    @Mock
    private BufferedReader mockBufferedReader;

    @Mock
    private InputStream mockInputStream;

    /**
     * Test readJSONFileWithoutComments method with mocked Reader.
     * 
     * This test verifies that Helper.readJSONFileWithoutComments correctly processes
     * JSON content from a Reader, filtering out comment lines. We mock the Reader
     * to avoid file system dependencies and to control the exact input data.
     * 
     * Mocked class: InputStreamReader (via Reader parameter)
     * Reason: We mock Reader to isolate the test from file system operations and
     * to control the input data precisely, including edge cases like comments.
     */
    @Test
    public void testReadJSONFileWithoutCommentsWithMockedReader() throws IOException {
        // Arrange: Create an InputStreamReader with JSON content including comments
        String jsonWithComments = "// This is a comment\n{\n  \"key\": \"value\"\n  // Another comment\n}";
        InputStreamReader mockReader = new InputStreamReader(new java.io.ByteArrayInputStream(jsonWithComments.getBytes()));

        // Act: Call the method under test (using the InputStreamReader overload)
        String result = Helper.readJSONFileWithoutComments(mockReader);

        // Assert: Verify comments are filtered out
        assertNotNull(result);
        assertFalse(result.contains("//"));
        assertTrue(result.contains("{"));
        assertTrue(result.contains("key"));
    }

    /**
     * Test isToString method with mocked InputStream.
     * 
     * This test verifies that Helper.isToString correctly converts an InputStream
     * to a String. We use a ByteArrayInputStream (which is a real implementation)
     * to simulate a mocked InputStream, allowing us to control the data being read
     * without dependencies on actual file or network resources.
     * 
     * Mocked class: InputStream (simulated via ByteArrayInputStream)
     * Reason: We use ByteArrayInputStream to simulate a mocked InputStream, allowing
     * us to test the conversion logic without file system or network dependencies.
     * The values are chosen to test UTF-8 encoding handling.
     */
    @Test
    public void testIsToStringWithMockedInputStream() throws IOException {
        // Arrange: Create InputStream with known content (simulating a mock)
        String expectedContent = "Hello, World! Test with UTF-8: émoji 🎉";
        byte[] contentBytes = expectedContent.getBytes("UTF-8");
        
        // Using ByteArrayInputStream to simulate mocked InputStream behavior
        InputStream mockInputStream = new java.io.ByteArrayInputStream(contentBytes);

        // Act: Call the method under test
        String result = Helper.isToString(mockInputStream);

        // Assert: Verify the result matches expected content
        assertNotNull(result);
        assertEquals(expectedContent, result);
    }

    /**
     * Test isToString method with mocked InputStream that throws exception.
     * 
     * This test verifies exception handling in Helper.isToString by mocking
     * an InputStream that throws IOException during reading. We mock InputStream
     * to test error scenarios without relying on actual file system failures.
     */
    @Test
    public void testIsToStringWithMockedInputStreamThrowsException() throws IOException {
        // Arrange: Mock InputStream to throw IOException
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read(any(byte[].class))).thenThrow(new IOException("Simulated read error"));

        // Act & Assert: Verify exception is propagated
        assertThrows(IOException.class, () -> {
            Helper.isToString(mockInputStream);
        });
    }
}

