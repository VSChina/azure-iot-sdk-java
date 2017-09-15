/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package tests.unit.com.microsoft.azure.sdk.iot.device.auth;

import com.microsoft.azure.sdk.iot.device.auth.X509CertificatePair;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Tests X509CertificatePair
 * Methods: 100%
 * Lines: 100%
 */
public class X509CertificatePairTest
{
    @Mocked
    Paths mockPaths;

    @Mocked
    Files mockFiles;

    @Mocked
    File mockPublicKeyCertFile;

    @Mocked
    File mockPrivateKeyFile;

    @Mocked
    Path mockedPublicKeyCertPath;

    @Mocked
    Path mockedPrivateKeyPath;

    private static final String someCert = "someCert";
    private static final String someKey = "someKey";

    private static final String someCertPath = "someCertPath";
    private static final String someKeyPath = "someKeyPath";

    //Tests_SRS_X509CERTIFICATEPAIR_34_001: [If the provided public key certificate or private key is null or empty, an IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsForEmptyPublicKey() throws IOException
    {
        //act
        new X509CertificatePair("", false, someKey, false);
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_001: [If the provided public key certificate or private key is null or empty, an IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsForEmptyPrivateKey() throws IOException
    {
        //act
        new X509CertificatePair(someCert, false, "", false);
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_001: [If the provided public key certificate or private key is null or empty, an IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsForNullPublicKey() throws IOException
    {
        //act
        new X509CertificatePair(null, false, someKey, false);
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_001: [If the provided public key certificate or private key is null or empty, an IllegalArgumentException shall be thrown.]
    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsForNullPrivateKey() throws IOException
    {
        //act
        new X509CertificatePair(someCert, false, null, false);
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_013: [If a path is provided for the public key certificate, the path will be saved and the contents of the file shall be read and saved as a string.]
    @Test
    public void constructorSavesPublicKeyAndPathWhenPathProvided() throws IOException
    {
        //arrange
        fileReadExpectations();

        //act
        X509CertificatePair certificatePair = new X509CertificatePair(someCertPath, true, someKey, false);

        //assert
        assertEquals(someCertPath, certificatePair.getPathToPublicKeyCertificate());
        assertEquals(someCert, certificatePair.getPublicKeyCertificate());
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_014: [If the public key certificate is not provided as a path, no path will be saved and the value of the public key certificate will be saved as a string.]
    @Test
    public void constructorSavesPublicKeyButNotPathWhenPathNotProvided() throws IOException
    {
        //arrange
        fileReadExpectations();

        //act
        X509CertificatePair certificatePair = new X509CertificatePair(someCert, false, someKey, false);

        //assert
        assertNull(certificatePair.getPathToPublicKeyCertificate());
        assertEquals(someCert, certificatePair.getPublicKeyCertificate());
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_015: [If a path is provided for the private key, the path will be saved and the contents of the file shall be read and saved as a string.]
    @Test
    public void constructorSavesPrivateKeyAndPathWhenPathProvided() throws IOException
    {
        //arrange
        fileReadExpectations();

        //act
        X509CertificatePair certificatePair = new X509CertificatePair(someCert, false, someKeyPath, true);

        //assert
        assertEquals(someKeyPath, certificatePair.getPathToPrivateKey());
        assertEquals(someKey, certificatePair.getPrivateKey());
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_016: [If the private key is not provided as a path, no path will be saved and the value of the private key will be saved as a string.]
    @Test
    public void constructorSavesPrivateKeyButNotPathWhenPathNotProvided() throws IOException
    {
        //arrange
        fileReadExpectations();

        //act
        X509CertificatePair certificatePair = new X509CertificatePair(someCert, false, someKey, false);

        //assert
        assertNull(certificatePair.getPathToPrivateKey());
        assertEquals(someKey, certificatePair.getPrivateKey());
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_002: [This method shall return the public key certificate string saved by this object.]
    @Test
    public void publicKeyCertificateGetter() throws IOException
    {
        //arrange
        X509CertificatePair x509CertificatePair = new X509CertificatePair(someCert, false, someKey, false);
        String expectedPublicKey = "some key";
        Deencapsulation.setField(x509CertificatePair, "publicKeyCertificate", expectedPublicKey);

        //act
        String actualPublicKey = x509CertificatePair.getPublicKeyCertificate();

        //assert
        assertEquals(expectedPublicKey, actualPublicKey);
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_003: [This method shall return the private key string saved by this object.]
    @Test
    public void privateKeyGetter() throws IOException
    {
        //arrange
        X509CertificatePair x509CertificatePair = new X509CertificatePair(someCert, false, someKey, false);
        String expectedPrivateKey = "some key";
        Deencapsulation.setField(x509CertificatePair, "privateKey", expectedPrivateKey);

        //act
        String actualPrivateKey = x509CertificatePair.getPrivateKey();

        //assert
        assertEquals(expectedPrivateKey, actualPrivateKey);
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_011: [This function shall return the saved path to the public key certificate or null if there is not one saved.]
    @Test
    public void publicKeyCertificatePathGetter() throws IOException
    {
        //arrange
        fileReadExpectations();
        X509CertificatePair x509CertificatePair = new X509CertificatePair(someCertPath, true, someKeyPath, true);

        //act
        String actualPublicKeyPath = x509CertificatePair.getPathToPublicKeyCertificate();

        //assert
        assertEquals(someCertPath, actualPublicKeyPath);
    }

    //Tests_SRS_X509CERTIFICATEPAIR_34_012: [This function shall return the saved path to the private key or null if there is not one saved.]
    @Test
    public void privateKeyPathGetter() throws IOException
    {
        //arrange
        fileReadExpectations();
        X509CertificatePair x509CertificatePair = new X509CertificatePair(someCertPath, true, someKeyPath, true);

        //act
        String actualPrivateKeyPath = x509CertificatePair.getPathToPrivateKey();

        //assert
        assertEquals(someKeyPath, actualPrivateKeyPath);
    }

    private void fileReadExpectations() throws IOException
    {
        new NonStrictExpectations()
        {
            {
                Paths.get(someCertPath);
                result = mockedPublicKeyCertPath;

                Files.readAllBytes(mockedPublicKeyCertPath);
                result = someCert.getBytes();

                Paths.get(someKeyPath);
                result = mockedPrivateKeyPath;

                Files.readAllBytes(mockedPrivateKeyPath);
                result = someKey.getBytes();
            }
        };
    }
}
