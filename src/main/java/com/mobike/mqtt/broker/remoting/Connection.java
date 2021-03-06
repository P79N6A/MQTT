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

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @author mudun
 * @version $Id: Connection.java, v 0.1 2019/4/9 下午2:10 mudun Exp $
 */
public class Connection {
    /**
     * the channel of connection used
     */
    private Channel channel;

    /**
     * the unique id of connection
     */
    private String clientId;

    /**
     * the group connection belongs to
     */
    private String groupId;


    public static final AttributeKey<Connection> CONNECTION = AttributeKey.valueOf("connection");

    /**
     * construct
     *
     * @param channel
     * @param clientId
     */
    public Connection(Channel channel, String groupId, String clientId) {
        this.channel = channel;
        this.clientId = clientId;
        this.groupId = groupId;
        this.channel.attr(CONNECTION).set(this);
    }

    public void close() {
        channel.close();
    }

    public String getClientId() {
        return clientId;
    }

    public String getGroupId() {
        return groupId;
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean isActive() {
        return this.channel != null && this.channel.isActive();
    }
}
