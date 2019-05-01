import $ from 'jquery';
import 'bootstrap';
import { storageUtil} from '../../../scripts/common/common';

class SoftConversionForm {
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$modal = $('#softConversionModal');
    this.cache.$form = $('#softConversionModal form');
    this.cache.$field = $('#softConversionModal input[type="text"]');
    this.cache.$submitBtn = $('#softConversionModal .form-submit');
    this.cache.$tabtoggle = $('#softConversionModal .pw-model__nextbtn[data-toggle="tab"]');
    this.cache.$radiobtns = $('#softConversionModal input:radio');
    this.cache.$questionBtn = $('#softConversionModal .questionBtn');
  }
  storageFormData() {
    let formData = this.cache.$form.serializeArray();
    storageUtil.set('softConversionData', formData);
  }
  setFormData(data) {
    for (var i = 0; i < data.length; i++) {
      if (data[i].name === 'group') {
        $('#softConversionModal input[name="group"]:checked').prop('checked',false);
        $('#softConversionModal input[name="group"][value="' + data[i].value + '"]').prop('checked', true);
      } else {
        $('#softConversionModal input[name="'+data[i].name+'"]').val(data[i].value);
      }
    }
  }
  bindEvents() {
    /* Bind jQuery events here */
    const self = this;
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
        $(this).closest('.form-group').removeClass('hasError');
      } else {
        $('.info-group.'+fieldName).removeClass('show');
      }
    });
    this.cache.$submitBtn.click(function(e) {
      let isvalid = true;
      let parentTab = e.target.closest('.tab-pane');
      $('input', parentTab).each(function(){
        if ($(this).prop('required') && $(this).val() === '') {
          isvalid = false;
          e.preventDefault();
          e.stopPropagation();
          $(this).closest('.form-group').addClass('hasError');
        }
      });
      if (isvalid) {
        self.storageFormData();
        $(this).closest('form').submit();
        let docpath = $('#softConversionModal input[name="docpath"]').val();
        window.open(docpath, '_blank');
        $('#softConversionModal').data('form-filled', true);
        $('#softConversionModal .softc-title-js').addClass('d-none');
        $('#softConversionModal .softc-thankyou-js').removeClass('d-none');
      }
    });
    this.cache.$tabtoggle.click(function(e) {
      let parentTab = e.target.closest('.tab-pane');
      let targetTab = $(this).data('target');
      console.log(targetTab); // eslint-disable-line no-console
      $('input', parentTab).each(function(){
        if ($(this).prop('required') && $(this).val() === '') {
          e.preventDefault();
          e.stopPropagation();
          $(this).closest('.form-group').addClass('hasError');
        }
      });
    });
    this.cache.$radiobtns.click(function() {
      if ($(this).val() !== 'Professional') {
        $('#softConversionModal .isPro').addClass('d-none');
        $('#softConversionModal .isNotPro').removeClass('d-none');
      } else {
        $('#softConversionModal .isPro').removeClass('d-none');
        $('#softConversionModal .isNotPro').addClass('d-none');
      }
    });
    this.cache.$questionBtn.click(function(e) {
      e.preventDefault();
      let target = $(this).data('target');
      if($(target).length) {
        $('html, body').animate({
          scrollTop: parseInt($(target).offset().top, 10)
        }, 1000);
      }
    });
  }
  init() {
    /* Mandatory method */
    let softConversionData = storageUtil.get('softConversionData');
    this.initCache();
    if(softConversionData) {
      this.setFormData(softConversionData);
      $('#softConversionModal').data('form-filled', true);
    }
    this.bindEvents();
  }
}

export default SoftConversionForm;
