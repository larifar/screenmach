package br.com.alura.screenmach.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiKey {
    private final static Properties properties = new Properties();

    public static String pegaKey() {
        String fileName = "src/key.properties";
        try (FileInputStream fileInput = new FileInputStream(fileName)) {
            properties.load(fileInput);
            return properties.getProperty("api.key");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar o arquivo de propriedades.");
            return null;
        }
    }
}
