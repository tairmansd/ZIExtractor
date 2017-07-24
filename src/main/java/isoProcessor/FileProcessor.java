package isoProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class FileProcessor {
	
	private List<FileInfo> fileInfo = new ArrayList<FileInfo>();
	
	private boolean isRawDataNeeded = false;
	
	private File sourceFile = null;
	
	public List<FileInfo> getFileInfo() {
		return fileInfo;
	}
	
	public FileProcessor(File inputFile, boolean needRawData) throws IOException {
		
		if(!inputFile.exists() && inputFile.isFile()) {
			throw new RuntimeException ("Input files doesn't exist at path:"+inputFile.getAbsolutePath());
		}
		
		this.isRawDataNeeded 	= needRawData;
		this.setSourceFile(inputFile);
		processFile(inputFile, this.fileInfo, this.isRawDataNeeded, "");
	}
	
	private void processFile(final File inputfile, List<FileInfo> result, boolean needRawData, String basePath) throws IOException {
		
		List<FileInfo> fileList = new ArrayList<FileInfo>(); 
		
		System.out.println("Processing file : "+inputfile.getName());
		
		String inputFileExt = FilenameUtils.getExtension(inputfile.getAbsolutePath());
		
		ISOZIPExtractor extractor = ISOZIPExtractor.getInstance(inputFileExt);
		
		List<FileInfo> listFiles = extractor.extract(inputfile, needRawData, basePath);
		
		fileList.addAll(listFiles);
		
		for (FileInfo fileInfo : listFiles) {
			
			if(fileInfo.getExtension().equalsIgnoreCase("iso") || fileInfo.getExtension().equalsIgnoreCase("zip")) {
				
				String path = System.getProperty("java.io.tmpdir") + "\\" + fileInfo.getFileName();
				
				basePath = basePath + "/" + fileInfo.getFileName();
				
				File tempFile = new File(path);
				
				FileUtils.writeByteArrayToFile(tempFile, fileInfo.getRawData());
				
				processFile(tempFile, result, needRawData, basePath);
				
				tempFile.delete();

			} else {
				result.add(fileInfo);
			}
		}
	}
	
	public void unpack(File destination, boolean withDirectories) throws IOException {
		
		List<FileInfo> resultantFile = new ArrayList<FileInfo>();
		
		if(!destination.exists()) {
			throw new RuntimeException("Destination file does not exist at path:"+destination.getAbsolutePath());
		}
		
		if(!isRawDataNeeded) {
			throw new RuntimeException("Can't use unpack if need raw data flag is false.");
		}
		
		resultantFile.addAll(getFileInfo());
		
		for (FileInfo fileInfo : resultantFile) {
			
			String unpackFilePath = !withDirectories ? destination.getAbsolutePath() + "\\" + fileInfo.getFileName() :
									destination.getAbsolutePath() + fileInfo.getPath(); 
			
			System.out.println("Unpacking file: "+unpackFilePath);
			
			File streamFilePath = new File(unpackFilePath);
			
			if (!streamFilePath.exists()) {
				streamFilePath.getParentFile().mkdirs();
			}
			
			streamFilePath.createNewFile();
			
			FileOutputStream out = new FileOutputStream(streamFilePath);
			
			out.write(fileInfo.getRawData());
			
			out.close();
		}
	}
	
	public FileInfo findSingleFileInfo(String searchFileName, File inputFile, boolean isCaseInSensitive) throws IOException {
		
		List<FileInfo> resultantFile = new ArrayList<FileInfo>();
		
		if(!inputFile.exists()) {
			throw new RuntimeException("Input file doesn't exists.");
		}
		
		for (FileInfo fileInfo : resultantFile) {
			boolean isMatch = !isCaseInSensitive ? fileInfo.getFileName().equals(searchFileName) : fileInfo.getFileName().equalsIgnoreCase(searchFileName); 
			if(isMatch) {
				return fileInfo;
			}
		}
		
		return null;
	};
	
	public File findSingleFile(String searchFileName, File inputFile, boolean isCaseSensitive) throws IOException {
		
		List<FileInfo> resultantFile = new ArrayList<FileInfo>();
		
		if(!inputFile.exists()) {
			throw new RuntimeException("Input file doesn't exists.");
		}
		
		for (FileInfo fileInfo : resultantFile) {
			boolean isMatch = isCaseSensitive ? fileInfo.getFileName().equals(searchFileName) : fileInfo.getFileName().equalsIgnoreCase(searchFileName); 
			if(isMatch) {
				
				String path = System.getProperty("java.io.tmpdir") + "\\" + fileInfo.getFileName();
				
				File tempFile = new File(path);
				
				FileUtils.writeByteArrayToFile(tempFile, fileInfo.getRawData());
				
				return tempFile;
			}
		}
		
		return null;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	};
	
	public static void main(String[] args) throws IOException {
		FileProcessor fp = new FileProcessor(new File("D:\\output\\testFile.zip"), true);
		
		for (FileInfo fip : fp.getFileInfo()) {
			fip.print();
		}
		
		fp.unpack(new File("D:\\output\\test"), false);
	}
}
