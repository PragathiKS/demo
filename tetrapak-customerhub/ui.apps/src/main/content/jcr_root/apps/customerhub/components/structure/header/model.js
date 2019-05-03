use(function() {
 var data = {
    "logoUrl": "/content/dam/customerhub/english/common/TetraPakLogo.png",
    "logoTextI18n": "cuhu.logotext.text",
    "getUserInfoI18n": "Charlie Svensson",
    "logoDisplay": true,
    "navDisplay": true,
    "rightNavDisplay": true,
    "mLogoLink": "/content/tetrapak/customerhub/en/dashboard.html",
    "dLogoLink": "/content/tetrapak/public-web/global/en/solutions.html",
    "headerNavLinks": [
        {
            "href": "https://www.tetrapak.com/processing",
            "name": "Processing",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "https://www.tetrapak.com/packaging",
            "name": "Packaging",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "https://www.tetrapak.com/services",
            "name": "Services",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "https://www.tetrapak.com/sustainability",
            "name": "Sustainability",
            "targetNew": true,
            "titleI18n": "cuhu.HeaderNavLink.title"
        },
        {
            "href": "https://www.tetrapak.com/about",
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
