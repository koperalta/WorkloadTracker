# 🛠️ Multi-DBMS JNDI Setup Guide (GlassFish)

This guide provides the required steps to configure the local GlassFish environment for our multi-database architecture. 

**Why we use JNDI instead of hardcoding:**
* **Performance:** GlassFish maintains a "Connection Pool" (keeping connections open and ready), preventing the massive overhead of opening/closing network connections on every single query.
* **Security:** Database credentials remain securely within the server configuration, preventing exposure in our source code.
* **Enterprise Standards:** Decoupling database configuration from application logic is standard practice in Java EE and required for production deployments.

---

## Step 1: Install Required JDBC Drivers
Before configuring GlassFish, the server needs the driver `.jar` files for each DBMS.

1. Download the latest drivers:
   * **MySQL:** MySQL Connector/J (`mysql-connector-j-8.x.x.jar`)
   * **PostgreSQL:** PostgreSQL JDBC Driver (`postgresql-42.x.x.jar`)
2. Copy both `.jar` files into your GlassFish domain library folder:
   * **Path:** `[GlassFish_Install_Dir]/glassfish/domains/domain1/lib/`
3. **Restart the GlassFish server** to load the drivers into memory.

---

## Step 2: Configure MySQL (Workload Core)
*This database handles the core business logic (Modules, Tasks, and Submissions).*

### A. Create Connection Pool
1. Open GlassFish Admin Console (`http://localhost:4848`).
2. Navigate to **Resources** -> **JDBC** -> **JDBC Connection Pools** -> **New**.
3. **Pool Name:** `MySQLWorkloadPool`
4. **Resource Type:** `javax.sql.DataSource`
5. **Database Driver Vendor:** `MySQL` -> Click **Next**.
6. In **Additional Properties**, delete existing properties and add:
   * `User`: `root` *(or your local MySQL user)*
   * `Password`: `your_mysql_password`
   * `URL`: `jdbc:mysql://localhost:3306/workload_db`
   * `ServerName`: `localhost`
   * `PortNumber`: `3306`
   * `DatabaseName`: `workload_db`
7. Click **Finish**. Click the pool name, go to the **Ping** tab, and click **Ping** to test.

### B. Create JDBC Resource
1. Navigate to **Resources** -> **JDBC** -> **JDBC Resources** -> **New**.
2. **JNDI Name:** `jdbc/MySQLWorkDB` *(Must match exactly!)*
3. **Pool Name:** `MySQLWorkloadPool`
4. Click **OK**.

---

## Step 3: Configure PostgreSQL (Audit & Time-Series)
*This database handles our immutable audit logs and time-tracking data.*

### A. Create Connection Pool
1. Navigate to **Resources** -> **JDBC** -> **JDBC Connection Pools** -> **New**.
2. **Pool Name:** `PostgresAuditPool`
3. **Resource Type:** `javax.sql.DataSource`
4. **Database Driver Vendor:** `PostgreSQL` -> Click **Next**.
5. In **Additional Properties**, delete existing properties and add:
   * `User`: `postgres` *(or your local Postgres user)*
   * `Password`: `your_postgres_password`
   * `URL`: `jdbc:postgresql://localhost:5432/audit_db`
   * `ServerName`: `localhost`
   * `PortNumber`: `5432`
   * `DatabaseName`: `audit_db`
6. Click **Finish**. Click the pool name, go to the **Ping** tab, and click **Ping** to test.

### B. Create JDBC Resource
1. Navigate to **Resources** -> **JDBC** -> **JDBC Resources** -> **New**.
2. **JNDI Name:** `jdbc/PostgresAuditDB` *(Verify this matches the string in `AuditDAO.java`)*
3. **Pool Name:** `PostgresAuditPool`
4. Click **OK**.

---

## Step 4: Configure Apache Derby (Identity Provider)
*Note: Currently, our `LoginServlet` connects to Derby using `DriverManager` via context parameters in `web.xml`. However, if we migrate it to JNDI for consistency, follow these steps.*

### A. Create Connection Pool
1. Navigate to **Resources** -> **JDBC** -> **JDBC Connection Pools** -> **New**.
2. **Pool Name:** `DerbyIdentityPool`
3. **Resource Type:** `javax.sql.DataSource`
4. **Database Driver Vendor:** `DerbyClient` *(for Network Server)* -> Click **Next**.
5. In **Additional Properties**, add:
   * `User`: `app`
   * `Password`: `app`
   * `ServerName`: `localhost`
   * `PortNumber`: `1527`
   * `DatabaseName`: `identity_db`
6. Click **Finish** and Ping to test.

### B. Create JDBC Resource
1. Navigate to **Resources** -> **JDBC** -> **JDBC Resources** -> **New**.
2. **JNDI Name:** `jdbc/DerbyIdentityDB`
3. **Pool Name:** `DerbyIdentityPool`
4. Click **OK**.

---

## 🚀 Final Step: Apply and Deploy
Once the JNDI resources are mapped:
1. Open NetBeans.
2. Right-click the **WorkloadTracker** project.
3. Select **Clean and Build**.
4. **Run / Deploy**. 

If you see `NameNotFoundException`, double-check that the JNDI Name in Step B of each section matches exactly what the Java code is requesting.
