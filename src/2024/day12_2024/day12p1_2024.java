import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day12p1_2024 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        if (lines.isEmpty()) {
            System.out.println("0");
            return;
        }
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        boolean[][] visited = new boolean[rows][cols];
        long totalPrice = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!visited[i][j]) {
                    Region region = getRegion(grid, visited, i, j);
                    totalPrice += (long) region.area * region.perimeter;
                }
            }
        }
        System.out.println(totalPrice);
    }

    static class Region {
        int area;
        int perimeter;

        Region(int area, int perimeter) {
            this.area = area;
            this.perimeter = perimeter;
        }
    }

    private static Region getRegion(char[][] grid, boolean[][] visited, int i, int j) {
        int area = 0;
        int perimeter = 0;
        int rows = grid.length;
        int cols = grid[0].length;
        char letter = grid[i][j];
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[] { i, j });
        visited[i][j] = true;
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int r = cell[0], c = cell[1];
            area++;
            for (int[] d : directions) {
                int nr = r + d[0], nc = c + d[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    perimeter++;
                } else if (grid[nr][nc] != letter) {
                    perimeter++;
                } else {
                    if (!visited[nr][nc]) {
                        visited[nr][nc] = true;
                        stack.push(new int[] { nr, nc });
                    }
                }
            }
        }
        return new Region(area, perimeter);
    }
}
