import ContactAnchorLink from './ContactAnchorLink';
import $ from 'jquery';
import contactAnchorTemplate from '../../../test-templates-hbs/contactAnchorLink.hbs';

describe('ContactAnchorLink', function () {
  before(function () {
    $(document.body).empty().html(contactAnchorTemplate());
    this.contactAnchor = new ContactAnchorLink({ el: document.body });
    this.initContactAnchorSpy = sinon.spy(this.contactAnchor, 'init');
    this.goToContactFormSpy = sinon.spy(this.contactAnchor, 'goToContactForm');
    this.contactAnchor.init();

  });
  after(function () {
    $(document.body).empty();
    this.initContactAnchorSpy.restore();
    this.goToContactFormSpy.restore();
  });
  it('should initialize', function () {
    expect(this.contactAnchor.init.called).to.be.true;
  });
  it('should go to contact form position on click', function () {
    $('.pw-contactAnchorLink').trigger('click');
    expect(this.contactAnchor.goToContactForm.called).to.be.true;
  });
});
