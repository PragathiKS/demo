package com.tetrapak.supplierportal.core.services;

public interface SupportRequestPurposesEmailsService {

	String[] getOnBoardingMaintananceEmail();

	String[] getSourcingContractingEmail();

	String[] getCataloguesEmail();

	String[] getOtherEmail();

}
