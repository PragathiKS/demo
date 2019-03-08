import OrderingCard from './OrderingCard';
import $ from 'jquery';
import { ajaxWrapper } from '../../../scripts/utils/ajax';
import { render } from '../../../scripts/utils/render';
import orderingCardData from './data/orderingCardData.json';
import orderingCardTemplate from '../../../test-templates-hbs/orderingcard.hbs';

describe('OrderingCard', function () {
  const jqRef = {};
  function ajaxResponse(response) {
    const pr = $.Deferred();
    pr.resolve(response, 'success', jqRef);
    return pr.promise();
  }
  before(function () {
    $(document.body).empty().html(orderingCardTemplate());
    this.orderingCard = new OrderingCard({ el: document.body });
    this.initSpy = sinon.spy(this.orderingCard, 'init');
    this.settingsSpy = sinon.spy(this.orderingCard, 'openSettingsPanel');
    this.saveSettingsSpy = sinon.spy(this.orderingCard, 'saveSettings');
    this.renderSpy = sinon.spy(render, 'fn');
    this.orderDetailSpy = sinon.spy(this.orderingCard, 'openOrderDetails');
    this.stopEvtPropSpy = sinon.spy(this.orderingCard, 'stopEvtProp');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(orderingCardData));
    this.openStub = sinon.stub(window, 'open');
    this.orderingCard.init();
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.settingsSpy.restore();
    this.saveSettingsSpy.restore();
    this.renderSpy.restore();
    this.orderDetailSpy.restore();
    this.stopEvtPropSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
  });
  it('should initialize', function () {
    expect(this.orderingCard.init.called).to.be.true;
  });
  it('should render component on page load', function () {
    expect(render.fn.called).to.be.true;
  });
  it('should open settings panel on click of settings icon', function () {
    $('.js-ordering-card__settings').trigger('click');
    expect(this.orderingCard.openSettingsPanel.called).to.be.true;
  });
  it('should save settings on click of save settings button', function () {
    this.ajaxStub.restore();
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    this.ajaxStub.returns(ajaxResponse({
      status: 'success'
    }));
    $('.js-ordering-card__modal-save').trigger('click');
    expect(this.orderingCard.saveSettings.called).to.be.true;
  });
  it('should display error message if settings are not saved', function () {
    this.ajaxStub.returns(ajaxResponse({
      status: 'failure'
    }));
    $('.js-ordering-card__modal-save').trigger('click');
    expect($('.js-ordering-card__save-error').hasClass('d-none')).to.be.false;
  });
  it('should redirect to order detail page on click of order summary row', function () {
    const rowLink = $('.js-ordering-card__row').first();
    if (rowLink.length) {
      rowLink.trigger('click');
      expect(this.orderingCard.openOrderDetails.called).to.be.true;
    }
  });
  it('should open default calling or email app depending upon device', function () {
    const tableLink = $('.js-ordering-card__row').first().find('a').first();
    if (tableLink.length) {
      tableLink.trigger('click');
      expect(this.orderingCard.stopEvtProp.called).to.be.true;
    }
  });
});
