// jQuery dependency (Running from vendor file)

export const tp_notifications = (() => {

    const $closeNotification = $( '.tp_notifications-close' );

    $closeNotification.on( 'click', function( e ) {
        e.preventDefault();
        let $this = $( this );

        $this.parent().remove();
    });

})();
