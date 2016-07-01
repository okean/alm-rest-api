## ALM REST API Integration
[![Build Status](https://travis-ci.org/okean/alm-rest-api.svg?branch=master)](https://travis-ci.org/okean/alm-rest-api)
[![Coverage Status](https://coveralls.io/repos/github/okean/alm-rest-api/badge.svg?branch=master)](https://coveralls.io/github/okean/alm-rest-api?branch=master)
[![Gitter](https://badges.gitter.im/okean/alm-rest-api.svg)](https://gitter.im/okean/alm-rest-api?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

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
// Init rest connector instance to authenticate your session
RestConnector.instance().init("localhost", "8181", "b2b", "rx");

Dao.login("admin", "admin");

// Get the test with specified ID
Test test = Dao.readTest("1");

Dao.logout();
```

### REST API Overview
http://alm-help.saas.hpe.com/en/12.20/api_refs/REST_TECH_PREVIEW/ALM_REST_API_TP.html

### Licensing
See [LICENSE](https://github.com/okean/alm-rest-api/blob/master/LICENSE)
