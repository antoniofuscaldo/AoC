import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class day4_2023 {
    public static void main(String[] args) {
        try {
            // Part 1: Calculate total points
            int totalPoints = calculateTotalPoints("input.txt");
            System.out.println("Part 1 - Total points: " + totalPoints);

            // Part 2: Calculate total scratchcards
            int totalCards = calculateTotalScratchcards("input.txt");
            System.out.println("Part 2 - Total scratchcards: " + totalCards);

        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }

    private static int calculateTotalPoints(String filename) throws IOException {
        int totalPoints = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalPoints += calculateCardPoints(line);
            }
        }

        return totalPoints;
    }

    private static int calculateCardPoints(String card) {
        // Remove the "Card X:" prefix and split into winning numbers and your numbers
        String[] parts = card.substring(card.indexOf(":") + 1).split("\\|");

        // Parse winning numbers
        Set<Integer> winningNumbers = parseNumbers(parts[0]);

        // Parse your numbers
        Set<Integer> yourNumbers = parseNumbers(parts[1]);

        // Find matches and calculate points
        int matches = countMatches(winningNumbers, yourNumbers);

        // Calculate points (1 point for first match, then doubled for each additional
        // match)
        return matches > 0 ? (int) Math.pow(2, matches - 1) : 0;
    }

    private static int calculateTotalScratchcards(String filename) throws IOException {
        // Read all cards first
        List<String> cards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cards.add(line);
            }
        }

        // Initialize card counts (start with 1 of each card)
        int[] cardCounts = new int[cards.size()];
        for (int i = 0; i < cardCounts.length; i++) {
            cardCounts[i] = 1;
        }

        // Process each card
        for (int i = 0; i < cards.size(); i++) {
            int matches = countMatchesForCard(cards.get(i));

            // For each copy of current card, add copies of subsequent cards
            for (int j = 0; j < matches; j++) {
                int nextCardIndex = i + j + 1;
                if (nextCardIndex < cards.size()) {
                    cardCounts[nextCardIndex] += cardCounts[i];
                }
            }
        }

        // Sum up all cards
        int totalCards = 0;
        for (int count : cardCounts) {
            totalCards += count;
        }

        return totalCards;
    }

    private static int countMatchesForCard(String card) {
        // Remove the "Card X:" prefix and split into winning numbers and your numbers
        String[] parts = card.substring(card.indexOf(":") + 1).split("\\|");

        // Parse winning numbers
        Set<Integer> winningNumbers = parseNumbers(parts[0]);

        // Parse your numbers
        Set<Integer> yourNumbers = parseNumbers(parts[1]);

        // Return the number of matches
        return countMatches(winningNumbers, yourNumbers);
    }

    private static Set<Integer> parseNumbers(String numbersStr) {
        // Split by spaces and convert to integers, adding to a set
        Set<Integer> numbers = new HashSet<>();
        String[] numberStrings = numbersStr.trim().split("\\s+");

        for (String numStr : numberStrings) {
            if (!numStr.isEmpty()) {
                numbers.add(Integer.parseInt(numStr));
            }
        }

        return numbers;
    }

    private static int countMatches(Set<Integer> winningNumbers, Set<Integer> yourNumbers) {
        // Count how many of your numbers are in the winning numbers
        int count = 0;
        for (int num : yourNumbers) {
            if (winningNumbers.contains(num)) {
                count++;
            }
        }
        return count;
    }
}