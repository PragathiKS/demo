import $ from 'jquery';
import 'bootstrap';
import { logger } from '../../../scripts/utils/logger';
import { getMaxSafeInteger } from '../../../scripts/common/common';

class InactivityDialog {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.idleTimeoutMinutes = this.root.find('#idleTimeoutMinutes').val();
    this.cache.MAX_SAFE_INTEGER = getMaxSafeInteger();
    this.cache.counter = 0;
  }
  bindEvents() {
    this.root
      .on('click', '.js-close-btn,.js-inactivity-modal__continue,.js-inactivity-modal__logout', this.closeModal)
      .on('click', '.js-inactivity-modal__logout', this.logoutSession)
      .on('click', '.js-close-btn,.js-inactivity-modal__continue', this.setIdleTimer);

    $(window).on('scroll', () => {
      const { idleTimeoutMinutes = 15 } = this.cache;
      this.cache.counter += 1;
      if ((this.cache.counter / idleTimeoutMinutes) > 1) {
        logger.log('[Webpack]: Session inactivity popup reset due to scroll');
        this.setIdleTimer();
        this.cache.counter = 0;
      }
    });

    $('button,a,[tabindex]').on('keydown', () => {
      logger.log('Keydown event triggered');
    });
  }
  closeModal = () => {
    this.root.modal('hide');
  };
  logoutSession = () => {
    logger.log('logout called');
  };
  setIdleTimer = () => {
    const { idleTimeoutMinutes = 15, MAX_SAFE_INTEGER } = this.cache;
    if (this.cache.idleTimer) {
      window.clearTimeout(this.cache.idleTimer);
    }
    logger.log('[Webpack]: Session timer started');
    // Avoid fractional values as final value is increased if divided by fraction
    if (idleTimeoutMinutes > 1) {
      let idleTimeoutMilliseconds = (idleTimeoutMinutes * 60 * 1000);
      idleTimeoutMilliseconds = (idleTimeoutMilliseconds > MAX_SAFE_INTEGER) ? MAX_SAFE_INTEGER : idleTimeoutMilliseconds;
      this.cache.idleTimer = setTimeout(() => {
        window.clearTimeout(this.cache.idleTimer);
        logger.log('[Webpack]: Session inactivity popup triggered');
        this.root.modal('show');
      }, idleTimeoutMilliseconds);
    }
  };
  init() {
    this.initCache();
    this.bindEvents();
    this.setIdleTimer();
  }
}

export default InactivityDialog;
