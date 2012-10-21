package bluegroups.filemanager.deprecated;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.io.file.FileConnection;
import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParser;

import bluegroups.filemanager.BGFile;
import bluegroups.filemanager.Diretorio;

/**
 * @deprecated 
 * @author Rafael
 */
public class ReadOnlyRepository {

    private String id;
    private String name;
    private Diretorio diretorio;
    private String revision;
    private String historyFile;
    private BGFile repositoryMetaData;

    public ReadOnlyRepository(Diretorio repDir, String name) {
        diretorio = repDir;
        this.name = name;
        id = "1"; //createId(); //Hash
        repositoryMetaData = createControlFile(); //Arquivo contendo informações sobre o repositorio
    }
	
    private String createId() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private BGFile createControlFile() {
//        Arquivo arquivo = new Arquivo(getDir().getAdress(), getName()+".xml");
//        try {            
//            arquivo.create();
//            arquivo.openReadWrite();            
//            OutputStream os = arquivo.getFile().openOutputStream();
//            Document doc = new Document();
//            
//            Element root = doc.createElement("", "parent");
//            root.setName("Repository");
//
//            Element nomeNode = doc.createElement("", "child");
//            nomeNode.setName("Nome");
//            nomeNode.addChild(0, Node.TEXT, this.getName());
//            //novel.setAttribute(null, "id", "101");
//
//            Element versionNode = doc.createElement("", "child");
//            versionNode.setName("Version");
//            versionNode.addChild(0, Node.TEXT, this.getRevision());
//
//            novel.addChild(0, Node.ELEMENT, novelName1);
//
//            Element novelName2 = doc.createElement("", "child");
//            novelName2.setName("NOVEL_NAME");
//            novelName2.addChild(0, Node.TEXT, "A Thousand Splendid Suns");
//
//            novel.addChild(1, Node.ELEMENT, novelName2);
//
//            Element eduBook = doc.createElement("", "child");
//            eduBook.setName("EDUCATIONAL_BOOK");
//            eduBook.setAttribute(null, "id", "102");
//
//            Element eduBook1 = doc.createElement("", "child");
//            eduBook1.setName("BOOK_NAME");
//            eduBook1.addChild(0, Node.TEXT, "Let us c");
//
//            eduBook.addChild(0, Node.ELEMENT, eduBook1);
//
//            Element eduBook2 = doc.createElement("", "child");
//            eduBook2.setName("BOOK_NAME");
//            eduBook2.addChild(0, Node.TEXT, "JAVA");
//
//            eduBook.addChild(0, Node.ELEMENT, eduBook2);		
//
//            root.addChild(0, Node.ELEMENT, novel);
//            root.addChild(1, Node.ELEMENT, eduBook);
//
//            doc.addChild(0, Node.ELEMENT, root);
//
//            KXmlSerializer serializer = new KXmlSerializer();            
//            serializer.setOutput(os, "UTF-8");
//            doc.write(serializer);            
//            os.close();
//            arquivo.close();
//        }
//        catch (IOException e) {
//            System.out.println("Erro IO: "+e.getMessage());
//        }
//        catch (Exception e) {
//            System.out.println("Erro: "+e.getMessage());
//        }
        return null;
    }
    
    public String getName(){
        return this.name;
    }
    
    public Diretorio getDir(){
        return diretorio;
    }

//    private Object getRevision() {
//        if (this.v)
//    }
    
}