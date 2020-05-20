import $ from 'jquery';
// import { ajaxMethods, API_CONTACTUS_FORM } from '../../../scripts/utils/constants';

class ContactUs {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    /* Initialize selector cache here */
    /**
     * Use "this.root" to find elements within current component
     * Example:
     * this.cache.$submitBtn = this.root.find('.js-submit-btn');
     */
    this.cache.$submitBtn = $('button[type="submit"]', this.root);
    this.cache.$nextbtn = this.root.find('.tpatom-btn');
    this.cache.$radio = this.root.find('input[type=radio][name="purposeOfContactOptions"]');
    this.cache.$dropItem = $('.pw-form__dropdown a.dropdown-item', this.root);
    this.cache.$dropdown = $('.pw-form__dropdown', this.root);
  }
  bindEvents() {
    /* Bind jQuery events here */
    const self = this;
    this.cache.$radio.on('change', function() {
      $('input[type=hidden][name="purposeOfContact"]').val(this.value);
    });

    this.cache.$nextbtn.click(function (e) {
      let isvalid = true;
      const target = $(this).data('target');
      const tab = $(this).closest('.tab-content-steps');
      const input = tab.find('input');
      const textarea = tab.find('textarea');
      if(!$(this).hasClass('previousbtn') && (input.length > 0 || textarea.length > 0)) {
        $('input, textarea', tab).each(function () {
          const fieldName = $(this).attr('name');
          $('p.' + fieldName).text($(this).val());
          if ($(this).prop('required') && $(this).val() === '') {
            isvalid = false;
            e.preventDefault();
            e.stopPropagation();
            $(this).closest('.form-group, .formfield').addClass('field-error');
          }
        });
      }
      if(isvalid) {
        tab.find('.form-group, .formfield').removeClass('field-error');
        $('.tab-pane').removeClass('active');
        $(target).addClass('active');
      }
    });

    this.cache.$submitBtn.click(function (e) {
      e.preventDefault();
      e.stopPropagation();
      const target = $(this).data('target');
      $('.tab-pane').removeClass('active');
      $(target).addClass('active');
      var isvalid = true; //eslint-disable-line
      // const parentTab = e.target.closest('.tab-pane');
      $('input, textarea').each(function () {
        if ($(this).val() === '') {
          isvalid = false; //eslint-disable-line
          e.preventDefault();
          e.stopPropagation();
          $(this).closest('.form-group').addClass('field-error');
        }
      });
      // if (isvalid) {
      //   e.preventDefault();
      //   ajaxWrapper.getXhrObj({
      //     url: API_CONTACTUS_FORM,
      //     method: ajaxMethods.GET,
      //     data: $('form.pw-form', self.root).serialize()
      //   }).done(
      //     () => {
      //       $('.tab-pane', self.root).removeClass('active');
      //       $('#cf-step-final', self.root).addClass('active');
      //     }
      //   );
      // }
    });

    this.cache.$dropdown.click(function () {
      $(this).toggleClass('show');
      $(this).find('.dropdown-menu').toggleClass('show');
    });

    this.cache.$dropItem.click(function (e) {
      e.preventDefault();
      const country = $(this).data('country');
      const parentDrop = $(this).closest('.dropdown');
      $('.dropdown-toggle', parentDrop).text(country);
      $('input', parentDrop).val(country);
      self.cache.$dropItem.removeClass('active');
      $(this).addClass('active');
      $(parentDrop).removeClass('show');
      $(parentDrop).find('.dropdown-menu').removeClass('show');
      e.stopPropagation();
    });

  }
  init() {
    /* Mandatory method */
    this.initCache();
    this.bindEvents();
  }
}

export default ContactUs;
