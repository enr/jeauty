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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.DumpVisitor;
import com.google.common.base.Strings;
import com.google.common.io.Closeables;

public class Beautifier {

	private RunOptions runOptions;

	private final Comparator<ImportDeclaration> importsComparator = new Comparator<ImportDeclaration>() {
		public int compare(ImportDeclaration o1, ImportDeclaration o2) {
			return o1.getName().toStringWithoutComments().compareTo(o2.getName().toStringWithoutComments());
		}
	};

	public Beautifier(RunOptions options) {
		this.runOptions = options;
	}

	public EnhancementResult enhance(String sourceFilePath) {
		if (Strings.isNullOrEmpty(sourceFilePath)) {
			return EnhancementResult.fail("Invalid empty source file path");			
		}
		Reader fr = null;
		try {
			fr = new InputStreamReader(new FileInputStream(sourceFilePath), runOptions.getCharset());
		} catch (FileNotFoundException e) {
			return EnhancementResult.fail(e);
		}
		BufferedReader br = new BufferedReader(fr);
		StringBuilder fmt = new StringBuilder();
		String s;
		boolean currentLineEmpty = false;
		boolean lastLineEmpty = false;
		try {
			while ((s = br.readLine()) != null) {
				currentLineEmpty = s.trim().isEmpty();
				if (currentLineEmpty && lastLineEmpty) {
					lastLineEmpty = currentLineEmpty;
				} else {
					fmt.append(spaceIndentation(s)).append(runOptions.getEol());
					lastLineEmpty = currentLineEmpty;
				}
			}
		} catch (IOException ioe) {
			return EnhancementResult.fail(ioe);
		} finally {
			Closeables.closeQuietly(fr);
			Closeables.closeQuietly(br);
		}
		String spacedSourceCode = fmt.toString();
		return beautify(spacedSourceCode);
	}

	/**
	 * Return the String representation of this node.
	 * 
	 * @return the String representation of this node
	 */
	private EnhancementResult beautify(String spacedSourceCode) {
		byte[] bytes = spacedSourceCode.getBytes(runOptions.getCharset());
		InputStream in = new ByteArrayInputStream(bytes);
		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(in);
		} catch (Exception e) {
			return EnhancementResult.fail(e);
		} finally {
			Closeables.closeQuietly(in);
		}
		if (cu == null) {
			return EnhancementResult.fail("Unexpected error parsing sources (cu=null)");
		}
		boolean printComments = true;
		final DumpVisitor visitor = new DumpVisitor(printComments);
		visitor.visit(cu, null);
		StringBuilder sb = new StringBuilder();
		int beginLine = cu.getBeginLine();
		if (beginLine > 1) {
			EnhancementResult headersResult = headerComments(spacedSourceCode, (beginLine - 1));
			if (!headersResult.success) {
				return headersResult;
			}
			String headers = headersResult.contents;
			EnhancementResult codeResult = withoutHeaderComments(spacedSourceCode, (beginLine - 1));
			if (!codeResult.success) {
				return codeResult;
			}
			String code = codeResult.contents;
			InputStream sin = null;
			try {
				sin = new ByteArrayInputStream(code.getBytes(runOptions.getCharset()));
				cu = JavaParser.parse(sin);
			} catch (Exception e) {
				return EnhancementResult.fail(e);
			} finally {
				Closeables.closeQuietly(sin);
			}
			if (cu == null) {
				return EnhancementResult.fail("Unexpected error parsing sources (cu=null)");
			}
			final DumpVisitor nv = new DumpVisitor(printComments);
			nv.visit(cu, null);
			sb.append(headers);
		}
		PackageDeclaration pd = cu.getPackage();
		sb.append(pd.toString());
		List<ImportDeclaration> imports = cu.getImports();
		if (imports != null && !imports.isEmpty()) {
			List<ImportDeclaration> javaImports = new ArrayList<>();
			List<ImportDeclaration> otherImports = new ArrayList<>();
			for (ImportDeclaration importDeclaration : imports) {
				NameExpr name = importDeclaration.getName();
				if (name.toStringWithoutComments().startsWith("java.")) {
					javaImports.add(importDeclaration);
				} else {
					otherImports.add(importDeclaration);
				}
			}
			if (!javaImports.isEmpty()) {
				Collections.sort(javaImports, importsComparator);
				for (ImportDeclaration importDeclaration : javaImports) {
					sb.append(importDeclaration.toString());
				}
				sb.append(runOptions.getEol());
			}
			if (!otherImports.isEmpty()) {
				Collections.sort(otherImports, importsComparator);
				for (ImportDeclaration importDeclaration : otherImports) {
					sb.append(importDeclaration.toString());
				}
				sb.append(runOptions.getEol());
			}
		}
		List<Comment> orphanComments = cu.getOrphanComments();
		if (orphanComments != null && !orphanComments.isEmpty()) {
			for (Comment comment : orphanComments) {
				sb.append(comment.toString());
			}
			sb.append(runOptions.getEol());
		}
		List<TypeDeclaration> types = cu.getTypes();
		for (TypeDeclaration typeDeclaration : types) {
			sb.append(typeDeclaration.toString());
		}
		if (runOptions.eofWithNewLine()) {
			sb.append(runOptions.getEol());
		}
		return EnhancementResult.successful(sb.toString());
	}

	private EnhancementResult headerComments(String sourceCode, int linesNum) {
		Reader fr = new StringReader(sourceCode);
		BufferedReader br = new BufferedReader(fr);
		StringBuilder sb = new StringBuilder();
		boolean currentLineEmpty = false;
		boolean lastLineEmpty = false;
		String s;
		try {
			for (int i = 0; i < linesNum; i++) {
				s = br.readLine();
				if (s == null) {
					break;
				}
				currentLineEmpty = s.trim().isEmpty();
				if (currentLineEmpty && lastLineEmpty) {
					lastLineEmpty = currentLineEmpty;
				} else {
					sb.append(s).append(runOptions.getEol());
					lastLineEmpty = currentLineEmpty;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			EnhancementResult.fail(e);
		} finally {
			Closeables.closeQuietly(fr);
			Closeables.closeQuietly(br);
		}
		if (!lastLineEmpty) {
			sb.append(runOptions.getEol());
		}
		return EnhancementResult.successful(sb.toString());
	}

	private EnhancementResult withoutHeaderComments(String sourceCode, int linesNum) {
		Reader fr = new StringReader(sourceCode);
		BufferedReader br = new BufferedReader(fr);
		StringBuilder sb = new StringBuilder();
		try {
			String s;
			int i = 0;
			while ((s = br.readLine()) != null) {
				if (i >= linesNum) {
					sb.append(s).append(runOptions.getEol());
				}
				i++;
			}
		} catch (Exception e) {
			return EnhancementResult.fail(e);
		} finally {
			Closeables.closeQuietly(fr);
			Closeables.closeQuietly(br);
		}
		sb.append(runOptions.getEol());
		return EnhancementResult.successful(sb.toString());
	}

	private String spaceIndentation(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		int ns = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\t') {
				ns += runOptions.getTabSpaces();
			} else if (Character.isWhitespace(c)) {
				ns += 1;
			} else {
				break;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ns; i++) {
			sb.append(' ');
		}
		sb.append(s.trim());
		return sb.toString();
	}
}
