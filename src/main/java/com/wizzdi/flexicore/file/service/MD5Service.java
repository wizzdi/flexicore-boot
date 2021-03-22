package com.wizzdi.flexicore.file.service;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.apache.commons.codec.binary.Hex;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Extension
@Component
public class MD5Service implements Plugin {

	private static final Logger logger = LoggerFactory.getLogger(MD5Service.class);

	public String generateMD5(byte[] inputBytes) {
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes)) {
			return generateMD5(byteArrayInputStream);
		} catch (IOException e) {
			logger.error("could not open stream", e);
		}
		return null;

	}

	public String generateMD5(String filePath) {
		if (filePath != null) {
			File file = new File(filePath);
			if (file.exists()) {
				return generateMD5(file);
			}
		}
		return null;
	}

	public String generateMD5(File file) {

		FileInputStream is;
		try {
			is = new FileInputStream(file);
			return generateMD5(is);
		} catch (FileNotFoundException e) {
			logger.error("could not open stream", e);
		}
		return null;

	}

	public String generateMD5(InputStream is) {
		String hash = null;
		try {

			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[10240];
			int read;
			while ((read = is.read(buffer)) != -1) {
				digest.update(buffer, 0, read);
			}

			byte[] hashed = digest.digest();
			hash = Hex.encodeHexString(hashed);
			is.close();

		} catch (NoSuchAlgorithmException | IOException e) {
			logger.warn("unable to generate MD5", e);
			if (is != null) {
				try {
					is.close();
				} catch (IOException e1) {
					logger.warn("unable to close is", e1);
				}
			}

		}
		return hash;
	}

}
