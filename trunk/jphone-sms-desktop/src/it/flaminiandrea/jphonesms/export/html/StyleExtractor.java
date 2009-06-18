package it.flaminiandrea.jphonesms.export.html;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class StyleExtractor {
	
	@SuppressWarnings("unchecked")
	public static boolean unzip(String fileUri, String pathWhereToExtract) throws IOException {
		final int BUFFER = 2048;
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile = new ZipFile(fileUri);
			Enumeration e = zipfile.entries();
			while(e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];
				if (entry.isDirectory()) {
					File directory = new File(pathWhereToExtract + entry.getName());
					if (!directory.exists())
						directory.mkdirs();
				} else {
					FileOutputStream fos = new FileOutputStream(pathWhereToExtract + entry.getName());
					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
					is.close();
				}
			}
			return true;
	}

}
