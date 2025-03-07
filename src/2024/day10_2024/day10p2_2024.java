import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class day10p2_2024 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        int m = lines.size();
        int n = lines.get(0).length();
        int[][] grid = new int[m][n];

        for (int i = 0; i < m; i++) {
            String line = lines.get(i);
            for (int j = 0; j < n; j++) {
                grid[i][j] = line.charAt(j) - '0';
            }
        }

        long[][] memo = new long[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(memo[i], -1);
        }

        long totalRating = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    totalRating += countPaths(grid, i, j, memo);
                }
            }
        }
        System.out.println(totalRating);
    }

    static long countPaths(int[][] grid, int i, int j, long[][] memo) {
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        int m = grid.length;
        int n = grid[0].length;
        int current = grid[i][j];
        if (current == 9) {
            memo[i][j] = 1;
            return 1;
        }
        long ways = 0;
        int[] dr = { -1, 1, 0, 0 };
        int[] dc = { 0, 0, -1, 1 };
        for (int d = 0; d < 4; d++) {
            int ni = i + dr[d];
            int nj = j + dc[d];
            if (ni < 0 || ni >= m || nj < 0 || nj >= n) {
                continue;
            }
            if (grid[ni][nj] == current + 1) {
                ways += countPaths(grid, ni, nj, memo);
            }
        }
        memo[i][j] = ways;
        return ways;
    }
}
