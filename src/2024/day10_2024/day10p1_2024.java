import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day10p1_2024 {
    static class Point {
        int r, c;

        Point(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Point))
                return false;
            Point that = (Point) o;
            return this.r == that.r && this.c == that.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r, c);
        }

        @Override
        public String toString() {
            return "(" + r + "," + c + ")";
        }
    }

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
        @SuppressWarnings("unchecked")
        Set<Point>[][] memo = new HashSet[m][n];
        int totalScore = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    Set<Point> reachableNines = dfs(grid, i, j, memo);
                    totalScore += reachableNines.size();
                }
            }
        }
        System.out.println(totalScore);
    }

    static Set<Point> dfs(int[][] grid, int i, int j, Set<Point>[][] memo) {
        if (memo[i][j] != null) {
            return memo[i][j];
        }
        Set<Point> result = new HashSet<>();
        int current = grid[i][j];
        if (current == 9) {
            result.add(new Point(i, j));
            memo[i][j] = result;
            return result;
        }
        int nextVal = current + 1;
        int m = grid.length;
        int n = grid[0].length;
        int[] dr = { -1, 1, 0, 0 };
        int[] dc = { 0, 0, -1, 1 };
        for (int d = 0; d < 4; d++) {
            int ni = i + dr[d];
            int nj = j + dc[d];
            if (ni < 0 || ni >= m || nj < 0 || nj >= n)
                continue;
            if (grid[ni][nj] == nextVal) {
                result.addAll(dfs(grid, ni, nj, memo));
            }
        }
        memo[i][j] = result;
        return result;
    }
}
