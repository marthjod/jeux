package de.fhb.jeux.context;

import de.fhb.jeux.config.BonusPointsDistribution;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;

public class ContextListener implements ServletContextListener {

    protected static Logger logger = Logger.getLogger(ContextListener.class);

    public ContextListener() {
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {

        logger.info("Context initialized");
        ServletContext sc = event.getServletContext();
        String scoresheetsLaTeXPreamble = null;
        String scoresheetsLaTeXBody = null;
        String scoresheetsLaTeXFooter = null;

        // get infos from web.xml and save object to servlet context
        // to be accessed by other servlets later
        // avoiding I/O overhead by reading config file only once at startup
        String configPath = sc.getRealPath(sc.getInitParameter("BONUS_POINTS_CONFIG_PATH"));
        BonusPointsDistribution config = new BonusPointsDistribution(configPath);

        if (config.getRules().size() > 0) {
            logger.info("Loaded " + config.getRules().size()
                    + " bonus points distribution rules from " + configPath);
        } else {
            logger.error("No bonus points distribution loaded from " + configPath);
        }

        try {
            scoresheetsLaTeXPreamble = FileUtils.readFileToString(new File(sc
                    .getRealPath(sc
                            .getInitParameter("SCORESHEETS_LATEX_PREAMBLE"))));
            scoresheetsLaTeXBody = FileUtils
                    .readFileToString(new File(sc.getRealPath(sc
                                            .getInitParameter("SCORESHEETS_LATEX_BODY"))));
            scoresheetsLaTeXFooter = FileUtils.readFileToString(new File(sc
                    .getRealPath(sc
                            .getInitParameter("SCORESHEETS_LATEX_FOOTER"))));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        sc.setAttribute("bonusPointsConfig", config);
        sc.setAttribute("scoresheetsLaTeXPreamble", scoresheetsLaTeXPreamble);
        sc.setAttribute("scoresheetsLaTeXBody", scoresheetsLaTeXBody);
        sc.setAttribute("scoresheetsLaTeXFooter", scoresheetsLaTeXFooter);
    }
}
