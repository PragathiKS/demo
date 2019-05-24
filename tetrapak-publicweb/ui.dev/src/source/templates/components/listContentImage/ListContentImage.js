import $ from 'jquery';

class ListContentImage {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    const self = this;
    this.cache.$tabMenuItemLink  = $( '.pw-listContentImage__tabMenuListItem__link', this.root );
    this.cache.$tabMenuItem      = $( '.pw-listContentImage__tabMenuListItem', this.root );
    this.cache.$editTabItem = $('.pw-listContentImage__editTab', this.root);
    this.cache.$contentWrapper = $( '.pw-listContentImage__contentWrapper', this.root );
    this.cache.digitalData = digitalData; //eslint-disable-line

    // Add Version Name to each instance
    $('.pw-listContentImage').each(function(index){
      $(this).addClass('listContentImage-version'+index);
    });

    // Clone all EditTab Content to the Content Wrapper
    $.each(this.cache.$editTabItem, function() {
      let tabID = $(this).data( 'tab-id' );
      let $clonedEditTabContent = $('.pw-listContentImage__contentTab', this).clone();
      let $clonedEditTabContentMobile = $('.pw-listContentImage__contentTab', this).clone();
      self.cache.$contentWrapper.append($clonedEditTabContent);
      $('#'+tabID).append($clonedEditTabContentMobile);
    });

  }
  bindEvents() {
    const self = this;
    this.cache.$tabMenuItemLink.click(function(e) {
      e.preventDefault();
      const $this = $( this );
      let width = window.innerWidth || document.body.clientWidth;
      let tabID   = $this.data( 'tab-id' );
      if (self.cache.digitalData) {
        self.cache.digitalData.linkClick = {};
        self.cache.digitalData.linkClick.linkType = 'internal';
        self.cache.digitalData.linkClick.linkSection = 'listContentImage';
        self.cache.digitalData.linkClick.linkParentTitle = $this.data( 'parent-title' );
        self.cache.digitalData.linkClick.linkName = $this.data( 'link-name' );
        self.cache.digitalData.linkClick.linkListPos = $this.data( 'tab-count' );
        if (typeof _satellite !== 'undefined') { //eslint-disable-line
          _satellite.track('linkClicked'); //eslint-disable-line
        }
      }

      // variables for the clicked organism only
      const $tabMenuItemLink  = $( '.pw-listContentImage__tabMenuListItem__link', self.root ),
        $tabMenuItem      = $( '.pw-listContentImage__tabMenuListItem', self.root ),
        $tabContent       = $( '.pw-listContentImage__contentTab', self.root );

      if ( $this.hasClass('active' ) ) {
        if (width < 768) {
          $this.removeClass( 'active' );
          $tabMenuItem.removeClass( 'active' );
          $tabContent.removeClass( 'active' );
        }
      } else {
        // set active class to Tab Menu List Item
        $tabMenuItemLink.removeClass( 'active' );
        $tabMenuItem.removeClass( 'active' );
        $this.addClass( 'active' );
        $this.closest('li').addClass( 'active' );

        // Show active tab content depending on the data-tab-id attribute on Tab Menu List Item to match Tab Content data-tab-id attribute
        $tabContent.removeClass( 'active' );
        $.each( $tabContent, function() {
          if ( $( this ).data( 'tab-id' ) === tabID ) {
            $( this ).addClass( 'active' );
          }
        });
      }
    });
    $(document).ready(() => {
      let width = window.innerWidth || document.body.clientWidth;
      if (width > 767) {
        this.cache.$tabMenuItemLink.first().addClass( 'active' );
        this.cache.$tabMenuItem.first().addClass( 'active' );
      }
    });
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default ListContentImage;
