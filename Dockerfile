FROM maven:3.8.8-openjdk-8

WORKDIR /restAssured

COPY pom.xml ./

RUN mvn install

COPY . .

CMD ["mvn", "test"]
