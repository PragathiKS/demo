import $ from 'jquery';

class ListContentImage {
  cache = {};
  initCache() {
    this.cache.$tabMenuItemLink  = $( '.pw-listContentImage__tabMenuListItem__link', '.pw-listContentImage' );
    let tabListContentArray = [];
    let $overallTabListContentClass = $( '.pw-listContentImage' );
    // get each specific class of tabListContent Organisms version and put in array
    $.each( $overallTabListContentClass , function () {
      let tabListContentClassArr        = $( this ).attr('class').match(/\S+/gi);
      let specificTabListContentVersion = tabListContentClassArr[tabListContentClassArr.length - 1];
      tabListContentArray.push( specificTabListContentVersion );
    });
    // Loop through the list of tabListContent organism version and first clone the editTabContent and then clone its active tabContent and append under active tab in the tablist (for mobile)
    tabListContentArray.forEach( function( element ) {
      const grandParentClass = '.'+element;
      const $tabMenuItem      = $( '.pw-listContentImage__tabMenuListItem', grandParentClass );
      const $editTabItem = $('.pw-listContentImage__editTab', grandParentClass);
      const $contentWrapper = $( '.pw-listContentImage__contentWrapper', grandParentClass );

      // Clone all EditTab Content to the Content Wrapper
      $.each($editTabItem, function() {
        let $clonedEditTabContent = $('.pw-listContentImage__contentTab', this).clone();
        $contentWrapper.append($clonedEditTabContent);
      });

      // Clone the Active Tab Content and put in clicked active Tab Menu List Item under Tab Menu List Item Link (this is hidden in desktop view with css)
      let $clonedActiveTabContent = $( '.pw-listContentImage__contentWrapper .pw-listContentImage__contentTab.active', grandParentClass ).clone();
      $.each( $tabMenuItem, function() {
        if ( $( this ).children( 'a.active' ).hasClass( 'active' ) ) {
          $( this ).append( $clonedActiveTabContent );
        }
      });

    });
  }
  bindEvents() {
    this.cache.$tabMenuItemLink.click(function(e) {
      e.preventDefault();
      const $this = $( this );
      let tabID   = $this.data( 'tab-id' );

      // Get this organism grandparent class version name
      let grandParentClassNamesArr = $this.closest( '.pw-listContentImage' ).attr('class').match(/\S+/gi);
      let specificGrandParentClassVersion = grandParentClassNamesArr[grandParentClassNamesArr.length - 1];
      const grandParentClass = '.'+specificGrandParentClassVersion;

      // variables for the clicked organism only
      const $tabMenuItemLink  = $( '.pw-listContentImage__tabMenuListItem__link', grandParentClass ),
        $tabMenuItem      = $( '.pw-listContentImage__tabMenuListItem', grandParentClass ),
        $tabContent       = $( '.pw-listContentImage__contentTab', grandParentClass );

      // dont do anything if link Tab Link is active
      if ( $this.hasClass('active' ) ) {
        return;
      }

      // set active class to Tab Menu List Item
      $tabMenuItemLink.removeClass( 'active' );
      $this.addClass( 'active' );

      // set no-border class for styling the Tab Menu List Item next sibling
      $tabMenuItemLink.removeClass( 'no-border' );
      $this.parent().next().children('.pw-listContentImage__tabMenuListItem__link').addClass( 'no-border' );

      // Show active tab content depending on the data-tab-id attribute on Tab Menu List Item to match Tab Content data-tab-id attribute
      $tabContent.removeClass( 'active' );
      $.each( $tabContent, function() {
        if ( $( this ).data( 'tab-id' ) === tabID ) {
          $( this ).addClass( 'active' );
        }
      });

      // Mobile
      // Clone the Active Tab Content and put in clicked active Tab Menu List Item under Tab Menu List Item Link
      let $clonedActiveTabContent = $( '.pw-listContentImage__contentWrapper .pw-listContentImage__contentTab.active', grandParentClass ).clone();
      $( '.pw-listContentImage__tabMenuListItem .pw-listContentImage__contentTab', grandParentClass ).slideUp( 'slow', function() {
        $( this ).remove();
      });
      $.each( $tabMenuItem, function() {
        if ( $( this ).children( 'a.active' ).hasClass( 'active' ) ) {
          $clonedActiveTabContent.appendTo( $( this ) ).slideDown( 'slow' );
        }
      });
    });
  }
  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default ListContentImage;
