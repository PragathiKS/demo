export const tp_tabListContent = (() => {

    const $tabMenuItemLink  = $( '.tp_tabListContent-tabMenuListItem-link', '.tp_tabListContent' );

    let tabListContentArray = [];
    let $overallTabListContentClass = $( '.tp_tabListContent' );
    // get each specific class of tabListContent Organisms version and put in array
    $.each( $overallTabListContentClass , function (i) {
        let tabListContentClassArr        = $( this ).attr('class').match(/\S+/gi);
        let specificTabListContentVersion = tabListContentClassArr[tabListContentClassArr.length - 1];
        tabListContentArray.push( specificTabListContentVersion );
    });

    // Loop through the list of tabListContent organism version and clone its active tabContent and append under active tab in the tablist (for mobile)
    tabListContentArray.forEach( function( element ) {
        // cloneOnStart( element );

        const grandParentClass = '.'+element;
        const $tabMenuItem      = $( '.tp_tabListContent-tabMenuListItem', grandParentClass );

        // Clone the Active Tab Content and put in clicked active Tab Menu List Item under Tab Menu List Item Link (this is hidden in desktop view with css)
        let $clonedActiveTabContent = $( '.tp_tabListContent-contentWrapper .tp_tabListContent-contentTab.active', grandParentClass ).clone();
        $.each( $tabMenuItem, function() {
            if ( $( this ).children( 'a.active' ).hasClass( 'active' ) ) {
                $( this ).append( $clonedActiveTabContent );
            }
        });

    });


    // function cloneOnStart( grandParentClassName ) {
    //     const grandParentClass = '.'+grandParentClassName;
    //     const $tabMenuItem      = $( '.tp_tabListContent-tabMenuListItem', grandParentClass );

    //     // Clone the Active Tab Content and put in clicked active Tab Menu List Item under Tab Menu List Item Link (this is hidden in desktop view with css)
    //     let $clonedActiveTabContent = $( '.tp_tabListContent-contentWrapper .tp_tabListContent-contentTab.active', grandParentClass ).clone();
    //     $.each( $tabMenuItem, function() {
    //         if ( $( this ).children( 'a.active' ).hasClass( 'active' ) ) {
    //             $( this ).append( $clonedActiveTabContent );
    //         }
    //     });
    // };




    $tabMenuItemLink.click(function(e) {
        e.preventDefault();
        const $this = $( this );
        let tabID   = $this.data( 'tab-id' );

        // Get this organism grandparent class version name 
        let grandParentClassNamesArr = $this.closest( '.tp_tabListContent' ).attr('class').match(/\S+/gi);
        let specificGrandParentClassVersion = grandParentClassNamesArr[grandParentClassNamesArr.length - 1];
        const grandParentClass = '.'+specificGrandParentClassVersion;

        // variables for the clicked organism only
        const $tabMenuItemLink  = $( '.tp_tabListContent-tabMenuListItem-link', grandParentClass ),
              $tabMenuItem      = $( '.tp_tabListContent-tabMenuListItem', grandParentClass ),
              $tabContent       = $( '.tp_tabListContent-contentTab', grandParentClass );

        // dont do anything if link Tab Link is active
        if ( $this.hasClass('active' ) ) {
            return;
        }

        // set active class to Tab Menu List Item
        $tabMenuItemLink.removeClass( 'active' );
        $this.addClass( 'active' );
        
        // set no-border class for styling the Tab Menu List Item next sibling
        $tabMenuItemLink.removeClass( 'no-border' );
        $this.parent().next().children('.tp_tabListContent-tabMenuListItem-link').addClass( 'no-border' );
        
        // Show active tab content depending on the data-tab-id attribute on Tab Menu List Item to match Tab Content data-tab-id attribute
        $tabContent.removeClass( 'active' );
        $.each( $tabContent, function() {
            if ( $( this ).data( 'tab-id' ) == tabID ) { 
                $( this ).addClass( 'active' );
            }
        });

        // Mobile 
        // Clone the Active Tab Content and put in clicked active Tab Menu List Item under Tab Menu List Item Link
        let $clonedActiveTabContent = $( '.tp_tabListContent-contentWrapper .tp_tabListContent-contentTab.active', grandParentClass ).clone();
        $( '.tp_tabListContent-tabMenuListItem .tp_tabListContent-contentTab', grandParentClass ).slideUp( 'slow', function() { 
            $( this ).remove(); 
        });
        $.each( $tabMenuItem, function() {
            if ( $( this ).children( 'a.active' ).hasClass( 'active' ) ) {
                $clonedActiveTabContent.appendTo( $( this ) ).slideDown( 'slow' ); // .css( 'display', 'none' )
            }
        });

        // // clear the clone variable
        // $clonedActiveTabContent = '';
    });
    
})();
