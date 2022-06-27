import $ from 'jquery';
import 'bootstrap';
import Keylines from './Keylines';
import assetsData from './data/keylines.json';
import KeylinesTemplate from '../../../test-templates-hbs/keylines.hbs';
import {ajaxWrapper} from '../../../scripts/utils/ajax';
import {render} from "../../../scripts/utils/render";
import { EVT_DROPDOWN_CHANGE } from '../../../scripts/utils/constants';

describe('Keylines', function () {
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
    $(document.body).empty().html(KeylinesTemplate());
    this.keylines = new Keylines({
      el: document.body
    });

    this.initSpy = sinon.spy(this.keylines, 'init');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse(assetsData));
    this.bindEventsSpy = sinon.spy(this.keylines, 'bindEvents');
    this.getShapeAssetsSpy = sinon.spy(this.keylines, 'getShapeAssets');
    this.renderModalSpy = sinon.spy(this.keylines, 'renderModal');
    this.setDownloadBtnSpy = sinon.spy(this.keylines, 'setDownloadBtn');
    this.renderSpy = sinon.spy(render, 'fn');

    this.keylines.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.renderSpy.restore();
    this.getShapeAssetsSpy.restore();
    this.renderModalSpy.restore();
    this.setDownloadBtnSpy.restore();
    this.ajaxStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    expect(this.bindEventsSpy.called).to.be.true;
    done();
  });

  it('should render modal when clicking download', function (done) {
    $('.js-tp-keylines__download').trigger('click');
    expect(this.getShapeAssetsSpy.called).to.be.true;
    expect(this.renderSpy.called).to.be.true;
    expect(this.renderModalSpy.called).to.be.true;
    done();
  });

  it('should enable download button after selecting dropdowns', function (done) {
    $('.js-tp-keylines__volumes').val('1000');
    $('.js-tp-keylines__volumes').trigger(EVT_DROPDOWN_CHANGE);
    $('.js-tp-keylines__openings').val('no-opening');
    $('.js-tp-keylines__openings').trigger(EVT_DROPDOWN_CHANGE);
    expect(this.setDownloadBtnSpy.called).to.be.true;
    done();
  });
})