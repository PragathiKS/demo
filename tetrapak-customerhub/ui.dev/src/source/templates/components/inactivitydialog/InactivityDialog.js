import $ from 'jquery';
import 'bootstrap';
import { logger } from '../../../scripts/utils/logger';
import { getMaxSafeInteger } from '../../../scripts/common/common';
import { $body } from '../../../scripts/utils/commonSelectors';
//import {EQ_LOCAL_STORAGE} from '../../../scripts/utils/constants';

class InactivityDialog {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};
  initCache() {
    this.cache.idleTimeoutMinutes = this.root.find('#idleTimeoutMinutes').val();
    this.cache.logoutURL = this.root.find('#logoutURL').val();
    this.cache.MAX_SAFE_INTEGER = getMaxSafeInteger();
    this.cache.counter = 0;
  }
  bindEvents() {
    this.root
      .on('click', '.js-close-btn,.js-inactivity-modal__continue,.js-inactivity-modal__logout', this.closeModal)
      .on('click', '.js-inactivity-modal__logout', this.logoutSession);

    $(window).on('scroll', () => {
      let { idleTimeoutMinutes } = this.cache;
      if (isNaN(idleTimeoutMinutes)) {
        idleTimeoutMinutes = 15;
      }
      this.cache.counter += 1;
      if ((this.cache.counter / idleTimeoutMinutes) > 1) {
        logger.log('[Webpack]: Session inactivity popup reset due to scroll');
        this.setIdleTimer();
        this.cache.counter = 0;
      }
    });
    $body
      .on('mousedown key.return', 'button,a,[tabindex]', this.setIdleTimer)
      .on('focusin', 'input', this.setIdleTimer)
      .on('input', 'input', this.setIdleTimer);
  }
  closeModal = () => {
    this.root.modal('hide');
  };
  logoutSession = () => {
    const { logoutURL } = this.cache;
    if (logoutURL) {
      //window.localStorage.removeItem(EQ_LOCAL_STORAGE);
      window.open(logoutURL, '_self');
    }
  };
  setIdleTimer = () => {
    let { idleTimeoutMinutes } = this.cache;
    const { MAX_SAFE_INTEGER } = this.cache;
    if (this.cache.idleTimer) {
      window.clearTimeout(this.cache.idleTimer);
    }
    if (isNaN(idleTimeoutMinutes)) {
      logger.log('[Webpack]: Timeout value is invalid. Setting default 15 mins timeout.');
      idleTimeoutMinutes = 15;
    }
    // Avoid fractional values as final value is increased if divided by fraction
    if (idleTimeoutMinutes >= 1) {
      logger.log('[Webpack]: Session timer started');
      let idleTimeoutMilliseconds = ((+idleTimeoutMinutes) * 60 * 1000);
      idleTimeoutMilliseconds = (idleTimeoutMilliseconds > MAX_SAFE_INTEGER) ? MAX_SAFE_INTEGER : idleTimeoutMilliseconds;
      this.cache.idleTimer = setTimeout(() => {
        window.clearTimeout(this.cache.idleTimer);
        logger.log('[Webpack]: Session inactivity popup triggered');
        this.root.modal('show');
      }, idleTimeoutMilliseconds);
      logger.log(`[Webpack]: Session timer set for ${idleTimeoutMinutes} minutes`);
    }
  };
  init() {
    this.initCache();
    this.bindEvents();
    this.setIdleTimer();
  }
}

export default InactivityDialog;
