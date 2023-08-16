import $ from 'jquery';
import 'bootstrap';
import { logger } from 'tpPublic/scripts/utils/logger';
import TabsListParent from 'tpPublic/templates/components/tabslist/TabsList.js';

let parent = null;

function debounce(func){
  var timer;
  return function(event){
    if(timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(func,100,event);
  };
}

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
    let $tabSection;
    let $winWidth = $(window).width();

    if($winWidth <= 1023) {
      $tabSection = $this.root.find('.pw-tablist__event-detail-wrapper');
    } else {
      $tabSection = $this.root.find('.js-tablist__events-sidesection');
    }

    this.cache.heading = $tabSection.find('.pw-modal-header__heading');
    this.cache.thankYou = $tabSection.find('.tl-contactForm__thankyou-heading');
    this.cache.form = $tabSection.find('.tl-contactForm').find('.cmp-form');
    this.cache.formThankYou = $tabSection.find('.tl-contactForm__thankyou');
    this.cache.errorMessage = $tabSection.find('.tl-contactForm__error');
    this.cache.modal = $tabSection.find('.js-tp-contact__modal');
    this.cache.closeBtn = $tabSection.find('.js-close-btn');
    this.cache.contactBtn = $tabSection.find('.js-tpatom-btn__tl-contactUs');
    this.cache.headingText = this.cache.heading.text();
    this.cache.thankYouText = this.cache.thankYou.eq(0).text();

    const { contactBtn, modal, closeBtn, form, errorMessage } = this.cache;
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
      const formData = `${form.serialize()}&${contactBtn.attr('data-companykey')}=${contactBtn.attr('data-companyvalue')}`;
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
          logger?.error(res, parent);
        });
    });
  }
  bindEvents () {
    const { activeTheme } = this.cache;
    const $this = this;
    this.root
      .on('click', '.js-tablist__event', function () {
        const self = $(this);
        if (!self.hasClass(`active--${activeTheme}`)) {
          $this.showTabDetail(self.data('target'));
          $this.loadContactUs();
        }
        $this.root.find('.js-tablist__event').removeClass(`active--${activeTheme}`);
        self.addClass(`active--${activeTheme}`).toggleClass('m-active');
      })
      .on('click', '.js-tablist__event-detail-description-link', this.trackAnalytics)
      .on('hidden.bs.collapse', '.collapse', this.pauseVideoIfExists);
    
    window.addEventListener('resize',debounce(function() {
      $this.loadContactUs();
    }));
  }
  init() {
    super.init();
    this.loadContactUs();
    this.bindEvents();
  }
}
export default TabList;
