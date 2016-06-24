package com.candao.www.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrderDetailParseTest {

	@Test
	public void test() {
		String sperequire = "|||||";
		String freeAuthorize = OrderDetailParse.getFreeAuthorize(sperequire);
		assertEquals("", freeAuthorize);
		String freeUser = OrderDetailParse.getFreeUser(sperequire);
		assertEquals("", freeUser);
		String globalRemark = OrderDetailParse.getGlobalRemark(sperequire);
		assertEquals("", globalRemark);
		String freeReason = OrderDetailParse.getFreeReason(sperequire);
		assertEquals("", freeReason);
		String sperequire2 = OrderDetailParse.getSperequire(sperequire);
		assertEquals("", sperequire2);
		String taste = OrderDetailParse.getTaste(sperequire);
		assertEquals("", taste);
	}

	@Test
	public void testNormal(){
		String sperequire = "a|bb|c|d|e|fff";
		String sperequire2 = OrderDetailParse.getSperequire(sperequire);
		assertEquals("a", sperequire2);
		String globalRemark = OrderDetailParse.getGlobalRemark(sperequire);
		assertEquals("bb", globalRemark);
		String taste = OrderDetailParse.getTaste(sperequire);
		assertEquals("c", taste);
		String freeUser = OrderDetailParse.getFreeUser(sperequire);
		assertEquals("d", freeUser);
		String freeAuthorize = OrderDetailParse.getFreeAuthorize(sperequire);
		assertEquals("e", freeAuthorize);
		String freeReason = OrderDetailParse.getFreeReason(sperequire);
		assertEquals("fff", freeReason);
	}


	@Test
	public void testNormal1(){
		String sperequire = "";
		String sperequire2 = OrderDetailParse.getSperequire(sperequire);
		assertEquals("", sperequire2);
		String globalRemark = OrderDetailParse.getGlobalRemark(sperequire);
		assertEquals("", globalRemark);
		String taste = OrderDetailParse.getTaste(sperequire);
		assertEquals("", taste);
		String freeUser = OrderDetailParse.getFreeUser(sperequire);
		assertEquals("", freeUser);
		String freeAuthorize = OrderDetailParse.getFreeAuthorize(sperequire);
		assertEquals("", freeAuthorize);
		String freeReason = OrderDetailParse.getFreeReason(sperequire);
		assertEquals("", freeReason);
	}
	
	@Test
	public void testBeforeVersion(){
		String sperequire = "abc|[y]";
		String sperequire2 = OrderDetailParse.getSperequire(sperequire);
		assertEquals("abc|[y]", sperequire2);
		String globalRemark = OrderDetailParse.getGlobalRemark(sperequire);
		assertEquals("", globalRemark);
		String taste = OrderDetailParse.getTaste(sperequire);
		assertEquals("", taste);
		String freeUser = OrderDetailParse.getFreeUser(sperequire);
		assertEquals("", freeUser);
		String freeAuthorize = OrderDetailParse.getFreeAuthorize(sperequire);
		assertEquals("", freeAuthorize);
		String freeReason = OrderDetailParse.getFreeReason(sperequire);
		assertEquals("", freeReason);
	}
	
	@Test
	public void testException(){
		String sperequire = null;
		String sperequire2 = OrderDetailParse.getSperequire(sperequire);
		assertEquals("", sperequire2);
		String globalRemark = OrderDetailParse.getGlobalRemark(sperequire);
		assertEquals("", globalRemark);
		String taste = OrderDetailParse.getTaste(sperequire);
		assertEquals("", taste);
		String freeUser = OrderDetailParse.getFreeUser(sperequire);
		assertEquals("", freeUser);
		String freeAuthorize = OrderDetailParse.getFreeAuthorize(sperequire);
		assertEquals("", freeAuthorize);
		String freeReason = OrderDetailParse.getFreeReason(sperequire);
		assertEquals("", freeReason);
	}

	@Test
	public void testException1(){
		String sperequire = "||||||||";
		String sperequire2 = OrderDetailParse.getSperequire(sperequire);
		assertEquals("||||||||", sperequire2);
		String globalRemark = OrderDetailParse.getGlobalRemark(sperequire);
		assertEquals("", globalRemark);
		String taste = OrderDetailParse.getTaste(sperequire);
		assertEquals("", taste);
		String freeUser = OrderDetailParse.getFreeUser(sperequire);
		assertEquals("", freeUser);
		String freeAuthorize = OrderDetailParse.getFreeAuthorize(sperequire);
		assertEquals("", freeAuthorize);
		String freeReason = OrderDetailParse.getFreeReason(sperequire);
		assertEquals("", freeReason);
	}
}
