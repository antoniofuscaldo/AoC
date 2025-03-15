import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class day3_2023 {
    public static void main(String[] args) {
        try {
            List<String> lines = readInput("input.txt");

            // Part 1
            int sumPartNumbers = calculatePartNumberSum(lines);
            System.out.println("Part 1 - Sum of all part numbers: " + sumPartNumbers);

            // Part 2
            int sumGearRatios = calculateGearRatioSum(lines);
            System.out.println("Part 2 - Sum of all gear ratios: " + sumGearRatios);

        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }

    private static List<String> readInput(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static int calculatePartNumberSum(List<String> schematic) {
        int rows = schematic.size();
        int cols = schematic.get(0).length();
        int sum = 0;

        for (int row = 0; row < rows; row++) {
            int col = 0;
            while (col < cols) {
                // Skip non-digit characters
                if (!Character.isDigit(schematic.get(row).charAt(col))) {
                    col++;
                    continue;
                }

                // Found a digit, extract the complete number
                int startCol = col;
                int number = 0;

                while (col < cols && Character.isDigit(schematic.get(row).charAt(col))) {
                    number = number * 10 + Character.getNumericValue(schematic.get(row).charAt(col));
                    col++;
                }

                int endCol = col - 1;

                // Check if this number is adjacent to a symbol
                if (isAdjacentToSymbol(schematic, row, startCol, endCol, rows, cols)) {
                    sum += number;
                }
            }
        }

        return sum;
    }

    private static boolean isAdjacentToSymbol(List<String> schematic, int row, int startCol, int endCol, int rows,
            int cols) {
        // Check all surrounding cells for symbols
        for (int r = Math.max(0, row - 1); r <= Math.min(rows - 1, row + 1); r++) {
            for (int c = Math.max(0, startCol - 1); c <= Math.min(cols - 1, endCol + 1); c++) {
                char ch = schematic.get(r).charAt(c);
                // A symbol is anything that's not a digit or a period
                if (!Character.isDigit(ch) && ch != '.') {
                    return true;
                }
            }
        }
        return false;
    }

    private static int calculateGearRatioSum(List<String> schematic) {
        int rows = schematic.size();
        int cols = schematic.get(0).length();

        // Map to store potential gears: key is "row,col", value is list of adjacent
        // part numbers
        Map<String, List<Integer>> potentialGears = new HashMap<>();

        // First pass: find all numbers and record them for adjacent asterisks
        for (int row = 0; row < rows; row++) {
            int col = 0;
            while (col < cols) {
                // Skip non-digit characters
                if (!Character.isDigit(schematic.get(row).charAt(col))) {
                    col++;
                    continue;
                }

                // Found a digit, extract the complete number
                int startCol = col;
                int number = 0;

                while (col < cols && Character.isDigit(schematic.get(row).charAt(col))) {
                    number = number * 10 + Character.getNumericValue(schematic.get(row).charAt(col));
                    col++;
                }

                int endCol = col - 1;

                // Check if this number is adjacent to any asterisks
                findAdjacentAsterisks(schematic, row, startCol, endCol, rows, cols, number, potentialGears);
            }
        }

        // Second pass: calculate gear ratios for asterisks adjacent to exactly two part
        // numbers
        int sumGearRatios = 0;
        for (Map.Entry<String, List<Integer>> entry : potentialGears.entrySet()) {
            List<Integer> adjacentNumbers = entry.getValue();
            if (adjacentNumbers.size() == 2) {
                int gearRatio = adjacentNumbers.get(0) * adjacentNumbers.get(1);
                sumGearRatios += gearRatio;
            }
        }

        return sumGearRatios;
    }

    private static void findAdjacentAsterisks(List<String> schematic, int row, int startCol, int endCol,
            int rows, int cols, int number, Map<String, List<Integer>> potentialGears) {

        // Check all surrounding cells for asterisks
        for (int r = Math.max(0, row - 1); r <= Math.min(rows - 1, row + 1); r++) {
            for (int c = Math.max(0, startCol - 1); c <= Math.min(cols - 1, endCol + 1); c++) {
                char ch = schematic.get(r).charAt(c);
                if (ch == '*') {
                    // Found an asterisk, record this number for it
                    String key = r + "," + c;
                    potentialGears.computeIfAbsent(key, k -> new ArrayList<>()).add(number);
                }
            }
        }
    }
}