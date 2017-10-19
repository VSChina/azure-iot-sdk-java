package com.microsoft.azure.sdk.iot.device;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Handling device client diagnostic information.
 */
public class DeviceClientDiagnostic {
    private int diagSamplingPercentage;
    private int currentMessageNumber;

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final int BASE_36 = 36;

    public DeviceClientDiagnostic()
    {
        // Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_001: [This constructor shall set sampling percentage to 0.]
        this.diagSamplingPercentage = 0;
        // Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_002: [This constructor shall set message number to 0.]
        this.currentMessageNumber = 0;
    }

    public int getDiagSamplingPercentage() {
        return diagSamplingPercentage;
    }

    public void setDiagSamplingPercentage(int diagSamplingPercentage) {
        // Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_003: [When percentage is less than 0 or larger than 100, throw IllegalArgumentException.]
        if(diagSamplingPercentage < 0 || diagSamplingPercentage > 100) {
            throw new IllegalArgumentException();
        }
        this.diagSamplingPercentage = diagSamplingPercentage;
        // Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_004: [This function shall reset message number to 0.]
        this.currentMessageNumber = 0;
    }

    // Get a character from 0-9a-z
    private char getBase36Char(int value)
    {
        return value <= 9 ? (char)('0' + value) : (char) ('a' + value - 10);
    }

    private String generateEightRandomCharacters()
    {
        // Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_005: [This function shall generate 8 random chars, each is from 0-9a-z.]
        StringBuilder result = new StringBuilder();
        for(int i=0;i<8;i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, BASE_36 );
            result.append(getBase36Char(randomNum));
        }
        return result.toString();
    }

    private String getCurrentTimeUtc()
    {
        // Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_006: [This function shall return the current timestamp in 0000000000.000 pattern.]
        DecimalFormat decimalFormat = new DecimalFormat("0000000000.000");
        return decimalFormat.format(System.currentTimeMillis() / 1000.0);
    }

    private boolean shouldAddDiagnosticInfo()
    {
        boolean result = false;
        // Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_007: [This function shall return false if sampling percentage is set to 0.]
        if(diagSamplingPercentage > 0) {
            if(currentMessageNumber == Integer.MAX_VALUE) {
                currentMessageNumber = 0;
            }
            currentMessageNumber++;
            // Codes_SRS_DEVICECLIENTDIAGNOSTIC_01_008: [This function shall return value due to the sampling percentage setting.]
            result = (Math.floor((currentMessageNumber - 2) * diagSamplingPercentage / 100.0) < Math.floor((currentMessageNumber - 1) * diagSamplingPercentage / 100.0));
        }
        return result;
    }

    public void addDiagnosticInfoIfNecessary(Message message)
    {
        if(shouldAddDiagnosticInfo()) {
            DiagnosticPropertyData diagnosticPropertyData = new DiagnosticPropertyData(generateEightRandomCharacters(),getCurrentTimeUtc());
            message.setDiagnosticPropertyData(diagnosticPropertyData);
        }
    }

}
