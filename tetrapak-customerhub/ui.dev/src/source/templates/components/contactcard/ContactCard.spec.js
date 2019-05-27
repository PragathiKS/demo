import ContactCard from './ContactCard';
import ContactCardTemplate from '../../../test-templates-hbs/contactCard.hbs';
import $ from 'jquery';

describe('ContactCard', function () {
  before(function () {
    $(document.body).empty().html(ContactCardTemplate());
    this.contactCard = new ContactCard({
      el: document.body
    });
    this.initSpy = sinon.spy(this.contactCard, 'init');
    this.trackAnalyticsSpy = sinon.spy(this.contactCard, 'trackAnalytics');
    this.contactCard.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
  });

  it('should initialize', function (done) {
    expect(this.contactCard.init.called).to.be.true;
    done();
  });
  it('should trigger trackAnalytics on click of view contact btn ', function (done) {
    $('.js-view-contact-card-btn').trigger('click');
    expect(this.trackAnalyticsSpy.called).to.be.true;
    done();
  });
});
