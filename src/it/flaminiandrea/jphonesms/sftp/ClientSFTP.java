package it.flaminiandrea.jphonesms.sftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SFTPv3Client;
import com.trilead.ssh2.SFTPv3FileHandle;


public class ClientSFTP {

	public File getFile(String user, String password, String ipAddress, String fileName, String pathToDirectory) throws IOException {
		Connection conn = new Connection(ipAddress);
		conn.connect();
		conn.authenticateWithPassword(user, password);
		SFTPv3Client sftp = new SFTPv3Client(conn);
		Long size = sftp.lstat(fileName).size;
		long fileOffset = 0;
		int destinationOffset = 0;
		final int length = 8192;
		byte[] destination = new byte[size.intValue()];
		while (true) {
			SFTPv3FileHandle handle = sftp.openFileRO(fileName);
			int bytesRead =
				sftp.read(handle, fileOffset, destination, destinationOffset, length);
			sftp.closeFile(handle);
			if (bytesRead < 0)
				break;
			fileOffset = fileOffset + length;
			destinationOffset = destinationOffset + length;
		}
		String[] splittedFileName = fileName.split("/");
		String fileSeparator = System.getProperties().getProperty("file.separator");
		File newFile= new File(pathToDirectory+fileSeparator+splittedFileName[splittedFileName.length-1]);
		FileOutputStream output = new FileOutputStream(newFile);
		output.write(destination);
		conn.close();
		return newFile;
	}	
}
