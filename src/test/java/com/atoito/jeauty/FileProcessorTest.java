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

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.atoito.jeauty.test.TestUtils;

public class FileProcessorTest {

	@Test
	public void testEmptyDirGivesResultSuccess() throws Exception {
		Path tmp = Files.createTempDirectory(FileProcessorTest.class.getCanonicalName());
		String[] args = { "--write", tmp.toString() };
		RunOptions options = TestUtils.runOptions(args);
		FileProcessor fileProcessor = new FileProcessor(options);
		RunResult result = fileProcessor.process(tmp.toFile());
		assertEquals("files", 0, tmp.toFile().listFiles().length);
		assertTrue("result success", result.success);
		assertEquals("process result errors", 0, result.errors.size());
	}

}
