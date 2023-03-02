/* eslint-disable */
import $ from 'jquery';
import 'bootstrap';

class FormContainer {
    cache = {};
    initCache() {
        this.cache.$contactBtn = $('.js-tpatom-btn__tl-contactUs');
        this.cache.$modal = $('.js-tp-contact__modal');
        this.cache.$closeBtn = $('.js-close-btn');
    }
    bindEvents() {
        const { $contactBtn, $modal, $closeBtn } = this.cache;
        $contactBtn.on('click', function() {
            $modal.modal();
        });
        $closeBtn.on('click', function() {
            $modal.modal('hide');
        });
    }

    init() {
        this.initCache();
        this.bindEvents();
    }
}
export default FormContainer;