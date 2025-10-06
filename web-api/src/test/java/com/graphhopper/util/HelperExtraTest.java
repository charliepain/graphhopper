package com.graphhopper.util;

import org.junit.jupiter.api.Test;
import com.github.javafaker.Faker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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

    @Test
    public void testCamelCaseToUnderScoreWithFaker() {
        int NUM_CASES = 50;
        Faker faker = new Faker(new Random(42));
        ArrayList<String> camelCaseNames = new ArrayList<>();
        ArrayList<String> underScoreNames = new ArrayList<>();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("camelCaseToUnderScore.txt"));
            writer.write("camelCaseNames, underScoreNames");
            writer.newLine();

            for (int i = 0; i < NUM_CASES; i++) {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();

                // Certains noms generes par java faker contiennent des majuscules sans etre
                // precedees par un espace. On s'assure que seulement la premiere lettre du nom est
                // majuscule.
                firstName = firstName.toLowerCase();
                firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1);
                lastName = lastName.toLowerCase();
                lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1);

                String name = firstName + " " + lastName;

                String lowerFirstLetterName = name.substring(0,1).toLowerCase() + name.substring(1);
                String camelCaseName = lowerFirstLetterName.replaceAll(" ","");
                camelCaseNames.add(camelCaseName);

                String underScoreName = name.toLowerCase().replaceAll(" ","_");
                underScoreNames.add(underScoreName);

                writer.append(String.format("%s, %s", camelCaseName, underScoreName));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < NUM_CASES; i++) {
            assertEquals(underScoreNames.get(i), Helper.camelCaseToUnderScore(camelCaseNames.get(i)));
        }
    }

}
