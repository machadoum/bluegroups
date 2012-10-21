/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 
package bluegroups.filemanager.deprecated;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParser;

import bluegroups.filemanager.BGFile;
import bluegroups.filemanager.Diretorio;

*//**
 *
 * @author Rafael
 *//*
public class DistributedRepository {

    private FileConnection fileXml;
    private BGFile repositoryXML;
    private String id;
    private String name;
    private Diretorio dir;
    private String revisionTimeStamp;
    private Vector arquivos;

    DistributedRepository() {
        arquivos = new Vector();
    }

    public void update(FileConnection file) throws Exception {
        InputStream is = file.openInputStream();
        KXmlParser xmlParser = new KXmlParser();

        xmlParser.setInput(new InputStreamReader(is));
        xmlParser.nextTag();
        xmlParser.require(XmlPullParser.START_TAG, null, "repository");
        System.out.println("id: "+this.id);
        System.out.println("nome: "+this.name);
        if (!(this.id.equals(xmlParser.getAttributeValue(0))) || !(this.name.equals(xmlParser.getAttributeValue(1)))) {
            System.out.println("Arquivo passado não é do mesmo repositório.");
        }
        xmlParser.nextTag();
        xmlParser.require(XmlPullParser.START_TAG, null, "revision");

        if (!(this.revisionTimeStamp.equals(xmlParser.getAttributeValue(0)))) {
            String aux, aux2;
            BGFile auxFile;
            System.out.println("Versão desatualizada, verificando arquivos...");
            Vector outdated = new Vector();
            while (xmlParser.nextTag() == XmlPullParser.START_TAG) {
                xmlParser.require(XmlPullParser.START_TAG, null, "file");
                aux2 = xmlParser.getAttributeValue(0);
                xmlParser.nextTag();
                xmlParser.require(XmlPullParser.START_TAG, null, "nome");
                aux = xmlParser.nextText();
                auxFile = new BGFile("", aux, false);
                auxFile.setRevision(aux2);
                xmlParser.nextTag();
                xmlParser.require(XmlPullParser.START_TAG, null, "id");
                aux = xmlParser.nextText();
                auxFile.setId(aux);
                xmlParser.nextTag();
                xmlParser.require(XmlPullParser.END_TAG, null, "file");

                int index = arquivos.indexOf(auxFile);
                System.out.println("Indx: "+index);
//                Arquivo x = (Arquivo)arquivos.elementAt(index);
//                if ( ! x.getRevision().equals(auxFile.getRevision())) {
//                    outdated.addElement(auxFile);
//                }
            }
            for(int i=0; i<outdated.size(); i++) {
                System.out.println("Out: "+outdated.elementAt(i));
            }
        } else {
            System.out.println("Versão ok.");
        }

    }

    public void checkout(Diretorio dir, FileConnection file) throws Exception {
        InputStream is = file.openInputStream();
        this.dir = dir;
        KXmlParser xmlParser = new KXmlParser();
        String aux, aux2;
        BGFile auxFile;
        
        xmlParser.setInput(new InputStreamReader(is));
//        Document doc = new Document();
//        doc.parse(xmlParser);
        
        xmlParser.nextTag();
        xmlParser.require(XmlPullParser.START_TAG, null, "repository");
        this.id = xmlParser.getAttributeValue(0);
        this.name = xmlParser.getAttributeValue(1);

        xmlParser.nextTag();
        xmlParser.require(XmlPullParser.START_TAG, null, "revision");
        
        this.revisionTimeStamp = xmlParser.getAttributeValue(0);

        
         * <repository id=xxxx name=xxxx>
         * <revision id=yyyy>
            <file revision="">
            <nome></nome>
            <id></id>
            </file>
         * </revision>
         * </repository>
         

        while (xmlParser.nextTag() == XmlPullParser.START_TAG) {
            xmlParser.require(XmlPullParser.START_TAG, null, "file");
            aux2 = xmlParser.getAttributeValue(0);
            xmlParser.nextTag();
            xmlParser.require(XmlPullParser.START_TAG, null, "nome");
            aux = xmlParser.nextText();
            auxFile = new BGFile("", aux, false);
            auxFile.setRevision(aux2);
            xmlParser.nextTag();
            xmlParser.require(XmlPullParser.START_TAG, null, "id");            
            aux = xmlParser.nextText();
            auxFile.setId(aux);
            xmlParser.nextTag();
            xmlParser.require(XmlPullParser.END_TAG, null, "file");

            arquivos.addElement(auxFile);
        }
        is.close();
        
        this.repositoryXML = new BGFile(dir.getAddress(), this.name + ".xml", false);
        this.repositoryXML.create();
        //this.fileXml = this.repositoryXML.getFile();  
        this.fileXml = (FileConnection)Connector.open("file:///root1/"+dir.getAddress()+"/"+this.name + ".xml", Connector.READ_WRITE);
        this.repositoryXML.copy(file, fileXml);
        //Arquivo xml = new Arquivo(name, );

    }
}
*/