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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.junit.Test;

import com.atoito.jeauty.test.TestUtils;
import com.google.common.collect.Lists;

public class AppTest {
	
	@Test
	public void testWriteHappyPathSingleFile() throws Exception {
		List<String> testData = Lists.newArrayList("Main01", "Main02", "Main03", "ShouldBeBroken01");
		Path tmp = Files.createTempDirectory(AppTest.class.getCanonicalName()+".F-");
		for (String testCase : testData) {
			String originalTestFile = String.format("src/test/data/%s.java", testCase);
			Path in = Paths.get(originalTestFile);
			File testFile = new File(tmp.toFile(), testCase+".java");
			System.out.println(testFile.getAbsolutePath());
			Files.copy(in, testFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			String expectedPath = String.format("src/test/data/%s.java.expected", testCase);
			Path expectedOutputFile = Paths.get(expectedPath);
			assertTrue(expectedPath + ": expected file not found", expectedOutputFile.toFile().exists());
			String[] args = { "--write", testFile.getAbsolutePath() };
			App app = new App();
			RunResult result = app.run(args);
			assertTrue("result success", result.success);
			assertEquals(testCase+" run result errors", 0, result.errors.size());
			TestUtils.compareLineToLine(testCase, expectedOutputFile, testFile.toPath());
		}
	}

	@Test
	public void testWriteHappyPathDirectory() throws Exception {
		List<String> testData = Lists.newArrayList("Main01", "Main02", "Main03", "ShouldBeBroken01");
		Path tmp = Files.createTempDirectory(AppTest.class.getCanonicalName()+".D-");
		for (String testCase : testData) {
			String originalTestFile = String.format("src/test/data/%s.java", testCase);
			Path in = Paths.get(originalTestFile);
			File testFile = new File(tmp.toFile(), testCase+".java");
			System.out.println(testFile.getAbsolutePath());
			Files.copy(in, testFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			String expectedPath = String.format("src/test/data/%s.java.expected", testCase);
			Path expectedOutputFile = Paths.get(expectedPath);
			assertTrue(expectedPath + ": expected file not found", expectedOutputFile.toFile().exists());
		}

		String[] args = { "--write", tmp.toFile().getAbsolutePath() };
		App app = new App();
		RunResult result = app.run(args);
		assertTrue("result success", result.success);
		assertEquals(" run result errors", 0, result.errors.size());
		for (String testCase : testData) {
			String expectedPath = String.format("src/test/data/%s.java.expected", testCase);
			Path expectedOutputFile = Paths.get(expectedPath);
			assertTrue(expectedPath + ": expected file not found", expectedOutputFile.toFile().exists());
			File testFile = new File(tmp.toFile(), testCase+".java");
			TestUtils.compareLineToLine(testCase, expectedOutputFile, testFile.toPath());
		}
	}
	
	@Test
	public void testFailIfFileNotExists() throws Exception {
		String[] args = { "--write", "src", "notexists" };
		App app = new App();
		RunResult result = app.run(args);
		assertTrue("result success", !result.success);
		assertEquals("run result errors", 1, result.errors.size());
	}

}
