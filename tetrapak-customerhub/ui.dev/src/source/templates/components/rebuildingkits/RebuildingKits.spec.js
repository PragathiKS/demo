import $ from "jquery";
import RebuildingKits from "./RebuildingKits";
import rebuildingKitsTmpl from "../../../test-templates-hbs/rebuildingkits.hbs";
import { ajaxWrapper } from "../../../scripts/utils/ajax";
import auth from "../../../scripts/utils/auth";
import countries from "./data/countries.json";

describe("RebuildingKits", function () {
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
  before(function () {
    this.enableTimeouts(false);
    $(document.body).empty().html(rebuildingKitsTmpl());
    this.rk = new RebuildingKits({
      el: document.body,
    });
    this.initSpy = sinon.spy(this.rk, "init");
    this.bindEventsSpy = sinon.spy(this.rk, "bindEvents");
    this.renderDefaultCountrySpy = sinon.spy(this.rk, "renderDefaultCountry");
    this.renderNewPageSpy = sinon.spy(this.rk, "renderNewPage");
    this.renderPaginationTableDataSpy = sinon.spy(this.rk, "renderPaginationTableData");
    this.applyFilterSpy = sinon.spy(this.rk, "applyFilter");
    this.renderFilterFormSpy = sinon.spy(this.rk, "renderFilterForm");
    this.downloadCsv = sinon.spy(this.rk, "downloadCsv");
    this.renderNewCountry = sinon.spy(this.rk, "renderNewCountry");
    this.showHideAllFilters = sinon.spy(this.rk, "showHideAllFilters");
    this.deleteAllFilters = sinon.spy(this.rk, "deleteAllFilters");
    this.ajaxStub = sinon.stub(ajaxWrapper, "getXhrObj");
    this.ajaxStub
      .yieldsTo("beforeSend", jqRef)
      .returns(ajaxResponse(countries));
    this.tokenStub = sinon.stub(auth, "getToken").callsArgWith(0, {
      data: {
        access_token: "fLW1l1EA38xjklTrTa5MAN7GFmo2",
        expires_in: "43199",
        token_type: "BearerToken",
      },
    });

    this.rk.init();
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
    this.downloadCsv.restore();
    this.renderNewCountry.restore();
    this.showHideAllFilters.restore();
    this.deleteAllFilters.restore();
  });
  it("should initialize", function (done) {
    expect(this.rk.init.called).to.be.true;
    expect(this.rk.bindEvents.called).to.be.true;
    done();
  });
  it("should get list of rebuildingkits", function (done) {
    expect(this.rk.renderDefaultCountry.called).to.be.true;
    done();
  });
  it("should get next/prev page data", function (done) {
    $(".js-page-number").trigger("click");
    expect(this.rk.renderNewPage.called).to.be.true;
    done();
  });
  it("should open filter form on button click", function (done) {
    $(".js-rk__customise-table-action").trigger("click");
    expect(this.rk.renderFilterForm.called).to.be.true;
    done();
  });
  it("should download csv on button click", function (done) {
    $(".js-rk__export-csv-action").trigger("click");
    expect(this.rk.downloadCsv.called).to.be.true;
    done();
  });
  it("should apply filters", function (done) {
    $(".tp-rk__functionalLocation-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;

    $(".tp-rk__equipmentDescription-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;


    $(".tp-rk__serialNumber-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;

    $(".tp-rk__rkNumber-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;
    
    $(".tp-rk__rkDesc-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;
    
    $(".tp-rk__implStatus-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;
    
    $(".tp-rk__machineSystem-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;
    
    $(".tp-rk__equipmentStatus-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;

    $(".tp-rk__rkType-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;

    $(".tp-rk__rkStatus-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;

    $(".tp-rk__rkHandling-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;

    $(".tp-rk__implDeadline-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;

    $(".tp-rk__plannedDate-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;

    $(".tp-rk__releaseDate-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;

    $(".tp-rk__generalRkNumber-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.applyFilter.called).to.be.true;
    
    done();
  });
  it("should render new country on country filter change", function (done) {
    $(".tp-rk__country-button-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk.renderNewCountry.called).to.be.true;
    done();
  });
  it("should hide filters on button click", function (done) {
    $(".js-tp-rk__show-hide-all-button").trigger("click");
    expect(this.rk.showHideAllFilters.called).to.be.true;
    done();
  });
  it("should delete all filters on button click", function (done) {
    $(".tp-rk__functionalLocation-filter").trigger("click");
    $(".js-tp-rk__remove-all-button").trigger("click");
    expect(this.rk.deleteAllFilters.called).to.be.true;
    done();
  });
});
