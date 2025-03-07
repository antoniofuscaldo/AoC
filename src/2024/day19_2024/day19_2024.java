import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day19_2024 {
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input.txt"));

            // Process the input
            String[] patterns = {};
            List<String> designs = new ArrayList<>();

            boolean isDesign = false;
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    isDesign = true;
                    continue;
                }

                if (!isDesign) {
                    // Parse available patterns
                    patterns = line.split(", ");
                } else {
                    // Add designs to check
                    designs.add(line.trim());
                }
            }

            // Part 1: Count possible designs
            int possibleCount = 0;
            for (String design : designs) {
                if (canCreateDesignDP(design, patterns)) {
                    possibleCount++;
                }
            }

            System.out.println("Part 1 - Number of possible designs: " + possibleCount);

            // Part 2: Count ways to make each design
            long totalWays = 0;
            for (String design : designs) {
                long ways = countWaysToCreateDesign(design, patterns);
                totalWays += ways;
            }

            System.out.println("Part 2 - Total ways to make all designs: " + totalWays);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean canCreateDesignDP(String design, String[] patterns) {
        int length = design.length();
        boolean[] dp = new boolean[length + 1];

        // Base case: empty string can always be formed
        dp[0] = true;

        // For each position in the design
        for (int i = 0; i < length; i++) {
            // Only proceed if we can reach this position
            if (!dp[i])
                continue;

            // Try each pattern
            for (String pattern : patterns) {
                int patternLength = pattern.length();
                int end = i + patternLength;

                // Check if this pattern can be applied at the current position
                if (end <= length && design.regionMatches(i, pattern, 0, patternLength)) {
                    dp[end] = true;
                }
            }
        }

        // Return true if we can reach the end of the design
        return dp[length];
    }

    private static long countWaysToCreateDesign(String design, String[] patterns) {
        int length = design.length();
        // Using long to handle potentially large number of combinations
        long[] dp = new long[length + 1];

        // Base case: there's one way to form an empty string
        dp[0] = 1;

        // For each position in the design
        for (int i = 0; i < length; i++) {
            // Only proceed if we can reach this position
            if (dp[i] == 0)
                continue;

            // Try each pattern
            for (String pattern : patterns) {
                int patternLength = pattern.length();
                int end = i + patternLength;

                // Check if this pattern can be applied at the current position
                if (end <= length && design.regionMatches(i, pattern, 0, patternLength)) {
                    // Add the number of ways to reach position i to the ways to reach position end
                    dp[end] += dp[i];
                }
            }
        }

        // Return the number of ways to reach the end of the design
        return dp[length];
    }
}