import Footer from './Footer';

describe('Footer', function () {
  before(function () {
    const componentTemplate = `
        <footer class="tp-pw-footer js-tp-pw-footer" data-module="Footer" id="footer">
            <div class="tp-pw-footer__container tp-container">
                <div class="row">
                    <div class="col-lg-5 col-xs-12 text-lg-left">
                        <div class="row">
                            <div class="col-12 tp-pw-footer__logo footer-logo footer-analytics">
                                <a class="tp-pw-footer-data-analytics tp-pw-footer-data-logo" href="" target="">
                                    <img src="" alt="" />
                                </a>
                            </div>
                            <div class="col-12 tp-pw-footer__text">
                                The Tetra Laval Group consists of three industry groups, Tetra Pak, Sidel and DeLaval, all focused on technologies for the efficient production, packaging and distribution of food.
                            </div>
                            <div class="col-12">
                                <ul class="tp-pw-footer__links-list">
                                    <li class="footer-analytics">
                                        <a class="tp-pw-footer__link tp-pw-footer-data-analytics"
                                        href="" target="_blank"
                                        data-link-name="">&#x24B8; Tetra Laval International S.A.</a>
                                    </li>
                                    <li class="footer-analytics">
                                        <a class="tp-pw-footer__link tp-pw-footer-data-analytics"
                                        href="" target="_blank"
                                        data-link-name="">Cookie Policy</a>
                                    </li>
                                    <li class="footer-analytics">
                                        <a class="tp-pw-footer__link tp-pw-footer-data-analytics"
                                        href="" target="_blank"
                                        data-link-name="">Link C</a>
                                    </li>
                                    <li class="footer-analytics">
                                        <a class="tp-pw-footer__link tp-pw-footer-data-analytics"
                                        href="" target="_blank"
                                        data-link-name="">Link D</a>
                                    </li>
                                    <li class="footer-analytics">
                                        <a class="tp-pw-footer__link tp-pw-footer-data-analytics"
                                        href="" target="_blank"
                                        data-link-name="">Link E</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-7 col-xs-12 text-lg-right">
                        <div class="row">
                            <div class="col">
                                <ul class="tp-pw-footer__navigation-list">
                                    <li class="tp-pw-footer__social-media-items footer-analytics">
                                        <a class="tp-pw-footer__social-media-items tp-pw-footer__link tp-pw-footer-data-analytics"
                                        href="" target="_blank"
                                        data-link-name="">
                                            <i class="icon-Linkedin_pw"></i>
                                        </a>
                                    </li>
                                    <li class="tp-pw-footer__social-media-items footer-analytics">
                                        <a class="tp-pw-footer__social-media-items tp-pw-footer__link tp-pw-footer-data-analytics"
                                        href="" target="_blank"
                                        data-link-name="">
                                            <i class="icon-Facebook_pw"></i>
                                        </a>
                                    </li>
                                    <li class="tp-pw-footer__social-media-items footer-analytics">
                                        <a class="tp-pw-footer__social-media-items  tp-pw-footer__link tp-pw-footer-data-analytics"
                                        href="" target="_blank"
                                        data-link-name="">
                                            <i class="icon-instagram_pw"></i>
                                        </a>
                                    </li>
                                    <li class="tp-pw-footer__social-media-items footer-analytics">
                                        <a class="tp-pw-footer__social-media-items tp-pw-footer__link tp-pw-footer-data-analytics"
                                        href="" target="_blank"
                                        data-link-name="">
                                            <i class="icon-Twitter_pw"></i>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
    `;
        document.body.insertAdjacentHTML('afterbegin', componentTemplate);

        this.subject = new Footer({
            el: document.querySelector('#footer')
        });

        sinon.spy(this.subject, 'init');
        sinon.spy(this.subject, 'initCache');
        sinon.spy(this.subject, 'bindEvents');
        sinon.spy(this.subject, 'logoTrackAnalytics');
        sinon.spy(this.subject, 'linksTrackAnalytics');
        sinon.spy(this.subject, 'socialMediasTrackAnalytics');

        this.subject.init();
    });

    it('should initialize component', function () {
        expect(this.subject.init.called).to.be.true;
        expect(this.subject).to.be.instanceOf(Footer);
    })

    it('should intialize cache and bind events', function () {
        expect(this.subject.initCache.called).to.be.true;
        expect(this.subject.bindEvents.called).to.be.true;
    })

    describe('calling events', function () {
        it('should track analitycs, while logo is click', function () {
            this.subject.cache.$footerLogo.trigger('click');

            expect(this.subject.logoTrackAnalytics.called).to.be.true;
        })
        it('should track analitycs, while links are click', function () {
            this.subject.cache.$footerLinks.trigger('click');

            expect(this.subject.linksTrackAnalytics.called).to.be.true;
        })
        it('should track analitycs, while social media links are click', function () {
            this.subject.cache.$footerSocialMedias.trigger('click');

            expect(this.subject.socialMediasTrackAnalytics.called).to.be.true;
        })
    });

    after(() => {
      document.body.removeChild(document.getElementById('footer'));
    });
});
