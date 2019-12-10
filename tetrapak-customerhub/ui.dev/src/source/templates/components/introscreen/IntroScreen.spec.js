import $ from 'jquery';
import 'slick-carousel';
import IntroScreen from './IntroScreen';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import introScreen from '../../../test-templates-hbs/introScreen.hbs';

describe('IntroScreen', function () {
  const jqXHR = {};
  function ajaxResponse(response, reject) {
    const pr = $.Deferred();
    if (!reject) {
      pr.resolve(response, 'success', jqXHR);
    } else {
      pr.reject(jqXHR, 'error', {});
    }
    return pr.promise();
  }
  before(function () {
    $(document.body).empty().html(introScreen());
    this.introScreen = new IntroScreen({
      el: document.body,
      templates: {
        cuhuDot: () => (`
          <button type="button" class="tp-dot" role="tab" id="slick-slide-control00" aria-controls="slick-slide00" aria-label="1 of 3" tabindex="0" aria-selected="true">
          </button>
        `)
      }
    });
    this.initSpy = sinon.spy(this.introScreen, 'init');
    this.popupSpy = sinon.spy(this.introScreen, 'showOnboardingPopup');
    this.errorSpy = sinon.spy(this.introScreen, 'renderError');
    this.closeSpy = sinon.spy(this.introScreen, 'closeCarousel');
    this.analyticsSpy = sinon.spy(this.introScreen, 'trackAnalytics');
    this.dataStub = sinon.stub($.fn, 'data').returns(0);
    this.slickStub = sinon.stub($.prototype, 'slick').yieldsTo('customPaging');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({
      isOnboarded: false
    }));
    this.introScreen.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.popupSpy.restore();
    this.errorSpy.restore();
    this.closeSpy.restore();
    this.analyticsSpy.restore();
    this.dataStub.restore();
    this.slickStub.restore();
    this.ajaxStub.restore();
  });
  it('should initialize on page load', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should open onboarding popup for first time logged in user', function () {
    expect(this.popupSpy.called).to.be.true;
  });
  it('should render error message if onboarding call returns an empty response', function () {
    $(document.body).empty().html(introScreen());
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({}));
    this.introScreen.init(); // Initialize again
    expect(this.errorSpy.called).to.be.true;
  });
  it('should render error message if onboarding call fails', function () {
    $(document.body).empty().html(introScreen());
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({}, true));
    this.introScreen.init(); // Initialize again
    expect(this.errorSpy.called).to.be.true;
  });
  it('should close carousel on click of close button', function () {
    $('.js-close-btn').trigger('click');
    expect(this.closeSpy.called).to.be.true;
  });
  it('should slide to next slide on click on next button', function () {
    this.slickStub.restore();
    this.slickStub = sinon.stub($.prototype, 'slick');
    this.slickStub.returns({
      slideCount: 1
    });
    $('.js-slick-next').addClass('js-get-started-btn').trigger('click');
    expect(this.slickStub.called).to.be.true;
    $('.js-intro-slider').trigger('beforeChange', [
      {
        $slides: {
          length: 1
        }
      },
      0,
      1
    ]);
  });
  it('should slide slick slider on click of slider dots', function () {
    $('.js-slider-dots').trigger('click');
    expect(this.analyticsSpy.called).to.be.true;
  });
});
