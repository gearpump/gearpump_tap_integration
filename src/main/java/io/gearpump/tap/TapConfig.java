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
     * get a HBase configuration by hbase service instance id
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
}
