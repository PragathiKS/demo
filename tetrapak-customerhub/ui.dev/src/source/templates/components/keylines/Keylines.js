import $ from 'jquery';
import {render} from '../../../scripts/utils/render';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';

function _replaceLabel(label, value) {
  return label.replace('{}', value);
}

class Keylines {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.configJson = this.root.find('.js-keylines__config').text();
    try {
      this.cache.i18nKeys = JSON.parse(this.cache.configJson);
    } catch (e) {
      this.cache.i18nKeys = {};
      logger.error(e);
    }
    this.cache.$keyLinesModal = this.root.find('.js-tp-keylines__modal');
    this.cache.apiUrl = this.root.data('apiUrl');
    this.cache.packageType = this.root.data('packagetype');
    this.cache.keylinesData = {};
  }

  renderModal(shapeName, shapeTitle) {
    const { $keyLinesModal, keylinesData, i18nKeys } = this.cache;
    const { shapes, assets } = keylinesData;
    const openings = shapes[0].openings.map(item => ({ key: item.key, desc: item.value }));
    const volumes = shapes[0].volumes.map(item => ({ key: item.key, desc: item.value }));
    const updatedi18nKeys = {...i18nKeys};
    updatedi18nKeys.modalTitle = _replaceLabel(i18nKeys.modalTitle, shapeTitle);

    render.fn({
      template: 'keyLinesModal',
      target: '.js-tp-keylines__modal',
      data: {
        assets,
        openings,
        volumes,
        shapeName,
        i18nKeys: updatedi18nKeys
      }
    });

    $keyLinesModal.modal();
  }

  getShapeAssets(shapeTag, shapeName, shapeTitle) {
    const { apiUrl, packageType } = this.cache;

    ajaxWrapper
      .getXhrObj({
        url: `${apiUrl}?type=${packageType}&shapes=${shapeTag}`,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json'
      }).done(res => {
        this.cache.keylinesData = res;
        this.renderModal(shapeName, shapeTitle);
      }).fail((e) => {
        logger.error(e);
      });
  }

  setDownloadBtn() {
    const { $keyLinesModal, keylinesData } = this.cache;
    const { assets } = keylinesData;
    const $downloadBtn = $keyLinesModal.find('.js-tp-keylines__modal-download');
    const shapeName = $downloadBtn.data('shape-name');
    const opening = $keyLinesModal.find('.js-tp-keylines__openings').val();
    const volume = $keyLinesModal.find('.js-tp-keylines__volumes').val();
    let isValid = true;

    if (opening === '' || volume === '') {
      isValid = false;
      $downloadBtn.attr('disabled', 'disabled');
    }

    if (isValid) {
      const downloadKey = `${opening}_${volume}_${shapeName}`;
      const assetObj = assets.find(asset => asset.keyname === downloadKey);

      if (assetObj) {
        const { assetpath } = assetObj;
        $downloadBtn.attr('onclick', `window.location.href='${assetpath}'`);
        $downloadBtn.removeAttr('disabled');
      } else {
        $downloadBtn.removeAttr('onclick');
        $downloadBtn.attr('disabled', 'disabled');
      }
    }
  }

  bindEvents() {
    this.root.on('click', '.js-tp-keylines__download', e => {
      const $btn = $(e.currentTarget);
      const shapeTag = $btn.data('shape');
      const shapeName = $btn.data('shape-name');
      const shapeTitle = $btn.data('shape-title');

      this.getShapeAssets(shapeTag, shapeName, shapeTitle);
    });

    this.root.on('change', '.js-tp-keylines__dropdown', e => {
      const $select = $(e.currentTarget);
      this.setDownloadBtn($select);
    });

    this.root.on('click', '.js-close-btn', () => {
      const { $keyLinesModal } = this.cache;
      $keyLinesModal.modal('hide');
    });
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Keylines;
