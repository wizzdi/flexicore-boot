package com.wizzdi.flexicore.boot.jpa.init.hibernate;

import java.lang.reflect.Method;

public class StandardEncryptionConfiguration extends EncryptionConfiguration{

	private final String encryptionKey;
	private final boolean bytea;
	private final String read;
	private final String write;
	private final String forColumn;


	public StandardEncryptionConfiguration(String encryptionKey, String columnName, Method getter) {
		this(encryptionKey,false,columnName,getter);
	}


	public StandardEncryptionConfiguration(String encryptionKey,boolean bytea, String columnName, Method getter) {
		super( columnName, getter);
		this.encryptionKey=encryptionKey;
		this.bytea=bytea;
		this.read= isBytea()?getReadBytea():getReadVarChar();
		this.write= isBytea()?getWriteBytea():getWriteVarChar();
		this.forColumn="";
	}

	private String getWriteBytea() {
		return "pgp_sym_encrypt(  ?, '" + getEncryptionKey() + "') ";
	}
	private String getWriteVarChar() {
		return "armor(pgp_sym_encrypt(  ?, '"+getEncryptionKey()+"')) ";
	}

	private String getReadBytea() {
		return "pgp_sym_decrypt(" + getColumnName() + ",'" + getEncryptionKey() + "')";
	}
	private String getReadVarChar() {
		return "pgp_sym_decrypt(dearmor("+getColumnName()+"),'"+getEncryptionKey()+"')";
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public boolean isBytea() {
		return bytea;
	}

	@Override
	public String getRead() {
		return read;
	}

	@Override
	public String getWrite() {
		return write;
	}

	@Override
	public String getForColumn() {
		return forColumn;
	}
}
