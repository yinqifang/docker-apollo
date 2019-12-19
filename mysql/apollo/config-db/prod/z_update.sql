

Use ApolloConfigDB;

# update eureka address
# ------------------------------------------------------------
UPDATE ServerConfig
SET `Value` = 'http://apollo-configservice-prd:8080/eureka'
WHERE
        `Key` = 'eureka.service.url'
;
