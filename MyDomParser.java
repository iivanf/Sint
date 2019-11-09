
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.SAXException;

public class MyDomParser{
    public static void main(String[] args) {
        String url1= "http://gssi.det.uvigo.es/users/agil/public_html/SINT/19-20/tvml-2004-12-01.xml";
        String url= "http://gssi.det.uvigo.es/users/agil/public_html/SINT/19-20/";
        HashMap<String,Document> mapDocs = new HashMap<String, Document>();
        String dia = "2004-12-01";
        String documento;
        
        ArrayList<String> fechas = new ArrayList<>();
        ArrayList<Canal> canales = new ArrayList<>();
        ArrayList<Programa> programas = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc=builder.parse(new URL (url1).openStream());
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            String exp = "//TVML";
            NodeList documents = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
            //METO NO MAP O 1º DOCUMENTO PARA QUE NN SE ME REPITA
            mapDocs.put(dia, doc);

            //creo o iterator e vouno avanzando para mierar todos os documentos
            //Iterator it = mapDocs.entrySet().iterator();
            //while (it.hasNext()) {            
             //meto todos os documentos que leo neste
                for(int i=0; i<documents.getLength(); i++){
                    documento = documents.item(i).getTextContent();
                    dia = documento.substring(documento.indexOf("-")+1,documento.length()-4);
                    if(!mapDocs.containsKey(dia)){
                        System.out.println(dia);
                        //doc = builder.parse(new URL (url+documento).openStream());
                        mapDocs.put(dia, doc);
                        //it = mapDocs.entrySet().iterator();
                    }        
                }         
           // }
        //getC1fechas();
           exp="//Fecha";
           NodeList fecha = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
           for(int i=0; i<fecha.getLength();i++){
               fechas.add(fecha.item(i).getTextContent());
           }
           System.out.println(fechas);
           
        //getc1Canales(pdia)
        String pdia ="2004/12/01";
        exp = "//Canal/@lang";
        NodeList listalangCanal = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        exp = "//Canal/@idCanal";
        NodeList listaidCanal =(NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        exp = "//Canal/NombreCanal";
        NodeList listaCanal =(NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        exp = "//Canal/Grupo";
        NodeList listaGrupo =(NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        for(int i=0; i<listaCanal.getLength();i++){
            Canal canalAux = new Canal();
            canalAux.setlang(listalangCanal.item(i).getTextContent());
            canalAux.setidCanal(listaidCanal.item(i).getTextContent());
            canalAux.setNombreCanal(listaCanal.item(i).getTextContent());
            canalAux.setGrupo(listaGrupo.item(i).getTextContent());
            canales.add(canalAux);
        }
        for (int i=0; i<canales.size(); i++){
            System.out.println(canales.get(i).getidCanal() +" "+ canales.get(i).getlang() +" "+ canales.get(i).getNombreCanal() +" "+ canales.get(i).getGrupo() );
        
        }


        //getC1Programas
        String canal="T5";
        exp ="//Programa[../NombreCanal='"+canal+"']/@edadminima";
        NodeList listaedadminima = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        exp ="//Programa[../NombreCanal='"+canal+"']/@langs";
        NodeList listalangs = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        exp ="//Programa[../NombreCanal='"+canal+"']/NombrePrograma";
        NodeList listaNombrePrograma = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        exp ="//Programa[../NombreCanal='"+canal+"']/Categoria";
        NodeList listaCategoria = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        exp ="//Programa[../NombreCanal='"+canal+"']/HoraInicio";
        NodeList listaHoraInicio = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        exp ="//Programa[../NombreCanal='"+canal+"']/HoraFin";
        NodeList listaHoraFin = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        exp ="//Programa[../NombreCanal='"+canal+"']/Duracion";
        NodeList listaDuracion = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
        for(int i=0; i<listaedadminima.getLength();i++){
            Programa programaAux = new Programa();
            programaAux.setedadminima(listaedadminima.item(i).getTextContent());
            programaAux.setlangs(listalangs.item(i).getTextContent());
            programaAux.setNombrePrograma(listaNombrePrograma.item(i).getTextContent());
            programaAux.setlCategoria(listaCategoria.item(i).getTextContent());
            programaAux.seetHoraInicio(listaHoraInicio.item(i).getTextContent());
            /*if(listaHoraFin.item(i).getTextContent()!=null){
                programaAux.setHoraFin(listaHoraFin.item(i).getTextContent());
            }
            if(listaDuracion.item(i).getTextContent()!=null){
                programaAux.setDuracion(listaDuracion.item(i).getTextContent());
            }*/
            programas.add(programaAux);
        }
        for(int i=0; i<programas.size();i++){
            System.out.println(programas.get(i).getlangs());
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
