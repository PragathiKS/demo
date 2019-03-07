import { $body } from './commonSelectors';

export const toast = {
  init() {
    $body.on('click', '.js-toast__dismiss', function () {
      $(this).parents('.js-toast').addClass('d-none');
    });
  }
};
