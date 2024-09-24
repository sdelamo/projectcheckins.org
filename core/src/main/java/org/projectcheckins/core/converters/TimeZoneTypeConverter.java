// Copyright 2024 Object Computing, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.projectcheckins.core.converters;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import java.util.Optional;
import java.util.TimeZone;


public class TimeZoneTypeConverter implements TypeConverter<String, TimeZone>{
    @Override
    public Optional<TimeZone> convert(String timeZoneId, Class<TimeZone> targetType, ConversionContext context) {
        return Optional.of(TimeZone.getTimeZone(timeZoneId));
    }
}
