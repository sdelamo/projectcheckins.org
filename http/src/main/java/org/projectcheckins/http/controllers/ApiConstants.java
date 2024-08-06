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

package org.projectcheckins.http.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "projectcheckins",
                version = "1.0"
        )
)
public interface ApiConstants {
    String PATH_VARIABLE_ID = "{id}";

    String SLASH = "/";
    String DOT = ".";
    String ACTION_DELETE = "delete";

    String ACTION_SHOW = "show";

    String ACTION_CREATE = "create";
    String ACTION_EDIT = "edit";
    String ACTION_SAVE = "save";
    String ACTION_UPDATE = "update";
    String ACTION_LIST = "list";
    String DOT_HTML = ".html";
    String PATH_DELETE =  SLASH + PATH_VARIABLE_ID + SLASH + ACTION_DELETE;
    String PATH_CREATE = SLASH + ACTION_CREATE;
    String VIEW_CREATE = SLASH + ACTION_CREATE + DOT_HTML;
    String VIEW_EDIT = SLASH + ACTION_EDIT + DOT_HTML;
    String VIEW_LIST = SLASH + ACTION_LIST + DOT_HTML;
    String VIEW_SHOW = SLASH + ACTION_SHOW + DOT_HTML;
    String PATH_SHOW = SLASH + PATH_VARIABLE_ID + SLASH + ACTION_SHOW;
    String PATH_EDIT = SLASH + PATH_VARIABLE_ID + SLASH + ACTION_EDIT;
    String PATH_SAVE = SLASH + ACTION_SAVE;
    String PATH_UPDATE = SLASH + PATH_VARIABLE_ID + SLASH + ACTION_UPDATE;
    String PATH_LIST = SLASH + ACTION_LIST;
    String MODEL_FORM = "form";
    String MODEL_BREADCRUMBS = "breadcrumbs";
}
