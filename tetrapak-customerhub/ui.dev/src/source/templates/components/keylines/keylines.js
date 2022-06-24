import $ from 'jquery';
import {render} from '../../../scripts/utils/render';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {ajaxMethods} from '../../../scripts/utils/constants';
import {logger} from '../../../scripts/utils/logger';

class Keylines {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};

  initCache() {
    this.cache.$downloadLink = this.root.find('.js-tp-keylines__download');
    this.cache.$keyLinesModal = this.root.find('.js-tp-keylines__modal');
    this.cache.apiUrl = this.root.data('apiUrl');
    this.cache.packageType = this.root.data('packagetype');
  }

  renderModal(targetShape) {
    const { $keyLinesModal, apiUrl, packageType } = this.cache;

    ajaxWrapper
      .getXhrObj({
        url: `${apiUrl}?type=${packageType}&shapes=${targetShape}`,
        method: ajaxMethods.GET,
        cache: true,
        dataType: 'json',
        contentType: 'application/json'
      }).done(res => {
        logger.log(res);
      }).fail((e) => {
        logger.error(e);
      });

    const openings = [];
    const volumes = [];

    render.fn({
      template: 'keyLinesModal',
      target: '.js-tp-keylines__modal',
      data: {
        assets: [],
        openings,
        volumes
      }
    });

    $keyLinesModal.modal();
  }

  bindEvents() {
    this.cache.$downloadLink.on('click', (e) => {
      const $btn = $(e.currentTarget);
      const targetShape = $btn.data('shape');
      this.renderModal(targetShape);
    });
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Keylines;
