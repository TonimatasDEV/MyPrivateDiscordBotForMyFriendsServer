package dev.tonimatas.util;

import java.io.File;
import java.net.URISyntaxException;

public class JarUtils {
    public static String getJarName() {
        try {
            File jarFile = new File(JarUtils.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());

            if (jarFile.getName().endsWith(".jar")) {
                return jarFile.getName();
            } else {
                return "No ejecutado desde un JAR.";
            }

        } catch (URISyntaxException e) {
            return "Error obteniendo el nombre del JAR.";
        }
    }

    public static void main(String[] args) {
        System.out.println("Nombre del JAR: " + getJarName());
    }
}
