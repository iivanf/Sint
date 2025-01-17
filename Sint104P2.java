package p2;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Sint104P2 extends HttpServlet {

  String url1 = "/Users/sint/tvml-2004-12-01.xml";
  String url = "/Users/sint/"
  String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
  String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";


  HashMap<String, Document> mapDocs = new HashMap<String, Document>();
  ArrayList<String> listaXMLs = new ArrayList<String>();
  ArrayList<String> listaErrores = new ArrayList<String>();

  String URLSchema = null;

  public void init(ServletConfig config) throws ServletException {

    super.init(config);
    
    try {
      listaXMLs.add(url1);
      URLSchema= config.getServletContext().getRealPath("/p2/tvml.xsd");

      for (int i = 0; i < listaXMLs.size(); i++) {
        leerXML(listaXMLs.get(i));
      }
      listaXMLs.sort(null);
    } catch (IOException e) {
      System.out.println("Fallo na lectura dos arquivos");
    }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

    // parametros que recibo
    String pwd = req.getParameter("p"); // password
    String pfase = req.getParameter("pfase"); // tipo de consulta
    if (pfase == null) {
      pfase = "01";
    }
    if (!(this.checkPwd(req, res)))
      return;

    switch (pfase) {
    case "01": // pantalla inicial
      this.doGetFase01(req, res);
      break;

    case "02": // lista ficheros erroneos
      this.doGetFase02(req, res);
      break;
    case "11": // lista de dias dos que temos info
      this.doGetFase11(req, res);
      break;
    case "12": // Lista dos canales dado un dia pdia
      if (!(this.checkPdia(req, res)))
        break;
      this.doGetFase12(req, res);
      break;
    case "13": // lista dunha categoria dado pdia e pcana
      if (!(this.checkPdia(req, res)))
        break;
      if (!(this.checkPcanal(req, res)))
        return;
      this.doGetFase13(req, res);
      break;
    }
  }

  // **********************AQUI EMPEZAN OS METODOS DE CADA
  // FASE********************************************************//

  public void doGetFase01(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    String pwd = req.getParameter("p");
    String auto = req.getParameter("auto");
    if (auto == null) {
      auto = "no";
    }
    if (!(auto.equals("si"))) {
      res.setContentType("text/html; charset=UTF-8");
      PrintWriter out = res.getWriter();
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servicio de consulta</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Servicio de consulta de información sobre canales de TV</h1>");
      out.println("<h2>Bienvenido a este servicio</h2>");
      out.println("<h6><a href=\"?pfase=02&p=" + pwd + "\">Pulsa aqui para ver los ficheros erróneos</a></h6>");
      out.println("<h3>Selecciona una consulta</h3>");
      out.println("<form name='miform'>");
      out.println("<input type='hidden' name='p' value=" + pwd + ">");
      out.println("<input type='hidden' name='pfase' value=''>");
      out.println("<input type=\"radio\" checked='checked'>Consulta 1: Películas de un día en un canal<br><br>");
      out.println("<input type='submit' value='Enviar' onClick='document.forms.miform.elements.pfase.value=\"11\"'>");
      out.println("</form>");
      out.println("</body>");
      out.println("<h6> <i>Práctica 2 - SINT | Iván Ferro Herbón </i> </h6>");
      out.println("</html>");
    } else {
      res.setContentType("text/XML; charset=UTF-8");
      res.setCharacterEncoding("UTF-8");
      PrintWriter out = res.getWriter();
      out.println("<service>");
      out.println("<status>OK</status>");
      out.println("</service>");
    }

    return;
  }

  public void doGetFase02(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    String pwd = req.getParameter("p");
    String auto = req.getParameter("auto");
    ArrayList<String> listaErrors = new ArrayList<String>();
    ArrayList<String> listaWarning = new ArrayList<String>();
    ArrayList<String> listaFatalErrores = new ArrayList<String>();
    for (int i = 0; i < listaErrores.size(); i++) {
      if (listaErrores.get(i).contains("Warning:")) {
        listaWarning.add(listaErrores.get(i));
      } else if (listaErrores.get(i).contains("Fatal error:")) {
        listaFatalErrores.add(listaErrores.get(i));
      } else
        listaErrors.add(listaErrores.get(i));
    }
    if (auto == null) {
      auto = "no";
    }
    if (!(auto.equals("si"))) {
      res.setContentType("text/html; charset=UTF-8");
      PrintWriter out = res.getWriter();
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servicio de consulta</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Servicio de consulta de información sobre canales de TV</h1>");
      // WARNING
      out.println("<h2>Se han encontrado " + listaWarning.size() + " ficheros con warnings</h2>");
      out.println("<ul>");
      for (int i = 0; i < listaWarning.size(); i++) {
        out.println("<li>" + listaWarning.get(i).toString().substring(listaWarning.get(i).toString().indexOf("Archivo: ")+"Archivo: ".length(),listaWarning.get(i).toString().indexOf("Warning: ") ) + ":</li><ul>");
        out.println("<li>"+ listaWarning.get(i).toString().substring(listaWarning.get(i).toString().indexOf("Warning: ")+"Warning: ".length() )+"</li>");
        out.println("</ul>");
        out.println("<br>");  
      }
      out.println("</ul>");
      // ERRORS
      out.println("<h2>Se han encontrado " + listaErrors.size() + " ficheros con errores</h2>");
      out.println("<ul>");
      for (int i = 0; i < listaErrors.size(); i++) {
        out.println("<li>" + listaErrors.get(i).toString().substring(listaErrors.get(i).toString().indexOf("Archivo: ")+"Archivo: ".length(),listaErrors.get(i).toString().indexOf("Error: ") ) + ":</li><ul>");
        out.println("<li>"+ listaErrors.get(i).toString().substring(listaErrors.get(i).toString().indexOf("Error: ")+"Error: ".length() )+"</li>");
        out.println("</ul>");
        out.println("<br>");
      }
      out.println("</ul>");
      // FATAL ERRORS
      out.println("<h2>Se han encontrado " + listaFatalErrores.size() + " ficheros con fatal errors</h2>");
      out.println("<ul>");
      for (int i = 0; i < listaFatalErrores.size(); i++) {
        out.println("<li>" + listaFatalErrores.get(i).toString().substring(listaFatalErrores.get(i).toString().indexOf("Archivo: ")+"Archivo: ".length(),listaFatalErrores.get(i).toString().indexOf("Fatal Error: ") ) + ":</li><ul>");
        out.println("<li>"+ listaFatalErrores.get(i).toString().substring(listaFatalErrores.get(i).toString().indexOf("Fatal Error: ")+"Fatal Error: ".length() )+"</li>");
        out.println("</ul>");
        out.println("<br>");
      }
      out.println("</ul>");
      out.println("</body>");
      out.println("<h6> <i>Práctica 2 - SINT | Iván Ferro Herbón </i> </h6>");
      out.println("</html>");
    } else {
      res.setContentType("text/XML; charset=UTF-8");
      res.setCharacterEncoding("UTF-8");
      PrintWriter out = res.getWriter();
      out.println("<?xml version='1.0' encoding='utf-8'?>");
      out.println("<errores>");
      out.println("<warnings>");
      for (int i = 0; i < listaWarning.size(); i++) {
        out.print("<warning>");
        out.println("<file>" + listaWarning.get(i).toString().substring(listaWarning.get(i).toString().indexOf("Archivo: ")+"Archivo: ".length(),listaWarning.get(i).toString().indexOf("Warning: ") ) + "</file>");
        out.println("<cause>"+ listaWarning.get(i).toString().substring(listaWarning.get(i).toString().indexOf("Warning: ")+"Warning: ".length() )+"</cause>");
        out.print("</warning>");
      }
      out.println("</warnings>");
      out.println("<errors>");
      for (int i = 0; i < listaErrors.size(); i++) {
        out.print("<error>");
        out.println("<file>" + listaErrors.get(i).toString().substring(listaErrors.get(i).toString().indexOf("Archivo: ")+"Archivo: ".length(),listaErrors.get(i).toString().indexOf("Error: ") ) + "</file>");
        out.println("<cause>"+ listaErrors.get(i).toString().substring(listaErrors.get(i).toString().indexOf("Error: ")+"Error: ".length() )+"</cause>");
        out.print("</error>");
      }
      out.println("</errors>");
      out.println("<fatalerrors>");
      for (int i = 0; i < listaFatalErrores.size(); i++) {
        out.print("<fatalerror>");
        out.println("<file>" + listaFatalErrores.get(i).toString().substring(listaFatalErrores.get(i).toString().indexOf("Archivo: ")+"Archivo: ".length(),listaFatalErrores.get(i).toString().indexOf("Fatal Error: ") ) + "</file>");
        out.println("<cause>"+ listaFatalErrores.get(i).toString().substring(listaFatalErrores.get(i).toString().indexOf("Fatal Error: ")+"Fatal Error: ".length() )+"</cause>");
        out.print("</fatalerror>");
      }
      out.println("</fatalerrors>");
      out.println("</errores>");
    }
    return;
  }


  public void doGetFase11(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    ArrayList<String> fechas = new ArrayList<>();

    String pwd = req.getParameter("p");
    String auto = req.getParameter("auto");
    if (auto == null) {
      auto = "no";
    }
    String cat = "Cine";// categoria que buscamos
    fechas = getC1fechas();
    fechas.sort(null);// Orden alfabetico
    if (!(auto.equals("si"))) {
      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servicio de consulta</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Servicio de consulta de información sobre canales de TV</h1>");
      out.println("<h2>Consulta 1</h2>");
      out.println("<h3>Selecciona una fecha:</h3>");
      out.println("<form name='miform'>");
      out.println("<input type='hidden' name='p' value=" + pwd + ">");
      out.println("<input type='hidden' name='pfase' value=''>");
      //out.println("<input type='hidden' name='categoria' value=" + cat + ">");

      for (int w = 0; w < fechas.size(); w++) {
        if (w==0){
          out.println("<input type='radio' name='pdia' checked='checked' value='" + fechas.get(w).toString() + "'>"
          + fechas.get(w).toString() + "<br><br>");
        } else  out.println("<input type='radio' name='pdia' value='" + fechas.get(w).toString() + "'>"
            + fechas.get(w).toString() + "<br><br>");
      }

      out.println("<input type='submit' value='Enviar' onClick='document.forms.miform.elements.pfase.value=\"12\"'>");
      out.println("<input type='submit' value='Atras' onClick='document.forms.miform.elements.pfase.value=\"01\"'>");
      out.println("</form>");
      out.println("</body>");
      out.println("<h6> <i>Práctica 2 - SINT | Iván Ferro Herbón </i> </h6>");
      out.println("</html>");
    } else {
      res.setContentType("text/XML; charset=UTF-8");
      res.setCharacterEncoding("UTF-8");
      PrintWriter out = res.getWriter();
      out.println("<?xml version='1.0' encoding='utf-8'?>");
      out.println("<dias>");
      for (int w = 0; w < fechas.size(); w++) {
        out.println("<dia>" + fechas.get(w).toString() + "</dia>");
      }

      out.println("</dias>");
    }

    return;
  }

  public void doGetFase12(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    ArrayList<Canal> canales = new ArrayList<>();
    
    String pwd = req.getParameter("p");
    String auto = req.getParameter("auto");
    if (auto == null) {
      auto = "no";
    }
    String cat = "Cine";// categoria que buscamos
    String pdia = req.getParameter("pdia");
    canales = getc1Canales(pdia);
    Collections.sort(canales);
    if (!(auto.equals("si"))) {
      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servicio de consulta</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Servicio de consulta de información sobre canales de TV</h1>");
      out.println("<h2>Consulta 1: Fecha=" + pdia + "</h2>");
      out.println("<h3>Selecciona una canal:</h3>");
      out.println("<form name='miform'>");
      out.println("<input type='hidden' name='p' value=" + pwd + ">");
      out.println("<input type='hidden' name='pfase' value=''>");
      out.println("<input type='hidden' name='pdia' value=" + pdia + ">");

      for (int w = 0; w < canales.size(); w++) {
        Canal canal = canales.get(w);
        if (w == 0){
          out.println(
            "<input type='radio' name='pcanal' checked='checked' value='" + canal.getNombreCanal() + "'>Canal='" + canal.getNombreCanal()
                + "' --- Idioma = '" + canal.getlang() + "' --- Grupo = '" + canal.getGrupo() + "'<br><br>");
        }else out.println(
            "<input type='radio' name='pcanal' value='" + canal.getNombreCanal() + "'>Canal='" + canal.getNombreCanal()
                + "' --- Idioma = '" + canal.getlang() + "' --- Grupo = '" + canal.getGrupo() + "'<br><br>");
      }
      out.println("<input type='submit' value='Enviar' onClick='document.forms.miform.elements.pfase.value=\"13\"'>");
      out.println("<input type='submit' value='Atras' onClick='document.forms.miform.elements.pfase.value=\"11\"'>");
      out.println("<input type='submit' value='Inicio' onClick='document.forms.miform.elements.pfase.value=\"01\"'>");
      out.println("</form>");
      out.println("</body>");
      out.println("<h6> <i>Práctica 2 - SINT | Iván Ferro Herbón </i> </h6>");
      out.println("</html>");
    } else {
      res.setContentType("text/XML; charset=UTF-8");
      res.setCharacterEncoding("UTF-8");
      PrintWriter out = res.getWriter();
      out.println("<?xml version='1.0' encoding='utf-8'?>");
      out.println("<canales>");
      for (int w = 0; w < canales.size(); w++) {
        Canal canal = canales.get(w);
        out.println("<canal idioma=\"" + canal.getlang() + "\" grupo=\"" + canal.getGrupo() + "\">"
            + canal.getNombreCanal() + "</canal>");
      }
      out.println("</canales>");
    }
    return;
  }

  public void doGetFase13(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    ArrayList<Programa> programas = new ArrayList<>();

    
    String pwd = req.getParameter("p");
    String auto = req.getParameter("auto");
    if (auto == null) {
      auto = "no";
    }
    String cat = "Cine";// categoria que buscamos
    String pdia = req.getParameter("pdia");// categoria que buscamos
    String pcanal = req.getParameter("pcanal");// canal que buscamos
    programas = getC1Pelicula(pdia, pcanal);
    if (!(auto.equals("si"))) {
      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servicio de consulta</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Servicio de consulta de información sobre canales de TV</h1>");
      out.println("<h2>Consulta 1: Fecha=" + pdia + ",Canal=" + pcanal + "</h2>");
      out.println("<h3>Selecciona una canal:</h3>");
      out.println("<form name='miform'>");
      out.println("<input type='hidden' name='p' value=" + pwd + ">");
      out.println("<input type='hidden' name='pfase' value=''>");
      out.println("<input type='hidden' name='pdia' value=" + pdia + ">");
      out.println("<ul>");
      for (int w = 0; w < programas.size(); w++) {
        Programa pelicula = programas.get(w);
        out.println("<li>Titulo ='" + pelicula.getNombrePrograma() + "' --- Edad Mínima = '" + pelicula.getedadminima()
            + "' -- Hora = '" + pelicula.getHoraInicio() + "' -- Resumen = '" + pelicula.getResumen() + "'");
      }
      out.println("</ul>");
      out.println("<input type='submit' value='Atras' onClick='document.forms.miform.elements.pfase.value=\"12\"'>");
      out.println("<input type='submit' value='Inicio' onClick='document.forms.miform.elements.pfase.value=\"01\"'>");
      out.println("</form>");
      out.println("</body>");
      out.println("<h6> <i>Práctica 2 - SINT | Iván Ferro Herbón </i> </h6>");
      out.println("</html>");
    } else {
      res.setContentType("text/XML; charset=UTF-8");
      res.setCharacterEncoding("UTF-8");
      PrintWriter out = res.getWriter();
      out.println("<?xml version='1.0' encoding='utf-8'?>");
      out.println("<peliculas>");
      for (int w = 0; w < programas.size(); w++) {
        Programa pelicula = programas.get(w);
        out.println("<pelicula edad=\"" + pelicula.getedadminima() + "\" hora=\"" + pelicula.getHoraInicio()
            + "\" resumen=\"" + pelicula.getResumen() + "\">" + pelicula.getNombrePrograma() + "</pelicula>");
      }
      out.println("</peliculas>");
    }
    return;
  }

  public boolean checkPwd(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    String auto = req.getParameter("auto");
    if (auto == null) {
      auto = "no";
    }
    String pwd = req.getParameter("p");

    // COMPROBACION DE PWD
    if (pwd == null) {
      if (!(auto.equals("si"))) {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>No pwd</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Password no introducida</h1>");
        out.println("</body>");
        out.println("<h6> <i>Práctica 2 - SINT | Iván Ferro Herbón </i> </h6>");
        out.println("</html>");
      } else {
        res.setContentType("text/XML; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8'?>");
        out.println("<wrongRequest>no passwd</wrongRequest>");
      }
      return false;
    }
    if (!(pwd.equals("Sint104104"))) {
      if (!(auto.equals("si"))) {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Bad pwd</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Password incorrecta</h1>");
        out.println("</body>");
        out.println("<h6> <i>Práctica 2 - SINT | Iván Ferro Herbón </i> </h6>");
        out.println("</html>");
      } else {
        res.setContentType("text/XML; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8'?>");
        out.println("<wrongRequest>bad passwd</wrongRequest>");
      }
      return false;
    }

    return true;
  }

  public boolean checkPdia(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    String auto = req.getParameter("auto");
    if (auto == null) {
      auto = "no";
    }
    String pdia = req.getParameter("pdia");

    // COMPROBACION DE Pdia
    if (pdia == null) {
      if (!(auto.equals("si"))) {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>No pdia</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Paramento pdia no introducido</h1>");
        out.println("</body>");
        out.println("<h6> <i>Práctica 2 - SINT | Iván Ferro Herbón </i> </h6>");
        out.println("</html>");
      } else {
        res.setContentType("text/XML; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8'?>");
        out.println("<wrongRequest>no param:pdia</wrongRequest>");
      }
      return false;
    }

    return true;
  }

  public boolean checkPcanal(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    String auto = req.getParameter("auto");
    if (auto == null) {
      auto = "no";
    }
    String pcanal = req.getParameter("pcanal");

    // COMPROBACION DE Pcanal
    if (pcanal == null) {
      if (!(auto.equals("si"))) {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>No pcanal</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Paramento pcanal no introducido</h1>");
        out.println("</body>");
        out.println("<h6> <i>Práctica 2 - SINT | Iván Ferro Herbón </i> </h6>");
        out.println("</html>");
      } else {
        res.setContentType("text/XML; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<?xml version='1.0' encoding='utf-8'?>");
        out.println("<wrongRequest>no param:pcanal</wrongRequest>");
      }
      return false;
    }

    return true;
  }

  // **********************AQUI ACABAN OS METODOS DE CADA
  // FASE********************************************************//

  /*****************************
   * METODO PARA DOM********************************
   */
  public void leerXML(String XML) throws ServletException, IOException, FileNotFoundException {
    DocumentBuilderFactory factory = null;
    DocumentBuilder builder = null;
    org.w3c.dom.Document doc = null;
    TVML_ErrorHandler errorHandler = new TVML_ErrorHandler();


    boolean existe = false;
    String XMLAux = XML;
    File fileSchema = new File(URLSchema);
    factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(true);
    factory.setNamespaceAware(true);
    factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
    factory.setAttribute(JAXP_SCHEMA_SOURCE, fileSchema);

    try {
      builder = factory.newDocumentBuilder();
      builder.setErrorHandler(errorHandler);

      boolean continuar = true;

      try {

        if (XML.startsWith("/Users/")) {
          //doc = builder.parse(new URL(XML).openStream();
          doc = builder.parse(XML);
        } else {
          //doc = builder.parse(new URL(url + XML).openStream());
          doc = builder.parse(url + XML);
        }

      } catch (FileNotFoundException e) {
        continuar = false;
      }
      if (continuar) {
        try {
          dia = doc.getElementsByTagName("Fecha").item(0).getTextContent();
          mapDocs.put(dia, doc);
          // listaLeidos.add(XML);
          XPathFactory xPathFactory = XPathFactory.newInstance();
          XPath xpath = xPathFactory.newXPath();
          String exp = "//TVML";
          NodeList documents = (NodeList) xpath.evaluate(exp, doc, XPathConstants.NODESET);

          for (int i = 0; i < documents.getLength(); i++) {
            existe = false;
            documento = documents.item(i).getTextContent();
            
            if (!(documento.startsWith("/Users"))) {
              documento = url + documento;
            }
            if (listaXMLs.contains(documento)){
              existe = true;}
            if (!existe){
              listaXMLs.add(documento);}
          }
        } catch (XPathException e) {
          e.printStackTrace();
        }
      }

    } catch (ParserConfigurationException e) {
    } catch (SAXException e) {
      
      if (errorHandler.hasWarning() || errorHandler.hasError() || errorHandler.hasFatalError()) {

        listaErrores.add("<b>Archivo: " + XMLAux + errorHandler.getMensaxe());
        errorHandler.clear();
        return;

      } else {

        listaErrores.add("<b>Archivo: " + XMLAux + "Error: " + e.toString());
        return;

      }
      
    }
  }

  /**********************************
   * BUSCAMOS AS COUSAS
   **********************************************/

  ArrayList<String> getC1fechas() {
    ArrayList<String> fechas = new ArrayList<>();
    ;
    Document doc;
    String exp;

    for (Map.Entry<String, Document> entry : mapDocs.entrySet()) {
      doc = entry.getValue();
      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xpath = xPathFactory.newXPath();
      try {
        exp = "//Fecha";
        NodeList fecha = (NodeList) xpath.evaluate(exp, doc, XPathConstants.NODESET);
        for (int j = 0; j < fecha.getLength(); j++) {
          // System.out.println(entry.getKey()+fecha.item(j));
          fechas.add(fecha.item(j).getTextContent().toString());
        }
      } catch (XPathException e) {
        e.printStackTrace();
      }

    }

    return fechas;
  }

  ArrayList<Canal> getc1Canales(String fecha) {
    ArrayList<Canal> canales = new ArrayList<>();
    ;
    Document doc;
    String exp;

    doc = mapDocs.get(fecha);
    XPathFactory xPathFactory = XPathFactory.newInstance();
    XPath xpath = xPathFactory.newXPath();
    try {
      exp = "//Canal/@lang";
      NodeList listalangCanal = (NodeList) xpath.evaluate(exp, doc, XPathConstants.NODESET);
      exp = "//Canal/@idCanal";
      NodeList listaidCanal = (NodeList) xpath.evaluate(exp, doc, XPathConstants.NODESET);
      exp = "//Canal/NombreCanal";
      NodeList listaCanal = (NodeList) xpath.evaluate(exp, doc, XPathConstants.NODESET);
      exp = "//Canal/Grupo";
      NodeList listaGrupo = (NodeList) xpath.evaluate(exp, doc, XPathConstants.NODESET);
      for (int i = 0; i < listaCanal.getLength(); i++) {
        Canal canalAux = new Canal();
        canalAux.setlang(listalangCanal.item(i).getTextContent());
        canalAux.setidCanal(listaidCanal.item(i).getTextContent());
        canalAux.setNombreCanal(listaCanal.item(i).getTextContent());
        canalAux.setGrupo(listaGrupo.item(i).getTextContent());
        canales.add(canalAux);
      }
    } catch (XPathException e) {
      e.printStackTrace();
    }

    return canales;
  }

 ArrayList<Programa> getC1Pelicula (String fecha, String canal){
  ArrayList<Programa> programas = new ArrayList<>();
  Document doc;
  String exp;

      doc = mapDocs.get(fecha);
      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xpath = xPathFactory.newXPath();
      try{
          exp ="//Programa[(Categoria='Cine')and(../NombreCanal='"+canal+"')]";
          NodeList listaProgramas = (NodeList)xpath.evaluate(exp, doc, XPathConstants.NODESET);
          NodeList aux;
          for(int i=0; i<listaProgramas.getLength();i++){
              Programa programaAux = new Programa();
              if(listaProgramas.item(i).getAttributes().getNamedItem("langs").getNodeValue().length()!=0){
              programaAux.setlangs(listaProgramas.item(i).getAttributes().getNamedItem("langs").getNodeValue());
              } else {
                for(Canal a: canales) {
                  String canle = a.getNombreCanal();
                  if(canle.equals(canal)) programaAux.setlangs(a.getlang());;
              }
              }
              programaAux.setedadminima(listaProgramas.item(i).getAttributes().getNamedItem("edadminima").getNodeValue());
              aux=(NodeList) xpath.evaluate("NombrePrograma", listaProgramas.item(i), XPathConstants.NODESET);
              programaAux.setNombrePrograma(aux.item(0).getFirstChild().getTextContent());
              aux=(NodeList) xpath.evaluate("Categoria", listaProgramas.item(i), XPathConstants.NODESET);
              programaAux.setCategoria(aux.item(0).getFirstChild().getTextContent());
              aux=(NodeList) xpath.evaluate("HoraInicio", listaProgramas.item(i), XPathConstants.NODESET);
              programaAux.setHoraInicio(aux.item(0).getFirstChild().getTextContent());
              aux=(NodeList) xpath.evaluate("Duracion", listaProgramas.item(i), XPathConstants.NODESET);
              if (aux.getLength() != 0){
                  programaAux.setDuracion(aux.item(0).getFirstChild().getTextContent());
              }
              aux=(NodeList) xpath.evaluate("HoraFin", listaProgramas.item(i), XPathConstants.NODESET);
              if (aux.getLength() != 0){
                  programaAux.setHoraFin(aux.item(0).getFirstChild().getTextContent());
              }
              String descripcion = (String)xpath.evaluate("text()[normalize-space()]",listaProgramas.item(i), XPathConstants.STRING);
              programaAux.setResumen(descripcion.trim());
              programas.add(programaAux);
              
          }
      
      }catch(

  XPathException e){
          e.printStackTrace();
      }

  return programas;
}

}