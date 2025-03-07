import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class day7p2_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        long calibrationSum = 0;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split(":");
            if (parts.length < 2)
                continue;

            long target = Long.parseLong(parts[0].trim());
            String[] numberTokens = parts[1].trim().split("\\s+");
            int n = numberTokens.length;
            long[] numbers = new long[n];
            for (int i = 0; i < n; i++) {
                numbers[i] = Long.parseLong(numberTokens[i].trim());
            }

            int opCount = n - 1;

            int totalCombos = 1;
            for (int i = 0; i < opCount; i++) {
                totalCombos *= 3;
            }

            boolean validEquation = false;

            for (int combo = 0; combo < totalCombos; combo++) {
                long result = numbers[0];
                int temp = combo;
                for (int op = 0; op < opCount; op++) {
                    int operator = temp % 3;
                    temp /= 3;
                    if (operator == 0) {
                        result = result + numbers[op + 1];
                    } else if (operator == 1) {
                        result = result * numbers[op + 1];
                    } else if (operator == 2) {
                        result = concat(result, numbers[op + 1]);
                    }
                }
                if (result == target) {
                    validEquation = true;
                    break;
                }
            }

            if (validEquation) {
                calibrationSum += target;
            }
        }

        System.out.println(calibrationSum);
    }

    private static long concat(long a, long b) {
        long multiplier = 1;
        long temp = b;
        if (temp == 0) {
            multiplier = 10;
        } else {
            while (temp > 0) {
                multiplier *= 10;
                temp /= 10;
            }
        }
        return a * multiplier + b;
    }
}
