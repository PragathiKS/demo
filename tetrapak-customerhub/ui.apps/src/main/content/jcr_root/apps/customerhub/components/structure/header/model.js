use(function() {
 var data = {
    "logoUrl": "/content/dam/customerhub/TetraPakLogo.png",
    "logoTextI18n": "cuhu.logotext.text",
    "getUserInfoI18n": "Charlie Svensson",
    "logoDisplay": true,
    "navDisplay": true,
    "rightNavDisplay": true,
    "mLogoLink": "/content/tetrapak/customerhub/global/dashboard.html",
    "dLogoLink": "/content/tetrapak/customerhub/global/dashboard.html",
    "headerNavLinks": [
        {
            "href": "/content/tetrapak/public-web/global/en/solutions.html",
            "name": "Solutions",
            "targetNew": false,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "/content/tetrapak/public-web/global/en/innovations.html",
            "name": "Innovation",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "/content/tetrapak/public-web/global/en/sustainability.html",
            "name": "Sustainability",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "/content/tetrapak/public-web/global/en/careers.html",
            "name": "Careers",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "/content/tetrapak/public-web/global/en/about-us.html",
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