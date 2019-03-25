import $ from 'jquery';
import Pagination from './Pagination';
import paginationTemplate from '../../../test-templates-hbs/pagination.hbs';

describe('Pagination', function () {
  before(function () {
    $(document.body).empty().html(paginationTemplate());
    this.pagination = new Pagination({
      el: '.js-pagination'
    });
    this.initSpy = sinon.spy(this.pagination, 'init');
    this.paginateSpy = sinon.spy(this.pagination, 'renderPagination');
    this.pageNavSpy = sinon.spy();
    this.pageDisabledSpy = sinon.spy();
    $('.js-pagination')
      .on('ordersearch.pagenav', this.pageNavSpy)
      .on('ordersearch.pagedisabled', this.pageDisabledSpy);
    this.pagination.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.paginateSpy.restore();
  });
  it('should initialize on page load', function () {
    expect(this.initSpy.called).to.be.true;
  });
  it('should render pagination component on "paginate" event', function () {
    $('.js-pagination').trigger('ordersearch.paginate', [{
      totalPages: 20,
      currentPage: 1
    }]);
    $('.js-pagination').trigger('ordersearch.paginate', [{
      totalPages: 20,
      currentPage: 10
    }]);
    $('.js-pagination').trigger('ordersearch.paginate', [{
      totalPages: 20,
      currentPage: 19
    }]);
    $('.js-pagination').trigger('ordersearch.paginate', [{
      totalPages: 4,
      currentPage: 1
    }]);
    expect(this.paginateSpy.called).to.be.true;
  });
  it('should trigger "pagenav" even on click of pagination button', function () {
    $('.js-page-number').eq(3).trigger('click');
    expect(this.pageNavSpy.called).to.be.true;
  });
  it('should disable pagination on "pagedisabled" event', function () {
    $('.js-pagination').trigger('ordersearch.pagedisabled');
    expect(this.pageDisabledSpy.called).to.be.true;
  });
});
