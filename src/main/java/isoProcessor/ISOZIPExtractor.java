package isoProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class ISOZIPExtractor {

	abstract List<FileInfo> extract(final File inputFile, final boolean needRawData, String basePath) throws IOException;
	
	public static final ISOZIPExtractor getInstance(final String fileExtension) {
		if(fileExtension.toLowerCase().endsWith("zip")) {
			return new ZIPProcessorImpl();
		} else if(fileExtension.toLowerCase().endsWith("iso")) {
			return new ISOProcessorImpl();
		} else {
			throw new RuntimeException("UNSUPPORTED FILE EXTENSION:"+fileExtension);
		}
	}
	
	public static final Date getDateFromFileTime(FileTime time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time.toMillis());
		return calendar.getTime();
	}
}
