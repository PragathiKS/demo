import $ from "jquery";
import RebuildingKits from "./RebuildingKits";
import rebuildingKitsTmpl from "../../../test-templates-hbs/rebuildingkits.hbs";
import { ajaxWrapper } from "../../../scripts/utils/ajax";
import auth from "../../../scripts/utils/auth";
import countries from "./data/countries.json";
import file from '../../../scripts/utils/file';

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
    this.bindEventsSpy = sinon.spy(this.rk, "_bindEvents");
    this.renderSpy = sinon.spy(this.rk, "_render");
    this.renderPaginationTableDataSpy = sinon.spy(this.rk, "_renderPaginatedTable");
    this.applyFilterSpy = sinon.spy(this.rk, "_applyFilter");
    this.renderCustomiseTableFormSpy = sinon.spy(this.rk, "_renderCustomiseTableForm");
    this.downloadCsv = sinon.spy(this.rk, "_downloadCsv");
    this.applyFiltersVisibility = sinon.spy(this.rk, "_applyFiltersVisibility");
    this.deleteAllFilters = sinon.spy(this.rk, "_deleteAllFilters");
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
    this.fileStub = sinon.stub(file, 'get').returns(Promise.resolve());
    this.rk.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.bindEventsSpy.restore();
    this.ajaxStub.restore();
    this.tokenStub.restore();
    this.renderPaginationTableDataSpy.restore();
    this.applyFilterSpy.restore();
    this.renderCustomiseTableFormSpy.restore();
    this.downloadCsv.restore();
    this.applyFiltersVisibility.restore();
    this.deleteAllFilters.restore();
    this.fileStub.restore();
  });
  it("should initialize", function (done) {
    expect(this.rk.init.called).to.be.true;
    expect(this.rk._bindEvents.called).to.be.true;
    done();
  });
  it("should get list of rebuildingkits", function (done) {
    expect(this.rk._render.called).to.be.true;
    done();
  });
  it("should get next/prev page data", function (done) {
    $(".js-page-number").trigger("click");
    expect(this.rk._render.called).to.be.true;
    done();
  });
  it("should open customiseTable form on button click", function (done) {
    $(".js-filtered-table__customise-table-action").trigger("click");
    expect(this.rk._renderCustomiseTableForm.called).to.be.true;
    done();
  });
  it("should download csv on button click", function (done) {
    $(".js-export-btn").trigger("click");
    file.get().then(() => {
      expect(this.rk._downloadCsv.called).to.be.true;
      done();
    });
  });
  it("should apply filters", function (done) {
    $(".tp-filtered-table__functionalLocation-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;

    $(".tp-filtered-table__equipmentDescription-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;


    $(".tp-filtered-table__serialNumber-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;

    $(".tp-filtered-table__rkNumber-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;
    
    $(".tp-filtered-table__rkDesc-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;
    
    $(".tp-filtered-table__implStatus-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;
    
    $(".tp-filtered-table__machineSystem-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;
    
    $(".tp-filtered-table__equipmentStatus-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;

    $(".tp-filtered-table__rkType-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;

    $(".tp-filtered-table__rkStatus-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;

    $(".tp-filtered-table__rkHandling-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;

    $(".tp-filtered-table__implDeadline-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;

    $(".tp-filtered-table__plannedDate-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;

    $(".tp-filtered-table__releaseDate-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;

    $(".tp-filtered-table__generalRkNumber-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._applyFilter.called).to.be.true;
    
    done();
  });
  it("should render new country on country filter change", function (done) {
    $(".tp-filtered-table__country-button-filter").trigger("click");
    $(".js-apply-filter-button").trigger("click");
    expect(this.rk._render.called).to.be.true;
    done();
  });
  it("should hide filters on button click", function (done) {
    $(".js-tp-filtered-table__show-hide-all-button").trigger("click");
    expect(this.rk._applyFiltersVisibility.called).to.be.true;
    done();
  });
  it("should delete all filters on button click", function (done) {
    $(".tp-filtered-table__functionalLocation-filter").trigger("click");
    $(".js-tp-filtered-table__remove-all-button").trigger("click");
    expect(this.rk._deleteAllFilters.called).to.be.true;
    done();
  });
});