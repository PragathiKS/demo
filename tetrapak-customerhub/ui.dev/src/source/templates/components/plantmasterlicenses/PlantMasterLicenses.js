import $ from 'jquery';
import PlantMasterLicensesEngineering from './PlantMasterLicenses-engineering';
import PlantMasterLicensesSite from './PlantMasterLicenses-site';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';


class PlantMasterLicenses {
  constructor({ el }) {
    this.root = $(el);
    this.$engineeringLicensesEl = this.root.find('#nav-engineering-licenses');
    this.$siteLicensesEl = this.root.find('#nav-site-licenses');
  }

  cache = {};

  init() {
    
    ajaxWrapper.getXhrObj({
      url: this.root.data('group-servlet-url'),
      method: ajaxMethods.GET,
      cache: true,
      async: false,
      dataType: 'json',
      contentType: 'application/json',
      beforeSend(jqXHR) {
        jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      },
      showLoader: true
    }).done(res => {
      const userGroup = res.groups;
      const siteLicenseUerGroup = [];
      const engLicenseUerGroup = [];
      if(userGroup){
        userGroup.forEach(group => {
          if(group.siteLicenseId === '' || group.siteLicenseId !== undefined){
            siteLicenseUerGroup.push(group.siteLicenseId);
          }
          if(group.engLicenseId === '' || group.engLicenseId !== undefined){
            engLicenseUerGroup.push(group.engLicenseId);
          }
        });
      }

      this.pmLicensesEngineering = new PlantMasterLicensesEngineering(this.$engineeringLicensesEl,engLicenseUerGroup);
      this.pmLicensesSite = new PlantMasterLicensesSite(this.$siteLicensesEl,siteLicenseUerGroup);

      this.pmLicensesEngineering.init();
      this.pmLicensesSite.init();

    }).fail((e) => {
      logger.error(e);
    });    
  }
}

export default PlantMasterLicenses;
