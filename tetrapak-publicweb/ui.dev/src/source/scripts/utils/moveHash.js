import $ from 'jquery';

const goToSection = function (id) {
  $('html, body').animate({
    scrollTop: $(id).offset().top
  }, 0);
};

export default () => {
  const hash = window.location.hash;
  if (hash) {
    goToSection(hash);
  }
};