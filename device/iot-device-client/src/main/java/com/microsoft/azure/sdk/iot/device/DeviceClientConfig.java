// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.sdk.iot.device;

import com.microsoft.azure.sdk.iot.device.auth.*;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * Configuration settings for an IoT Hub client. Validates all user-defined
 * settings.
 */
public final class DeviceClientConfig
{
    /** The default value for readTimeoutMillis. */
    private static final int DEFAULT_READ_TIMEOUT_MILLIS = 240000;
    /** The default value for messageLockTimeoutSecs. */
    private static final int DEFAULT_MESSAGE_LOCK_TIMEOUT_SECS = 180;

    private boolean useWebsocket;

    private IotHubX509Authentication x509Authentication;
    private IotHubSasTokenAuthentication sasTokenAuthentication;

    /**
     * The callback to be invoked if a message of Device Method type received.
     */
    private MessageCallback deviceMethodsMessageCallback;
    /** The context to be passed in to the device method type message callback. */
    private Object deviceMethodsMessageContext;

    /**
     * The callback to be invoked if a message of Device Twin type received.
     */
    private MessageCallback deviceTwinMessageCallback;
    /** The context to be passed in to the device twin type message callback. */
    private Object deviceTwinMessageContext;

    /**
     * The callback to be invoked if a message is received.
     */
    private MessageCallback deviceTelemetryMessageCallback;
    /** The context to be passed in to the message callback. */
    private Object deviceTelemetryMessageContext;

    private CustomLogger logger;

    public enum AuthType
    {
        X509_CERTIFICATE,
        SAS_TOKEN
    }

    private AuthType authenticationType;

    /**
     * Constructor
     *
     * @param iotHubConnectionString is the string with the hostname, deviceId, and
     *                               deviceKey or token, which identify the device in
     *                               the Azure IotHub.
     * @throws IllegalArgumentException if the IoT Hub hostname does not contain
     * a valid IoT Hub name as its prefix.
     * @throws IOException if any exception occurs when creating the authentication layer for this config
     */
    public DeviceClientConfig(IotHubConnectionString iotHubConnectionString) throws IOException, IllegalArgumentException
    {
        // Codes_SRS_DEVICECLIENTCONFIG_21_034: [If the provided `iotHubConnectionString` is null,
        // the constructor shall throw IllegalArgumentException.]
        if(iotHubConnectionString == null)
        {
            throw new IllegalArgumentException("connection string cannot be null");
        }

        if (iotHubConnectionString.isUsingX509())
        {
            throw new IllegalArgumentException("Cannot use this constructor for x509 connection strings. Use constructor that takes public key certificate and private key instead");
        }

        this.useWebsocket = false;

        //Codes_SRS_DEVICECLIENTCONFIG_34_046: [If the provided `iotHubConnectionString` does not use x509 authentication, it shall be saved to a new IotHubSasTokenAuthentication object and the authentication type of this shall be set to SASToken.]
        this.authenticationType = AuthType.SAS_TOKEN;

        try
        {
            this.sasTokenAuthentication = new IotHubSasTokenAuthentication(iotHubConnectionString);
        }
        catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_048: [If an exception is thrown when creating the appropriate Authentication object, an IOException shall be thrown containing the details of that exception.]
            throw new IOException(e.getCause());
        }

        this.logger = new CustomLogger(this.getClass());
        logger.LogInfo("DeviceClientConfig object is created successfully with IotHubName=%s, deviceID=%s , method name is %s ",
                iotHubConnectionString.getHostName(), iotHubConnectionString.getDeviceId(), logger.getMethodName());
    }

    /**
     * Constructor for device configs that use x509 authentication
     *
     * @param iotHubConnectionString The connection string for the device. (format: "HostName=<hostname>;DeviceId=<device id>;x509=true")
     * @param publicKeyCertificate The PEM encoded public key certificate or the path to the PEM encoded public key certificate file
     * @param isPathForPublic If the provided publicKeyCertificate is a path to the actual public key certificate
     * @param privateKey The PEM encoded private key or the path to the PEM encoded private key file
     * @param isPathForPrivate If the provided privateKey is a path to the actual private key
     * @throws IOException If any exception is thrown when creating the SSL Layer with the provided certs
     */
    public DeviceClientConfig(IotHubConnectionString iotHubConnectionString, String publicKeyCertificate, boolean isPathForPublic, String privateKey, boolean isPathForPrivate) throws IOException
    {
        if(iotHubConnectionString == null)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_069: [If the provided connection string is null or does not use x509 auth, and IllegalArgumentException shall be thrown.]
            throw new IllegalArgumentException("connection string cannot be null");
        }

        if (!iotHubConnectionString.isUsingX509())
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_069: [If the provided connection string is null or does not use x509 auth, and IllegalArgumentException shall be thrown.]
            throw new IllegalArgumentException("Cannot use this constructor for connection strings that don't use x509 authentication.");
        }

        this.useWebsocket = false;

        try
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_069: [This function shall generate a new SSLContext and set this to using X509 authentication.]
            this.x509Authentication = new IotHubX509Authentication(iotHubConnectionString, publicKeyCertificate, isPathForPublic, privateKey, isPathForPrivate);
            this.authenticationType = AuthType.X509_CERTIFICATE;
        }
        catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | UnrecoverableKeyException e)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_070: [If any exceptions are encountered while generating the new SSLContext, an IOException shall be thrown.]
            throw new IOException(e.getCause());
        }

        this.logger = new CustomLogger(this.getClass());
        logger.LogInfo("DeviceClientConfig object is created successfully with IotHubName=%s, deviceID=%s , method name is %s ",
                iotHubConnectionString.getHostName(), iotHubConnectionString.getDeviceId(), logger.getMethodName());
    }

    /**
     * Getter for Websocket
     * @return true if set, false otherwise
     */
    public boolean isUseWebsocket()
    {
        //Codes_SRS_DEVICECLIENTCONFIG_25_037: [The function shall return the true if websocket is enabled, false otherwise.]
        return this.useWebsocket;
    }

    /**
     * Setter for Websocket
     * @param useWebsocket true if to be set, false otherwise
     */
    public void setUseWebsocket(boolean useWebsocket)
    {
        //Codes_SRS_DEVICECLIENTCONFIG_25_038: [The function shall save useWebsocket.]
        this.useWebsocket = useWebsocket;
    }

    /**
     * Getter for the IotHubSSLContext
     * @return IotHubSSLContext for this IotHub
     */
    public IotHubSSLContext getIotHubSSLContext()
    {
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_051: [If this is using Sas token Authentication, then this function shall return the IotHubSSLContext saved in its Sas token authentication object.]
            return this.sasTokenAuthentication.getIotHubSSLContext();
        }
        else
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_052: [If this is using X509 Authentication, then this function shall return the IotHubSSLContext saved in its X509 authentication object.]
            return this.x509Authentication.getIotHubSSLContext();
        }
    }

    /**
     * Setter for the message callback. Can be {@code null}.
     * @param callback the message callback. Can be {@code null}.
     * @param context the context to be passed in to the callback.
     */
    public void setMessageCallback(MessageCallback callback,
                                   Object context)
    {
        // Codes_SRS_DEVICECLIENTCONFIG_11_006: [The function shall set the message callback, with its associated context.]
        this.deviceTelemetryMessageCallback = callback;
        this.deviceTelemetryMessageContext = context;
    }

    /**
     * Getter for X509PrivateKey
     *
     * @return The value of X509PrivateKey
     */
    X509CertificatePair getX509CertificatePair()
    {
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_056: [If this is using sas token authentication, this function shall return null.]
            return null;
        }
        else
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_045: [If this is using x509 authentication, this function shall return this object's x509 certificate pair.]
            return this.x509Authentication.getX509CertificatePair();
        }
    }

    /**
     * Getter for the IoT Hub hostname.
     * @return the IoT Hub hostname.
     */
    public String getIotHubHostname()
    {
        // Codes_SRS_DEVICECLIENTCONFIG_11_002: [The function shall return the IoT Hub hostname given in the constructor.]
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            return this.sasTokenAuthentication.getConnectionString().getHostName();
        }
        else
        {
            return this.x509Authentication.getIotHubConnectionString().getHostName();
        }
    }

    /**
     * Getter for the IoT Hub name.
     * @return the IoT Hub name.
     */
    public String getIotHubName()
    {
        // Codes_SRS_DEVICECLIENTCONFIG_11_007: [The function shall return the IoT Hub name given in the constructor, where the IoT Hub name is embedded in the IoT Hub hostname as follows: [IoT Hub name].[valid HTML chars]+.]
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            return this.sasTokenAuthentication.getConnectionString().getHubName();
        }
        else
        {
            return this.x509Authentication.getIotHubConnectionString().getHubName();
        }
    }

    /**
     * Getter for the device ID.
     *
     * @return the device ID.
     */
    public String getDeviceId()
    {
        // Codes_SRS_DEVICECLIENTCONFIG_11_003: [The function shall return the device ID given in the constructor.]
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            return this.sasTokenAuthentication.getConnectionString().getDeviceId();
        }
        else
        {
            return this.x509Authentication.getIotHubConnectionString().getDeviceId();
        }
    }

    /**
     * Getter for the device key.
     *
     * @return the device key.
     */
    public String getDeviceKey()
    {
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            // Codes_SRS_DEVICECLIENTCONFIG_11_004: [If this is using Sas token authentication, the function shall return the device key given in the constructor.]
            return this.sasTokenAuthentication.getConnectionString().getSharedAccessKey();
        }
        else
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_054: [If this is using x509 authentication, the function shall return null.]
            return null;
        }
    }

    /**
     * Getter for the shared access signature.
     *
     * @return the shared access signature.
     */
    public String getSharedAccessToken() throws SecurityException
    {
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            // Codes_SRS_DEVICECLIENTCONFIG_25_018: [If this is using sas token authentication, the function shall return the SharedAccessToken saved in the sas token authentication object..]
            return this.sasTokenAuthentication.getSasToken();
        }
        else
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_053: [If this is using x509 authentication, the function shall return null.]
            return null;
        }
    }

    /**
     * Getter for the number of seconds a SAS token should be valid for. A
     * message that arrives at an IoT Hub in time of length greater than this
     * value will be rejected by the IoT Hub.
     *
     * @return the number of seconds a message in transit to an IoT Hub is valid
     * for.
     */
    public long getTokenValidSecs()
    {
        // Codes_SRS_DEVICECLIENTCONFIG_11_005: [If this is using Sas token authentication, then this function shall return the value of tokenValidSecs saved in it and 0 otherwise.]
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            return this.sasTokenAuthentication.getTokenValidSecs();
        }
        else
        {
            return 0L;
        }
    }

    /**
     * Setter for the number of seconds a SAS token should be valid for. A
     * message that arrives at an IoT Hub in time of length greater than this
     * value will be rejected by the IoT Hub.
     *
     * @param expiryTime is the token valid time in seconds.
     */
    public void setTokenValidSecs(long expiryTime)
    {
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            // Codes_SRS_DEVICECLIENTCONFIG_25_008: [The function shall set the value of tokenValidSecs.]
            this.sasTokenAuthentication.setTokenValidSecs(expiryTime);
        }
    }

    /**
     * Getter for the timeout, in milliseconds, after a connection is
     * established for the server to respond to the request.
     *
     * @return the timeout, in milliseconds, after a connection is established
     * for the server to respond to the request.
     */
    public int getReadTimeoutMillis()
    {
        // Codes_SRS_DEVICECLIENTCONFIG_11_012: [The function shall return 240000ms.]
        return DEFAULT_READ_TIMEOUT_MILLIS;
    }

    /**
     * Getter for the message callback.
     *
     * @return the message callback.
     */
    public MessageCallback getDeviceTelemetryMessageCallback()
    {
        // Codes_SRS_DEVICECLIENTCONFIG_11_010: [The function shall return the current message callback.]
        return this.deviceTelemetryMessageCallback;
    }

    /**
     * Getter for the context to be passed in to the message callback.
     *
     * @return the message context.
     */
    public Object getDeviceTelemetryMessageContext()
    {
        // Codes_SRS_DEVICECLIENTCONFIG_11_011: [The function shall return the current message context.]
        return this.deviceTelemetryMessageContext;
    }

    /**
     * Setter for the device method message callback.
     *
     * @param callback Callback for device method messages.
     * @param context is the context for the callback.
     */
    public void setDeviceMethodsMessageCallback(MessageCallback callback, Object context)
    {
        /*
        Codes_SRS_DEVICECLIENTCONFIG_25_023: [**The function shall set the DeviceMethod message callback.**] **
         */
        this.deviceMethodsMessageCallback = callback;

        /*
        Codes_SRS_DEVICECLIENTCONFIG_25_022: [**The function shall return the current DeviceMethod message context.**] **
         */
        this.deviceMethodsMessageContext = context;
    }

    /**
     * Getter for the device twin message callback.
     *
     * @return the device method message callback.
     */
    public MessageCallback getDeviceMethodsMessageCallback()
    {
        /*
        Codes_SRS_DEVICECLIENTCONFIG_25_021: [**The function shall return the current DeviceMethod message callback.**] **
         */
        return this.deviceMethodsMessageCallback;
    }

    /**
     * Getter for the context to be passed in to the device twin message callback.
     *
     * @return the device method message context.
     */
    public Object getDeviceMethodsMessageContext()
    {
        /*
        Codes_SRS_DEVICECLIENTCONFIG_25_022: [**The function shall return the current DeviceMethod message context.**] **
         */
        return this.deviceMethodsMessageContext;
    }

    /**
     * Setter for the device twin message callback.
     *
     * @param callback callback to be invoked for device twin messages.
     * @param context is the context for the callback.
     */
    public void setDeviceTwinMessageCallback(MessageCallback callback, Object context)
    {
        /*
        Codes_SRS_DEVICECLIENTCONFIG_25_023: [**The function shall set the DeviceTwin message callback.**] **
         */
        this.deviceTwinMessageCallback = callback;
        /*
        Codes_SRS_DEVICECLIENTCONFIG_25_024: [**The function shall set the DeviceTwin message context.**] **
         */
        this.deviceTwinMessageContext = context;
    }

    /**
     * Getter for the device twin message callback.
     *
     * @return the device twin message callback.
     */
    public MessageCallback getDeviceTwinMessageCallback()
    {
        /*
        Codes_SRS_DEVICECLIENTCONFIG_25_025: [**The function shall return the current DeviceTwin message callback.**] **
         */
        return this.deviceTwinMessageCallback;
    }

    /**
     * Getter for the context to be passed in to the device twin message callback.
     *
     * @return the device twin message context.
     */
    public Object getDeviceTwinMessageContext()
    {
        /*
        Codes_SRS_DEVICECLIENTCONFIG_25_026: [**The function shall return the current DeviceTwin message context.**] **
         */
        return this.deviceTwinMessageContext;
    }

    /**
     * Getter for the timeout, in seconds, for the lock that the client has on a
     * received message.
     *
     * @return the timeout, in seconds, for a received message lock.
     */
    public int getMessageLockTimeoutSecs()
    {
        // Codes_SRS_DEVICECLIENTCONFIG_11_013: [The function shall return 180s.]
        return DEFAULT_MESSAGE_LOCK_TIMEOUT_SECS;
    }

    /**
     * Tells if the config needs to get a new sas token. If a device key is present in config, no token refresh is needed.
     * @return if the config needs a new sas token.
     */
    public boolean needsToRenewSasToken()
    {
        if (this.authenticationType == AuthType.SAS_TOKEN)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_049: [If this is using SAS token authentication, this function shall return if that SAS Token authentication needs renewal.]
            return this.sasTokenAuthentication.needsToRenewSasToken();
        }
        else
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_050: [If this isn't using SAS token authentication, this function shall return false.]
            return false;
        }
    }

    /*
     * Getter for AuthenticationType
     *
     * @return The value of AuthenticationType
     */
    public AuthType getAuthenticationType()
    {
        //Codes_SRS_DEVICECLIENTCONFIG_34_039: [This function shall return the type of authentication that the config is set up to use.]
        return authenticationType;
    }

    /**
     * Setter for the providing trusted certificate.
     * @param pathToCertificate path to the certificate for one way authentication.
     * @deprecated as of release 1.4.35 this function is deprecated and is replaced by the constructor that takes a public key certificate and private key.
     * @throws IOException if there was an exception thrown when creating the SSL Context with the new certificate
     * @throws IllegalStateException if this function is called when this is using sas token authentication
     */
    @Deprecated
    public void setPathToCert(String pathToCertificate) throws IOException, IllegalStateException
    {
        if (this.authenticationType == authenticationType.SAS_TOKEN)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_059: [If this function is called while this is using Sas token authentication, an IllegalStateException shall be thrown.]
            throw new IllegalStateException("Cannot set only the public key certificate when using Sas token authentication.");
        }
        else
        {
            try
            {
                //Codes_SRS_DEVICECLIENTCONFIG_34_060: [This function shall regenerate it's SSL context using the new public key certificate.]
                this.x509Authentication = new IotHubX509Authentication(this.x509Authentication.getIotHubConnectionString(), pathToCertificate, true, this.x509Authentication.getX509CertificatePair().getPrivateKey(), false);
            }
            catch (Exception e)
            {
                //Codes_SRS_DEVICECLIENTCONFIG_34_061: [If any exceptions are thrown while creating a new SSL context with the new public key certificate, this function shall throw an IOException.]
                throw new IOException(e.getCause());
            }
        }
    }

    /**
     * Getter for the path to the certificate. If using sas token authentication, this function shall return false
     * @deprecated as of release 1.4.35 this function is deprecated and is replaced by getX509CertificatePair.
     * @return the path to certificate.
     */
    @Deprecated
    public String getPathToCertificate()
    {
        if (this.authenticationType == authenticationType.SAS_TOKEN)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_063: [If this function is called while this is using Sas token authentication, null shall be returned.]
            return null;
        }
        else
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_062: [This function shall return the saved path to the public key certificate or null if it is not saved.]
            return this.x509Authentication.getX509CertificatePair().getPathToPublicKeyCertificate();
        }
    }

    /**
     * Setter for the user trusted certificate
     * @param userCertificateString valid user trusted certificate string
     * @deprecated as of release 1.4.35 this function is deprecated and is replaced by the constructor that takes a public key certificate and private key.
     * @throws IOException if there is an exception thrown while creating the SSL context using the new certificate string
     * @throws IllegalStateException if this function is called when the config is using sas token authentication
     */
    @Deprecated
    public void setUserCertificateString(String userCertificateString) throws IOException , IllegalStateException
    {
        if (this.authenticationType == authenticationType.SAS_TOKEN)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_064: [If this function is called while this is using Sas token authentication, an IllegalStateException shall be thrown.]
            throw new IllegalStateException("Cannot set only the public key certificate when using Sas token authentication.");
        }
        else
        {
            try
            {
                //Codes_SRS_DEVICECLIENTCONFIG_34_065: [This function shall create a new SSL context using the new public key certificate.]
                this.x509Authentication = new IotHubX509Authentication(this.x509Authentication.getIotHubConnectionString(), userCertificateString, false, this.x509Authentication.getX509CertificatePair().getPrivateKey(), false);
            }
            catch (Exception e)
            {
                //Codes_SRS_DEVICECLIENTCONFIG_34_066: [If any exceptions are thrown while creating a new SSL context with the new public key certificate, this function shall throw an IOException.]
                throw new IOException(e.getCause());
            }
        }
    }

    /**
     * Getter for the user trusted certificate
     * @deprecated as of release 1.4.35 this function is deprecated and is replaced by getX509CertificatePair.
     * @return user trusted certificate as string
     */
    @Deprecated
    public String getUserCertificateString()
    {
        if (this.authenticationType == authenticationType.SAS_TOKEN)
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_067: [If this function is called while this is using Sas token authentication, null shall be returned.]
            return null;
        }
        else
        {
            //Codes_SRS_DEVICECLIENTCONFIG_34_068: [This function shall return the saved user certificate string.]
            return this.x509Authentication.getX509CertificatePair().getPublicKeyCertificate();
        }
    }

    @SuppressWarnings("unused")
    protected DeviceClientConfig(){}
}
