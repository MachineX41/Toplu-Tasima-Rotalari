package org.example.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractData {
    private final String veri;

    public AbstractData() throws IOException {
        this.veri = set_data("veriseti.json");
    }

    public String get_data() {
        return this.veri;
    }

    public String set_data(String filename) throws IOException {
        // Dosyanın içeriğini oku
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
