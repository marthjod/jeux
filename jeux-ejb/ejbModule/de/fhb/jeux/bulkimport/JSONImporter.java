package de.fhb.jeux.bulkimport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import org.jboss.logging.Logger;

public class JSONImporter {

    private static Logger logger = Logger.getLogger(JSONImporter.class);

    public static List<ImportGroup> importGroupsFromJson(Reader reader) {

        List<ImportGroup> importGroups = null;

        try {
            importGroups = new Gson().fromJson(reader,
                    new TypeToken<List<ImportGroup>>() {
                    }.getType());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        importGroups = removeNullImports(importGroups);

        return importGroups;
    }

    public static List<ImportRule> importRulesFromJson(Reader reader) {

        List<ImportRule> importRules = null;

        try {
            importRules = new Gson().fromJson(reader,
                    new TypeToken<List<ImportRule>>() {
                    }.getType());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        importRules = removeNullImports(importRules);

        return importRules;
    }

    private static List removeNullImports(List imports) {

        if (imports != null) {
            Iterator it = imports.listIterator();
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                }
            }
        }

        return imports;
    }
}
