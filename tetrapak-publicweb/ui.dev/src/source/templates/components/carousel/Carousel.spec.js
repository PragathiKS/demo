import Carousel from './Carousel';
import $ from 'jquery';
import carouselTemplate from '../../../test-templates-hbs/carousel.hbs';
import { $body } from '../../../scripts/utils/commonSelectors';

describe('Carousel', function () {
  before(function () {
    $(document.body).empty().html(carouselTemplate());
    this.carousel = new Carousel({ el: '.js-pw-carousel' });
    this.initCarouselSpy = sinon.spy(this.carousel, 'init');
    this.showTabSpy = sinon.spy(this.carousel, 'showTab');
    this.carousel.init();

  });
  after(function () {
    $(document.body).empty();
    this.initCarouselSpy.restore();
    this.showTabSpy.restore();
  });
  it('should initialize', function () {
    expect(this.carousel.init.called).to.be.true;
  });
  it('should show tab on "show.bs.tab" event', function () {
    this.carousel.root.trigger('show.bs.tab');
    expect(this.showTabSpy.called).to.be.true;
  });
});
