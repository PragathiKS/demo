// jQuery dependency (Running from vendor file)

// Side Menu = sm_


export const tp_sideMenu = (() => {

    const $sm_ListItemWithSubMenu = $( '.tp_sideMenu-listItem.has-subMenu' );
    const $sm_subList = ('.tp_sideMenu-subMenuList');

    $sm_ListItemWithSubMenu.find('a').on( 'click', function( e ) {
        e.preventDefault();
        let $this = $( this );

        if ( $this.siblings('.tp_sideMenu-subMenuList').length) {
            $this.parent().toggleClass('subMenu-open');
            $this.next($sm_subList).toggleClass('open');
        }
    });

})();
