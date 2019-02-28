use(function() {
 var data = {
    "logoUrl": "/content/dam/tp_images/logo.jpg",
    "logoTextI18n": "cuhu.logotext.text",
    "getUserInfoI18n": "Charlie Svensson",
    "logoDisplay": true,
    "navDisplay": true,
    "rightNavDisplay": true,
    "mLogoLink": "https://www.google.com/",
    "dLogoLink": "https://www.publicissapient.com/",
    "headerNavLinks": [
        {
            "href": "https://www.google.com/",
            "name": "Solutions",
            "targetNew": false,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "https://www.google.com/",
            "name": "Innovation",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "https://www.google.com/",
            "name": "Sustainability",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "https://www.google.com/",
            "name": "Careers",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "https://www.google.com/",
            "name": "About us",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        }
    ]
};
data.requestURI = request.requestURI;
data.nodePathWithSelector = resource.path + '.ux-preview.html';
data.pagePathWithSelector = resource.path + '.ux-preview.html';
 return data;

});