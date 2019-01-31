From openjdk:8
copy ./src/main/resources/build/application-1.0.jar suchmaschinen-1.0.jar
CMD ["java","-jar","suchmaschinen-1.0.jar"]