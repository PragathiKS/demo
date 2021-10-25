import $ from 'jquery';
import { getLinkClickAnalytics } from 'tpPublic/scripts/common/common';
import { trackAnalytics } from 'tpPublic/scripts/utils/analytics';

class Header {
  constructor({ el }) {
    this.root = $(el);
  }
  cache = {};

  bindEvents () {
    this.cache.$searchBtn.on('click', (e) => {
      if (!this.isSearchBarShown()) {
        this.cache.$searchBar.addClass('show');
        this.searchBtnTrackAnalytics(e);
      }
      if (this.isMobileMode()) {
        const searchIcon = this.cache.$searchBtn.find('.icon-Search_blue_pw');


        if (this.isSearchBarShown()) {
          if (this.cache.$searchBtn.find('.icon-Close_pw')[0]) {
            this.cache.$searchBar.removeClass('show');
            this.cache.$searchBtn.find('i').removeClass('icon-Close_pw');
            this.cache.$searchBtn.find('i').addClass('icon-Search_blue_pw');

            this.setSearchBtnAttributes('Find on page');
          } else {
            searchIcon.removeClass('icon-Search_blue_pw');
            searchIcon.addClass('icon-Close_pw');

            this.setSearchBtnAttributes('Close search');
          }

        } else {
          searchIcon.removeClass('icon-Close_pw');
          searchIcon.addClass('icon-Search_blue_pw');

          this.setSearchBtnAttributes('Find on page');
        }
      }
    });
    this.cache.$searchBarCloseIcon.on('click', () => {
      this.cache.$searchBar.removeClass('show');
    });
    this.cache.$headerLogo.on('click', this.logoTrackAnalytics);
    this.cache.$contactBtn.on('click', this.contactBtnTrackAnalytics);
    this.cache.$firstLevelNavigation.on('click', this.firstLevelNavigationTrackAnalytics);
    this.cache.$secondLevelNavigation.on('click', this.secondLevelNavigationTrackAnalytics);
    this.cache.$megaMenu.on('mouseover', () => {
      this.cache.$overlay.removeClass('d-none');
    });
  }

  setSearchBtnAttributes = (textValue) => {
    this.cache.$searchBtn.attr('aria-label', textValue);
    this.cache.$searchBtn.attr('title', textValue);
  }

  isSearchBarShown = () => this.cache.$searchBar.hasClass('show')

  isMobileMode = () => {
    const outerWidth = $(window).outerWidth();
    return outerWidth < 1025;
  }

  logoTrackAnalytics = e => {
    e.preventDefault();
    getLinkClickAnalytics(e, '', 'Header', this.cache.linkClassSelector, true, { linkParentTitle: '' });
  }

  searchBtnTrackAnalytics = e => {
    e.preventDefault();
    getLinkClickAnalytics(e, '', 'Search', this.cache.linkClassSelector, false, { linkParentTitle: '' });
  }

  contactBtnTrackAnalytics = e => {
    e.preventDefault();
    getLinkClickAnalytics(e, '', 'Header', this.cache.linkClassSelector, true, { linkParentTitle: '' });
  }

  firstLevelNavigationTrackAnalytics = e => {
    const $target = $(e.target);
    const linkType = $target.attr('target') === '_blank' ? 'external' : 'internal';
    const linkName = $target.text();

    trackAnalytics({
      navigationLinkName: linkName,
      navigationSection: 'Header Navigation'
    }, 'navigation', 'navigationClick', undefined, false, {
      eventType: 'navigationClick',
      event: 'Header'
    }, {
      linkType, linkName,
      linkSection: 'hyperlink click',
      linkParentTitle: ''
    });

    if (!$target.closest('.tp_pw-header__mega-menu').length) {
      this.sendRedirect(e, $target, linkType);
    }
  }

  secondLevelNavigationTrackAnalytics = e => {
    e.preventDefault();

    const $target = $(e.target);
    const $navItem = $target.closest('.tp_pw-header__nav-item').find('> a');
    const $parentHeader = $target.parent().find('> .tp_pw-header__mega-menu-header');
    const linkType = $target.attr('target') === '_blank' ? 'external' : 'internal';
    const linkName = $target.text();

    trackAnalytics({
      navigationLinkName: `${$navItem.text()}:${$parentHeader.text()}:${linkName}`,
      navigationSection: ($parentHeader.length ? $parentHeader : $navItem).text()
    }, 'navigation', 'navigationClick', undefined, false, {
      eventType: 'navigationClick',
      event: 'Navigation'
    }, {
      linkType, linkName,
      linkSection: 'hyperlink click',
      linkParentTitle: ''
    });
    this.sendRedirect(e, $target, linkType);
  }

  sendRedirect(e, $obj, linkType) {
    setTimeout(function() {
      if (linkType === 'internal') {
        if (e.metaKey || e.ctrlKey || e.keyCode === 91 || e.keyCode === 224) {
          window.open($obj.attr('href'), '_blank');
        } else {
          window.open($obj.attr('href'),'_self');
        }
      } else {
        window.open($obj.attr('href'), $obj.attr('target'));
      }
    }, 500);
  }

  init() {
    this.cache.linkClassSelector = '.header-analytics';
    this.cache.$headerLogo = this.root.find(`.js-tp_pw-header__nav-logo`);
    this.cache.$searchBtn = this.root.find(`.js-tp_pw-header__nav-options-search`);
    this.cache.$searchBar = this.root.find('.js-pw-header-search-bar');
    this.cache.$searchBarCloseIcon = this.root.find('.search-bar-close');
    this.cache.$contactBtn = this.root.find(`.js-tp_pw-header__nav-options-envelope`);
    this.cache.$firstLevelNavigation = this.root.find('.tp_pw-header__nav-item');
    this.cache.$secondLevelNavigation = this.root.find('.tp_pw-header__mega-menu-link');
    this.cache.$megaMenu = this.root.find('.tp_pw-header__mega-menu');
    this.cache.$overlay = this.root.find('.pw-overlay');

    this.bindEvents();
  }
}

export default Header;
