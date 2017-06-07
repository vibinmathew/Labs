# Build

## environment variables

Some env variables are required at runtime. Depending on local or cloud deployment different tools are used to set them up.

### Local dev using 'direnv'

When entering the project folder, if 'direnv' is installed it will detect the .envrc file and will execute it.
.envrc contains an 'lpass' command that run some export commands to inject credentials or other config variables.

Example:
```
`lpass show workstation_env --notes`
```
workstation_env file defined as a lastpass tile would contain some exports:
```
export SECURITY_OAUTH2_CLIENT_CLIENT_ID=...
export SECURITY_OAUTH2_CLIENT_CLIENT_SECRET=...
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/pivotal
export FRONTEND_ORIGIN=http://localhost:3000
```
### Cloud deployment using 'User Provided' services

Cloud Foundry can be managed from web interface: console.run.pivotal.io, or from the CLI (cf).

Tip to login to CF CLI without exposing credentials using Lastpass CLI:
```
cf login -a `lpass show pivotal.io --url` -u `lpass show pivotal.io -u` -p `lpass show pivotal.io -p`
```

Environment variables can be provided by specific CF services named User Provided Service.

Example:
* telstra-dev-api
  * OAuth2 client credentials to access Telstra SMS API
* rti-db
  * JDBC template resources to connect to RTI DB
* frontend
  * Gives the origin hostname (frontend hostname) for CORS purpose

Those variables are either:
 * Automatically accessed by Spring Boot
 * Or manually injected in application-cloud.yml (as Spring profile 'cloud' is automatically activated)
```
security:
  oauth2:
    client:
      client-id: ${vcap.services.telstra-dev-api.credentials.clientid}
      client-secret: ${vcap.services.telstra-dev-api.credentials.secret}

frontend:
  origin: ${vcap.services.frontend.credentials.origin}
```

'vcap.services.frontend' is path to the User Provided Service. 
'credentials' is root object that contains all the variables 

All env variables can be view from: datacat-api > Settings > Reveal Env Vars

To create a new User Provided Service (e.g. JDBC template to RTI DB):
```
cf cups rti-db -p "driver,password,url,username"
```

### Telstra internal VM

The SMS API is different in Telstra internal network. The Spring profile 'internal' has to be activated to run on proper Adapters.
```
-Dspring.profiles.active=internal
```

Also, it requires different credentials that are stored in 'internal_env' tile in LastPass.

#### Start/Stop/Restart the application

```
sudo service telstradatacatsrv start
sudo service telstradatacatsrv stop
sudo service telstradatacatsrv restart
```

The script is located at: /etc/init.d/telstradatacatsrv

Logs are located at: /tmp/datacat.log

#### Reverse proxy (NGINX)
nginx config is located at: /etc/nginx/nginx.conf

The important part of the config is:
```
location /api/ {
  proxy_pass http://10.60.106.26:8080;
  proxy_redirect off;
}

location / {
  root /opt/datacat-web;
  try_files $uri /index.html;
}
```

#### Tips

To copy the jar to the VM:
```
scp datacat-api/build/libs/datacat-0.0.1-SNAPSHOT.jar cloud-user@10.60.106.26:/home/cloud-user/
```


## Backend (datacat-api)
```./gradlew build```

## Frontend (datacat-web)
```yarn build```

# Deployment

```cf push```

It picks configuration from local manifest.yml

Check your deployed apps: ```cf apps```
Tail logs: ```cf logs <app>```

# CI/CD

GitLab pipeline is defined in ```.gitlab-ci.yml```

## Radiator
A Gitlab radiator is hosted by Pivotal Labs. The last build can be monitored providing project URL and token:
```
http://gitlab-radiator-sydney.cfapps.io/?gitlab=https://gitlab.com&token=Hf85mXzcoqXM5vY91eWv&projects=data-cat&ref=master
```

# SSL

The application holds a trust store file (trustStore.jks).
As at today, it contains the self-signed non-trusted certificate of nonprod-api.in.telstra.com.au.

In the case the certificate changes, the trust store has to be updated as well.

Use keytool (from JDK) to import a certificate to the trust store.

Example:
```
keytool -import -trustcacerts -alias TelstraTestRootCA -file TelstraTestRootCA.cer -keystore trustStore.jks
```
The store password should be included in a LastPass secure note.

# Logging

In application.yml you can change dependency packages log level.

For an external library:
```
logging.level.org.springframework.web: DEBUG
```

For our own code:
```
logging.level.com.telstra.datacat: DEBUG
```
Ref.: https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html

Or you can also use JVM env variables:
```
-Dlogging.level.com.telstra.datacat=DEBUG
```
