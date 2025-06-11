# Evidence Management Test Helper

This is a client library to facilitate common task during functional tests, like:
- IDAM user creation
- S2S authentication
- CCD case creation
- CCD definition import 

It is published to jitpack repository via github actions - see .github/workflows/jitpack_build.yml
Reference in your functional tests in gradle ala   functionalTestImplementation 'com.github.hmcts:em-test-helper:1.18.2'


Usage:

- Include EmTestConfig in your Spring Test

<pre>
@SpringBootTest(classes = {EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@RunWith(SpringRunner.class)
public class MyTestScenario {
</pre>

- Make sure that particular Helpers are initiated by providing URL to the respective services

<pre>
idam:
  api:
    url: 'http://localhost:5000'
  client:
    id: 'webshow'
    secret: 'AAAAAAAAAAAAAAAA'
    redirect_uri: 'http://localhost:8080/oauth2redirect'

s2s:
  api:
    url: 'http://localhost:4502'
    secret: 'AAAAAAAAAAAAAAAA'
    serviceName: 'em_gw'
    ccdGwSecret: 'AAAAAAAAAAAAAAAA'
    ccdGwServiceName: 'ccd_gw'    
    
document_management:
  url: 'http://localhost:4603'    

ccd-def:
  api:
    url: 'http://localhost:4451'

core_case_data:
  api:
    url: 'http://localhost:4452'
</pre>

Then @Autowire the following components:
- IdamHelper
- S2sHelper (there are 2 beans of that type: em_gw helper and ccd_gw helper - the format, main one [@Qualifier("s2sHelper")] and the latter used by ccd helpers [@Qualifier("ccdS2sHelper")])
- DmHelper
- CcdDataHelper
- CcdDefinitionHelper

see functional tests in "aat" source folder for examples

### Prerequisites

- [Java 21](https://www.oracle.com/java)


We use [SemVer](http://semver.org/) for versioning.
For the versions available, see the tags on this repository.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
