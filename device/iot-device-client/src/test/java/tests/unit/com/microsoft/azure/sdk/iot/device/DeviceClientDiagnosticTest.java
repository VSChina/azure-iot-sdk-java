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

import static org.junit.Assert.assertEquals;

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

    @Test
    public void addDiagnosticInfoIfNecessaryWillAddDiagnosticInfo(@Mocked final Message mockMessage)
    {
        // arrange
        DeviceClientDiagnostic diagnostic = new DeviceClientDiagnostic();
        diagnostic.setDiagSamplingPercentage(100);

        // act
        diagnostic.addDiagnosticInfoIfNecessary(mockMessage);

        // assert
        new Verifications()
        {
            {
                mockMessage.setDiagnosticId((String) any);
                times = 1;
                mockMessage.setDiagnosticCreationTimeUtc((String) any);
                times = 1;
            }
        };
    }

    @Test
    public void addDiagnosticInfoIfNecessaryWillNotAddDiagnosticInfoIfPercentageIsZero(@Mocked final Message mockMessage)
    {
        // arrange
        final DeviceClientDiagnostic diagnostic = new DeviceClientDiagnostic();
        diagnostic.setDiagSamplingPercentage(0);

        // act
        diagnostic.addDiagnosticInfoIfNecessary(mockMessage);

        // assert
        new Verifications()
        {
            {
                mockMessage.setDiagnosticId((String) any);
                times = 0;
                mockMessage.setDiagnosticCreationTimeUtc((String) any);
                times = 0;
                assertEquals(0, Deencapsulation.getField(diagnostic,"currentMessageNumber"));
            }
        };
    }

    @Test
    public void addDiagnosticInfoIfNecessaryWillAddDiagnosticInfoApplyToPercentage(@Mocked final Message mockMessage)
    {
        // arrange
        DeviceClientDiagnostic diagnostic = new DeviceClientDiagnostic();
        diagnostic.setDiagSamplingPercentage(80);

        // act
        for(int i=0;i<10;i++) {
            diagnostic.addDiagnosticInfoIfNecessary(mockMessage);
        }
        // assert
        new Verifications()
        {
            {
                mockMessage.setDiagnosticId((String) any);
                times = 8;
                mockMessage.setDiagnosticCreationTimeUtc((String) any);
                times = 8;
            }
        };
    }
}
