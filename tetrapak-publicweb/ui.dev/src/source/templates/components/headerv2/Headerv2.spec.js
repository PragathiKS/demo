import Headerv2 from './Headerv2';
import $ from 'jquery';
import headerv2Template from '../../../test-templates-hbs/headerv2.hbs';
import { ajaxWrapper } from '../../../scripts/utils/ajax';

describe('Headerv2', function () {
  const jqRef = {
   setRequestHeader() {
    // Dummy method
   }
  };
  
  function setDom($this) {
    $(document.body).empty().html(headerv2Template());
    $this.headerv2 = new Headerv2({
        el: document.body
    });
  }

  function ajaxResponse(response, operation = 'success') {
    const pr = $.Deferred();
    if(operation === 'success') {
      pr.resolve(response, operation, jqRef);
    } else {
      pr.reject(response);
    }
    
    return pr.promise();
  };

  before(function () {
    //window.onbeforeunload = () => 'reload!';
    $(document.body).empty().html(headerv2Template());
    this.headerv2 = new Headerv2({ el: document.body });
    this.initSpy = sinon.spy(this.headerv2, 'init');
    this.searchIconClickSpy = sinon.spy(this.headerv2, 'searchIconClick');
    this.marketSelectorClickSpy = sinon.spy(this.headerv2, 'openMarketSelector');
    this.openMobileMenuClickSpy = sinon.spy(this.headerv2, 'openMobileMenu');
    this.openSubmenuClickSpy = sinon.spy(this.headerv2,'openSubmenuEvent');
    this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
    const apiResponse = {data :[]};
    this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse(apiResponse));
    this.openStub = sinon.stub(window, 'open');
    this.headerv2.init();
  });

  after(function () {
    $(document.body).empty();
    this.initSpy.restore();
    this.searchIconClickSpy.restore();
    this.marketSelectorClickSpy.restore();
    this.openMobileMenuClickSpy.restore();
    this.openSubmenuClickSpy.restore();
    this.ajaxStub.restore();
    this.openStub.restore();
  });

  it('should initialize', function (done) {
    expect(this.initSpy.called).to.be.true;
    done();
  });

  it('should call searchIconClick on click', function (done) {
    $('.js-submenu-mobile-icon').addClass('icon-Close_pw').removeClass('icon-Burger_pw');
    $('.tp-pw-headerv2-search-box-toggle').trigger('click');
    expect(this.headerv2.searchIconClick.called).to.be.true;
    done();
  });

  it('should call openMarketSelector on click', function (done) {
    $('.js-header__selected-lang-pw').trigger('click');
    expect(this.headerv2.openMarketSelector.called).to.be.true;
    done();
  });

  it('should call openMobileMenu on click', function (done) {
    $('.tp-pw-headerv2-search-box-toggle > i').addClass('icon-Close_pw').removeClass('icon-Search_pw');;
    $('.js-submenu-mobile-icon').trigger('click');
    expect(this.headerv2.openMobileMenu.called).to.be.true;
    done();
  });

  it('should call openMobileMenu on close button click', function (done) {
    $('.js-submenu-mobile-icon').addClass('icon-Close_pw').removeClass('icon-Burger_pw');
    $('.js-submenu-mobile-icon').trigger('click');
    expect(this.headerv2.openMobileMenu.called).to.be.true;
    done();
  });

  it('should call openSubmenuEvent on click', function (done) {
    $('.js-submenu-icon').trigger('click');
    expect(this.headerv2.openSubmenuEvent.called).to.be.true;
    done();
  });

  it('should call openSubmenuEvent on click to disable', function (done) {
    $('.js-submenu-icon').addClass('active');
    $('.js-submenu-icon').trigger('click');
    expect(this.headerv2.openSubmenuEvent.called).to.be.true;
    done();
  });

  it('should open submenu page on link click', function (done) {
    $('.js-submenu-mobile-icon').trigger('click');
    $('.js-megamenulink-0-mobile').trigger('click', function(e) {
        e.preventDefault();
        this.ajaxStub.restore();
        this.ajaxStub = sinon.stub(ajaxWrapper, 'getXhrObj');
        this.ajaxStub.yieldsTo('beforeSend', jqRef).returns(ajaxResponse({ status: 200 }));
        expect($('.tp-pw-megamenu-mobile-subpage-0').hasClass('hidden')).to.be.false;
    });
    done();
  });

  it('should open mega menu on mouse enter', function (done) {
   $('.js-megamenulink-first').trigger('mouseenter', function(e) {
       e.preventDefault();
   });
   expect($('.js-megamenu-0').hasClass('hidden')).to.be.false;
   done();
 });

 it('should close mega menu on mouse leave', function (done) {
    $('.js-megamenulink-first').trigger('mouseenter', function(e) {
        e.preventDefault();
    });
    $('.js-megamenulink-first').trigger('mouseleave', function(e) {
        e.preventDefault();
    });
    expect($('.js-megamenu-0').hasClass('hidden')).to.be.true;
    done();
  });

  it('should close mega menu on window resize', function (done) {
    $('.submenu-mobile-icon').addClass('icon-Close_pw').removeClass('icon-Burger_pw');;
    $(window).trigger('resize');
    expect($('.submenu-mobile-icon').hasClass('icon-Burger_pw')).to.be.true;
    done();
  });

});
