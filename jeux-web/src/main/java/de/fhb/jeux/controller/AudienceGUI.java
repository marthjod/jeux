package de.fhb.jeux.controller;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.session.GameLocal;
import de.fhb.jeux.session.GroupLocal;
import de.fhb.jeux.session.RankingLocal;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Stateless
@Path("/gui/audience")
public class AudienceGUI {

    protected static Logger logger = Logger.getLogger(AudienceGUI.class);

    private final String TEMPLATE_PREFIX = "/WEB-INF/templates/";
    private final String TEMPLATE_SUFFIX = ".html";

    @Context
    HttpServletRequest request;

    @EJB
    private GroupLocal groupBean;

    @EJB
    private GameLocal gameBean;

    @EJB
    private RankingLocal playerRankingBean;

    private PebbleTemplate getTemplate(ServletContext ctx, String templateName) {
        PebbleTemplate compiledTemplate = null;
        Loader templateLoader = new ServletLoader(ctx);
        templateLoader.setPrefix(TEMPLATE_PREFIX);
        templateLoader.setSuffix(TEMPLATE_SUFFIX);
        PebbleEngine engine = new PebbleEngine(templateLoader);
        engine.setStrictVariables(true);
        try {
            compiledTemplate = engine.getTemplate(templateName);
        } catch (PebbleException ex) {
            logger.warn(ex);
        }
        return compiledTemplate;
    }

    private Writer renderTemplate(PebbleTemplate template, Map<String, Object> context) {
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

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String getOverview(@Context ServletContext servletContext) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = getTemplate(servletContext, "overview");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("groups", groupBean.getAllGroupDTOs());
            writer = renderTemplate(compiledTemplate, context);
        }

        return writer.toString();
    }

    @GET
    @Path("/rankings/group/{groupId}")
    @Produces(MediaType.TEXT_HTML)
    public String getRankingsInGroup(@Context ServletContext servletContext, @PathParam("groupId") int groupId) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = getTemplate(servletContext, "rankings");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<String, Object>();
            IGroup group = groupBean.getGroupById(groupId);

            if (group != null) {
                context.put("groupName", group.getName());
                context.put("rankings", playerRankingBean.getRankedPlayerDTOs(group));
            } else {
                context.put("groupName", "Group not found");
                context.put("rankings", new ArrayList());
            }
            writer = renderTemplate(compiledTemplate, context);
        }

        return writer.toString();
    }

    @GET
    @Path("/results/group/{groupId}")
    @Produces(MediaType.TEXT_HTML)
    public String getResultsInGroup(@Context ServletContext servletContext,
            @PathParam("groupId") int groupId) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = getTemplate(servletContext, "results");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<String, Object>();
            IGroup group = groupBean.getGroupById(groupId);

            if (group != null) {
                context.put("groupName", group.getName());
                context.put("results", gameBean.getPlayedGameDTOsInGroup(group));
            } else {
                context.put("groupName", "Group not found");
                context.put("results", new ArrayList());
            }
            writer = renderTemplate(compiledTemplate, context);
        }

        return writer.toString();
    }

}
