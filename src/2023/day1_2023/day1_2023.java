import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day1_2023 {

    public static void main(String[] args) {
        try {
            String filePath = "input.txt";

            // Part 1: Only consider numeric digits
            int sumPart1 = calculateCalibrationSum(filePath, false);
            System.out.println("Part 1 Answer: " + sumPart1);

            // Part 2: Consider both numeric digits and spelled-out numbers
            int sumPart2 = calculateCalibrationSum(filePath, true);
            System.out.println("Part 2 Answer: " + sumPart2);

        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }

    private static int calculateCalibrationSum(String filePath, boolean includeWords) throws IOException {
        int sum = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int calibrationValue = getCalibrationValue(line, includeWords);
                sum += calibrationValue;
            }
        }

        return sum;
    }

    private static int getCalibrationValue(String line, boolean includeWords) {
        // For part 1, find first and last digit
        if (!includeWords) {
            String digits = line.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) {
                return 0;
            }
            char firstDigit = digits.charAt(0);
            char lastDigit = digits.charAt(digits.length() - 1);
            return Integer.parseInt("" + firstDigit + lastDigit);
        }

        // For part 2, find first and last digit (including spelled-out numbers)
        Map<String, Integer> wordToDigit = createWordToDigitMap();

        // Create a pattern to match both digits and spelled-out numbers
        // Using lookahead (?=...) to handle overlapping matches like "oneight"
        String wordPattern = "(?=([0-9]|one|two|three|four|five|six|seven|eight|nine))";
        Pattern pattern = Pattern.compile(wordPattern);
        Matcher matcher = pattern.matcher(line);

        String firstMatch = null;
        String lastMatch = null;

        while (matcher.find()) {
            // Group 1 contains the actual match
            String match = matcher.group(1);
            if (firstMatch == null) {
                firstMatch = match;
            }
            lastMatch = match;
        }

        // Convert words to digits
        int first = (firstMatch.length() == 1) ? Integer.parseInt(firstMatch) : wordToDigit.get(firstMatch);
        int last = (lastMatch.length() == 1) ? Integer.parseInt(lastMatch) : wordToDigit.get(lastMatch);

        return first * 10 + last;
    }

    private static Map<String, Integer> createWordToDigitMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        map.put("five", 5);
        map.put("six", 6);
        map.put("seven", 7);
        map.put("eight", 8);
        map.put("nine", 9);
        return map;
    }
}