package com.microsoft.azure.sdk.iot.device;

import com.microsoft.azure.sdk.iot.device.DeviceTwin.Device;

/**
 * Created by zhqqi on 9/22/2017.
 */
public class DeviceClientDiagnostic {
    private Integer diagSamplingPercentage;
    private Integer currentMessageNumber;
    public Integer getDiagSamplingPercentage() {
        return diagSamplingPercentage;
    }

    public void setDiagSamplingPercentage(Integer diagSamplingPercentage) {
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

    private 

}
