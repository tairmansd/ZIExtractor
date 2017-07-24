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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import net.didion.loopy.iso9660.ISO9660FileEntry;
import net.didion.loopy.iso9660.ISO9660FileSystem;

public class ISOProcessorImpl extends ISOZIPExtractor {

	@SuppressWarnings("unchecked")
	public List<FileInfo> extract(File inputFile, boolean needRawData, String basePath) throws IOException {
		
		List<FileInfo> result = new ArrayList<FileInfo>();
		
		ISO9660FileSystem sys = new ISO9660FileSystem(inputFile, true);
		
		Enumeration<ISO9660FileEntry> es = sys.getEntries();
		while (es.hasMoreElements()) {
			
			net.didion.loopy.iso9660.ISO9660FileEntry entry = (ISO9660FileEntry) es.nextElement();
			
			if(entry.isDirectory() || entry.getName() == ".") continue;
			
			InputStream stream = sys.getInputStream(entry);
			
			byte[] byteArr = needRawData ? IOUtils.toByteArray(stream) : null;
			
			FileInfo fileinfo = createFileInfo(entry, byteArr, inputFile.getName());
			
			fileinfo.setPath(basePath+"\\"+fileinfo.getPath());
			
			result.add(fileinfo);
			
			stream.close();
		}
		
		sys.close();
		
		return result;
	}
	
	private FileInfo createFileInfo(ISO9660FileEntry entry, byte[] arrdata, String inputFileName) {
		FileInfo info = new FileInfo();
		
		Date createdTime = null;
		
		Date lastModifiedTime = new Date(entry.getLastModifiedTime()); 
		
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
