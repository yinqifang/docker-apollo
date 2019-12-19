from mysql:5.7
env MYSQL_ROOT_PASSWORD=mushroom

# add initilization script
copy V1.0.0__initialization.sql /docker-entrypoint-initdb.d
copy z_update.sql /docker-entrypoint-initdb.d

expose 3306

