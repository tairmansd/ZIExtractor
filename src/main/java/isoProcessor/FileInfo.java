package isoProcessor;

import java.util.Date;

public class FileInfo {

	private String fileName;
	private String extension;
	private long size;
	private Date createdOn;
	private Date lastModified;
	private byte[] rawData;
	private String extractedFrom;
	private String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExtractedFrom() {
		return extractedFrom;
	}

	public void setExtractedFrom(String extractedFrom) {
		this.extractedFrom = extractedFrom;
	}

	public byte[] getRawData() {
		return rawData;
	}

	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long l) {
		this.size = l;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}
	
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public void print() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("**************************").append("\r\n").
		append("Filename : ").append(this.fileName).append("\r\n").
		append("Extracted From : ").append(this.extractedFrom).append("\r\n").
		append("Path : ").append(this.path).append("\r\n").
		append("Extension : ").append(this.extension).append("\r\n").
		append("Size : ").append(this.size).append("\r\n").
		append("Created On : ").append(this.createdOn).append("\r\n").
		append("Lastmodified : ").append(this.lastModified).append("\r\n").
		append("rawdata length : ").append(this.rawData.toString()).append("\r\n").
		append("**************************").append("\r\n");
		
		System.out.println(buffer.toString());
	}
	
	@Override
	public String toString() {
		return this.path;
	}
}
