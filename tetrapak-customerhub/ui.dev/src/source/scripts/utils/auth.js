import { ajaxWrapper } from './ajax';
import LZStorage from 'lzstorage';
import 'core-js/features/promise';

const lzs = new LZStorage({
  compression: true
});

export const getToken = () => (
  new Promise(function (resolve, reject) {
    const appData = lzs.get('appData');
    if (appData) {
      resolve({
        data: appData,
        textStatus: 'success',
        jqXHR: {
          fromStorage: true
        }
      });
    } else {
      ajaxWrapper.getXhrObj({
        url: 'https://api-mig.tetrapak.com/oauth2/v2/token',
        dataType: 'json',
        beforeSend(jqXHR) {
          jqXHR.setRequestHeader('Authorization', `Basic ${window.btoa('KHEnJskMGGogWrJAD3OyUI3VwerCLSDQ:jX38HGX7Ze4j6vvZ')}`);
          jqXHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          jqXHR.setRequestHeader('Accept', 'application/json');
        }
      }).done(function (data, textStatus, jqXHR) {
        if (data) {
          resolve({
            data,
            textStatus,
            jqXHR
          });
        } else {
          reject({
            jqXHR,
            textStatus: 'empty',
            errorThrown: ''
          });
        }
      }).fail(function (jqXHR, textStatus, errorThrown) {
        reject({
          jqXHR,
          textStatus,
          errorThrown
        });
      });
    }
  })
);
