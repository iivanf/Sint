
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.SAXException;

public class MyDomParser{
    public static void main(String[] args) {
        String url1= "http://gssi.det.uvigo.es/users/agil/public_html/SINT/19-20/tvml-2004-12-01.xml";
        String url= "http://gssi.det.uvigo.es/users/agil/public_html/SINT/19-20/";
        HashMap<String,Document> mapDocs = new HashMap<String, Document>();
        String fecha = "2004-12-01";
        String documento;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc=builder.parse(new URL (url1).openStream());
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            String exp = "//TVML";
            NodeList documents = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
            //METO NO MAP O 1º DOCUMENTO PARA QUE NN SE ME REPITA
            mapDocs.put(fecha, doc);
            //meto todos os documentos que leo
            for(int i=0; i<documents.getLength(); i++){
                documento = documents.item(i).getTextContent();
                fecha = documento.substring(documento.indexOf("-")+1,documento.length()-4);
                if(!mapDocs.containsKey(fecha)){
                    //System.out.println(fecha);
                    mapDocs.put(fecha, doc);
                }        
            }
            Iterator it = mapDocs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry)it.next();
                System.out.println(e.getKey() + " " + e.getValue());
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch(SAXException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }catch(XPathException e){
            e.printStackTrace();
        }
        
    }
}