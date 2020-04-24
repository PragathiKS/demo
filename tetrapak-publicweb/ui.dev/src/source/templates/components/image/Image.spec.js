import Image from './Image';
import $ from 'jquery';
import imageTemplate from '../../../test-templates-hbs/image.hbs';

describe('Image', function () {
  before(function () {
    $(document.body).empty().html(imageTemplate());
    this.image = new Image({el: document.body});
    this.initSpy = sinon.spy(this.image, 'init');
    this.trackAnalyticsSpy = sinon.spy(this.image, 'trackAnalytics');
    window.digitalData = {};
    window._satellite = {
      track() { /* Dummy method */ }
    };
    this.image.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.trackAnalyticsSpy.restore();
  });

  it('should initialize', function () {
    expect(this.image.init.called).to.be.true;
  });
  
  it('should call track analytics on click', function () {
    $('.js-tp-pw-image').trigger('click');
    expect(this.image.trackAnalytics.called).to.be.true;
  });

});
