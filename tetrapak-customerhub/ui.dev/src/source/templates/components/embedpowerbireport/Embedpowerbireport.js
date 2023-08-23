import $ from 'jquery';
import { storageUtil } from '../../../scripts/common/common';
import * as pbi from 'powerbi-client';

/**
 * Renders power bi report
 */
function _renderPowerBIReport() {
  const accessTokenPBI=storageUtil.getCookie('pbi-accesstoken');
  const embedConfiguration = {
    type: 'report',
    id: this.cache.reportId,
    embedUrl: this.cache.embedURL ,
    tokenType: pbi.models.TokenType.Embed,
    accessToken: accessTokenPBI
  };
  const $reportContainer = $('#reportContainer').get(0);
  this.cache.service = new pbi.service.Service(pbi.factories.hpmFactory, pbi.factories.wpmpFactory, pbi.factories.routerFactory);
  this.cache.service.embed($reportContainer, embedConfiguration);
}
class Embedpowerbireport {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    /**
     * Use "this.root" to find elements within current component
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
    this.cache.reportId = this.root.data('report-id');
    this.cache.embedURL = this.root.data('embed-url');
  }
  renderPowerBIReport() {
    return _renderPowerBIReport.apply(this, arguments);
  }  
  bindEvents() {
    /* Bind jQuery events here */
    /**
     * Example:
     * const { $submitBtn } = this.cache;
     * $submitBtn.on('click', () => { ... });
     */
    this.renderPowerBIReport();   
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Embedpowerbireport;
