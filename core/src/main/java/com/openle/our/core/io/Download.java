/* example:

		Download mt = new Download(
				"http://down.sandai.net/mini/ThunderMini1.5.3.288.exe",
				"d:\\ThunderMini1.5.3.288.exe");
		// mt.setFileSize(3457008);//未设置取getContentLength值
		mt.setThreadNum(5);
		mt.start(); 
 */

package com.openle.our.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

public class Download extends Thread {
	// 定义的一些常量变量，看名字就知道什么意思了
	private static final int BUFFER_SIZE = 1024;
	private int blockSize;
	private int threadNum = 5;
	private int fileSize = -1;// 默认值为-1是为了防止当未设置文件长度时,避免0字节的同名空文件存在

	private int downloadedSize;
	String urlStr, threadNo, pathFileName;
	private int downloadPercent = 0, downloadSpeed = 0, usedTime = 0;
	private long startTime, curTime;
	private boolean completed = false;

	// 默认5个线程，覆盖下载。
	public Download(String URL, String pathFileName) {
		this.urlStr = URL;
		this.pathFileName = pathFileName;
	}

	@Override
	public void run() {

		File f = new File(pathFileName);

		FileDownloadThread[] fds = new FileDownloadThread[threadNum];
		try {

			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();

			if (fileSize == -1) {
				fileSize = conn.getContentLength();
			}

			if (!f.exists() && f.length() != fileSize) {

				System.out.println("getContentLength:" + fileSize);
				blockSize = fileSize / threadNum;
				File file[] = new File[threadNum];
				// 根据默认的线程数，或者自己修改设置的线程数来分块，创建分块后的文件块
				for (int i = 0; i < threadNum; i++) {
					file[i] = new File(pathFileName + ".part"
							+ String.valueOf(i));

					int avgLength = i * blockSize;
					int endLength = ((i + 1) * blockSize);
					boolean isFinish = false;

					int startLength = 0;
					startLength = avgLength;
					endLength = endLength - 1;

					if ((i + 1) == threadNum) {
						endLength = fileSize;
					}

					if (file[i].exists()) {
						int existsLength = (int) (file[i].length());

						if ((i + 1) == threadNum) {
							blockSize = endLength - startLength;
						}
						System.out.println("t" + i + " " + blockSize + "="
								+ existsLength);
						if (blockSize == existsLength) {
							isFinish = true;
						} else {
							startLength = startLength + existsLength;
						}

					}

					// 将分块的文件交给每个线程处理，最后一块应该大于等于平均块，因为可能有余数
					FileDownloadThread fdt = new FileDownloadThread(url,
							file[i], startLength, endLength, isFinish);
					fdt.setName("Thread" + i);
					fdt.start();
					fds[i] = fdt;

				}
				startTime = System.currentTimeMillis();
				// 获取起始下载的时间，用次来计算速度。
				boolean finished = false;
				while (!finished) {
					downloadedSize = 0;
					finished = true;
					for (int i = 0; i < fds.length; i++) {
						downloadedSize += fds[i].getDownloadSize();
						if (!fds[i].isFinished()) {
							finished = false;
						}
					}
					// 计算下载的百分比
					downloadPercent = (downloadedSize * 100) / fileSize;
					// 获取当前时间，计算平均下载速度
					curTime = System.currentTimeMillis();
					usedTime = (int) ((curTime - startTime) / 1000);
					if (usedTime == 0)
						usedTime = 1;
					downloadSpeed = (downloadedSize / usedTime) / 1024;
					sleep(1000);
				}
				// 这个是分块下载完成的标志
				completed = true;
				// 进行模块整合
				RandomAccessFile raf = new RandomAccessFile(pathFileName
						+ ".full", "rw");
				byte[] tempbytes = new byte[BUFFER_SIZE];
				InputStream in = null;
				int byteread = 0;
				for (int i = 0; i < threadNum; i++) {
					in = new FileInputStream(file[i]);
					while ((byteread = in.read(tempbytes)) != -1) {
						raf.write(tempbytes, 0, byteread);
					}

					in.close();
					// file[i].delete(); // 每次整合完一块就删除一块。
				}
				raf.close();
				System.out.println("Merge done!");
				File fullFile = new File(pathFileName + ".full");
				fullFile.renameTo(new File(pathFileName));
				System.out.println("Remove .full ext Name!");
				// 删除分块
				for (int i = 0; i < threadNum; i++) {
					file[i].delete();
				}

				System.out.println("Done!");
			} else {
				System.out.println("File exists and correct.");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	// 获取下载百分比
	public int getDownloadPercent() {
		return this.downloadPercent;
	}

	// 获取下载速度
	public int getDownloadSpeed() {
		return this.downloadSpeed;
	}

	// 修改默认线程数
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	// 分块下载完成的标志
	public boolean isCompleted() {
		return this.completed;
	}
}