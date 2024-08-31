import java.util.Comparator;
import java.util.Scanner;
import java.util.Arrays;

public class Elpriser {
    private static final int[] prices = new int[24];  // Array to store prices for 24 hours
    private static boolean pricesEntered = false;  // Flag to track if prices have been entered

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice.toLowerCase()) {
                case "1":
                    inputPrices(scanner);
                    break;
                case "2":
                    printMinMaxAvg();
                    break;
                case "3":
                    sortPrices();
                    break;
                case "4":
                    findBestChargingTime();
                    break;
                case "e":
                    running = false;
                    System.out.println("Programmet avslutas.");
                    break;
                default:
                    System.out.println("Ogiltigt val, försök igen.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("Elpriser");
        System.out.println("========");
        System.out.println("1. Inmatning");
        System.out.println("2. Min, Max och Medel");
        System.out.println("3. Sortera");
        System.out.println("4. Bästa Laddningstid (4h)");
        System.out.println("e. Avsluta");
        System.out.print("Välj ett alternativ: ");
    }

    private static void inputPrices(Scanner scanner) {
        System.out.println("Mata in elpriserna för varje timme (i öre):");
        for (int i = 0; i < 24; i++) {
            while (true) {
                try {
                    System.out.printf("Pris för timme %02d-%02d: ", i, i + 1);
                    prices[i] = Integer.parseInt(scanner.nextLine().trim());
                    if (prices[i] < 0) {
                        System.out.println("Priset kan inte vara negativt. Försök igen.");
                    } else {
                        pricesEntered = true;  // Set the flag to true when prices are entered
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ogiltigt värde, skriv in ett heltal.");
                }
            }
        }
    }

    private static void printMinMaxAvg() {
        if (!pricesEntered) {
            System.out.println("Inga priser har matats in ännu.");
            return;
        }

        int minPrice = Integer.MAX_VALUE;
        int maxPrice = Integer.MIN_VALUE;
        int sum = 0;
        int minHour = 0, maxHour = 0;

        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < minPrice) {
                minPrice = prices[i];
                minHour = i;
            }
            if (prices[i] > maxPrice) {
                maxPrice = prices[i];
                maxHour = i;
            }
            sum += prices[i];
        }

        double average = sum / (double) prices.length;

        System.out.println("Min pris: " + minPrice + " öre vid timme " + minHour + "-" + (minHour + 1));
        System.out.println("Max pris: " + maxPrice + " öre vid timme " + maxHour + "-" + (maxHour + 1));
        System.out.printf("Medelpris: %.2f öre%n", average);
    }

    private static void sortPrices() {
        if (!pricesEntered) {
            System.out.println("Inga priser har matats in ännu.");
            return;
        }

        // Create an array of indices to sort prices without altering the original data
        Integer[] indices = new Integer[prices.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, Comparator.comparingInt(a -> prices[a]));


        for (int index : indices) {
            System.out.printf("%02d-%02d: %d öre%n", index, index + 1, prices[index]);
        }
    }

    private static void findBestChargingTime() {
        if (!pricesEntered) {
            System.out.println("Inga priser har matats in ännu.");
            return;
        }

        int bestStartHour = 0;
        int minTotalPrice = Integer.MAX_VALUE;

        for (int i = 0; i <= prices.length - 4; i++) {
            int totalPrice = 0;
            for (int j = i; j < i + 4; j++) {
                totalPrice += prices[j];
            }
            if (totalPrice < minTotalPrice) {
                minTotalPrice = totalPrice;
                bestStartHour = i;
            }
        }

        double averagePrice = minTotalPrice / 4.0;
        System.out.printf("Bästa tid att börja ladda: %02d-%02d%n", bestStartHour, bestStartHour + 4);
        System.out.printf("Medelpris under dessa 4 timmar: %.2f öre%n", averagePrice);
    }
}
