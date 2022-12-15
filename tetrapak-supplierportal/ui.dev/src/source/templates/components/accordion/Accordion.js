import $ from 'jquery';
import 'bootstrap';
import { logger } from '../../../scripts/utils/logger';

function _renderFirstTab() {
  const { componentId } = this.cache;
  const $tabSection = this.root.find('.js-accordion__events-sidesection');
  $tabSection.html($(`#tab_${componentId}_0`).html());
}

class Accordion {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.selectedTabIndex = 0;
    this.cache.componentId = this.root.find('#tabComponentId').val();
    this.cache.heading = $.trim(this.root.find('.js-accordion__heading').text());
  }
  bindEvents() {
    const $this = this;
    logger.log('bind evnt');

    this.root
      .on('click', '.js-accordion__event', function () {
        logger.log('clicked');
        const self = $(this);
        if (!self.hasClass('active')) {
          $this.showTabDetail(self.data('target'));
        }
        $this.root.find('.js-accordion__event').removeClass('active');
        self.addClass('active').toggleClass('m-active');
      })

      .on('hidden.bs.collapse', '.collapse', this.pauseVideoIfExists);
  }
  showTabDetail = (el) => {
    const $tabSection = this.root.find('.js-accordion__events-sidesection');
    $tabSection.html($(el).html());
  }
  renderFirstTab = () => _renderFirstTab.call(this);

  init() {
    this.initCache();
    this.bindEvents();
    this.renderFirstTab();
  }
}

export default Accordion;
