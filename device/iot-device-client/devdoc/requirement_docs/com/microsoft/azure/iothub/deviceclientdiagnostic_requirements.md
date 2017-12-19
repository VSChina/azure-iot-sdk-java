# DeviceClientDiagnostic Requirements

## Overview

Handling device client diagnostic information.

## References

## Exposed API

```java
public final class DeviceClientDiagnostic
{
    public DeviceClientDiagnostic();
    public DeviceClientDiagnostic(DeviceTwin diagnosticTwin);
    public int getDiagSamplingPercentage();
    public void setDiagSamplingPercentage(int diagSamplingPercentage);
    public void addDiagnosticInfoIfNecessary(Message message);
    public DeviceTwin getDiagnosticTwin();
}

private class DiagnosticTwinPropertyCallback extends Device
{
    public void PropertyCall(String propertyKey, Object propertyValue, Object context);
}
```

### DeviceClientDiagnostic

```java
public DeviceClientDiagnostic();
```

** SRS_DEVICECLIENTDIAGNOSTIC_01_001: [**This constructor shall set sampling percentage to 0.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_01_002: [**This constructor shall set message number to 0.**]**


```java
public DeviceClientDiagnostic(DeviceTwin diagnosticTwin);
```

** SRS_DEVICECLIENTDIAGNOSTIC_01_004: [**This constructor shall initialize a twinPropertyCallback.**]**


### setDiagSamplingPercentage

```java
public setDiagSamplingPercentage(int diagSamplingPercentage);
```

** SRS_DEVICECLIENTDIAGNOSTIC_01_005: [**When percentage is less than 0 or larger than 100, throw IllegalArgumentException.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_01_006: [**This function shall reset message number to 0.**]**


### addDiagnosticInfoIfNecessary

```java
public addDiagnosticInfoIfNecessary(Message message);
```

** SRS_DEVICECLIENTDIAGNOSTIC_01_007: [**This function shall add diagnostic id to message.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_01_008: [**This function shall add encoded diagnostic correlation context to message.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_01_009: [**Function `generateEightRandomCharacters` shall generate 8 random chars, each is from 0-9a-z.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_01_010: [**Function `getCurrentTimeUtc` shall return the current timestamp in 0000000000.000 pattern.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_01_011: [**Function `shouldAddDiagnosticInfo` shall return false if sampling percentage is set to 0.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_01_012: [**Function `shouldAddDiagnosticInfo` shall return value due to the sampling percentage setting.**]**


### DiagnosticTwinPropertyCallback.setDiagSamplingPercentage

```java
public void PropertyCall(String propertyKey, Object propertyValue, Object context);
```

** SRS_DEVICECLIENTDIAGNOSTIC_02_001: [**This function shall set diagnostic settings.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_02_002: [**This function shall generate error message if value is invalid.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_02_003: [**This function shall report applied settings and error.**]**

** SRS_DEVICECLIENTDIAGNOSTIC_02_004: [**This function shall set diagnostic sample rate to 0 if the twin value is null.**]**
