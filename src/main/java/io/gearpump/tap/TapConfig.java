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



import org.apache.hadoop.conf.Configuration;

/*
 *
 * TAP Configuration JSON File format key elements:
 *
 *
 * {
 *   "VCAP_SERVICES": {
 *      "hbase": [
 *        {
 *          "credentials": {
 *            "HADOOP_CONFIG_KEY": "hadoop configuration keys"
 *              "hbase.namespace": "a namespace provisioned for the user by TAP",
 *              "kerberos": {
 *                "kdc": "address of Key Distribution Center",
 *                "krealm": "kerberos realm"
 *              }
 *          },
 *          "name": "service instance name"
 *        }
 *      ],
 *      "hdfs": [
 *        {
 *          "credentials": {
 *            "HADOOP_CONFIG_KEY": "hadoop configuration keys"
 *            "fs.defaultFS": "default filesystem for hdfs"
 *              "kerberos": {
 *                "kdc": "address of Key Distribution Center",
 *                "krealm": "kerberos realm"
 *              }
 *            "uri": "uri of hdfs folder provisioned for the user by TAP"
 *          },
 *          "name": "service instance name"
 *        }
 *      ]
 *   }
 * }
 *
 * "hbase", "hdfs" keys represent srvice types. There can by many instances within given type.
 * The instances of the same type are distinguished by service name ("name" key)
 */
public interface TapConfig {
    /**
     * get a HBase configuration (Hadoop configuration object) by hbase service instance name
     *
     * @param serviceId HBase service instance Id
     * @return
     */
    Configuration getHBase(String serviceId);

    /**
     * get a HDFS Configuration by HDFS instance Id
     *
     * @param serviceId
     * @return
     */
    Configuration getHDFSConfig(String serviceId);

    /**
     * Get a namespace provisioned for given HBase service by TAP.
     *
     * @param serviceId HBase service instance Id
     * @return
     */
    String getHBaseNamespace(String serviceId);


}
