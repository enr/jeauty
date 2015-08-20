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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.atoito.jeauty.CliFlags;
import com.atoito.jeauty.RunOptions;
import com.atoito.jeauty.Settings;
import com.beust.jcommander.JCommander;

public class RunOptionsTest {

	@Test
	public void testWriteHappyPath() throws Exception {
		CliFlags flags = new CliFlags();
		String[] args = { "--write",
		                  "file.java" };
		new JCommander(flags, args);
		RunOptions options = RunOptions.fromCliFlags(flags);
		assertTrue("write", options.isWrite());
	}
	
	@Test
	public void testDefaultOptions() throws Exception {
		CliFlags flags = new CliFlags();
		String[] args = { "file.java" };
		new JCommander(flags, args);
		RunOptions options = RunOptions.fromCliFlags(flags);
		assertEquals("charset", Settings.DEFAULT_CHARSET, options.getCharset());
		assertEquals("keep comments", Settings.DEFAULT_KEEP_COMMENTS, options.keepComments());
		assertEquals("use soft tabs", Settings.DEFAULT_SOFT_TABS, options.useSoftTabs());
	}
}
