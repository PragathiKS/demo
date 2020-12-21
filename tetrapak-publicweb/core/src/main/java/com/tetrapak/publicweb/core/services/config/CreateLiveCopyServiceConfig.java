package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface CreateLiveCopyServiceConfig.
 */
@ObjectClassDefinition(name = "Public Web Create Live Copy Config", description = "Public Web Create Live Copy Config")
public @interface CreateLiveCopyServiceConfig {

    /**
     * Enable config.
     *
     * @return true, if successful
     */
    @AttributeDefinition(
            name = "Enable automatic rollout and activate",
            description = "Enable automatic rollout and activate",
            type = AttributeType.BOOLEAN)
    boolean enableConfig() default false;

    /**
     * Gets the english live copy base paths.
     *
     * @return the english live copy base paths
     */
    @AttributeDefinition(name = "English Live Copy Base Paths", description = "English Live Copy Base Paths")
    String[] getEnglishLiveCopyBasePaths();

    /**
     * Gets the french live copy base paths.
     *
     * @return the french live copy base paths
     */
    @AttributeDefinition(name = "French Live Copy Base Paths", description = "French Live Copy Base Paths")
    String[] getFrenchLiveCopyBasePaths();

    /**
     * Gets the chinese live copy base paths.
     *
     * @return the chinese live copy base paths
     */
    @AttributeDefinition(name = "Chinese Live Copy Base Paths", description = "Chinese Live Copy Base Paths")
    String[] getChineseLiveCopyBasePaths();

    /**
     * Gets the german live copy base paths.
     *
     * @return the german live copy base paths
     */
    @AttributeDefinition(name = "German Live Copy Base Paths", description = "German Live Copy Base Paths")
    String[] getGermanLiveCopyBasePaths();

    /**
     * Gets the italian live copy base paths.
     *
     * @return the italian live copy base paths
     */
    @AttributeDefinition(name = "Italian Live Copy Base Paths", description = "Italian Live Copy Base Paths")
    String[] getItalianLiveCopyBasePaths();

    /**
     * Gets the japanese live copy base paths.
     *
     * @return the japanese live copy base paths
     */
    @AttributeDefinition(name = "Japanese Live Copy Base Paths", description = "Japanese Live Copy Base Paths")
    String[] getJapaneseLiveCopyBasePaths();

    /**
     * Gets the portugese live copy base paths.
     *
     * @return the portugese live copy base paths
     */
    @AttributeDefinition(name = "Portugese Live Copy Base Paths", description = "Portugese Live Copy Base Paths")
    String[] getPortugeseLiveCopyBasePaths();

    /**
     * Gets the russian live copy base paths.
     *
     * @return the russian live copy base paths
     */
    @AttributeDefinition(name = "Russian Live Copy Base Paths", description = "Russian Live Copy Base Paths")
    String[] getRussianLiveCopyBasePaths();

    /**
     * Gets the spanish live copy base paths.
     *
     * @return the spanish live copy base paths
     */
    @AttributeDefinition(name = "Spanish Live Copy Base Paths", description = "Spanish Live Copy Base Paths")
    String[] getSpanishLiveCopyBasePaths();

    /**
     * Gets the swedish live copy base paths.
     *
     * @return the swedish live copy base paths
     */
    @AttributeDefinition(name = "Swedish Live Copy Base Paths", description = "Swedish Live Copy Base Paths")
    String[] getSwedishLiveCopyBasePaths();

    /**
     * Gets the turkish live copy base paths.
     *
     * @return the turkish live copy base paths
     */
    @AttributeDefinition(name = "Turkish Live Copy Base Paths", description = "Turkish Live Copy Base Paths")
    String[] getTurkishLiveCopyBasePaths();

    /**
     * Gets the rollout configs.
     *
     * @return the rollout configs
     */
    @AttributeDefinition(name = "Rollout Configs", description = "Rollout Configs")
    String[] getRolloutConfigs();

}
