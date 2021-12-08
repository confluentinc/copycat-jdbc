/*
 * Copyright 2021 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.connect.jdbc.data;

import java.sql.Timestamp;


import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

/**
 * Utility class for managing nano timestamp representations
 * 
 */
public class NanoStringTimestamp {
  public static final String SCHEMA_NAME = "io.confluent.connect.jdbc.data.NanoTimestampString";

  static final String NULL_TIMESTAMP = "NULL";

  public static SchemaBuilder builder() {
    return SchemaBuilder.string()
                        .name(SCHEMA_NAME)
                        .version(1);
  }

  public static Schema schema() {
    return builder().build();
  }

  public static String toNanoString(Timestamp timestamp) {
    if (timestamp != null) {
      return timestamp.toString();
    } else {
      return null;
      // return NULL_TIMESTAMP;
    }
  }

  public static Timestamp fromNanoString(String nanoString) {
    if (nanoString != null) {
      // if (nanoString != null && ! nanoString.equalsIgnoreCase(NULL_TIMESTAMP)) {
      return Timestamp.valueOf(nanoString);
    } else {
      return null;
    }
  }

  private NanoStringTimestamp() {
  }
}