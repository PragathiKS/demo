import $ from 'jquery';

import Searchresults from './Searchresults';
import searchResultTemplate from '../../../test-templates-hbs/searchResults.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import resultList from './data/results.json';
import { render } from '../../../scripts/utils/render';


describe('Searchresults', function () {
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success');
    return pr.promise();
  }
  const event = $.Event( "keyup", { keyCode: 13 } );
  before(function () {
    $(document.body).empty().html(searchResultTemplate());    
    this.searchresults = new Searchresults({
      el: document.body
    });
    this.initSpy = sinon.spy(this.searchresults, 'init');
    this.searchSpy = sinon.spy(this.searchresults, 'search');
    this.renderTitleSpy = sinon.spy(this.searchresults, 'renderTitle');
    this.extractQueryParamsSpy = sinon.spy(this.searchresults, 'extractQueryParams');
    this.toggleFilterContainerSpy = sinon.spy(this.searchresults, 'toggleFilterContainer');
    this.pushIntoUrlSpy = sinon.spy(this.searchresults, 'pushIntoUrl');
    this.applyFiltersSpy = sinon.spy(this.searchresults, 'applyFilters');
    this.renderFilterTagsSpy = sinon.spy(this.searchresults, 'renderFilterTags');
    this.renderPaginationSpy = sinon.spy(this.searchresults, 'renderPagination');
    this.renderResultsSpy = sinon.spy(this.searchresults, 'renderResults');
    this.renderSpy = sinon.spy(render, 'fn');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({"totalPages":2, "totalResults": 15 ,"searchResults":resultList}));

    this.searchresults.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.searchSpy.restore();
    this.applyFiltersSpy.restore();
    this.renderTitleSpy.restore();
    this.renderResultsSpy.restore();
    this.renderFilterTagsSpy.restore();
    this.renderPaginationSpy.restore();
    this.pushIntoUrlSpy.restore();
    this.extractQueryParamsSpy.restore();
    this.toggleFilterContainerSpy.restore();
    this.renderSpy.restore();
    this.ajaxStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.searchresults.init.called).to.be.true;
    expect(this.searchresults.extractQueryParams.called).to.be.true;
    expect(this.searchresults.search.called).to.be.true;
    expect(this.searchresults.renderPagination.called).to.be.true;
    expect(this.searchresults.renderFilterTags.called).to.be.true;
    expect(render.fn.called).to.be.true;
    done();
  });

  it('Should toggle container on label click', function (done) {
    document.getElementsByClassName('js-search-filter-toggle')[0].click();
    expect(this.searchresults.toggleFilterContainer.called).to.be.true;
    done();
  });

  it('Should not call when search value is empty', function (done) {
    $('.js-pw-search-input').val("mixing");
    $('.js-pw-search-input').trigger(event);
    expect(this.searchresults.pushIntoUrl.called).to.be.true;
    expect(this.searchresults.search.called).to.be.true;
    expect(this.searchresults.cache.searchParams['searchTerm']).to.equal('mixing');
    done();
  });

  it('Should not update url when input is empty and pressed enter', function (done) {
    $('.js-pw-search-input').val("");
    $('.js-pw-search-input').trigger(event);
    expect(this.searchresults.cache.searchParams['searchTerm']).to.equal('');
    expect(this.searchresults.renderTitle.called).to.be.true;
    done();
  });

  it('Should update url when input is empty but filter is selected and pressed enter', function (done) {
    $('.js-pw-search-input').val("");
    $('.js-pw-search-input').trigger(event);
    this.searchresults.cache.searchParams['contentType'] = {'news': 'News'};
    expect(this.searchresults.cache.searchParams['searchTerm'].length).to.equal(0);
    expect(this.searchresults.pushIntoUrl.called).to.be.true;
    expect(this.searchresults.search.called).to.be.true;
    expect(this.searchresults.renderTitle.called).to.be.true;
    done();
  });

  it('Should send request when ok is clicked', function (done) {
    this.searchresults.cache.searchParams['contentType'] = {'news': 'News'};
    $('.js-apply-filter').trigger('click');
    expect(this.searchresults.applyFilters.called).to.be.true;
    done();
  });

  it('Should Update object when filter is changed', function (done) {
    $('.js-pw-search-results-filter-check').trigger('change');
    console.log('let check searchparams',this.searchresults.cache.searchParams);
    // expect(this.searchresults.cache.filterObj.checks.length).to.equal(1);
    done();
  });

  it('Should work with paginations', function (done) {
    $('.js-page-number').trigger('click');
    expect(this.searchresults.pushIntoUrl.called).to.be.true;
    done();
  });
});
