import $ from 'jquery';
import InactivityDialog from './InactivityDialog';
import inactivityDialog from '../../../test-templates-hbs/inactivitydialog.hbs';

describe('InactivityDialog', function () {
  before(function () {
    $(document.body).empty().html(inactivityDialog());
    this.inactivityDialog = new InactivityDialog({
      el: document.body
    });
    this.clock = sinon.useFakeTimers();
    this.initSpy = sinon.spy(this.inactivityDialog, 'init');
    this.idleTimerSpy = sinon.spy(this.inactivityDialog, 'setIdleTimer');
    this.closeDialogSpy = sinon.spy(this.inactivityDialog, 'closeModal');
    this.logoutSpy = sinon.spy(this.inactivityDialog, 'logoutSession');
    this.inactivityDialog.init();
    this.inactivityDialog.cache.counter = 15;
  });
  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.idleTimerSpy.restore();
    this.closeDialogSpy.restore();
    this.logoutSpy.restore();
    this.clock.restore();
  });
  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });
  it('should call setIdleTimer on page load', function (done) {
    expect(this.idleTimerSpy.called).to.be.true;
    done();
  });
  it('should call setIdleTimer on scroll event', function (done) {
    this.idleTimerSpy.restore();
    this.idleTimerSpy = sinon.spy(this.inactivityDialog, 'setIdleTimer');
    $(window).one('scroll', () => {
      this.clock.tick(15 * 60 * 60);
      expect(this.idleTimerSpy.called).to.be.true;
      done();
    });
    $(window).trigger('scroll');
  });
  it('should close modal on click of close button', function (done) {
    $('.js-close-btn').trigger('click');
    expect(this.closeDialogSpy.called).to.be.true;
    done();
  });
  it('should close modal on click of continue button', function (done) {
    $('.js-inactivity-modal__continue').trigger('click');
    expect(this.closeDialogSpy.called).to.be.true;
    done();
  });
  it('should logout session on click of logout button', function (done) {
    $('.js-inactivity-modal__logout').trigger('click');
    expect(this.logoutSpy.called).to.be.true;
    done();
  });
});
