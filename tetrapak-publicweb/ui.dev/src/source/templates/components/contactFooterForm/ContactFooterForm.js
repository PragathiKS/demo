import $ from 'jquery';
import 'bootstrap';

class ContactFooterForm {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize cache here */
    this.cache.$field = $('input[type="text"]', this.root);
    this.cache.$submitBtn = $('.form-submit', this.root);
    this.cache.$tabtoggle = $('.pw-form__nextbtn[data-toggle="tab"]', this.root);
    this.cache.$toggleBtns = $('.tpatom-button[data-toggle="tab"]', this.root);
  }
  bindEvents() {
    /* Bind jQuery events here */
    const self = this;
    $('body').bind('show.bs.tab', (e) => {
      let parent = e.target.closest('.pw-contact-form');
      if (!parent) {
        return;
      }
      this.cache.$toggleBtns.removeClass('active show');
      let selectedTarget = $(e.target).data('target');
      $('[data-target="'+selectedTarget+'"]').addClass('active show');
    });
    this.cache.$field.change(function() {
      let fieldName = $(this).attr('name');
      if ($(this).val().length){
        $('p.'+fieldName, self.root).text($(this).val());
        $('.info-group.'+fieldName, self.root).addClass('show');
        $(this).closest('.form-group').removeClass('hasError');
      } else {
        $('.info-group.'+fieldName, self.root).removeClass('show');
      }
      if($('.info-group.show', self.root).length) {
        $('.info-box', self.root).removeClass('d-none');
      } else {
        $('.info-box', self.root).addClass('d-none');
      }
    });
    this.cache.$submitBtn.click(function(e) {
      let isvalid = true;
      let parentTab = e.target.closest('.tab-pane');
      $('input, textarea', parentTab).each(function(){
        if ($(this).prop('required') && $(this).val() === '') {
          isvalid = false;
          e.preventDefault();
          e.stopPropagation();
          $(this).closest('.form-group').addClass('hasError');
        }
      });
      if (isvalid) {
        $('.thankyou .first-name', self.root).text($('#first-name', self.root).val());
        $('.thankyou .last-name', self.root).text($('#last-name', self.root).val());
        $(this).closest('form').submit();
        if (digitalData) { //eslint-disable-line
            digitalData.formInfo = {}; //eslint-disable-line
            digitalData.formInfo.formName = 'contact us'; //eslint-disable-line
            digitalData.formInfo.stepName = 'thank you'; //eslint-disable-line
            digitalData.formInfo.totalSteps = 7; //eslint-disable-line
            _satellite.track('form_tracking'); //eslint-disable-line
        }
      }
    });
    this.cache.$tabtoggle.click(function(e) {
      let parentTab = e.target.closest('.tab-pane');
      $('input', parentTab).each(function(){
        if ($(this).prop('required') && $(this).val() === '') {
          e.preventDefault();
          e.stopPropagation();
          $(this).closest('.form-group').addClass('hasError');
        }
      });
      const stepNumber = parentTab.getAttribute('data-stepNumber');
      const stepName = parentTab.getAttribute('data-stepName');
      console.log("Test - " + stepNumber + "***" + stepName) //eslint-disable-line
      if (digitalData) { //eslint-disable-line
        digitalData.formInfo = {}; //eslint-disable-line
        digitalData.formInfo.formName = 'contact us'; //eslint-disable-line
        digitalData.formInfo.stepName = stepName; //eslint-disable-line
        digitalData.formInfo.stepNo = stepNumber; //eslint-disable-line
        digitalData.formInfo.totalSteps = 7; //eslint-disable-line
        _satellite.track('form_tracking'); //eslint-disable-line
      }
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default ContactFooterForm;
