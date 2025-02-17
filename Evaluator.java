
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Represents an operation that can be performed on a landscape.
 * This class is used to encapsulate the type and argument of an operation.
 * 
 * @author Maharshi Gor, Rohan Bhatnagar
 */
class Operation {
    int type; // Type of operation (1 for getFirst, 2 for remove, 3 for insert)
    Integer arg; // Argument for the operation (null for getFirst and remove, height for insert)

    /**
     * Constructs an Operation object with the given type and argument.
     * 
     * @param type The type of operation.
     * @param arg  The argument for the operation.
     */
    Operation(int type, Integer arg) {
        this.type = type;
        this.arg = arg;
    }

    /**
     * Returns a string representation of the operation.
     * 
     * @return A string indicating the operation type and argument.
     */
    public String toString() {
        if (type == 1) {
            return "Op:[getFirst]";
        } else if (type == 2) {
            return "Op:[remove]";
        } else if (type == 3) {
            return "Op:[insert " + arg + "]";
        } else if (type == 4) {
            return "Op:[getTotalTreasure]";
        }
        return "Op:[Invalid]";
    }
}

/**
 * Represents a test case for evaluating the ValleyTraveler class.
 * This class is used to encapsulate the landscape, operations, and expected
 * results.
 * 
 * @author Maharshi Gor, Rohan Bhatnagar
 */
class TestCase {
    int[] landscape; // The initial landscape
    Operation[] operations; // The operations to be performed on the landscape
    double[] expected; // The expected results after performing the operations

    /**
     * Constructs a TestCase object with the given landscape, operations, and
     * expected results.
     * 
     * @param landscape  The initial landscape.
     * @param operations The operations to be performed on the landscape.
     * @param expected   The expected results after performing the operations.
     */
    TestCase(int[] landscape, Operation[] operations, double[] expected) {
        this.landscape = landscape;
        this.operations = operations;
        this.expected = expected;
    }

    /**
     * Constructs a TestCase object by reading from a file.
     * 
     * File format:
     * N (number of elements in the landscape)
     * [landscape with N elements separated by spaces]
     * M (number of operations)
     * [ith operation (one per line): 1 for getFirst, 2 for remove, "3 val" for
     * insert(val)]
     * K (number of expected results)
     * [expected results separated by spaces]
     * 
     * @param filepath The path to the file containing the test case.
     */
    TestCase(String filepath) {
        try {
            Scanner scanner = new Scanner(new File(filepath));
            int N = scanner.nextInt();
            landscape = new int[N];
            for (int i = 0; i < N; i++) {
                landscape[i] = scanner.nextInt();
            }
            int M = scanner.nextInt();
            operations = new Operation[M];
            for (int i = 0; i < M; i++) {
                int type = scanner.nextInt();
                if (type == 1) {
                    operations[i] = new Operation(type, null);
                } else if (type == 2) {
                    operations[i] = new Operation(type, null);
                } else if (type == 3) {
                    operations[i] = new Operation(type, scanner.nextInt());
                } else if (type == 4) {
                    operations[i] = new Operation(type, null);
                }
            }
            int K = scanner.nextInt();
            expected = new double[K];
            for (int i = 0; i < K; i++) {
                expected[i] = scanner.nextDouble();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Testcase file not found: " + filepath);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a string representation of the test case.
     * 
     * @return A string indicating the landscape, operations, and expected results.
     */
    public String toString() {
        String result = "Landscape[" + landscape.length + "]:{" + Arrays.toString(landscape) + "}\n";
        result += "Operations[" + operations.length + "]:{\n";
        for (Operation op : operations) {
            result += "  " + op.toString() + "\n";
        }
        result += "}\n";
        result += "Expected[" + expected.length + "]:{" + Arrays.toString(expected) + "}\n";
        return result;
    }
}

/**
 * Evaluates the ValleyTraveler class by running test cases.
 * 
 * @author Maharshi Gor, Rohan Bhatnagar
 */
public class Evaluator {
    /**
     * Executes the operations on the initial landscape and returns the results.
     * 
     * @param initLandscape The initial landscape.
     * @param operations    The operations to be performed on the landscape.
     * @return An array of results after performing the operations.
     */
    public static double[] getResults(int[] initLandscape, Operation[] operations) {
        ValleyTraveler valleyTraveler = new ValleyTraveler(initLandscape);

        // Count the number of operations that are not insert
        int resultSize = 0;
        for (Operation op : operations) {
            if (op.type != 3) {
                resultSize++;
            }
        }
        double[] result = new double[resultSize];
        int i = 0;
        for (Operation op : operations) {
            if (op.type == 1) {
                double firstValley = valleyTraveler.getFirst();
                result[i++] = firstValley;
            } else if (op.type == 2) {
                double removedValley = valleyTraveler.remove();
                result[i++] = removedValley;
            } else if (op.type == 3) {
                valleyTraveler.insert(op.arg);
            } else if (op.type == 4) {
                double totalTreasure = valleyTraveler.getTotalTreasure();
                result[i++] = totalTreasure;
            }
        }
        return result;
    }

    /**
     * Runs a test case and checks if the results match the expected results.
     * 
     * @param testCase The test case to be run.
     * @return True if the test case passes, false otherwise.
     */
    public static boolean runTestCase(TestCase testCase) {
        double[] results = getResults(testCase.landscape, testCase.operations);
        boolean passed = true;
        // Check if the results array has the same length as the expected array
        if (results.length != testCase.expected.length) {
            System.out
                    .println("Test failed: results array length does not match expected array length. Expected length: "
                            + testCase.expected.length + ", but got " + results.length);
            return false;
        }
        double epsilon = 1e-6;
        for (int i = 0; i < testCase.expected.length; i++) {
            if (Math.abs(results[i] - testCase.expected[i]) > epsilon) {
                System.out.println("Test failed at operation " + i + ": expected " + testCase.expected[i] + " but got "
                        + results[i]);
                passed = false;
            }
            // if (results[i] != testCase.expected[i]) {
            //     System.out.println("Test failed at operation " + i + ": expected " + testCase.expected[i] + " but got "
            //             + results[i]);
            //     passed = false;
            // }
        }
        return passed;
    }

    /**
     * Main method to run the Evaluator.
     * 
     * @param args The command line arguments. The first argument should be the path
     *             to the test case file.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No testcase file provided");
            return;
        } else {
            for (String filepath : args) {
                TestCase testCase = new TestCase(filepath);
                System.out.println(testCase.toString());
                boolean passed = runTestCase(testCase);
                if (passed)
                    System.out.println("Test passed!");
            }
        }
    }
}
