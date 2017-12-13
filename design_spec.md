# Design specification for E2E Diagnostic in Java SDK

## Introduction

### 1.What we do?
Customers would like to track end-to-end flow of a message. They want to troubleshoot where the error happens of a message, or track message across multiple Azure services.

### 2.Concern
Considering it needs cost for sending diagnostic info, there’s one option provided for user to configure the diagnostic for each device: the sampling percentage which indicates percentage of D2C messages that will carry diagnostic data.

## Definition

### Data structure

#### 1.Diagnostic Info
The following table describes the names and format of diagnostic related properties in detail.

| System Property Name | MQTT Property Name | Property Purpose                          | Property Value Format                                    | Property Value Sample                        |
|----------------------|--------------------|-------------------------------------------|----------------------------------------------------------|----------------------------------------------|
| diag-id              | $.diagid           | Track the message in consequent flow      | 8 characters, [a-z0-9]8                                  | "8cd869z4"                                   |
| Correlation-Context  | $.cctx             | Context contains multiple key-value pairs | Comma separated list of key-value, it is RFC2396 encoded | “creationtimeutc=2017-02-22T01:01:01Z,hop=2” |

| ﻿Property Name in Correlation-Context                                               | Property Purpose                                        | Property Value Format | Property Value Sample      |  
|------------------------------------------------------------------------------------|---------------------------------------------------------|-----------------------|----------------------------|
| creationtimeutc                                                                    | Timestamp when creating message                         | UTC time string       | "2017-02-22T03:27:28.633Z" |
| hop * it is necessary only in gateway scenario, not covered in current implementation.                                                                               | The number of nodes/services that the message pass thru | Integer string        | “2”                        |

#### 2.Dianostic setting
We provide 2 ways to set the diagnostic settings(sampling percentage). The first way is to set locally in code. The second is fetch from device twin. Here gives the data structure of device twin:

    {
      diag_enable: 'true|false',
      diag_sample_rate: '0-100'
    }

### Interface and class
    class DeviceClient
    {
        // To provide function of diagnostics
        private DeviceClientDiagnostic deviceDiagnostic;

        // A flag to make sure enableDiagnostics only be called once.
        private boolean diagnosticAlreadyEnabled; 

        // Calling enableDiagnostics with a parameter will set diagnostic setting from local
        public void enableDiagnostics(int percentage);

        // Calling enableDiagnostics without a parameter will set diagnostic setting from cloud(device twin)
        public void enableDiagnostics();
    }

    class DeviceClientDiagnostic
    {
        private int diagSamplingPercentage;
        private int currentMessageNumber;

        public DeviceClientDiagnostic();
        public int getDiagSamplingPercentage();
        public void setDiagSamplingPercentage(int diagSamplingPercentage);

        // To decide if this message would attach diagnostic information
        public void addDiagnosticInfoIfNecessary(Message message);
    }

    class Message
    {
        // According to our general design, here diagnostic properties should be add to system properties, but in Java SDK, there isn't message properties member in Message class. So define 2 more private members here.
        private String diagnosticId;
        private String diagnosticCorrelationContext;
    }

    class AmqpsDeviceTelemetry
    {
        // Add diagnostic info(if exist) to AMQP message annotations
        protected MessageImpl iotHubMessageToProtonMessage(Message message);
    }

    class MqttMessaging
    {
        // Add diagnostic info(if exist) to MQTT properties
        public void send(Message message);
    }




