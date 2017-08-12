## ALM REST API Integration
[![Build Status](https://travis-ci.org/okean/alm-rest-api.svg?branch=master)](https://travis-ci.org/okean/alm-rest-api)
[![Coverage Status](https://coveralls.io/repos/github/okean/alm-rest-api/badge.svg?branch=master)](https://coveralls.io/github/okean/alm-rest-api?branch=master)

A simple Java API client to connect to HP ALM using REST service.

### Features
Create, read, update, and delete entities on the ALM HP platform.

### Installing
```
mvn clean install
```

### Examples
Here is a simple example that login, returns a test entity and logout from ALM server.
```java
String almProperties = "host=yourhost.com\nport=8080\ndomain=yourdomain\nproject=yourproject\nusername=yourlogin\npassword=yourpassword""

Client client = new Client(new Config(almProperties));

client.login();

// Get the test set with specified ID
TestSet testSet = client.loadTestSet("1");

client.logout();
```

### REST API Overview
http://alm-help.saas.hpe.com/en/12.50/api_refs/REST_TECH_PREVIEW/ALM_REST_API_TP.html

### Licensing
See [LICENSE](https://github.com/okean/alm-rest-api/blob/master/LICENSE)
