import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day8p1_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        int rows = lines.size();
        int cols = lines.get(0).length();

        Map<Character, List<int[]>> antennaGroups = new HashMap<>();
        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);
                if (ch != '.') {
                    antennaGroups.computeIfAbsent(ch, k -> new ArrayList<>()).add(new int[] { r, c });
                }
            }
        }

        Set<String> antinodePositions = new HashSet<>();

        for (Map.Entry<Character, List<int[]>> entry : antennaGroups.entrySet()) {
            List<int[]> positions = entry.getValue();
            int n = positions.size();
            if (n < 2)
                continue;
            for (int i = 0; i < n; i++) {
                int[] posA = positions.get(i);
                for (int j = i + 1; j < n; j++) {
                    int[] posB = positions.get(j);

                    int r1 = 2 * posB[0] - posA[0];
                    int c1 = 2 * posB[1] - posA[1];
                    if (inBounds(r1, c1, rows, cols)) {
                        antinodePositions.add(r1 + "," + c1);
                    }

                    int r2 = 2 * posA[0] - posB[0];
                    int c2 = 2 * posA[1] - posB[1];
                    if (inBounds(r2, c2, rows, cols)) {
                        antinodePositions.add(r2 + "," + c2);
                    }
                }
            }
        }

        System.out.println(antinodePositions.size());
    }

    private static boolean inBounds(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }
}
