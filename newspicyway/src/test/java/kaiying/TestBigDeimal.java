package kaiying;

import java.math.BigDecimal;

import com.candao.www.webroom.model.TjObj;

public class TestBigDeimal {

	public static void main (String [] args){
		
		BigDecimal bDecimal = new BigDecimal("0.34");
//		bDecimal = bDecimal.multiply(new BigDecimal(100));
		System.out.println(bDecimal);
		
		TjObj tj = new TjObj();
		tj.setObjvalue(bDecimal.multiply(new BigDecimal(100)));
		
		System.out.println(tj.getObjvalue());
	}
}
