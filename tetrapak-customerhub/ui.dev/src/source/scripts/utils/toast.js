import { $body } from './commonSelectors';
import { render } from './render';

export const toast = {
  init() {
    $body.on('click', '.js-toast__dismiss', function () {
      $(this).parents('.js-toast').addClass('d-none');
    });
  },
  render(errorMsg, closeBtnLabel) {
    if (
      typeof errorMsg === 'string'
      && errorMsg.length
    ) {
      const toast = render.get('cuhuToastMessage');
      const existingToast = $body.find('.js-toast');
      if (existingToast.length) {
        existingToast.remove();
      }
      $body.append(
        toast({
          errorMsg,
          closeBtnLabel
        })
      );
      $body.find('.js-toast').removeClass('d-none');
    }
  }
};
