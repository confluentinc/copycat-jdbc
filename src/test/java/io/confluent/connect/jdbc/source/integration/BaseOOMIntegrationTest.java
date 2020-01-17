package io.confluent.connect.jdbc.source.integration;

import static junit.framework.TestCase.assertTrue;

import io.confluent.connect.jdbc.source.JdbcSourceConnectorConfig;
import io.confluent.connect.jdbc.source.JdbcSourceTask;
import io.confluent.connect.jdbc.source.JdbcSourceTaskConfig;
import java.util.Map;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseOOMIntegrationTest {

  private static Logger log = LoggerFactory.getLogger(BaseOOMIntegrationTest.class);

  public static final long MAX_MEMORY = Runtime.getRuntime().maxMemory();

  public static final int BYTES_PER_ROW = 1024;
  // enough rows to take up the whole heap
  public static final long LARGE_QUERY_ROW_COUNT = MAX_MEMORY / BYTES_PER_ROW;

  protected abstract String buildLargeQuery();

  public Map<String, String> props;
  public JdbcSourceTask task;

  public void startTask() {
    task = new JdbcSourceTask();
    task.start(props);
  }

  @After
  public void stopTask() {
    if (task != null) {
      task.stop();
    }
  }

  @Test(expected = OutOfMemoryError.class)
  public void assertOutOfMemoryWithLargeBatch() throws InterruptedException {
    props.put(JdbcSourceTaskConfig.TABLES_CONFIG, "");
    props.put(JdbcSourceConnectorConfig.QUERY_CONFIG, buildLargeQuery());
    props.put(JdbcSourceConnectorConfig.BATCH_MAX_ROWS_CONFIG, Long.toString(LARGE_QUERY_ROW_COUNT));
    startTask();
    task.poll();
  }

  @Test
  public void testStreamingReads() throws InterruptedException {
    props.put(JdbcSourceTaskConfig.TABLES_CONFIG, "");
    props.put(JdbcSourceConnectorConfig.QUERY_CONFIG, buildLargeQuery());
    startTask();
    assertTrue(task.poll().size() > 0);
  }
}
