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

public class RunOptions {
	
	private boolean debug;

	private String eol;

	private Charset charset;
	
	private boolean softTabs;

	private int tabSpaces;
	
	private boolean comments;
	
	private boolean write;
	
	/*
	 * Add new line at the end of the file
	 */
	private boolean eofnl;
	
	public static RunOptions defaults() {
		RunOptions options = new RunOptions();
		options.setEol(Settings.DEFAULT_EOL);
		options.setCharset(Settings.DEFAULT_CHARSET);
		options.useSoftTabs(Settings.DEFAULT_SOFT_TABS);
		options.setTabSpaces(Settings.DEFAULT_SOFT_TABS_SPACES);
		options.keepComments(Settings.DEFAULT_KEEP_COMMENTS);
		options.addNewLineAtEof(Settings.DEFAULT_EOFNL);
		options.setDebug(Settings.DEFAULT_DEBUG);
		return options;
	}

	public static RunOptions fromCliFlags(CliFlags flags) {
		RunOptions options = new RunOptions();
		options.setWrite(flags.write);
		options.setDebug(flags.debug);
		options.setEol(Settings.DEFAULT_EOL);
		options.setCharset(Settings.DEFAULT_CHARSET);
		options.useSoftTabs(Settings.DEFAULT_SOFT_TABS);
		options.setTabSpaces(Settings.DEFAULT_SOFT_TABS_SPACES);
		options.keepComments(Settings.DEFAULT_KEEP_COMMENTS);
		options.addNewLineAtEof(Settings.DEFAULT_EOFNL);
		return options;
	}
	
	public String getEol() {
		return eol;
	}

	public void setEol(String eol) {
		this.eol = eol;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public boolean useSoftTabs() {
		return softTabs;
	}

	public void useSoftTabs(boolean softTabs) {
		this.softTabs = softTabs;
	}

	public int getTabSpaces() {
		return tabSpaces;
	}

	public void setTabSpaces(int tabSpaces) {
		this.tabSpaces = tabSpaces;
	}

	public boolean keepComments() {
		return comments;
	}

	public void keepComments(boolean keepComments) {
		this.comments = keepComments;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public boolean eofWithNewLine() {
		return eofnl;
	}

	public void addNewLineAtEof(boolean eofnl) {
		this.eofnl = eofnl;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
