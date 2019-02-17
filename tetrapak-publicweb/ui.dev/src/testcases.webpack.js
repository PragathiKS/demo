// Import test cases
const context = require.context('./source/test-cases', true, /\.spec\.js$/);
context.keys().forEach(context);
