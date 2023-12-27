#!/bin/bash

# MySQL user and password
DB_USER="root"
DB_PASSWORD="walgotech"

# Directory to store the backups
BACKUP_DIR="/usr/local/bin/db"

# Get a list of all databases
databases=$(mysql -u "$DB_USER" -p"$DB_PASSWORD" -e "SHOW DATABASES;" | grep -Ev "(Database|information_schema|performance_schema|sys)")

# Iterate through each database and create a separate backup
for db in $databases; do
  echo "Backing up database: $db"
  mysqldump -u "$DB_USER" -p"$DB_PASSWORD" --databases "$db" > "$BACKUP_DIR/$db.sql"
done

echo "Backup completed."
