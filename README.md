# csc326-TP-202-3


*Line Coverage (should be >=70%)*

![Coverage](.github/badges/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](.github/badges/branches.svg)


### Extra Credit

_Any extra credit done must be documented in the README or Wiki so that we can easily see what was done._

- [Optional Element: Additional User Role](https://github.ncsu.edu/engr-csc326-spring2024/csc326-TP-202-3/wiki/%5BUser-Story%5D-Additional-User-Role)
  - **Frontend:** Staff user split between Manager and Barista views after login, each view has respective functionality.
  - **Backend:** There are enumerations for each user type, and the front end will check this usertype.
- [Optional Element: Anonymous Orders](https://github.ncsu.edu/engr-csc326-spring2024/csc326-TP-202-3/wiki/%5BUser-Story%5D-Anonymous-Orders)
  - **Frontend:** Anonymous User will have the same view as customer, but will not have to login. Clicking continue as guest will create an anonymous user.
- [Optional Element: Order History](https://github.ncsu.edu/engr-csc326-spring2024/csc326-TP-202-3/wiki/%5BUser-Story%5D-Order-History)
  -  **Frontend:** Order History designed and placed under Manager view.
- [Optional Element: Privacy Policy](https://github.ncsu.edu/engr-csc326-spring2024/csc326-TP-202-3/wiki/%5BUser-Story%5D-Privacy-Policy)
  -  **Frontend:** Website Implemented.

- Secutriy Audit: User passwords are hashed before being stored in the database as a securtiy measure. Users who should not have access to specific pages cannot access them. Users who are not authenticated do not have access to any pages.

- UI Enhancements: The entire frontend was overhauled to allow for a consistent style and theme across the CoffeeMaker project
