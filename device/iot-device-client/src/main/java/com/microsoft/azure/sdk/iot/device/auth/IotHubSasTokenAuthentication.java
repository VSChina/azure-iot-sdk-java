/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package com.microsoft.azure.sdk.iot.device.auth;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class IotHubSasTokenAuthentication
{
    /**
     * The number of seconds after which the generated SAS token for a message
     * will become invalid. We also use the expiry time, which is computed as
     * {@code currentTime() + DEVICE_KEY_VALID_LENGTH}, as a salt when generating our
     * SAS token. Use {@link #getTokenValidSecs()} instead in case the field becomes
     * configurable later on.
     */
    private long tokenValidSecs = 3600;

    private static final long MILLISECONDS_PER_SECOND = 1000L;
    private static final long MINIMUM_EXPIRATION_TIME_OFFSET = 1L;

    private IotHubSasToken sasToken;
    private IotHubSSLContext iotHubSSLContext;
    private IotHubConnectionString connectionString;

    /**
     * Constructor that takes a connection string containing a sas token or a device key
     *
     * @param connectionString the device connection string
     * @throws KeyStoreException  if no Provider supports a KeyStoreSpi implementation for the specified type or
     *                            if the keystore has not been initialized,
     *                            or the given alias already exists and does not identify an entry containing a trusted certificate,
     *                            or this operation fails for some other reason.
     * @throws KeyManagementException As per https://docs.oracle.com/javase/7/docs/api/java/security/KeyManagementException.html
     * @throws IOException If the certificate provided was null or invalid
     * @throws CertificateException As per https://docs.oracle.com/javase/7/docs/api/java/security/cert/CertificateException.html
     * @throws NoSuchAlgorithmException if the default SSL Context cannot be created
     * @throws IllegalArgumentException if the provided connection string doesn't contain one of either a sas token or device key
     */
    public IotHubSasTokenAuthentication(IotHubConnectionString connectionString) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IllegalArgumentException
    {
        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_002: [This constructor shall save the provided connection string.]
        this.connectionString = connectionString;
        this.sasToken = new IotHubSasToken(connectionString.getHostName(), connectionString.getDeviceId(), connectionString.getSharedAccessKey(), connectionString.getSharedAccessToken(), tokenValidSecs);

        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_003: [This constructor shall generate a default IotHubSSLContext.]
        this.iotHubSSLContext = new IotHubSSLContext();
    }

    /**
     * Getter for SasToken
     *
     * @return The value of SasToken
     */
    public String getSasToken()
    {
        if (this.connectionString.getSharedAccessKey() != null && this.sasToken.isExpired())
        {
            //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_004: [If the saved sas token has expired and there is a device key present, the saved sas token shall be renewed.]
            Long expiryTime = (System.currentTimeMillis() / MILLISECONDS_PER_SECOND) + this.getTokenValidSecs() + MINIMUM_EXPIRATION_TIME_OFFSET;
            IotHubSasToken generatedSasToken = new IotHubSasToken(
                    this.connectionString.getHostName(),
                    this.connectionString.getDeviceId(),
                    this.connectionString.getSharedAccessKey(),
                    this.sasToken.toString(),
                    expiryTime);

            this.connectionString.setSharedAccessToken(generatedSasToken.toString());
            this.sasToken = generatedSasToken;
        }

        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_005: [This function shall return the saved sas token.]
        return this.sasToken.toString();
    }

    /**
     * Setter for SasToken
     *
     * @param sasToken the value to set
     * @throws IllegalArgumentException if sasToken is null
     */
    public void setSasToken(IotHubSasToken sasToken) throws IllegalArgumentException
    {
        if (sasToken == null)
        {
            //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_007: [If the provided sas token is null, an IllegalArgumentException shall be thrown.]
            throw new IllegalArgumentException("sasToken cannot be null");
        }

        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_006: [This function shall save the provided sas token.]
        this.sasToken = sasToken;
    }

    /**
     * Getter for IotHubSSLContext
     *
     * @return The value of IotHubSSLContext
     */
    public IotHubSSLContext getIotHubSSLContext()
    {
        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_008: [This function shall return the generated IotHubSSLContext.]
        return this.iotHubSSLContext;
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
            //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_010: [If the provided iotHubSSLContext is null, an IllegalArgumentException shall be thrown.]
            throw new IllegalArgumentException("iotHubSSLContext cannot be null");
        }

        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_009: [This function shall save the provided IotHubSSLContext.]
        this.iotHubSSLContext = iotHubSSLContext;
    }

    /**
     * Getter for TokenValidSecs
     *
     * @return The value of TokenValidSecs
     */
    public long getTokenValidSecs()
    {
        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_011: [This function shall return the number of seconds that tokens are valid for.]
        return tokenValidSecs;
    }

    /**
     * Setter for TokenValidSecs
     *
     * @param tokenValidSecs the value to set
     * @throws IllegalArgumentException if tokenValidSecs is null
     */
    public void setTokenValidSecs(long tokenValidSecs) throws IllegalArgumentException
    {
        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_012: [This function shall save the provided tokenValidSecs as the number of seconds that created sas tokens are valid for.]
        this.tokenValidSecs = tokenValidSecs;
    }

    /**
     * Getter for ConnectionString
     *
     * @return The value of ConnectionString
     */
    public IotHubConnectionString getConnectionString()
    {
        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_013: [This function shall return the current connection string.]
        return connectionString;
    }

    /**
     * Setter for ConnectionString
     *
     * @param connectionString the value to set
     * @throws IllegalArgumentException if connectionString is null
     */
    public void setConnectionString(IotHubConnectionString connectionString) throws IllegalArgumentException
    {
        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_015: [If the provided connection string is null, an IllegalArgumentException shall be thrown.]
        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_016: [If the provided connection string is missing both a device key and a sas token, an IllegalArgumentException shall be thrown.]
        if (connectionString == null || connectionString.isUsingX509())
        {
            throw new IllegalArgumentException("connectionString cannot be null");
        }

        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_014: [This function shall save the provided connection string.]
        this.connectionString = connectionString;
    }

    /**
     * Returns true if the saved sas token has expired and cannot be auto-renewed through the device key
     * @return if the sas token needs manual renewal
     */
    public boolean needsToRenewSasToken()
    {
        //Codes_SRS_IOTHUBSASTOKENAUTHENTICATION_34_017: [If the saved sas token has expired and cannot be renewed, this function shall return true.]
        return (this.sasToken != null && this.sasToken.isExpired() && this.connectionString.getSharedAccessKey() == null);
    }
}
