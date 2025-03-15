import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class day7_2023 {
    // Card values from highest to lowest (Part 1)
    private static final String CARD_VALUES_PART1 = "AKQJT98765432";
    // Card values for Part 2 (J is now lowest)
    private static final String CARD_VALUES_PART2 = "AKQT98765432J";

    public static void main(String[] args) {
        List<Hand> handsPart1 = new ArrayList<>();
        List<Hand> handsPart2 = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                String cards = parts[0];
                int bid = Integer.parseInt(parts[1]);
                handsPart1.add(new Hand(cards, bid, false)); // Part 1 rules
                handsPart2.add(new Hand(cards, bid, true)); // Part 2 rules with jokers
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }

        // Part 1
        Collections.sort(handsPart1);
        long totalWinningsPart1 = calculateWinnings(handsPart1);
        System.out.println("Part 1 - Total winnings: " + totalWinningsPart1);

        // Part 2
        Collections.sort(handsPart2);
        long totalWinningsPart2 = calculateWinnings(handsPart2);
        System.out.println("Part 2 - Total winnings: " + totalWinningsPart2);
    }

    private static long calculateWinnings(List<Hand> hands) {
        long totalWinnings = 0;
        for (int i = 0; i < hands.size(); i++) {
            int rank = i + 1; // Rank starts at 1 for the weakest hand
            totalWinnings += hands.get(i).bid * rank;
        }
        return totalWinnings;
    }

    // Hand class represents a hand of cards with its bid
    static class Hand implements Comparable<Hand> {
        String cards;
        int bid;
        boolean useJokerRules;

        public Hand(String cards, int bid, boolean useJokerRules) {
            this.cards = cards;
            this.bid = bid;
            this.useJokerRules = useJokerRules;
        }

        // Get the type of the hand (Five of a kind, Four of a kind, etc.)
        private int getType() {
            if (!useJokerRules) {
                return getTypeWithoutJokers();
            } else {
                return getTypeWithJokers();
            }
        }

        // Get type without considering jokers (Part 1 rules)
        private int getTypeWithoutJokers() {
            // Count occurrences of each card
            Map<Character, Integer> counts = new HashMap<>();
            for (char c : cards.toCharArray()) {
                counts.put(c, counts.getOrDefault(c, 0) + 1);
            }

            return calculateHandType(counts);
        }

        // Get type considering J as jokers (Part 2 rules)
        private int getTypeWithJokers() {
            // Count occurrences of each card
            Map<Character, Integer> counts = new HashMap<>();
            for (char c : cards.toCharArray()) {
                counts.put(c, counts.getOrDefault(c, 0) + 1);
            }

            // If no jokers or all jokers, proceed normally
            int jokerCount = counts.getOrDefault('J', 0);
            if (jokerCount == 0 || jokerCount == 5) {
                return calculateHandType(counts);
            }

            // Remove jokers from the counts
            counts.remove('J');

            // Find the most frequent card to replace jokers with
            char mostFrequentCard = ' ';
            int maxFrequency = 0;

            for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
                if (entry.getValue() > maxFrequency) {
                    maxFrequency = entry.getValue();
                    mostFrequentCard = entry.getKey();
                } else if (entry.getValue() == maxFrequency) {
                    // If tied, choose the higher card value
                    String cardValues = useJokerRules ? CARD_VALUES_PART2 : CARD_VALUES_PART1;
                    if (cardValues.indexOf(entry.getKey()) < cardValues.indexOf(mostFrequentCard)) {
                        mostFrequentCard = entry.getKey();
                    }
                }
            }

            // Add jokers to the most frequent card
            counts.put(mostFrequentCard, counts.get(mostFrequentCard) + jokerCount);

            return calculateHandType(counts);
        }

        // Calculate hand type from card counts
        private int calculateHandType(Map<Character, Integer> counts) {
            if (counts.size() == 1) {
                return 7; // Five of a kind
            } else if (counts.size() == 2) {
                // Either Four of a kind or Full house
                if (counts.containsValue(4)) {
                    return 6; // Four of a kind
                } else {
                    return 5; // Full house
                }
            } else if (counts.size() == 3) {
                // Either Three of a kind or Two pair
                if (counts.containsValue(3)) {
                    return 4; // Three of a kind
                } else {
                    return 3; // Two pair
                }
            } else if (counts.size() == 4) {
                return 2; // One pair
            } else {
                return 1; // High card
            }
        }

        @Override
        public int compareTo(Hand other) {
            // First compare by type
            int typeComparison = Integer.compare(this.getType(), other.getType());
            if (typeComparison != 0) {
                return typeComparison;
            }

            // If same type, compare cards in order
            for (int i = 0; i < cards.length(); i++) {
                char thisCard = cards.charAt(i);
                char otherCard = other.cards.charAt(i);

                if (thisCard != otherCard) {
                    // Lower index in CARD_VALUES means higher card value
                    String cardValues = useJokerRules ? CARD_VALUES_PART2 : CARD_VALUES_PART1;
                    return Integer.compare(
                            cardValues.indexOf(otherCard),
                            cardValues.indexOf(thisCard));
                }
            }

            // Hands are identical
            return 0;
        }
    }
}