var http = require('http');
var versions_server = http.createServer( (request, response) => {
  response.end(JSON.stringify(process.versions));
});
console.log("start http server....")
console.log("listen http://0.0.0.0:3000")
versions_server.listen(3000);