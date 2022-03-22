import $ from 'jquery';
import PlantMasterLicensesSite from './PlantMasterLicenses-site';
import PlantMasterSiteLicensesData from './data/plantMasterLicenses-site.json';
import PlantMasterSiteLicensesTemplate from '../../../test-templates-hbs/PlantMasterSiteLicenses.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import auth from '../../../scripts/utils/auth';

describe('PlantMasterLicensesSite', function () {

    const jqRef = {
        setRequestHeader() {
          // Dummy method
        }
    };
    function ajaxResponse(response) {
        const pr = $.Deferred();
        pr.resolve(response, 'success', jqRef);
        return pr.promise();
    };
      
    before(function () {
        $(document.body).empty().html(PlantMasterSiteLicensesTemplate());
        this.siteLicenses = new PlantMasterLicensesSite(
            document.body,
        );
      
        this.initSpy = sinon.spy(this.siteLicenses, 'init');
        this.getSiteLicensesDataSpy = sinon.spy(this.siteLicenses, 'getSiteLicensesData');
        this.renderSiteLicensesDataSpy = sinon.spy(this.siteLicenses, 'renderSiteLicensesData');
        this.addErrorMsgSpy = sinon.spy(this.siteLicenses, 'addErrorMsg');
        this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
        this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(PlantMasterSiteLicensesData));
        this.tokenStub = sinon.stub(auth, 'getToken').callsArgWith(0, {
            data: {
              access_token: 'fLW1l1EA38xjklTrTa5MAN7GFmo2',
              expires_in: '43199',
              token_type: 'BearerToken'
            }
          });
        this.submitRequestFormSpy = sinon.spy(this.siteLicenses, 'submitRequestForm');
        this.siteLicenses.init();
  
    });
  
    after(function () {
      $(document.body).empty();
      this.initSpy.restore();
      this.submitRequestFormSpy.restore();
      this.addErrorMsgSpy.restore();
      this.getSiteLicensesDataSpy.restore();
      this.renderSiteLicensesDataSpy.restore();
      this.ajaxStub.restore();
      this.tokenStub.restore();
    });
  
    it('should initialize', function (done) {
      expect(this.initSpy.called).to.be.true;
      done();
    });

    it('should get data before rendering Site License Description sections', function (done) {
        expect(this.getSiteLicensesDataSpy.called).to.be.true;
        done();
    });

    it('should render Site License Description sections', function (done) {
        expect(this.renderSiteLicensesDataSpy.called).to.be.true;
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
 