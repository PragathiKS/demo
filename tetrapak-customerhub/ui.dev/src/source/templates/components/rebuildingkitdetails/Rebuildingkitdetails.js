import $ from 'jquery';
import 'bootstrap';
import { render } from '../../../scripts/utils/render';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';


/**
 * Fetch and render the Rebuilding kit Details
 */

function _renderRebuildingKitDetailsBottom() {
  const $this = this;
  const { $rebuildingData } = $this.cache;
  const { i18nKeys } = $this.cache;


  render.fn({
    template: 'rebuildingkitDetailsBottom',
    target: this.cache.$contenbottom,
    data: { i18nKeys: i18nKeys, rebuildingData: $rebuildingData}
  });
}

function _renderRebuildingKitDetails() {
  const $this = this;
  const { $rebuildingData } = $this.cache;
  const { i18nKeys } = $this.cache;


  render.fn({
    template: 'rebuildingkitDetails',
    target: $this.cache.$content,
    data: { i18nKeys: i18nKeys, rebuildingData: $rebuildingData}
  });
}

function _getRebuildingKitDetails() {
  const $this = this;
  auth.getToken(({ data: authData }) => {
    ajaxWrapper
      .getXhrObj({
        url:'https://api-dev.tetrapak.com/installedbase/rebuildingkits?rknumbers=1284002-0781&equipmentnumber=9060000022',
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Bearer ${authData.access_token}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        showLoader: true
      }).done(res => {
        $this.cache.$rebuildingData = res.data[0];
        $this.renderRebuildingKitDetails();
        $this.renderRebuildingKitDetailsBottom();
      }).fail((e) => {
        logger.error(e);
      });
  });
}

class Rebuildingkitdetails {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.configJson = this.root.find('.js-rebuilding-details__config').text();
    this.cache.rebuildingdetailsApi = this.root.data('rebuilding-details-api');
    this.cache.$content = this.root.find('.js-rebuilding-details__content');
    this.cache.$contenbottom = this.root.find('.js-rebuilding-details__contentbottom');
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
  }
  getRebuildingKitDetails() {
    return _getRebuildingKitDetails.apply(this, arguments);
  }
  renderRebuildingKitDetails() {
    return _renderRebuildingKitDetails.apply(this, arguments);
  }
  renderRebuildingKitDetailsBottom() {
    return _renderRebuildingKitDetailsBottom.apply(this, arguments);
  }

  init() {
    /* Mandatory method */
    this.initCache();
    this.getRebuildingKitDetails();
  }
}

export default Rebuildingkitdetails;
