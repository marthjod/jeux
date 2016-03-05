package de.fhb.jeux.bulk.bulkexport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.RuleDTO;
import java.util.List;
import org.jboss.logging.Logger;

public class JSONExporter {

    private static Logger logger = Logger.getLogger(JSONExporter.class);

    public static String exportGroupsToJson(List<GroupDTO> groups) {

        String json = "";

        try {
            json = new Gson().toJson(groups);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return json;
    }

    public static String exportRulesToJson(List<RuleDTO> rules) {

        String json = "";
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        try {
            json = gson.toJson(rules);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return json;
    }

}
