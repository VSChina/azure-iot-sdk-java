// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package tests.unit.com.microsoft.azure.sdk.iot.device.auth;

import com.microsoft.azure.sdk.iot.device.auth.IotHubCertificateManager;
import com.microsoft.azure.sdk.iot.device.auth.IotHubSSLContext;
import mockit.*;
import org.bouncycastle.openssl.PEMReader;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/*
 * Unit tests for IotHubSSLContext
 * Code Coverage:
 * Methods: 100%
 * Lines: 95%
 */
public class IotHubSSLContextTest
{
    @Mocked
    private SSLContext mockedSSLContext;

    @Mocked
    private TrustManagerFactory mockedTrustManagerFactory;

    @Mocked
    private KeyStore mockedKeyStore;

    @Mocked
    private IotHubCertificateManager mockedDefaultCert;

    @Mocked
    private Certificate mockedCertificate;

    @Mocked
    private TrustManager[] mockedTrustManager;

    @Mocked
    private SecureRandom mockedSecureRandom;

    private final static Collection<Certificate> testCollection = new LinkedHashSet<Certificate>();

    private void generateSSLContextExpectations() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, CertificateException
    {
        new NonStrictExpectations()
        {
            {
                mockedSSLContext.getInstance(anyString);
                result = mockedSSLContext;
                mockedTrustManagerFactory.getInstance(anyString);
                result = mockedTrustManagerFactory;
                mockedKeyStore.getInstance(anyString);
                result = mockedKeyStore;
                Deencapsulation.invoke(mockedDefaultCert, "getCertificateCollection");
                result = testCollection;
                mockedTrustManagerFactory.getTrustManagers();
                result = mockedTrustManager;
                new SecureRandom();
                result = mockedSecureRandom;
            }
        };
    }

    private void generateSSLContextVerifications() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, CertificateException
    {
        new Verifications()
        {
            {
                mockedKeyStore.load(null);
                times = 1;
                mockedKeyStore.setCertificateEntry(anyString, mockedCertificate);
                minTimes = 1;
                mockedTrustManagerFactory.init(mockedKeyStore);
                times = 1;
                mockedSSLContext.init(null, mockedTrustManager, mockedSecureRandom);
                times = 1;
            }
        };
    }

    //Tests_SRS_IOTHUBSSLCONTEXT_25_001: [**The constructor shall create a default certificate to be used with IotHub.**]**
    //Tests_SRS_IOTHUBSSLCONTEXT_25_002: [**The constructor shall create default SSL context for TLSv1.2.**]**
    //Tests_SRS_IOTHUBSSLCONTEXT_25_003: [**The constructor shall create default TrustManagerFactory with the default algorithm.**]**
    //Tests_SRS_IOTHUBSSLCONTEXT_25_004: [**The constructor shall create default KeyStore instance with the default type and initialize it.**]**
    //Tests_SRS_IOTHUBSSLCONTEXT_25_005: [**The constructor shall set the above created certificate into a keystore.**]**
    //Tests_SRS_IOTHUBSSLCONTEXT_25_006: [**The constructor shall initialize TrustManagerFactory with the above initialized keystore.**]**
    //Tests_SRS_IOTHUBSSLCONTEXT_25_007: [**The constructor shall initialize SSL context with the above initialized TrustManagerFactory and a new secure random.**]**
    @Test
    public void constructorCreatesSSLContext() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, CertificateException
    {
        //arrange
        testCollection.add(mockedCertificate);
        generateSSLContextExpectations();

        //act

        IotHubSSLContext testContext = Deencapsulation.newInstance(IotHubSSLContext.class);

        //assert
        generateSSLContextVerifications();
        assertNotNull(Deencapsulation.invoke(testContext, "getIotHubSSlContext"));
        testCollection.remove(mockedCertificate);

    }

    //Tests_SRS_IOTHUBSSLCONTEXT_25_017: [*This method shall return the value of sslContext.**]**
    @Test
    public void getterGetsContext() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, CertificateException
    {
        //arrange
        testCollection.add(mockedCertificate);
        generateSSLContextExpectations();

        IotHubSSLContext testContext = Deencapsulation.newInstance(IotHubSSLContext.class, new Class[] {});

        //act
        SSLContext testSSLContext = Deencapsulation.invoke(testContext, "getIotHubSSlContext");

        //assert
        generateSSLContextVerifications();
        assertNotNull(testSSLContext);
        testCollection.remove(mockedCertificate);
    }

    //Tests_SRS_IOTHUBSSLCONTEXT_34_018: [This constructor shall generate a temporary password to protect the created keystore holding the private key.]
    //Tests_SRS_IOTHUBSSLCONTEXT_34_019: [The constructor shall create default SSL context for TLSv1.2.]
    //Tests_SRS_IOTHUBSSLCONTEXT_34_020: [The constructor shall create a keystore containing the public key certificate and the private key.]
    //Tests_SRS_IOTHUBSSLCONTEXT_34_021: [The constructor shall initialize a default trust manager factory that accepts communications from Iot Hub.]
    //Tests_SRS_IOTHUBSSLCONTEXT_34_024: [The constructor shall initialize SSL context with its initialized keystore, its initialized TrustManagerFactory and a new secure random.]
    @Test
    public void constructorWithCertAndKeySuccess(@Mocked final PEMReader mockedPemReaderForPublic,
                                                 @Mocked final PEMReader mockedPemReaderForPrivate,
                                                 @Mocked final KeyStore mockedKeyStore,
                                                 @Mocked final KeyPair mockedKeyPair,
                                                 @Mocked final X509Certificate mockedCertificate,
                                                 @Mocked final PrivateKey mockedPrivateKey,
                                                 @Mocked final StringReader mockStringReaderPublic,
                                                 @Mocked final StringReader mockStringReaderPrivate,
                                                 @Mocked final UUID mockUUID,
                                                 @Mocked final KeyManagerFactory mockKeyManagerFactory,
                                                 @Mocked final KeyManager[] mockKeyManagers)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, CertificateException, UnrecoverableKeyException
    {
        //arrange
        final String publicKeyCert = "someCert";
        final String privateKey = "someKey";
        final String temporaryPassword = "00000000-0000-0000-0000-000000000000";

        new NonStrictExpectations()
        {
            {
                new StringReader(privateKey);
                result = mockStringReaderPrivate;

                new StringReader(publicKeyCert);
                result = mockStringReaderPublic;

                new PEMReader(mockStringReaderPrivate);
                result = mockedPemReaderForPrivate;

                new PEMReader(mockStringReaderPublic);
                result = mockedPemReaderForPublic;

                mockedPemReaderForPrivate.readObject();
                result = mockedKeyPair;

                mockedPemReaderForPublic.readObject();
                result = mockedCertificate;

                KeyStore.getInstance(KeyStore.getDefaultType());
                result = mockedKeyStore;

                mockedKeyPair.getPrivate();
                result = mockedPrivateKey;

                KeyManagerFactory.getInstance("SunX509");
                result = mockKeyManagerFactory;

                mockUUID.randomUUID();
                result = UUID.fromString(temporaryPassword);

                Deencapsulation.newInstance(IotHubCertificateManager.class);
                result = mockedDefaultCert;

                mockKeyManagerFactory.getKeyManagers();
                result = mockKeyManagers;

                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                result = mockedTrustManagerFactory;

                mockedTrustManagerFactory.getTrustManagers();
                result = mockedTrustManager;
            }
        };

        final IotHubSSLContext iotHubSSLContext = new IotHubSSLContext(publicKeyCert, privateKey);

        //assert
        new Verifications()
        {
            {
                SSLContext.getInstance("TLSv1.2");
                times = 1;

                Deencapsulation.invoke(iotHubSSLContext, "generateTemporaryPassword");
                times = 1;

                mockedKeyStore.setCertificateEntry(anyString, mockedCertificate);
                times = 1;

                new SecureRandom();
                times = 1;

                Deencapsulation.invoke(iotHubSSLContext, "generateDefaultTrustManagerFactory", new Class[] { IotHubCertificateManager.class, KeyStore.class }, mockedDefaultCert, mockedKeyStore);
                times = 1;

                mockedSSLContext.init(mockKeyManagers, mockedTrustManager, new SecureRandom());
                times = 1;
            }
        };
    }
}
