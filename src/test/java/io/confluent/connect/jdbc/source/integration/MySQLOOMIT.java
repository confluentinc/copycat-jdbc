package io.confluent.connect.jdbc.source.integration;

import ch.vorburger.mariadb4j.junit.MariaDB4jRule;
import io.confluent.common.utils.IntegrationTest;
import io.confluent.connect.jdbc.source.JdbcSourceConnectorConfig;
import io.confluent.connect.jdbc.source.JdbcSourceTaskConfig;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Category(IntegrationTest.class)
public class MySQLOOMIT extends BaseOOMIntegrationTest {

  private static Logger log = LoggerFactory.getLogger(MySQLOOMIT.class);

  @Rule
  public MariaDB4jRule dbRule = new MariaDB4jRule(0);

  @Before
  public void before() {
    props = new HashMap<>();
    props.put(JdbcSourceConnectorConfig.CONNECTION_URL_CONFIG,
            dbRule.getDBConfiguration().getURL("test") + "?useCursorFetch=true");
    props.put(JdbcSourceConnectorConfig.CONNECTION_USER_CONFIG, "root");
    props.put(JdbcSourceConnectorConfig.MODE_CONFIG, JdbcSourceConnectorConfig.MODE_BULK);
    props.put(JdbcSourceTaskConfig.TOPIC_PREFIX_CONFIG, "topic_");
  }

  protected String buildLargeQuery() {
    StringBuilder qb = new StringBuilder();
    qb.append("SELECT");
    qb.append(" '");
    for (int i = 0; i < BYTES_PER_ROW; i++) {
      qb.append('a');
    }
    qb.append("' ");
    qb.append("FROM seq_1_to_");
    qb.append(LARGE_QUERY_ROW_COUNT);
    log.info(
        "Large query will generate "
            + MAX_MEMORY
            + " bytes across "
            + LARGE_QUERY_ROW_COUNT + " rows"
    );
    return qb.toString();
  }
}
