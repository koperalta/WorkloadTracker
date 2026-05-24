# Project Database Setup

All required .jar files can be found here: [JAR Files](https://drive.google.com/drive/folders/1LAMD9kSVpjJ_diRoZkC3Gy_0pQOTqU2J?usp=sharing)

### MySQL DBMS (Core Logic)
* **JDBC Link:** [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
1. **Connect:** Right-click 'Databases' > 'New Connection' > Select MySQL > Enter credentials.
2. **Create:** Right-click connection > 'Create Database' > Name it `workload_db`.
3. **Setup:** Right-click `workload_db` > 'Execute Command' > Paste and run `mysql.md`.

### PostgreSQL DBMS (Audit Ledger)
* **JDBC Link:** [PostgreSQL JDBC Driver (pgJDBC)](https://jdbc.postgresql.org/download/)
1. **Driver:** Download pgJDBC or use the provided GDrive link.
2. **Create:** Right-click 'Databases' > 'Execute Command' > Run `CREATE DATABASE workload_audit_db;`.
3. **Connect:** Right-click 'Databases' > 'New Connection' > Select PostgreSQL > Set Database to `workload_audit_db`.
4. **Setup:** Right-click new `workload_audit_db` node > 'Execute Command' > Paste and run `postgresql.md`.
* *Note: PostgreSQL does not support `USE` commands; you must execute scripts in the dedicated connection node.*

### Derby DBMS (Identity Provider)
1. **Start:** Right-click 'Java DB' in Services > 'Start Server'.
2. **Create:** Right-click 'Java DB' > 'Create Database' > Name `identity_db` (User/Pass: `app`/`app`).
3. **Setup:** Right-click the new `identity_db` connection > 'Execute Command' > Paste and run `derby.md`.