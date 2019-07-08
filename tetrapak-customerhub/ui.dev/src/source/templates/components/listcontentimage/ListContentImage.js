import $ from 'jquery';
import 'bootstrap';
import dynamicMedia from '../../../scripts/utils/dynamicMedia';
import { render } from '../../../scripts/utils/render';
import { trackAnalytics } from '../../../scripts/utils/analytics';

class ListContentImage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.$tabMenuItemLink = this.root.find('.js-list-content-image__tab-menu-list-item__link');
    this.cache.$tabMenuItem = this.root.find('.js-list-content-image__tab-menu-list-item');
    this.cache.$contentTabItems = this.root.find('.js-list-content-image__content-tab');
    this.cache.$contentWrapper = this.root.find('.js-list-content-image__content-wrapper');
  }
  bindEvents() {
    const self = this;
    this.root.on('click', '.js-list-content-image__tab-menu-list-item__link', function (e) {
      e.preventDefault();
      const $this = $(this);
      self.setActiveTab.apply(this, [self]);
      dynamicMedia.processImageAttributes();
      // Handle analytics
      trackAnalytics({
        linkType: 'internal',
        linkSection: 'tabListText',
        linkParentTitle: $this.data('parentTitle'),
        linkName: $this.data('linkName'),
        linkListPos: $this.data('tabCount')
      }, 'linkClick', 'linkClicked', undefined, false);
    });
  }
  setActiveTab($this) {
    const self = $(this);
    const { $tabMenuItem, $tabMenuItemLink, $contentTabItems, $contentWrapper } = $this.cache;
    const tabId = self.data('tabId');
    $tabMenuItem.removeClass('active');
    $tabMenuItemLink.removeClass('active');
    $tabMenuItem.filter(`[data-tab-id="${tabId}"]`).addClass('active');
    $tabMenuItemLink.filter(`[data-tab-id="${tabId}"]`).addClass('active');
    $contentTabItems.removeClass('active');
    const thisHTML = $contentTabItems.filter(`[data-tab-id="${tabId}"]`).addClass('active')[0].outerHTML;
    render.fn({
      template: 'listRteContent',
      target: $contentWrapper,
      data: {
        content: thisHTML
      }
    });
  }
  renderRTE() {
    const { $contentTabItems, $contentWrapper } = this.cache;
    $contentTabItems.each((index, el) => {
      const $this = $(el);
      const tabId = $this.data('tabId');
      const thisHTML = $this[0].outerHTML;
      const target = this.root.find(`[data-section-id="${tabId}"]`);
      if (target.length) {
        render.fn({
          template: 'listRteContent',
          target,
          data: {
            content: thisHTML
          }
        }, () => {
          target.find('.js-list-content-image__content-tab').addClass('active');
        });
        // Desktop render
        if (index === 0) {
          render.fn({
            template: 'listRteContent',
            target: $contentWrapper,
            data: {
              content: thisHTML
            }
          });
        }
      }
    });
  }
  init() {
    this.initCache();
    this.bindEvents();
    this.renderRTE();
  }
}

export default ListContentImage;
