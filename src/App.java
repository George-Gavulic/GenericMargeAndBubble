import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class App {

    // Generic method to create a random array of any Comparable type
    public static <T extends Comparable<T>> T[] createRandomArray(int arrayLength, Class<T> clazz) {
        Random rand = new Random();
        T[] array = (T[]) java.lang.reflect.Array.newInstance(clazz, arrayLength);
        for (int i = 0; i < arrayLength; i++) {
            if (clazz == Integer.class) {
                array[i] = (T) Integer.valueOf(rand.nextInt(101));
            } else if (clazz == Double.class) {
                array[i] = (T) Double.valueOf(rand.nextDouble() * 100);
            } else if (clazz == String.class) {
                array[i] = (T) generateRandomString();
            }
        }
        return array;
    }

    // Generic bubble sort method
    public static <T extends Comparable<T>> void bubbleSort(T[] array, String filename) {
        long startTime = System.currentTimeMillis();
        boolean swapped;
        for (int i = 0; i < array.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j].compareTo(array[j + 1]) > 0) {
                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }
            writeArrayToFile(array, filename, "Bubble Sort Step " + (i + 1) + ":\n");
            if (!swapped) {
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        writeArrayToFile(array, filename, "Total Bubble Sort Time: " + (endTime - startTime) + " ms\n");
    }

    // Generic merge sort method
    public static <T extends Comparable<T>> void mergeSort(T[] array, String filename) {
        long startTime = System.currentTimeMillis();
        mergeSortHelper(array, 0, array.length - 1, filename);
        long endTime = System.currentTimeMillis();
        writeArrayToFile(array, filename, "Total Merge Sort Time: " + (endTime - startTime) + " ms\n");
    }

    // Helper method for merge sort
    private static <T extends Comparable<T>> void mergeSortHelper(T[] array, int left, int right, String filename) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortHelper(array, left, mid, filename);
            mergeSortHelper(array, mid + 1, right, filename);
            merge(array, left, mid, right, filename);
        }
    }

    // Merge method for merge sort
    private static <T extends Comparable<T>> void merge(T[] array, int left, int mid, int right, String filename) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        T[] L = (T[]) new Comparable[n1];
        T[] R = (T[]) new Comparable[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i].compareTo(R[j]) <= 0) {
                array[k++] = L[i++];
            } else {
                array[k++] = R[j++];
            }
        }
        while (i < n1) {
            array[k++] = L[i++];
        }
        while (j < n2) {
            array[k++] = R[j++];
        }

        writeArrayToFile(array, filename, "Merge Step (left: " + left + ", mid: " + mid + ", right: " + right + "):\n");
    }

    // Method to write array to file
    public static <T> void writeArrayToFile(T[] array, String filename, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(message);
            for (T value : array) {
                writer.write(value + " ");
            }
            writer.write("\n\n"); // Add a newline for better separation
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to generate random strings for String type array
    private static String generateRandomString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    // Main method to test the sorting with generic types
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the length of the array: ");
        int length = scanner.nextInt();

        System.out.print("Enter the type of array (1 for Integer, 2 for Double, 3 for String): ");
        int type = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over

        System.out.print("Enter a filename (or press Enter to skip): ");
        String filename = scanner.nextLine();

        if (type == 1) {
            Integer[] randomArray = createRandomArray(length, Integer.class);
            processArray(randomArray, filename);
        } else if (type == 2) {
            Double[] randomArray = createRandomArray(length, Double.class);
            processArray(randomArray, filename);
        } else if (type == 3) {
            String[] randomArray = createRandomArray(length, String.class);
            processArray(randomArray, filename);
        } else {
            System.out.println("Invalid type selected.");
        }

        scanner.close();
    }

    // Method to process and print sorted arrays
    private static <T extends Comparable<T>> void processArray(T[] randomArray, String filename) {
        System.out.println("Generated Array: ");
        for (T value : randomArray) {
            System.out.print(value + " ");
        }
        System.out.println();

        if (!filename.isEmpty()) {
            writeArrayToFile(randomArray, filename, "Initial Array:\n");
            System.out.println("Array written to " + filename);

            T[] bubbleArray = randomArray.clone();
            T[] mergeArray = randomArray.clone();

            bubbleSort(bubbleArray, filename);
            mergeSort(mergeArray, filename);

            System.out.println("Final Bubble Sorted Array: ");
            for (T value : bubbleArray) {
                System.out.print(value + " ");
            }
            System.out.println();

            System.out.println("Final Merge Sorted Array: ");
            for (T value : mergeArray) {
                System.out.print(value + " ");
            }
            System.out.println();
        } else {
            System.out.println("Filename not provided. Skipping file operations.");
        }
    }
}
