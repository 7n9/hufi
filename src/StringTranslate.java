import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StringTranslate {

    private File langFile;
    private Properties translateTable;
    private static StringTranslate instance = new StringTranslate();


    public StringTranslate() {
        translateTable = new Properties();
        langFile = new File("lang/en.lang");
        try {
            //translateTable.load(langFile.toURL().openStream());
            translateTable.load((StringTranslate.class).getResourceAsStream("/lang/pl.lang"));
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    public static StringTranslate getInstance() {
        return instance;
    }

    public String translateString(String s) {
        return translateTable.getProperty(s);
    }

}
