/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.gearpump.tap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TapJsonConfig<T extends Map> implements TapConfig {

    private static final String TAP_CONFIG_ROOT = "VCAP_SERVICES";
    private static final String TAP_CONFIG_HBASE_TYPE = "hbase";
    private static final String TAP_CONFIG_HDFS_TYPE = "hdfs";
    private static final String TAP_CONFIG_KAFKA_TYPE = "kafka";
    private static final String TAP_CONFIG_ZOOKEEPER_TYPE = "zookeeper";
    private static final String TAP_CONFIG_INSTANCE_ID_KEY = "name";
    private static final String TAP_CONFIG_NAMESPACE_KEY = "hbase.namespace";
    private static final String HADOOP_CONFIG_KEY_VALUE = "HADOOP_CONFIG_KEY";
    private static final String KAFKA_URI = "uri";
    private static final String KAFKA_BROKERS = "brokers";
    private static final String ZOOKEEPER_CLUSTER = "zk.cluster";
    private static final String ZOOKEEPER_SERVERS = "zookeepers";
    private static final String CREDENTIALS_KEY_VALUE = "credentials";

    private final T tapConfig;

    JsonFactory factory = new JsonFactory();
    ObjectMapper mapper = new ObjectMapper(factory);

    /**
     * Construct the object from json string (tap config representation)
     *
     * @param jsonString
     */
    public TapJsonConfig(String jsonString) throws IOException {
        TypeReference<T> typeRef = new TypeReference<T>() {};

        tapConfig = mapper.readValue(jsonString, typeRef);
    }

    private ArrayList<T> getServiceNodes(String serviceType) {
        return (ArrayList<T>) ((Map)tapConfig.get(TAP_CONFIG_ROOT)).get(serviceType);
    }

    private T serviceNodeForInstance(ArrayList<T> serviceNodes, String instanceName) {
        T result = null;

        if (serviceNodes != null) {
            result = serviceNodes.stream()
                        .filter(i -> i.get(TAP_CONFIG_INSTANCE_ID_KEY).equals(instanceName)).findFirst()
                        .get();
        }
        return result;
    }

    private static <T extends Map> Configuration createConfiguration(T service, String type) {
        Configuration result = null;

        if (service != null) {
            result = new Configuration();
            Map credentials;
            Map<String, String> values;
            switch(type) {
                case TAP_CONFIG_HBASE_TYPE:
                    credentials = (Map) service.get(CREDENTIALS_KEY_VALUE);
                    values = (Map<String, String>) credentials.get(TapJsonConfig.HADOOP_CONFIG_KEY_VALUE);
                    values.forEach(result::set);
                    break;
                case TAP_CONFIG_HDFS_TYPE:
                    credentials = (Map) service.get(CREDENTIALS_KEY_VALUE);
                    values = (Map<String, String>) credentials.get(TapJsonConfig.HADOOP_CONFIG_KEY_VALUE);
                    values.forEach(result::set);
                    break;
                case TAP_CONFIG_KAFKA_TYPE:
                    credentials = (Map) service.get(CREDENTIALS_KEY_VALUE);
                    values = new HashMap<String, String>();
                    values.put(TapJsonConfig.KAFKA_BROKERS, credentials.get(TapJsonConfig.KAFKA_URI).toString());
                    values.forEach(result::set);
                    break;
                case TAP_CONFIG_ZOOKEEPER_TYPE:
                    credentials = (Map) service.get(CREDENTIALS_KEY_VALUE);
                    values = new HashMap<String, String>();
                    values.put(TapJsonConfig.ZOOKEEPER_SERVERS, credentials.get(TapJsonConfig.ZOOKEEPER_CLUSTER).toString());
                    values.forEach(result::set);
                    break;
            }
        }

        return result;
    }

    @Override
    public String getHBaseNamespace(String instanceName) {
        ArrayList<T> hbases = getServiceNodes(TAP_CONFIG_HBASE_TYPE);
        T hbaseService = serviceNodeForInstance(hbases, instanceName);

        return (String) ((Map) hbaseService.get(CREDENTIALS_KEY_VALUE)).get(TAP_CONFIG_NAMESPACE_KEY);
    }

    @Override
    public Configuration getHBase(String instanceName) {
        ArrayList<T> hbase = getServiceNodes(TAP_CONFIG_HBASE_TYPE);
        T serviceNode = serviceNodeForInstance(hbase, instanceName);

        return createConfiguration(serviceNode, TAP_CONFIG_HBASE_TYPE);
    }

    @Override
    public Configuration getHDFSConfig(String instanceName) {
        ArrayList<T> hdfs = getServiceNodes(TAP_CONFIG_HDFS_TYPE);
        T serviceNode = serviceNodeForInstance(hdfs, instanceName);

        return createConfiguration(serviceNode, TAP_CONFIG_HDFS_TYPE);
    }

    @Override
    public Configuration getKafkaConfig(String instanceName) {
        ArrayList<T> kafka = getServiceNodes(TAP_CONFIG_KAFKA_TYPE);
        T serviceNode = serviceNodeForInstance(kafka, instanceName);

        return createConfiguration(serviceNode, TAP_CONFIG_KAFKA_TYPE);
    }

    @Override
    public Configuration getZookeeperConfig(String instanceName) {
        ArrayList<T> zookeeper = getServiceNodes(TAP_CONFIG_ZOOKEEPER_TYPE);
        T serviceNode = serviceNodeForInstance(zookeeper, instanceName);

        return createConfiguration(serviceNode, TAP_CONFIG_ZOOKEEPER_TYPE);
    }
}
