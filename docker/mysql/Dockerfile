# Use the official MySQL 8.0 image as the base image
FROM mysql:8.0

ENV MYSQL_DATABASE=customer-data-platform
ENV MYSQL_ROOT_PASSWORD=package1107N
ENV MYSQL_USER=book_shop
ENV MYSQL_PASSWORD=package1107N

# Copy the SQL script to initialize the table
COPY init.sql /docker-entrypoint-initdb.d/

# Expose the MySQL port
EXPOSE 3306