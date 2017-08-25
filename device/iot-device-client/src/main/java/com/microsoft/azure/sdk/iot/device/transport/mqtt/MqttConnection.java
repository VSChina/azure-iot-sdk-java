/*
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package com.microsoft.azure.sdk.iot.device.transport.mqtt;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MqttConnection
{
    private MqttAsyncClient mqttAsyncClient = null;
    private MqttConnectOptions connectionOptions = null;
    private ConcurrentLinkedQueue<Pair<String, byte[]>> allReceivedMessages;
    private Object mqttLock;
    private MqttCallback mqttCallback;

    //mqtt connection options
    private static final int KEEP_ALIVE_INTERVAL = 20;
    private static final int MQTT_VERSION = 4;
    private static final boolean SET_CLEAN_SESSION = false;
    static final int QOS = 1;
    static final int MAX_WAIT_TIME = 1000;

    // paho mqtt only supports 10 messages in flight at the same time
    static final int MAX_IN_FLIGHT_COUNT = 10;

    MqttConnection(String serverURI, String clientId, String userName, String password, SSLContext iotHubSSLContext) throws IOException
    {
        if (serverURI == null || clientId == null || userName == null || password == null || iotHubSSLContext == null)
        {
            throw new InvalidParameterException();
        }

        else if (serverURI.length() == 0 || clientId.length() == 0 || userName.length() == 0 || password.length() == 0)
        {
            throw new InvalidParameterException();
        }

        try
        {
            mqttAsyncClient = new MqttAsyncClient(serverURI, clientId, new MemoryPersistence());
            connectionOptions = new MqttConnectOptions();
            this.updateConnectionOptions(userName, password, iotHubSSLContext);
        }
        catch (MqttException e)
        {
            mqttAsyncClient = null;
            connectionOptions = null;
            throw new IOException("Error initializing MQTT connection:" + e.getMessage());
        }

        this.allReceivedMessages = new ConcurrentLinkedQueue<>();
        this.mqttLock = new Object();
    }

    /**
     * Generates the connection options for the mqtt broker connection.
     *
     * @param userName the user name for the mqtt broker connection.
     * @param userPassword the user password for the mqtt broker connection.
     */
    private void updateConnectionOptions(String userName, String userPassword, SSLContext iotHubSSLContext)
    {
        this.connectionOptions.setKeepAliveInterval(KEEP_ALIVE_INTERVAL);
        this.connectionOptions.setCleanSession(SET_CLEAN_SESSION);
        this.connectionOptions.setMqttVersion(MQTT_VERSION);
        this.connectionOptions.setUserName(userName);
        this.connectionOptions.setPassword(userPassword.toCharArray());
        this.connectionOptions.setSocketFactory(iotHubSSLContext.getSocketFactory());
    }

    void setMqttCallback(MqttCallback mqttCallback) throws IllegalArgumentException
    {
        if (mqttCallback == null)
        {
            throw new IllegalArgumentException("callback cannot be null");
        }
        this.mqttCallback = mqttCallback;
        this.getMqttAsyncClient().setCallback(mqttCallback);
    }

    MqttAsyncClient getMqttAsyncClient()
    {
        return mqttAsyncClient;
    }

    ConcurrentLinkedQueue<Pair<String, byte[]>> getAllReceivedMessages()
    {
        return allReceivedMessages;
    }

    Object getMqttLock()
    {
        return mqttLock;
    }

    MqttConnectOptions getConnectionOptions()
    {
        return connectionOptions;
    }

    void setMqttAsyncClient(MqttAsyncClient mqttAsyncClient)
    {
        this.mqttAsyncClient = mqttAsyncClient;
    }
}
