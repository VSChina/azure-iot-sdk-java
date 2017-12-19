package com.microsoft.azure.sdk.iot.device;

import com.microsoft.azure.sdk.iot.device.DeviceTwin.Device;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceTwin;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.PropertyCallBack;
import com.sun.javafx.css.CssError;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Handling device client diagnostic information.
 */
public class DeviceClientDiagnostic {
    public PropertyCallBack<String, Object> twinPropertyCallback;

    // Key of desired twins
    public static final String DIAGNOSTIC_TWIN_KEY_DIAG_SAMPLE_RATE = "_diag_sample_rate";
    public static final String DIAGNOSTIC_TWIN_KEY_DIAG_ERROR = "_diag_info";

    private int diagSamplingPercentage;
    private int currentMessageNumber;

    private DeviceTwin diagnosticTwin;

    private static final int DIAGNOSTIC_ID_CHARACTER_BASE = 62;
    // Total number of 0-9
    private static final int DIAGNOSTIC_ID_CHARACTER_NUMBER = 10;
    // Total number of A-Z
    private static final int DIAGNOSTIC_ID_CHARACTER_CAPITALIZED = 26;

    // Key of creation time in diagnostic context.
    private static final String DIAGNOSTIC_CONTEXT_CREATION_TIME_UTC_PROPERTY = "creationtimeutc";
    // Equal symbol in diagnostic context.
    private static final String DIAGNOSTIC_CONTEXT_SYMBOL_EQUAL = "=";
    // And symbol in diagnostic context.
    private static final String DIAGNOSTIC_CONTEXT_SYMBOL_AND = ",";

    private class DiagnosticTwinPropertyCallback extends Device {
        @Override
        public void PropertyCall(String propertyKey, Object propertyValue, Object context) {
            String errorMessage = "";
            if (propertyKey.equals(DIAGNOSTIC_TWIN_KEY_DIAG_SAMPLE_RATE)) {
                try {
                    if (propertyValue == null) {
                        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_02_004: [This function shall set diagnostic sample rate to 0 if the twin value is null.]*/
                        setDiagSamplingPercentage(0);
                    } else {
                        // in Java SDK, integer value will be converted to float value
                        // e.g., set a => 10, here propertyValue is 10.0
                        // so make a workaround
                        Double doublePropertyValue = (Double) propertyValue;
                        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_02_001: [This function shall set diagnostic settings.]*/
                        setDiagSamplingPercentage(doublePropertyValue.intValue());
                    }
                } catch (Exception e) {
                    /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_02_002: [This function shall generate error message if value is invalid.]*/
                    errorMessage += ("Value of " + DIAGNOSTIC_TWIN_KEY_DIAG_SAMPLE_RATE + " is invalid: " + propertyValue + "(check if it is number).");
                }
                if (diagnosticTwin != null) {
                    /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_02_003: [This function shall report applied settings and error.]*/
                    HashSet<Property> reportedProperties = new HashSet<Property>();
                    reportedProperties.add(new Property(DIAGNOSTIC_TWIN_KEY_DIAG_SAMPLE_RATE, diagSamplingPercentage));
                    reportedProperties.add(new Property(DIAGNOSTIC_TWIN_KEY_DIAG_ERROR, errorMessage));
                    /**
                     * If update reported properties in the same thread of desired property callback, the addMessage will wait for the same lock
                     * of invokeCallbacks(in MqttTransport) and cause dead lock.
                     */
                    UpdateReportedTwinThread updateReportedTwinThread = new UpdateReportedTwinThread(diagnosticTwin, reportedProperties);
                    new Thread(updateReportedTwinThread).start();
                }
            }
        }
    }

    /**
     * Constructor of DeviceClientDiagnostic for local settings.
     */
    public DeviceClientDiagnostic() {
        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_001: [This constructor shall set sampling percentage to 0.]*/
        this.diagSamplingPercentage = 0;
        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_002: [This constructor shall set message number to 0.]*/
        this.currentMessageNumber = 0;
    }

    /**
     * Constructor of DeviceClientDiagnostic for cloud settings.
     *
     * @param diagnosticTwin The device twin instance of the device client.
     */
    public DeviceClientDiagnostic(DeviceTwin diagnosticTwin) {
        this();
        this.diagnosticTwin = diagnosticTwin;
        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_004: [This constructor shall initialize a twinPropertyCallback.]*/
        this.twinPropertyCallback = new DiagnosticTwinPropertyCallback();
    }

    /**
     * Getter of diagSamplingPercentage.
     */
    public int getDiagSamplingPercentage() {
        return diagSamplingPercentage;
    }

    /**
     * Setter of diagSamplingPercentage.
     *
     * @param diagSamplingPercentage Value of sampling percentage.
     * @throws IllegalArgumentException When sampling percentage is not in scope.
     */
    public void setDiagSamplingPercentage(int diagSamplingPercentage) {
        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_005: [When percentage is less than 0 or larger than 100, throw IllegalArgumentException.]*/
        if (diagSamplingPercentage < 0 || diagSamplingPercentage > 100) {
            throw new IllegalArgumentException("Sampling rate should between [0,100]");
        }
        this.diagSamplingPercentage = diagSamplingPercentage;
        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_006: [This function shall reset message number to 0.]*/
        this.currentMessageNumber = 0;
    }

    /**
     * Attach diagnostic information to message if necessary.
     *
     * @param message The original message.
     */
    public void addDiagnosticInfoIfNecessary(Message message) {
        if (shouldAddDiagnosticInfo()) {
            /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_007: [This function shall add diagnostic id to message.]*/
            message.setDiagnosticId(generateEightRandomCharacters());
            /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_008: [This function shall add encoded diagnostic correlation context to message.]*/
            message.setDiagnosticCorrelationContext(DIAGNOSTIC_CONTEXT_CREATION_TIME_UTC_PROPERTY + DIAGNOSTIC_CONTEXT_SYMBOL_EQUAL + getCurrentTimeUtc());
        }
    }

    /**
     * Getter of diagnosticTwin.
     */
    public DeviceTwin getDiagnosticTwin() {
        return diagnosticTwin;
    }

    // Get a character from 0-9a-zA-Z
    private char getDiagnosticIdChar(int value) {
        if (value < DIAGNOSTIC_ID_CHARACTER_NUMBER) {
            return (char) ('0' + value);
        } else if (value < DIAGNOSTIC_ID_CHARACTER_NUMBER + DIAGNOSTIC_ID_CHARACTER_CAPITALIZED) {
            return (char) ('A' + value - DIAGNOSTIC_ID_CHARACTER_NUMBER);
        } else {
            return (char) ('a' + value - DIAGNOSTIC_ID_CHARACTER_CAPITALIZED - DIAGNOSTIC_ID_CHARACTER_NUMBER);
        }
    }

    private String generateEightRandomCharacters() {
        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_009: [Function `generateEightRandomCharacters` shall generate 8 random chars, each is from 0-9a-z.]*/
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, DIAGNOSTIC_ID_CHARACTER_BASE);
            result.append(getDiagnosticIdChar(randomNum));
        }
        return result.toString();
    }

    private String getCurrentTimeUtc() {
        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_010: [Function `getCurrentTimeUtc` shall return the current timestamp in 0000000000.000 pattern.]*/
        DecimalFormat decimalFormat = new DecimalFormat("##########.000");
        return decimalFormat.format(System.currentTimeMillis() / 1000.0);
    }

    private boolean shouldAddDiagnosticInfo() {
        boolean result = false;
        /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_011: [Function `shouldAddDiagnosticInfo` shall return false if sampling percentage is set to 0.]*/
        if (diagSamplingPercentage > 0 && diagSamplingPercentage <= 100) {
            if (currentMessageNumber == Integer.MAX_VALUE) {
                currentMessageNumber = 0;
            }
            currentMessageNumber++;
            /* Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_012: [Function `shouldAddDiagnosticInfo` shall return value due to the sampling percentage setting.]*/
            result = (Math.floor((currentMessageNumber - 2) * diagSamplingPercentage / 100.0) < Math.floor((currentMessageNumber - 1) * diagSamplingPercentage / 100.0));
        }
        return result;
    }
}

/**
 * A new thread to update reported properties.
 * If update reported properties in the same thread of desired property callback, the addMessage will wait for the same lock
 * of invokeCallbacks(in MqttTransport) and cause dead lock.
 */
class UpdateReportedTwinThread implements Runnable {
    private DeviceTwin diagnosticTwin;
    private HashSet<Property> properties;

    public UpdateReportedTwinThread(DeviceTwin diagnosticTwin, HashSet<Property> properties) {
        this.diagnosticTwin = diagnosticTwin;
        this.properties = properties;
    }

    public void run() {
        try {
            diagnosticTwin.updateReportedProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
