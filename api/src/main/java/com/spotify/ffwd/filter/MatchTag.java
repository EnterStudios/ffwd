/*
 * Copyright 2013-2017 Spotify AB. All rights reserved.
 *
 * The contents of this file are licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.spotify.ffwd.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.spotify.ffwd.model.Event;
import com.spotify.ffwd.model.Metric;
import lombok.Data;

import java.io.IOException;

@Data
public class MatchTag implements Filter {
    private final String key;
    private final String value;

    @Override
    public boolean matchesEvent(final Event event) {
        final String value = event.getTags().get(key);

        if (value == null) {
            return false;
        }

        return value.equals(this.value);
    }

    @Override
    public boolean matchesMetric(final Metric metric) {
        final String value = metric.getTags().get(key);

        if (value == null) {
            return false;
        }

        return value.equals(this.value);
    }

    public static class Deserializer implements FilterDeserializer.PartialDeserializer {
        @Override
        public Filter deserialize(JsonParser p, DeserializationContext ctx)
            throws IOException, JsonProcessingException {
            final String key = p.nextTextValue();
            final String value = p.nextTextValue();

            if (p.nextToken() != JsonToken.END_ARRAY) {
                throw ctx.wrongTokenException(p, JsonToken.END_ARRAY, null);
            }

            return new MatchTag(key, value);
        }
    }
}
