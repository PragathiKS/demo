import Contacts from './Contacts';

describe('Contacts', function () {
  before(function () {
    this.contacts = new Contacts({
      el: document.body
    });
  });
});
