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
 */
package org.apache.beam.sdk.nexmark.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Objects;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.coders.VarLongCoder;
import org.apache.beam.sdk.nexmark.NexmarkUtils;

/**
 * Result of Query5.
 */
public class AuctionCount implements KnownSize, Serializable {
  private static final Coder<Long> LONG_CODER = VarLongCoder.of();

  public static final Coder<AuctionCount> CODER = new CustomCoder<AuctionCount>() {
    @Override
    public void encode(AuctionCount value, OutputStream outStream)
        throws CoderException, IOException {
      LONG_CODER.encode(value.auction, outStream);
      LONG_CODER.encode(value.num, outStream);
    }

    @Override
    public AuctionCount decode(InputStream inStream)
        throws CoderException, IOException {
      long auction = LONG_CODER.decode(inStream);
      long num = LONG_CODER.decode(inStream);
      return new AuctionCount(auction, num);
    }

    @Override
    public Object structuralValue(AuctionCount v) {
      return v;
    }
  };

  @JsonProperty public final long auction;

  @JsonProperty public final long num;

  // For Avro only.
  @SuppressWarnings("unused")
  private AuctionCount() {
    auction = 0;
    num = 0;
  }

  public AuctionCount(long auction, long num) {
    this.auction = auction;
    this.num = num;
  }

  @Override
  public boolean equals(Object otherObject) {
    if (this == otherObject) {
      return true;
    }
    if (otherObject == null || getClass() != otherObject.getClass()) {
      return false;
    }

    AuctionCount other = (AuctionCount) otherObject;
    return Objects.equals(auction, other.auction)
        && Objects.equals(num, other.num);
  }

  @Override
  public int hashCode() {
    return Objects.hash(auction, num);
  }

  @Override
  public long sizeInBytes() {
    return 8L + 8L;
  }

  @Override
  public String toString() {
    try {
      return NexmarkUtils.MAPPER.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
