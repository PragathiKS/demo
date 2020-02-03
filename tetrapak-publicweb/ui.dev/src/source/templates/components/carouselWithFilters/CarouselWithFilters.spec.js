import CarouselWithFilters from './CarouselWithFilters';
import $ from 'jquery';
import carouselWFiltersTemplate from '../../../test-templates-hbs/carouselWithFilters.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

describe('CarouselWithFilters', function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    }
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  }
  before(function () {
    $(document.body).empty().html(carouselWFiltersTemplate());
    this.carouselWFilters = new CarouselWithFilters({ el: document.body });
    this.initCarouselWFiltersSpy = sinon.spy(this.carouselWFilters, 'init');
    this.renderSubcatSpy = sinon.spy(this.carouselWFilters, 'renderSubcategories');
    this.renderPracticeSpy = sinon.spy(this.carouselWFilters, 'renderPractice');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse([{}]));
    this.carouselWFilters.init();
  });
  after(function () {
    $(document.body).empty();
    this.initCarouselWFiltersSpy.restore();
    this.renderSubcatSpy.restore();
    this.renderPracticeSpy.restore();
    this.ajaxStub.restore();
  });
  it('should initialize', function () {
    expect(this.carouselWFilters.init.called).to.be.true;
  });
  it('should render subcategories on click', function () {
    $('.pw-carousel__filters .js-filter-category .dropdown-item').first().trigger('click');
    expect(this.carouselWFilters.renderSubcategories.called).to.be.true;
  });
  it('should render practice on click', function () {
    $('.pw-carousel__filters .js-filter-subcategory .dropdown-item').first().trigger('click');
    expect(this.carouselWFilters.renderPractice.called).to.be.true;
  });
});
