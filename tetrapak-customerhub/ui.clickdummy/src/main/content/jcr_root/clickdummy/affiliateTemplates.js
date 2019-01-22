exports.templates = {
  '.content.xml': [
    '<?xml version="1.0" encoding="UTF-8"?>',
    '<jcr:root xmlns:cq="http://www.day.com/jcr/cq/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"',
    'jcr:primaryType="cq:ClientLibraryFolder"',
    'categories="[{category}]"/>'
  ],
  'css.txt': [
    'css/{filename}.css'
  ],
  'js.txt': [
    'js/{filename}.js'
  ],
  'sassImport': [
    '@import "../../{component}";'
  ],
  'globalSassImport': [
    '@import "{importPath}";'
  ]
}
