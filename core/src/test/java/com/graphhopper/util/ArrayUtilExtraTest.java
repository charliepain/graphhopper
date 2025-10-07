package com.graphhopper.util;

import com.carrotsearch.hppc.IntArrayList;
import com.github.javafaker.Faker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
import java.lang.Math;

public class ArrayUtilExtraTest {

    @Test
    public void testRemoveConsecutiveDuplicatesDupesOnlyAfterEnd() {
        int[] dupesOnlyAfterEnd = {0, 1, 2, 3, 4, 7, 7, 7};
        assertEquals(5, ArrayUtil.removeConsecutiveDuplicates(dupesOnlyAfterEnd, 5));
        assertEquals(IntArrayList.from(0, 1, 2, 3, 4, 7, 7, 7), IntArrayList.from(dupesOnlyAfterEnd));
    }

    @Test
    public void testTransformMapTooSmall() {
        IntArrayList arrWithLargeNum = IntArrayList.from(1, 2, 3, 4, 9, 3);
        IntArrayList smallMap = IntArrayList.from(10, 11, 12, 13, 14);
        assertThrows(Error.class, () -> ArrayUtil.transform(arrWithLargeNum, smallMap));
    }

    @Test
    public void testMergeSameElems() {
        int[] arr1 = {-2, -1, 0, 1, 2};
        int[] arrWithSameElems = arr1.clone();
        assertArrayEquals(arr1, ArrayUtil.merge(arr1, arrWithSameElems));
    }

    @Test
    public void testShuffleWithJavaFaker() {
        Faker faker = new Faker();
        
        int randomSize = faker.number().numberBetween(5, 21);
        
        // Créer une liste avec des valeurs aléatoires
        IntArrayList originalList = new IntArrayList(randomSize);
        for (int i = 0; i < randomSize; i++) {

            int randomValue = faker.number().numberBetween(-100, 101);
            originalList.add(randomValue);
        }
        
        // Créer une copie pour vérification
        IntArrayList originalCopy = new IntArrayList(originalList);
        
        // Utiliser un Random avec seed fixe pour reproductibilité
        Random fixedRandom = new Random(12345);
        
        // Exécuter shuffle
        IntArrayList shuffledList = ArrayUtil.shuffle(originalList, fixedRandom);
        
        // Vérifications
        assertEquals(randomSize, shuffledList.size(), "La taille doit rester la même après shuffle");
        
        // Vérifier que tous les éléments originaux sont présents
        for (int i = 0; i < originalCopy.size(); i++) {
            int originalValue = originalCopy.get(i);
            assertTrue(shuffledList.contains(originalValue), 
                "La liste mélangée doit contenir l'élément " + originalValue + " de la liste originale");
        }
        
        // Vérifier que la liste a été modifiée 
        // Note: Ce test peut parfois échouer si le shuffle produit le même ordre par hasard
        boolean orderChanged = false;
        for (int i = 0; i < Math.min(originalCopy.size(), shuffledList.size()); i++) {
            if (originalCopy.get(i) != shuffledList.get(i)) {
                orderChanged = true;
                break;
            }
        }
        
        if (randomSize >= 5) {
            assertTrue(orderChanged, "Avec des données aléatoires, l'ordre devrait changer après shuffle");
        }
    }

}
