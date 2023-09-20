import $ from 'jquery';
import MyEquipment from './MyEquipment';
import myequipmentTmpl from '../../../test-templates-hbs/myequipment.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';
import  filters  from '../../../scripts/utils/filters';
import countries from './data/countries.json';



describe('MyEquipment', function () {
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
    this.enableTimeouts(false);
    $(document.body).empty().html(myequipmentTmpl());
    this.myequipment = new MyEquipment({
      el: document.body
    });
    this.initSpy = sinon.spy(this.myequipment, 'init');
    this.bindEventsSpy = sinon.spy(this.myequipment, 'bindEvents');
    this.renderDefaultCountrySpy = sinon.spy(this.myequipment, 'renderDefaultCountry');
    this.renderNewPageSpy = sinon.spy(this.myequipment, 'renderNewPage');
    this.renderPaginationTableDataSpy = sinon.spy(this.myequipment, 'renderPaginationTableData');
    this.applyFilterSpy = sinon.spy(this.myequipment, 'applyFilter');
    this.renderFilterFormSpy = sinon.spy(this.myequipment, 'renderFilterForm');
    this.downloadExcel = sinon.spy(this.myequipment, 'downloadExcel');
    this.sortTableByKey = sinon.spy(this.myequipment, 'sortTableByKey');
    this.renderNewCountry = sinon.spy(this.myequipment, 'renderNewCountry');
    this.deleteAllFilters = sinon.spy(this.myequipment, 'deleteAllFilters');
    this.updateFilterCountValue= sinon.spy(this.myequipment, 'updateFilterCountValue');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub
      .yieldsTo('beforeSend', jqRef)
      .returns(ajaxResponse(countries));
    this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
      data: {
        access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
        expires_in: '43199',
        token_type: 'BearerToken'
      }
    });
    this.myequipment.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
    this.renderDefaultCountrySpy.restore();
    this.renderNewPageSpy.restore();
    this.renderPaginationTableDataSpy.restore();
    this.applyFilterSpy.restore();
    this.renderFilterFormSpy.restore();
    this.downloadExcel.restore();
    this.sortTableByKey.restore();

    this.renderNewCountry.restore();
    this.deleteAllFilters.restore();
    this.updateFilterCountValue.restore();
  });
  it('should initialize', function (done) {
    expect(this.myequipment.init.called).to.be.true;
    expect(this.myequipment.bindEvents.called).to.be.true;
    done();
  });
  it('should get list of myequipment', function (done) {
    expect(this.myequipment.renderDefaultCountry.called).to.be.true;
    done();
  });
  it('should get next/prev page data', function (done) {
    $('.js-page-number').trigger('click');
    expect(this.myequipment.renderNewPage.called).to.be.true;
    done();
  });
  it('should open filter form on button click', function (done) {

    $('.js-my-equipment__customise-table-action').trigger('click');
    expect(this.myequipment.renderFilterForm.called).to.be.true;
    done();
  });
  it('should download excel on button click', function (done) {
    $('.js-my-equipment__export-excel-action').trigger('click');
    expect(this.myequipment.downloadExcel.called).to.be.true;
    done();
  });

  it('Sort Table row', function(done){
    $('.js-my-equipment__table-summary__sort').trigger('click');
    expect(this.myequipment.sortTableByKey.called).to.be.true;
    done();
  });
  it('should open equipment details', function(done){
    $('.js-my-equipment__table-summary__row').trigger('click');
    expect(this.myequipment.sortTableByKey.called).to.be.true;
    done();
  });
 
  
  it('should render new country on country filter change', function (done) {
    $('.tp-my-equipment__country-button-filter').trigger('click');
    $('.js-apply-filter-button').trigger('click');
    expect(this.myequipment.renderNewCountry.called).to.be.true;
    done();
  });
  it('update Filter Button Count', function (done) {
    $('.tp-my-equipment__filter-button').trigger('click');
    $('.tp-my-equipment__customer-button-filter').trigger('click');
    $('.js-apply-filter-button').trigger('click');
    expect(this.myequipment.updateFilterCountValue.called).to.be.true;
    done();
  });
  
  it('should apply filters', function (done) {
    $('.tp-my-equipment__equip-desc-button-filter').trigger('click');
    $('.js-apply-filter-button').trigger('click');
    expect(this.myequipment.applyFilter.called).to.be.true;

    $('.js-tp-my-equipment__remove-button').trigger('click');
    expect(this.myequipment.applyFilter.called).to.be.true;
    
    $('.tp-my-equipment__customer-button-filter').trigger('click');
    $('.js-apply-filter-button').trigger('click');
    expect(this.myequipment.applyFilter.called).to.be.true;

    $('.tp-my-equipment__filter-button').trigger('click');
    $('.js-tp-my-equipment__remove-button').trigger('click');
    expect(this.myequipment.applyFilter.called).to.be.true;
    
    $('.tp-my-equipment__line-button-filter').trigger('click');
    $('.js-apply-filter-button').trigger('click');
    expect(this.myequipment.applyFilter.called).to.be.true;
    
    $('.tp-my-equipment__status-button-filter').trigger('click');
    $('.js-apply-filter-button').trigger('click');
    expect(this.myequipment.applyFilter.called).to.be.true;
    
    $('.tp-my-equipment__equip-type-button-filter').trigger('click');
    $('.js-apply-filter-button').trigger('click');
    expect(this.myequipment.applyFilter.called).to.be.true;

    $('.tp-my-equipment__serial-button-filter ').trigger('click');
    $('.js-apply-filter-button').trigger('click');
    expect(this.myequipment.applyFilter.called).to.be.true;
    
    $('.js-my-equipment__customise-table-action').trigger('click');
    $('.js-apply-filter-button').trigger('click');
    expect(this.myequipment.applyFilter.called).to.be.true;

    done();
  });

  it('should delete all filters on button click', function (done) {
    $('.tp-my-equipment__customer-button-filter').trigger('click');
    $('.js-tp-my-equipment__remove-all-button').trigger('click');
    expect(this.myequipment.deleteAllFilters.called).to.be.true;
    done();
  });
  
})