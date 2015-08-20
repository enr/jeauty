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

import java.nio.charset.Charset;

import com.google.common.base.Charsets;

public interface Settings {

	public static Charset DEFAULT_CHARSET = Charsets.UTF_8;
	public static String DEFAULT_EOL = "\n";
	public static boolean DEFAULT_KEEP_COMMENTS = true;
	public static boolean DEFAULT_SOFT_TABS = true;
	public static int DEFAULT_SOFT_TABS_SPACES = 4;
	public static boolean DEFAULT_EOFNL = true;
	public static boolean DEFAULT_DEBUG = false;
}
