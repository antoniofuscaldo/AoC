import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day2_2023 {
    // Maximum number of cubes for each color
    private static final int MAX_RED = 12;
    private static final int MAX_GREEN = 13;
    private static final int MAX_BLUE = 14;

    public static void main(String[] args) {
        try {
            String filePath = "input.txt";

            // Part 1: Calculate sum of IDs of possible games
            int possibleGamesSum = calculatePossibleGamesSum(filePath);
            System.out.println("Part 1 - Sum of IDs of possible games: " + possibleGamesSum);

            // Part 2: Calculate sum of power of minimum cube sets
            int powerSum = calculatePowerSum(filePath);
            System.out.println("Part 2 - Sum of power of minimum cube sets: " + powerSum);

        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }

    private static int calculatePossibleGamesSum(String filePath) throws IOException {
        int sum = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Extract game ID and check if the game is possible
                int gameId = extractGameId(line);
                boolean isPossible = isGamePossible(line);

                if (isPossible) {
                    sum += gameId;
                }
            }
        }

        return sum;
    }

    private static int calculatePowerSum(String filePath) throws IOException {
        int sum = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Calculate minimum cubes required and their power
                int power = calculateGamePower(line);
                sum += power;
            }
        }

        return sum;
    }

    private static int extractGameId(String line) {
        // Extract the game ID using regex
        Pattern pattern = Pattern.compile("Game (\\d+):");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0; // Default if no match found
    }

    private static boolean isGamePossible(String line) {
        // Extract the part after "Game X: "
        String[] parts = line.split(": ", 2);
        if (parts.length < 2) {
            return false;
        }

        String gameData = parts[1];
        // Split by semicolon to get each subset
        String[] subsets = gameData.split("; ");

        // Check each subset of cubes
        for (String subset : subsets) {
            // Split each subset into individual cube counts
            String[] cubes = subset.split(", ");

            // Check each cube color count
            for (String cube : cubes) {
                String[] countAndColor = cube.split(" ");
                if (countAndColor.length == 2) {
                    int count = Integer.parseInt(countAndColor[0]);
                    String color = countAndColor[1];

                    // Check if the count exceeds the maximum allowed for the color
                    if (color.equals("red") && count > MAX_RED) {
                        return false;
                    } else if (color.equals("green") && count > MAX_GREEN) {
                        return false;
                    } else if (color.equals("blue") && count > MAX_BLUE) {
                        return false;
                    }
                }
            }
        }

        // If we've checked all subsets and none exceeded the limits, the game is
        // possible
        return true;
    }

    private static int calculateGamePower(String line) {
        // Extract the part after "Game X: "
        String[] parts = line.split(": ", 2);
        if (parts.length < 2) {
            return 0;
        }

        String gameData = parts[1];
        // Split by semicolon to get each subset
        String[] subsets = gameData.split("; ");

        // Track minimum required cubes for each color
        int minRed = 0;
        int minGreen = 0;
        int minBlue = 0;

        // Check each subset of cubes
        for (String subset : subsets) {
            // Split each subset into individual cube counts
            String[] cubes = subset.split(", ");

            // Check each cube color count
            for (String cube : cubes) {
                String[] countAndColor = cube.split(" ");
                if (countAndColor.length == 2) {
                    int count = Integer.parseInt(countAndColor[0]);
                    String color = countAndColor[1];

                    // Update minimum required cubes for each color
                    if (color.equals("red")) {
                        minRed = Math.max(minRed, count);
                    } else if (color.equals("green")) {
                        minGreen = Math.max(minGreen, count);
                    } else if (color.equals("blue")) {
                        minBlue = Math.max(minBlue, count);
                    }
                }
            }
        }

        // Calculate the power (product of minimum cubes required)
        return minRed * minGreen * minBlue;
    }
}