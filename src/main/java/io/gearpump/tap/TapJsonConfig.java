package io.gearpump.tap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class TapJsonConfig<T extends Map> implements TapConfig {

    private static final String TAP_CONFIG_ROOT = "VCAP_SERVICES";
    private static final String TAP_CONFIG_HBASE_TYPE = "hbase";
    private static final String TAP_CONFIG_INSTANCE_ID_KEY = "name";
    private static final String TAP_CONFIG_NAMESPACE_KEY = "hbase.namespace";
    private static final String HADOOP_CONFIG_KEY_VALUE = "HADOOP_CONFIG_KEY";
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

    public String getHBaseNamespace(String instanceName) {
        ArrayList<T> hbases = getServiceNodes(TAP_CONFIG_HBASE_TYPE);
        T hbaseService = serviceNodeForInstance(hbases, instanceName);

        return (String) ((Map) hbaseService.get(CREDENTIALS_KEY_VALUE)).get(TAP_CONFIG_NAMESPACE_KEY);
    }

    private static <T extends Map> Configuration createConfiguration(T hbaseService) {
        Configuration result = null;

        if (hbaseService != null) {
           result = new Configuration();
            Map credentials = (Map) hbaseService.get(CREDENTIALS_KEY_VALUE);
            Map<String, String> values = (Map<String, String>) credentials.get(TapJsonConfig.HADOOP_CONFIG_KEY_VALUE);
            values.forEach(result::set);
        }

        return result;
    }

    @Override
    public Configuration getHBase(String instanceName) {
        ArrayList<T> hbases = getServiceNodes(TAP_CONFIG_HBASE_TYPE);
        T serviceNode = serviceNodeForInstance(hbases, instanceName);

        return createConfiguration(serviceNode);
    }

    @Override
    public Configuration getHDFSConfig(String instanceName) {
        ArrayList<T> hbases = getServiceNodes(TAP_CONFIG_HBASE_TYPE);
        T serviceNode = serviceNodeForInstance(hbases, instanceName);

        return createConfiguration(serviceNode);
    }
}
