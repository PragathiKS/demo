package com.tetralaval.services;

import java.util.List;

import com.tetralaval.beans.Dropdown;

public interface FormDatasourceConfigService {

    List<Dropdown> getActionTypes();
    
    List<Dropdown> getEmailTemplates();

}
