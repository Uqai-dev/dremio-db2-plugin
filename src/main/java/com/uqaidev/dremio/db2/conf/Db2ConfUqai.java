package com.uqaidev.dremio.db2.conf;

import com.dremio.exec.catalog.conf.DisplayMetadata;
import com.dremio.exec.catalog.conf.NotMetadataImpacting;
import com.dremio.exec.catalog.conf.Secret;
import com.dremio.exec.catalog.conf.SourceType;
import com.dremio.exec.store.jdbc.CloseableDataSource;
import com.dremio.exec.store.jdbc.DataSources;
import com.dremio.exec.store.jdbc.JdbcPluginConfig;
import com.dremio.exec.store.jdbc.conf.AbstractArpConf;
import com.uqaidev.dremio.db2.dialect.Db2DialectUqai;
import com.dremio.exec.store.jdbc.dialect.arp.ArpDialect;
import com.dremio.options.OptionManager;
import com.dremio.security.CredentialsService;
import com.google.common.annotations.VisibleForTesting;
import io.protostuff.Tag;
import org.hibernate.validator.constraints.NotBlank;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author dacopan on 03/11/19
 */
@SourceType(value = "DB22", label = "IBM Db2 Uqai", uiConfig = "db22-layout.json", externalQuerySupported = true)
public class Db2ConfUqai extends AbstractArpConf<Db2ConfUqai> {

    private static final String ARP_FILENAME = "arp/implementation/db22-arp.yaml";
    private static final ArpDialect ARP_DIALECT =
            AbstractArpConf.loadArpFile(ARP_FILENAME, (Db2DialectUqai::new));

    private static final String DRIVER = "com.ibm.db2.jcc.DB2Driver";

    @NotBlank
    @Tag(1)
    @DisplayMetadata(label = "Database")
    public String db;

    @Tag(2)
    @DisplayMetadata(label = "Record fetch size")
    @NotMetadataImpacting
    public int fetchSize = 200;

    @NotBlank
    @Tag(3)
    @DisplayMetadata(label = "Host")
    public String host;

    @Tag(4)
    @DisplayMetadata(label = "Port")
    public Integer port;

    @NotBlank
    @Tag(5)
    @DisplayMetadata(label = "Username")
    public String username;

    @NotBlank
    @Tag(6)
    @DisplayMetadata(label = "Password")
    @Secret
    public String password;

    @Tag(7)
    @DisplayMetadata(label = "Maximum idle connections")
    @NotMetadataImpacting
    public int maxIdleConns = 8;

    @Tag(8)
    @DisplayMetadata(label = "Connection idle time (s)")
    @NotMetadataImpacting
    public int idleTimeSec = 60;


    @VisibleForTesting
    public String toJdbcConnectionString() {
        final String host = checkNotNull(this.host, "Missing host.");
        final String db = checkNotNull(this.db, "Missing database.");

        return String.format("jdbc:db2://%s:%d/%s", host, port, db);
    }

    @Override
    public JdbcPluginConfig buildPluginConfig(JdbcPluginConfig.Builder builder, CredentialsService credentialsService, OptionManager optionManager) {
        return builder.withDialect(getDialect())
                .withDialect(getDialect())
                .withFetchSize(fetchSize)
                .withDatasourceFactory(this::newDataSource)
                .clearHiddenSchemas()
                .addHiddenSchema("SYSTEM")
                .build();
    }

    private CloseableDataSource newDataSource() {
        final String username = checkNotNull(this.username, "Missing username.");
        final String password = checkNotNull(this.password, "Missing password.");

        return DataSources.newGenericConnectionPoolDataSource(DRIVER,
                toJdbcConnectionString(), username, password, null, DataSources.CommitMode.DRIVER_SPECIFIED_COMMIT_MODE,
                maxIdleConns, idleTimeSec);
    }

    @Override
    public ArpDialect getDialect() {
        return ARP_DIALECT;
    }

    @VisibleForTesting
    public static ArpDialect getDialectSingleton() {
        return ARP_DIALECT;
    }


}
