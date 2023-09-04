import $ from "jquery";
import TechnicalPublications from "./TechnicalPublications";
import technicalPublicationsTmpl from "../../../test-templates-hbs/technicalpublications.hbs";
import { ajaxWrapper } from "../../../scripts/utils/ajax";
import auth from "../../../scripts/utils/auth";
import countriesMockResponse from './mocks/countries.json';
import customersMockResponse from './mocks/customers.json';
import linesMockResponse from './mocks/lines.json';
import lineFoldersMockResponse from './mocks/equipments.json';
import serialNumberMockResponse from './mocks/serialNumber.json';

describe("TechnicalPublications", function () {
  const jqRef = {
    setRequestHeader() {
      // Dummy method
    },
  };
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, "success", jqRef);
    return pr.promise();
  }
  function setDom($this) {
    $(document.body).empty().html($this.domHtml);
    $this.techPub = new TechnicalPublications({
      el: document.body,
    });
  }

  before(function () {
    this.enableTimeouts(false);
    this.domHtml = technicalPublicationsTmpl();
    setDom(this);
    this.initSpy = sinon.spy(this.techPub, "init");
    this.showSpinnerSpy = sinon.spy(this.techPub, "showSpinner");
    this.getFolderDataSpy = sinon.spy(this.techPub, "getFolderData");
    this.renderBreadcrumbsSpy = sinon.spy(this.techPub, "renderBreadcrumbs");
    this.renderFolderDataSpy = sinon.spy(this.techPub, "renderFolderData");
    this.setFolderNavDataSpy = sinon.spy(this.techPub, "setFolderNavData");

    this.ajaxStub = sinon.stub(ajaxWrapper, "getXhrObj");
    this.ajaxStub
    .yieldsTo("beforeSend", jqRef)
    .returns(ajaxResponse(countriesMockResponse));

    this.tokenStub = sinon.stub(auth, "getToken").callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken"
      }
    });
    this.techPub.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.getFolderDataSpy.restore();
  });

  it("should initialize", function (done) {
    expect(this.techPub.init.called).to.be.true;
    done();
  });

  it("should correctly render the 'countries' step", function (done) {
    const innerHeaderText = $($(".tp-tech-pub__thead-primary").get(0).children[0].innerHTML).text().replace(/\s+/g, '');
    const innerBreadcrumbText = $(".tp-tech-pub__bc-item > button").get(0).innerHTML.replace(/\s+/g, '');
    const renderedBtnLabels = $('.js-tech-pub__folder-btn').map(function() {
      return $(this).text().trim();
    }).get();

    expect(this.techPub.init.called).to.be.true;
    expect(this.techPub.getFolderData.called).to.be.true;
    expect(this.techPub.renderBreadcrumbs.called).to.be.true;
    expect(this.techPub.renderFolderData.called).to.be.true;
    expect(innerHeaderText).to.equal('cuhu.technicalpublications.country');
    expect(innerBreadcrumbText).to.equal('cuhu.technicalpublications.allFiles');
    expect(renderedBtnLabels.includes('Sweden')).to.be.true;

    done();
  });

  it("should correctly render the 'country' step", function (done) {
    this.ajaxStub
    .yieldsTo("beforeSend", jqRef)
    .returns(ajaxResponse(customersMockResponse));
    $(".js-tech-pub__folder-btn").trigger("click");

    const innerHeaderText = $("thead.tp-tech-pub__thead-primary th:eq(1)").text();
    const innerBreadcrumbText = $(".tp-tech-pub__bc-item > button").get(0).innerHTML.replace(/\s+/g, '');
    const renderedBtnLabels = $('.js-tech-pub__folder-btn').map(function() {
      return $(this).text().trim();
    }).get();

    expect(innerHeaderText).to.equal('cuhu.technicalpublications.customer');
    expect(innerBreadcrumbText).to.equal('cuhu.technicalpublications.allFiles');
    expect(renderedBtnLabels.includes('Arla Foods Deutschland GmbH - Pronsfeld - 92451')).to.be.true;

    done();
  });

  it("should correctly render the 'customer' step", function (done) {
    this.ajaxStub
    .yieldsTo("beforeSend", jqRef)
    .returns(ajaxResponse(linesMockResponse));
    $(".js-tech-pub__folder-btn").trigger("click");

    const innerHeaderText = $("thead.tp-tech-pub__thead-primary th:eq(1)").text();
    const innerBreadcrumbText = $(".tp-tech-pub__bc-item > button").get(0).innerHTML.replace(/\s+/g, '');
    const renderedBtnLabels = $('.js-tech-pub__folder-btn').map(function() {
      return $(this).text().trim();
    }).get();

    expect(innerHeaderText).to.equal('cuhu.technicalpublications.line');
    expect(innerBreadcrumbText).to.equal('cuhu.technicalpublications.allFiles');
    expect(renderedBtnLabels.includes('Polling, Line H1, TPA3/S TBA200S')).to.be.true;

    done();
  });

  it("should correctly render the 'line' step", function (done) {
    this.ajaxStub
    .yieldsTo("beforeSend", jqRef)
    .returns(ajaxResponse(lineFoldersMockResponse));
    $(".js-tech-pub__folder-btn").trigger("click");

    const innerHeaderText = $("thead.tp-tech-pub__thead-primary th:eq(1)").text();
    const innerBreadcrumbText = $(".tp-tech-pub__bc-item > button").get(0).innerHTML.replace(/\s+/g, '');
    const renderedBtnLabels = $('.js-tech-pub__folder-btn').map(function() {
      return $(this).text().trim();
    }).get();

    expect(innerHeaderText).to.equal('cuhu.technicalpublications.lineEquipment');
    expect(innerBreadcrumbText).to.equal('cuhu.technicalpublications.allFiles');
    expect(renderedBtnLabels.includes('Polling, Line H3, TPA3/S TBA200S')).to.be.true;

    done();
  });

  it("should correctly render the 'lineFolders' step", function (done) {
    this.ajaxStub
    .yieldsTo("beforeSend", jqRef)
    .returns(ajaxResponse(serialNumberMockResponse));
    $(".js-tech-pub__folder-btn").trigger("click");

    const innerHeaderText = $("thead.tp-tech-pub__thead-primary th:eq(1)").text();
    const innerBreadcrumbText = $(".tp-tech-pub__bc-item > button").get(0).innerHTML.replace(/\s+/g, '');
    const renderedBtnLabels = $('.js-tech-pub__folder-btn').map(function() {
      return $(this).text().trim();
    }).get();

    expect(innerHeaderText).to.equal('cuhu.technicalpublications.documentType');
    expect(innerBreadcrumbText).to.equal('cuhu.technicalpublications.allFiles');
    expect(renderedBtnLabels.includes('OM - Operation Manual')).to.be.true;

    done();
  });

  it("should correctly render the 'folderDetails' step", function (done) {
    $(".js-tech-pub__folder-btn").trigger("click");

    const innerHeaderText = $("thead.tp-tech-pub__thead-primary th:eq(1)").text();
    const innerBreadcrumbText = $(".tp-tech-pub__bc-item > button").get(0).innerHTML.replace(/\s+/g, '');

    const renderedLinkLabels = $('.tpatom-link').map(function() {
      return $(this).text().trim();
    }).get();

    expect(innerHeaderText).to.equal('cuhu.technicalpublications.documentNumber');
    expect(innerBreadcrumbText).to.equal('cuhu.technicalpublications.allFiles');
    expect(renderedLinkLabels.includes('3526594-0107')).to.be.true;

    done();
  });

  it("should use previously saved data when navigating with breadcrumbs", function (done) {
    expect(this.ajaxStub.callCount).to.equal(5);

    $('.js-tech-pub__bc-btn').trigger('click');

    const innerHeaderText = $($(".tp-tech-pub__thead-primary").get(0).children[0].innerHTML).text().replace(/\s+/g, '');
    const innerBreadcrumbText = $(".tp-tech-pub__bc-item > button").get(0).innerHTML.replace(/\s+/g, '');
    const renderedBtnLabels = $('.js-tech-pub__folder-btn').map(function() {
      return $(this).text().trim();
    }).get();

    expect(innerHeaderText).to.equal('cuhu.technicalpublications.country');
    expect(innerBreadcrumbText).to.equal('cuhu.technicalpublications.allFiles');
    expect(renderedBtnLabels.includes('Sweden')).to.be.true;

    expect(this.ajaxStub.callCount).to.equal(5);

    done();
  })
});
