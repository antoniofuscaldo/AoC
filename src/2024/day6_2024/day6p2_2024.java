import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day6p2_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];

        int startRow = -1, startCol = -1;
        int startDir = -1;
        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            for (int j = 0; j < cols; j++) {
                char ch = line.charAt(j);
                grid[i][j] = ch;
                if (ch == '^' || ch == 'v' || ch == '<' || ch == '>') {
                    startRow = i;
                    startCol = j;
                    if (ch == '^')
                        startDir = 0;
                    else if (ch == '>')
                        startDir = 1;
                    else if (ch == 'v')
                        startDir = 2;
                    else if (ch == '<')
                        startDir = 3;
                    grid[i][j] = '.';
                }
            }
        }

        int validCandidateCount = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == startRow && j == startCol)
                    continue;
                if (grid[i][j] == '#')
                    continue;

                if (simulateWithExtraObstacle(grid, rows, cols, startRow, startCol, startDir, i, j)) {
                    validCandidateCount++;
                }
            }
        }

        System.out.println(validCandidateCount);
    }

    private static boolean simulateWithExtraObstacle(char[][] grid, int rows, int cols,
            int startRow, int startCol, int startDir,
            int candRow, int candCol) {
        int[] dr = { -1, 0, 1, 0 };
        int[] dc = { 0, 1, 0, -1 };

        int r = startRow, c = startCol, d = startDir;
        Set<String> seenStates = new HashSet<>();

        while (true) {
            String state = r + "," + c + "," + d;
            if (seenStates.contains(state)) {
                return true;
            }
            seenStates.add(state);

            int nr = r + dr[d];
            int nc = c + dc[d];

            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                return false;
            }

            if (grid[nr][nc] == '#' || (nr == candRow && nc == candCol)) {
                d = (d + 1) % 4;
            } else {
                r = nr;
                c = nc;
            }
        }
    }
}
