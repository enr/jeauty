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

public class EnhancementResult {

	public boolean success = false;
	public String contents = "";
	public List<String> errors = new ArrayList<>();

	public static EnhancementResult successful(String contents) {
		EnhancementResult res = new EnhancementResult();
		res.success = true;
		res.contents = contents;
		return res;
	}

	public static EnhancementResult fail(Throwable t) {
		EnhancementResult res = new EnhancementResult();
		res.errors.add(t.getMessage());
		return res;
	}

	public static EnhancementResult fail(String errorMessage) {
		EnhancementResult res = new EnhancementResult();
		res.errors.add(errorMessage);
		return res;
	}
}
