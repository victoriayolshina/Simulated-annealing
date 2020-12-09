package com.company;

import java.util.BitSet;
import java.util.Random;

/**
 * Вспомогательный класс.
 * Класс для работы с BitSet
 */
public class BitSetTools {

    public static void swap(BitSet arr, int i, int j) {
        boolean temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    public static void shuffle(BitSet arr, int size, Random rnd) {
        for (int i = size; i > 1; i--)
            swap(arr, i-1, rnd.nextInt(i));
    }
}
