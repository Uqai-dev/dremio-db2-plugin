package com.uqaidev.dremio.db2.dialect;

import com.dremio.exec.store.jdbc.dialect.arp.ArpDialect;
import com.dremio.exec.store.jdbc.dialect.arp.ArpYaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * A <code>SqlDialect</code> implementation for the Db2 database.
 * Reference https://github.com/dremioJonny/dremio-sybase-connector/blob/master/src/main/java/com/dremio/exec/store/jdbc/dialect/SybaseDialect.java
 *
 * @author dacopan on 29/7/20
 */
public class Db2DialectUqai extends ArpDialect {

    Logger logger = LoggerFactory.getLogger(Db2DialectUqai.class);


    public Db2DialectUqai(ArpYaml yaml) {
        super(yaml);
    }

    @Override
    public boolean hasImplicitTableAlias() {
        return false;
    }

    @Override
    public ContainerSupport supportsCatalogs() {
        return super.supportsCatalogs();
    }
}
