import $ from 'jquery';
import PlantMasterLicensesEngineering from './PlantMasterLicenses-engineering';
import PlantMasterLicensesSite from './PlantMasterLicenses-site';

class PlantMasterLicenses {
  constructor({ el }) {
    this.root = $(el);
    this.$engineeringLicensesEl = this.root.find('#nav-engineering-licenses');
    this.$siteLicensesEl = this.root.find('#nav-site-licenses');
    this.pmLicensesEngineering = new PlantMasterLicensesEngineering(this.$engineeringLicensesEl);
    this.pmLicensesSite = new PlantMasterLicensesSite(this.$siteLicensesEl);
  }

  cache = {};

  init() {
    this.pmLicensesEngineering.init();
    this.pmLicensesSite.init();
  }
}

export default PlantMasterLicenses;
