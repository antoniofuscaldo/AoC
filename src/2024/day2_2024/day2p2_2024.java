import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class day2p2_2024 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));
        int safeCount = 0;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] tokens = line.split("\\s+");
            int n = tokens.length;
            int[] levels = new int[n];
            for (int i = 0; i < n; i++) {
                levels[i] = Integer.parseInt(tokens[i]);
            }

            if (isSafeReport(levels) || canBeMadeSafe(levels)) {
                safeCount++;
            }
        }

        System.out.println(safeCount);
    }

    private static boolean isSafeReport(int[] levels) {
        if (levels.length <= 1)
            return true;

        int firstDiff = levels[1] - levels[0];
        if (Math.abs(firstDiff) < 1 || Math.abs(firstDiff) > 3) {
            return false;
        }
        boolean increasing = firstDiff > 0;

        for (int i = 1; i < levels.length; i++) {
            int diff = levels[i] - levels[i - 1];
            int absDiff = Math.abs(diff);
            if (absDiff < 1 || absDiff > 3) {
                return false;
            }
            if (increasing && diff <= 0)
                return false;
            if (!increasing && diff >= 0)
                return false;
        }

        return true;
    }

    private static boolean canBeMadeSafe(int[] levels) {
        for (int i = 0; i < levels.length; i++) {
            int[] newLevels = new int[levels.length - 1];
            int idx = 0;
            for (int j = 0; j < levels.length; j++) {
                if (j == i)
                    continue;
                newLevels[idx++] = levels[j];
            }
            if (isSafeReport(newLevels)) {
                return true;
            }
        }
        return false;
    }
}
