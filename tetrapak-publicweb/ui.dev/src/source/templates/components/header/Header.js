/* eslint-disable no-console */
/* eslint-disable no-unused-vars */

import $ from 'jquery';
import { trackAnalytics } from '../../../scripts/utils/analytics';
import { render } from '../../../scripts/utils/render';
import { dynMedia } from '../../../scripts/utils/dynamicMedia';
import { updateQueryString } from '../../../scripts/common/common';

class Header {
  constructor({ el }) {
    this.root = $(el);
    this.toggleFlag = false;
    this.toggleButtonId = '#toggle-button';
  }

  cache = {};

  initCache() {
    this.cache.$mobileMenu = this.root.find('.js-tp-pw-mobile-navigation');
    this.cache.$hamburgerToggle = this.root.find('.js-tp-pw-header__hamburger');
    this.cache.$headerLogoPlaceholder = this.root.find('.js-tp-pw-header-logo-digital-data');
    this.cache.$hoverMenuLink = this.root.find('.js-hover-menu-link');
    this.cache.$clickMenuLink = this.root.find('.js-click-menu-link');
    this.cache.$headerMobile = this.root.find('.tp-pw-header__container');
    this.cache.$megaMenuDesktop = this.root.find('.tp-pw-header__container .pw-megamenu');
    this.cache.$megaMenuMobile = this.root.find('.pw-megamenu');
    this.cache.$parentNavElement = this.root.find('.tp-pw-header__main-navigation.col-6');
    this.cache.$menuCloseSol = this.root.find('.js-close-menu-solution');
    this.cache.$bottomTeaserH = this.root.find('.js-bottom-teaser-list');
    this.cache.$headerItem = this.root.find('.js-main-menu-link-hover');
    this.cache.$overlay = $('.js-pw-overlay');
    this.cache.$body = $('body');

  }

  bindEvents() {
    const { $hamburgerToggle, $headerLogoPlaceholder, $headerItem,$parentNavElement,$body} = this.cache;
    $hamburgerToggle.on('click', this.openMobileMenuBoxToggle);
    $headerLogoPlaceholder.on('click', this.trackAnalytics);
    $(window).on('resize', this.hideMobileMenuOnResize);
    this.cache.$hoverMenuLink.on('mouseover', this.handleMouseOver);
    this.cache.$hoverMenuLink.on('mouseout', this.handleMouseOut);
    this.cache.$headerItem.on('mouseover', this.handleHeaderItemMouseOver);
    this.cache.$headerItem.on('mouseout', this.handleHeaderItemMouseOut);
    this.cache.$clickMenuLink.on('click', this.handleMenuClick);
    this.cache.$menuCloseSol.on('click', this.handleCloseSolEvent);
    $headerItem.on('click', this.trackNavigationAnalytics);
    $('.js-tp-pw-header-item').on('click', this.handleMainNavClick);
    // $parentNavElement.addClass('pw-position-static');
    // $body.addClass('pw-position-relative');
    this.getActiveHeaderData();
  }

  getActiveHeaderData = () => {
    const getUrlParameter = function getUrlParameter(sParam) {
      var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

      for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
          return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
      }
    };
    const activeMainMenu = getUrlParameter('header');
    const activeL2 = getUrlParameter('l2');
    const activeL3 = getUrlParameter('l3');
    this.getStickyHeaderData({activeMainMenu,activeL2,activeL3});
  }

  getStickyHeaderData = (data) => {
    // const header = $('.js-hidden').data('header-list');
    const headerArray = [
      {
        'linkText': 'Solutions',
        'linkPath': '/content/tetrapak/public-web/global/en/solutions'
      },
      {
        'linkText': 'Insights',
        'linkPath': '/content/tetrapak/public-web/global/en/insights',
        'navigationConfigurationModel': {
          'sectionHomePageTitle': 'Insight overview',
          'sectionHomePagePath': '',
          'sectionRaws': '6',
          'sectionMenuMap': [
            {
              'linkText': 'Insight Processing',
              'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications.html',
              'external': false,
              'subSectionMenu': {
                'subSections': [
                  {
                    'linkText': 'Processing',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/diary.html',
                    'external': false
                  },
                  {
                    'linkText': 'Servicing',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/beverages.html',
                    'external': false
                  },
                  {
                    'linkText': 'Packages',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/diary.html',
                    'external': true
                  },
                  {
                    'linkText': 'Beverages',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/beverages.html',
                    'external': false
                  },
                  {
                    'linkText': 'Dairy',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/diary.html',
                    'external': true
                  },
                  {
                    'linkText': 'Beverages',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/beverages.html',
                    'external': false
                  }
                ]
              }
            },
            {
              'linkText': 'Insight equipment',
              'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications.html',
              'external': false
            },
            {
              'linkText': 'Insight external',
              'linkPath': 'https://www.google.com',
              'external': true
            }
          ]
        }
      },
      {
        'linkText': 'Sustainability',
        'linkPath': '/content/tetrapak/public-web/global/en/insights',
        'navigationConfigurationModel': {
          'sectionHomePageTitle': 'Sustainability overview',
          'sectionHomePagePath': '',
          'sectionRaws': '6',
          'sectionMenuMap': [
            {
              'linkText': 'Sustainability Processing',
              'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications.html',
              'external': false,
              'subSectionMenu': {
                'subSections': [
                  {
                    'linkText': 'Sustainability Pro',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/diary.html',
                    'external': false
                  },
                  {
                    'linkText': 'Sustainability Ser',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/beverages.html',
                    'external': false
                  },
                  {
                    'linkText': 'Sustainability Pack',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/diary.html',
                    'external': true
                  },
                  {
                    'linkText': 'Beverages',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/beverages.html',
                    'external': false
                  },
                  {
                    'linkText': 'Dairy',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/diary.html',
                    'external': true
                  },
                  {
                    'linkText': 'Beverages',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/beverages.html',
                    'external': false
                  }
                ]
              }
            },
            {
              'linkText': 'Sustainability equipment',
              'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications.html',
              'external': false
            },
            {
              'linkText': 'Sustainability external',
              'linkPath': 'https://www.google.com',
              'external': true
            }
          ]
        }
      },
      {
        'linkText': 'About Tetra pak',
        'linkPath': '/content/tetrapak/public-web/global/en/insights',
        'navigationConfigurationModel': {
          'sectionHomePageTitle': 'About Tetra pak overview',
          'sectionHomePagePath': '',
          'sectionRaws': '6',
          'sectionMenuMap': [
            {
              'linkText': 'About Tetra pak Processing',
              'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications.html',
              'external': false,
              'subSectionMenu': {
                'subSections': [
                  {
                    'linkText': 'About Pro',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/diary.html',
                    'external': false
                  },
                  {
                    'linkText': 'About Ser',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/beverages.html',
                    'external': false
                  },
                  {
                    'linkText': 'Packages',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/diary.html',
                    'external': true
                  },
                  {
                    'linkText': 'Beverages',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/beverages.html',
                    'external': false
                  },
                  {
                    'linkText': 'Dairy',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/diary.html',
                    'external': true
                  },
                  {
                    'linkText': 'Beverages',
                    'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications/beverages.html',
                    'external': false
                  }
                ]
              }
            },
            {
              'linkText': 'About Tetra pak equipment',
              'linkPath': '/content/tetrapak/public-web/lang-masters/en/solutions/processing/applications.html',
              'external': false
            },
            {
              'linkText': 'About Tetra pak external',
              'linkPath': 'https://www.google.com',
              'external': true
            }
          ]
        }
      }
    ];
    const activeMainHeader = headerArray.filter((header) => header.linkText === data.activeMainMenu);
    return activeMainHeader[0] && this.getActiveMenu(activeMainHeader[0],data);
  }

  getActiveMenu = (header,data) => {
    header['active'] = true;
    if(data.activeL2){
      header['navigationConfigurationModel']['sectionMenuMap'].forEach((sectionItem) => {
        if(sectionItem.linkText === data.activeL2){
          sectionItem['active'] = true;
        }
      });
    }
    if(data.activeL3){
      for(const i in header){
        if(i === 'navigationConfigurationModel'){
          for(const j in header[i]){
            if( j === 'sectionMenuMap'){
              header[i][j].forEach((sectionMenuItem) => {
                if(sectionMenuItem.active === true){
                  if(sectionMenuItem.subSectionMenu.pseudoCategoriesSection){
                    sectionMenuItem.subSectionMenu.pseudoCategoriesSection.forEach((pseudoCategory) => {
                      pseudoCategory.subsections.forEach((subSection) => {
                        if(subSection.linkText === data.activeL3){
                          subSection['active'] = true;
                        }
                      });
                    });
                  } else {
                    sectionMenuItem.subSectionMenu.subSections.forEach((subCategory) => {
                      if(subCategory.linkText === data.activeL3){
                        subCategory['active'] = true;
                      }
                    });
                  }
                }
              });
            }
          }
        }
      }
    }
    return header;
  }

  handleMouseOver = () => {
    const { $megaMenuDesktop, $parentNavElement, $overlay, $body } = this.cache;
    $parentNavElement.addClass('pw-position-static');
    $megaMenuDesktop.addClass('d-block').attr('aria-hidden','false').attr('aria-expanded','true');
    $body.addClass('pw-position-relative');
    $overlay.removeClass('d-none');
    dynMedia.processImages();
  }

  handleHeaderItemMouseOver = (e) => {
    const {$parentNavElement, $body } = this.cache;
    $parentNavElement.addClass('pw-position-static');
    $body.addClass('pw-position-relative');
    const $target = $(e.target);
    const $this = $target.closest('.js-main-menu-link-hover');
    $this.children('.pw-navigation').addClass('show').attr('aria-hidden','false').attr('aria-expanded','true');
  }

  handleHeaderItemMouseOut = (e) => {
    const {$parentNavElement, $body } = this.cache;
    const $target = $(e.target);
    const $this = $target.closest('.js-main-menu-link-hover');
    $parentNavElement.removeClass('pw-position-static');
    $body.removeClass('pw-position-relative');
    $this.children('.pw-navigation').removeClass('show').attr('aria-hidden', 'true').attr('aria-expanded','false');
  }

  handleMouseOut = () => {
    const { $megaMenuDesktop,$parentNavElement, $overlay,$body } = this.cache;
    $megaMenuDesktop.removeClass('d-block').attr('aria-hidden', 'true').attr('aria-expanded','false');
    $parentNavElement.removeClass('pw-position-static');
    $body.removeClass('pw-position-relative');
    $overlay.addClass('d-none');
  }

  handleMenuClick = () => {
    const { $megaMenuMobile } = this.cache;
    $megaMenuMobile.removeClass('is-close');
    $megaMenuMobile.addClass('is-open');
    // this.cache.$headerMobile.addClass('d-none');
  }

  handleCloseSolEvent = () => {
    const { $megaMenuMobile } = this.cache;
    $megaMenuMobile.removeClass('is-open');
    $megaMenuMobile.addClass('is-close');
    // this.cache.$headerMobile.removeClass('d-none');
  }

  hideMobileMenuOnResize = () => {
    this.cache.$mobileMenu.fadeOut(10);
    this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close');
    this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger_pw');
    this.toggleFlag = false;
  }

  openMobileMenuBoxToggle = () => {
    if(!this.toggleFlag){
      this.cache.$mobileMenu.fadeIn(300);
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Burger_pw');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Close');
      this.toggleFlag = true;
      $('body').css('overflow','hidden');
    }else {
      this.cache.$mobileMenu.fadeOut(300);
      this.cache.$hamburgerToggle.children(this.toggleButtonId).removeClass('icon-Close');
      this.cache.$hamburgerToggle.children(this.toggleButtonId).addClass('icon-Burger_pw');
      this.toggleFlag = false;

      //hide other navigation on close
      const { $megaMenuMobile, $bottomTeaserH } = this.cache;
      $bottomTeaserH.removeClass('active').addClass('hide');
      $megaMenuMobile.removeClass('is-open');
      $megaMenuMobile.addClass('is-close');
      $('body').css('overflow','auto');
    }
  }

  handleMainNavClick =(e) => {
    event.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-item');
    const linkName = $this.data('link-name');
    const updatedUrl = updateQueryString($this.attr('href'),'header',linkName);
    if(updatedUrl){
      window.open(updatedUrl, '_self');
    }
  }

  trackNavigationAnalytics = (e) => {
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-item');
    const navigationLinkName = $this.data('link-name');

    const trackingObj = {
      navigationLinkName
    };

    const eventObj = {
      eventType: 'navigation click',
      event: 'Navigation'
    };
    trackAnalytics(
      trackingObj,
      'navigation',
      'navigationClick',
      undefined,
      false,
      eventObj
    );
  }

  trackAnalytics = (e) => {
    e.preventDefault();
    const $target = $(e.target);
    const $this = $target.closest('.js-tp-pw-header-logo-digital-data');
    const targetLink = $this.attr('target');
    const url = $this.attr('href');

    const linkName = $this.data('link-name');
    if(linkName==='contact us envelope') {
      const trackingObj = {
        linkSection: 'Find my office',
        linkParentTitle: '',
        linkName: 'Contact Us'
      };
      const eventObj = {
        eventType: 'linkClick',
        event: 'findmyoffice'
      };
      trackAnalytics(trackingObj, 'linkClick', 'linkClick', undefined, false, eventObj);
    }

    if(url && targetLink){
      window.open(url, targetLink);
    }
  }

  init() {
    this.initCache();
    this.bindEvents();
  }
}

export default Header;
