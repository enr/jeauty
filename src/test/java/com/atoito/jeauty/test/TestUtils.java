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
package com.atoito.jeauty.test;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.atoito.jeauty.CliFlags;
import com.atoito.jeauty.RunOptions;
import com.beust.jcommander.JCommander;
import com.google.common.base.Charsets;

public class TestUtils {
	
	public static RunOptions runOptions(String[] args) {
		CliFlags flags = new CliFlags();
		new JCommander(flags, args);
		RunOptions options = RunOptions.fromCliFlags(flags);
		return options;
	}

	public static void compareLineToLine(String testCase, Path expectedPath, Path actualPath) throws Exception {
		List<String> expectedLines = Files.readAllLines(expectedPath, Charsets.UTF_8);
		List<String> lines = Files.readAllLines(actualPath, Charsets.UTF_8);
		assertEquals(testCase+" lines", expectedLines.size(), lines.size());
		String id = "";
		int i = 0;
		for (String actualLine : lines) {
			id = String.format("%s L%d", testCase, (i+1));
			String expectedLine = expectedLines.get(i);
			assertEquals(id, expectedLine, actualLine);
			i++;
        }
	}
}
