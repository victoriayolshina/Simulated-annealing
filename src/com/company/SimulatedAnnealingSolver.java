package com.company;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Решает задачу методом  симуляции отжига
 */
public class SimulatedAnnealingSolver {
    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);

        ProblemDescription problem = ProblemDescription.fromFile(GlobalSettings.problemFilePath);

        while (true) {
            System.out.print("\nВведите кол-во итераций: ");
            int iters = in.nextInt();
            System.out.print("Введите минимальную температуру: ");
            double tmin = in.nextDouble();
            System.out.print("Введите максимальную температуру: ");
            double tmax = in.nextDouble();

            while (true) {

                ProblemSolution solution = solve(problem, iters, tmin, tmax);

                if (GlobalSettings.checkSolution)
                    solution.check();

                System.out.println("Решение найдено!");
                if (GlobalSettings.maxLenToShow <= solution.fSize)
                    BinaryTools.printF(solution.f, solution.fSize);
                System.out.printf("Необходимое кол-во отрицательных переменных: %d%n", problem.negativeVarsRequired);
                System.out.printf("Найденное кол-во отрицательных переммных: %d%n", solution.negativeVarsCount);
                System.out.printf("Разница: %d%n", Math.abs(problem.negativeVarsRequired - solution.negativeVarsCount));

                System.out.print("\nСохранить решение? (y, n-заново, p-задать параметры, e-выход) ");

                String command = in.next();

                if (command.equals("p")) break;

                switch (command) {
                    case "y": solution.save(GlobalSettings.solutionFilePath); return;
                    case "n": continue;
                    case "e": return;
                }
            }
        }
    }

    public static ProblemSolution solve(ProblemDescription problem, int iters, double tmin, double tmax) {

        int len = problem.fSize;
        int varsCount = problem.varsCount;
        BitSet f = problem.f;
        BitSet fDefined = problem.fDefined;
        long negativeVarsRequired = problem.negativeVarsRequired;

        long randomSeed = ThreadLocalRandom.current().nextLong();
        Random rnd = new Random(randomSeed);
        System.out.format("--Random seed: %d%n", randomSeed);

        List<Integer> notDefined = new ArrayList<>(len);

        for (int i = 0; i < len; i++)
            if (!fDefined.get(i)) {
                notDefined.add(i);
                f.set(i, rnd.nextBoolean());
            }

        int notDefinedSize = notDefined.size();

        long negativeVarsCount = BinaryTools.getNegativeCount(f, len);

        System.out.println("--Вычисляю решение...");
        double t = tmax;
        double tStep = (tmax - tmin) / iters;
        for (int i = 0; i < iters; i++, t -= tStep) {

            // Количество бит, меняющихся за итерацию
            int bitsCount = (int)(notDefinedSize * 0.05);// (int)t;
            List<Integer> changedBits = new ArrayList<>(bitsCount);

            long zOld = Math.abs(negativeVarsRequired - negativeVarsCount);

            for (int j = 0; j < bitsCount; j++) {
                int index = notDefined.get(rnd.nextInt(notDefinedSize));
                boolean oldValue = f.get(index);
                changedBits.add(index);
                f.set(index, !oldValue);
                negativeVarsCount += BinaryTools.getNegativeCount(index, len) * (oldValue ? -1 : 1);
            }

            long zNew = Math.abs(negativeVarsRequired - negativeVarsCount);

            if (zNew == 0)
                break;

            long deltaZ = zNew - zOld;

            // вероятность перехода в новое состояние
            double p = deltaZ <= 0 ? 1 : 0;
            //double p = deltaZ <= 0 ? 1 : Math.pow(Math.E, -((double)deltaZ / t)) * 0.5;

            // откат решения
            if (rnd.nextDouble() > p) {
                int size = changedBits.size();
                for (int index : changedBits) {
                    boolean value = f.get(index);
                    f.set(index, !value);
                    negativeVarsCount += BinaryTools.getNegativeCount(index, len) * (value ? -1 : 1);
                }
            }
        }

        return new ProblemSolution(f, negativeVarsCount, varsCount);
    }
}
