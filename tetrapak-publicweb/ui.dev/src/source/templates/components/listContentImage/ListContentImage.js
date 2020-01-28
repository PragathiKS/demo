import $ from 'jquery';
import { getWindowWidth } from '../../../scripts/common/common';
import { dynMedia } from '../../../scripts/utils/dynamicMedia';

class ListContentImage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    const self = this;
    this.cache.$tabMenuItemLink = $('.pw-listContentImage__tabMenuListItem__link', this.root);
    this.cache.$tabMenuItem = $('.pw-listContentImage__tabMenuListItem', this.root);
    this.cache.$editTabItem = $('.pw-listContentImage__editTab', this.root);
    this.cache.$contentWrapper = $('.pw-listContentImage__contentWrapper', this.root);

    // Add Version Name to each instance
    $('.pw-listContentImage').each(function (index) {
      $(this).addClass('listContentImage-version' + index);
    });

    // Clone all EditTab Content to the Content Wrapper
    $.each(this.cache.$editTabItem, function () {
      const tabID = $(this).data('tab-id');
      const $clonedEditTabContent = $('.pw-listContentImage__contentTab', this).clone();
      const $clonedEditTabContentMobile = $('.pw-listContentImage__contentTab', this).clone();
      self.cache.$contentWrapper.append($clonedEditTabContent);
      $(`#${tabID}`).append($clonedEditTabContentMobile);
    });

  }
  bindEvents() {
    const self = this;
    const { $tabMenuItemLink } = this.cache;
    $tabMenuItemLink.click(function (e) {
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
    const width = getWindowWidth();
    if (width > 767) {
      this.cache.$tabMenuItemLink.first().addClass('active');
      this.cache.$tabMenuItem.first().addClass('active');
    }
  }
  setActiveTab($this) {
    const self = this;
    // variables for the clicked organism only
    const width = getWindowWidth();
    const tabID = $this.data('tab-id');
    const $tabMenuItemLink = $('.pw-listContentImage__tabMenuListItem__link', self.root),
      $tabMenuItem = $('.pw-listContentImage__tabMenuListItem', self.root),
      $tabContent = $('.pw-listContentImage__contentTab', self.root);

    if ($this.hasClass('active')) {
      if (width < 768) {
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
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default ListContentImage;
