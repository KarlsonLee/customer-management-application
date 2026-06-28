# Customer Management Application — Spring Boot

## Prerequisites

1. **Java 17 or above** — download from [https://adoptium.net/)
2. **IntelliJ IDEA** (available at https://www.jetbrains.com/idea/)
3. **SQL Server** running locally and reachable on `localhost:1433` — see setup below

## Database Setup

### 1. Install SQL Server

Download SQL Server **Developer Edition** (free, full-featured, for non-production use): — https://www.microsoft.com/en-us/sql-server/sql-server-downloads

During setup:

- Choose **Developer** edition
- Enable **TCP/IP** protocol (via SQL Server Configuration Manager → SQL
  Server Network Configuration → Protocols)
- Restart the SQL Server service for the TCP/IP change to take effect
- Install  **SSMS** — [Download](https://learn.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms)
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
| 4 | GET    | `/api/v1/customers `              | page, size, sortBy, sortDir | Paginated list, **Defaults to 10 records per page, sorted by id ascending** (**GET**)                                       |
| 5 | DELETE | `/api/v1/customers/{id}`          |                             | Delete an customer                                                                                                          |
| 6 | GET    | `/api/v1/customers/{id}/enriched` |                             | **Nested API call** — fetches the customer, then calls our own internal mock "3rd-party" country API to enrich the response |
| 7 | GET    | `/api/mock/countries/{name}`      |                             | The mock "3rd-party" API - Fetch details for a specific country from the mock dataset                                       |  
| 8 | GET    | `/api/mock/countries`             |                             | The mock "3rd-party" API - List all seeded countries in the mock dataset                                                    |  


### Sample request body (POST)

```json
{
  "firstName": "Alex",
  "lastName": "Lee",
  "email": "alex@example.com",
  "country": "Malaysia",
  "dateOfBirth": "2023-01-15"
}
```

### Sample request body (PUT)

```json
{
  "firstName": "Alex",
  "lastName": "Lee",
  "email": "alex2@example.com",
  "country": "Malaysia"
}
```

### Sample Response body (POST/PUT/GET)

```json
{
  "id": 1,
  "firstName": "Alex",
  "lastName": "Lee",
  "email": "alex2@example.com",
  "country": "malaysia",
  "dateOfBirth": "1990-01-15"
}
```

### Sample Response body (GET All)

```json
{
  "data": [
    {
      "id": 1,
      "firstName": "Alex",
      "lastName": "Lee",
      "email": "elex2@example.com",
      "country": "Malaysia",
      "dateOfBirth": "1990-01-15"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### Sample Response body (GET All with Enriched)

```json
{
     "customer": {
        "id": 1,
        "firstName": "Alex",
        "lastName": "Lee",
        "email": "elex2@example.com",
        "country": "Malaysia",
        "dateOfBirth": "1990-01-15"
    },
    "countryInfo": {
        "countryName": "Malaysia",
        "officialName": "Malaysia",
        "region": "Asia"
    }
}