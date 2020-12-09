package com.company;

import java.util.BitSet;

/**
 * Вспомогательный класс.
 * Класс для работы с бинарными функциями
 */
public class BinaryTools {
    public static int pow2(int n) {
        int value = 1;
        for (int i = 1; i <= n; i++) {
            value *= 2;
        }
        return value;
    }

    public static int getNegativeCount(int n, int len) {
        int count = 0;
        int temp = n;

        for (int i = len/2; i > 0; i /= 2) {
            if (temp < i)
                count++;
            else
                temp -= i;
        }

        return count;
    }

    public static long getNegativeCount(BitSet f, int len, boolean value) {
        long count = 0;

        for (int i = 0; i < len; i++)
            if (f.get(i) == value)
                count += getNegativeCount(i, len);

        return count;
    }

    public static long getNegativeCount(BitSet f, int len) {
        return getNegativeCount(f, len, true);
    }

    public static void printF(BitSet f, BitSet fDefined, int len) {
        for (int i = 0; i < len; i++)
            System.out.print(fDefined.get(i) ? (f.get(i) ? '1' : '0') : '-');
        System.out.println();
    }
    public static void printF(BitSet f, int len) {
        for (int i = 0; i < len; i++)
            System.out.print(f.get(i) ? '1' : '0');
        System.out.println();
    }
}
