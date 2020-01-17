package p2;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

class TVML_ErrorHandler extends DefaultHandler {
    public static boolean error;
    public static boolean warning;
    public static boolean fatalerror;
    public static String mensaxe;
    public TVML_ErrorHandler() {
        error = false;
        warning = false;
        fatalerror = false;
        mensaxe = null;
    }
    public void warning(SAXParseException e) throws SAXException {
        warning = true;
        mensaxe = "Warning: " + e.toString();
        throw new SAXException();
    }
    public void error(SAXParseException e) throws SAXException {
        error = true;
        mensaxe = "Error: " + e.toString();
        throw new SAXException();
    }
    public void fatalerror(SAXParseException e) throws SAXException {
        fatalerror = true;
        mensaxe = "Fatal Error: " + e.toString();
        throw new SAXException();
    }
    public static boolean hasWarning() {
        return warning;
    }
    public static boolean hasError() {
        return error;
    }
    public static boolean hasFatalError() {
        return fatalerror;
    }
    public static String getMensaxe() {
        return mensaxe;
    }
    public static void clear() {
        warning = false;
        error = false;
        fatalerror = false;
        mensaxe = null;
    }
}
