import path from 'path';
import {internalIpV6, internalIpV4} from 'internal-ip';
console.log('hello droidjs');
console.log('版本号:\n',process.version)
console.log('当前路径', path.resolve("./"))
import express from 'express';
const app = express();
const port = 3000;

app.get('/', (req, res) => res.send('Hello Javascript World!'));
app.get('/health', (req, res) => res.json({'res': 'ok'}));
app.get('/javaresponse', (req, res) => res.send(someJavaMethod()));
console.log(await internalIpV6());
//=> 'fe80::1'
let ipv4 = await internalIpV4();
console.log(ipv4);
app.listen(port, () => console.log(`Example app listening on http://${ipv4}:${port}!`));
