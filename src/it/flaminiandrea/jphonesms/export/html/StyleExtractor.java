package it.flaminiandrea.jphonesms.export.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StyleExtractor {

	public static boolean unzip(String fileUri, String pathWhereToExtract) {
		try {
			byte[] buf = new byte[1024];
			ZipInputStream zinstream = new ZipInputStream(
					new FileInputStream(fileUri));
			ZipEntry zentry = zinstream.getNextEntry();
			while (zentry != null) {
				if (zentry.isDirectory()) {
					File directory = new File(pathWhereToExtract + zentry.getName());
					if (!directory.exists())
						directory.mkdirs();
					zinstream.closeEntry();
					zentry = zinstream.getNextEntry();
				} else {
					String entryName = zentry.getName();
					FileOutputStream outstream = new FileOutputStream( pathWhereToExtract + entryName);
					int n;

					while ((n = zinstream.read(buf, 0, 1024)) > -1) {
						outstream.write(buf, 0, n);

					}
					outstream.close();
					zinstream.closeEntry();
					zentry = zinstream.getNextEntry();
				} 
			}
			zinstream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
