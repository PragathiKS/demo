package com.tetrapak.publicweb.core.msm;

import java.util.Map;
import java.util.Objects;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.ActionConfig;
import com.day.cq.wcm.msm.api.LiveAction;
import com.day.cq.wcm.msm.api.LiveActionFactory;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.config.PWLiveActionFactoryConfig;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * A factory for creating PWNewsReferencesUpdate objects.
 */
@Component(service = LiveActionFactory.class, name = "PW News References Update Factory", immediate = true,
property = {
        LiveActionFactory.LIVE_ACTION_NAME + "=pwUpdateReferrences"})
@Designate(ocd = PWLiveActionFactoryConfig.class)
public class PWNewsReferencesUpdateFactory implements LiveActionFactory<LiveAction>  {
   
    /** The config. */
    private PWLiveActionFactoryConfig config;

    /**
     * Creates a new PWNewsReferencesUpdate object.
     *
     * @return the string
     */
    /*
     * (non-Javadoc)
     * @see com.day.cq.wcm.msm.api.LiveActionFactory#createsAction()
     */
    @Override
    public String createsAction() {
        return config.actionName();
    }

    /**
     * Creates a new PWNewsReferencesUpdate object.
     *
     * @param resource the resource
     * @return the live action
     * @throws WCMException the WCM exception
     */
    /*
     * (non-Javadoc)
     * @see com.day.cq.wcm.msm.api.LiveActionFactory#createAction(org.apache.sling.api.resource.Resource)
     */
    @Override
    public LiveAction createAction(final Resource resource) throws WCMException {
        return new PWUpdateReferencesRolloutAction();
    }
    
    /**
     * activate method.
     *
     * @param config
     *            PW Live Action Factory configuration
     */
    @Activate
    public void activate(final PWLiveActionFactoryConfig config) {

        this.config = config;
    }

    // ======= Live action class

    /**
     * The Class PWUpdateReferencesRolloutAction.
     */
    public static class PWUpdateReferencesRolloutAction implements LiveAction {

        /**
         * Execute.
         *
         * @param arg0 the arg 0
         * @param arg1 the arg 1
         * @param arg2 the arg 2
         * @param arg3 the arg 3
         * @throws WCMException the WCM exception
         */
        @SuppressWarnings("deprecation")
        @Override
        public void execute(ResourceResolver arg0, LiveRelationship arg1, ActionConfig arg2, boolean arg3)
                throws WCMException {
            throw new UnsupportedOperationException();
        }

        /**
         * Execute.
         *
         * @param source the source
         * @param target the target
         * @param liveRelationship the live relationship
         * @param autoSave the auto save
         * @param isResetRollout the is reset rollout
         * @throws WCMException the WCM exception
         */
        @Override
        public void execute(final Resource source, final Resource target, final LiveRelationship liveRelationship,
                final boolean autoSave, final boolean isResetRollout)
                throws WCMException {
            if(Objects.nonNull(target)) {
                String rootPathSource = LinkUtils.getRootPath(source.getPath().substring(0,source.getPath().indexOf("/jcr:content")));
                String rootPathTarget = LinkUtils.getRootPath(target.getPath().substring(0,target.getPath().indexOf("/jcr:content")));
                String newsArchiveSourcePath = rootPathSource + PWConstants.NEW_ARCHIVE_REL_PATH;
                String newsArchiveTargetPath = rootPathTarget + PWConstants.NEW_ARCHIVE_REL_PATH;
                ModifiableValueMap map = target.adaptTo(ModifiableValueMap.class);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    processEntity(map, entry, newsArchiveSourcePath, newsArchiveTargetPath);
                }
            }
            
        }
        
        /**
         * Process entity.
         *
         * @param map the map
         * @param entry the entry
         * @param newsArchiveSourcePath the news archive source path
         * @param newsArchiveTargetPath the news archive target path
         */
        private void processEntity(ModifiableValueMap map, Map.Entry<String, Object> entry, String newsArchiveSourcePath, String newsArchiveTargetPath) {
            if((entry.getValue() instanceof String && entry.getValue().toString().contains("/news-and-events/newsarchive/")) || (entry.getValue() instanceof String[]) && String.join(",", (String[])entry.getValue()).contains("/news-and-events/newsarchive/")) {                     
                if(entry.getValue() instanceof String) {
                    String value =  entry.getValue().toString();
                    value = value.replaceAll(newsArchiveSourcePath, PWConstants.NEW_ARCHIVE_GLOBAL_PATH);
                    value = value.replaceAll(newsArchiveTargetPath, PWConstants.NEW_ARCHIVE_GLOBAL_PATH);
                    map.put(entry.getKey(), value);
                }
                if(entry.getValue() instanceof String[]) {
                    String value =  String.join(",",(String[])entry.getValue());
                    value = value.replaceAll(newsArchiveSourcePath, PWConstants.NEW_ARCHIVE_GLOBAL_PATH);
                    value = value.replaceAll(newsArchiveTargetPath, PWConstants.NEW_ARCHIVE_GLOBAL_PATH);                         
                    map.put(entry.getKey(), value.split(","));
                }
            }
        }

        /**
         * Execute.
         *
         * @param arg0 the arg 0
         * @param arg1 the arg 1
         * @param arg2 the arg 2
         * @param arg3 the arg 3
         * @param arg4 the arg 4
         * @throws WCMException the WCM exception
         */
        @Override
        public void execute(ResourceResolver arg0, LiveRelationship arg1, ActionConfig arg2, boolean arg3, boolean arg4)
                throws WCMException {
            throw new UnsupportedOperationException();        
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        @Override
        public String getName() {
            return "pwUpdateReferrences";
        }

        /**
         * Gets the parameter name.
         *
         * @return the parameter name
         */
        @Override
        public String getParameterName() {
            return null;
        }

        /**
         * Gets the properties names.
         *
         * @return the properties names
         */
        @Override
        public String[] getPropertiesNames() {
            return new String[0];
        }

        /**
         * Gets the rank.
         *
         * @return the rank
         */
        @Override
        public int getRank() {
            return 0;
        }

        /**
         * Gets the title.
         *
         * @return the title
         */
        @Override
        public String getTitle() {
            return null;
        }

        /**
         * Write.
         *
         * @param arg0 the arg 0
         * @throws JSONException the JSON exception
         */
        @SuppressWarnings("deprecation")
        @Override
        public void write(JSONWriter arg0) throws JSONException {
            throw new UnsupportedOperationException();
            
        }

    }
}

