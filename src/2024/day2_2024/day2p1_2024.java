import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class day2p1_2024 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));
        int safeCount = 0;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] tokens = line.split("\\s+");
            if (tokens.length == 0)
                continue;

            int[] levels = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                levels[i] = Integer.parseInt(tokens[i]);
            }
            if (isSafeReport(levels)) {
                safeCount++;
            }
        }

        System.out.println(safeCount);
    }

    private static boolean isSafeReport(int[] levels) {
        if (levels.length == 0)
            return false;
        if (levels.length == 1)
            return true;

        int diff = levels[1] - levels[0];

        if (Math.abs(diff) < 1 || Math.abs(diff) > 3)
            return false;

        boolean increasing = (diff > 0);

        for (int i = 1; i < levels.length; i++) {
            int d = levels[i] - levels[i - 1];
            if (Math.abs(d) < 1 || Math.abs(d) > 3) {
                return false;
            }
            if ((increasing && d <= 0) || (!increasing && d >= 0)) {
                return false;
            }
        }
        return true;
    }
}
