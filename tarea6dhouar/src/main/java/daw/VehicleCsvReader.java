package daw;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VehicleCsvReader {

    public List<Vehicle> readVehicles(Path csvPath) throws IOException {
        try (Reader reader = Files.newBufferedReader(csvPath)) {
            return new CsvToBeanBuilder<Vehicle>(reader)
                    .withType(Vehicle.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        }
    }
}
