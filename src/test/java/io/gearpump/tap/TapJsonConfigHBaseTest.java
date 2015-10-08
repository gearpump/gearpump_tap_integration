package io.gearpump.tap;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.trustedanalytics.hadoop.config.ConfigurationHelper;
import org.trustedanalytics.hadoop.config.ConfigurationHelperImpl;
import org.trustedanalytics.hadoop.config.ConfigurationLocator;
import org.trustedanalytics.hadoop.config.PropertyLocator;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * Test usage of hadoop-utils.
 */
public class TapJsonConfigHBaseTest {

    private static String CONFIG_2HBASES = "/2hbases.json";
    private static String CONFIG_SIMPLIFIED = "/hbase-filtered.json";
    private static String CONFIG_0HBASE = "/no-hbase.json";

    private String configSimplified;
    private String configNoHBase;
    private String config2HBases;

    @Before
    public void prepareTestConfigs() throws IOException {
        this.configSimplified = IOUtils.toString(getClass().getResourceAsStream(CONFIG_SIMPLIFIED));
        this.configNoHBase = IOUtils.toString(getClass().getResourceAsStream(CONFIG_0HBASE));
        this.config2HBases = IOUtils.toString(getClass().getResourceAsStream(CONFIG_2HBASES));
    }

    @Test
    public void testSimpleGet1Of2() throws Exception {
        TapJsonConfig tjc = new TapJsonConfig(configSimplified);

        String instanceName = "hbase1";
        Configuration c = tjc.getHBase(instanceName);
        String namespace = tjc.getHBaseNamespace(instanceName);

        Assert.assertThat(c, notNullValue());
        Assert.assertThat(namespace, equalTo("2bd6c4db32236dd4a33d19f8ef76257b4a69ff1b"));
    }

    @Test
    public void testSimpleGetSecondOf2() throws Exception {
        TapJsonConfig tjc = new TapJsonConfig(configSimplified);

        String instanceName = "hbase2";
        Configuration c = tjc.getHBase(instanceName);
        String namespace = tjc.getHBaseNamespace(instanceName);

        Assert.assertThat(c, notNullValue());
        Assert.assertThat(namespace, equalTo("6f1bb0fdab0502079c4c4ca6bc770574fe546fc1"));
    }

    @Test
    public void testNoHBase() throws Exception {
        TapJsonConfig tjc = new TapJsonConfig(configNoHBase);

        String instanceName = "hbase1";
        Configuration c = tjc.getHBase(instanceName);

        Assert.assertThat(c, nullValue());
    }
}
