package com.graphhopper.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HelperExtraTest {
    @Test
    public void testPruneFileEndTwoDots() {
        String fileNameTwoDots = "c_program.c.txt";
        assertEquals("c_program.c", Helper.pruneFileEnd(fileNameTwoDots));
    }

    @Test
    public void testKeepInExceedsMax() {
        int biggerThanMax = 9;
        int max = 5;
        assertEquals(max, Helper.keepIn(3, max, biggerThanMax));
    }

}
