package tareagui36houar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GestorUsuariosCsv {

    private final Path rutaCsv;

    public GestorUsuariosCsv(Path rutaCsv) {
        this.rutaCsv = rutaCsv;
    }

    public Set<Usuario> cargarUsuarios() {
        Set<Usuario> usuarios = new LinkedHashSet<>();

        if (!Files.exists(rutaCsv)) {
            return usuarios;
        }

        try {
            List<String> lineas = Files.readAllLines(rutaCsv, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                if (linea == null || linea.isBlank()) {
                    continue;
                }

                String[] partes = linea.split(",", 2);
                if (partes.length == 2) {
                    usuarios.add(new Usuario(partes[0].trim(), partes[1].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudieron cargar los usuarios del CSV.");
        }

        return usuarios;
    }

    public void guardarUsuarios(Set<Usuario> usuarios) {
        List<String> lineas = new java.util.ArrayList<String>();

        for (Usuario usuario : usuarios) {
            lineas.add(usuario.getUsername() + "," + usuario.getPassword());
        }

        try {
            Files.write(rutaCsv, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se pudieron guardar los usuarios en el CSV.");
        }
    }
}
