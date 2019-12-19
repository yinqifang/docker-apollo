

Use ApolloPortalDB;

# update eureka address
# ------------------------------------------------------------
UPDATE ServerConfig
SET `Value` = 'dev,pro'
WHERE
        `Key` = 'apollo.portal.envs'
;
