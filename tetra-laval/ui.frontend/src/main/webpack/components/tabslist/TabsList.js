/* eslint-disable */
import $ from 'jquery';
import 'bootstrap';
import { logger } from 'tpPublic/scripts/utils/logger';
import TabsListParent from 'tpPublic/templates/components/tabslist/TabsList.js';

let parent;
class TabList extends TabsListParent {
  constructor({ el }) {
    parent = super({ el });
    this.root = $(el);
  }

  // Contact US
  resetForm(isForm) {
    const { form, formThankYou, thankYouText, heading, headingText, errorMessage } = this.cache;
    errorMessage.addClass('d-none');
    if (isForm) {
      heading.show().text(headingText);
      heading.removeClass('text-center');
      form.show();
      formThankYou.hide();
    }
    if (!isForm) {
      heading.addClass('text-center');
      heading.text(thankYouText);
      form.hide();
      formThankYou.show();
    }
  }

  loadContactUs = () => {
    const $this = this;
    const $tabSection = $this.root.find('.js-tablist__events-sidesection');
    this.cache.heading = $tabSection.find('.pw-modal-header__heading');
    this.cache.thankYou = $tabSection.find('.tl-contactForm__thankyou-heading');
    this.cache.form = $tabSection.find('.tl-contactForm').find('.cmp-form');
    this.cache.formThankYou = $tabSection.find('.tl-contactForm__thankyou');
    this.cache.errorMessage = $tabSection.find('.tl-contactForm__error');
    this.cache.modal = $tabSection.find('.js-tp-contact__modal');
    this.cache.closeBtn = $tabSection.find('.js-close-btn');
    this.cache.contactBtn = $tabSection.find('.js-tpatom-btn__tl-contactUs');

    console.log('Hiren Parmar | Contact Us button in TabList', this.cache.contactBtn);
    this.cache.headingText = this.cache.heading.text();
    this.cache.thankYouText = this.cache.thankYou.text();

    const { contactBtn, modal, closeBtn, form, errorMessage } = this.cache;
    console.log('Hiren Parmar | Contact Us button For Events', this.cache.contactBtn);
    contactBtn.on('click', function () {
      modal.modal();
      $this.resetForm(true);
    });

    closeBtn.on('click', function () {
      modal.modal('hide');
      $this.resetForm();
    });

    form.submit(function (e) {
      e.preventDefault();
      const form = $(this);
      const formData = form.serialize() +'&'+contactBtn.attr('data-companykey')+'='+contactBtn.attr('data-companyvalue');
      $.ajax({
        type: form.attr('method'),
        url: form.attr('action'),
        data: formData
      })
        .done(function (res) {
          form[0].reset();
          const resStatus = JSON.parse(res);
          if(resStatus.statusCode === 200) {
            if(resStatus.type === 'redirect') {
              modal.modal('hide');
              $this.resetForm();
              window.open(resStatus.redirectURL, '_blank');
            } else {
              $this.resetForm(false);
            }
          }
        })
        .fail(function (res) {
          const resStatus = JSON.parse(res.responseText);
          errorMessage.removeClass('d-none');
          errorMessage.text(resStatus.statusMessage);
          logger?.error(res);
        });
    });
    /*-- END CONTACT US --*/
  }
  
  bindEvents () {
    // super.bindEvents();
    const $this = this;
    $('.js-tablist__event').on('click', function() {
      setTimeout(() => {
        $this.loadContactUs();
      }, 200);
    });
  }

  init() {
    super.init();
    this.bindEvents();
    this.loadContactUs();
  }
}
export default TabList;
