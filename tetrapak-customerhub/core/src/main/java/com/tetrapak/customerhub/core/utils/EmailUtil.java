package com.tetrapak.customerhub.core.utils;

import com.tetrapak.customerhub.core.jobs.MyTetrapakEmailJob;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.event.jobs.JobManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for Email Jobs related methods
 *
 * @author Pawan Kumar
 */
public final class EmailUtil {
    private static final String HIDE_SUFFIX = "HideClass";
    private static final String HIDE_CSS_CLASS = "hide";
    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

    private EmailUtil() {
        //adding private constructor
    }

    /**
     * This is to add hide class in email template as per key value
     * @param key to be used in email template
     * @param value to be displayed in email template
     * @return map string
     */
    public static Map<String, String> cssHideIfEmpty(final String key, final String value) {
        Map<String, String> entry = new HashMap<>();
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            entry.put(key+HIDE_SUFFIX,HIDE_CSS_CLASS);
        } else {
            entry.put(key+HIDE_SUFFIX,org.apache.commons.lang3.StringUtils.EMPTY);
        }
        return entry;
    }

    /**
     * Create sling job to send email.
     * @param emailParams
     * @param templatePath
     * @return
     */
    public static boolean addEmailJob(Map<String, String> emailParams, String[] emailRecipient, String templatePath, JobManager jobMgr) {
        boolean isSuccess = false;
        Map<String, Object> properties = new HashMap<>();
        properties.put(MyTetrapakEmailJob.TEMPLATE_PATH, templatePath);
        properties.put(MyTetrapakEmailJob.EMAIL_PARAMS, emailParams);
        properties.put(MyTetrapakEmailJob.RECIPIENTS_ARRAY, emailRecipient);
        LOGGER.debug(" Email properties : Template path = {}, Recipients = {}", templatePath,
                ArrayUtils.toString(emailRecipient));
        for (Map.Entry<String, String> entry : emailParams.entrySet()) {
            LOGGER.debug("Email Parameters '{}' = {}", entry.getKey(), entry.getValue());
        }
        if (jobMgr != null && emailRecipient != null) {
            jobMgr.addJob(MyTetrapakEmailJob.JOB_TOPIC_NAME, properties);
            isSuccess = true;
            LOGGER.debug("Email job added");
        } else {
            LOGGER.error("Error in setting up pre-requisites for Email job.");
        }
        return isSuccess;
    }
}
