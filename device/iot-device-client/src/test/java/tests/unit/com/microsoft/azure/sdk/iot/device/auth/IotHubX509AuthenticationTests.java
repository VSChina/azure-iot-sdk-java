/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package tests.unit.com.microsoft.azure.sdk.iot.device.auth;

import com.microsoft.azure.sdk.iot.device.auth.IotHubConnectionString;
import com.microsoft.azure.sdk.iot.device.auth.IotHubSSLContext;
import com.microsoft.azure.sdk.iot.device.auth.IotHubX509Authentication;
import com.microsoft.azure.sdk.iot.device.auth.X509CertificatePair;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for IotHubX509Authentication.java
 * Methods: 100%
 * Lines: 100%
 */
public class IotHubX509AuthenticationTests
{
    @Mocked
    IotHubConnectionString mockConnectionString;

    @Mocked
    IotHubSSLContext mockIotHubSSLContext;

    @Mocked
    X509CertificatePair mockX509CertificatePair;

    private static final String publicKeyCertificate = "someCert";
    private static final String privateKey = "someKey";

    private void commonExpectations() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, IOException
    {
        new NonStrictExpectations()
        {
            {
                new X509CertificatePair(publicKeyCertificate, false, privateKey, false);
                result = mockX509CertificatePair;
                mockX509CertificatePair.getPublicKeyCertificate();
                result = publicKeyCertificate;
                mockX509CertificatePair.getPrivateKey();
                result = privateKey;
                new IotHubSSLContext(publicKeyCertificate, privateKey);
                result = mockIotHubSSLContext;
            }
        };
    }


    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_001: [This constructor will save the provided connection string.]
    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_002: [This constructor will create and save an X509CertificatePair object using the provided public key certificate and private key.]
    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_003: [This constructor will create a new IotHubSSLContext using the provided public key certificate and private key.]
    @Test
    public void constructorSuccess() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException
    {
        //arrange
        commonExpectations();

        //act
        IotHubX509Authentication x509Auth = new IotHubX509Authentication(mockConnectionString, publicKeyCertificate, false, privateKey, false);

        //assert
        IotHubConnectionString actualConnectionString = Deencapsulation.getField(x509Auth, "iotHubConnectionString");

        assertEquals(mockConnectionString, actualConnectionString);
        new Verifications()
        {
            {
                new X509CertificatePair(publicKeyCertificate, false, privateKey, false);
                times = 1;
                new IotHubSSLContext(publicKeyCertificate, privateKey);
                times = 1;
            }
        };
    }

    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_004: [This function shall return the saved x509 certificate pair.]
    @Test
    public void getX509CertificatePairGets(@Mocked final X509CertificatePair otherMockCertificatePair) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException
    {
        //arrange
        IotHubX509Authentication x509Auth = new IotHubX509Authentication(mockConnectionString, publicKeyCertificate, false, privateKey, false);
        Deencapsulation.setField(x509Auth, "x509CertificatePair", otherMockCertificatePair);

        //act
        X509CertificatePair actualSSLContext = x509Auth.getX509CertificatePair();

        //assert
        assertEquals(otherMockCertificatePair, actualSSLContext);
    }

    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_005: [This function shall return the saved IotHubSSLContext.]
    @Test
    public void getIotHubSSLContextGets(@Mocked final IotHubSSLContext otherMockIotHubSSLContext) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException
    {
        //arrange
        IotHubX509Authentication x509Auth = new IotHubX509Authentication(mockConnectionString, publicKeyCertificate, false, privateKey, false);
        Deencapsulation.setField(x509Auth, "iotHubSSLContext", otherMockIotHubSSLContext);

        //act
        IotHubSSLContext actualSSLContext = x509Auth.getIotHubSSLContext();

        //assert
        assertEquals(otherMockIotHubSSLContext, actualSSLContext);
    }

    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_006: [If the provided iotHubSSLContext is null, this function shall throw an IllegalArgumentException.]
    @Test (expected = IllegalArgumentException.class)
    public void setIotHubSSLContextThrowsForNullContext() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException
    {
        //act
        new IotHubX509Authentication(mockConnectionString, publicKeyCertificate, false, privateKey, false).setIotHubSSLContext(null);
    }

    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_007: [This function shall save the provided IotHubSSLContext.]
    @Test
    public void setIotHubSSLContextSets(@Mocked final IotHubSSLContext otherMockSSLContext) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException
    {
        //arrange
        IotHubX509Authentication x509Auth = new IotHubX509Authentication(mockConnectionString, publicKeyCertificate, false, privateKey, false);

        //act
        x509Auth.setIotHubSSLContext(otherMockSSLContext);

        //assert
        IotHubSSLContext actualSSLContext = x509Auth.getIotHubSSLContext();
        assertEquals(otherMockSSLContext, actualSSLContext);
    }

    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_008: [This function shall return the saved IotHubConnectionString.]
    @Test
    public void getIotHubConnectionStringGets(@Mocked final IotHubConnectionString otherMockConnectionString) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException
    {
        //arrange
        IotHubX509Authentication x509Auth = new IotHubX509Authentication(mockConnectionString, publicKeyCertificate, false, privateKey, false);
        Deencapsulation.setField(x509Auth, "iotHubConnectionString", otherMockConnectionString);

        //act
        IotHubConnectionString actualConnectionString = x509Auth.getIotHubConnectionString();

        //assert
        assertEquals(otherMockConnectionString, actualConnectionString);
    }

    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_009: [If the provided iotHubConnectionString is null, this function shall throw an IllegalArgumentException.]
    @Test (expected = IllegalArgumentException.class)
    public void setIotHubConnectionStringThrowsForNullConnectionString() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException
    {
        //act
        new IotHubX509Authentication(mockConnectionString, publicKeyCertificate, false, privateKey, false).setIotHubConnectionString(null);
    }

    //Tests_SRS_IOTHUBX509AUTHENTICATION_34_010: [This function shall save the provided iotHubConnectionString.]
    @Test
    public void setIotHubConnectionStringSets(@Mocked final IotHubConnectionString otherMockConnectionString) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException
    {
        //arrange
        IotHubX509Authentication x509Auth = new IotHubX509Authentication(mockConnectionString, publicKeyCertificate, false, privateKey, false);

        //act
        x509Auth.setIotHubConnectionString(otherMockConnectionString);

        //assert
        IotHubConnectionString actualConnectionString = x509Auth.getIotHubConnectionString();
        assertEquals(otherMockConnectionString, actualConnectionString);
    }
}
