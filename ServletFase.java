import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
//IMPORT PARA O DOM
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ServletFase extends HttpServlet{


    public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException{

        //parametros que recibo
        String pwd = req.getParameter("p"); //password
        String pfase = req.getParameter("pfase"); //tipo de consulta
        if (pfase == null){pfase="01";}
        //String auto = req.getParameter("auto");// auto ou html
        //PrintWriter out=res.getWriter();
        if(!(this.checkPwd(req , res))) return;
        
        switch(pfase){
            case "01": //pantalla inicial
                    this.doGetFase01(req , res);
                    break;

            case "02": //lista ficheros erroneos
                    this.doGetFase02(req , res);
                    break;
            case "11": //lista de dias dos que temos info
                    this.doGetFase11(req , res);
                    break;
            case "12": //Lista dos canales dado un dia pdia
                    if(!(this.checkPdia(req , res)))break;
                    this.doGetFase12(req , res);
                    break;
            case "13": //lista dunha categoria dado pdia e pcana
                    if(!(this.checkPdia(req , res)))break;
                    if(!(this.checkPcanal(req , res)))return;
                    this.doGetFase13(req , res);
                    break;
    }
  }

  //**********************AQUI EMPEZAN OS METODOS DE CADA FASE********************************************************//

    public void doGetFase01(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        String pwd = req.getParameter("p");
        String auto = req.getParameter("auto");
        if (auto == null){auto = "no";}
        if (!(auto.equals("si"))){
            res.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servicio de consulta</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servicio de consulta de información sobre canales de TV</h1>");
            out.println("<h2>Bienvenido a este servicio</h2>");
            out.println("<h6><a href=\"?pfase=02\">Pulsa aqui para ver los ficheros erróneos</a></h6>");
            out.println("<h3>Selecciona una consulta</h3>");
            out.println("<form name='miform'>");
            out.println("<input type='hidden' name='auto' value="+auto+">");
            out.println("<input type='hidden' name='p' value="+pwd+">");
            out.println("<input type='hidden' name='pfase' value=''>");
            out.println("<input type=\"radio\" name=\"categoria\" value=\"Cine\">Consulta 1: Películas de un día en un canal<br><br>");
            out.println("<input type='submit' value='Enviar' onClick='document.forms.miform.elements.pfase.value=\"11\"'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }   else {
            res.setContentType("text/XML");
            out.println("<service>");
            out.println("<status>OK</status>");
            out.println("</service>");
        }

        return;
    }

    public void doGetFase02(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servicio de consulta</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Estou na fase de erros</h1>");
        out.println("</body>");
        out.println("</html>");
        return;
    }

    public void doGetFase11(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        String pwd = req.getParameter("p");
        String auto = req.getParameter("auto");
        if (auto == null){auto = "no";}
        String cat = req.getParameter("categoria");// categoria que buscamos
        if(!(auto.equals("si"))){
            res.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servicio de consulta</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servicio de consulta de información sobre canales de TV</h1>");
            out.println("<h2>Bienvenido a este servicio</h2>");
            out.println("<h3>Selecciona una fecha:</h3>");
            out.println("<form name='miform'>");
            out.println("<input type='hidden' name='p' value="+pwd+">");
            out.println("<input type='hidden' name='pfase' value=''>");
            out.println("<input type='hidden' name='auto' value="+auto+">");
            out.println("<input type='hidden' name='categoria' value="+cat+">");
            out.println("<input type=\"radio\" name=\"pdia\" value=\"2004/12/01\">2004/12/01<br><br>");
            out.println("<input type=\"radio\" name=\"pdia\" value=\"2004/12/02\">2004/12/02<br><br>");
            out.println("<input type=\"radio\" name=\"pdia\" value=\"2004/12/03\">2004/12/03<br><br>");
            out.println("<input type=\"radio\" name=\"pdia\" value=\"2004/12/04\">2004/12/04<br><br>");
            out.println("<input type='submit' value='Enviar' onClick='document.forms.miform.elements.pfase.value=\"12\"'>");
            out.println("<input type='submit' value='Atras' onClick='document.forms.miform.elements.pfase.value=\"01\"'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }   else {
            res.setContentType("text/XML");
            out.println("<dias>");
            out.println("<dia>2014/12/01</dia>");
            out.println("<dia>2014/12/02</dia>");
            out.println("<dia>2014/12/03</dia>");
            out.println("<dia>2014/12/04</dia>");
            out.println("</dias>");
        }

        return;
    }

    public void doGetFase12(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        String pwd = req.getParameter("p");
        String auto = req.getParameter("auto");
        if (auto == null){auto = "no";}
        String cat = req.getParameter("categoria");// categoria que buscamos
        String pdia = req.getParameter("pdia");// categoria que buscamos
        if(!(auto.equals("si"))){
            res.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servicio de consulta</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servicio de consulta de información sobre canales de TV</h1>");
            out.println("<h2>Consulta 1: Fecha="+pdia+"</h2>");
            out.println("<h3>Selecciona una canal:</h3>");
            out.println("<form name='miform'>");
            out.println("<input type='hidden' name='p' value="+pwd+">");
            out.println("<input type='hidden' name='pfase' value=''>");
            out.println("<input type='hidden' name='auto' value="+auto+">");
            out.println("<input type='hidden' name='categoria' value="+cat+">");
            out.println("<input type='hidden' name='pdia' value="+pdia+">");
            out.println("<input type=\"radio\" name=\"pcanal\" value=\"A3\">Canal='A3' --- Idioma = 'en' --- Grupo = 'A3Media'<br><br>");
            out.println("<input type=\"radio\" name=\"pcanal\" value=\"Neox\">Canal='Neox' --- Idioma = 'fr' --- Grupo = 'A3Media'<br><br>");
            out.println("<input type=\"radio\" name=\"pcanal\" value=\"T5\">Canal='T5' --- Idioma = 'it' --- Grupo = 'Mediaset'<br><br>");
            out.println("<input type=\"radio\" name=\"pcanal\" value=\"TVE1\">Canal='TVE1' --- Idioma = 'es' --- Grupo = 'RTVE'<br><br>");
            out.println("<input type=\"radio\" name=\"pcanal\" value=\"TVE2\">Canal='TVE2' --- Idioma = 'es' --- Grupo = 'RTVE'<br><br>");
            out.println("<input type='submit' value='Enviar' onClick='document.forms.miform.elements.pfase.value=\"13\"'>");
            out.println("<input type='submit' value='Atras' onClick='document.forms.miform.elements.pfase.value=\"11\"'>");
            out.println("<input type='submit' value='Inicio' onClick='document.forms.miform.elements.pfase.value=\"01\"'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }   else {
            res.setContentType("text/XML");
            out.println("<canales>");
            out.println("<canal idioma=\"en\" grupo=\"A3Media\">A3</canal>");
            out.println("<canal idioma=\"fr\" grupo=\"A3Media\">Neox</canal>");
            out.println("<canal idioma=\"it\" grupo=\"Mediaset\">T5</canal>");
            out.println("</canales>");
        }
        return;
    }

    public void doGetFase13(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException{
        PrintWriter out = res.getWriter();
        String pwd = req.getParameter("p");
        String auto = req.getParameter("auto");
        if (auto == null){auto = "no";}
        String cat = req.getParameter("categoria");// categoria que buscamos
        String pdia = req.getParameter("pdia");// categoria que buscamos
        String pcanal = req.getParameter("pcanal");// canal que buscamos
        if(!(auto.equals("si"))){
            res.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servicio de consulta</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servicio de consulta de información sobre canales de TV</h1>");
            out.println("<h2>Consulta 1: Fecha="+pdia+",Canal="+pcanal+"</h2>");
            out.println("<h3>Selecciona una canal:</h3>");
            out.println("<form name='miform'>");
            out.println("<input type='hidden' name='p' value="+pwd+">");
            out.println("<input type='hidden' name='pfase' value=''>");
            out.println("<input type='hidden' name='auto' value="+auto+">");
            out.println("<input type='hidden' name='categoria' value="+cat+">");
            out.println("<input type='hidden' name='pdia' value="+pdia+">");
            out.println("<ul>");
            out.println("<li>Titulo ='Dumbo' --- Edad Mínima = '6' -- Hora = '18:00' -- Resumen = 'El orejones'");
            out.println("<li>Titulo ='Al este del Edén' --- Edad Mínima = '14' -- Hora = '16:00' -- Resumen = 'Dean el rebelde'");
            out.println("<li>Titulo ='Misión imposible' --- Edad Mínima = '16' -- Hora = '22:00' -- Resumen = 'Cruise otra vez'");
            out.println("</ul>");
            out.println("<input type='submit' value='Atras' onClick='document.forms.miform.elements.pfase.value=\"12\"'>");
            out.println("<input type='submit' value='Inicio' onClick='document.forms.miform.elements.pfase.value=\"01\"'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }   else {
            res.setContentType("text/XML");
            out.println("<peliculas>");
            out.println("<pelicula edad=\"6\" hora=\"18:00\" resumen=\"El orejones\">Dumbo</pelicula>");
            out.println("<pelicula edad=\"14\" hora=\"16:00\" resumen=\"Dean el rebelde\">Al este del Edén</pelicula>");
            out.println("<pelicula edad=\"16\" hora=\"22:00\" resumen=\"Cruise otra vez\">Misión Imposible</pelicula>");
            out.println("</peliculas>");
        }
        return;
    }

    public boolean checkPwd(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        String auto = req.getParameter("auto");
        if (auto == null){auto = "no";}
        String pwd = req.getParameter("p");

        // COMPROBACION DE PWD
        if (pwd == null){
          if (!(auto.equals("si"))){
              res.setContentType("text/html");
              out.println("<html>");
              out.println("<head>");
              out.println("<title>No pwd</title>");
              out.println("</head>");
              out.println("<body>");
              out.println("<h1>Password no introducida</h1>");
              out.println("</body>");
              out.println("</html>");
          }   else {
              res.setContentType("text/XML");
              out.println("<wrongRequest>no passwd</wrongRequest>");
          }
          return false;
        }
        if (!(pwd.equals("Sint104"))){
          if (!(auto.equals("si"))){
              res.setContentType("text/html");
              out.println("<html>");
              out.println("<head>");
              out.println("<title>Bad pwd</title>");
              out.println("</head>");
              out.println("<body>");
              out.println("<h1>Password incorrecta</h1>");
              out.println("</body>");
              out.println("</html>");
          }   else {
              res.setContentType("text/XML");
              out.println("<wrongRequest>bad passwd</wrongRequest>");
          }
          return false;
        }

        return true;
    }

    public boolean checkPdia(HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException {
      PrintWriter out = res.getWriter();
      String auto = req.getParameter("auto");
      if (auto == null){auto = "no";}
      String pdia = req.getParameter("pdia");

      // COMPROBACION DE Pdia
      if (pdia == null){
        if (!(auto.equals("si"))){
            res.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>No pdia</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Paramento pdia no introducido</h1>");
            out.println("</body>");
            out.println("</html>");
        }   else {
            res.setContentType("text/XML");
            out.println("<wrongRequest>no param:pdia</wrongRequest>");
        }
        return false;
      }

      return true;
  }


  public boolean checkPcanal(HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException {
      PrintWriter out = res.getWriter();
      String auto = req.getParameter("auto");
      if (auto == null){auto = "no";}
      String pcanal = req.getParameter("pcanal");

      // COMPROBACION DE Pcanal
      if (pcanal == null){
        if (!(auto.equals("si"))){
            res.setContentType("text/html");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>No pcanal</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Paramento pcanal no introducido</h1>");
            out.println("</body>");
            out.println("</html>");
        }   else {
            res.setContentType("text/XML");
            out.println("<wrongRequest>no param:pcanal</wrongRequest>");
        }
        return false;
      }

      return true;
  }

//**********************AQUI ACABAN OS METODOS DE CADA FASE********************************************************//
  

}                           