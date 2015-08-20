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

import java.util.ArrayList;
import java.util.List;

public class RunResult {

	public boolean success = false;
	public List<String> errors = new ArrayList<>();

	public static RunResult successful() {
		RunResult res = new RunResult();
		res.success = true;
		return res;
	}

	public static RunResult fail(Throwable t) {
		RunResult res = new RunResult();
		res.errors.add(t.getMessage());
		return res;
	}

	public static RunResult fail(String errorMessage) {
		RunResult res = new RunResult();
		res.errors.add(errorMessage);
		return res;
	}

	public static RunResult fail(EnhancementResult enhancementResult) {
		RunResult res = new RunResult();
		res.errors = enhancementResult.errors;
		return res;
	}
}
