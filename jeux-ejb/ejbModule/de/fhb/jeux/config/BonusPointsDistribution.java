package de.fhb.jeux.config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;

// holds multiple single bonus points distribution rules in a list
public class BonusPointsDistribution {

    private static Logger logger = Logger
            .getLogger(BonusPointsDistribution.class);

    private List<BonusPointsRule> ruleList;

    public BonusPointsDistribution(List<BonusPointsRule> ruleList) {
        this.ruleList = ruleList;
    }

    public BonusPointsDistribution(String configPath) {
        this.ruleList = marshalJson(stringFromPath(configPath));
    }

    private static String stringFromPath(String path) {
        String json = null;

        try {
            json = FileUtils.readFileToString(new File(path));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return json;
    }

    protected static List<BonusPointsRule> marshalJson(String json) {
        ArrayList<BonusPointsRule> rules = new ArrayList<>();

        if (json != null) {
            Gson gson = new Gson();
            try {
                rules = gson.fromJson(json,
                        new TypeToken<List<BonusPointsRule>>() {
                        }.getType());
            } catch (JsonSyntaxException e) {
                logger.error("Error parsing JSON '" + json + "': "
                        + e.getMessage());
            }
        }
        return rules;
    }

    public List<BonusPointsRule> getRules() {
        return ruleList;
    }

}
