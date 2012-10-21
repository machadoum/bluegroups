package bluegroups.filemanager.deprecated;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import bluegroups.filemanager.Diretorio;

/**
 *@deprecated 
 * @author Rafael
 */
public class LocalRepository {

    private String id;
    private String name;
    private Diretorio diretorio;
    private String historyFile;

    public LocalRepository(Diretorio repDir, String name, Diretorio workDir) {
        diretorio = repDir;
        this.name = name;
        id = createId(); //Hash
        //createControlFile(); //Arquivo contendo informações sobre o repositorio
        new WorkSpace(workDir);
    }
	
    private String createId() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}