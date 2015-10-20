package de.fhb.jeux.bulkimport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.util.List;
import org.jboss.logging.Logger;

public class JSONImporter {

    private static Logger logger = Logger.getLogger(JSONImporter.class);

    public static List<ImportGroup> importFromJson(Reader reader) {

        List<ImportGroup> importGroups = null;
        Gson gson = new Gson();

        try {
            importGroups = gson.fromJson(reader,
                    new TypeToken<List<ImportGroup>>() {
                    }.getType());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return importGroups;
    }
}
