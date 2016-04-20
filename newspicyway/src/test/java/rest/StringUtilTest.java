package rest;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.candao.common.utils.StringUtils;

public class StringUtilTest {

	private String src;

	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("C:\\Users\\Administrator\\Desktop\\test.txt");
		byte[] array = new byte[1024];
		StringBuffer buffer = new StringBuffer();
		int length = 0;
		while ((length = fis.read(array)) != -1) {
			buffer.append(new String(array, 0, length));
		}
		fis.close();
		src = buffer.toString();
	}

	/**
	 * 测试分页性能，测试边界条件
	 */
	@Test
	public void testSplit() {
		// 临界校验
		String dst = getSplitString(50, src, ";");
		assertEquals("fatal error! diffrence result with param of size 2", src, dst);
		// 临界校验
		String dst1 = getSplitString(3, src,";" );
		assertEquals("fatal error! diffrence result with param of size 3", src, dst1);
		// 临界校验
		String dst2 = getSplitString(5, src, ";");
		assertEquals("fatal error! diffrence result with param of size 5", src, dst2);
		// 临界校验
		String dst3 = getSplitString(6, src,";" );
		assertEquals("fatal error! diffrence result with param of size 6", src, dst3);
		// 临界校验
		String dst4 = getSplitString(50, src,";" );
		assertEquals("fatal error! diffrence result with param of size 50", src, dst4);

	}
	
	/**
	 * 特殊参数的校验
	 * dilimiter 为空 和 size为0
	 */
	@Test
	public void testSplitBySpecialParam(){
		List<String> res = StringUtils.split("", null, 0);
		assertNull("fatal error! diffrence result with param of src null", res);
		
		List<String> res1 = StringUtils.split(src, ";", 0);
		assertEquals("fatal error! diffrence result with param of size 0", src, res1.get(0));

		List<String> res2 = StringUtils.split(src, null, 0);
		assertEquals("fatal error! diffrence result with param of delimit null", src, res2.get(0));
		
		List<String> res3 = StringUtils.split(src, null, 3);
		assertEquals("fatal error! diffrence result with param of delimit null", src, res3.get(0));
	}

	private String getSplitString(int size, String src,String delimiter) {
		List<String> res = StringUtils.split(src, delimiter, size);
		StringBuffer buffer = new StringBuffer();
		if (res != null) {
			for (String string : res) {
				buffer.append(string);
			}
		}

		String dst2 = buffer.toString();
		return dst2;
	}
}
