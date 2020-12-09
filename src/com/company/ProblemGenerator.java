package com.company;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Генератор задачи.
 * Создает случайную задачу заданной длины (2^кол-во_переменных)
 */
public class ProblemGenerator {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.print("Введите кол-во переменных: ");
            int n = in.nextInt();

            while (true) {
                ProblemDescription result = generate(n);

                System.out.print("Сохранить? (y, n-перегенерировать, v-задать кол-во переменных, e-выход) ");
                String command = in.next();

                if (command.equals("v")) break;

                switch (command) {
                    case "y": result.save(GlobalSettings.problemFilePath); return;
                    case "n": continue;
                    case "e": return;
                }
            }
        }
    }

    /**
     *
     * @param n - кол-во переменных в функции. Длина функции будет равна 2^n
     * @return Постановка задачи
     */
    public static ProblemDescription generate(int n) {
        long randomSeed = ThreadLocalRandom.current().nextLong();
        Random rnd = new Random(randomSeed);
        System.out.format("--Random seed: %d%n", randomSeed);

        int len = BinaryTools.pow2(n);

        BitSet f = new BitSet(len);
        BitSet fDefined = new BitSet(len);

        System.out.println("--Генерирую функцию...");
        for (int i = 0; i < len; i++)
            f.set(i, rnd.nextBoolean());

        int definedCount = rnd.nextInt(len);

        System.out.println("--Определяю кол-во известных компонентов...");
        for (int i = 0; i < definedCount; i++)
            fDefined.set(i, true);

        if (GlobalSettings.shuffleNotDefinedBits) {
            System.out.println("--Перемешиваю неизвестные компоненты...");
            BitSetTools.shuffle(fDefined, len, rnd);
        }

        System.out.println("--Определяю кол-во отрицательных переменных...");
        for (int i = 0; i < len; i++)
            if (!fDefined.get(i))
                f.set(i, false);

        long negativeVarsCount = BinaryTools.getNegativeCount(f, len);

        System.out.println("--Определяю максимальное кол-во отрицательных переменных...");
        long maxNegativeVarsCount = negativeVarsCount + BinaryTools.getNegativeCount(fDefined, len, false);

        long randomLong = rnd.nextLong();
        if (randomLong < 0) randomLong = -randomLong;

        long negativeVarsRequired = randomLong % (maxNegativeVarsCount - negativeVarsCount + 1) + negativeVarsCount;

        System.out.println();
        System.out.println("Сгенерировано!");

        if (len <= GlobalSettings.maxLenToShow) {
            System.out.print("Функция: ");
            BinaryTools.printF(f, fDefined, len);
        }

        System.out.format("Длина: %d%n", len);
        System.out.format("Определено: %d (%.2f)%n", definedCount, (double)definedCount / len);
        System.out.format("Не определено: %d (%.2f)%n", len - definedCount, (double)(len - definedCount) / len);
        System.out.format("Возможное кол-во переменных с отрицанием: [%d, %d]%n", negativeVarsCount, maxNegativeVarsCount);
        System.out.format("Искать решение: %d%n%n", negativeVarsRequired);

        return new ProblemDescription(f, fDefined, n, negativeVarsRequired);
    }
}