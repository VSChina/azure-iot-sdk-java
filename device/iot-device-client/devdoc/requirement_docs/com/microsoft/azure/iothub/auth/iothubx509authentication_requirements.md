# IotHubX509Authentication Requirements

## Overview

This class holds all the authentication information needed for a device to connect to a Iot Hub using x509 authentication

## References

## Exposed API

```java
public class IotHubX509Authentication
{
    public IotHubX509Authentication(IotHubConnectionString iotHubConnectionString, String publicKeyCertificate, boolean isPathForPublic, String privateKey, boolean isPathForPrivate) throws IOException;
    public X509CertificatePair getX509CertificatePair();
    public IotHubSSLContext getIotHubSSLContext();
    public void setIotHubSSLContext(IotHubSSLContext iotHubSSLContext) throws IllegalArgumentException;
    public IotHubConnectionString getIotHubConnectionString();
    public void setIotHubConnectionString(IotHubConnectionString iotHubConnectionString) throws IllegalArgumentException;
}
```

### IotHubX509Authentication
```java
public IotHubX509Authentication(IotHubConnectionString iotHubConnectionString, String publicKeyCertificate, boolean isPathForPublic, String privateKey, boolean isPathForPrivate);
```

**SRS_IOTHUBX509AUTHENTICATION_34_001: [**This constructor will save the provided connection string.**]**

**SRS_IOTHUBX509AUTHENTICATION_34_002: [**This constructor will create and save an X509CertificatePair object using the provided public key certificate and private key.**]**

**SRS_IOTHUBX509AUTHENTICATION_34_003: [**This constructor will create a new IotHubSSLContext using the provided public key certificate and private key.**]**


### getX509CertificatePair
```java
public X509CertificatePair getX509CertificatePair();
```

**SRS_IOTHUBX509AUTHENTICATION_34_004: [**This function shall return the saved x509 certificate pair.**]**


### getIotHubSSLContext
```java
public IotHubSSLContext getIotHubSSLContext();
```

**SRS_IOTHUBX509AUTHENTICATION_34_005: [**This function shall return the saved IotHubSSLContext.**]**


### setIotHubSSLContext
```java
public void setIotHubSSLContext(IotHubSSLContext iotHubSSLContext);
```

**SRS_IOTHUBX509AUTHENTICATION_34_006: [**If the provided iotHubSSLContext is null, this function shall throw an IllegalArgumentException.**]**

**SRS_IOTHUBX509AUTHENTICATION_34_007: [**This function shall save the provided IotHubSSLContext.**]**


### getIotHubConnectionString
```java
public IotHubConnectionString getIotHubConnectionString();
```

**SRS_IOTHUBX509AUTHENTICATION_34_008: [**This function shall return the saved IotHubConnectionString.**]**


### setIotHubConnectionString
```java
public void setIotHubConnectionString(IotHubConnectionString iotHubConnectionString);
```

**SRS_IOTHUBX509AUTHENTICATION_34_009: [**If the provided iotHubConnectionString is null, this function shall throw an IllegalArgumentException.**]**

**SRS_IOTHUBX509AUTHENTICATION_34_010: [**This function shall save the provided iotHubConnectionString.**]**