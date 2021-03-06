/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.proxy.backend.netty.client;

import io.shardingsphere.proxy.config.ProxyContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * backend netty client manager.
 *
 * @author chenqingyang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BackendNettyClientManager {
    
    private static final BackendNettyClientManager INSTANCE = new BackendNettyClientManager();
    
    private static final ProxyContext PROXY_CONTEXT = ProxyContext.getInstance();
    
    private final Map<String, BackendNettyClient> clientMap = new HashMap<>();
    
    /**
     * Get instance of backend netty client manager.
     *
     * @return instance of backend netty client manager.
     */
    public static BackendNettyClientManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * get backend netty client of schema.
     *
     * @param schema schema
     * @return backend netty client of schema
     */
    public BackendNettyClient getBackendNettyClient(final String schema) {
        return clientMap.get(schema);
    }
    
    /**
     * Start all backend connection client for netty.
     *
     * @throws InterruptedException interrupted exception
     */
    public void start() throws InterruptedException {
        for (String schema : PROXY_CONTEXT.getSchemaNames()) {
            BackendNettyClient backendNettyClient = new BackendNettyClient(PROXY_CONTEXT.getRuleRegistry(schema));
            clientMap.put(schema, backendNettyClient);
            backendNettyClient.start();
        }
    }
    
    /**
     * Stop all backend connection client for netty.
     */
    public void stop() {
        for (BackendNettyClient backendNettyClient : clientMap.values()) {
            backendNettyClient.stop();
        }
    }
}
