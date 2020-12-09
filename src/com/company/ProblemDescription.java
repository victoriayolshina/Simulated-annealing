package com.company;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;

/**
 * Описание задачи.
 */
class ProblemDescription {
    public BitSet f;
    public BitSet fDefined;
    public int varsCount;
    public int fSize;
    public long negativeVarsRequired;

    public ProblemDescription(BitSet f, BitSet fDefined, int varsCount, long negativeVarsRequired) {
        this.f = f;
        this.fDefined = fDefined;
        this.varsCount = varsCount;
        this.fSize = BinaryTools.pow2(varsCount);
        this.negativeVarsRequired = negativeVarsRequired;
    }

    private static long readLong(FileReader file) throws IOException {
        StringBuilder number = new StringBuilder();
        while (true) {
            char ch = (char)file.read();

            if (ch >= '0' && ch <= '9')
                number.append(ch);
            else
                return Long.parseLong(number.toString());
        }
    }

    public static ProblemDescription fromFile(String filePath) throws IOException {

        System.out.format("%n--Считываю задачу из файла %s...%n", filePath);
        FileReader file = new FileReader(filePath);

        int varsCount = (int)readLong(file);
        int len = BinaryTools.pow2(varsCount);

        BitSet f = new BitSet(len);
        BitSet fDefined = new BitSet(len);

        for (int i = 0; i < len;) {
            char ch = (char)file.read();
            switch (ch) {
                case '0': fDefined.set(i); i++; break;
                case '1': fDefined.set(i); f.set(i); i++; break;
                case '-': i++; break;
            }
        }
        file.read();

        long negativeVarsMin = readLong(file);

        file.close();

        return new ProblemDescription(f, fDefined, varsCount, negativeVarsMin);
    }

    public void save(String filePath) throws IOException {

        System.out.format("%n--Сохраняю задачу в файл %s...%n", filePath);
        FileWriter file = new FileWriter(filePath);

        file.write(varsCount + "\n");

        int lineSize = fSize;
        if (GlobalSettings.splitFunction)
            lineSize = GlobalSettings.splitFunctionOneLineLength;

        for (int count = 0; count < fSize; count += lineSize) {
            for (int i = 0; i < lineSize && i + count < fSize; i++)
                if (fDefined.get(count + i))
                    file.write(f.get(count + i) ? '1' : '0');
                else
                    file.write('-');

            file.write("\n");
        }

        file.write(negativeVarsRequired + "\n");

        file.close();
        System.out.println("Сохранено!");
    }
}
