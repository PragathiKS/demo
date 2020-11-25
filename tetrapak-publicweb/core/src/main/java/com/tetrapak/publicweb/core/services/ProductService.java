package com.tetrapak.publicweb.core.services;

import java.util.List;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;

/**
 * API GEE Service class.
 */
public interface ProductService {

    /**
     * Creates the or update product filling machine.
     *
     * @param resolver
     *            the resolver
     * @param session
     *            the session
     * @param productType
     *            the product type
     * @param fillingMachines
     *            the filling machines
     * @param langauge
     *            the langauge
     * @return the list
     */
    List<String> createOrUpdateProductFillingMachine(ResourceResolver resolver, Session session, String productType,
            List<FillingMachine> fillingMachines, String langauge);

    /**
     * Creates the or update product package type.
     *
     * @param resolver
     *            the resolver
     * @param session
     *            the session
     * @param productType
     *            the product type
     * @param packageTypes
     *            the package types
     * @param langauge
     *            the langauge
     * @return the list
     */
    List<String> createOrUpdateProductPackageType(ResourceResolver resolver, Session session, String productType,
            List<Packagetype> packageTypes, String langauge);

    /**
     * Creates the or update product processing equipement.
     *
     * @param resolver
     *            the resolver
     * @param session
     *            the session
     * @param productType
     *            the product type
     * @param equipements
     *            the equipements
     * @param langauge
     *            the langauge
     * @return the list
     */
    List<String> createOrUpdateProductProcessingEquipement(ResourceResolver resolver, Session session,
            String productType, List<ProcessingEquipement> equipements, String langauge);
}
