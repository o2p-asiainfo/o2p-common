package com.ailk.eaap.o2p.util.file.local;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class FileSortUtil {
	private FileSortUtil(){}
	
	// 按照文件从大到小排序
	public static File[] orderByLengthDesc(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				long diff = f2.length() - f1.length();
				if (diff > 0){
					return 1;					
				}
				else if (diff == 0){
					return 0;
				}
				else{
					return -1;
				}
					
			}
		});
		return files;
	}
	
	// 按照文件从小到大排序
	public static File[] orderByLengthAsc(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				long diff = f1.length() - f2.length();
				if (diff > 0)
					return 1;
				else if (diff == 0)
					return 0;
				else
					return -1;
			}
		});
		return files;
	}

	// 按照文件名称排序(z-aZ-A9-0)
	public static File[] orderByNameDesc(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				if (o1.isDirectory() && o2.isFile()){
					return -1;
				}
				if (o1.isFile() && o2.isDirectory()){
					return 1;
				}
				return o2.getName().compareTo(o1.getName());
			}
		});
		return files;
	}
	
	// 按照文件名称排序(0-9A-Za-z)	
	public static File[] orderByNameAsc(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				if (o1.isDirectory() && o2.isFile()){
					return -1;
				}
				if (o1.isFile() && o2.isDirectory()){
					return 1;
				}
				return o1.getName().compareTo(o2.getName());
			}
		});
		return files;
	}

	// 按日期降序排序
	public static File[] orderByDateDesc(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				long diff = f2.lastModified() - f1.lastModified();
				if (diff > 0){
					return 1;
				}
				else if (diff == 0){
					return 0;
				}
				else{
					return -1;
				}
			}
		});
		return files;
	}
	
	// 按日期升序排序
	public static File[] orderByDateAsc(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				long diff = f1.lastModified() - f2.lastModified();
				if (diff > 0){
					return 1;
				}
				else if (diff == 0){
					return 0;
				}
				else
					return -1;
			}

		});
		return files;
	}
		
	// 按文件类型升序排序
	public static File[] orderByFileTypeAsc(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				if (f1.isDirectory() && f2.isFile())
					return -1;
				if (f1.isFile() && f2.isDirectory())
					return 1;
				String suffix1 = "";
				String suffix2 = "";
				int flag = f1.getName().lastIndexOf(".");
				if(flag != -1) {
					suffix1 = f1.getName().substring(flag+1,f1.getName().length());	//获取后缀名
				}
				flag = f2.getName().lastIndexOf(".");
				if(flag != -1) {
					suffix2 = f2.getName().substring(flag+1,f2.getName().length());	//获取后缀名
				}
				return suffix1.compareTo(suffix2);
			}
		});
		return files;
	}

	// 按文件类型降序排序
	public static File[] orderByFileTypeDesc(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				if (f1.isDirectory() && f2.isFile())
					return -1;
				if (f1.isFile() && f2.isDirectory())
					return 1;
				String suffix1 = "";
				String suffix2 = "";
				int flag = f1.getName().lastIndexOf(".");
				if(flag != -1) {
					suffix1 = f1.getName().substring(flag+1,f1.getName().length());	//获取后缀名
				}
				flag = f2.getName().lastIndexOf(".");
				if(flag != -1) {
					suffix2 = f2.getName().substring(flag+1,f2.getName().length());	//获取后缀名
				}
				return suffix2.compareTo(suffix1);
			}
		});
		return files;
	}
}
