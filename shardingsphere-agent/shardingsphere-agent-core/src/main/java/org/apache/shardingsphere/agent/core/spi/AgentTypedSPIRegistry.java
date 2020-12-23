/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.agent.core.spi;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.agent.core.exception.AgentServiceProviderNotFoundException;

/**
 *  Agent typed SPI registry.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AgentTypedSPIRegistry {
    
    /**
     * Get registered service.
     * 
     * @param typedSPIClass typed SPI class
     * @param type type
     * @param <T> type
     * @return registered service
     */
    public static <T extends AgentTypedSPI> T getRegisteredService(final Class<T> typedSPIClass, final String type) {
        Optional<T> serviceInstance = AgentServiceLoader.getServiceLoader(typedSPIClass).newServiceInstances().stream().filter(each -> each.getType().equalsIgnoreCase(type)).findFirst();
        if (serviceInstance.isPresent()) {
            return serviceInstance.get();
        }
        throw new AgentServiceProviderNotFoundException(typedSPIClass, type);
    }
    
    /**
     * Get all registered service.
     *
     * @param typedSPIClass typed SPI class
     * @param <T> type
     * @return registered service
     */
    public static <T extends AgentTypedSPI> Collection<T> getAllRegisteredService(final Class<T> typedSPIClass) {
        return AgentServiceLoader.getServiceLoader(typedSPIClass).newServiceInstances();
    }
    
    /**
     * Get registered services.
     *
     * @param types types
     * @param typedSPIClass typed SPI class
     * @param <V> type of typed SPI class
     * @return registered services
     */
    public static <V extends AgentTypedSPI> Map<String, V> getRegisteredServices(final Collection<String> types, final Class<V> typedSPIClass) {
        Collection<V> registeredServices = AgentServiceLoader.getServiceLoader(typedSPIClass).newServiceInstances();
        Map<String, V> result = new LinkedHashMap<>(registeredServices.size(), 1);
        for (V each : registeredServices) {
            types.stream().filter(type -> Objects.equals(each.getType(), type)).forEach(type -> result.put(type, each));
        }
        return result;
    }
}