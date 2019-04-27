import $ from 'jquery';
import 'bootstrap';

class SoftConversionForm {
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$modal = $('#softConversionModal');
    this.cache.$field = $('#softConversionModal input[type="text"]');
    this.cache.$submitBtn = $('#softConversionModal .form-submit');
    this.cache.$tabtoggle = $('#softConversionModal [data-toggle="tab"]');
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
      $toggleBtns.removeClass('active show');
      let selectedTarget = $(e.target).data('target');
      $('[data-target="'+selectedTarget+'"]').addClass('active show');
    });

    this.cache.$modal.on('hidden.bs.modal', function () {
      $('[data-toggle="tab"]', '#softConversionModal').removeClass('active show');
      $('.tab-pane', '#softConversionModal').removeClass('active');
      $('.tab-content .tab-pane:first', '#softConversionModal').addClass('active');
    });
    this.cache.$field.change(function() {
      let fieldName = $(this).attr('name');
      if ($(this).val().length){
        $('p.'+fieldName).text($(this).val());
        $('.info-group.'+fieldName).addClass('show');
        $(this).removeClass('hasError');
      } else {
        $('.info-group.'+fieldName).removeClass('show');
      }
    });
    this.cache.$submitBtn.click(function() {
      $(this).closest('form').submit();
    });
    this.cache.$tabtoggle.click(function(e) {
      let parentTab = e.target.closest('.tab-pane');
      $('input', parentTab).each(function(){
        if ($(this).prop('required') && $(this).val() === '') {
          e.preventDefault();
          e.stopPropagation();
          $(this).addClass('hasError');
        }
      });
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default SoftConversionForm;
