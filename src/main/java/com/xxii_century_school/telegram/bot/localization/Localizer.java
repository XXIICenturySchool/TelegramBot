package com.xxii_century_school.telegram.bot.localization;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Localizer {
    private String locale = "en-US";// languageCode: in telegram API
    private Properties properties = new Properties();

    public Localizer(String locale) {
        try {
            tryLoad(locale);
        } catch (IOException e) {
            //log.error("Locale file " + locale + ".properties was not found, switching to en-US");
            locale = this.locale;
            try {
                tryLoad(locale);
            } catch (IOException e1) {
                //log.error("Locale file " + locale + ".properties was not found");
                e1.printStackTrace();
            }
        }
        this.locale = locale;
    }

    public String localize(String string) {
        return getMessage(string);
    }

    public String getMessage(String message_id) {
        return properties.getProperty(message_id);
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + locale + ")";
    }

    private void tryLoad(String locale) throws IOException {
        try {// Loading locale file from working directory
            File file = new File(propertiesFileName(locale));
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (IOException e) {// Loading locale file from resources
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName(locale));
            if (resourceStream != null) {
                properties.load(resourceStream);
            } else {
                throw new IOException("failed to get resourceStream");
            }
        }
    }

    private String propertiesFileName(String locale) {
        return locale + ".properties";
    }

}

