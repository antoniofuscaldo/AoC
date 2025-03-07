import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class day9p2_2024 {
	public static void main(String[] args) throws Exception {
		String input = new String(Files.readAllBytes(Paths.get("input.txt"))).trim();

		List<Integer> diskList = new ArrayList<>();
		int fileId = 0;
		for (int i = 0; i < input.length(); i++) {
			int segmentLength = input.charAt(i) - '0';
			if (i % 2 == 0) {
				for (int j = 0; j < segmentLength; j++) {
					diskList.add(fileId);
				}
				fileId++;
			} else {
				for (int j = 0; j < segmentLength; j++) {
					diskList.add(-1);
				}
			}
		}
		int[] disk = new int[diskList.size()];
		for (int i = 0; i < disk.length; i++) {
			disk[i] = diskList.get(i);
		}

		int totalFiles = fileId;

		for (int f = totalFiles - 1; f >= 0; f--) {
			int start = -1;
			int length = 0;
			for (int i = 0; i < disk.length; i++) {
				if (disk[i] == f) {
					start = i;
					int j = i;
					while (j < disk.length && disk[j] == f) {
						length++;
						j++;
					}
					break;
				}
			}
			if (start == -1)
				continue;

			int candidateStart = -1;
			for (int i = 0; i < start;) {
				if (disk[i] == -1) {
					int segStart = i;
					int segLength = 0;
					while (i < start && disk[i] == -1) {
						segLength++;
						i++;
					}
					if (segLength >= length) {
						candidateStart = segStart;
						break;
					}
				} else {
					i++;
				}
			}

			if (candidateStart != -1) {
				int newPos = candidateStart;
				for (int i = newPos; i < newPos + length; i++) {
					disk[i] = f;
				}
				for (int i = start; i < start + length; i++) {
					disk[i] = -1;
				}
			}
		}

		long checksum = 0;
		for (int i = 0; i < disk.length; i++) {
			if (disk[i] != -1) {
				checksum += (long) i * disk[i];
			}
		}

		System.out.println(checksum);
	}
}
