import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class day7p1_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        long calibrationSum = 0;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split(":");
            long target = Long.parseLong(parts[0].trim());
            String[] numberTokens = parts[1].trim().split("\\s+");
            int n = numberTokens.length;
            long[] numbers = new long[n];
            for (int i = 0; i < n; i++) {
                numbers[i] = Long.parseLong(numberTokens[i].trim());
            }

            boolean validEquation = false;
            int opCount = n - 1;
            int totalCombinations = 1 << opCount;
            for (int combo = 0; combo < totalCombinations; combo++) {
                long result = numbers[0];
                for (int op = 0; op < opCount; op++) {
                    if (((combo >> op) & 1) == 0) {
                        result = result + numbers[op + 1];
                    } else {
                        result = result * numbers[op + 1];
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
}
