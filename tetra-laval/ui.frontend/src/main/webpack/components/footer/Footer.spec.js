import Footer from './Footer';
import * as commonFunctions from 'tpPublic/scripts/common/common';

describe('Footer', function () {
  beforeAll(function () {
    const htmlElement = `
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
        document.body.insertAdjacentHTML('afterbegin', htmlElement);

        this.subject = new Footer({
            el: document.querySelector('#footer')
        });
    });

    it('should create component', function () {
        expect(this.subject).toBeInstanceOf(Footer);
    })

    it('should intialize cache and bind events', function () {
        spyOn(this.subject, 'initCache');
        spyOn(this.subject, 'bindEvents');

        this.subject.init();

        expect(this.subject.initCache).toHaveBeenCalled();
        expect(this.subject.bindEvents).toHaveBeenCalled();
    });

    describe('calling events', function () {
        xit('should track analitycs, while logo is click', function () {
            this.subject.init();

            this.subject.cache.$footerLogo.trigger('click');
        })
    });

    afterAll(() => {
      document.body.removeChild(document.getElementById('footer'));
    });
});
