
# Book Store Application
This is a book store project developed using Spring Boot. It provides a web application for managing a book store, allowing users to view and search for books, add books to their shopping cart, and complete the purchase.
## Technologies

- Spring boot
- Spring data JPA
- Spring security
- Spring web
- JWT
- MySQL
- H2
- Swagger
- Lombok
- Mockito
- Assertj



## API Reference



### Auth Controller

| Request type | Endpoint           | Role | Description           |
|--------------|--------------------|------|-----------------------|
| POST         | /api/auth/register | ALL  | register a new user   |
| POST         | /api/auth/login    | ALL  | login registered user |

### Book Controller:

| Request type | Endpoint        | Role  | Description       |
|------------- |-----------------|-------|-------------------|
| POST         | /api/books      | ADMIN | create a new book |
| GET          | /api/books      | USER  | get all books     |
| GET          | /api/books/{id} | USER  | get book by id    |
| PUT          | /api/books/{id} | ADMIN | update book by id |
| DELETE       | /api/books/{id} | ADMIN | delete book by id |

### Category Controller:

| Request type | Endpoint                   | Role  | Description               |
|------------- |--------------------------- |-------|---------------------------|
| POST         | /api/categories            | ADMIN | create a new category     |
| GET          | /api/categories            | USER  | get all categories        |
| GET          | /api/categories/{id}       | USER  | get category by id        |
| PUT          | /api/categories/{id}       | ADMIN | update category by id     |
| DELETE       | /api/categories/{id}       | ADMIN | delete category by id     |
| DELETE       | /api/categories/{id}/books | USER  | get all books by category |

### Order Controller:

| Request type | Endpoint                             | Role  | Description                  | 
|------------- |--------------------------------------|-------|------------------------------|
| POST         | /api/orders                          | USER  | create a new order           |
| GET          | /api/orders                          | USER  | get orders history           |
| PATCH        | /api/orders/{id}                     | ADMIN | update order's status        |
| GET          | /api/orders/{orderId}/items          | USER  | get list of order items      |
| GET          | /api/orders/{orderId}/items/{itemId} | USER  | get specific item from order |


### Shopping Cart Controller:

| Request type | Endpoint                          | Role | Description                    |
|------------- |---------------------------------- |------|--------------------------------|
| GET          | /api/cart                         | USER | get shopping cart              |
| POST         | /api/cart                         | USER | add a book to cart             |
| PUT          | /api/cart/cart-items/{cartItemId} | USER | update number of books in cart |
| DELETE       | /api/сart/cart-items/{cartItemId} | USER | delete book from cart          |

## Run Locally

Clone the project

```bash
  git clone https://github.com/viacheslav-torbin/jv-book-store
```

Go to the project directory

```bash
  cd jv-book-store
```

Install dependencies

```bash
  mvn clean package
```

Start the application

```bash
  mvn spring-boot:run
```
## Running Tests

To run tests, run the following command

```bash
  mvn test
```


## 🔗 Links
[![cv](https://img.shields.io/badge/MY_CV-green?style=for-the-badge&logo=read.cv)](https://google.com/)
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/viacheslav-torbin-5394b6294/)

