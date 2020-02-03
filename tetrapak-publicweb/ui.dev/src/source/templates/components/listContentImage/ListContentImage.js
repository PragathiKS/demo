import $ from 'jquery';
import { isDesktopMode, isMobileMode } from '../../../scripts/common/common';
import { dynMedia } from '../../../scripts/utils/dynamicMedia';

class ListContentImage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    const self = this;
    this.cache.$tabMenuItemLink = this.root.find('.pw-listContentImage__tabMenuListItem__link');
    this.cache.$tabMenuItem = this.root.find('.pw-listContentImage__tabMenuListItem');
    this.cache.$editTabItem = this.root.find('.pw-listContentImage__editTab');
    this.cache.$contentWrapper = this.root.find('.pw-listContentImage__contentWrapper');
    this.cache.$tabContent = this.root.find('.pw-listContentImage__contentTab');

    // Add Version Name to each instance
    this.root.each(function (index) {
      $(this).addClass(`listContentImage-version${index}`);
    });

    // Clone sall EditTab Content to the Content Wrapper
    $.each(this.cache.$editTabItem, function () {
      const $this = $(this);
      const tabID = $this.data('tab-id');
      const $clonedEditTabContent = $this.find('.pw-listContentImage__contentTab').clone();
      const $clonedEditTabContentMobile = $this.find('.pw-listContentImage__contentTab').clone();
      self.cache.$contentWrapper.append($clonedEditTabContent);
      $(`#${tabID}`).append($clonedEditTabContentMobile);
    });

  }
  bindEvents() {
    const self = this;
    const { $tabMenuItemLink } = this.cache;
    $tabMenuItemLink.on('click', function (e) {
      e.preventDefault();
      const $this = $(this);
      if (window.digitalData) {
        $.extend(window.digitalData, {
          linkClick: {
            linkType: 'internal',
            linkSection: 'tabListText',
            linkParentTitle: $this.data('parent-title'),
            linkName: $this.data('link-name'),
            linkListPos: $this.data('tab-count')
          }
        });
        if (window._satellite) {
          window._satellite.track('linkClicked');
        }
      }
      self.setActiveTab($this);
      dynMedia.processImages();
    });
    if (self.isDesktopMode()) {
      this.cache.$tabMenuItemLink.first().addClass('active');
      this.cache.$tabMenuItem.first().addClass('active');
    }
  }
  setActiveTab($this) {
    // variables for the clicked organism only
    const tabID = $this.data('tab-id');
    const { $tabMenuItemLink, $tabMenuItem, $tabContent } = this.cache;

    if ($this.hasClass('active')) {
      if (this.isMobileMode()) {
        $this.removeClass('active');
        $tabMenuItem.removeClass('active');
        $tabContent.removeClass('active');
      }
    } else {
      // set active class to Tab Menu List Item
      $tabMenuItemLink.removeClass('active');
      $tabMenuItem.removeClass('active');
      $this.addClass('active');
      $this.closest('li').addClass('active');

      // Show active tab content depending on the data-tab-id attribute on Tab Menu List Item to match Tab Content data-tab-id attribute
      $tabContent.removeClass('active');
      $.each($tabContent, function () {
        if ($(this).data('tab-id') === tabID) {
          $(this).addClass('active');
        }
      });
    }
  }
  isDesktopMode() {
    return isDesktopMode(...arguments);
  }
  isMobileMode() {
    return isMobileMode(...arguments);
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default ListContentImage;
