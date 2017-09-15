/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package tests.unit.com.microsoft.azure.sdk.iot.device.auth;

import com.microsoft.azure.sdk.iot.device.auth.IotHubConnectionString;
import com.microsoft.azure.sdk.iot.device.auth.IotHubSSLContext;
import com.microsoft.azure.sdk.iot.device.auth.IotHubSasToken;
import com.microsoft.azure.sdk.iot.device.auth.IotHubSasTokenAuthentication;
import mockit.*;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Unit tests for IotHubSasTokenAuthentication.java
 * Methods: 100%
 * Lines: 100%
 */
public class IotHubSasTokenAuthenticationTest
{
    @Mocked
    IotHubConnectionString mockConnectionString;

    @Mocked
    IotHubSSLContext mockSSLContext;

    @Mocked
    IotHubSasToken mockSasToken;

    private static String expectedDeviceId = "deviceId";
    private static String expectedHostname = "hostname";
    private static String expectedHubName = "hubname";
    private static String expectedDeviceKey = "deviceKey";
    private static String expectedSasToken = "sasToken";
    private static long expectedExpiryTime = 3600;

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_002: [This constructor shall save the provided connection string.]
    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_003: [This constructor shall generate a default IotHubSSLContext.]
    @Test
    public void constructorSavesConnectionStringAndSSLContext() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                mockConnectionString.isUsingX509();
                result = false;
            }
        };

        //act
        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //assert
        IotHubConnectionString actualConnString = Deencapsulation.getField(sasAuth, "connectionString");
        assertEquals(mockConnectionString, actualConnString);
        new Verifications()
        {
            {
                new IotHubSSLContext();
                times = 1;
            }
        };
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_004: [If the saved sas token has expired and there is a device key present, the saved sas token shall be renewed.]
    @Test
    public void getSasTokenAutoRenews() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        commonConnectionStringExpectations();
        new Expectations()
        {
            {
                new IotHubSasToken(expectedHostname, expectedDeviceId, expectedDeviceKey, expectedSasToken, expectedExpiryTime);
                result = mockSasToken;
                mockSasToken.isExpired();
                result = true;
            }
        };

        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //act
        String actualSasToken = sasAuth.getSasToken();

        //assert
        assertNotEquals(mockSasToken.toString(), actualSasToken);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_005: [This function shall return the saved sas token.]
    @Test
    public void getSasTokenReturnsSavedValue() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        commonConnectionStringExpectations();
        new Expectations()
        {
            {
                new IotHubSasToken(expectedHostname, expectedDeviceId, expectedDeviceKey, expectedSasToken, expectedExpiryTime);
                result = mockSasToken;
                mockSasToken.isExpired();
                result = false;
            }
        };

        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //act
        String actualSasToken = sasAuth.getSasToken();

        //assert
        assertEquals(mockSasToken.toString(), actualSasToken);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_006: [This function shall save the provided sas token.]
    @Test
    public void setSasTokenSavesValue(@Mocked final IotHubSasToken otherMockedSasToken) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        commonConnectionStringExpectations();
        new Expectations()
        {
            {
                new IotHubSasToken(expectedHostname, expectedDeviceId, expectedDeviceKey, expectedSasToken, expectedExpiryTime);
                result = mockSasToken;
            }
        };

        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //act
        sasAuth.setSasToken(otherMockedSasToken);

        //assert
        IotHubSasToken actualSasToken = Deencapsulation.getField(sasAuth, "sasToken");
        assertEquals(otherMockedSasToken, actualSasToken);
        assertNotEquals(mockSasToken, actualSasToken);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_007: [If the provided sas token is null, an IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void setSasTokenThrowsForNullSasToken() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //act
        new IotHubSasTokenAuthentication(mockConnectionString).setSasToken(null);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_008: [This function shall return the generated IotHubSSLContext.]
    @Test
    public void getIotHubSSLContextGets() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);
        Deencapsulation.setField(sasAuth, "iotHubSSLContext", mockSSLContext);

        //act
        IotHubSSLContext actualSSLContext = sasAuth.getIotHubSSLContext();

        //assert
        assertEquals(mockSSLContext, actualSSLContext);
        new Verifications()
        {
            {
                new IotHubSSLContext();
                times = 1;
            }
        };
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_009: [This function shall save the provided IotHubSSLContext.]
    @Test
    public void setIotHubSSLContextSets() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //act
        sasAuth.setIotHubSSLContext(mockSSLContext);

        //assert
        IotHubSSLContext savedIotHubSSLContext = Deencapsulation.getField(sasAuth, "iotHubSSLContext");
        assertEquals(mockSSLContext, savedIotHubSSLContext);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_010: [If the provided iotHubSSLContext is null, an IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void setIotHubSSLContextThrowsForNullContext() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //act
        new IotHubSasTokenAuthentication(mockConnectionString).setIotHubSSLContext(null);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_011: [This function shall return the number of seconds that tokens are valid for.]
    @Test
    public void getTokenValidSecsGets() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        long newTokenValidSecs = 5000L;
        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);
        Deencapsulation.setField(sasAuth, "tokenValidSecs", newTokenValidSecs);

        //act
        long actualSeconds = sasAuth.getTokenValidSecs();

        //assert
        assertEquals(newTokenValidSecs, actualSeconds);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_012: [This function shall save the provided tokenValidSecs as the number of seconds that created sas tokens are valid for.]
    @Test
    public void setTokenValidSecsSets() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        long newTokenValidSecs = 5000L;
        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //act
        sasAuth.setTokenValidSecs(newTokenValidSecs);

        //assert
        long actualTokenValidSecs = Deencapsulation.getField(sasAuth, "tokenValidSecs");
        assertEquals(newTokenValidSecs, actualTokenValidSecs);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_013: [This function shall return the current connection string.]
    @Test
    public void getConnectionStringGets() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);
        Deencapsulation.setField(sasAuth, "connectionString", mockConnectionString);

        //act
        IotHubConnectionString actualConnectionString = sasAuth.getConnectionString();

        //assert
        assertEquals(mockConnectionString, actualConnectionString);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_014: [This function shall save the provided connection string.]
    @Test
    public void setConnectionStringSaves(@Mocked final IotHubConnectionString otherMockConnectionString) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //act
        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //arrange
        sasAuth.setConnectionString(otherMockConnectionString);

        //assert
        IotHubConnectionString actualConnectionString = Deencapsulation.getField(sasAuth, "connectionString");
        assertEquals(otherMockConnectionString, actualConnectionString);
        assertNotEquals(mockConnectionString, actualConnectionString);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_015: [If the provided connection string is null, an IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void setConnectionStringThrowsForNullConnectionString() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //act
        new IotHubSasTokenAuthentication(mockConnectionString).setConnectionString(null);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_016: [If the provided connection string is missing both a device key and a sas token, an IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void setConnectionStringThrowsForConnectionStringNotContainingSasTokenOrDeviceKey(@Mocked final IotHubConnectionString otherMockConnectionString) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                otherMockConnectionString.getSharedAccessKey();
                result = null;
                otherMockConnectionString.getSharedAccessToken();
                result = null;
                otherMockConnectionString.isUsingX509();
                result = true;
            }
        };

        //act
        new IotHubSasTokenAuthentication(mockConnectionString).setConnectionString(otherMockConnectionString);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_017: [If the saved sas token has expired and cannot be renewed, this function shall return true.]
    @Test
    public void needsToRenewReturnsTrueWhenTokenHasExpiredAndNoDeviceKeyIsPresent() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        commonConnectionStringExpectations();
        new NonStrictExpectations()
        {
            {
                mockSasToken.isExpired();
                result = true;
                mockConnectionString.getSharedAccessKey();
                result = null;
            }
        };

        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //act
        boolean needsToRenew = sasAuth.needsToRenewSasToken();

        //assert
        assertTrue(needsToRenew);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_017: [If the saved sas token has expired and cannot be renewed, this function shall return true.]
    @Test
    public void needsToRenewReturnsFalseDeviceKeyPresent() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        commonConnectionStringExpectations();
        new NonStrictExpectations()
        {
            {
                mockSasToken.isExpired();
                result = true;
                mockConnectionString.getSharedAccessKey();
                result = expectedDeviceKey;
            }
        };

        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //act
        boolean needsToRenew = sasAuth.needsToRenewSasToken();

        //assert
        assertFalse(needsToRenew);
    }

    //Tests_SRS_IOTHUBSASTOKENAUTHENTICATION_34_017: [If the saved sas token has expired and cannot be renewed, this function shall return true.]
    @Test
    public void needsToRenewReturnsFalseWhenTokenHasNotExpired() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException
    {
        //arrange
        commonConnectionStringExpectations();
        new NonStrictExpectations()
        {
            {
                mockSasToken.isExpired();
                result = false;
            }
        };

        IotHubSasTokenAuthentication sasAuth = new IotHubSasTokenAuthentication(mockConnectionString);

        //act
        boolean needsToRenew = sasAuth.needsToRenewSasToken();

        //assert
        assertFalse(needsToRenew);
    }

    private void commonConnectionStringExpectations() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        new NonStrictExpectations()
        {
            {
                mockConnectionString.getHostName();
                result = expectedHostname;
                mockConnectionString.getDeviceId();
                result = expectedDeviceId;
                mockConnectionString.getHubName();
                result = expectedHubName;
                mockConnectionString.getSharedAccessKey();
                result = expectedDeviceKey;
                mockConnectionString.getSharedAccessToken();
                result = expectedSasToken;

                new IotHubSSLContext();
                result = mockSSLContext;
            }
        };
    }
}
