import $ from 'jquery';
import PlantMasterLicensesSite from './PlantMasterLicenses-site';
import PlantMasterSiteLicensesTemplate from '../../../test-templates-hbs/PlantMasterSiteLicenses.hbs';

describe('PlantMasterLicensesSite', function () { 
      
    before(function () {
        $(document.body).empty().html(PlantMasterSiteLicensesTemplate());
        this.siteLicenses = new PlantMasterLicensesSite({
          el: document.body,
        });
      
        this.initSpy = sinon.spy(this.siteLicenses, 'init');        
        this.addErrorMsgSpy = sinon.spy(this.siteLicenses, 'addErrorMsg');
        this.submitRequestFormSpy = sinon.spy(this.siteLicenses, 'submitRequestForm');
        this.siteLicenses.init();
  
    });
  
    after(function () {
      $(document.body).empty();
      this.initSpy.restore();
      this.submitRequestFormSpy.restore();
      this.addErrorMsgSpy.restore();
    });
  
    it('should initialize', function (done) {
      expect(this.initSpy.called).to.be.true;
      done();
    });

    it('should render form validation errors', function (done) {
        $('.js-tp-aip-licenses-site__btn').trigger('click');
        expect(this.addErrorMsgSpy.called).to.be.true;
        done();
    });

    it('should submit form', function (done) {
        $('#nameOfSite').val('Test Data');
        $('#locationOfSite').val('Test Data');
        $('#application option:eq(1)').prop('selected', true);
        $('#basic-unit').val('Test Data');
        $('#advanced-unit').val('Test Data');
        $('.js-tp-aip-licenses-site__btn').trigger('click');
        expect(this.submitRequestFormSpy.called).to.be.true;
        done();
      });
})
  