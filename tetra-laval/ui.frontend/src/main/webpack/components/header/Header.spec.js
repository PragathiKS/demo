import Header from './Header';

describe('Header', function () {
  before(function () {
    const componentTemplate = `
      <header class="tp_pw-header-wrapper" data-module="Header">
        <div class="tp-pw-header__container">
            <nav class="tp_pw-header">
                <div class="tp_pw-header__mobile-nav">
                    <input class="tp_pw-header__mobile-nav-btn" type="checkbox" id="menu-btn" aria-label="Mobile menu"/>
                    <label class="tp_pw-header__mobile-nav-label" for="menu-btn">
                        <i class="tp_pw-header__mobile-nav-navicon"></i>
                    </label>
                    <ul class="tp_pw-header__mobile-nav-menu">
                        <li class="tp_pw-header__mobile-nav-menu-item tp_pw-header__mobile-nav-menu-mega">
                            <input class="mega-menu-btn" type="checkbox" id="mobile-navigation-next-lvl-menu-btn" aria-label="second menu"/>
                            <label for="mobile-navigation-next-lvl-menu-btn" class="d-flex justify-content-between">
                                <span class="tp_pw-header__link">Menu item A</span>
                                <i class="tp_pw-header-arrow icon-Arrow_Right_pw"></i>
                            </label>
                            <ul class="tp_pw-header__mobile-nav-menu-next-lvl-menu">
                                <li class="tp_pw-header__mobile-nav-menu-item">
                                    <label for="mobile-navigation-next-lvl-menu-btn" class="d-flex justify-content-between text-center" aria-label="main menu">
                                        <i class="tp_pw-header-arrow tp_pw-header-arrow--left icon-Arrow_Right_pw"></i>
                                        <span class="text-center flex-grow-1">Level 1, Menu item A</span>
                                    </label>
                                </li>
                                <li class="tp_pw-header__mobile-nav-menu-item">
                                    <ul>
                                        <li><h4 class="tp_pw-header__mega-menu-header">Tetra Laval</h4></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                    </ul>
                                </li>
                                <li class="tp_pw-header__mobile-nav-menu-item">
                                    <ul>
                                        <li><h4 class="tp_pw-header__mega-menu-header">Tetra Laval</h4></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                    </ul>
                                </li>
                                <li class="tp_pw-header__mobile-nav-menu-item">
                                    <ul>
                                        <li><h4 class="tp_pw-header__mega-menu-header">Tetra Laval</h4></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                    </ul>
                                </li>
                                <li class="tp_pw-header__mobile-nav-menu-item">
                                    <ul>
                                        <li><h4 class="tp_pw-header__mega-menu-header">Tetra Laval</h4></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                        <li><a href="" class="tp_pw-header__link">Level 1, Menu item B</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                        <li class="tp_pw-header__mobile-nav-menu-item">
                            <a href="" class="tp_pw-header__link">Menu item B</a>
                        </li>
                        <li class="tp_pw-header__mobile-nav-menu-item">
                            <a href="" class="tp_pw-header__link">Menu item C</a>
                        </li>
                        <li class="tp_pw-header__mobile-nav-menu-item">
                            <a href="" class="tp_pw-header__link">Menu item D</a>
                        </li>
                    </ul>
                </div>
                <a class="js-tp_pw-header__nav-logo header-analytics" href="Go_to_main_page">
                    <img class="tp_pw-header__nav-logo" src="https://banner2.cleanpng.com/20180525/he/kisspng-tetra-pak-tetra-laval-packaging-and-labeling-sidel-tetra-pak-5b0889cd26fad9.8094498915272862211597.jpg" alt="TetraPak Logo" title="MainLogo">
                </a>
                <ul class="tp_pw-header__nav">
                    <li class="tp_pw-header__nav-item tp_pw-header__mega-menu">
                        <a href="">Annual Reports</a>
                        <div class="tp_pw-header__mega-menu-content">
                            <div class="tp_pw-header__mega-menu-content__wrapper">
                                <div class="row">
                                    <div class="col-3">
                                        <h4 class="tp_pw-header__mega-menu-header">Tetra Laval</h4>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval in brief</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Comments by the chairman of the boiard</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval group broad</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Sidel facts</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">DeLaval facts</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval world treds</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval International</a>
                                    </div>
                                    <div class="col-3">
                                        <h4 class="tp_pw-header__mega-menu-header">Tetra Pak</h4>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval in brief</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Comments by the chairman of the boiard</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval group broad</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Sidel facts</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">DeLaval facts</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval world treds</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval International</a>
                                    </div>
                                    <div class="col-3">
                                        <h4 class="tp_pw-header__mega-menu-header">Sidel</h4>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval in brief</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Comments by the chairman of the boiard</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval group broad</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Sidel facts</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">DeLaval facts</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval world treds</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval International</a>
                                    </div>
                                    <div class="col-3">
                                        <h4 class="tp_pw-header__mega-menu-header">Delaval</h4>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval in brief</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Comments by the chairman of the boiard</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval group broad</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Sidel facts</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">DeLaval facts</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval world treds</a>
                                        <a href="" class="tp_pw-header__mega-menu-link">Tetra Laval International</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li class="tp_pw-header__nav-item">
                        <a href="">Facts</a>
                    </li>
                    <li class="tp_pw-header__nav-item">
                        <a href="">Sustainability</a>
                    </li>
                    <li class="tp_pw-header__nav-item">
                        <a href="">Carrer</a>
                    </li>
                    <li class="tp_pw-header__nav-item">
                        <a href="">News</a>
                    </li>
                    <li class="tp_pw-header__nav-item">
                        <a href="">Contact</a>
                    </li>
                </ul>
                <div class="tp_pw-header__nav-options">
                    <button class="js-tp_pw-header__nav-options-search header-analytics" aria-label="Find on page" type="button" title="Find on page">
                        <i class="icon-Search_blue_pw"></i>
                    </button>
                    <a href="" class="js-tp_pw-header__nav-options-envelope header-analytics" aria-label="Contact us" type="button" title="Contact us">
                        <i class="icon-Envelope_Outlined_Blue_pw d-none d-lg-block"></i>
                    </a>
                </div>
            </nav>
        </div>
        <div class="pw-header-search-bar js-pw-header-search-bar" data-module="Searchbar">
            <div auto-locator="searchBoxContainerPaddingMargin" class="d-flex align-items-center tp-container pw-header-search-bar__container">
                <div auto-locator="searchBoxPaddingTopAndBottom" class="row no-gutters">
                    <div class="col-lg-8 input-box">
                        <label for="searchLabel" class="search-label">What are you looking for?</label>
                        <input id="searchLabel" auto-locator="searchBoxDescriptionText-searchBoxDescriptionTextFontStyle" type="text" class="search-bar-input js-search-bar-input" placeholder="What are you looking for?">
                        <div auto-locator="crossLeftPadding" class="icon-wrapper">
                            <button class="search-bar-close" aria-label="close search box" title="close search box"><i class="icon icon-Close_pw"></i></button>
                            <div auto-locator="VerticalLineMargin" class="vl"></div>
                            <button data-search-url="" class="search-icon" aria-label="search icon" title="search icon"><span><i class="icon icon-Search_pw"></i></span></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
      </header>
    `;
    document.body.insertAdjacentHTML('afterbegin', componentTemplate);

    this.subject = new Header({
      el: document.querySelector('header[data-module="Header"]')
    });

    sinon.spy(this.subject, 'init');
    sinon.spy(this.subject, 'bindEvents');
    this.subject.init();
  });

  it('should initialize component and bind events', function () {
    expect(this.subject.init.called).to.be.true;
    expect(this.subject.bindEvents.called).to.be.true;
    expect(this.subject).to.be.instanceOf(Header);
  });

  it('should set aria-label and title for search button', function () {
    this.subject.setSearchBtnAttributes('test');

    expect(this.subject.cache.$searchBtn.attr('aria-label')).to.eql('test');
    expect(this.subject.cache.$searchBtn.attr('title')).to.eql('test');
  });

  describe('events', function () {
    describe('on search button click', function () {
      it('should show searchbar and run track analytics', function () {
        sinon.spy(this.subject, 'isSearchBarShown');
        sinon.spy(this.subject, 'searchBtnTrackAnalytics');

        this.subject.cache.$searchBtn.trigger('click');

        expect(this.subject.isSearchBarShown.called).to.be.true;
        expect(this.subject.searchBtnTrackAnalytics.called).to.be.true;
        expect(this.subject.cache.$searchBar.hasClass('show')).to.be.true;
      });
    });
    describe('on search bar close button click', function () {
      it('should close searchbar', function () {
        this.subject.cache.$searchBarCloseIcon.trigger('click');

        expect(this.subject.cache.$overlay.hasClass('show')).to.be.false;
      });
    });
    describe('on megamenu link mouseover', function () {
      it('should show content of megamenu', function () {
        this.subject.cache.$megaMenu.trigger('mouseover');

        expect(this.subject.cache.$overlay.hasClass('d-none')).to.be.false;
      });
    });
    describe('on header logo click', function () {
      it('should run track analytics', function (done) {
        sinon.spy(this.subject, 'logoTrackAnalytics');

        this.subject.cache.$headerLogo.trigger('click');
        done();
        expect(this.subject.logoTrackAnalytics.called).to.be.true;
      });
    });
    describe('on contact button click', function () {
      it('should run track analytics', function (done) {
        sinon.spy(this.subject, 'contactBtnTrackAnalytics');

        this.subject.cache.$contactBtn.trigger('click');
        done();
        expect(this.subject.contactBtnTrackAnalytics.called).to.be.true;
      });
    });
    describe('on first level navigation link click', function () {
      it('should run track analytics', function (done) {
        sinon.spy(this.subject, 'firstLevelNavigationTrackAnalytics');

        this.subject.cache.$firstLevelNavigation.trigger('click');
        done();

        expect(this.subject.firstLevelNavigationTrackAnalytics.called).to.be.true;
      });
    });
    describe('on second level navigation link click', function () {
      it('should run track analytics', function (done) {
        sinon.spy(this.subject, 'secondLevelNavigationTrackAnalytics');

        this.subject.cache.$secondLevelNavigation.trigger('click');
        done();

        expect(this.subject.secondLevelNavigationTrackAnalytics.called).to.be.true;
      });
    });
  });

  after(function() {
    document.body.removeChild(document.querySelector('header[data-module="Header"]'));
  });
});
