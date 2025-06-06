FROM maven:latest

WORKDIR /restAssured

COPY pom.xml ./

RUN mvn install

COPY . .

CMD ["mvn", "test"]
