/* eslint-disable */
import $ from 'jquery';
import 'bootstrap';
import { trackAnalytics } from 'tpPublic/scripts/utils/analytics';
import { isExternal } from 'tpPublic/scripts/utils/updateLink';
import { pauseVideosByReference, initializeYoutubePlayer, removeYTReferences, ytPromise, initializeDAMPlayer } from 'tpPublic/scripts/utils/videoAnalytics';
import { logger } from 'tpPublic/scripts/utils/logger';

function _renderFirstTab() {
  const { componentId } = this.cache;
  const $tabSection = this.root.find('.js-tablist__events-sidesection');
  $tabSection.html($(`#tab_${componentId}_0`).html());
  $tabSection.find('.js-yt-player, .js-dam-player').removeClass('video-init');
  ytPromise.then(() => {
    initializeYoutubePlayer();
  }).catch( err => {
    logger.log('err in Tablist>>>',err);
  });
  initializeDAMPlayer();
}

class TabsList {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.selectedTabIndex = 0;
    this.cache.componentId = this.root.find('.tabComponentId').val();
    this.cache.activeTheme = this.root.find('.activeTheme').val();
    this.cache.tabButton = this.root.find('.js-pw-tablist__event');
    this.cache.heading = $.trim(this.root.find('.js-tablist__heading').text());
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
  /*-- END CONTACT US --*/

  bindEvents() {
    const $this = this;
    const { activeTheme, tabButton } = this.cache;
    this.root
      .on('click', '.js-tablist__event', function () {
        const self = $(this);
        if (!self.hasClass(`active--${activeTheme}`)) {
          $this.showTabDetail(self.data('target'));
        }
        $this.root.find('.js-tablist__event').removeClass(`active--${activeTheme}`);
        self.addClass(`active--${activeTheme}`).toggleClass('m-active');
      })
      .on('click', '.js-tablist__event-detail-description-link', this.trackAnalytics)
      .on('hidden.bs.collapse', '.collapse', this.pauseVideoIfExists);

    tabButton.on('click', this.trackAnalyticsTabs);
  }

  showTabDetail = (el) => {
    const $tabSection = this.root.find('.js-tablist__events-sidesection');
    removeYTReferences($tabSection.find('.js-yt-player'));
    $tabSection.html($(el).html());
    $tabSection.find('.js-yt-player, .js-dam-player').removeClass('video-init');
    ytPromise.then(() => {
      initializeYoutubePlayer();
    });
    initializeDAMPlayer();

    // Contact US Modal
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
  pauseVideoIfExists() {
    pauseVideosByReference($(this).find('.is-playing'));
  }
  renderFirstTab = () => _renderFirstTab.call(this);

  trackAnalyticsTabs = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-pw-tablist__event');
    const trackingObj = {
      linkType:'internal',
      linkSection:$this.data('link-section'),
      linkParentTitle:$this.data('parent-title'),
      linkName: $this.data('link-name')
    };

    const eventObj = {
      eventType: 'linkClick',
      event: 'tablist'
    };
    trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    return true;
  }
  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tablist__event-detail-description-link');
    let linkParentTitle = '';
    let trackingObj = {};
    let eventObj = {};
    const dwnType = 'ungated';
    let eventType = 'content-load';
    const linkType = $this.attr('target') === '_blank' ? 'external' : 'internal';
    const linkSection = $this.data('link-section');
    const linkName = $this.data('link-name');
    const buttonLinkType = $this.data('button-link-type');
    const downloadtype = $this.data('download-type');
    const dwnDocName = $this.data('asset-name');
    const tabTitle = $this.data('tab-title');
    const componentName = $this.data('component-name');
    let extension = '';
    if(downloadtype === 'download'){
      extension = $this.attr('href').split('.').pop();
    }

    if (buttonLinkType === 'secondary' && downloadtype === 'download') {
      linkParentTitle = `CTA_Download_${extension}_${tabTitle}`;
      eventType = 'download';
    }

    if (buttonLinkType === 'link' && downloadtype === 'download') {
      linkParentTitle = `Text hyperlink_Download_${extension}_${tabTitle}`;
      eventType = 'download';
    }

    if (buttonLinkType === 'secondary' && downloadtype !== 'download') {
      linkParentTitle = `CTA_${tabTitle}`;
    }

    if (buttonLinkType === 'link' && downloadtype !== 'download') {
      linkParentTitle = `Text hyperlink_${tabTitle}`;
    }

    if (downloadtype === 'download') {
      trackingObj = {
        linkType,
        linkSection,
        linkParentTitle,
        linkName,
        dwnDocName,
        dwnType,
        eventType
      };

      eventObj = {
        eventType: 'downloadClick',
        event: componentName
      };
      trackAnalytics(trackingObj, 'linkClick', 'downloadClick', undefined, false, eventObj);
    }

    if (downloadtype !== 'download') {
      trackingObj = {
        linkType,
        linkSection,
        linkParentTitle,
        linkName
      };

      eventObj = {
        eventType: 'linkClick',
        event: componentName
      };
      trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    }
    if (linkType === 'internal') {
      if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224){ 
        window.open($this.attr('href'),'_blank');
      }
      else {
        window.open($this.attr('href'),'_self');
      }
    }
    else {
      window.open($this.attr('href'), $this.attr('target'));
    }
  }

  addLinkAttr() {
    $('.js-tablist__event-detail-description-link').each(function () {
      const thisHref = $(this).attr('href');
      if (thisHref) {
        if (isExternal(thisHref)) {
          $(this).attr('target', '_blank');
          $(this).data('download-type', 'download');
          //$(this).data('download-type', 'download');
          $(this).data('link-section', $(this).data('link-section') + '_Download');
          $(this).attr('rel', 'noopener noreferrer');
        } else {
          $(this).data('download-type', 'hyperlink');
        }
      }
    });
  }

  init() {
    this.initCache();
    this.bindEvents();
    this.renderFirstTab();
    this.addLinkAttr();
  }
}

export default TabsList;
