import $ from 'jquery';
import PlantMasterLicenses from './PlantMasterLicenses';
import pingUserData from './data/plantMasterLicenses.json';
import PlantMasterLicensesTemplate from '../../../test-templates-hbs/PlantMasterLicenses.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

describe('PlantMasterLicenses', function () {
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
        $(document.body).empty().html(PlantMasterLicensesTemplate());
        this.licenses = new PlantMasterLicenses(
            document.body,
        );

        this.initSpy = sinon.spy(this.licenses, 'init');
        this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
        this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(pingUserData));
        this.licenses.init();

    });

    after(function () {
      $(document.body).empty();
      this.initSpy.restore();
      this.ajaxStub.restore();
    });

    it('should initialize', function (done) {
        expect(this.initSpy.called).to.be.true;
        done();
    });
})