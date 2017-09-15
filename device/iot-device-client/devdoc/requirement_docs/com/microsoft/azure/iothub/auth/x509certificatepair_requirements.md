# X509CertificatePair Requirements
 
## Overview

Holds a public key certificate and a private key that are used for x509 authentication.

## References

## Exposed API


```java
public final class X509CertificatePair
{
    public X509CertificatePair(String publicKeyCertificate, boolean isPathForPublic, String privateKey, boolean isPathForPrivate) throws IOException, IllegalArgumentException;
    
    public String getPublicKeyCertificate();
    public String getPrivateKey();
    public String getPathToPrivateKey();
    public String getPathToPublicKeyCertificate();
    public boolean hasNeededKeys();
}
```


### X509CertificatePair

```java
public X509CertificatePair();
```
    
**SRS_X509CERTIFICATEPAIR_34_001: [**If the provided public key certificate or private key is null or empty, an IllegalArgumentException shall be thrown.**]**

**SRS_X509CERTIFICATEPAIR_34_013: [**If a path is provided for the public key certificate, the path will be saved and the contents of the file shall be read and saved as a string.**]**

**SRS_X509CERTIFICATEPAIR_34_014: [**If the public key certificate is not provided as a path, no path will be saved and the value of the public key certificate will be saved as a string.**]**

**SRS_X509CERTIFICATEPAIR_34_015: [**If a path is provided for the private key, the path will be saved and the contents of the file shall be read and saved as a string.**]**

**SRS_X509CERTIFICATEPAIR_34_016: [**If the private key is not provided as a path, no path will be saved and the value of the private key will be saved as a string.**]**


### getPublicKeyCertificate

```java
public String getPublicKeyCertificate();
```

**SRS_X509CERTIFICATEPAIR_34_002: [**This method shall return the public key certificate string saved by this object.**]**


### getPrivateKey

```java
public String getPrivateKey();
```

**SRS_X509CERTIFICATEPAIR_34_003: [**This method shall return the private key string saved by this object.**]**


### setPublicKeyCertificate

```java
public void setPublicKeyCertificate(String publicKeyCertificate, boolean isPath);
```

**SRS_X509CERTIFICATEPAIR_34_004: [**If 'isPath' is false, this method shall save the provided string as the public key certificate.**]**
**SRS_X509CERTIFICATEPAIR_34_005: [**If 'isPath' is true, this method shall find the file in the path specified by the provided string and save that file's contents as the public key certificate.**]**
**SRS_X509CERTIFICATEPAIR_34_006: [**If the provided publicKeyCertificate is null or empty, an IllegalArgumentException shall be thrown.**]**


### setPrivateKey

```java
public void setPrivateKey(String privateKey, boolean isPath);
```

**SRS_X509CERTIFICATEPAIR_34_007: [**If 'isPath' is false, this method shall save the provided string as the public key certificate.**]**
**SRS_X509CERTIFICATEPAIR_34_008: [**If 'isPath' is true, this method shall find the file in the path specified by the provided string and save that file's contents as the public key certificate.**]**
**SRS_X509CERTIFICATEPAIR_34_009: [**If the provided privateKey is null or empty, an IllegalArgumentException shall be thrown.**]**


### hasNeededKeys
```java
public boolean hasNeededKeys();
```

**SRS_X509CERTIFICATEPAIR_34_010: [**If this object's public key certificate or private key are null or empty, this method shall return false.**]**


### getPathToPublicKeyCertificate
```java
public String getPathToPublicKeyCertificate();
```

**SRS_X509CERTIFICATEPAIR_34_011: [**This function shall return the saved path to the public key certificate or null if there is not one saved.**]**


### getPathToPrivateKey
```java
public String getPathToPrivateKey();
```

**SRS_X509CERTIFICATEPAIR_34_012: [**This function shall return the saved path to the private key or null if there is not one saved.**]**


