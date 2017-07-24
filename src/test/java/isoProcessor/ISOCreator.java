package isoProcessor;

import java.io.File;

import de.tu_darmstadt.informatik.rbg.hatlak.eltorito.impl.ElToritoConfig;
import de.tu_darmstadt.informatik.rbg.hatlak.iso9660.ISO9660File;
import de.tu_darmstadt.informatik.rbg.hatlak.iso9660.ISO9660RootDirectory;
import de.tu_darmstadt.informatik.rbg.hatlak.iso9660.impl.CreateISO;
import de.tu_darmstadt.informatik.rbg.hatlak.iso9660.impl.ISO9660Config;
import de.tu_darmstadt.informatik.rbg.hatlak.iso9660.impl.ISOImageFileHandler;
import de.tu_darmstadt.informatik.rbg.hatlak.joliet.impl.JolietConfig;
import de.tu_darmstadt.informatik.rbg.hatlak.rockridge.impl.RockRidgeConfig;

public class ISOCreator {

    public static void main(String[] args) throws Exception {

        System.out.println("Start");

        // Output file
        File outfile = new File(args.length>0 ? args[0] : "ISOTest.iso");

        // Directory hierarchy, starting from the root
        ISO9660RootDirectory.MOVED_DIRECTORIES_STORE_NAME = "rr_moved";
        ISO9660RootDirectory root = new ISO9660RootDirectory();

        // Files with different versions
        // (to appear in descending order, pointing to same LSN)
        File inputFolder = new File("D:\\output");
        
        for (File file : inputFolder.listFiles()) {
        	if(file.isFile()) {
        		ISO9660File isoFile = new ISO9660File(file);
        		root.addFile(isoFile);        		
        	}
		}
        
        // ISO9660 support
        System.out.println("ISO9660 support");
        ISO9660Config iso9660Config = new ISO9660Config();
        iso9660Config.allowASCII(false);
        iso9660Config.setInterchangeLevel(1);
        iso9660Config.restrictDirDepthTo8(true);
        iso9660Config.setPublisher("Name Nickname");
        iso9660Config.setVolumeID("ISO Test Jiic");
        iso9660Config.setDataPreparer("Name Nickname");

        //iso9660Config.setCopyrightFile(new File("Copyright.txt"));
        iso9660Config.forceDotDelimiter(true);

        RockRidgeConfig rrConfig = null;
        ElToritoConfig elToritoConfig = null;

        JolietConfig jolietConfig = null;

        // Joliet support
        System.out.println("Joliet support");
        jolietConfig = new JolietConfig();
        jolietConfig.setPublisher("Test 2");
        jolietConfig.setVolumeID("Joliet Test");
        jolietConfig.setDataPreparer("Jens Hatlak");
        //jolietConfig.setCopyrightFile(new File("Copyright.txt"));
        jolietConfig.forceDotDelimiter(true);


        System.out.println("Create ISO");
        ISOImageFileHandler streamHandler = new ISOImageFileHandler(outfile);
        System.out.println("streamHandler");
        CreateISO iso = new CreateISO(streamHandler, root);
        System.out.println("iso");
        iso.process(iso9660Config, rrConfig, jolietConfig, elToritoConfig);
        System.out.println("process");
        System.out.println("Done. File is: " + outfile);
    }
}