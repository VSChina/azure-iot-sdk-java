/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package tests.integration.com.microsoft.azure.sdk.iot.helpers;

import com.microsoft.azure.sdk.iot.service.IotHubConnectionStringBuilder;

import java.io.IOException;

public class Tools
{
    public static String getX509ConnectionStringForDeviceFromSharedAccessKey(String iotHubConnectionString, String deviceId) throws IOException
    {
        String hostName = IotHubConnectionStringBuilder.createConnectionString(iotHubConnectionString).getHostName();
        return "HostName=" + hostName + ";DeviceId=" + deviceId + ";x509=true";
    }
}