/*
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package tests.unit.com.microsoft.azure.sdk.iot.device.transport.mqtt;

import com.microsoft.azure.sdk.iot.device.IotHubSSLContext;
import com.microsoft.azure.sdk.iot.device.transport.mqtt.Mqtt;
import com.microsoft.azure.sdk.iot.device.transport.mqtt.MqttConnection;
import com.microsoft.azure.sdk.iot.device.transport.mqtt.MqttMessaging;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.Assert.assertNotNull;

public class MqttConnectionTest
{
    private static final String serverUri = "test.host.name";
    private static final String clientId = "test.iothub";
    private static final String userName = "test-deviceId";
    private static final String password = "test-devicekey?&test";

    @Mocked
    SSLContext mockIotHubSSLContext;

    @Mocked
    private MqttAsyncClient mockMqttAsyncClient;

    @Mocked
    private MqttConnectOptions mockMqttConnectionOptions;

    @Mocked
    private MemoryPersistence mockMemoryPersistence;

    @Mocked
    private IMqttToken mockMqttToken;

    private void baseConstructorExpectations() throws MqttException
    {

        new NonStrictExpectations()
        {
            {
                new MemoryPersistence();
                result = mockMemoryPersistence;
                new MqttAsyncClient(serverUri, clientId, mockMemoryPersistence);
                result = mockMqttAsyncClient;
                new MqttConnectOptions();
                result = mockMqttConnectionOptions;
            }
        };
    }

    private void baseConstructorVerifications() throws MqttException
    {
        new Verifications()
        {
            {
                new MqttAsyncClient(serverUri, clientId, mockMemoryPersistence);
                times = 1;
                mockMqttAsyncClient.setCallback((Mqtt) any);
                times = 1;
                mockMqttConnectionOptions.setKeepAliveInterval(anyInt);
                times = 1;
                mockMqttConnectionOptions.setCleanSession(anyBoolean);
                times = 1;
                mockMqttConnectionOptions.setMqttVersion(anyInt);
                times = 1;
                mockMqttConnectionOptions.setUserName(userName);
                times = 1;
                mockMqttConnectionOptions.setPassword(password.toCharArray());
                times = 1;
                mockMqttConnectionOptions.setSocketFactory(mockIotHubSSLContext.getSocketFactory());
                times = 1;
                new ConcurrentSkipListMap<>();
                times = 1;
                new Object();
                times = 1;
            }
        };
    }

    @Test
    public void constructorSucceeds() throws Exception
    {
        //arrange
        baseConstructorExpectations();
        //act
        final MqttConnection mqttConnection = Deencapsulation.newInstance(MqttConnection.class, serverUri, clientId, userName, password, mockIotHubSSLContext);

        //assert
        MqttConnection actualInfo = Deencapsulation.getField(mqttConnection, "mqttConnection");
        assertNotNull(actualInfo);
        MqttAsyncClient actualAsyncClient = Deencapsulation.getField(actualInfo, "mqttAsyncClient");
        assertNotNull(actualAsyncClient);
        MqttConnectOptions actualConnectionOptions = Deencapsulation.getField(actualInfo, "connectionOptions");
        assertNotNull(actualConnectionOptions);
        Queue<Pair<String, byte[]>> actualQueue = Deencapsulation.getField(mqttConnection, "allReceivedMessages");
        assertNotNull(actualQueue);
        Object actualLock = Deencapsulation.getField(mqttConnection, "MQTT_LOCK");
        assertNotNull(actualLock);

        new Verifications()
        {
            {
                Deencapsulation.invoke(mqttConnection, "getAllReceivedMessages");
                times = 1;
                Deencapsulation.invoke(mqttConnection, "getMqttLock");
                times = 1;
            }
        };
    }

}
