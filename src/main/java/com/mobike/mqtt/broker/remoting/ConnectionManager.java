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
package com.mobike.mqtt.broker.remoting;

import com.mobike.mqtt.broker.log.MqttLoggerFactory;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;

/**
 * @author mudun
 * @version $Id: ConnectionManager.java, v 0.1 2019/4/9 下午2:12 mudun Exp $
 */
public class ConnectionManager {
    private static final Logger logger = MqttLoggerFactory.getLogger("CommonDefault");

    private final ConcurrentHashMap<String/*groupId*/, ConnectionGroup/*group*/> groups;

    public ConnectionManager() {
        this.groups = new ConcurrentHashMap<>();
    }

    public void put(String groupId, Connection connection) {
        ConnectionGroup group = groups.get(groupId);
        if (group == null) {
            group = new ConnectionGroup(connection.getGroupId());
            groups.put(groupId, group);
        }

        group.put(connection.getClientId(), connection);
    }

    public boolean exists(String clientId, String groupId) {
        if (groups.containsKey(groupId) && groups.get(groupId).exists(clientId)) {
            return true;
        }
        return false;
    }

    public void remove(Connection connection) {
        ConnectionGroup group = groups.get(connection.getGroupId());
        if (group != null) {
            group.remove(connection.getClientId());
        }
    }

    /**
     * @param groupKey
     * @param uniqueKey
     * @return
     */
    public Connection getConnection(String groupKey, String uniqueKey) {
        ConnectionGroup group = groups.get(groupKey);
        return group != null ? group.get(uniqueKey) : null;
    }


    public ConcurrentHashMap<String, ConnectionGroup> getGroups() {
        return groups;
    }

    public int count() {
        return groups.size();
    }

    public void clear() {
        if (groups == null || groups.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<String, ConnectionGroup>> iterator = this.groups.entrySet().iterator();
        while (iterator.hasNext()) {
            ConnectionGroup connectionGroup = iterator.next().getValue();
            connectionGroup.closeAndClear();
            iterator.remove();
        }
    }
}
