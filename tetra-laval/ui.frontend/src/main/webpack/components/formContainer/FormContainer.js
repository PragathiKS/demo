import $ from 'jquery';
import 'bootstrap';

class FormContainer {
    cache = {};
    resetForm(isForm) {
        const { $form, $formThankYou, $heading, $headingText } = this.cache;
        if(isForm) {
            $heading.show().text($headingText);
            $form.show();
            $formThankYou.hide();
        }
        if(!isForm) {
            $heading.text('');
            $form.hide();
            $formThankYou.show();
        }
    }
    
    bindEvents() {
        const $this = this;
        const { $contactBtn, $modal, $closeBtn, $form } = this.cache;
        
        $contactBtn.on('click', function() {
            $modal.modal();
            $this.resetForm(true);
        });
        $closeBtn.on('click', function() {
            $modal.modal('hide');
            $this.resetForm();
        });

        $form.submit(function(e) {
            e.preventDefault();
            const form = $(this);
            $.ajax({
                type: form.attr('method'),
                url: form.attr('action'),
                data: form.serialize()
            }).done(function(res) {
                console.log('Hiren Parmar | Data', res);
                form[0].reset();
                $this.resetForm(false);
            }).fail(function(res) {
                console.log('Hiren Parmar | Error Data', res);
            });
        });
    }

    initCache() {
        this.cache.$heading = $('.pw-lang-selector__header__heading');
        this.cache.$headingText = this.cache.$heading.text();
        this.cache.$form = $('.tl-contactForm').find('.cmp-form');
        this.cache.$formThankYou = $('.tl-contactForm__thankyou');
        this.cache.$contactBtn = $('.js-tpatom-btn__tl-contactUs');
        this.cache.$modal = $('.js-tp-contact__modal');
        this.cache.$closeBtn = $('.js-close-btn');
    }
    
    init() {
        this.initCache();
        this.bindEvents();
    }
}
export default FormContainer;