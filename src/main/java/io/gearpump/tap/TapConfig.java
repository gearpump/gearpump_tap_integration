package io.gearpump.tap;

import org.apache.commons.configuration.Configuration;

/**
 *
 * TAP Configuration JSON File format definition:
 *
 *{
 * "tap": {
 *   "hbase1": {
 *     uid: "globally unique service instance Id on the Cloud Foundary"
 *     type: "Service Type"
 *     config: "Configuration in BASE64 encoding"
 *     crendential: "Credential information in Base64 encoding"
 *   }
 *   "hbase2": {
 *     uid: "globally unique service instance Id on the Cloud Foundary"
 *     type: "Service Type"
 *     config: "Configuration in BASE64 encoding"
 *     crendential: "Credential information in Base64 encoding"
 *   }
 *   "hdfs1": {
 *     uid: "globally unique service instance Id on the Cloud Foundary"
 *     type: "Service Type"
 *     config: "Configuration in BASE64 encoding"
 *     crendential: "Credential information in Base64 encoding"
 *   }
 * }
 *}
 */
public interface TapConfig {
  /**
   * get a hbase configuration by hbase service instance id
   * @param serviceId Hbase service instance Id
   * @return
   */
  Configuration getHBase(String serviceId);

  /**
   * get a HDFS Configuration by HDFS instance Id
   * @param serviceId
   * @return
   */
  Configuration getHDFSConfig(String serviceId);
}