import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day12p2_2024 {

    static class Interval {
        int start;
        int end;

        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        int rows = lines.size();
        if (rows == 0) {
            System.out.println(0);
            return;
        }
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            grid[r] = lines.get(r).toCharArray();
        }

        boolean[][] visited = new boolean[rows][cols];
        long totalPrice = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!visited[r][c]) {
                    char plant = grid[r][c];
                    List<int[]> regionCells = new ArrayList<>();
                    Queue<int[]> queue = new LinkedList<>();
                    queue.add(new int[] { r, c });
                    visited[r][c] = true;
                    while (!queue.isEmpty()) {
                        int[] cell = queue.poll();
                        int cr = cell[0], cc = cell[1];
                        regionCells.add(cell);
                        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
                        for (int[] d : dirs) {
                            int nr = cr + d[0], nc = cc + d[1];
                            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                                continue;
                            if (!visited[nr][nc] && grid[nr][nc] == plant) {
                                visited[nr][nc] = true;
                                queue.add(new int[] { nr, nc });
                            }
                        }
                    }
                    int area = regionCells.size();
                    boolean[][] inRegion = new boolean[rows][cols];
                    for (int[] cell : regionCells) {
                        inRegion[cell[0]][cell[1]] = true;
                    }

                    HashMap<String, ArrayList<Interval>> edgeMap = new HashMap<>();

                    for (int[] cell : regionCells) {
                        int cr = cell[0], cc = cell[1];
                        if (cr - 1 < 0 || !inRegion[cr - 1][cc]) {
                            String key = "H,0," + cr;
                            addEdge(edgeMap, key, cc, cc + 1);
                        }
                        if (cr + 1 >= rows || !inRegion[cr + 1][cc]) {
                            String key = "H,1," + (cr + 1);
                            addEdge(edgeMap, key, cc, cc + 1);
                        }
                        if (cc - 1 < 0 || !inRegion[cr][cc - 1]) {
                            String key = "V,0," + cc;
                            addEdge(edgeMap, key, cr, cr + 1);
                        }
                        if (cc + 1 >= cols || !inRegion[cr][cc + 1]) {
                            String key = "V,1," + (cc + 1);
                            addEdge(edgeMap, key, cr, cr + 1);
                        }
                    }

                    int sides = 0;
                    for (Map.Entry<String, ArrayList<Interval>> entry : edgeMap.entrySet()) {
                        ArrayList<Interval> list = entry.getValue();
                        Collections.sort(list, Comparator.comparingInt(i -> i.start));
                        sides += mergeIntervals(list);
                    }

                    totalPrice += (long) area * sides;
                }
            }
        }

        System.out.println(totalPrice);
    }

    static void addEdge(Map<String, ArrayList<Interval>> map, String key, int s, int e) {
        map.putIfAbsent(key, new ArrayList<>());
        map.get(key).add(new Interval(s, e));
    }

    static int mergeIntervals(ArrayList<Interval> intervals) {
        if (intervals.isEmpty())
            return 0;
        int count = 0;
        Interval current = intervals.get(0);
        for (int i = 1; i < intervals.size(); i++) {
            Interval next = intervals.get(i);
            if (next.start <= current.end) {
                current.end = Math.max(current.end, next.end);
            } else {
                count++;
                current = next;
            }
        }
        count++;
        return count;
    }
}
