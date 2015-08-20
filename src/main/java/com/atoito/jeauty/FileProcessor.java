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
import java.io.IOException;

import com.google.common.base.Strings;
import com.google.common.io.Files;

public class FileProcessor {
	
	private final RunOptions options;
	private final Beautifier beautifier;
	
	public FileProcessor(RunOptions options) {
		this.options = options;
		this.beautifier = new Beautifier(this.options);
	}

	public RunResult process(File f) {
		if (f.isDirectory()) {
			return processDirectory(f);
		}
		return processFile(f);
	}
	
	private RunResult processDirectory(File root) {
		//System.out.println( "PROCESS DIR:" + root );
        File[] list = root.listFiles();
        if (list == null || list.length == 0) {
        	return RunResult.successful();
        }
        for ( File f : list ) {
        	String filePath = f.getAbsolutePath();
            if ( f.isDirectory() ) {
            	//System.out.println( "ISDIR:" + filePath );
                processDirectory(f);
            } else {
        		if (!support(filePath)) {
        			continue;
        		}
                RunResult result = processFile(f);
                if (!result.success) {
                	return result;
                }
            }
        }
        return RunResult.successful();
	}

	private RunResult processFile(File f) {
		//System.out.println( "PROCESS FILE:" + f );
		String filePath = f.getAbsolutePath();
		EnhancementResult result = beautifier.enhance(filePath);
        if (result.success) {
			if (options.isWrite()) {
				//System.out.println( " >> write contents to " + filePath );
				try {
					Files.write(result.contents, f, options.getCharset());
				} catch (IOException e) {
					return RunResult.fail(e);
				}
			} else {
				System.out.printf("Would write %s%n", filePath);
			}
		} else {
			return RunResult.fail(result);
		}
        return RunResult.successful();
	}

	private boolean support(String filePath) {
		return Strings.nullToEmpty(filePath).endsWith(".java");
	}
}
