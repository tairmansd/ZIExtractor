package isoProcessor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;

import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import net.didion.loopy.iso9660.ISO9660FileEntry;
import net.didion.loopy.iso9660.ISO9660FileSystem;

public class ISOExtractor {
	public static void main(String[] args) throws HandlerException, IOException {

		String pathname = new String("D:\\projects\\braille\\isoProcessor\\src\\main\\resources");

		ISO9660FileSystem sys = new ISO9660FileSystem(new File(pathname, "ISOTest6.iso"), true);

		Enumeration<ISO9660FileEntry> es = sys.getEntries();
		while (es.hasMoreElements()) {
			net.didion.loopy.iso9660.ISO9660FileEntry entry = (ISO9660FileEntry) es.nextElement(); 
			
			if(entry.getName() == ".") {
				continue;
			}
			
			InputStream stream = sys.getInputStream(entry);
			
			byte[] byteArr = IOUtils.toByteArray(stream);
			
			File outFile = new File("D:\\output\\"+entry.getName());
			
			FileOutputStream outStream = new FileOutputStream(outFile);
			
			outStream.write(byteArr);
			
			outStream.close();
			
			stream.close();
		}
		
		sys.close();
	}
}
