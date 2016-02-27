package de.fhb.jeux.bulk.bulkimport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.RuleDTO;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import org.jboss.logging.Logger;

public class JSONImporter {

    private static Logger logger = Logger.getLogger(JSONImporter.class);

    public static List<GroupDTO> importGroupsFromJson(Reader reader) {

        List<GroupDTO> importGroups = null;

        try {
            importGroups = new Gson().fromJson(reader,
                    new TypeToken<List<GroupDTO>>() {
                    }.getType());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        importGroups = removeNulls(importGroups);

        return importGroups;
    }

    public static List<RuleDTO> importRulesFromJson(Reader reader) {

        List<RuleDTO> importRules = null;

        try {
            importRules = new Gson().fromJson(reader,
                    new TypeToken<List<RuleDTO>>() {
                    }.getType());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        importRules = removeNulls(importRules);

        return importRules;
    }

    private static List removeNulls(List list) {

        if (list != null) {
            Iterator it = list.listIterator();
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                }
            }
        }

        return list;
    }
}
