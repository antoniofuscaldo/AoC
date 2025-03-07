import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class day15_2024 {
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("input.txt"));
            // Separa le linee della mappa da quelle dei movimenti
            List<String> gridLines = new ArrayList<>();
            List<String> moveLines = new ArrayList<>();
            boolean blankFound = false;
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    blankFound = true;
                    continue;
                }
                if (!blankFound) {
                    gridLines.add(line);
                } else {
                    moveLines.add(line);
                }
            }

            // Parte 1 – simulazione standard (push singola)
            Warehouse warehouse1 = new Warehouse(gridLines, moveLines);
            int part1 = warehouse1.simulate();
            System.out.println("Part 1 Answer: " + part1);

            // Parte 2 – simulazione con push a catena e calcolo GPS wide
            Warehouse2 warehouse2 = new Warehouse2(gridLines, moveLines);
            int part2 = warehouse2.simulate();
            System.out.println("Part 2 Answer: " + part2);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}

/*
 * PART 1: Mappa originale – se il robot incontra una scatola 'O'
 * spinge solo quella, se possibile.
 */
class Warehouse {
    protected char[][] grid;
    protected int rows, cols;
    protected int robotX, robotY;
    protected List<Character> moves;

    public Warehouse(List<String> gridLines, List<String> moveLines) {
        rows = gridLines.size();
        cols = gridLines.get(0).length();
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            grid[i] = gridLines.get(i).toCharArray();
            if (gridLines.get(i).indexOf('@') != -1) {
                robotY = i;
                robotX = gridLines.get(i).indexOf('@');
            }
        }
        moves = new ArrayList<>();
        for (String line : moveLines) {
            for (char c : line.toCharArray()) {
                if (c == '^' || c == 'v' || c == '<' || c == '>') {
                    moves.add(c);
                }
            }
        }
    }

    public int simulate() {
        Map<Character, int[]> directions = Map.of(
                '^', new int[] { -1, 0 },
                'v', new int[] { 1, 0 },
                '<', new int[] { 0, -1 },
                '>', new int[] { 0, 1 });

        for (char move : moves) {
            int[] d = directions.get(move);
            if (d == null)
                continue;
            int dy = d[0], dx = d[1];
            moveRobot(dy, dx);
        }
        return calculateGPS();
    }

    protected void moveRobot(int dy, int dx) {
        int newY = robotY + dy;
        int newX = robotX + dx;
        if (isWall(newY, newX))
            return;
        if (grid[newY][newX] == 'O') {
            int boxY = newY + dy;
            int boxX = newX + dx;
            if (isWall(boxY, boxX) || grid[boxY][boxX] == 'O')
                return;
            grid[boxY][boxX] = 'O';
            grid[newY][newX] = '@';
            grid[robotY][robotX] = '.';
            robotY = newY;
            robotX = newX;
        } else if (grid[newY][newX] == '.') {
            grid[newY][newX] = '@';
            grid[robotY][robotX] = '.';
            robotY = newY;
            robotX = newX;
        }
    }

    protected boolean isWall(int y, int x) {
        return y < 0 || y >= rows || x < 0 || x >= cols || grid[y][x] == '#';
    }

    // Per la Parte 1, il GPS di una scatola in (i,j) è 100*i + j.
    protected int calculateGPS() {
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'O') {
                    sum += 100 * i + j;
                }
            }
        }
        return sum;
    }
}

/*
 * PART 2: Mappa "wide" – il robot spinge a catena.
 * La simulazione si esegue sulla griglia logica originale (stessa dimensione),
 * ma con la seguente differenza:
 * • Se il robot si muove verso una cella contenente una scatola 'O', raccoglie
 * tutte le scatole contigue in quella direzione (push a catena).
 * • Se la cella subito dopo la catena è vuota ('.'), tutte le scatole vengono
 * spinte.
 * Inoltre, per il calcolo del GPS per le scatole wide, si usa:
 * GPS = 100 * riga + (2 * colonna + 1)
 * (cioè, la distanza orizzontale si calcola come 2j+1, in modo che ad esempio
 * una scatola
 * originariamente in colonna 2 abbia distanza 2*2+1 = 5, come nell’esempio.)
 */
class Warehouse2 extends Warehouse {

    public Warehouse2(List<String> gridLines, List<String> moveLines) {
        super(gridLines, moveLines);
    }

    // Sovrascriviamo moveRobot per implementare la spinta a catena
    @Override
    protected void moveRobot(int dy, int dx) {
        int newY = robotY + dy;
        int newX = robotX + dx;
        if (isWall(newY, newX))
            return;
        if (grid[newY][newX] == 'O') {
            // Raccogli la catena di scatole contigue nella direzione
            List<Point> chain = new ArrayList<>();
            int cy = newY, cx = newX;
            while (inBounds(cy, cx) && grid[cy][cx] == 'O') {
                chain.add(new Point(cy, cx));
                cy += dy;
                cx += dx;
            }
            // Se la cella subito dopo la catena non è vuota, la mossa fallisce.
            if (!inBounds(cy, cx) || grid[cy][cx] != '.')
                return;
            // Spingi la catena: dall'ultima scatola verso la prima.
            for (int i = chain.size() - 1; i >= 0; i--) {
                Point p = chain.get(i);
                grid[p.y + dy][p.x + dx] = 'O';
                grid[p.y][p.x] = '.';
            }
            // Muovi il robot nella cella liberata
            grid[newY][newX] = '@';
            grid[robotY][robotX] = '.';
            robotY = newY;
            robotX = newX;
        } else if (grid[newY][newX] == '.') {
            grid[newY][newX] = '@';
            grid[robotY][robotX] = '.';
            robotY = newY;
            robotX = newX;
        }
    }

    private boolean inBounds(int y, int x) {
        return y >= 0 && y < rows && x >= 0 && x < cols;
    }

    // Calcola il GPS wide: per ogni scatola in (i,j) si somma 100*i + (2*j + 1).
    @Override
    protected int calculateGPS() {
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'O') {
                    sum += 100 * i + (2 * j + 1);
                }
            }
        }
        return sum;
    }
}

class Point {
    int y, x;

    public Point(int y, int x) {
        this.y = y;
        this.x = x;
    }
}
