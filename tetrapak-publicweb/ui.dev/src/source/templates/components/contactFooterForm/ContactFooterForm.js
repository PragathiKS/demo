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
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);
  }
  validEmail(email) {
    let pattern = new RegExp(/^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i);
    return pattern.test(email);
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
      if ($(this).val().length){
        $(this).closest('.form-group').removeClass('hasError');
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
      }
    });
    this.cache.$tabtoggle.click(function(e) {
      let parentTab = e.target.closest('.tab-pane');
      $('input', parentTab).each(function(){
        let fieldName = $(this).attr('name');
        if ($(this).prop('required') && ($(this).val() === '') || (fieldName ==='email-address') && !self.validEmail($(this).val())) {
          e.preventDefault();
          e.stopPropagation();
          $(this).closest('.form-group').addClass('hasError');
          $('.info-group.'+fieldName).removeClass('show');
        } else {
          $('p.'+fieldName).text($(this).val());
          $('.info-group.'+fieldName).addClass('show');
          $(this).closest('.form-group').removeClass('hasError');
        }
        if($('.info-group.show', self.root).length) {
          $('.info-box', self.root).removeClass('d-none');
        } else {
          $('.info-box', self.root).addClass('d-none');
        }
      });
    });
    this.cache.$dropItem.click(function(e) {
      e.preventDefault();
      let country = $(this).data('country');
      let parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle', parentDrop).text(country);
      $('input', parentDrop).val(country);
      self.cache.$dropItem.removeClass('active');
      $(this).addClass('active');
    });
  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default ContactFooterForm;
