package tests.unit.com.microsoft.azure.sdk.iot.device;

import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.*;
import com.microsoft.azure.sdk.iot.device.auth.IotHubSasToken;
import com.microsoft.azure.sdk.iot.device.fileupload.FileUpload;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * Created by zhqqi on 9/22/2017.
 */
public class DeviceClientDiagnosticTest {
    @Mocked
    DeviceClientConfig mockConfig;

    @Mocked
    IotHubConnectionString mockIotHubConnectionString;

    @Mocked
    DeviceIO mockDeviceIO;

    @Test (expected = IllegalArgumentException.class)
    public void setDiagSamplingPercentageNegativeValueThrows()
    {
        // arrange
        DeviceClientDiagnostic diagnostic = new DeviceClientDiagnostic();

        // act
        diagnostic.setDiagSamplingPercentage(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void setDiagSamplingPercentageIllegalValueThrows()
    {
        // arrange
        DeviceClientDiagnostic diagnostic = new DeviceClientDiagnostic();

        // act
        diagnostic.setDiagSamplingPercentage(101);
    }
}
