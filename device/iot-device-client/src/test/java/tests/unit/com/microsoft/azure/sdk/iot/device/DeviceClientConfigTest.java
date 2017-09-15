// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package tests.unit.com.microsoft.azure.sdk.iot.device;

import com.microsoft.azure.sdk.iot.device.DeviceClientConfig;
import com.microsoft.azure.sdk.iot.device.MessageCallback;
import com.microsoft.azure.sdk.iot.device.auth.*;
import mockit.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit tests for deviceClientConfig.
 * Methods: 96%
 * Lines: 95%
 */
public class DeviceClientConfigTest
{
    @Mocked IotHubSasTokenAuthentication mockSasTokenAuthentication;
    @Mocked IotHubX509Authentication mockX509Authentication;

    private static final String publicKeyCert = "someCert";
    private static final String privateKey = "someKey";

    // Tests_SRS_DEVICECLIENTCONFIG_11_002: [The function shall return the IoT Hub hostname given in the constructor.]
    @Test
    public void getIotHubHostnameReturnsIotHubHostname()
            throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        new NonStrictExpectations()
        {
            {
                new IotHubSasTokenAuthentication(iotHubConnectionString);
                result = mockSasTokenAuthentication;
                mockSasTokenAuthentication.getConnectionString().getHostName();
                result = iotHubHostname;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        String testIotHubHostname = config.getIotHubHostname();

        final String expectedIotHubHostname = iotHubHostname;
        assertThat(testIotHubHostname, is(expectedIotHubHostname));
    }

    // Tests_SRS_DEVICECLIENTCONFIG_11_007: [The function shall return the IoT Hub name given in the constructor,
    // where the IoT Hub name is embedded in the IoT Hub hostname as follows: [IoT Hub name].[valid HTML chars]+.]
    @Test
    public void getIotHubNameReturnsIotHubName() throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);
        final String expectedIotHubName = "test";

        new NonStrictExpectations()
        {
            {
                new IotHubSasTokenAuthentication(iotHubConnectionString);
                result = mockSasTokenAuthentication;
                mockSasTokenAuthentication.getConnectionString().getHubName();
                result = expectedIotHubName;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        final String testIotHubName = config.getIotHubName();

        assertThat(testIotHubName, is(expectedIotHubName));
    }

    // Tests_SRS_DEVICECLIENTCONFIG_11_003: [The function shall return the device ID given in the constructor.]
    @Test
    public void getDeviceIdReturnsDeviceId() throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        new NonStrictExpectations()
        {
            {
                new IotHubSasTokenAuthentication(iotHubConnectionString);
                result = mockSasTokenAuthentication;
                mockSasTokenAuthentication.getConnectionString().getDeviceId();
                result = deviceId;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        String testDeviceId = config.getDeviceId();

        final String expectedDeviceId = deviceId;
        assertThat(testDeviceId, is(expectedDeviceId));
    }

    // Tests_SRS_DEVICECLIENTCONFIG_11_004: [If this is using Sas token authentication, the function shall return the device key given in the constructor.]
    @Test
    public void getDeviceKeyReturnsDeviceKey() throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        new NonStrictExpectations()
        {
            {
                new IotHubSasTokenAuthentication(iotHubConnectionString);
                result = mockSasTokenAuthentication;
                mockSasTokenAuthentication.getConnectionString().getSharedAccessKey();
                result = deviceKey;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        String testDeviceKey = config.getDeviceKey();

        final String expectedDeviceKey = deviceKey;
        assertThat(testDeviceKey, is(expectedDeviceKey));
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_054: [If this is using x509 authentication, the function shall return null.]
    @Test
    public void getDeviceKeyReturnsNullIfNotUsingSasTokenAuth(@Mocked final IotHubConnectionString mockConnectionString) throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnrecoverableKeyException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockConnectionString.isUsingX509();
                result = true;
                new IotHubX509Authentication(mockConnectionString, "", false, "", false);
                result = mockX509Authentication;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockConnectionString,  "", false, "", false);

        //act
        String testDeviceKey = config.getDeviceKey();

        //assert
        assertNull(testDeviceKey);
    }

    // Tests_SRS_DEVICECLIENTCONFIG_25_018: [If this is using sas token authentication, the function shall return the SharedAccessToken saved in the sas token authentication object.]
    @Test
    public void getSasTokenReturnsSasToken(@Mocked final IotHubSasToken mockSasToken, @Mocked final IotHubConnectionString mockIotHubConnectionString) throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = null;
        final String sharedAccessToken = "SharedAccessSignature sr=sample-iothub-hostname.net%2fdevices%2fsample-device-ID&sig=S3%2flPidfBF48B7%2fOFAxMOYH8rpOneq68nu61D%2fBP6fo%3d&se=" + Long.MAX_VALUE;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        new NonStrictExpectations()
        {
            {
                new IotHubSasTokenAuthentication(iotHubConnectionString);
                result = mockSasTokenAuthentication;
                mockSasTokenAuthentication.getSasToken();
                result = sharedAccessToken;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        String testSasToken = config.getSharedAccessToken();

        final String expectedSasToken = sharedAccessToken;
        assertThat(testSasToken, is(expectedSasToken));
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_053: [If this is using x509 authentication, the function shall return null.]
    @Test
    public void getSasTokenReturnsNullIfNotUsingSasTokenAuth(@Mocked final IotHubSasToken mockSasToken, @Mocked final IotHubConnectionString mockIotHubConnectionString) throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnrecoverableKeyException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = true;
                new IotHubX509Authentication(mockIotHubConnectionString, "", false, "", false);
                result = mockX509Authentication;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString,  "", false, "", false);

        //act
        String testSasToken = config.getSharedAccessToken();

        //assert
        assertNull(testSasToken);
    }


    // Tests_SRS_DEVICECLIENTCONFIG_11_005: [If this is using Sas token authentication, then this function shall return the value of tokenValidSecs saved in it and 0 otherwise.]
    @Test
    public void getMessageValidSecsReturnsConstant() throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        final long expectedMessageValidSecs = 3600;
        new NonStrictExpectations()
        {
            {
                new IotHubSasTokenAuthentication(iotHubConnectionString);
                result = mockSasTokenAuthentication;
                mockSasTokenAuthentication.getTokenValidSecs();
                result = expectedMessageValidSecs;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        long testMessageValidSecs = config.getTokenValidSecs();

        assertThat(testMessageValidSecs, is(expectedMessageValidSecs));
    }

    // Tests_SRS_DEVICECLIENTCONFIG_11_005: [If this is using Sas token authentication, then this function shall return the value of tokenValidSecs saved in it and 0 otherwise.]
    @Test
    public void getMessageValidSecsReturnsZeroIfNotUsingSasAuth(@Mocked final IotHubConnectionString mockConnectionString) throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        new NonStrictExpectations()
        {
            {
                mockConnectionString.isUsingX509();
                result = true;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockConnectionString, "", false, "", false);
        long testMessageValidSecs = config.getTokenValidSecs();

        assertEquals(0L, testMessageValidSecs);
    }

    // Tests_SRS_DEVICECLIENTCONFIG_25_008: [The function shall set the value of tokenValidSecs.]
    @Test
    public void getandsetMessageValidSecsReturnsConstant() throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);
        final long testsetMessageValidSecs = 60;

        new NonStrictExpectations()
        {
            {
                new IotHubSasTokenAuthentication(iotHubConnectionString);
                result = mockSasTokenAuthentication;
                mockSasTokenAuthentication.getTokenValidSecs();
                result = testsetMessageValidSecs;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);

        config.setTokenValidSecs(testsetMessageValidSecs);
        long testgetMessageValidSecs= config.getTokenValidSecs();

        final long expectedMessageValidSecs = testsetMessageValidSecs;
        assertThat(testgetMessageValidSecs, is(expectedMessageValidSecs));
    }

    // Tests_SRS_DEVICECLIENTCONFIG_11_006: [The function shall set the message callback, with its associated context.]
    // Tests_SRS_DEVICECLIENTCONFIG_11_010: [The function shall return the current message callback.]
    @Test
    public void getAndSetMessageCallbackMatch(
            @Mocked final MessageCallback mockCallback)
            throws URISyntaxException, IOException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        Object context = new Object();
        config.setMessageCallback(mockCallback, context);
        MessageCallback testCallback = config.getDeviceTelemetryMessageCallback();

        final MessageCallback expectedCallback = mockCallback;
        assertThat(testCallback, is(expectedCallback));
    }

    // Tests_SRS_DEVICECLIENTCONFIG_11_006: [The function shall set the message callback, with its associated context.]
    // Tests_SRS_DEVICECLIENTCONFIG_11_011: [The function shall return the current message context.]
    @Test
    public void getAndSetMessageCallbackContextsMatch(
            @Mocked final MessageCallback mockCallback)
            throws URISyntaxException, IOException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        Object context = new Object();
        config.setMessageCallback(mockCallback, context);
        Object testContext = config.getDeviceTelemetryMessageContext();

        final Object expectedContext = context;
        assertThat(testContext, is(expectedContext));
    }

    /*
    **Tests_SRS_DEVICECLIENTCONFIG_25_023: [**The function shall set the DEVICE_TWIN message callback.**] **
    **Tests_SRS_DEVICECLIENTCONFIG_25_024: [**The function shall set the DEVICE_TWIN message context.**] **
    **Tests_SRS_DEVICECLIENTCONFIG_25_025: [**The function shall return the current DEVICE_TWIN message callback.**] **
    **Tests_SRS_DEVICECLIENTCONFIG_25_026: [**The function shall return the current DEVICE_TWIN message context.**] **
     */
    @Test
    public void getAndSetDeviceTwinMessageCallbackAndContextsMatch(
            @Mocked final MessageCallback mockCallback)
            throws URISyntaxException, IOException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        Object context = new Object();
        config.setDeviceTwinMessageCallback(mockCallback, context);
        Object testContext = config.getDeviceTwinMessageContext();

        final Object expectedContext = context;
        assertThat(testContext, is(expectedContext));
        assertEquals(config.getDeviceTwinMessageCallback(), mockCallback);
    }

    /*
    **Tests_SRS_DEVICECLIENTCONFIG_25_022: [**The function shall return the current DeviceMethod message context.**] **
    **Tests_SRS_DEVICECLIENTCONFIG_25_023: [**The function shall set the DeviceTwin message callback.**] **
    **Tests_SRS_DEVICECLIENTCONFIG_25_021: [**The function shall return the current DeviceMethod message callback.**] **
    **Tests_SRS_DEVICECLIENTCONFIG_25_022: [**The function shall return the current DeviceMethod message context.**] **
     */
    @Test
    public void getAndSetDeviceMethodMessageCallbackAndContextsMatch(
            @Mocked final MessageCallback mockCallback)
            throws URISyntaxException, IOException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        Object context = new Object();
        config.setDeviceMethodsMessageCallback(mockCallback, context);
        Object testContext = config.getDeviceMethodsMessageContext();


        final Object expectedContext = context;
        assertThat(testContext, is(expectedContext));
        assertEquals(config.getDeviceMethodsMessageCallback(), mockCallback);
    }

    @Test
    public void getAndSetDeviceMethodAndTwinMessageCallbackAndContextsMatch(
            @Mocked final MessageCallback mockCallback)
            throws URISyntaxException, IOException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        Object dMContext = new Object();
        config.setDeviceMethodsMessageCallback(mockCallback, dMContext);
        Object testContextDM = config.getDeviceMethodsMessageContext();

        Object dTcontext = new Object();
        config.setDeviceTwinMessageCallback(mockCallback, dTcontext);
        Object testContextDT = config.getDeviceTwinMessageContext();

        final Object expectedDTContext = dTcontext;
        assertThat(testContextDT, is(expectedDTContext));
        assertEquals(config.getDeviceTwinMessageCallback(), mockCallback);


        final Object expectedDMContext = dMContext;
        assertThat(testContextDM, is(expectedDMContext));
        assertEquals(config.getDeviceMethodsMessageCallback(), mockCallback);
    }
    // Tests_SRS_DEVICECLIENTCONFIG_11_012: [The function shall return 240000ms.]
    @Test
    public void getReadTimeoutMillisReturnsConstant() throws URISyntaxException, IOException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        int testReadTimeoutMillis = config.getReadTimeoutMillis();

        final int expectedReadTimeoutMillis = 240000;
        assertThat(testReadTimeoutMillis, is(expectedReadTimeoutMillis));
    }

    //Tests_SRS_DEVICECLIENTCONFIG_25_038: [The function shall save useWebsocket.]
    @Test
    public void setWebsocketEnabledSets() throws URISyntaxException, IOException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                                            new Class[] {String.class, String.class, String.class, String.class},
                                            iotHubHostname,
                                            deviceId,
                                            deviceKey,
                                            sharedAccessToken);

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        config.setUseWebsocket(true);
        assertTrue(config.isUseWebsocket());
    }

    //Tests_SRS_DEVICECLIENTCONFIG_25_037: [The function shall return the true if websocket is enabled, false otherwise.]
    @Test
    public void getWebsocketEnabledGets() throws URISyntaxException, IOException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                                            new Class[] {String.class, String.class, String.class, String.class},
                                            iotHubHostname,
                                            deviceId,
                                            deviceKey,
                                            sharedAccessToken);

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        config.setUseWebsocket(true);
        assertTrue(config.isUseWebsocket());
    }

    // Tests_SRS_DEVICECLIENTCONFIG_11_013: [The function shall return 180s.]
    @Test
    public void getMessageLockTimeoutSecsReturnsConstant()
            throws URISyntaxException, IOException
    {
        final String iotHubHostname = "test.iothubhostname";
        final String deviceId = "test-deviceid";
        final String deviceKey = "test-devicekey";
        final String sharedAccessToken = null;
        final IotHubConnectionString iotHubConnectionString =
                Deencapsulation.newInstance(IotHubConnectionString.class,
                        new Class[] {String.class, String.class, String.class, String.class},
                        iotHubHostname,
                        deviceId,
                        deviceKey,
                        sharedAccessToken);

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);
        int testMessageLockTimeoutSecs = config.getMessageLockTimeoutSecs();

        final int expectedMessageLockTimeoutSecs = 180;
        assertThat(testMessageLockTimeoutSecs,
                is(expectedMessageLockTimeoutSecs));
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_051: [If this is using Sas token Authentication, then this function shall return the IotHubSSLContext saved in its Sas token authentication object.]
    @Test
    public void getIotHubSSLContextGetsFromSASTokenAuthWhenUsingSASTokenAuth(@Mocked final IotHubSSLContext mockedContext, @Mocked final IotHubConnectionString iotHubConnectionString) throws URISyntaxException, IOException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                iotHubConnectionString.isUsingX509();
                result = false;
                mockSasTokenAuthentication.getIotHubSSLContext();
                result = mockedContext;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString);

        //act
        IotHubSSLContext testContext = config.getIotHubSSLContext();

        //assert
        assertNotNull(testContext);
        assertEquals(testContext, mockedContext);
        new Verifications()
        {
            {
                mockSasTokenAuthentication.getIotHubSSLContext();
                times = 1;
                mockX509Authentication.getIotHubSSLContext();
                times = 0;
            }
        };
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_052: [If this is using X509 Authentication, then this function shall return the IotHubSSLContext saved in its X509 authentication object.]
    @Test
    public void getIotHubSSLContextGetsFromX509AuthWhenUsingX509Auth(@Mocked final IotHubSSLContext mockedContext, @Mocked final IotHubConnectionString iotHubConnectionString) throws URISyntaxException, IOException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                iotHubConnectionString.isUsingX509();
                result = true;
                mockX509Authentication.getIotHubSSLContext();
                result = mockedContext;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(iotHubConnectionString, publicKeyCert, false, privateKey, false);

        //act
        IotHubSSLContext testContext = config.getIotHubSSLContext();

        //assert
        assertNotNull(testContext);
        assertEquals(testContext, mockedContext);
        new Verifications()
        {
            {
                mockSasTokenAuthentication.getIotHubSSLContext();
                times = 0;
                mockX509Authentication.getIotHubSSLContext();
                times = 1;
            }
        };
    }

    // Tests_SRS_DEVICECLIENTCONFIG_21_034: [If the provided `iotHubConnectionString` is null,
    // the constructor shall throw IllegalArgumentException.]
    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsNullConnectionString()
            throws URISyntaxException, IOException
    {
        new DeviceClientConfig(null);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_049: [If this is using SAS token authentication, this function shall return if that SAS Token authentication needs renewal.]
    @Test
    public void needsToRenewSasTokenReturnsTrueWhenSASTokenExpired(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //arrange
        final String expiredSasToken = "SharedAccessSignature sr=sample-iothub-hostname.net%2fdevices%2fsample-device-ID&sig=S3%2flPidfBF48B7%2fOFAxMOYH8rpOneq68nu61D%2fBP6fo%3d&se=" + 0L;;

        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.getSharedAccessToken();
                result = expiredSasToken;
                mockSasTokenAuthentication.needsToRenewSasToken();
                result = true;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString);

        //act
        boolean needsToRenewToken = config.needsToRenewSasToken();

        //assert
        assertTrue(needsToRenewToken);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_050: [If this isn't using SAS token authentication, this function shall return false.]
    @Test
    public void needsToRenewSasTokenReturnsFalseWhenNotUsingSasToken(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = true;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString,  "", false, "", false);

        //act
        boolean needsToRenewToken = config.needsToRenewSasToken();

        //assert
        assertFalse(needsToRenewToken);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_039: [This function shall return the type of authentication that the config is set up to use.]
    @Test
    public void getAuthenticationTypeWorks(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //arrange
        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString);

        DeviceClientConfig.AuthType expectedAuthType = DeviceClientConfig.AuthType.X509_CERTIFICATE;
        Deencapsulation.setField(config, "authenticationType", expectedAuthType);

        //act
        DeviceClientConfig.AuthType actualAuthType = config.getAuthenticationType();

        //assert
        assertEquals(expectedAuthType, actualAuthType);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_045: [If this is using x509 authentication, this function shall return this object's x509 certificate pair.]
    @Test
    public void getPublicKeyCertWorks(@Mocked final X509CertificatePair mockCertPair, @Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //arrange
        new Expectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = true;
                mockX509Authentication.getX509CertificatePair();
                result = mockCertPair;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString, publicKeyCert, false, privateKey, false);

        //act
        X509CertificatePair actualCertPair = Deencapsulation.invoke(config, "getX509CertificatePair");

        //assert
        assertEquals(mockCertPair, actualCertPair);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_056: [If this is using sas token authentication, this function shall return null.]
    @Test
    public void getPublicKeyCertReturnsNullIfNotUsingX509Auth(@Mocked final X509CertificatePair mockCertPair, @Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //arrange
        new Expectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = false;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString);

        //act
        X509CertificatePair actualCertPair = Deencapsulation.invoke(config, "getX509CertificatePair");

        //assert
        assertNull(actualCertPair);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_046: [If the provided `iotHubConnectionString` does not use x509 authentication, it shall be saved to a new IotHubSasTokenAuthentication object and the authentication type of this shall be set to SASToken.]
    @Test
    public void constructorUsingSASTokenSetsTypeCorrectly(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws URISyntaxException, IOException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = false;
            }
        };

        //act
        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString);

        //assert
        DeviceClientConfig.AuthType actualAuthType = Deencapsulation.getField(config, "authenticationType");
        assertEquals(DeviceClientConfig.AuthType.SAS_TOKEN, actualAuthType);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_048: [If an exception is thrown when creating the appropriate Authentication object, an IOException shall be thrown containing the details of that exception.]
    @Test (expected = IOException.class)
    public void constructorThrowsIOExceptionIfAnyExceptionCaughtCreatingAuthentication(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws URISyntaxException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = false;
                new IotHubSasTokenAuthentication(mockIotHubConnectionString);
                result = new CertificateException();
            }
        };

        //act
        new DeviceClientConfig(mockIotHubConnectionString);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_059: [If this function is called while this is using Sas token authentication, an IllegalStateException shall be thrown.]
    @Test (expected = IllegalStateException.class)
    public void setCertPathWhileUsingSasAuthThrows(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //arrange
        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString);

        //act
        config.setPathToCert("any string");
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_061: [If any exceptions are thrown while creating a new SSL context with the new public key certificate, this function shall throw an IOException.]
    @Test (expected = IOException.class)
    public void getCertPathHandlesExceptions(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //arrange
        final String expectedCertPath = "someCert";
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = true;
                mockX509Authentication.getX509CertificatePair().getPublicKeyCertificate();
                result = expectedCertPath;
                mockX509Authentication.getX509CertificatePair().getPrivateKey();
                result = privateKey;
                mockX509Authentication.getIotHubConnectionString();
                result = mockIotHubConnectionString;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString, expectedCertPath, true, privateKey, true);

        new NonStrictExpectations()
        {
            {
                new IotHubX509Authentication(mockIotHubConnectionString, expectedCertPath, true, privateKey, false);
                result = new CertificateException();
            }
        };

        //act
        config.setPathToCert(expectedCertPath);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_060: [This function shall regenerate it's SSL context using the new public key certificate.]
    @Test
    public void setCertPathCreatesNewSSLContext(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockX509Authentication.getX509CertificatePair().getPrivateKey();
                result = privateKey;
                mockX509Authentication.getIotHubConnectionString();
                result = mockIotHubConnectionString;
                mockIotHubConnectionString.isUsingX509();
                result = true;
            }
        };

        final String expectedPath = "some/path";
        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString, "some cert", false, privateKey, false);

        //act
        config.setPathToCert(expectedPath);

        //assert
        new Verifications()
        {
            {
                new IotHubX509Authentication(mockIotHubConnectionString, expectedPath, true, privateKey, false);
            }
        };
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_063: [If this function is called while this is using Sas token authentication, null shall be returned.]
    @Test
    public void getCertPathWhileSasAuthReturnsNull(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //act
        assertNull(new DeviceClientConfig(mockIotHubConnectionString).getPathToCertificate());
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_064: [If this function is called while this is using Sas token authentication, an IllegalStateException shall be thrown.]
    @Test (expected = IllegalStateException.class)
    public void setCertWhileUsingSasAuthThrows(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //arrange
        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString);

        //act
        config.setUserCertificateString("any string");
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_062: [This function shall return the saved path to the public key certificate or null if it is not saved.]
    @Test
    public void getCertPathSuccess(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //arrange
        final String expectedCertPath = "someCertPath";
        final String expectedKeyPath = "someKeyPath";
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = true;
                mockX509Authentication.getX509CertificatePair().getPathToPublicKeyCertificate();
                result = expectedCertPath;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString, expectedCertPath, true, expectedKeyPath, true);

        //act
        String actualCertPath = config.getPathToCertificate();

        //assert
        assertEquals(expectedCertPath, actualCertPath);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_067: [If this function is called while this is using Sas token authentication, null shall be returned.]
    @Test
    public void getCertWhileSasAuthReturnsNull(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //act
        assertNull(new DeviceClientConfig(mockIotHubConnectionString).getUserCertificateString());
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_065: [This function shall create a new SSL context using the new public key certificate.]
    @Test
    public void setCertCreatesNewSSLContext(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockX509Authentication.getX509CertificatePair().getPrivateKey();
                result = privateKey;
                mockX509Authentication.getIotHubConnectionString();
                result = mockIotHubConnectionString;
                mockIotHubConnectionString.isUsingX509();
                result = true;
            }
        };

        final String expectedCert = "someCert";
        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString, "some cert", false, privateKey, false);

        //act
        config.setUserCertificateString(expectedCert);

        //assert
        new Verifications()
        {
            {
                new IotHubX509Authentication(mockIotHubConnectionString, expectedCert, false, privateKey, false);
            }
        };
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_066: [If any exceptions are thrown while creating a new SSL context with the new public key certificate, this function shall throw an IOException.]
    @Test (expected = IOException.class)
    public void getCertHandlesExceptions(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = true;
                mockX509Authentication.getX509CertificatePair().getPublicKeyCertificate();
                result = publicKeyCert;
                mockX509Authentication.getX509CertificatePair().getPrivateKey();
                result = privateKey;
                mockX509Authentication.getIotHubConnectionString();
                result = mockIotHubConnectionString;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString, publicKeyCert, true, privateKey, true);

        new NonStrictExpectations()
        {
            {
                new IotHubX509Authentication(mockIotHubConnectionString, publicKeyCert, false, privateKey, false);
                result = new CertificateException();
            }
        };

        //act
        config.setUserCertificateString(publicKeyCert);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_068: [This function shall return the saved user certificate string.]
    @Test
    public void getCertSuccess(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = true;
                mockX509Authentication.getX509CertificatePair().getPublicKeyCertificate();
                result = publicKeyCert;
            }
        };

        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString, publicKeyCert, true, privateKey, true);

        //act
        String actualCert = config.getUserCertificateString();

        //assert
        assertEquals(publicKeyCert, actualCert);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_069: [This function shall generate a new SSLContext and set this to using X509 authentication.]
    @Test
    public void x509ConstructorGeneratesNewSSLContext(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = true;
            }
        };

        //act
        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString, publicKeyCert, false, privateKey, false);

        //assert
        new Verifications()
        {
            {
                new IotHubX509Authentication(mockIotHubConnectionString, publicKeyCert, false, privateKey, false);
            }
        };
    }


    //Tests_SRS_DEVICECLIENTCONFIG_34_070: [If any exceptions are encountered while generating the new SSLContext, an IOException shall be thrown.]
    @Test (expected = IOException.class)
    public void x509ConstructorExceptions(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = true;
                new IotHubX509Authentication(mockIotHubConnectionString, publicKeyCert, false, privateKey, false);
                result = new CertificateException();
            }
        };

        //act
        DeviceClientConfig config = new DeviceClientConfig(mockIotHubConnectionString, publicKeyCert, false, privateKey, false);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_069: [If the provided connection string is null or does not use x509 auth, and IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void constructorWithNullConnStringThrows(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //act
        new DeviceClientConfig(mockIotHubConnectionString, "", false, "", false);
    }

    //Tests_SRS_DEVICECLIENTCONFIG_34_069: [If the provided connection string is null or does not use x509 auth, and IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void constructorWithWrongAuthTypeConnStringThrows(@Mocked final IotHubConnectionString mockIotHubConnectionString) throws IOException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockIotHubConnectionString.isUsingX509();
                result = false;
            }
        };

        //act
        new DeviceClientConfig(mockIotHubConnectionString, "", false, "", false);
    }
}
