package de.fhb.jeux.controller;

import com.mitchellbosecke.pebble.template.PebbleTemplate;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.session.GameLocal;
import de.fhb.jeux.session.GroupLocal;
import de.fhb.jeux.session.PlayerLocal;
import de.fhb.jeux.session.RoundSwitchRuleLocal;
import de.fhb.jeux.bulkimport.ImportGroup;
import de.fhb.jeux.bulkimport.ImportRule;
import de.fhb.jeux.bulkimport.JSONImporter;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.dto.RoundSwitchRuleDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.session.CreateGroupLocal;
import de.fhb.jeux.session.CreatePlayerLocal;
import de.fhb.jeux.session.CreateRoundSwitchRuleBean;
import de.fhb.jeux.session.CreateRoundSwitchRuleLocal;
import de.fhb.jeux.template.Template;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Stateless
@Path("/gui/admin")
public class AdminGUI {

    protected static Logger logger = Logger.getLogger(AdminGUI.class);

    @EJB
    private GroupLocal groupBean;

    @EJB
    private PlayerLocal playerBean;

    @EJB
    private GameLocal gameBean;

    @EJB
    private CreateGroupLocal createGroupBean;

    @EJB
    private CreatePlayerLocal createPlayerBean;

    @EJB
    private RoundSwitchRuleLocal roundSwitchRuleBean;

    @EJB
    private CreateRoundSwitchRuleLocal createRuleBean;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String startView(@Context ServletContext servletContext) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "admin-start");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("prefix", "/" + servletContext.getServletContextName());
            writer = Template.renderTemplate(compiledTemplate, context);
        }
        return writer.toString();
    }

    @GET
    @Path("/groups")
    @Produces(MediaType.TEXT_HTML)
    public String editGroups(@Context ServletContext servletContext) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "admin-groups");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("prefix", "/" + servletContext.getServletContextName());
            context.put("groups", groupBean.getAllGroupDTOs());
            writer = Template.renderTemplate(compiledTemplate, context);
        }
        return writer.toString();
    }

    @GET
    @Path("/players")
    @Produces(MediaType.TEXT_HTML)
    public String editPlayers(@Context ServletContext servletContext) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "admin-players");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("prefix", "/" + servletContext.getServletContextName());
            context.put("groups", groupBean.getAllGroupDTOs());
            context.put("players", playerBean.getAllPlayerDTOs());
            writer = Template.renderTemplate(compiledTemplate, context);
        }
        return writer.toString();
    }

    @GET
    @Path("/rules")
    @Produces(MediaType.TEXT_HTML)
    public String editRules(@Context ServletContext servletContext) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "admin-rules");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("prefix", "/" + servletContext.getServletContextName());
            context.put("groups", groupBean.getAllGroupDTOs());
            context.put("rules", roundSwitchRuleBean.getAllRoundSwitchRuleDTOs());
            writer = Template.renderTemplate(compiledTemplate, context);
        }
        return writer.toString();
    }

    @GET
    @Path("/games/unplayed")
    @Produces(MediaType.TEXT_HTML)
    public String getUnplayedGames(@Context ServletContext servletContext) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "admin-unplayed-games");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("prefix", "/" + servletContext.getServletContextName());

            List<Object> gamesByGroup = new ArrayList<>();
            for (GroupDTO group : groupBean.getAllGroupDTOs()) {
                Map<String, Object> games = new HashMap<>();
                games.put("group", group);
                games.put("games", gameBean.getUnplayedGameDTOsInGroup(
                        groupBean.getGroupById(group.getId())));
                gamesByGroup.add(games);
            }
            context.put("gamesByGroup", gamesByGroup);
            writer = Template.renderTemplate(compiledTemplate, context);
        }
        return writer.toString();
    }

    @GET
    @Path("/games/played")
    @Produces(MediaType.TEXT_HTML)
    public String getPlayedGames(@Context ServletContext servletContext) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "admin-played-games");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("prefix", "/" + servletContext.getServletContextName());

            List<Object> gamesByGroup = new ArrayList<>();
            for (GroupDTO group : groupBean.getAllGroupDTOs()) {
                Map<String, Object> games = new HashMap<>();
                games.put("group", group);
                games.put("games", gameBean.getPlayedGameDTOsInGroup(
                        groupBean.getGroupById(group.getId())));
                gamesByGroup.add(games);
            }
            context.put("gamesByGroup", gamesByGroup);
            writer = Template.renderTemplate(compiledTemplate, context);
        }
        return writer.toString();
    }

    @POST
    @Path("/groups/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importGroups(@Context HttpServletRequest request,
            MultipartFormDataInput input) {
        Response response = Response.serverError().build();
        URI returnUrl = getReturnUrl(request);
        List<ImportGroup> importGroups = new ArrayList<>();

        BufferedReader bufreader = getReaderFromFormData(input);
        try {
            if (!bufreader.ready()) {
                response = Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        importGroups = JSONImporter.importGroupsFromJson(bufreader);

        if (saveImportGroups(importGroups)) {
            response = Response.seeOther(returnUrl).build();
        }

        return response;
    }

    @POST
    @Path("/rules/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importRules(@Context HttpServletRequest request,
            MultipartFormDataInput input) {
        Response response = Response.serverError().build();
        URI returnUrl = getReturnUrl(request);
        List<ImportRule> importRules = new ArrayList<>();

        BufferedReader bufreader = getReaderFromFormData(input);
        try {
            if (!bufreader.ready()) {
                response = Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        importRules = JSONImporter.importRulesFromJson(bufreader);

        if (saveImportRules(importRules)) {
            response = Response.seeOther(returnUrl).build();
        }

        return response;
    }

    private boolean saveImportGroups(List<ImportGroup> importGroups) {
        boolean success = false;

        if (importGroups != null) {
            for (ImportGroup ig : importGroups) {
                GroupDTO groupDTO = new GroupDTO(ig.getName(),
                        ig.getMinSets(), ig.getMaxSets(),
                        ig.getRoundId(), ig.isActive(), ig.isCompleted());
                int groupId = createGroupBean.createGroup(groupDTO);
                IGroup group = groupBean.getGroupById(groupId);
                for (String playerName : ig.getPlayers()) {
                    PlayerDTO player = new PlayerDTO(playerName);
                    createPlayerBean.createPlayer(player, group);
                }
            }
            success = true;
        }
        return success;
    }

    private boolean saveImportRules(List<ImportRule> importRules) {
        boolean success = false;

        if (importRules != null) {
            for (ImportRule ir : importRules) {
                RoundSwitchRuleDTO ruleDTO = new RoundSwitchRuleDTO(
                        ir.getSrcGroupName(), ir.getDestGroupName(),
                        ir.getStartWithRank(), ir.getAdditionalPlayers());
                logger.debug(ruleDTO);
                int status = createRuleBean.createRoundSwitchRule(ruleDTO,
                        groupBean.getGroupByName(ir.getSrcGroupName()),
                        groupBean.getGroupByName(ir.getDestGroupName()));
                if (status == CreateRoundSwitchRuleBean.STATUS_OK) {
                    success = true;
                }
            }
        }

        return success;
    }

    private BufferedReader getReaderFromFormData(MultipartFormDataInput input) {
        BufferedReader bufreader = null;

        Map<String, List<InputPart>> formParts = input.getFormDataMap();
        List<InputPart> inPart = formParts.get("file");
        if (inPart != null && inPart.size() > 0) {
            for (InputPart inputPart : inPart) {
                try {
                    InputStream instream = inputPart.getBody(InputStream.class, null);
                    bufreader = new BufferedReader(new InputStreamReader(instream));
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
            }
        }

        return bufreader;
    }

    private URI getReturnUrl(HttpServletRequest request) {
        URI returnUrl = null;
        try {
            String uri = request.getRequestURI();
            returnUrl = new URI(uri.substring(request.getContextPath().length(),
                    uri.lastIndexOf("/")));
        } catch (URISyntaxException ex) {
            logger.error(ex.getMessage());
        }
        return returnUrl;
    }
}
