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

import java.io.File;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class App {

	public static void main(String[] args) throws Exception {
		App app = new App();
		RunResult result = app.run(args);
		if (result.success) {
			System.exit(0);
		}
		System.err.printf("Errors:%n%s%n", result.errors);
		System.exit(1);
	}

	private FileProcessor fileProcessor;
	
	public RunResult run(String[] args) {
		CliFlags flags = new CliFlags();
		try {
			new JCommander(flags, args);			
		} catch (ParameterException e) {
			return RunResult.fail(e);
		}
		RunOptions options = RunOptions.fromCliFlags(flags);
		if (flags.parameters.isEmpty()) {
			return RunResult.fail("missing input paths");
		}
		fileProcessor = new FileProcessor(options);
		for (String param: flags.parameters) {
			File f = new File(param);
			if (!f.exists()) {
				return RunResult.fail("not found file "+param);
			}
		}		
		for (String param: flags.parameters) {
			File f = new File(param);
			RunResult result = fileProcessor.process(f);
			if (!result.success) {
				return result;
			}
		}
		return RunResult.successful();
	}
}
