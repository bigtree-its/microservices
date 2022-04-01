package com.bigtree.chef.orders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Fixtures {

    private static final String BASE_PATH = "src/component/resources/fixtures/";

    public static String getFixture(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(BASE_PATH + filePath)));
    }
}
