package com.jagornet.Entity;

/**
 * The DhcpOption POJO class for the DHCPOPTION database table.
 * 
 * @author A. Gregory Rabil
 */

public class DhcpOption
{
	protected Long id;	// the database-generated object ID
	protected int code;	// int = ushort
	protected byte[] value;	// value includes 2 bytes for length to facilitate encode/decode
	protected Long identityAssocId;
	protected Long iaAddressId;
	protected Long iaPrefixId;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 * 
	 * @param code the new code
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public byte[] getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param value the new value
	 */
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	/**
	 * Gets the identity assoc id.
	 * 
	 * @return the identity assoc id
	 */
	public Long getIdentityAssocId() {
		return identityAssocId;
	}
	
	/**
	 * Sets the identity assoc id.
	 * 
	 * @param identityAssocId the new identity assoc id
	 */
	public void setIdentityAssocId(Long identityAssocId) {
		this.identityAssocId = identityAssocId;
	}
	
	/**
	 * Gets the ia address id.
	 * 
	 * @return the ia address id
	 */
	public Long getIaAddressId() {
		return iaAddressId;
	}
	
	/**
	 * Sets the ia address id.
	 * 
	 * @param iaAddressId the new ia address id
	 */
	public void setIaAddressId(Long iaAddressId) {
		this.iaAddressId = iaAddressId;
	}

	/**
	 * Gets the ia prefix id.
	 *
	 * @return the ia prefix id
	 */
	public Long getIaPrefixId() {
		return iaPrefixId;
	}

	/**
	 * Sets the ia prefix id.
	 *
	 * @param iaPrefixId the new ia prefix id
	 */
	public void setIaPrefixId(Long iaPrefixId) {
		this.iaPrefixId = iaPrefixId;
	}

	public String toHexString(byte[] binary) {
		if(binary != null) {
			StringBuffer str = new StringBuffer(binary.length * 2);
			for(int i=0; i < binary.length; i++) {
				int v = (binary[i] << 24) >>> 24;
				str.append((v < 0x10 ? "0" : "") + Integer.toHexString(v));
			}
			return str.toString();
		}
		return null;
	}
	public String toString() {
		return "DhcpOption: code=" + getCode() + 
				" value=" + toHexString(getValue());
	}
}
