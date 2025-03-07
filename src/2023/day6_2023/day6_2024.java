import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day6_2024 {
    public static void main(String[] args) {
        try {
            // Read input from file
            List<String> lines = readInput("input.txt");

            // Parse times and distances
            List<Long> times = parseNumbers(lines.get(0));
            List<Long> distances = parseNumbers(lines.get(1));

            // Calculate ways to win for each race and multiply them for Part 1
            long part1Result = 1;
            for (int i = 0; i < times.size(); i++) {
                long waysToWin = calculateWaysToWin(times.get(i), distances.get(i));
                part1Result *= waysToWin;
            }

            System.out.println("Part 1: " + part1Result);

            // Part Two: Treat the numbers as a single race (ignoring spaces)
            long singleTime = parseSingleNumber(lines.get(0));
            long singleDistance = parseSingleNumber(lines.get(1));
            long part2Result = calculateWaysToWin(singleTime, singleDistance);

            System.out.println("Part 2: " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static List<String> readInput(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static List<Long> parseNumbers(String line) {
        List<Long> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            numbers.add(Long.parseLong(matcher.group()));
        }

        return numbers;
    }

    private static long parseSingleNumber(String line) {
        // Extract all digits and concatenate them to form a single number
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            sb.append(matcher.group());
        }

        return Long.parseLong(sb.toString());
    }

    private static long calculateWaysToWin(long time, long recordDistance) {
        // For each hold time (h), the distance traveled is: h * (time - h)
        // We need to find all h values where h * (time - h) > recordDistance

        // This can be solved with a quadratic equation: -h² + time*h - recordDistance >
        // 0
        // Using the quadratic formula to find the roots
        double discriminant = time * time - 4 * recordDistance;

        // If discriminant is negative, there are no solutions
        if (discriminant < 0) {
            return 0;
        }

        // Calculate the two roots
        double sqrtDiscriminant = Math.sqrt(discriminant);
        double lowerBound = (time - sqrtDiscriminant) / 2;
        double upperBound = (time + sqrtDiscriminant) / 2;

        // Find the integer solutions in the range (lowerBound, upperBound)
        long minHoldTime = (long) Math.ceil(lowerBound);
        if (minHoldTime * (time - minHoldTime) == recordDistance) {
            // If the minimum equals the record exactly, we need to beat it, not tie it
            minHoldTime++;
        }

        long maxHoldTime = (long) Math.floor(upperBound);
        if (maxHoldTime * (time - maxHoldTime) == recordDistance) {
            // If the maximum equals the record exactly, we need to beat it, not tie it
            maxHoldTime--;
        }

        // The number of ways to win is the number of integers in the range
        // [minHoldTime, maxHoldTime]
        return maxHoldTime - minHoldTime + 1;
    }
}