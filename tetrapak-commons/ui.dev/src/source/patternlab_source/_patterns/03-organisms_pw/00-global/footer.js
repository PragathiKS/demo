export const footer = (() => {

    const $toTopLink = $( '.tp_footer .tp_textBtn' );

    $toTopLink.click( function() {
        $( 'html, body' ).animate({ scrollTop: 0 }, 700);
        return false;
    });

})();
