/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package com.microsoft.azure.sdk.iot.device.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class X509CertificatePair
{
    private String publicKeyCertificate;
    private String privateKey;

    private String pathToPublicKeyCertificate;
    private String pathToPrivateKey;

    /**
     * Constructor that takes a public key certificate and a private key.
     * @throws IOException if there was an exception thrown reading the certificate or key from a file
     * @throws IllegalArgumentException if the public key certificate or private key is null or empty
     */
    public X509CertificatePair(String publicKeyCertificate, boolean isPathForPublic, String privateKey, boolean isPathForPrivate) throws IOException, IllegalArgumentException
    {
        if (publicKeyCertificate == null || publicKeyCertificate.isEmpty())
        {
            //Codes_SRS_X509CERTIFICATEPAIR_34_001: [If the provided public key certificate or private key is null or empty, an IllegalArgumentException shall be thrown.]
            throw new IllegalArgumentException("Public key certificate cannot be null or empty");
        }

        if (privateKey == null || privateKey.isEmpty())
        {
            //Codes_SRS_X509CERTIFICATEPAIR_34_001: [If the provided public key certificate or private key is null or empty, an IllegalArgumentException shall be thrown.]
            throw new IllegalArgumentException("Private key certificate cannot be null or empty");
        }

        if (isPathForPublic)
        {
            //Codes_SRS_X509CERTIFICATEPAIR_34_013: [If a path is provided for the public key certificate, the path will be saved and the contents of the file shall be read and saved as a string.]
            this.publicKeyCertificate = readFromFile(publicKeyCertificate);
            this.pathToPublicKeyCertificate = publicKeyCertificate;
        }
        else
        {
            //Codes_SRS_X509CERTIFICATEPAIR_34_014: [If the public key certificate is not provided as a path, no path will be saved and the value of the public key certificate will be saved as a string.]
            this.publicKeyCertificate = publicKeyCertificate;
        }

        if (isPathForPrivate)
        {
            //Codes_SRS_X509CERTIFICATEPAIR_34_015: [If a path is provided for the private key, the path will be saved and the contents of the file shall be read and saved as a string.]
            this.privateKey = readFromFile(privateKey);
            this.pathToPrivateKey = privateKey;
        }
        else
        {
            //Codes_SRS_X509CERTIFICATEPAIR_34_016: [If the private key is not provided as a path, no path will be saved and the value of the private key will be saved as a string.]
            this.privateKey = privateKey;
        }
    }

    /**
     * Getter for PublicKeyCertificate
     *
     * @return The value of PublicKeyCertificate
     */
    public String getPublicKeyCertificate()
    {
        //Codes_SRS_X509CERTIFICATEPAIR_34_002: [This method shall return the public key certificate string saved by this object.]
        return this.publicKeyCertificate;
    }

    /**
     * Getter for PrivateKey
     *
     * @return The value of PrivateKey
     */
    public String getPrivateKey()
    {
        //Codes_SRS_X509CERTIFICATEPAIR_34_003: [This method shall return the private key string saved by this object.]
        return this.privateKey;
    }

    /**
     * Getter for PathToPublicKeyCertificate
     *
     * @return The value of PathToPublicKeyCertificate
     */
    public String getPathToPublicKeyCertificate()
    {
        //Codes_SRS_X509CERTIFICATEPAIR_34_011: [This function shall return the saved path to the public key certificate or null if there is not one saved.]
        return pathToPublicKeyCertificate;
    }

    /**
     * Getter for PathToPrivateKey
     *
     * @return The value of PathToPrivateKey
     */
    public String getPathToPrivateKey()
    {
        //Codes_SRS_X509CERTIFICATEPAIR_34_012: [This function shall return the saved path to the private key or null if there is not one saved.]
        return pathToPrivateKey;
    }

    /**
     * Reads from a file into a string.
     * @param path the path to the file
     * @return the contents of the file
     * @throws IOException if an IO error occurs when reading from a file
     */
    private String readFromFile(String path) throws IOException
    {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
