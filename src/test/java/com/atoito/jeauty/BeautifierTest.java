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

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import com.atoito.jeauty.Beautifier;
import com.atoito.jeauty.RunOptions;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

public class BeautifierTest {

	@Test
	public void testHappyPath() throws Exception {
		List<String> testData = Lists.newArrayList("Main01", "Main02", "Main03", "ShouldBeBroken01");
		for (String testCase : testData) {
			String testFile = String.format("src/test/data/%s.java", testCase);
			assertTrue(testFile + ": test file not found", new File(testFile).exists());
			String expectedPath = String.format("src/test/data/%s.java.expected", testCase);
			Path expectedOutputFile = Paths.get(expectedPath);
			assertTrue(expectedPath + ": expected file not found", expectedOutputFile.toFile().exists());
			List<String> expectedLines = Files.readAllLines(expectedOutputFile, Charsets.UTF_8);

			RunOptions options = RunOptions.defaults();
			Beautifier beautifier = new Beautifier(options);
			EnhancementResult result = beautifier.enhance(testFile);
			assertTrue(testCase+" enhancement result success", result.success);
			assertEquals(testCase+" enhancement result errors", 0, result.errors.size());
//			System.err.println(result);
//			System.err.println("-----------------------");
			Reader fr = new StringReader(result.contents);
	        BufferedReader br = new BufferedReader(fr);

			String id = "";
			int i = 0;
	        String actualLine;
	        while ((actualLine = br.readLine()) != null) {
				id = String.format("%s L%d", testCase, i);
				String expectedLine = expectedLines.get(i);
				assertEquals(id, expectedLine, actualLine);
				i++;
	        }
	        fr.close();
	        br.close();
		}
	}
	
	@Test
	public void testInvalidFile() throws Exception {
		List<String> testData = Lists.newArrayList("NOT_EXISTS", "", null);
		for (String testCase : testData) {
			RunOptions options = RunOptions.defaults();
			Beautifier beautifier = new Beautifier(options);
			EnhancementResult result = beautifier.enhance(testCase);
			assertFalse(" enhancement result success", result.success);
			assertEquals(" enhancement result errors", 1, result.errors.size());
		}
	}

	@Test
	public void testBrokenClass01() throws Exception {
		List<String> testData = Lists.newArrayList("Broken01");
		for (String testCase : testData) {
			String testFile = String.format("src/test/data/%s.java", testCase);
			assertTrue(testFile + ": test file not found", new File(testFile).exists());
			RunOptions options = RunOptions.defaults();
			Beautifier beautifier = new Beautifier(options);
			EnhancementResult result = beautifier.enhance(testFile);
			assertFalse(testCase+" enhancement result success", result.success);
			assertEquals(testCase+" enhancement result errors", 1, result.errors.size());
		}
	}

}
