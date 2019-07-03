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
  init() {
    $body.on('click', '.js-custom-dropdrown-li', function (e) {
      e.preventDefault();
      const $btn = $(this).parents('.js-custom-dropdown').find('.js-dropdown-btn');
      $btn.text(e.currentTarget.innerText);
      $btn.trigger('changeCustomDropdown', $(this).data('key'));
    });
  }
};

