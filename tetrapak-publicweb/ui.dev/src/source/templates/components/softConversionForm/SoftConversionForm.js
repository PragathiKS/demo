import $ from 'jquery';
import 'bootstrap';

class SoftConversionForm {
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$selector = $('#softConversionModal');
  }
  bindEvents() {
    /* Bind jQuery events here */
    $('body').bind('show.bs.tab', function(e){
      let parentModal = e.target.closest('.modal');
      if (!parentModal) {
        return;
      }
      let grandParentId = $(parentModal).attr('id');
      const grandParentIdSelector = '#'+grandParentId;

      const $toggleBtns = $('[data-toggle="tab"]', grandParentIdSelector);

      let selectedTarget = $(e.target).data('taget');
      $toggleBtns.each(function() {
        if ($(this).data('target') !== selectedTarget) {
          $(this).removeClass('active show');
        } else {
          $(this).addClass('active show');
        }
      });
    });

    this.cache.$selector.on('hidden.bs.modal', function () {
      $('[data-toggle="tab"]', '#softConversionModal').removeClass('active show');
      $('.tab-pane', '#softConversionModal').removeClass('active');
      $('.tab-content .tab-pane:first', '#softConversionModal').addClass('active');
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default SoftConversionForm;
