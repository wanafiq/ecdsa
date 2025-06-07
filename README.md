<a id="readme-top"></a>

<br />

## About The Project

A sample spring boot project for signing jwt token with ES256.

### Built With

- [![Java][Java21]][Java-url]
- [![Springboot][Springboot]][Springboot-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

### Installation

1. Clone the repo
   ```sh
   git https://github.com/wanafiq/ecdsa
   ```
2. Configure JWT key pairs in `application-dev.yaml`
   ```yaml
   application:
      jwt:
        private-key: |
          -----BEGIN PRIVATE KEY-----
          
          -----END PRIVATE KEY-----
        public-key: |
          -----BEGIN PUBLIC KEY-----

          -----END PUBLIC KEY-----
   ```
3. You can use the provided `generate-key.sh` script

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## H2 Database
To access h2 db console, navigate to `http://localhost:8080/h2`

Database Confgs:
```yaml
 url: jdbc:h2:mem:ecdsa;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
 username: root
 password: root
```

## Usage

1. Start the application
   ```sh
   mvn
   ```

2. Build the application
   ```sh
   mvn clean install
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[Springboot]: https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white
[Springboot-url]: https://spring.io/projects/spring-boot
[Java21]: https://img.shields.io/badge/Java-21%2B-orange
[Java-url]: https://www.oracle.com/java/technologies/downloads/?er=221886
