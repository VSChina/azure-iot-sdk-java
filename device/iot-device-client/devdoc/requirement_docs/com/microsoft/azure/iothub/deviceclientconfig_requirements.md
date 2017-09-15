# DeviceClientConfig Requirements

## Overview

Configuration settings for an IoT Hub device client. Validates all user-defined settings.

## References

## Exposed API

```java
public final class DeviceClientConfig
{
    public DeviceClientConfig(IotHubConnectionString iotHubConnectionString) throws IOException, IllegalArgumentException;
    
    public DeviceClientConfig(IotHubConnectionString iotHubConnectionString, String publicKeyCertificate, boolean isPathForPublic, String privateKey, boolean isPathForPrivate) throws IOException
    
    public boolean isUseWebsocket();
    public void setUseWebsocket(boolean useWebsocket);
    
    public IotHubSSLContext getIotHubSSLContext();
    X509CertificatePair getX509CertificatePair();
    
    public String getIotHubHostname();
    public String getIotHubName();
    public String getDeviceId();
    public String getDeviceKey();
    public String getSharedAccessToken() throws SecurityException;
    public long getTokenValidSecs();
    public void setTokenValidSecs(long expiryTime);
    public int getReadTimeoutMillis();
    
    public void setMessageCallback(MessageCallback callback, Object context);
    public MessageCallback getDeviceTelemetryMessageCallback();
    public Object getDeviceTelemetryMessageContext();
    public void setDeviceMethodsMessageCallback(MessageCallback callback, Object context);
    public MessageCallback getDeviceMethodsMessageCallback();
    public Object getDeviceMethodsMessageContext();
    public void setDeviceTwinMessageCallback(MessageCallback callback, Object context);
    public MessageCallback getDeviceTwinMessageCallback();
    public Object getDeviceTwinMessageContext();
    
    public int getMessageLockTimeoutSecs();
    public boolean needsToRenewSasToken();
    public AuthType getAuthenticationType();
    
    public void setPathToCert(String pathToCertificate) throws IOException, IllegalStateException;
    public String getPathToCertificate();
    public void setUserCertificateString(String userCertificateString) throws IOException , IllegalStateException;
    public String getUserCertificateString();
}
```

### DeviceClientConfig

```java
public DeviceClientConfig(IotHubConnectionString iotHubConnectionString) throws URISyntaxException;
```

** SRS_DEVICECLIENTCONFIG_34_046: [**If the provided `iotHubConnectionString` does not use x509 authentication, it shall be saved to a new IotHubSasTokenAuthentication object and the authentication type of this shall be set to SASToken.**]**

** SRS_DEVICECLIENTCONFIG_34_048: [**If an exception is thrown when creating the appropriate Authentication object, an IOException shall be thrown containing the details of that exception.**]**

** SRS_DEVICECLIENTCONFIG_21_034: [**If the provided `iotHubConnectionString` is null, the constructor shall throw IllegalArgumentException.**] **


```java
public DeviceClientConfig(IotHubConnectionString iotHubConnectionString, String publicKeyCertificate, boolean isPathForPublic, String privateKey, boolean isPathForPrivate) throws IOException
```

** SRS_DEVICECLIENTCONFIG_34_069: [**If the provided connection string is null or does not use x509 auth, and IllegalArgumentException shall be thrown.**] **

** SRS_DEVICECLIENTCONFIG_34_069: [**This function shall generate a new SSLContext and set this to using X509 authentication.**] **

** SRS_DEVICECLIENTCONFIG_34_070: [**If any exceptions are encountered while generating the new SSLContext, an IOException shall be thrown.**] **


### getIotHubHostname

```java
public String getIotHubHostname();
```

** SRS_DEVICECLIENTCONFIG_11_002: [**The function shall return the IoT Hub hostname given in the constructor.**] **


### getIotHubName

```java
public String getIotHubName();
```

** SRS_DEVICECLIENTCONFIG_11_007: [**The function shall return the IoT Hub name given in the constructor, where the IoT Hub name is embedded in the IoT Hub hostname as follows: [IoT Hub name].[valid HTML chars]+.**] ** 


### getDeviceId

```java
public String getDeviceId();
```

** SRS_DEVICECLIENTCONFIG_11_003: [**The function shall return the device ID given in the constructor.**] **


### getDeviceKey

```java
public String getDeviceKey();
```

** SRS_DEVICECLIENTCONFIG_11_004: [**If this is using Sas token authentication, the function shall return the device key given in the constructor.**] **

** SRS_DEVICECLIENTCONFIG_34_054: [**If this is using x509 authentication, the function shall return null.**] **


### getSharedAccessToken

```java
public String getSharedAccessToken();
```

** SRS_DEVICECLIENTCONFIG_25_018: [**If this is using sas token authentication, the function shall return the SharedAccessToken saved in the sas token authentication object.**] **

** SRS_DEVICECLIENTCONFIG_34_053: [**If this is using x509 authentication, the function shall return null.**] **

### getMessageValidSecs

```java
public long getTokenValidSecs();
```

** SRS_DEVICECLIENTCONFIG_11_005: [**If this is using Sas token authentication, then this function shall return the value of tokenValidSecs saved in it and 0 otherwise.**] **

### setTokenValidSecs

```java
public setTokenValidSecs(long expiryTime);
```

** SRS_DEVICECLIENTCONFIG_25_008: [**The function shall set the value of tokenValidSecs.**] **


### setIotHubSSLContext

```java
public void setIotHubSSLContext(IotHubSSLContext iotHubSSLContext);
```

** SRS_DEVICECLIENTCONFIG_34_057: [**If this is using x509 authentication, the function shall save the provided IotHub SSL Context into its x509 authentication object.**] **

** SRS_DEVICECLIENTCONFIG_34_058: [**If this is using sas authentication, the function shall save the provided IotHub SSL Context into its sas authentication object.**] **


### getIotHubSSLContext

```java
public IotHubSSLContext getIotHubSSLContext();
```

** SRS_DEVICECLIENTCONFIG_34_051: [**If this is using Sas token Authentication, then this function shall return the IotHubSSLContext saved in its Sas token authentication object.**] **

** SRS_DEVICECLIENTCONFIG_34_052: [**If this is using X509 Authentication, then this function shall return the IotHubSSLContext saved in its X509 authentication object.**] **


### setMessageCallback

```java
public void setMessageCallback(MessageCallback  callback, Object context);
```

** SRS_DEVICECLIENTCONFIG_11_006: [**The function shall set the message callback, with its associated context.**] ** 


### getReadTimeoutMillis

```java
public int getReadTimeoutMillis();
```

** SRS_DEVICECLIENTCONFIG_11_012: [**The function shall return 240000ms.**] **


### getMessageCallback

```java
public MessageCallback getMessageCallback();
```

** SRS_DEVICECLIENTCONFIG_11_010: [**The function shall return the current message callback.**] ** 


### getMessageContext

```java
public Object getMessageContext();
```

** SRS_DEVICECLIENTCONFIG_11_011: [**The function shall return the current message context.**] **


### getMessageLockTimeoutSecs

```java
public int getMessageLockTimeoutSecs();
```

** SRS_DEVICECLIENTCONFIG_11_013: [**The function shall return 180s.**] **


### setDeviceMethodMessageCallback

```java
public void setDeviceMethodMessageCallback(MessageCallback  callback, Object context);
```

** SRS_DEVICECLIENTCONFIG_25_019: [**The function shall set the DeviceMethod message callback.**] ** 

** SRS_DEVICECLIENTCONFIG_25_020: [**The function shall set the DeviceMethod message context.**] **


### getDeviceMethodMessageCallback

```java
public MessageCallback getDeviceMethodMessageCallback();
```

** SRS_DEVICECLIENTCONFIG_25_021: [**The function shall return the current DeviceMethod message callback.**] ** 


### getDeviceMethodMessageContext

```java
public Object getDeviceMethodMessageContext();
```

** SRS_DEVICECLIENTCONFIG_25_022: [**The function shall return the current DeviceMethod message context.**] **

### setDeviceTwinMessageCallback

```java
public void setDeviceTwinMessageCallback(MessageCallback  callback, Object context);
```

** SRS_DEVICECLIENTCONFIG_25_023: [**The function shall set the DeviceTwin message callback.**] ** 

** SRS_DEVICECLIENTCONFIG_25_024: [**The function shall set the DeviceTwin message context.**] **


### getDeviceTwinMessageCallback

```java
public MessageCallback getDeviceTwinMessageCallback();
```

** SRS_DEVICECLIENTCONFIG_25_025: [**The function shall return the current DeviceTwin message callback.**] ** 


### getDeviceTwinMessageContext

```java
public Object getDeviceTwinMessageContext();
```

** SRS_DEVICECLIENTCONFIG_25_026: [**The function shall return the current DeviceTwin message context.**] **


### needsToRenewSasToken

```java
public boolean needsToRenewSasToken();
```

** SRS_DEVICECLIENTCONFIG_34_049: [**If this is using SAS token authentication, this function shall return if that SAS Token authentication needs renewal.**] **

** SRS_DEVICECLIENTCONFIG_34_050: [**If this isn't using SAS token authentication, this function shall return false.**] **


### isUseWebsocket

```java
public boolean isUseWebsocket();
```

**SRS_DEVICECLIENTCONFIG_25_037: [**The function shall return true if websocket is enabled, false otherwise.**]**

### setUseWebsocket

```java
public void setUseWebsocket(boolean useWebsocket);
```

**SRS_DEVICECLIENTCONFIG_25_038: [**The function shall save `useWebsocket`.**]**


### getAuthenticationType
```java
public AuthType getAuthenticationType();
```

**SRS_DEVICECLIENTCONFIG_34_039: [**This function shall return the type of authentication that the config is set up to use.**]**


### getX509CertificatePair
```java
X509CertificatePair getX509CertificatePair();
```

**SRS_DEVICECLIENTCONFIG_34_045: [**If this is using x509 authentication, this function shall return this object's x509 certificate pair.**]**

**SRS_DEVICECLIENTCONFIG_34_056: [**If this is using sas token authentication, this function shall return null.**]**


### setPathToCert
```java
public void setPathToCert(String pathToCertificate);
```

**SRS_DEVICECLIENTCONFIG_34_059: [**If this function is called while this is using Sas token authentication, an IllegalStateException shall be thrown.**]**

**SRS_DEVICECLIENTCONFIG_34_060: [**This function shall create a new SSL context using the new public key certificate.**]**

**SRS_DEVICECLIENTCONFIG_34_061: [**If any exceptions are thrown while creating a new SSL context with the new public key certificate, this function shall throw an IOException.**]**


### getPathToCertificate
```java
public String getPathToCertificate();
```

**SRS_DEVICECLIENTCONFIG_34_063: [**If this function is called while this is using Sas token authentication, null shall be returned.**]**

**SRS_DEVICECLIENTCONFIG_34_062: [**This function shall return the saved path to the public key certificate or null if it is not saved.**]**


### setUserCertificateString
```java
public void setUserCertificateString(String userCertificateString);
```

**SRS_DEVICECLIENTCONFIG_34_064: [**If this function is called while this is using Sas token authentication, an IllegalStateException shall be thrown.**]**

**SRS_DEVICECLIENTCONFIG_34_065: [**This function shall create a new SSL context using the new public key certificate.**]**

**SRS_DEVICECLIENTCONFIG_34_066: [**If any exceptions are thrown while creating a new SSL context with the new public key certificate, this function shall throw an IOException.**]**


### getUserCertificateString
```java
public String getUserCertificateString();
```

**SRS_DEVICECLIENTCONFIG_34_067: [**If this function is called while this is using Sas token authentication, null shall be returned.**]**

**SRS_DEVICECLIENTCONFIG_34_068: [**This function shall return the saved user certificate string.**]**
