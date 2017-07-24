package isoProcessor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class ZIPProcessorImpl extends ISOZIPExtractor {

	public List<FileInfo> extract(File inputFile, boolean needRawData, String basePath) throws IOException {
		
		List<FileInfo> result = new ArrayList<FileInfo>();
		
		ZipFile zipFile = new ZipFile(inputFile.getAbsolutePath());

	    Enumeration<? extends ZipEntry> entries = zipFile.entries();

	    while(entries.hasMoreElements()){
	    	
	        ZipEntry entry = entries.nextElement();
	        
	        if(entry.isDirectory()) continue;
	        
	        InputStream stream = zipFile.getInputStream(entry);
	        
	        byte[] byteArr = needRawData ? IOUtils.toByteArray(stream) : null;
	        
	        FileInfo fileinfo = createFileInfo(entry, byteArr, inputFile.getName());
	        
	        fileinfo.setPath(basePath+"/"+fileinfo.getPath());
	        
	        result.add(fileinfo);
	        
	        stream.close();
	    }
	    
	    zipFile.close();
	    
		return result;
	}

	private FileInfo createFileInfo(ZipEntry entry, byte[] arrdata, String inputFileName) {
		FileInfo info = new FileInfo();
		
		Date createdTime = null;
		if(entry.getCreationTime() != null) {
			createdTime = ISOZIPExtractor.getDateFromFileTime(entry.getCreationTime());
		}
		
		Date lastModifiedTime = null; 
		if(entry.getLastModifiedTime() != null) {
			lastModifiedTime = ISOZIPExtractor.getDateFromFileTime(entry.getLastModifiedTime());
		} 
		
		info.setCreatedOn(createdTime);
		info.setExtension(FilenameUtils.getExtension(entry.getName()));
		
		Path path = Paths.get(entry.getName());
		info.setFileName(path.getFileName().toString());
		
		info.setLastModified(lastModifiedTime);
		info.setRawData(arrdata);
		info.setExtractedFrom(inputFileName);		
		info.setPath(entry.getName());
		info.setSize(entry.getSize());
		
		return info;
	}

}
