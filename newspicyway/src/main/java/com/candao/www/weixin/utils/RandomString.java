package com.candao.www.weixin.utils;

import java.util.Random;

public class RandomString {

	
	public static String getRondom(int length){
		String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
          
        for(int i = 0 ; i < length; ++i){  
            int number = random.nextInt(62);//[0,62)  
              
            sb.append(str.charAt(number));  
        }  
        return sb.toString();  
	}
	
	public static void main(String args[]){
		RandomString randomString=new RandomString();
		System.out.println(randomString.getRondom(20));
	}
}
