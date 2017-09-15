# IotHubSSLContext Requirements

## Overview

This class creates ssl context to be used to secure all the underlying transport.

## References

## Exposed API

```java
public final class IotHubSSLContext
{
    IotHubSSLContext() throws KeyManagementException, IOException, CertificateException;

    IotHubSSLContext(String publicKeyCertificateString, String privateKeyString)
            throws KeyManagementException, IOException, CertificateException, KeyStoreException;

    public SSLContext getIotHubSSLContext();
}
```


### IotHubSSLContext

```java
protected IotHubSSLContext() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, CertificateException;
```

**SRS_IOTHUBSSLCONTEXT_25_001: [**The constructor shall create a default certificate to be used with IotHub.**]**

**SRS_IOTHUBSSLCONTEXT_25_002: [**The constructor shall create default SSL context for TLSv1.2.**]**

**SRS_IOTHUBSSLCONTEXT_25_003: [**The constructor shall create default TrustManagerFactory with the default algorithm.**]**

**SRS_IOTHUBSSLCONTEXT_25_004: [**The constructor shall create default KeyStore instance with the default type and initialize it.**]**

**SRS_IOTHUBSSLCONTEXT_25_005: [**The constructor shall set the above created certificate into a keystore.**]**

**SRS_IOTHUBSSLCONTEXT_25_006: [**The constructor shall initialize TrustManagerFactory with the above initialized keystore.**]**

**SRS_IOTHUBSSLCONTEXT_25_007: [**The constructor shall initialize SSL context with the above initialized TrustManagerFactory and a new secure random.**]**


### IotHubSSLContext

```java
IotHubSSLContext(String publicKeyCertificateString, String privateKeyString);
```

**SRS_IOTHUBSSLCONTEXT_34_018: [**This constructor shall generate a temporary password to protect the created keystore holding the private key.**]**

**SRS_IOTHUBSSLCONTEXT_34_019: [**The constructor shall create default SSL context for TLSv1.2.**]**

**SRS_IOTHUBSSLCONTEXT_34_020: [**The constructor shall create a keystore containing the public key certificate and the private key.**]**

**SRS_IOTHUBSSLCONTEXT_34_021: [**The constructor shall initialize a default trust manager factory that accepts communications from Iot Hub.**]**

**SRS_IOTHUBSSLCONTEXT_34_024: [**The constructor shall initialize SSL context with its initialized keystore, its initialized TrustManagerFactory and a new secure random.**]**


### getIotHubSSLContext

```java
public SSLContext getIotHubSSLContext();
```

**SRS_IOTHUBSSLCONTEXT_25_017: [*This method shall return the value of sslContext.**]**