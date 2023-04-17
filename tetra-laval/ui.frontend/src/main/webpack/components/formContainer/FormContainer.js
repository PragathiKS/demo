import $ from 'jquery';
import 'bootstrap';
import { logger } from 'tpPublic/scripts/utils/logger';

class FormContainer {
  cache = {};
  resetForm(isForm) {
    const { $form, $formThankYou, $thankYouText, $heading, $headingText, $errorMessage } = this.cache;
    $errorMessage.addClass('d-none');
    if (isForm) {
      $heading.show().text($headingText);
      $heading.removeClass('text-center');
      $form.show();
      $formThankYou.hide();
    }
    if (!isForm) {
      $heading.addClass('text-center');
      $heading.text($thankYouText);
      $form.hide();
      $formThankYou.show();
    }
  }

  bindEvents() {
    const $this = this;
    const { $contactBtn, $modal, $closeBtn, $form, $errorMessage } = this.cache;

    $contactBtn.on('click', function () {
      $modal.modal();
      $this.resetForm(true);
    });

    $closeBtn.on('click', function () {
      $modal.modal('hide');
      $this.resetForm();
    });

    $form.submit(function (e) {
      e.preventDefault();
      const form = $(this);
      const formData = form.serialize() +'&'+$contactBtn.attr('data-companykey')+'='+$contactBtn.attr('data-companyvalue');
      $.ajax({
        type: form.attr('method'),
        url: form.attr('action'),
        data: formData
      })
        .done(function (res) {
          form[0].reset();
          $this.resetForm(false);
          logger?.log(res);
        })
        .fail(function (res) {
          const errResponse = JSON.parse(res.responseText);
          $errorMessage.removeClass('d-none');
          $errorMessage.text(errResponse.statusMessage);
          logger?.error(res);
        });
    });
  }

  initCache() {
    this.cache.$heading = $('.pw-modal-header__heading');
    this.cache.$headingText = this.cache.$heading.text();
    this.cache.$form = $('.tl-contactForm').find('.cmp-form');
    this.cache.$formThankYou = $('.tl-contactForm__thankyou');
    this.cache.$errorMessage = $('.tl-contactForm__error');
    this.cache.$thankYouText = $('.tl-contactForm__thankyou-heading').text();
    this.cache.$contactBtn = $('.js-tpatom-btn__tl-contactUs');
    this.cache.$modal = $('.js-tp-contact__modal');
    this.cache.$closeBtn = $('.js-close-btn');
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}
export default FormContainer;
