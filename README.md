# Integration between Gearpump and TrustedAnalytics.

The goal of this utility is to enable for easy creation of Hadoop config object to initialize GearPump's HBase Sink and/or source. 

## Work in progress...
TODO:

* use latest version of tap's hadoop-utils
* extend test coverage


## Example TAP services configuration
TAP services configuration's schema is provided [here](src/main/resources/tap-config-schema.json).
 

Example configuration file, stripped to present only key elements, looks like this:

```
{
  "VCAP_SERVICES": {
    "hbase": [
      {
        "credentials": {
          "HADOOP_CONFIG_KEY": { ... },
          "hbase.namespace": "2bd6c4db32236dd4a33d19f8ef76257b4a69ff1b",
          "kerberos": {
            "kdc": "cdh-manager.server.com",
            "krealm": "CLOUDERA"
          }
        },
        "label": "hbase",
        "name": "hbase1",
        "plan": "shared",
        "tags": []
      },
      {
        "credentials": {
          "HADOOP_CONFIG_KEY": { ... },
          "hbase.namespace": "6f1bb0fdab0502079c4c4ca6bc770574fe546fc1",
          "kerberos": {
            "kdc": "cdh-manager.server.com",
            "krealm": "CLOUDERA"
          }
        },
        "label": "hbase",
        "name": "hbase2",
        "plan": "shared",
        "tags": []
      }
    ],
    "hdfs": [
      {
        "credentials": {
          "HADOOP_CONFIG_KEY": { ... },
          "fs.defaultFS": "hdfs://nameservice",
          "kerberos": {
            "kdc": "cdh-manager.server.com",
            "krealm": "CLOUDERA"
          },
          "uri": "hdfs://nameservice/somepath/b2be9863-f73c-4fa8-8c26-89ab41352c37/"
        },
        "label": "hdfs",
        "name": "hdfs-inst",
        "plan": "shared",
        "tags": []
      }
    ]
  }
}
```
