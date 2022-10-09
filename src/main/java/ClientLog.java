import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClientLog {
    private static Map<Integer, Integer> log = new HashMap<>();
    public static void logProd (int productNum, int amount) {
        log.put(productNum, amount);
    }

    public static void exportAsCSV(File txtFile) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile))) {
            for (int i=0; i<log.size(); i++) {
                String[] row = {String.valueOf(i), String.valueOf(log.get(i))};
                writer.writeNext(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
