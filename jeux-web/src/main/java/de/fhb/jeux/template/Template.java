package de.fhb.jeux.template;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import javax.servlet.ServletContext;
import org.jboss.logging.Logger;

public class Template {

    private final static String TEMPLATE_PREFIX = "/WEB-INF/templates/";
    private final static String TEMPLATE_SUFFIX = ".html";

    protected static Logger logger = Logger.getLogger(Template.class);

    public static PebbleTemplate getTemplate(ServletContext ctx, String templateName) {
        PebbleTemplate compiledTemplate = null;
        Loader templateLoader = new ServletLoader(ctx);
        templateLoader.setPrefix(Template.TEMPLATE_PREFIX);
        templateLoader.setSuffix(Template.TEMPLATE_SUFFIX);
        PebbleEngine engine = new PebbleEngine(templateLoader);
        engine.setStrictVariables(true);
        try {
            compiledTemplate = engine.getTemplate(templateName);
        } catch (PebbleException ex) {
            logger.warn(ex);
        }
        return compiledTemplate;
    }

    public static Writer renderTemplate(PebbleTemplate template, Map<String, Object> context) {
        Writer w = new StringWriter();

        if (template != null && context != null) {
            try {
                template.evaluate(w, context);
            } catch (PebbleException ex) {
                logger.warn(ex);
            } catch (IOException ex) {
                logger.error(ex);
            }
        }

        return w;
    }
}
