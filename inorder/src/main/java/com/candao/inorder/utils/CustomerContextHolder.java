package com.candao.inorder.utils;

public class CustomerContextHolder {

	private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

	public static void setCustomerType(String customerType) {

		dataSourceKey.set(customerType);

	}

	public static String getCustomerType() {

		return (String) dataSourceKey.get();

	}


	public static void clearCustomerType() { 
		dataSourceKey.remove(); 

    }

}
