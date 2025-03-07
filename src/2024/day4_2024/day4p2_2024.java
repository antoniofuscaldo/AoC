import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class day4p2_2024 {
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

        int count = 0;
        for (int i = 0; i <= rows - 3; i++) {
            for (int j = 0; j <= cols - 3; j++) {
                if (grid[i + 1][j + 1] != 'A') {
                    continue;
                }

                char d1_first = grid[i][j];
                char d1_second = grid[i + 2][j + 2];

                char d2_first = grid[i][j + 2];
                char d2_second = grid[i + 2][j];

                boolean diag1Valid = (d1_first == 'M' && d1_second == 'S')
                        || (d1_first == 'S' && d1_second == 'M');
                boolean diag2Valid = (d2_first == 'M' && d2_second == 'S')
                        || (d2_first == 'S' && d2_second == 'M');

                if (diag1Valid && diag2Valid) {
                    count++;
                }
            }
        }

        System.out.println(count);
    }
}
