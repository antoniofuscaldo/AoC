import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class day4p1_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        String word = "XMAS";
        int count = 0;

        int[][] directions = {
                { 0, 1 },
                { 0, -1 },
                { 1, 0 },
                { -1, 0 },
                { 1, 1 },
                { 1, -1 },
                { -1, 1 },
                { -1, -1 }
        };

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'X') {
                    for (int[] dir : directions) {
                        int dr = dir[0];
                        int dc = dir[1];
                        boolean found = true;

                        for (int k = 0; k < word.length(); k++) {
                            int rr = r + dr * k;
                            int cc = c + dc * k;
                            if (rr < 0 || rr >= rows || cc < 0 || cc >= cols) {
                                found = false;
                                break;
                            }
                            if (grid[rr][cc] != word.charAt(k)) {
                                found = false;
                                break;
                            }
                        }

                        if (found) {
                            count++;
                        }
                    }
                }
            }
        }

        System.out.println(count);
    }
}
