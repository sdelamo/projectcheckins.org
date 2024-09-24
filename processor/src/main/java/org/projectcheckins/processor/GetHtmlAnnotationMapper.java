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

package org.projectcheckins.processor;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.AnnotationValueBuilder;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.inject.annotation.TypedAnnotationMapper;
import io.micronaut.inject.visitor.VisitorContext;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.views.View;
import io.micronaut.views.turbo.TurboFrameView;
import io.swagger.v3.oas.annotations.Hidden;
import org.projectcheckins.annotations.GetHtml;

import java.util.ArrayList;
import java.util.List;

public class GetHtmlAnnotationMapper implements TypedAnnotationMapper<GetHtml> {

    public static final String MEMBER_VALUE = "value";
    public static final String MEMBER_VIEW = "view";
    public static final String MEMBER_URI = "uri";
    private static final String MEMBER_TURBO_VIEW = "turboView";
    public static final String MEMBER_ROLESALLOWED  = "rolesAllowed";
    public static final String MEMBER_EXECUTES_ON = "executesOn";
    public static final String MEMBER_HIDDEN = "hidden";
    public static final String MEMBER_ACTION = "action";

    @Override
    public Class<GetHtml> annotationType() {
        return GetHtml.class;
    }

    @Override
    public List<AnnotationValue<?>> map(AnnotationValue<GetHtml> annotation, VisitorContext visitorContext) {
        List<AnnotationValue<?>> result = new ArrayList<>();

        result.add(AnnotationValue.builder(Produces.class).member(MEMBER_VALUE, MediaType.TEXT_HTML).build());

        annotation.stringValue(MEMBER_VIEW)
                .filter(StringUtils::isNotEmpty)
                .ifPresent(view ->
                result.add(AnnotationValue.builder(View.class).member(MEMBER_VALUE, view).build()));

        annotation.stringValue(MEMBER_TURBO_VIEW)
                .filter(StringUtils::isNotEmpty)
                .ifPresent(view -> {
                    AnnotationValueBuilder<TurboFrameView> b = AnnotationValue.builder(TurboFrameView.class)
                                        .member(MEMBER_VALUE, view);
                    result.add(b.build());
                });

        annotation.stringValue(MEMBER_URI)
                .filter(StringUtils::isNotEmpty)
                .ifPresent(uri ->
                result.add(AnnotationValue.builder(Get.class).member(MEMBER_URI, uri).build()));

        String[] rolesAllowed = annotation.stringValues(MEMBER_ROLESALLOWED);
        result.add(AnnotationValue.builder(Secured.class).member(MEMBER_VALUE, rolesAllowed).build());

        result.add(AnnotationValue.builder(ExecuteOn.class).member(MEMBER_VALUE, annotation.stringValue(MEMBER_EXECUTES_ON).orElse(TaskExecutors.BLOCKING)).build());
        if (annotation.booleanValue(MEMBER_HIDDEN).orElse(true)) {
            result.add(AnnotationValue.builder(Hidden.class).build());
        }

        return result;
    }
}
