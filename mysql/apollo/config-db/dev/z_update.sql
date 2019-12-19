

Use ApolloConfigDB;

# update eureka address
# ------------------------------------------------------------
UPDATE ServerConfig
SET `Value` = 'http://apollo-configservice-dev:8080/eureka'
WHERE
        `Key` = 'eureka.service.url'
;
