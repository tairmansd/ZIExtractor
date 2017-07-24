package isoProcessor;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class FileProcessorTest {
	
	private static final String TEST_FILES_FOLDER = new String(System.getProperty("user.dir")+"\\src\\test\\resources\\");
	
	private static final String BASE_OUTPUT_PATH = new String("D:\\output\\test");
	
	@Before
	public void before() throws IOException {
		File outputDirectory = new File(BASE_OUTPUT_PATH);
		
		if(outputDirectory.exists()) {
			FileUtils.deleteDirectory(outputDirectory);			
		}
	}
	
	@Test
	public void testUnpackOnNestedZip() throws IOException {
		File inputFile = getTestFile("testFileNestedFolders.zip");
		FileProcessor processor = new FileProcessor(inputFile, true);
		
		File outputDirectory = new File(BASE_OUTPUT_PATH);
		
		outputDirectory.mkdirs();
		
		processor.unpack(outputDirectory, false);
				
		//checking nested folder
		File checkFile = new File(BASE_OUTPUT_PATH+"\\Saved Pictures\\more\\Capture.png");
		
		assertTrue(checkFile.exists());
		
		File checkFile2 = new File(BASE_OUTPUT_PATH+"\\Saved Pictures\\asd.jpg");
		
		assertTrue(checkFile2.exists());
		
		FileUtils.deleteDirectory(outputDirectory);
	}
	
	@Test
	public void testUnpackOnNestedISO() throws IOException {
		File inputFile = getTestFile("ISOTest6.iso");
		FileProcessor processor = new FileProcessor(inputFile, true);
		
		File outputDirectory = new File(BASE_OUTPUT_PATH);
		
		outputDirectory.mkdirs();
		
		processor.unpack(outputDirectory, false);
				
		//checking nested folder
		File checkFile = new File(BASE_OUTPUT_PATH+"\\asd.jpg");
		
		assertTrue(checkFile.exists());
		
		File checkFile2 = new File(BASE_OUTPUT_PATH+"\\noImageFound.png");
		
		assertTrue(checkFile2.exists());
		
		FileUtils.deleteDirectory(outputDirectory);
	}
	
	@Test
	public void testFindFileInfoZIP() throws IOException {
		File inputFile = getTestFile("testFileNestedFolders.zip");
		FileProcessor processor = new FileProcessor(inputFile, true);
		
		FileInfo info = processor.findSingleFileInfo("capture.png", inputFile, false);
				
		//case sensitive search
		assertTrue(info == null);
		
		info = processor.findSingleFileInfo("Capture.PNG", inputFile, false);
		
		assertTrue(info != null);
		
		//insensitive search
		info = processor.findSingleFileInfo("CapTUre.PNG", inputFile, true);
		
		assertTrue(info != null);
		
		//search failure case 
		
		info = processor.findSingleFileInfo("CapTUre.jpG", inputFile, true);
		
		assertTrue(info == null);		
	}
	
	@Test
	public void testFindFileInfoISO() throws IOException {
		File inputFile = getTestFile("ISOTest6.iso");
		FileProcessor processor = new FileProcessor(inputFile, true);
		
		FileInfo info = processor.findSingleFileInfo("NOIMAGEFOUND.png", inputFile, false);
				
		//case sensitive search
		assertTrue(info == null);
		
		info = processor.findSingleFileInfo("noImageFound.png", inputFile, false);
		
		assertTrue(info != null);
		
		//insensitive search
		info = processor.findSingleFileInfo("nOImAgeFouNd.pNg", inputFile, true);
		
		assertTrue(info != null);
		
		//search failure case file doesn't exists
		info = processor.findSingleFileInfo("noImageFou.png", inputFile, true);
		
		assertTrue(info == null);		
	}
	
	private static final File getTestFile(String fileName) {
		File file = new File(TEST_FILES_FOLDER+fileName);
		
		if(!file.exists()) {
			throw new RuntimeException("TEST file :"+fileName+" doesn't exist at path:"+file.getAbsolutePath());
		}
		
		return file;
	}
}
