/*
 * Copyright (C) 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wizzdi.flexicore.boot.test.helper;

import java.io.*;
import java.nio.file.Files;

/**
 * Get class data from the class path.
 *
 * @author Decebal Suiu
 */
public class PluginizedClassDataProvider implements ClassDataProvider {


	private final String basePath;

	public PluginizedClassDataProvider() {
		this("./target/");
	}

	public PluginizedClassDataProvider(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public byte[] getClassData(String className) {
		String path = className.replace('.', '/') + ".class";
		try{
		return  Files.readAllBytes(new File(basePath,path).toPath());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}


}