import $ from 'jquery';
import { $body } from './commonSelectors';

/**
 * customDropdown.js
 * The file implements custom dropdown
 * @project   Tetra Pak
 * @date      2019-07-02
 * @author    Ankur Gupta
 * @dependencies jQuery,$body
 */

export const customDropdown = {
  extendJQueryAPI() {
    $.prototype.customSelect = function (key) {
      const $this = $(this);
      if ($this.hasClass('js-dropdown-btn')) {
        if (typeof key === 'undefined') {
          return $this.data('key');
        }
        const selectedLi = $this.parents('.js-custom-dropdown').find(`li > a[data-key="${key}"]`);
        $this.data('key', selectedLi.data('key')).attr('data-key', selectedLi.data('key'));
        $this.find('span').text(selectedLi.text());
      }
      return this;
    };
  },
  bindEvents() {
    $body.on('click', '.js-custom-dropdown-li', function (e) {
      e.preventDefault();
      const $this = $(this);
      const currentTarget = $(e.currentTarget);
      const desc = currentTarget.text();
      const $btn = $this.parents('.js-custom-dropdown').find('.js-dropdown-btn');
      $btn.find('span').text(desc);
      $btn.data('key', currentTarget.data('key')).attr('data-key', currentTarget.data('key'));
      $btn.trigger('dropdown.change');
    });
  },
  init() {
    this.extendJQueryAPI();
    this.bindEvents();
  }
};

