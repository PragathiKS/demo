import $ from 'jquery';
import { storageUtil } from '../../../scripts/common/common';
import * as pbi from 'powerbi-client';

/**
 * Renders power bi report
 */
function _renderPowerBIReport() {
  //const reportId='5d3c5f2a-b062-4ddb-ab09-4224fb845a99';
  //const accessToken='H4sIAAAAAAAEAB3St86kVgBA4Xf5WyyRhmRpC3IcMgzQkXOGgYvld_d6-1N9Ov_82CkY5rT4-fsHH6WdEK-z380CXgj5AzqohxNKkYbqEOoljV_jyKNPY-wvSRnHQtq_y_IetFIBTBSiuBQ16sADi_K9Y2_exZV7CJx0SWzr4zPKODqxGtzvkk7wZboidO4bD9PEjovbV5mootHQ93gEeBGiphyl3epIrBUR-oKZAnWDaKRFPB-OS5ExTou15AXZ17p9ajHBEJUpQmzF9agaus_nUlNBInjMQhEurMQi1ssJEVravLej9Xe_shoxLefVGiZkCcyoJVYedXZghQHgG_-WMTOzenl-Ywy9Ea6r2zyDT4azxnRtSI1lPDzNkN-hJEiXdWpc3ngEr3B76iQZ5phogWJtgNHXxo-TRl9-x8l-En_PgmU_GLxGppt1RTqm6hnW8IBWA443i1Nmgdil9h0FX72zvLxmTSVy7ihP3GtUtNK0xBoO5CYxa0rlzXsdXoLTMOWGVjcocDaq9B4ywE0uguYg-u02khwRMyzTtJCsu27PyJjJd2NQVJ4G69mNiTYOTk_fG8tOyjvl7EcTeeK16JJZem9M1ityrokUGsf-EjPH4uBv8BQeBb_DtKIwXZJcwdlIAOTApPsuDWf_7ra6r3XteIgG94MqOEhCnFW4PhryUU6G47_ENEJVFH7e07huimM4D3GScX0enBd3jkpisfQeaWM4IzgI-cDIcpY_lV6n7aAV54NteFyyjOoslDyeMUoUs2cyhzehuGm20j7PHy2AEUdf-mySrR2VKj7wzKbxPMMAKOvfzq-fv374DSzHrJfg9_pioPMAl1UEMtogaQtEYgA1GhM0WgumKV4Wvi0OdaB8v5OFA6PV17xnyODT8Qn8Rae8xctrwZX6fKkWZT402xaTtBYN0whIq6pdmHpVWe5CwQayRbITydUtSrpJk3eLstF-TI4ZxMKAobJvqPZpCMp1v87oS1g6jOoGgoW9yGg5hm8V6becJce1sa8yDILra2bEZQVni5H0_AEfVk7jLiW9vAp6TN6HJelcKYdyxpp7rsrDRGbxnehDBmEV3xcwW74ZlJULwo8bHmTqdNKXW2EHyOJ5P9B9BG7bz4MG6HOXptzrOCHH-d4hvbLQEo2SLSsfIORVt9nEoIF8S_qxGfnC_vrDDJam3NTwtzLpkiJqo58XWR_0y-dYewdR_afy2npKj3Mrf2cWutPq_PQN9Z5r6R0IMb70QEJ3sDZhxpRZTHEUrafhPXkpeh7VVtHqLWEebiview_eQgBrLjFqNC7StgJ64gueTDuQJTTtyDx59tUrR5fnD-SxY9Rnb0OYv-KESZkFJpgdi8pfy23du04lX2qul4ag1Cy2jaH2htUr_Or1c9NS6D5fQze9yrrQzYVMJd4NqX-w5eKbr5M627Wa7JnpI7vCyUM9nJJtt_XSvBc2KpmmAPuZQ_3jRGEi3NIxVD0wHQ8KrlrBUcdPUb9uEVovbDQMHcyo3znYgu1TITV1-i3fDhkOhTs-QtYItVqwCmhOsnGHTIM0Ys3YBtOAvyaW86VzVv5n_vc_UQFJFu4FAAA=.eyJjbHVzdGVyVXJsIjoiaHR0cHM6Ly9XQUJJLU5PUlRILUVVUk9QRS1FLVBSSU1BUlktcmVkaXJlY3QuYW5hbHlzaXMud2luZG93cy5uZXQiLCJleHAiOjE2ODc5Mzc1ODYsImFsbG93QWNjZXNzT3ZlclB1YmxpY0ludGVybmV0Ijp0cnVlfQ==';
  //storageUtil.setCookie('pbi-accesstoken',accessToken);
  const accessTokenPBI=storageUtil.getCookie('pbi-accesstoken');
  const embedConfiguration = {
    type: 'report',
    id: this.cache.reportId,
    embedUrl: this.cache.embedURL ,
    tokenType: pbi.models.TokenType.Embed,
    accessToken: accessTokenPBI
  };
  const $reportContainer = $('#reportContainer').get(0);
  const service = new pbi.service.Service(pbi.factories.hpmFactory, pbi.factories.wpmpFactory, pbi.factories.routerFactory);
  service.embed($reportContainer, embedConfiguration);
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
    this.cache.embedURL = this.root.data('embed-preurl')+this.cache.reportId;  
    this.renderPowerBIReport();
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
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default Embedpowerbireport;
