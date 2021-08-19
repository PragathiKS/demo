import CookiePolicy from './CookiePolicy';

describe('CookiePolicy', function () {
  before(function () {
    const componentTemplate = `
      <div id="cookie-policy">
        <div class="pc-dark-filter"></div>
        <div class="otFloatingRoundedCorner" data-module="CookiePolicy">
          <button class="confirmation-button">Button_Text</button>
        </div>
      </div>
    `
    document.body.insertAdjacentHTML('afterbegin', componentTemplate);

    this.subject = new CookiePolicy({
        el: document.querySelector('#cookie-policy')
    });

    sinon.spy(this.subject, 'init');
    sinon.spy(this.subject, 'initCache');
    sinon.spy(this.subject, 'bindEvents');
    sinon.spy(this.subject, 'removeCookieBar');
    sinon.spy(this.subject, 'createCookie');

    this.subject.init();
    this.subject.initCache();
  });

  it('should initialize component', function () {
    expect(this.subject.init.called).to.be.true;
    expect(this.subject).to.be.instanceOf(CookiePolicy);
  });

  it('should intialize cache and bind events', function () {
      expect(this.subject.initCache.called).to.be.true;
      expect(this.subject.bindEvents.called).to.be.true;
  });

  it('should create new cookie, when cookie not exist', function () {
    this.subject.cache.$confirmationButton.trigger('click');

    expect(this.subject.removeCookieBar.called).to.be.true;
    expect(this.subject.createCookie.called).to.be.true;
  });

  it('should remove existing cookie and hide elements, when cookie exist', function () {
    let cookies = document.cookie.split(';');

    for (let i = 0; i < cookies.length; i++) {
      let cookie = cookies[i];
      const eqPos = cookie.indexOf("=");
      const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
      document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
    this.subject.bindEvents()

    expect(this.subject.removeCookieBar.called).to.be.true;
    expect(this.subject.cache.$cookiesOverlay.hasClass('hide')).to.be.true;
    expect(this.subject.cache.$cookiesBar.hasClass('hide')).to.be.true;
  });

  after(() => {
    document.body.removeChild(document.getElementById('cookie-policy'));
  });
})