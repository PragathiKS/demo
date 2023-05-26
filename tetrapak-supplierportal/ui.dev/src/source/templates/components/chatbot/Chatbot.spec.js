import Chatbot from './Chatbot';

describe('Chatbot', function () {
  before(function () {
    this.chatbot = new Chatbot({
      el: document.body
    });
  });
});
