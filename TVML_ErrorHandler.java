package p2;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

class TVML_ErrorHandler extends DefaultHandler {
    public static boolean error;
    public static boolean warning;
    public static boolean fatalerror;
    public static String message;
    public TVML_ErrorHandler() {
        error = false;
        warning = false;
        fatalerror = false;
        message = null;
    }
    public void warning(SAXParseException spe) throws SAXException {
        warning = true;
        message = "Warning: " + spe.toString();
        throw new SAXException();
    }
    public void error(SAXParseException spe) throws SAXException {
        error = true;
        message = "Error: " + spe.toString();
        throw new SAXException();
    }
    public void fatalerror(SAXParseException spe) throws SAXException {
        fatalerror = true;
        message = "Fatal Error: " + spe.toString();
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
    public static String getMessage() {
        return message;
    }
    public static void clear() {
        warning = false;
        error = false;
        fatalerror = false;
        message = null;
    }
}
