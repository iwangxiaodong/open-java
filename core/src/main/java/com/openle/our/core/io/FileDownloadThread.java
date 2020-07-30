package com.openle.our.core.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloadThread extends Thread {
	private static final int BUFFER_SIZE = 1024;
	private URL url;
	private File file;
	private int startPosition;
	private int endPosition;
	private int curPosition;
	private boolean finished = false;

	public boolean isFinished() {
		return finished;
	}

	private int downloadSize = 0;

	// 分块构造函数
	public FileDownloadThread(URL url, File file, int startPosition,
			int endPosition, boolean finished) {
		this.url = url;
		this.file = file;
		this.startPosition = startPosition;
		this.curPosition = startPosition;
		this.endPosition = endPosition;
		this.finished = finished;
	}

	public void run() {
		if (this.finished == false) { // 文件未下载完成，获取到当前指针位置，继续下载。

			BufferedInputStream bis = null;
			RandomAccessFile fos = null;
			byte[] buf = new byte[BUFFER_SIZE];
			URLConnection con = null;
			try {
				// 打开URL连接
				con = url.openConnection();
				con.setAllowUserInteraction(true);

				// // 判断是否该文件存在，如果存在且下载完成，直接返回。
				// if ((file.length() + startPosition) == endPosition) {
				// this.finished = true;
				// }

				System.out.println(getName() + ":" + startPosition + "|"
						+ endPosition);

				con.setRequestProperty("Range", "bytes=" + startPosition + "-"
						+ endPosition);
				fos = new RandomAccessFile(file, "rw");
				fos.seek(file.length());
				bis = new BufferedInputStream(con.getInputStream());
				while (curPosition < endPosition) {
					int len = bis.read(buf, 0, BUFFER_SIZE);
					if (len == -1) {
						break;
					}
					fos.write(buf, 0, len);
					curPosition = curPosition + len;
					if (curPosition > endPosition) {
						downloadSize += len - (curPosition - endPosition) + 1;
					} else {
						downloadSize += len;
					}
				}
				this.finished = true;
				bis.close();
				fos.close();

			} catch (IOException e) {
				System.out.println(getName() + " Error:" + e.getMessage());
			}
		}
	}

	public int getDownloadSize() {
		return downloadSize;
	}
}