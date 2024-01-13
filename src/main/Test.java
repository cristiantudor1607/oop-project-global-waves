package main;

import checker.CheckerConstants;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Use this if you want to test on a specific input file
 */
public final class Test {
    private static final String test15 = "test15_etapa3_complex.json";

    /**
     * for coding style
     */
    private Test() {
    }

    /**
     * @param args input files
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        File[] inputDir = directory.listFiles();

        if (inputDir != null) {
            Arrays.sort(inputDir);

//            Scanner scanner = new Scanner(System.in);
//            String fileName = scanner.next();
            String fileName = test15;
            for (File file : inputDir) {
                if (file.getName().equalsIgnoreCase(fileName)) {
                    Main.action(fileName, CheckerConstants.OUT_FILE);
                    break;
                }
            }
        }
    }
}
