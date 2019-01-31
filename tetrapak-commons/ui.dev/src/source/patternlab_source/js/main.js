'use strict';

// jQuery.js,
// Bootstrap.js 
// are included in the './js/vendor/' folder 
// and is concatonated in that order (jQuery,Bootstrap) and output as vendor.min.js file in public './js/' folder

// Testing jQuery
//$('body').css('background','red');

// Customer Hub Organisms
const tp_sideMenu = require('../_patterns/02-organisms_ch/tp_sideMenu/tp_sideMenu');
const tp_notifications = require('../_patterns/02-organisms_ch/tp_notifications/tp_notifications');

// Public Web Organisms
const footer = require('../_patterns/03-organisms_pw/00-global/footer');
const tp_tabListContent = require('../_patterns/03-organisms_pw/tp_pw-tabListContent/tp_pw-tabListContent');

// Example including JS files that lives (for example) in the molecules/organisms folders
const example = require('./test/example');

// Progress Bar (included from the start demo)
const progressBar = require('./test/progress');
