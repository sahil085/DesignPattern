package com.ttn.bluebell.rest.exception;

import com.ttn.bluebell.app.BluebellApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ttn on 26/10/16.
 */

public class CustomisedExceptionMessageLoader {

    private static Properties validationProperties = null;

    private static void loadValidationPropertiesBuilder() throws IOException{
        if (validationProperties == null) {
            InputStream inputStream = BluebellApplication.class.getClassLoader().getResourceAsStream("ValidationMessages.properties");
            validationProperties = new Properties();
            validationProperties.load(inputStream);
        }
    }

    public static String getPropertyValue(String key){
        try {
            loadValidationPropertiesBuilder();
            return validationProperties.getProperty(key);
        }
        catch (Exception exp) {
            return "Server error.";
        }
    }
}
