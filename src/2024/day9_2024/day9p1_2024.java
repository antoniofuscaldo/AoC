import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class day9p1_2024 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        if (lines.isEmpty()) {
            System.out.println(0);
            return;
        }

        String mapData = lines.get(0).trim();
        StringBuilder disk = new StringBuilder();
        int fileId = 0;
        for (int i = 0; i < mapData.length(); i++) {
            int len = mapData.charAt(i) - '0';
            if (i % 2 == 0) {
                for (int j = 0; j < len; j++) {
                    disk.append((char) ('0' + fileId));
                }
                fileId++;
            } else {
                for (int j = 0; j < len; j++) {
                    disk.append('.');
                }
            }
        }

        while (true) {
            int freeIndex = -1;
            int lastFileIndex = -1;

            for (int i = 0; i < disk.length(); i++) {
                if (disk.charAt(i) == '.') {
                    freeIndex = i;
                    break;
                }
            }
            if (freeIndex == -1)
                break;

            for (int i = disk.length() - 1; i >= 0; i--) {
                if (disk.charAt(i) != '.') {
                    lastFileIndex = i;
                    break;
                }
            }
            if (lastFileIndex == -1)
                break;

            if (freeIndex < lastFileIndex) {
                disk.setCharAt(freeIndex, disk.charAt(lastFileIndex));
                disk.setCharAt(lastFileIndex, '.');
            } else {
                break;
            }
        }

        long checksum = 0;
        for (int i = 0; i < disk.length(); i++) {
            char ch = disk.charAt(i);
            if (ch != '.') {
                int id = ch - '0';
                checksum += (long) i * id;
            }
        }

        System.out.println(checksum);
    }
}
