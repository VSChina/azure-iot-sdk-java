# IotHubSasTokenAuthentication Requirements

## Overview

This class holds all the authentication information needed for a device to connect to a Iot Hub using sas tokens or a device key

## References

## Exposed API

```java
public class IotHubSasTokenAuthentication
{
    public IotHubSasTokenAuthentication(IotHubConnectionString connectionString) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException;
    public String getSasToken();
    public void setSasToken(IotHubSasToken sasToken) throws IllegalArgumentException;
    public IotHubSSLContext getIotHubSSLContext();
    public void setIotHubSSLContext(IotHubSSLContext iotHubSSLContext) throws IllegalArgumentException;
    public long getTokenValidSecs();
    public void setTokenValidSecs(long tokenValidSecs) throws IllegalArgumentException;
    public IotHubConnectionString getConnectionString();
    public void setConnectionString(IotHubConnectionString connectionString) throws IllegalArgumentException;
    public boolean needsToRenewSasToken();
}
```

### IotHubSasTokenAuthentication
```java
public IotHubSasTokenAuthentication(IotHubConnectionString connectionString);
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_002: [**This constructor shall save the provided connection string.**]**

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_003: [**This constructor shall generate a default IotHubSSLContext.**]**

### getSasToken
```java
public String getSasToken();
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_004: [**If the saved sas token has expired and there is a device key present, the saved sas token shall be renewed.**]**

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_005: [**This function shall return the saved sas token.**]**


### setSasToken
```java
public void setSasToken(IotHubSasToken sasToken);
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_006: [**This function shall save the provided sas token.**]**

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_007: [**If the provided sas token is null, an IllegalArgumentException shall be thrown.**]**


### getIotHubSSLContext
```java
public IotHubSSLContext getIotHubSSLContext();
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_008: [**This function shall return the generated IotHubSSLContext.**]**


### setIotHubSSLContext
```java
public void setIotHubSSLContext(IotHubSSLContext iotHubSSLContext);
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_009: [**This function shall save the provided IotHubSSLContext.**]**

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_010: [**If the provided iotHubSSLContext is null, an IllegalArgumentException shall be thrown.**]**


### getTokenValidSecs
```java
public long getTokenValidSecs();
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_011: [**This function shall return the number of seconds that tokens are valid for.**]**


### setTokenValidSecs
```java
public void setTokenValidSecs(long tokenValidSecs);
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_012: [**This function shall save the provided tokenValidSecs as the number of seconds that created sas tokens are valid for.**]**


### getConnectionString
```java
public IotHubConnectionString getConnectionString();
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_013: [**This function shall return the current connection string.**]**


### setConnectionString
```java
public void setConnectionString(IotHubConnectionString connectionString);
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_014: [**This function shall save the provided connection string.**]**

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_015: [**If the provided connection string is null, an IllegalArgumentException shall be thrown.**]**

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_016: [**If the provided connection string is missing both a device key and a sas token, an IllegalArgumentException shall be thrown.**]**


### needsToRenewSasToken
```java
public boolean needsToRenewSasToken();
```

**SRS_IOTHUBSASTOKENAUTHENTICATION_34_017: [**If the saved sas token has expired and cannot be renewed, this function shall return true.**]**
