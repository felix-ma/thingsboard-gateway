/**
 * Copyright © 2017 The Thingsboard Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.gateway.service.gateway;

import org.thingsboard.gateway.service.MqttDeliveryFuture;
import org.thingsboard.gateway.service.conf.TbExtensionConfiguration;
import org.thingsboard.gateway.service.data.*;
import org.thingsboard.server.common.data.kv.KvEntry;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by ashvayka on 16.01.17.
 */
public interface GatewayService {

    /**
     * 初始化
     *
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 销毁
     *
     * @throws Exception
     */
    void destroy() throws Exception;

    /**
     * 获取配置的标签
     *
     * @return
     */
    String getTenantLabel();

    /**
     * Inform gateway service that device is connected
     * 通知网关服务设备已连接
     *
     * @param deviceName
     * @param deviceType
     */
    MqttDeliveryFuture onDeviceConnect(String deviceName, String deviceType);

    /**
     * Inform gateway service that device is disconnected
     * 通知网关服务设备已断开连接
     *
     * @param deviceName
     */
    Optional<MqttDeliveryFuture> onDeviceDisconnect(String deviceName);

    /**
     * Report device attributes change to Thingsboard
     * 往平台发送设备属性信息变更，不携带ts
     *
     * @param deviceName - the device name
     * @param attributes - the attribute values list
     */
    MqttDeliveryFuture onDeviceAttributesUpdate(String deviceName, List<KvEntry> attributes);

    /**
     * Report device telemetry to Thingsboard
     * 往平台发送设备遥测数据，携带ts
     *
     * @param deviceName - the device name
     * @param telemetry  - the telemetry values list
     */
    MqttDeliveryFuture onDeviceTelemetry(String deviceName, List<TsKvEntry> telemetry);

    /**
     * Report attributes request to Thingsboard
     * 往平台发送属性数据请求
     *
     * @param attributeRequest - attributes request
     * @param listener         - attributes response
     */
    void onDeviceAttributeRequest(AttributeRequest attributeRequest, Consumer<AttributeResponse> listener);

    /**
     * Report response from device to the server-side RPC call from Thingsboard
     * 往平台发送服务段rpc请求
     *
     * @param response - the device response to RPC call
     */
    void onDeviceRpcResponse(RpcCommandResponse response);

    /**
     * Subscribe to attribute updates from Thingsboard
     * 订阅平台数据更新
     *
     * @param subscription - the subscription
     * @return true if successful, false if already subscribed
     */
    boolean subscribe(AttributesUpdateSubscription subscription);

    /**
     * Subscribe to server-side rpc commands from Thingsboard
     * 订阅平台 rpc 服务器端调用
     *
     * @param subscription - the subscription
     * @return true if successful, false if already subscribed
     */
    boolean subscribe(RpcCommandSubscription subscription);

    /**
     * Unsubscribe to attribute updates from Thingsboard
     * 取消在平台订阅属性更新
     *
     * @param subscription - the subscription
     * @return true if successful, false if already unsubscribed
     */
    boolean unsubscribe(AttributesUpdateSubscription subscription);

    /**
     * Unsubscribe to server-side rpc commands from Thingsboard
     * 取消订阅平台 rpc 服务器端调用
     *
     * @param subscription - the subscription
     * @return true if successful, false if already unsubscribed
     */
    boolean unsubscribe(RpcCommandSubscription subscription);

    /**
     * Report generic error from one of gateway components
     * 网关组件，报告一般性错误
     *
     * @param e - the error
     */
    void onError(Exception e);

    /**
     * Report error related to device
     * 报告与设备相关的错误
     *
     * @param deviceName - the device name
     * @param e          - the error
     */
    void onError(String deviceName, Exception e);

    /**
     * Report applied configuration
     * 当应用配置更新时
     *
     * @param configuration - extension configuration
     */
    void onAppliedConfiguration(String configuration);

    /**
     * Report extension configuration error
     * 当配置更新失败
     *
     * @param e             - the error
     * @param configuration - extension configuration
     */
    void onConfigurationError(Exception e, TbExtensionConfiguration configuration);

    /**
     * Report extension configuration status
     * 配置目前状态
     *
     * @param id     - extension id
     * @param status - extension status
     */
    void onConfigurationStatus(String id, String status);
}
