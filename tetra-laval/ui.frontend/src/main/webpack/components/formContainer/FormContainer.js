/* eslint-disable */

import $ from 'jquery';
import 'bootstrap';
import { logger } from 'tpPublic/scripts/utils/logger';

class FormContainer {
  constructor({ el }) {
    this.root = $(el);
  }

  cache = {};
  resetForm(isForm) {
    const { $form, $formThankYou, thankYouText, $heading, headingText, $errorMessage } = this.cache;
    $errorMessage.addClass('d-none');
    if (isForm) {
      $heading.show().text(headingText);
      $heading.removeClass('text-center');
      $form.show();
      $formThankYou.hide();
    }
    if (!isForm) {
      $heading.addClass('text-center');
      $heading.text(thankYouText);
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
          const resStatus = JSON.parse(res);
          if(resStatus.statusCode === 200) {
            if(resStatus.type === 'redirect') {
              $modal.modal('hide');
              $this.resetForm();
              window.open(resStatus.redirectURL, '_blank');
            } else {
              $this.resetForm(false);
            }
          }
        })
        .fail(function (res) {
          const resStatus = JSON.parse(res.responseText);
          $errorMessage.removeClass('d-none');
          $errorMessage.text(resStatus.statusMessage);
          logger?.error(res);
        });
    });
  }

  initCache() {
    console.log(
      '\n\n\n',
      { root: this.root },
      '\n\n\n'
    );

    this.cache.$heading = this.root.find('.pw-modal-header__heading');
    this.cache.$thankYou = this.root.find('.tl-contactForm__thankyou-heading');
    this.cache.$form = this.root.find('.tl-contactForm').find('.cmp-form');
    this.cache.$formThankYou = this.root.find('.tl-contactForm__thankyou');
    this.cache.$errorMessage = this.root.find('.tl-contactForm__error');
    this.cache.$modal = this.root.find('.js-tp-contact__modal');
    this.cache.$closeBtn = this.root.find('.js-close-btn');

    this.cache.$contactBtn = this.root.parent().find('.js-tpatom-btn__tl-contactUs');

    this.cache.headingText = this.cache.$heading.text();
    this.cache.thankYouText = this.cache.$thankYou.text();
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}
export default FormContainer;
