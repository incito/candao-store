/**
 * 
 */
package com.candao.www.data.model;

/**
 * @author zhao
 *
 */
public class BusinessUser extends User {
  /**
   * 
   */
	private static final long serialVersionUID = -6021454173868573295L;
	
	private String userId;
	private String province;
	private String city;
	private String region;
	private String address;
	private String businessName;
  /**
   * @return the userId
   */
  public String getUserId() {
    return userId;
  }
  /**
   * @param userId the userId to set
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }
  /**
   * @return the province
   */
  public String getProvince() {
    return province;
  }
  /**
   * @param province the province to set
   */
  public void setProvince(String province) {
    this.province = province;
  }
  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }
  /**
   * @param city the city to set
   */
  public void setCity(String city) {
    this.city = city;
  }
  /**
   * @return the region
   */
  public String getRegion() {
    return region;
  }
  /**
   * @param region the region to set
   */
  public void setRegion(String region) {
    this.region = region;
  }
  /**
   * @return the address
   */
  public String getAddress() {
    return address;
  }
  /**
   * @param address the address to set
   */
  public void setAddress(String address) {
    this.address = address;
  }
  /**
   * @return the businessName
   */
  public String getBusinessName() {
    return businessName;
  }
  /**
   * @param businessName the businessName to set
   */
  public void setBusinessName(String businessName) {
    this.businessName = businessName;
  }
}
