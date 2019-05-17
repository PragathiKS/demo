import Carousel from './Carousel';
import $ from 'jquery';
import carouselTemplate from '../../../test-templates-hbs/carousel.hbs';

describe('Carousel', function () {
  before(function () {
    $(document.body).empty().html(carouselTemplate());
    this.carousel = new Carousel({ el: document.body });
    this.initCarouselSpy = sinon.spy(this.carousel, 'init');
    this.carousel.init();

  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.initCarouselSpy.restore();
  });
  it('should initialize', function () {
    expect(this.carousel.init.called).to.be.true;
  });
});
