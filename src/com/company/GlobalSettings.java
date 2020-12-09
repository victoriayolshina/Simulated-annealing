package com.company;

/**
 * Глобальные настройки программы.
 */
public class GlobalSettings {
    // загружать задачу из
    public static String problemFilePath = "input.txt";
    // сохранять решение в
    public static String solutionFilePath = "output.txt";

    // Проверять корректность решения
    public static boolean checkSolution = true;

    // Выводить функцию на экран если её длина не превышает
    public static int maxLenToShow = 1024;

    // false - записать всю функцию в одну строку при сохранении в файл, true - разбить на несколько строк
    public static boolean splitFunction = true;
    // количество символов в одной строке
    public static int splitFunctionOneLineLength = 128;

    // ProblemGenerator
    // Перемешать неизвестные биты (true: 10-01-1- ; false: ---10100)
    public static boolean shuffleNotDefinedBits = true;
}
