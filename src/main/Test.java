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
    private static final String test0 = "test00_etapa3_wrapped_one_user_one_artist.json";
    private static final String test1 = "test01_etapa3_wrapped_one_user_n_artist.json";
    private static final String test2 = "test02_etapa3_wrapped_n_user_one_artist.json";
    private static final String test3 = "test03_etapa3_wrapped_n_user_n_artist.json";
    private static final String test9 = "test09_etapa3_merch_buy.json";
    private static final String test10 = "test10_etapa3_wrapped_host.json";

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
            String fileName = test9;
            for (File file : inputDir) {
                if (file.getName().equalsIgnoreCase(fileName)) {
                    Main.action(fileName, CheckerConstants.OUT_FILE);
                    break;
                }
            }
        }
    }
}
