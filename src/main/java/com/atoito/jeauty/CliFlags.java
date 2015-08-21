/**
 * Copyright (C) 2015 Jeauty contributors <https://github.com/enr/jeauty>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atoito.jeauty;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.google.common.collect.Lists;

public class CliFlags {

    @Parameter(description = "The list of paths to check")
    public List<String> paths = Lists.newArrayList();

//    @Parameter(names = {"-c", "--charset"}, description = "Charset")
//    public String charset = "utf-8";
//    @Parameter(names = { "-s", "--soft-tabs" }, description = "Use soft tabs")
//    public boolean softTabs = true;

    @Parameter(names = {"-d", "--debug"}, description = "Debug mode")
    public boolean debug = false;

    @Parameter(names = {"-w", "--write"}, description = "Overwrite sources with Jeauty's version")
    public boolean write = false;

    @Parameter(names = {"-h", "--help"}, description = "Print this help", help = true)
    public boolean help;

}
