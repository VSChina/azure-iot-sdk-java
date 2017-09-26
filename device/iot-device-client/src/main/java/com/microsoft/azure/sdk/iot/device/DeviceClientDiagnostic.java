package com.microsoft.azure.sdk.iot.device;

import com.microsoft.azure.sdk.iot.device.DeviceTwin.Device;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by zhqqi on 9/22/2017.
 */
public class DeviceClientDiagnostic {
    private int diagSamplingPercentage;
    private int currentMessageNumber;

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final int BASE_36 = 36;


    public int getDiagSamplingPercentage() {
        return diagSamplingPercentage;
    }

    public void setDiagSamplingPercentage(int diagSamplingPercentage) {
        if(diagSamplingPercentage < 0 || diagSamplingPercentage > 100) {
            throw new IllegalArgumentException();
        }
        this.diagSamplingPercentage = diagSamplingPercentage;
        this.currentMessageNumber = 0;
    }

    public DeviceClientDiagnostic()
    {
        this.diagSamplingPercentage = 0;
        this.currentMessageNumber = 0;
    }

    private char getBase36Char(int value)
    {
        return value <= 9 ? (char)('0' + value) : (char) ('a' + value - 10);
    }

    private String generateEightRandomCharacters()
    {
        StringBuilder result = new StringBuilder();
        for(int i=0;i<8;i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, BASE_36 + 1);
            result.append(getBase36Char(randomNum));
        }
        return result.toString();
    }

    private String getCurrentTimeUtc()
    {
        DecimalFormat decimalFormat = new DecimalFormat("0000000000.000");
        return decimalFormat.format(System.currentTimeMillis() / 1000.0);
    }

    private boolean shouldAddDiagnosticInfo()
    {
        boolean result = false;
        if(diagSamplingPercentage > 0) {
            if(currentMessageNumber == Integer.MAX_VALUE) {
                currentMessageNumber = 0;
            }
            currentMessageNumber++;
            result = (Math.floor((currentMessageNumber - 2) * diagSamplingPercentage / 100.0) < Math.floor((currentMessageNumber - 1) * diagSamplingPercentage / 100.0));
        }
        return result;
    }

    public void addDiagnosticInfoIfNecessary(Message message)
    {
        if(shouldAddDiagnosticInfo()) {
            message.setDiagnosticId(generateEightRandomCharacters());
            message.setDiagnosticCreationTimeUtc(getCurrentTimeUtc());
            // success or failure?
        }
    }

}
