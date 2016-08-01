package xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

import org.springframework.core.io.ClassPathResource;

public class XmlTest {

	public static void main(String[] args) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
//		loader.
		Enumeration<URL> dirs = loader.getResources("template/printerTemplate");
		
		while (dirs.hasMoreElements()) {
			URL url = (URL) dirs.nextElement();
			
			String protocol = url.getProtocol();
			
			if ("file".equals(protocol)) {
				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
				System.out.println(filePath);
				File file = new File(filePath);
				for (int i = 0; i < file.list().length; i++) {
					System.out.println(file.list()[i]);
				}
			}
		}

	}
}
