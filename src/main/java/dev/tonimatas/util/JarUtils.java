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
                return "Not executed from Jar.";
            }

        } catch (URISyntaxException e) {
            return "Error getting the jar file.";
        }
    }
}
