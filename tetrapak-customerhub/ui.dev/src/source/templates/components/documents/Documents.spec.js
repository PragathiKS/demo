import Documents from './Documents';

describe('Documents', function () {
  before(function () {
    this.documents = new Documents({
      el: document.body
    });
  });
});
