/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package com.microsoft.azure.sdk.iot.device.auth;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class IotHubX509Authentication
{
    private X509CertificatePair x509CertificatePair;
    private IotHubSSLContext iotHubSSLContext;
    private IotHubConnectionString iotHubConnectionString;

    /**
     * Constructor that takes in a connection string and certificate/private key pair needed to use x509 authentication
     * @param iotHubConnectionString The iot hub device connection string. Expected format is "HostName=<hostname>;DeviceId=<device id>;x509=true"
     * @param publicKeyCertificate The PEM encoded string for the public key certificate or the path to a file containing it
     * @param isPathForPublic If the provided publicKeyCertificate is a path to the PEM encoded public key certificate file
     * @param privateKey The PEM encoded string for the private key or the path to a file containing it.
     * @param isPathForPrivate If the provided privateKey is a path to the PEM encoded private key file
     * @throws IOException if there was an exception thrown reading the certificate or key from a file
     * @throws IllegalArgumentException if the public key certificate or private key is null or empty
     * @throws KeyStoreException  if no Provider supports a KeyStoreSpi implementation for the specified type or
     *                            if the keystore has not been initialized,
     *                            or the given alias already exists and does not identify an entry containing a trusted certificate,
     *                            or this operation fails for some other reason.
     * @throws KeyManagementException As per https://docs.oracle.com/javase/7/docs/api/java/security/KeyManagementException.html
     * @throws CertificateException As per https://docs.oracle.com/javase/7/docs/api/java/security/cert/CertificateException.html
     * @throws NoSuchAlgorithmException if the SSL Context cannot be created
     */
    public IotHubX509Authentication(IotHubConnectionString iotHubConnectionString, String publicKeyCertificate, boolean isPathForPublic, String privateKey, boolean isPathForPrivate) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        //Codes_SRS_IOTHUBX509AUTHENTICATION_34_001: [This constructor will save the provided connection string.]
        this.iotHubConnectionString = iotHubConnectionString;

        //Codes_SRS_IOTHUBX509AUTHENTICATION_34_002: [This constructor will create and save an X509CertificatePair object using the provided public key certificate and private key.]
        this.x509CertificatePair = new X509CertificatePair(publicKeyCertificate, isPathForPublic, privateKey, isPathForPrivate);

        //Codes_SRS_IOTHUBX509AUTHENTICATION_34_003: [This constructor will create a new IotHubSSLContext using the provided public key certificate and private key.]
        this.iotHubSSLContext = new IotHubSSLContext(this.x509CertificatePair.getPublicKeyCertificate(), this.x509CertificatePair.getPrivateKey());
    }

    /**
     * Getter for X509CertificatePair
     *
     * @return The value of X509CertificatePair
     */
    public X509CertificatePair getX509CertificatePair()
    {
        //Codes_SRS_IOTHUBX509AUTHENTICATION_34_004: [This function shall return the saved x509 certificate pair.]
        return x509CertificatePair;
    }

    /**
     * Getter for IotHubSSLContext
     *
     * @return The value of IotHubSSLContext
     */
    public IotHubSSLContext getIotHubSSLContext()
    {
        //Codes_SRS_IOTHUBX509AUTHENTICATION_34_005: [This function shall return the saved IotHubSSLContext.]
        return iotHubSSLContext;
    }

    /**
     * Setter for IotHubSSLContext
     *
     * @param iotHubSSLContext the value to set
     * @throws IllegalArgumentException if iotHubSSLContext is null
     */
    public void setIotHubSSLContext(IotHubSSLContext iotHubSSLContext) throws IllegalArgumentException
    {
        if (iotHubSSLContext == null)
        {
            //Codes_SRS_IOTHUBX509AUTHENTICATION_34_006: [If the provided iotHubSSLContext is null, this function shall throw an IllegalArgumentException.]
            throw new IllegalArgumentException("iotHubSSLContext cannot be null");
        }

        //Codes_SRS_IOTHUBX509AUTHENTICATION_34_007: [This function shall save the provided IotHubSSLContext.]
        this.iotHubSSLContext = iotHubSSLContext;
    }

    /**
     * Getter for IotHubConnectionString
     *
     * @return The value of IotHubConnectionString
     */
    public IotHubConnectionString getIotHubConnectionString()
    {
        //Codes_SRS_IOTHUBX509AUTHENTICATION_34_008: [This function shall return the saved IotHubConnectionString.]
        return iotHubConnectionString;
    }

    /**
     * Setter for IotHubConnectionString
     *
     * @param iotHubConnectionString the value to set
     * @throws IllegalArgumentException if iotHubConnectionString is null
     */
    public void setIotHubConnectionString(IotHubConnectionString iotHubConnectionString) throws IllegalArgumentException
    {
        if (iotHubConnectionString == null)
        {
            //Codes_SRS_IOTHUBX509AUTHENTICATION_34_009: [If the provided iotHubConnectionString is null, this function shall throw an IllegalArgumentException.]
            throw new IllegalArgumentException("iotHubConnectionString cannot be null");
        }

        //Codes_SRS_IOTHUBX509AUTHENTICATION_34_010: [This function shall save the provided iotHubConnectionString.]
        this.iotHubConnectionString = iotHubConnectionString;
    }
}
