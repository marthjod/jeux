package de.fhb.jeux.bulkimport;

import de.fhb.jeux.dto.GroupDTO;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.jboss.logging.Logger;

public class CSVImporter {

    private static Logger logger = Logger.getLogger(CSVImporter.class);

    public static List<GroupDTO> getGroups(Reader reader) {
        // Example file content:
        //
        // "name","minSets","maxSets","roundId","active","completed"
        // "Group A",2,3,1,1,0
        // "Group B",2,3,1,0,0
        // "Group C",1,1,2,0,0

        List<GroupDTO> groups = new ArrayList<>();
        try {
            for (CSVRecord record : CSVFormat.DEFAULT.withHeader().parse(reader)) {
                String name = record.get("name");
                int minSets = Integer.parseInt(record.get("minSets"), 10);
                int maxSets = Integer.parseInt(record.get("maxSets"), 10);
                int roundId = Integer.parseInt(record.get("roundId"), 10);
                boolean active = "1".equals(record.get("active"));
                boolean completed = "1".equals(record.get("completed"));
                GroupDTO groupDTO = new GroupDTO(name, minSets, maxSets,
                        roundId, active, completed);
                groups.add(groupDTO);
            }
        } catch (IOException ex) {
            logger.warn(ex.getMessage());
        }

        return groups;
    }
}
