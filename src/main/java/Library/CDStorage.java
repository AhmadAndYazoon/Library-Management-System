package Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CDStorage {

    private static final String CD_FILE = "cds.txt";

    public static void saveCDs(List<CD> cds) {
        try (FileWriter writer = new FileWriter(new File(CD_FILE))) {
            for (CD cd : cds) {
                String borrowed = cd.isBorrowed ? "yes" : "no";
                String line = cd.title + "," + cd.author + "," + cd.ISBN + "," + borrowed;
                writer.write(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CD> loadCDs() {
        List<CD> cds = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                boolean borrowed = parts[3].equals("yes");

                CD cd = new CD(parts[0], parts[1], parts[2], borrowed);
                cds.add(cd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cds;
    }
}
