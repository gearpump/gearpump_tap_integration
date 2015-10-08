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

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
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
