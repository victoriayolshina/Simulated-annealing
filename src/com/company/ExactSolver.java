package com.company;

import java.io.IOException;
import java.util.*;

/**
 * Решает задачу за линейное время. На практике проверялся на функциях длины до 2^30
 */
public class ExactSolver {

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        ProblemDescription problem = ProblemDescription.fromFile(GlobalSettings.problemFilePath);

        ProblemSolution solution = solve(problem);

        if (GlobalSettings.checkSolution)
            solution.check();

        System.out.println("Решение найдено!");
        if (GlobalSettings.maxLenToShow <= solution.fSize)
            BinaryTools.printF(solution.f, solution.fSize);
        System.out.printf("Необходимое кол-во отрицательных переменных: %d%n", problem.negativeVarsRequired);
        System.out.printf("Найденное кол-во отрицательных переммных: %d%n", solution.negativeVarsCount);
        System.out.printf("Разница: %d%n", Math.abs(problem.negativeVarsRequired - solution.negativeVarsCount));

        System.out.print("\nСохранить решение? (y/n) ");

        if (in.next().equals("y"))
            solution.save(GlobalSettings.solutionFilePath);
    }

    public static ProblemSolution solve(ProblemDescription problem) {
        int len = problem.fSize;
        int varsCount = problem.varsCount;
        BitSet f = problem.f;
        BitSet fDefined = problem.fDefined;
        long negativeVarsRequired = problem.negativeVarsRequired;

        long[] negativeCountSorted = new long[problem.varsCount + 1];

        System.out.println("--Сортирую функцию по кол-ву отрицательных переменных...");
        for (int i = 0; i < len; i++)
            if (!fDefined.get(i)) {
                int negativeCount = BinaryTools.getNegativeCount(i, len);
                negativeCountSorted[negativeCount]++;
            }

        long negativeVarsNotDefinedMax = 0;
        for (int i = 0; i < negativeCountSorted.length; i++)
            negativeVarsNotDefinedMax += negativeCountSorted[i] * i;

        long negativeVarsNow = BinaryTools.getNegativeCount(f, len);

        long negativeVarsNeed = negativeVarsRequired - negativeVarsNow;

        System.out.println("--Вычисляю решение...");
        long[] negativeCountTaken = new long[problem.varsCount + 1];
        int negativeCountTakenSum = 0;
        for (int i = varsCount; i > 0; i--) {
            long stillNeed = negativeVarsNeed - negativeCountTakenSum;
            // если можно взять всё, то берем все
            if ((negativeCountSorted[i]) * i <= stillNeed) {
                negativeCountTakenSum += (negativeCountSorted[i]) * i;
                negativeCountTaken[i] = negativeCountSorted[i];
            }
            // иначе, берем максимум, который можем взять
            else {
                long taken = stillNeed / i;
                negativeCountTakenSum += i * taken;
                negativeCountTaken[i] = taken;
            }
        }

        System.out.println("--Форматирую найденное решение...");
        for (int i = 0; i < len; i++)
            if (!fDefined.get(i)) {
                int negativeCount = BinaryTools.getNegativeCount(i, len);
                if (negativeCountTaken[negativeCount] > 0) {
                    negativeCountTaken[negativeCount]--;
                    f.set(i);
                }
            }

        return new ProblemSolution(f, negativeCountTakenSum + negativeVarsNow, varsCount);
    }
}
