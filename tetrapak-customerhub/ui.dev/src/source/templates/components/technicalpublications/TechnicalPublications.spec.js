import $ from "jquery";
import TechnicalPublications from "./TechnicalPublications";
import technicalPublicationsTmpl from "../../../test-templates-hbs/technicalpublications.hbs";
import { ajaxWrapper } from "../../../scripts/utils/ajax";
import auth from "../../../scripts/utils/auth";

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
      .returns(ajaxResponse({ statusCode: "200" }));
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
  it("should show spinner", function (done) {
    $(".js-tech-pub__folder-btn").trigger("click");
    expect(this.techPub.init.called).to.be.true;
    done();
  });
  it("should get folder data", function (done) {
    $(".js-tech-pub__folder-btn").trigger("click");
    expect(this.techPub.getFolderData.called).to.be.true;
    done();
  });
  it("should render breadcrumbs", function (done) {
    $(".js-tech-pub__folder-btn").trigger("click");
    expect(this.techPub.renderBreadcrumbs.called).to.be.true;
    done();
  });
  it("should renders files data", function (done) {
    $(".js-tech-pub__folder-btn").trigger("click");
    expect(this.techPub.renderFolderData.called).to.be.true;
    done();
  });
  it("should set folder nav data", function (done) {
    $(".js-tech-pub__folder-btn").trigger("click");
    expect(this.techPub.renderFolderData.called).to.be.true;
    done();
  });
  it("should render i18n values", function (done) {
    const innerHeaderText = $($(".tp-tech-pub__thead-primary").get(0).children[0].innerHTML).text();
    const innerBreadcrumbText = $(".tp-tech-pub__bc-item > button").get(0).innerHTML;

    expect(innerHeaderText).to.equal('cuhu.technicalpublications.country');
    expect(innerBreadcrumbText).to.equal('cuhu.technicalpublications.allFiles');

    done();
  });
});
