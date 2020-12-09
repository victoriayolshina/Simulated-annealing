package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;


/**
 * Решение задачи
 */
public class ProblemSolution {
    public BitSet f;
    public long negativeVarsCount;
    public int varsCount;
    public int fSize;

    public ProblemSolution(BitSet f, long negativeVarsCount, int varsCount) {
        this.f = f;
        this.negativeVarsCount = negativeVarsCount;
        this.varsCount = varsCount;
        this.fSize = BinaryTools.pow2(varsCount);
    }

    /**
     * Проверяет, равно ли кол-во отрицательных переменных в функции значению negativeVarsCount
     * @return true - ok, false - алгоритм решения неправильно работает
     */
    public boolean check() {
        System.out.println("--Проверяю решение...");
        boolean correct = BinaryTools.getNegativeCount(f, fSize) == negativeVarsCount;
        if (correct) System.out.println("Решение верно!");
        else System.out.println("Ошибка! Решение неверно!");
        return correct;
    }

    public void save(String filePath) throws IOException {

        System.out.format("%n--Сохраняю решение в файл %s...%n", filePath);
        FileWriter file = new FileWriter(filePath);

        file.write(varsCount + "\n");

        int lineSize = fSize;
        if (GlobalSettings.splitFunction)
            lineSize = GlobalSettings.splitFunctionOneLineLength;

        for (int count = 0; count < fSize; count += lineSize) {
            for (int i = 0; i < lineSize && i + count < fSize; i++)
                file.write(f.get(count + i) ? '1' : '0');

            file.write("\n");
        }

        file.write(negativeVarsCount + "\n");

        file.close();
        System.out.println("Сохранено!");
    }
}
