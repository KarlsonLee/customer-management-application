# Customer Management Application — Spring Boot

## Prerequisites

1. **Java 17 or above** — download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
2. **Maven 3.8 or above** — [Download](https://maven.apache.org/download.cgi) | [Install guide](https://maven.apache.org/install.html)
3. **SQL Server** running locally and reachable on `localhost:1433` — see setup below

## Database Setup

### 1. Install SQL Server

Download SQL Server **Developer Edition** (free, full-featured, for non-production use):
👉 https://www.microsoft.com/en-us/sql-server/sql-server-downloads

During setup:

- Choose **Developer** edition
- Enable **TCP/IP** protocol (via SQL Server Configuration Manager → SQL
  Server Network Configuration → Protocols)
- Restart the SQL Server service for the TCP/IP change to take effect
- Install  **SSMS** (
  Windows) — [Download](https://learn.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms)
- Open SSMS and login (e.g. your device name)
- Run `scripts/create-database.sql` against your SQL Server to create the `TESTDB` database
- In **Security → Logins**, right-click and select **New Login**.
  Enter a login name (e.g. `user`), choose **SQL Server authentication**, and set a password (e.g. `Password`).
- Go to the **User Mapping** page, check the box next to `TESTDB`,
  and assign the `db_owner` role membership for that database.

## API Reference

Base path: `/api/v1/customers`

| # | Method | Endpoint                          | Request Parameter           | Description                                                                                                                 | 
|---|--------|-----------------------------------|-----------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| 1 | POST   | `/api/v1/customers`               |                             | Create a new customer (**INSERT**)                                                                                          |
| 2 | PUT    | `/api/v1/customers/{id}`          |                             | Update an existing customer (**UPDATE**)                                                                                    |
| 3 | GET    | `/api/v1/customers/{id}`          |                             | Fetch a single customer (**GET**)                                                                                           |
| 4 | GET    | `/api/v1/customers `              | page, size, sortBy, sortDir | Paginated list, **10 records per page** (**GET**)                                                                           |
| 5 | DELETE | `/api/v1/customers/{id}`          |                             | Delete an customer                                                                                                          |
| 6 | GET    | `/api/v1/customers/{id}/enriched` |                             | **Nested API call** — fetches the customer, then calls our own internal mock "3rd-party" country API to enrich the response |
| 7 | GET    | `/api/mock/countries/{name}`      |                             | The mock "3rd-party" API itself                                                                                             |  
