import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class day6p1_2024 {
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
        int direction = -1;
        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            for (int j = 0; j < cols; j++) {
                char ch = line.charAt(j);
                grid[i][j] = ch;
                if (ch == '^' || ch == 'v' || ch == '<' || ch == '>') {
                    startRow = i;
                    startCol = j;
                    if (ch == '^') {
                        direction = 0;
                    } else if (ch == '>') {
                        direction = 1;
                    } else if (ch == 'v') {
                        direction = 2;
                    } else if (ch == '<') {
                        direction = 3;
                    }
                    grid[i][j] = '.';
                }
            }
        }

        int[] dr = { -1, 0, 1, 0 };
        int[] dc = { 0, 1, 0, -1 };

        Set<String> visited = new HashSet<>();
        visited.add(startRow + "," + startCol);

        int currentRow = startRow;
        int currentCol = startCol;

        while (true) {
            int nextRow = currentRow + dr[direction];
            int nextCol = currentCol + dc[direction];

            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                break;
            }

            if (grid[nextRow][nextCol] == '#') {
                direction = (direction + 1) % 4;
                continue;
            }

            currentRow = nextRow;
            currentCol = nextCol;
            visited.add(currentRow + "," + currentCol);
        }

        System.out.println(visited.size());
    }
}
