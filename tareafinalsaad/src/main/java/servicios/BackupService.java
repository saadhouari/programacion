package servicios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BackupService {

    private static final String URL = "jdbc:mysql://localhost:3306/empresa_ventas?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final Path BACKUP_ROOT = Paths.get("backups");
    private static final DateTimeFormatter FOLDER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public Path crearCopiaSeguridad() throws SQLException, IOException {
        Path folder = BACKUP_ROOT.resolve(LocalDateTime.now().format(FOLDER_FORMAT));
        Files.createDirectories(folder);
        try (Connection connection = openConnection()) {
            exportTable(connection, folder, "cliente",
                    "select id, nombre, nif from cliente order by id");
            exportTable(connection, folder, "producto",
                    "select id, descripcion, precio, stock from producto order by id");
            exportTable(connection, folder, "venta",
                    "select id, idcliente, fecha from venta order by id");
            exportTable(connection, folder, "detalleventa",
                    "select id, idventa, idproducto, cantidad, precioventa from detalleventa order by id");
        }
        return folder.toAbsolutePath();
    }

    public Path restaurarUltimaCopia() throws SQLException, IOException {
        Path folder = ultimaCopia()
                .orElseThrow(() -> new IOException("No hay copias de seguridad en la carpeta backups."));
        try (Connection connection = openConnection(); Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            try {
                statement.execute("set foreign_key_checks = 0");
                statement.executeUpdate("delete from detalleventa");
                statement.executeUpdate("delete from venta");
                statement.executeUpdate("delete from cliente");
                statement.executeUpdate("delete from producto");
                importClientes(connection, folder.resolve("cliente.csv"));
                importProductos(connection, folder.resolve("producto.csv"));
                importVentas(connection, folder.resolve("venta.csv"));
                importDetalles(connection, folder.resolve("detalleventa.csv"));
                resetAutoIncrement(connection, "cliente");
                resetAutoIncrement(connection, "producto");
                resetAutoIncrement(connection, "venta");
                resetAutoIncrement(connection, "detalleventa");
                statement.execute("set foreign_key_checks = 1");
                connection.commit();
            } catch (Exception ex) {
                connection.rollback();
                statement.execute("set foreign_key_checks = 1");
                throw ex;
            }
        }
        return folder.toAbsolutePath();
    }

    public Optional<Path> ultimaCopia() throws IOException {
        if (!Files.isDirectory(BACKUP_ROOT)) {
            return Optional.empty();
        }
        try (var stream = Files.list(BACKUP_ROOT)) {
            return stream.filter(Files::isDirectory)
                    .max(Comparator.comparing(path -> path.getFileName().toString()));
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void exportTable(Connection connection, Path folder, String tableName, String sql)
            throws SQLException, IOException {
        Path file = folder.resolve(tableName + ".csv");
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql);
             BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            int columns = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columns; i++) {
                if (i > 1) {
                    writer.write(",");
                }
                writer.write(csv(resultSet.getMetaData().getColumnName(i)));
            }
            writer.newLine();
            while (resultSet.next()) {
                for (int i = 1; i <= columns; i++) {
                    if (i > 1) {
                        writer.write(",");
                    }
                    writer.write(csv(resultSet.getString(i)));
                }
                writer.newLine();
            }
        }
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private List<List<String>> readRows(Path file) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                rows.add(parseCsvLine(line));
            }
        }
        return rows;
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }
        values.add(current.toString());
        return values;
    }

    private void importClientes(Connection connection, Path file) throws SQLException, IOException {
        try (PreparedStatement ps = connection.prepareStatement(
                "insert into cliente (id, nombre, nif) values (?, ?, ?)")) {
            for (List<String> row : readRows(file)) {
                ps.setInt(1, Integer.parseInt(row.get(0)));
                ps.setString(2, emptyToNull(row.get(1)));
                ps.setString(3, row.get(2));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void importProductos(Connection connection, Path file) throws SQLException, IOException {
        try (PreparedStatement ps = connection.prepareStatement(
                "insert into producto (id, descripcion, precio, stock) values (?, ?, ?, ?)")) {
            for (List<String> row : readRows(file)) {
                ps.setInt(1, Integer.parseInt(row.get(0)));
                ps.setString(2, emptyToNull(row.get(1)));
                ps.setFloat(3, Float.parseFloat(row.get(2)));
                ps.setInt(4, Integer.parseInt(row.get(3)));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void importVentas(Connection connection, Path file) throws SQLException, IOException {
        try (PreparedStatement ps = connection.prepareStatement(
                "insert into venta (id, idcliente, fecha) values (?, ?, ?)")) {
            for (List<String> row : readRows(file)) {
                ps.setInt(1, Integer.parseInt(row.get(0)));
                ps.setInt(2, Integer.parseInt(row.get(1)));
                ps.setTimestamp(3, row.get(2).isBlank() ? null : Timestamp.valueOf(row.get(2)));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void importDetalles(Connection connection, Path file) throws SQLException, IOException {
        try (PreparedStatement ps = connection.prepareStatement(
                "insert into detalleventa (id, idventa, idproducto, cantidad, precioventa) values (?, ?, ?, ?, ?)")) {
            for (List<String> row : readRows(file)) {
                ps.setInt(1, Integer.parseInt(row.get(0)));
                ps.setInt(2, Integer.parseInt(row.get(1)));
                ps.setInt(3, Integer.parseInt(row.get(2)));
                ps.setInt(4, Integer.parseInt(row.get(3)));
                ps.setFloat(5, Float.parseFloat(row.get(4)));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private void resetAutoIncrement(Connection connection, String tableName) throws SQLException {
        int nextId = 1;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select coalesce(max(id), 0) + 1 from " + tableName)) {
            if (resultSet.next()) {
                nextId = resultSet.getInt(1);
            }
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute("alter table " + tableName + " auto_increment = " + nextId);
        }
    }
}
