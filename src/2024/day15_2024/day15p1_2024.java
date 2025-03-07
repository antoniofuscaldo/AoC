import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class day15p1_2024 {
    public static void main(String[] args) {
        try {
            // Read all lines from the puzzle input file.
            List<String> lines = Files.readAllLines(Paths.get("input.txt"));

            // The input is structured as follows:
            // - A block of lines representing the warehouse map.
            // - One (or more) blank lines separating the map from the moves.
            // - The remainder lines (possibly broken into several lines)
            // represent the robot’s move sequence.
            List<String> mapLines = new ArrayList<>();
            StringBuilder movesBuilder = new StringBuilder();
            boolean readingMap = true;
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    // When we hit a blank line, switch to reading the moves.
                    readingMap = false;
                    continue;
                }
                if (readingMap) {
                    mapLines.add(line);
                } else {
                    // Append any non-empty line to the move sequence (ignoring spaces).
                    movesBuilder.append(line.trim());
                }
            }
            String moves = movesBuilder.toString();

            // Convert the list of map lines into a 2D character grid.
            int rows = mapLines.size();
            int cols = mapLines.get(0).length();
            char[][] grid = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                grid[i] = mapLines.get(i).toCharArray();
            }

            // Find the starting position of the robot, indicated by '@'
            int robotRow = -1, robotCol = -1;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == '@') {
                        robotRow = r;
                        robotCol = c;
                        break;
                    }
                }
                if (robotRow != -1)
                    break;
            }

            // Process each move instruction one by one.
            // Moves are one of: '^' (up), 'v' (down), '<' (left), or '>' (right).
            for (char move : moves.toCharArray()) {
                int dx = 0, dy = 0;
                switch (move) {
                    case '^':
                        dx = -1;
                        dy = 0;
                        break;
                    case 'v':
                        dx = 1;
                        dy = 0;
                        break;
                    case '<':
                        dx = 0;
                        dy = -1;
                        break;
                    case '>':
                        dx = 0;
                        dy = 1;
                        break;
                    default:
                        // Ignore any unexpected character.
                        continue;
                }

                int newRow = robotRow + dx;
                int newCol = robotCol + dy;
                char target = grid[newRow][newCol];

                // If the robot is trying to move into a wall (#), do nothing.
                if (target == '#') {
                    continue;
                }
                // If the robot moves into an empty space ('.'), simply update its position.
                else if (target == '.') {
                    grid[robotRow][robotCol] = '.';
                    grid[newRow][newCol] = '@';
                    robotRow = newRow;
                    robotCol = newCol;
                }
                // If the adjacent cell contains a box ('O'), attempt to push a contiguous chain
                // of boxes.
                else if (target == 'O') {
                    ArrayList<int[]> chain = new ArrayList<>();
                    int curRow = newRow;
                    int curCol = newCol;
                    // Follow the chain of boxes in the move direction.
                    while (grid[curRow][curCol] == 'O') {
                        chain.add(new int[] { curRow, curCol });
                        curRow += dx;
                        curCol += dy;
                    }
                    // If the cell immediately after the chain is a wall, then the push fails.
                    if (grid[curRow][curCol] == '#') {
                        continue;
                    }
                    // Otherwise, push all boxes one step in the indicated direction.
                    // First clear the cells where boxes are currently located.
                    for (int[] pos : chain) {
                        grid[pos[0]][pos[1]] = '.';
                    }
                    // Then place each box at its new position.
                    // (Because the boxes form a contiguous chain, each new position is simply old
                    // position + (dx,dy))
                    for (int[] pos : chain) {
                        int newBoxRow = pos[0] + dx;
                        int newBoxCol = pos[1] + dy;
                        grid[newBoxRow][newBoxCol] = 'O';
                    }
                    // Now that the boxes have been pushed, move the robot into the first box's old
                    // cell.
                    grid[robotRow][robotCol] = '.';
                    grid[newRow][newCol] = '@';
                    robotRow = newRow;
                    robotCol = newCol;
                }
            }

            // After all moves have been simulated, compute the sum of the GPS coordinates
            // for every box.
            // The GPS coordinate for a box at (row, col) is: 100 * row + col.
            long totalSum = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 'O') {
                        totalSum += (r * 100 + c);
                    }
                }
            }

            // Output the answer
            System.out.println(totalSum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
